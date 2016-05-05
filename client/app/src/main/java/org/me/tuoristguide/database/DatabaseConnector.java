package org.me.tuoristguide.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.me.tuoristguide.model.User;

import java.io.File;
import java.sql.DataTruncation;
import java.util.ArrayList;

/*
  User: Name, Email, photo_url to save current_user
 */
public class DatabaseConnector extends  SQLiteOpenHelper{
    private User user;
    public DataTruncation instance;

    private static  final String createUser = "create table User ("
            + "id integer primary key autoincrement, "
            + "user_name TEXT, "
            + "email TEXT, "
            + "photo_url TEXT);";

    public DatabaseConnector(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
       super(context, name, factory, version);
       // super(context, "/sdcard/"+name, null, version);
//        SQLiteDatabase.openOrCreateDatabase("/sdcard/" + name, null);

        Log.d("Database Opreations", "CreateDatabase"+getDatabaseName());
    }
    public  void setUser(User user){
        this.user=user;
    }
    public  ContentValues insertValues(){
        ContentValues contentValues=new ContentValues();
        System.out.println("here is the saved user!");
        System.out.println(user.name);
        System.out.println(user.email);
        System.out.println(user.picture_url);
        contentValues.put("user_name", user.name);
        contentValues.put("email", user.email);
        contentValues.put("photo_url",user.picture_url);
        return  contentValues;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createUser);
        Log.d("Database Operations", "Create Tables!");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists User");
        onCreate(db);
    }


    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }


}


