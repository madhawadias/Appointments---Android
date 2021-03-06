package cwk2.appointments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TimePicker;

import com.github.clans.fab.FloatingActionMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.graphics.Color.WHITE;

public class CalendarActivity extends AppCompatActivity {

    private boolean isOpen = false;
    private CalendarView calendarview;
    private Toolbar toolbar;
    private LinearLayout mainLayout;
    private ListView listView;
    private FloatingActionMenu floatingActionMenu;
    private com.github.clans.fab.FloatingActionButton create, edit, move, delete, search;
    private EditText titleAppointment;
    private EditText detailsAppointment;
    private EditText thesaurus;
    private TimePicker timePicker1;
    private Button thesBut, selectedTextBut;
    int count=0;
    // Create function
    String titleUserInput, detailsUserInput, timeUserInput, dateUserInput;
    SQLHelper sqlHelper;
    ArrayAdapter<String> theList;
    ArrayList<String> arrayList;
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
        // Items Related to the Floating Action Menu
        //======================================================================
        floatingActionMenu = (FloatingActionMenu)findViewById(R.id.actionMenu);
        create = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.createActionBut);
        edit = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.editActionbut);
        move = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.moveActionbut);
        delete = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.delete);
        search = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.searchActionBut);

        //======================================================================
//        Button enterCreate = (Button)findViewById(R.id.enterBut);
        arrayList = new ArrayList<>();

        //======================================================================
          //Initializing the date
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        dateUserInput = df.format(c);
        Log.d("FIRST DATE : ", dateUserInput);

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
                thesBut = (Button) inflator.findViewById(R.id.Thesbutton);
                selectedTextBut = (Button) inflator.findViewById(R.id.selectTextBut);
                thesaurus = (EditText) inflator.findViewById(R.id.thesaurusText);


                selectedTextBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText et=(EditText)findViewById(R.id.detailsEdit);

                        int startSelection=et.getSelectionStart();
                        int endSelection=et.getSelectionEnd();
                        final String selectedText = et.getText().toString().substring(startSelection, endSelection);
                        Intent intent = new Intent(CalendarActivity.this, ThesaurusActivity.class).putExtra("thesText",selectedText);
                        startActivity(intent);
                    }
                });

                thesBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CalendarActivity.this, ThesaurusActivity.class).putExtra("thesText",thesaurus.getText().toString());
                        startActivity(intent);
                    }
                });

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

                            Toast.makeText(CalendarActivity.this, "Appointment NOT created. Please fill all details.", Toast.LENGTH_SHORT).show();
                        }
                        if(!duplicateEntry()){
                            Toast.makeText(CalendarActivity.this, titleUserInput+" Already Exists", Toast.LENGTH_SHORT).show();
                            titleAppointment.setText("");
                        }
                        else{

                            arrayList.add(titleUserInput+" || "+detailsUserInput);
                            Log.d("ARRAY LIST : ", arrayList.toString());
                            //ready to store in sqlite.
                            sqlHelper.insert(titleUserInput,detailsUserInput, timeUserInput, dateUserInput);
                            Toast.makeText(CalendarActivity.this, "Data store successfully.", Toast.LENGTH_SHORT).show();

                            count++;
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

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, ViewEditActivity.class).putExtra("DateFromPrevious",dateUserInput);
                startActivity(intent);
            }
        });
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, MoveActivity.class).putExtra("DateFromPrevious",dateUserInput);
                startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, SearchActivity.class).putExtra("mylist",arrayList);
                startActivity(intent);
            }
        });


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

    //Method called when items in the action bar are selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.search:

                break;
            case R.id.delete:
                Intent intent = new Intent(CalendarActivity.this, DeleteActivity.class).putExtra("DateFromPrevious",dateUserInput);
                startActivity(intent);
//                final View inflator = (LayoutInflater.from(CalendarActivity.this)).inflate(R.layout.dialog_delete_selected,null);
//                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CalendarActivity.this);
//                listView = (ListView) findViewById(R.id.listview);
//                alertBuilder.setView(inflator);
//
//
//                sqlHelper = new SQLHelper(CalendarActivity.this);
//
//                ArrayList<String> theList = new ArrayList<>();
//                Cursor data = sqlHelper.getAllListContent();
//                theList.add(data.getString(1));
//                theList.add(data.getString(2));
//                theList.add(data.getString(3));
//                ListAdapter listAdapter = new ArrayAdapter<>(CalendarActivity.this,android.R.layout.simple_list_item_1,theList);
//                listView.setAdapter(listAdapter);
//
//                alertBuilder.setCancelable(true).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//
//                    }
//                });
//
//
//                alertBuilder.setCancelable(true).setNegativeButton("Delete All", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if(count>0){
//                            if (sqlHelper.rowCheck(dateUserInput)==0) {
//                                Toast.makeText(CalendarActivity.this, "No Appointments to be deleted", Toast.LENGTH_SHORT).show();
//                            }else {
//                                sqlHelper.deleteRow(dateUserInput);
//                                Toast.makeText(CalendarActivity.this, "All Appointments deleted", Toast.LENGTH_SHORT).show();
//                            }
//                        }else{
//                            Toast.makeText(CalendarActivity.this, "No Appointments to be deleted", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//                alertBuilder.setCancelable(true).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                Dialog dialog= alertBuilder.create();
//                dialog.setTitle("Do you want to delete all appointments?");
//                dialog.show();
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

    public ArrayList<String> getList() {
        return arrayList;
    }





}
