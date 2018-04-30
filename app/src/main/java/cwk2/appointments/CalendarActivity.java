package cwk2.appointments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TimePicker;

import com.github.clans.fab.FloatingActionMenu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.graphics.Color.WHITE;

public class CalendarActivity extends AppCompatActivity {

    private boolean isOpen = false;
    private CalendarView calendarview;
    private Toolbar toolbar;
    private LinearLayout mainLayout;

    private FloatingActionMenu floatingActionMenu;
    private com.github.clans.fab.FloatingActionButton create, edit, move, delete;

    private EditText titleAppointment;
    private EditText detailsAppointment;

    private TimePicker timePicker1;

    // Create function
    String titleUserInput, detailsUserInput, timeUserInput, dateUserInput;

    SQLHelper sqlHelper;

    String getTitle, getDate, getTime = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        //======================================================================
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //======================================================================
        calendarview = (CalendarView)findViewById(R.id.calendarView);
        toolbar.setTitle("Appointments");
        toolbar.setTitleTextColor(WHITE);
        //======================================================================
        floatingActionMenu = (FloatingActionMenu)findViewById(R.id.actionMenu);
        create = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.createActionBut);
        edit = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.editActionbut);
        move = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.moveActionbut);
        delete = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.delete);
        //======================================================================
//        Button enterCreate = (Button)findViewById(R.id.enterBut);

        //======================================================================
          //Initializing the date
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        dateUserInput = df.format(c);

//        containerLayout = new LinearLayout(this);
//        mainLayout = new LinearLayout(this);
//        popUpWindow = new PopupWindow(this);
        //======================================================================

        calendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dateUserInput = dayOfMonth+"/"+(month+1)+"/"+year;
                Log.d("SELECTED DATE", dateUserInput);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View inflator = (LayoutInflater.from(CalendarActivity.this)).inflate(R.layout.dialog_create,null);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CalendarActivity.this);
                alertBuilder.setView(inflator);

                //======================Dialog Box Layout===============================
                titleAppointment = (EditText)inflator.findViewById(R.id.titleEdit);
                detailsAppointment = (EditText)inflator.findViewById(R.id.detailsEdit);
                timePicker1 = (TimePicker) inflator.findViewById(R.id.timePicker1);

                detailsAppointment.setMovementMethod(new ScrollingMovementMethod()); //Enabling scrolling
                //======================================================================

                alertBuilder.setCancelable(true).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        titleUserInput = titleAppointment.getText().toString();
                            Log.d("TITLE USER INPUT", titleUserInput);
                        detailsUserInput = detailsAppointment.getText().toString();
                            Log.d("DETAILS USER INPUT", detailsUserInput);
                        int hour = timePicker1.getCurrentHour();
                            Log.d("HOUR : ", String.valueOf(hour));
                        int min = timePicker1.getCurrentMinute();
                            Log.d("MINUTE : ", String.valueOf(min));
                        timeUserInput = String.valueOf(hour)+":"+String.valueOf(min);

                        //=========== Database =============

                        sqlHelper = new SQLHelper(CalendarActivity.this);
                        //create table.
                        sqlHelper.createTbl();
                        //get values from views and pass to sqlite.
                        if(titleUserInput.equals("") ||detailsUserInput.equals("") ||timeUserInput.equals("")){

                            Toast.makeText(CalendarActivity.this, "Please fill all details.", Toast.LENGTH_SHORT).show();
                        }
                        if(!duplicateEntry()){
                            Toast.makeText(CalendarActivity.this, titleUserInput+" Already Exists", Toast.LENGTH_SHORT).show();
                            titleAppointment.setText("");
                        }
                        else{

                            //ready to store in sqlite.
                            sqlHelper.insert(titleUserInput,detailsUserInput, timeUserInput, dateUserInput);
                            Toast.makeText(CalendarActivity.this, "Data store successfully.", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                alertBuilder.setCancelable(true).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                Dialog dialog= alertBuilder.create();
                dialog.show();

            }
        });



//        mainFloatBut = (FloatingActionButton)findViewById(R.id.floatbutmain);
//        createFloatBut = (FloatingActionButton)findViewById(R.id.floatbutcreate);
//        editFloatbut = (FloatingActionButton)findViewById(R.id.floatbutedit);
//        deleteFloatbut = (FloatingActionButton)findViewById(R.id.floatbutdelete);
//        moveFloatBut = (FloatingActionButton)findViewById(R.id.floatbutmove);
//        searchFloatBut = (FloatingActionButton)findViewById(R.id.floatbutsearch);

//        mainFloatBut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isOpen){
//                    createFloatBut.setClickable(false);
//                    editFloatbut.setClickable(false);
//                    deleteFloatbut.setClickable(false);
//                    moveFloatBut.setClickable(false);
//                    searchFloatBut.setClickable(false);
//
//                }else{
//                    createFloatBut.setClickable(true);
//                    editFloatbut.setClickable(true);
//                    deleteFloatbut.setClickable(true);
//                    moveFloatBut.setClickable(true);
//                    searchFloatBut.setClickable(true);
//                }
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(CalendarActivity.this, "Action Expanded", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Toast.makeText(CalendarActivity.this, "Action collapsed", Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        MenuItem searchItems = menu.findItem(R.id.search);
        searchItems.setOnActionExpandListener(onActionExpandListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.search:

                break;
            case R.id.delete:
                View inflator = (LayoutInflater.from(CalendarActivity.this)).inflate(R.layout.dialog_delete,null);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CalendarActivity.this);
                alertBuilder.setView(inflator);

                alertBuilder.setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (sqlHelper.rowCheck(dateUserInput)==0) {
                            Toast.makeText(CalendarActivity.this, "No Appointments to be deleted", Toast.LENGTH_SHORT).show();
                        }else {
                            sqlHelper.deleteRow(dateUserInput);
                        }

                    }
                });
                alertBuilder.setCancelable(true).setNegativeButton("Select Appointment", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertBuilder.setCancelable(true).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                Dialog dialog= alertBuilder.create();
                dialog.setTitle("Do you want to delete all appointments?");
                dialog.show();

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean duplicateEntry(){
        if(sqlHelper.getCursorCount(titleUserInput,dateUserInput) == 0 ){
            return true;
        }else{
            return false;
        }

    }



}
