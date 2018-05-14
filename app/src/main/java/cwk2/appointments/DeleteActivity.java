package cwk2.appointments;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeleteActivity extends AppCompatActivity {

    SQLHelper myDB;
    CalendarActivity calendarActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        final String dateSlected= getIntent().getStringExtra("DateFromPrevious");

        Button delete = (Button)findViewById(R.id.deleteBut);
        Button deleteAll = (Button)findViewById(R.id.deleteAllBut);
        final EditText titleUserInput = (EditText) findViewById(R.id.titleuserinput);

        ListView listView = (ListView) findViewById(R.id.listviewDelete);
        myDB = new SQLHelper(this);

        //populate an ArrayList<String> from the database and then view it
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getRow(dateSlected);
        if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                String listItem;
                listItem = data.getString(2)+" | "+data.getString(0)+" \n "+data.getString(1);

                theList.add(listItem);
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_expandable_list_item_1,theList);
                listView.setAdapter(listAdapter);
            }
        }

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.deleteData(dateSlected);
                finish();
                startActivity(getIntent());
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleInput = titleUserInput.getText().toString();
                myDB.deleteRow(titleInput);
                finish();
                startActivity(getIntent());

            }
        });

    }
}
