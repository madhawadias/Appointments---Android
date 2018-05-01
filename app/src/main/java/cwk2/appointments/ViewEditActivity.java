package cwk2.appointments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewEditActivity extends AppCompatActivity {

    SQLHelper myDB;
    String titleUserInput, detailsUserInput, timeUserInput, dateUserInput;
    private EditText titleAppointment;
    private EditText detailsAppointment;
    private TimePicker timePicker1;
    int count=0;
    CalendarActivity calendarActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit);

        final String dateSelected1= getIntent().getStringExtra("DateFromPrevious");

        Button edit = (Button)findViewById(R.id.editBut);
        final EditText titleEditText = (EditText) findViewById(R.id.titleTextEdit);

        ListView listView = (ListView) findViewById(R.id.listviewEdit);
        myDB = new SQLHelper(this);

        //populate an ArrayList<String> from the database and then view it
        ArrayList<String> list = new ArrayList<>();
        Cursor data = myDB.getRow(dateSelected1);
        if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                String listItemOne;
                listItemOne = data.getString(2)+"  "+data.getString(0)+"  "+data.getString(1);

                list.add(listItemOne);
                ListAdapter listAdapter = new ArrayAdapter<>(ViewEditActivity.this,android.R.layout.simple_expandable_list_item_1,list);
                listView.setAdapter(listAdapter);
            }
        }

        // Method invoked when pressing the edit button the Edit activity
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View inflator = (LayoutInflater.from(ViewEditActivity.this)).inflate(R.layout.dialog_create,null);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ViewEditActivity.this);
                alertBuilder.setView(inflator);

                //======================Dialog Box Layout===============================
                titleAppointment = (EditText)inflator.findViewById(R.id.titleEdit);
                titleAppointment.setText(titleEditText.getText());

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

                        //Deleting record from database
//                        myDB.deleteRow(titleUserInput);

                        //=========== Database =============

                        myDB = new SQLHelper(ViewEditActivity.this);
                        //create table.
                        myDB.createTbl();
                        //get values from views and pass to sqlite.
                        if(titleUserInput.equals("") ||detailsUserInput.equals("") ||timeUserInput.equals("")){

                            Toast.makeText(ViewEditActivity.this, "Appointment NOT created. Please fill all details.", Toast.LENGTH_SHORT).show();
                        }
//                        if(!calendarActivity.duplicateEntry()){
//                            Toast.makeText(ViewEditActivity.this, titleUserInput+" Already Exists", Toast.LENGTH_SHORT).show();
//                            titleAppointment.setText("");
//                        }
                        else{

                            //ready to store in sqlite.
                            myDB.updateRow(titleUserInput,detailsUserInput, timeUserInput, dateSelected1);
                            Toast.makeText(ViewEditActivity.this, "Data store successfully.", Toast.LENGTH_SHORT).show();

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
    }
}