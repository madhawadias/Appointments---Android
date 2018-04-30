package cwk2.appointments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import static android.graphics.Color.WHITE;

public class CalendarActivity extends AppCompatActivity {

    FloatingActionButton mainFloatBut, createFloatBut, editFloatbut, deleteFloatbut, moveFloatBut, searchFloatBut;
    Animation openfloat, closeFloat;
    private boolean isOpen = false;
    private CalendarView calendarview;
    private Toolbar toolbar;

    private FloatingActionMenu floatingActionMenu;
    private com.github.clans.fab.FloatingActionButton create, edit, move;

    private EditText titleAppointment;
    private EditText detailsAppointment;

    // Create function
    String titleUserInput;
    String detailsUserInput;

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
        //======================================================================
//        Button enterCreate = (Button)findViewById(R.id.enterBut);

        //======================Dialog Box Layout===============================
        //======================================================================

        //======================================================================
        //======================================================================

        calendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth+"/"+(month+1)+"/"+year;
                Log.d("SELECTED DATE", date);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CalendarActivity.this, "Create is clicked", Toast.LENGTH_SHORT).show();

                View inflator = (LayoutInflater.from(CalendarActivity.this)).inflate(R.layout.dialog_create,null);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CalendarActivity.this);
                alertBuilder.setView(inflator);

                titleAppointment = (EditText)inflator.findViewById(R.id.titleEdit);
                detailsAppointment = (EditText)inflator.findViewById(R.id.detailsEdit);

                alertBuilder.setCancelable(true).setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        titleUserInput = titleAppointment.getText().toString();
                        Log.d("TITLE USER INPUT", titleUserInput);
                        detailsUserInput = detailsAppointment.getText().toString();
                        Log.d("DETAILS USER INPUT", detailsUserInput);
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
//                Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "Delete Clicked", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }



}
