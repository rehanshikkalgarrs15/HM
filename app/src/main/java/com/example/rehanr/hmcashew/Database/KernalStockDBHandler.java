package com.example.rehanr.hmcashew.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rehanr.hmcashew.Models.TinStock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rehan r on 13-01-2017.
 */
public class KernalStockDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cashewdb";
    private static final String TABLE_KERNALSTOCK = "kernalstock";
    private static final String TABLE_KERNALSTOCKLASTSTOREDDATE = "kernalstockstoreddate";

    // product Table Columns names
    private static final String KEY_ID = "grade_name";
    private static final String KEY_TINS = "tins";
    private static final String KEY_DATE = "date";



    public KernalStockDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_KERNALSTOCK + "("
                + KEY_ID + " TEXT PRIMARY KEY," + KEY_TINS + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        String CREATE_STOREDATE_TABLE = "CREATE TABLE " + TABLE_KERNALSTOCKLASTSTOREDDATE + "("
                + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_STOREDATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KERNALSTOCK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KERNALSTOCKLASTSTOREDDATE);
        onCreate(db);
    }


    public void addStock(TinStock tinStock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, tinStock.getGradeName());
        values.put(KEY_TINS, tinStock.getTins());
        db.insert(TABLE_KERNALSTOCK, null, values);
        db.close();
    }

    public void addLastStoredDate(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);
        db.insert(TABLE_KERNALSTOCKLASTSTOREDDATE, null, values);
        db.close();
    }



    public List<TinStock> getAllStocks() {
        List<TinStock> productList = new ArrayList<TinStock>();
        String selectQuery = "SELECT  * FROM " + TABLE_KERNALSTOCK;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                TinStock tinStock = new TinStock();
                tinStock.setGradeName(cursor.getString(0));
                tinStock.setTins(Integer.parseInt(cursor.getString(1)));
                productList.add(tinStock);
            } while (cursor.moveToNext());
        }
        return productList;
    }

    public String getLastStoredDate(){
        String selectQuery = "SELECT  * FROM " + TABLE_KERNALSTOCKLASTSTOREDDATE;
        String date = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                date = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return date;
    }

    public void deleteStock(TinStock tinStock) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_KERNALSTOCK, KEY_ID + " = ?",
                new String[] { String.valueOf(tinStock.getGradeName()) });
        db.close();
    }

    public void deleteDate(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_KERNALSTOCKLASTSTOREDDATE, KEY_DATE + " = ?",
                new String[] { String.valueOf(date) });
        db.close();
    }

    public int getKernalStockCount() {
        String countQuery = "SELECT  * FROM " + TABLE_KERNALSTOCK;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void ClearAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+TABLE_KERNALSTOCK);
        db.execSQL("delete from "+TABLE_KERNALSTOCKLASTSTOREDDATE);
        db.close();
    }


}
