package cwk2.appointments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    CalendarActivity calendarActivity;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //getting array list used to store data in the CalendarActivity.java
        ArrayList<String> list = (ArrayList<String>) getIntent().getSerializableExtra("mylist");

        ListView listView = (ListView)findViewById(R.id.listviewSearch);
        EditText searchFilter = (EditText)findViewById(R.id.editTextSearch);

//        ArrayList<String> records = new ArrayList<>();


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        searchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (SearchActivity.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
