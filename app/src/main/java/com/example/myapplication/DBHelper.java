package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tidesDB";
    private static final String TABLE_ScheduleTides = "scheduleTides";

    private static final String KEY_ID = "_id";
    private static final String KEY_SunriseTime = "sunriseTime";
    private static final String KEY_SunsetTime = "sunsetTime";
    private static final String KEY_TidesTime_1 = "tidesTime_1";
    private static final String KEY_TidesHeight_1 = "tidesHeight_1";
    private static final String KEY_TidesTime_2 = "tidesTime_2";
    private static final String KEY_TidesHeight_2 = "tidesHeight_2";
    private static final String KEY_TidesTime_3 = "tidesTime_3";
    private static final String KEY_TidesHeight_3 = "tidesHeight_3";
    private static final String KEY_TidesTime_4 = "tidesTime_4";
    private static final String KEY_TidesHeight_4 = "tidesHeight_4";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_ScheduleTides + "("
            + KEY_ID + " integer primary key, "
            + KEY_SunriseTime + " text not null, "
            + KEY_SunsetTime + " text not null,"
            + KEY_TidesTime_1 + " text not null,"
            + KEY_TidesHeight_1 + " text not null,"
            + KEY_TidesTime_2 + " text not null,"
            + KEY_TidesHeight_2 + " text not null,"
            + KEY_TidesTime_3 + " text not null,"
            + KEY_TidesHeight_3 + " text not null,"
            + KEY_TidesTime_4 + " text not null,"
            + KEY_TidesHeight_4 + " text not null"
            + ");";



    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_ScheduleTides);
        onCreate(db);
    }

    public void addTidesData(SQLiteDatabase db, List<String> tidesTable){
        ContentValues values = new ContentValues();
        for(int i = 0; i < tidesTable.size(); i+=11) {
            values.put(KEY_ID, " " + tidesTable.get(i));
            values.put(KEY_SunriseTime, " " + tidesTable.get(i + 1));
            values.put(KEY_SunsetTime, " " + tidesTable.get(i + 2));
            values.put(KEY_TidesTime_1, " " + tidesTable.get(i + 3));
            values.put(KEY_TidesHeight_1, " " + tidesTable.get(i + 4));
            values.put(KEY_TidesTime_2, " " + tidesTable.get(i + 5));
            values.put(KEY_TidesHeight_2, " " + tidesTable.get(i + 6));
            values.put(KEY_TidesTime_3, " " + tidesTable.get(i + 7));
            values.put(KEY_TidesHeight_3, " " + tidesTable.get(i + 8));
            values.put(KEY_TidesTime_4, " " + tidesTable.get(i + 9));
            values.put(KEY_TidesHeight_4, " " + tidesTable.get(i + 10));
            db.insert(TABLE_ScheduleTides, null, values);
        }
    }
}
