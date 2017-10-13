package com.example.martinmelo.uvarobotics;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by martinmelo on 10/13/17.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "number.sqlite";
    private static final int DB_SCHEME_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DataBaseManager.CREATE_TABLE_NUMBERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE numbers");
        sqLiteDatabase.execSQL(DataBaseManager.CREATE_TABLE_NUMBERS);
    }

    public ContentValues generarContentValuesNumbers(String number)
    {
        ContentValues values = new ContentValues();
        values.put(DataBaseManager.NUM_NUMBER, number);
        return values;
    }
}
