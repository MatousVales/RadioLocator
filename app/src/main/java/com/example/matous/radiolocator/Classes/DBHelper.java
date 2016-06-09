package com.example.matous.radiolocator.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.matous.radiolocator.Activities.DatabaseActivity;
import com.example.matous.radiolocator.Models.Datapoint;
import com.example.matous.radiolocator.Models.Measurement;

import java.util.ArrayList;

/**
 * Created by Matous on 27.02.2016.
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Measurements.db";
    public static final String TABLE_NAME = "Measurements";
    public static final String COLUMN_mID = "mID";
    public static final String COLUMN_DATE = "datetime";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_ANGLE = "angle";

    private ArrayList<Measurement> measurements = null;
    private ArrayList<Datapoint> datapoints = null;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Measurements ( mID INTEGER, datetime TEXT, latitude REAL, longitude REAL, angle REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Measurements");
        onCreate(db);
    }

    public boolean deleteMeasurement(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_mID + "=" + id, null);
        return true;
    }

    public boolean insertMeasurement(ContentValues cv){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, cv);
        return true;
    }

    public ArrayList<Measurement> getMeasurement(int pos){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select mID, count(mID) as Count from " + TABLE_NAME + " group by mID having mID =" + pos + "", null);
        cursor.moveToFirst();
        measurements = new ArrayList<>();

        while(!cursor.isAfterLast()){
            measurements.add(new Measurement(cursor.getInt(cursor.getColumnIndex(COLUMN_mID)),cursor.getInt(cursor.getColumnIndex("Count"))));
            cursor.moveToNext();
        }

        return measurements;
    }

    public ArrayList<Measurement> getMeasurements(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select mID, count(mID) as Count from " + TABLE_NAME + " group by mID",null);
        cursor.moveToFirst();
        measurements = new ArrayList<>();

        while(!cursor.isAfterLast()){
            measurements.add(new Measurement(cursor.getInt(cursor.getColumnIndex(COLUMN_mID)),cursor.getInt(cursor.getColumnIndex("Count"))));
            cursor.moveToNext();
        }

        return measurements;
    }

    public ArrayList<Datapoint> getDatapoints(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where mID=" + id +"" ,null);
        cursor.moveToFirst();
        datapoints = new ArrayList<>();

        while(!cursor.isAfterLast()){
            datapoints.add(new Datapoint(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)), cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE)), cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE)), cursor.getDouble(cursor.getColumnIndex(COLUMN_ANGLE))));
            cursor.moveToNext();
        }
        return datapoints;
    }


    public void removeAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "1", null);
    }

    public boolean isEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select count(*) from " + TABLE_NAME + "",null);
        if(c != null){
            c.moveToFirst();
            if(c.getInt(0) == 0){
                return true;
            }
        } else {
            return  false;
        }
        return false;
    }

}
