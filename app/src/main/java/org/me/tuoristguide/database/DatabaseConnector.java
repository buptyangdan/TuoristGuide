package org.me.tuoristguide.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseConnector
{
    public DatabaseConnector(Context context)
    {
    }

    // open the database connection
    public void open() throws SQLException
    {
    }

    public void close()
    {
    }

    public Cursor getAllRows(String tableName) {

        return null;
    }

    public Cursor getAllScoresForOneQuiz(String colName)
    {
        return null;
    }

    public void showDataBaseEntries() {

    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper
    {
        public DatabaseOpenHelper(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
        }
    }
}

