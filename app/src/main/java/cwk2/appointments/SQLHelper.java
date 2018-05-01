package cwk2.appointments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLHelper extends SQLiteOpenHelper{

    private static final String dbName = "mydb.db";
    private static final String tblName = "appointments";
    public int cursorCount=0;

    public SQLHelper(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTbl(){
        SQLiteDatabase database = getWritableDatabase();
        String query = "create table if not exists " + tblName +"(title text primary key, details text, time text, date text)";
        database.execSQL(query);
        Log.d("TABLE CREATE : ", query);
    }

    public void dropTbl(){
        SQLiteDatabase database = getWritableDatabase();
        String query = "drop table " + tblName;
        database.execSQL(query);
        Log.d("TABLE DROP : ", query);
    }

    public void deleteRow(String title){
        SQLiteDatabase database = getWritableDatabase();
        String query = "delete from " + tblName + " where title = '"+title+"'";
        database.execSQL(query);
        Log.d("TABLE DELETE : ", query);
    }

    // Insert query
    public void insert(String title, String details, String time, String date){
        SQLiteDatabase database = getWritableDatabase();
        String query = "insert into " + tblName + " (title, details, time, date) values ('"+title+"','"+details+"','"+time+"','"+date+"')";
        database.execSQL(query);
        Log.d("TABLE INSERT : ", query);
    }

    //get rows values.
    //do remember we use cursor for getting current pointing values from result set(retrieved rows set).
    public Cursor getRow(String date) {
        SQLiteDatabase database = getReadableDatabase();//get database read reference because we need to read something from database.
        Cursor cursor = database.rawQuery("select title,details,time,date from " + tblName + " where date = '"+ date +"'", null);

        return cursor;
    }

    public int getCursorCount(String title, String date){
        SQLiteDatabase database = getReadableDatabase();//get database read reference because we need to read something from database.
        Cursor cursor = database.rawQuery("select title,details,time,date from " + tblName + " where title = '" + title + "'And date = '"+date+"'", null);
        cursorCount = cursor.getCount();
        return cursorCount;
    }

    public int rowCheck(String date) {
        SQLiteDatabase database = getReadableDatabase();//get database read reference because we need to read something from database.
        Cursor cursor = database.rawQuery("select title from " + tblName + " where date = '"+date+"'", null);
        cursorCount = cursor.getCount();
        Log.d("CURSOR COUNT : ", String.valueOf(cursorCount));
        return cursorCount;
    }

    public Cursor getAllListContent() {
        SQLiteDatabase database = getReadableDatabase();//get database read reference because we need to read something from database.
        Cursor data = database.rawQuery("select * from " + tblName, null);

        return data;
    }

    public void deleteData(String date){
        SQLiteDatabase database = getWritableDatabase();
        String query = "delete from " + tblName + " where date = '"+ date +"'";
        database.execSQL(query);
        Log.d("TABLE DELETE : ", query);
    }

    public void updateRow(String title, String details, String time, String date){
        SQLiteDatabase database = getWritableDatabase();
        String query = "update " + tblName + " SET details = '"+details+ "', time = '"+time+ "' ,date = '"+date+ "' where title = '"+title+"'";
        database.execSQL(query);
        Log.d("TABLE UPDATE : ", query);
    }

    public void updateMove(String title, String date){
        SQLiteDatabase database = getWritableDatabase();
        String query = "update " + tblName + " SET date = '"+date+ "' where title = '"+title+"'";
        database.execSQL(query);
        Log.d("TABLE UPDATE : ", query);
    }

}
