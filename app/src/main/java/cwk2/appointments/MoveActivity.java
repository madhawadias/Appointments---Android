package cwk2.appointments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

public class MoveActivity extends AppCompatActivity {

    SQLHelper myDB;
    String dateInDialog, titleMove;
    private TextView titleAppointment;
    private CalendarView calendarView;
    int count=0;
    CalendarActivity calendarActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);

        final String dateSelected2= getIntent().getStringExtra("DateFromPrevious");

        Button move = (Button)findViewById(R.id.movebut);
        final EditText titleEditMove =  findViewById(R.id.titleEditMove);

//        Log.d("TITLE VALUUUUUEEE : ", titleMove);

        ListView listView = (ListView) findViewById(R.id.listviewMove);
        myDB = new SQLHelper(this);

        //populate an ArrayList<String> from the database and then view it
        ArrayList<String> list = new ArrayList<>();
        Cursor data = myDB.getRow(dateSelected2);
        if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                String listItemOne;
                listItemOne = data.getString(2)+" | "+data.getString(0)+" \n "+data.getString(1);

                list.add(listItemOne);
                ListAdapter listAdapter = new ArrayAdapter<>(MoveActivity.this,android.R.layout.simple_expandable_list_item_1,list);
                listView.setAdapter(listAdapter);
            }
        }

        // Method invoked when pressing the edit button the Edit activity
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleMove = titleEditMove.getText().toString();
                View inflator = (LayoutInflater.from(MoveActivity.this)).inflate(R.layout.dialog_move,null);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MoveActivity.this);
                alertBuilder.setView(inflator);

                //======================Dialog Box Layout===============================
                calendarView = (CalendarView)inflator.findViewById(R.id.calendarViewMove);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        dateInDialog = dayOfMonth+"/"+(month+1)+"/"+year;
                        Log.d("SELECTED DATE", dateInDialog);
                    }
                });
                titleAppointment = (TextView)inflator.findViewById(R.id.titleText);
                titleAppointment.setText(titleEditMove.getText());



                //======================================================================

                alertBuilder.setCancelable(true).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //=========== Database =============

                        myDB = new SQLHelper(MoveActivity.this);
                        //create table.
                        myDB.createTbl();
                        //get values from views and pass to sqlite.
                        if(dateInDialog.equals("")){

                            Toast.makeText(MoveActivity.this, "Appointment NOT created. Please fill all details.", Toast.LENGTH_SHORT).show();
                        }
//                        if(!calendarActivity.duplicateEntry()){
//                            Toast.makeText(ViewEditActivity.this, titleUserInput+" Already Exists", Toast.LENGTH_SHORT).show();
//                            titleAppointment.setText("");
//                        }
                        else{
                            Log.d("SELECTED TITLE : ", titleMove);
                            //ready to store in sqlite.
                            myDB.updateMove(titleMove, dateInDialog);
                            Toast.makeText(MoveActivity.this, "Data moved successfully.", Toast.LENGTH_SHORT).show();

                            count++;
                        }

                        finish();
                        startActivity(getIntent());


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
