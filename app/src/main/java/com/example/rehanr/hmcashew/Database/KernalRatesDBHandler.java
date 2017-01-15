package com.example.rehanr.hmcashew.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rehanr.hmcashew.Models.KernalRates;
import com.example.rehanr.hmcashew.Models.TinStock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rehan r on 14-01-2017.
 */
public class KernalRatesDBHandler  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cashewdb2";
    private static final String TABLE_KERNALRATES= "kernalstock";

    // product Table Columns names
    private static final String KEY_ID = "grade_name";
    private static final String KEY_DATE1 = "date1";
    private static final String KEY_RATE1 = "rate1";
    private static final String KEY_DATE2 = "date2";
    private static final String KEY_RATE2 = "rate2";


    public KernalRatesDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_KERNALRATES + "("
                + KEY_ID + " TEXT PRIMARY KEY," +KEY_DATE1 + " TEXT," + KEY_RATE1 + " REAL," +
                KEY_DATE2 + " TEXT," + KEY_RATE2 + " REAL" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KERNALRATES);
        onCreate(db);
    }

    public void addKernalRates(KernalRates kernalRate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, kernalRate.getGradeName());
        values.put(KEY_DATE1, kernalRate.getDate1());
        values.put(KEY_RATE1, kernalRate.getRate1());
        values.put(KEY_DATE2, kernalRate.getDate2());
        values.put(KEY_RATE2, kernalRate.getRate2());
        db.insert(TABLE_KERNALRATES, null, values);
        db.close();
    }


    public List<KernalRates> getAllStocks() {
        List<KernalRates> rates = new ArrayList<KernalRates>();
        String selectQuery = "SELECT  * FROM " + TABLE_KERNALRATES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                KernalRates kernalRates = new KernalRates();
                kernalRates.setGradeName(cursor.getString(0));
                kernalRates.setDate1(cursor.getString(1));
                kernalRates.setRate1(Double.parseDouble(cursor.getString(2)));
                kernalRates.setDate2(cursor.getString(3));
                kernalRates.setRate2(Double.parseDouble(cursor.getString(4)));

                rates.add(kernalRates);
            } while (cursor.moveToNext());
        }
        return rates;
    }


    public int getKernalRatesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_KERNALRATES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void ClearAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+TABLE_KERNALRATES);
        db.close();
    }

}
