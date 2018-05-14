package cwk2.appointments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class ThesaurusActivity extends AppCompatActivity {
    final String endpoint = "http://thesaurus.altervista.org/thesaurus/v1";
    static ArrayList<String> SynonymsList;
    static ListView SynList;
    static ListAdapter adapter;
    static  String ThesaurusWord;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thesaurus);
        SynList=(ListView)findViewById(R.id.thesaurusList);
        Intent intent = getIntent();
        ThesaurusWord = intent.getStringExtra("thesText");
        Log.d("WORD : ", "Hey");
        SendThesaurusRequest str= new SendThesaurusRequest(ThesaurusWord, "en_US", "PdgVDXgGWSfM4HzdF10n", "json",SynList,this,ThesaurusActivity.this);
        str.execute();
    }

    public void onBackPressed() {
        SendThesaurusRequest.clearList();
        super.onBackPressed();

    }


}

