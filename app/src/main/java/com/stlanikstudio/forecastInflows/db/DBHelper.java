package com.stlanikstudio.forecastInflows.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.stlanikstudio.forecastInflows.models.TidesDay;

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

    public void addTidesData(SQLiteDatabase db, List<TidesDay> tidesTable){
        ContentValues values = new ContentValues();
        for(int i = 0; i < tidesTable.size(); i++) {
            TidesDay tidesDay = tidesTable.get(i);
            values.put(KEY_ID, " " + tidesDay.getId());
            values.put(KEY_SunriseTime, " " + tidesDay.getSunriseTime());
            values.put(KEY_SunsetTime, " " + tidesDay.getSunsetTime());
            values.put(KEY_TidesTime_1, " " + tidesDay.getTidesTimeFirst());
            values.put(KEY_TidesHeight_1, " " + tidesDay.getTidesHeightFirst());
            values.put(KEY_TidesTime_2, " " + tidesDay.getTidesTimeSecond());
            values.put(KEY_TidesHeight_2, " " + tidesDay.getGetTidesHeightSecond());
            values.put(KEY_TidesTime_3, " " + tidesDay.getTidesTimeThird());
            values.put(KEY_TidesHeight_3, " " + tidesDay.getGetTidesHeightThird());
            values.put(KEY_TidesTime_4, " " + tidesDay.getTidesTimeFourth());
            values.put(KEY_TidesHeight_4, " " + tidesDay.getGetTidesHeightFourth());
            db.insert(TABLE_ScheduleTides, null, values);
        }
    }
}
