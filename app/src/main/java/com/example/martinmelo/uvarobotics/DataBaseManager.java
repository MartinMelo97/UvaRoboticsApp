package com.example.martinmelo.uvarobotics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by martinmelo on 10/13/17.
 */

public class DataBaseManager {
    public static final String TABLE_NAME = "numbers";
    public static final String NUM_ID = "_id";
    public static final String NUM_NUMBER = "numero";

    public static final String CREATE_TABLE_NUMBERS = "CREATE TABLE "+TABLE_NAME+
            "("+NUM_ID+" integer primary key autoincrement," +
            NUM_NUMBER+" varchar(13) not null)";

    private DataBaseHelper helper;
    private SQLiteDatabase db;

    public DataBaseManager(Context context, String type)
    {
        helper = new DataBaseHelper(context);
        switch (type)
        {
            case "write":
                db = helper.getWritableDatabase();
                break;
            case "read":
                db = helper.getReadableDatabase();
                break;
        }
    }

    public Cursor lookingForNumber()
    {
        String[] columns = new String[]{NUM_ID, NUM_NUMBER};

        Cursor register = db.query(TABLE_NAME, columns,null,null,null,null,null);

        if(register == null)
        {
            Log.d("MainActivity","Empty");
        }
        else
        {
            Log.d("MainActivity","Si hay: "+register.getCount());
        }

        return register;
    }

    public void insertNumber(ContentValues values, String table)
    {
        db.insert(table, null, values);
    }
}
