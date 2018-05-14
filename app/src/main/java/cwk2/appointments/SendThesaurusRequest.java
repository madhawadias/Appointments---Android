package cwk2.appointments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.PatternMatcher;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class SendThesaurusRequest extends AsyncTask<Void, ArrayList<String>, Boolean> {
    static String word,language,key,output;
    static ListView threshList;
    static Context newCont;
    static ListAdapter adapter;
    static ArrayList<String> SynonymsList = new ArrayList<String>();
    static ArrayList<String> SynonymsF1 = new ArrayList<String>();
    static ArrayList<String> SynonymsF2 = new ArrayList<String>();
    static ThesaurusActivity ThesAct;
    final String endpoint = "http://thesaurus.altervista.org/thesaurus/v1";
    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            URL serverAddress = new URL(endpoint + "?word="+ URLEncoder.encode(word, "UTF-8")+"&language="+language+"&key="+key+"&output="+output);
            HttpURLConnection connection = (HttpURLConnection)serverAddress.openConnection();
            connection.connect();
            int rc = connection.getResponseCode();
            if (rc == 200) {
                String line = null;
                BufferedReader br = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null)
                    sb.append(line + '\n');
                JSONObject obj = (JSONObject) JSONValue.parse(sb.toString());
                JSONArray array = (JSONArray)obj.get("response");
                for (int i=0; i < array.size(); i++) {
                    JSONObject list = (JSONObject) ((JSONObject)array.get(i)).get("list");
                    System.out.println(list.get("category")+":"+list.get("synonyms"));
                    Log.d("Synonyms",list.get("category")+":"+list.get("synonyms"));
                    SynonymsList.add(list.get("synonyms").toString());
                }
            } else System.out.println("HTTP error:"+rc);
            connection.disconnect();
        } catch (java.net.MalformedURLException e) {
            e.printStackTrace();
        } catch (java.net.ProtocolException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    public static void clearList() {
        SynonymsList.clear();
        SynonymsF1.clear();
        SynonymsF2.clear();
    }

    public void FormatList(){

        for(String d: SynonymsList ){
            String[] set1=d.split(Pattern.quote("|"));
            for (String i :set1){
                SynonymsF1.add(i);
            }
        }

        for(String e : SynonymsF1){
            String[] set2=e.split(Pattern.quote("(generic term)"));
            for(String f:set2){
                SynonymsF2.add(f);
            }
        }

    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        FormatList();
        adapter = new ArrayAdapter<>(newCont, android.R.layout.simple_list_item_1, SynonymsF2);
        threshList.setAdapter(adapter);
        threshList.setTextFilterEnabled(true);
        threshList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long id) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(newCont);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("synonym",SynonymsF2.get(position));
                editor.commit();
                SynonymsList.clear();
                SynonymsF1.clear();
                SynonymsF2.clear();
                ThesAct.finish();

            }});
        // SynonymsList.clear();

    }


    public SendThesaurusRequest(String word, String language, String key, String output, ListView updateList, Context newCont, ThesaurusActivity ThesAct ) {
        this.word=word;
        this.language=language;
        this.key=key;
        this.output=output;
        this.threshList=updateList;
        this.newCont=newCont;
        this.ThesAct=ThesAct;

    }

}
