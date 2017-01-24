package com.example.rehanr.hmcashew.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rehanr.hmcashew.Models.RcnStock;
import com.example.rehanr.hmcashew.Models.TinStock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rehan r on 23-01-2017.
 */
public class RCNStockDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cashewdb3";
    private static final String TABLE_RCNSTOCK = "rcnstock";
    private static final String TABLE_RCNSTOCKLASTSTOREDDATE = "kernalstockstoreddate";

    // product Table Columns names
    private static final String KEY_TAG = "tagname";
    private static final String KEY_BAGS = "bag";
    private static final String KEY_QTY = "qty";

    private static final String KEY_DATE = "date";



    public RCNStockDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_RCNSTOCK + "("
                + KEY_TAG + " TEXT," + KEY_BAGS + " REAL," +  KEY_QTY + " REAL" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        String CREATE_STOREDATE_TABLE = "CREATE TABLE " + TABLE_RCNSTOCKLASTSTOREDDATE + "("
                + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_STOREDATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RCNSTOCK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RCNSTOCKLASTSTOREDDATE);
        onCreate(db);
    }


    public void addStock(RcnStock rcnStock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TAG, rcnStock.getTag());
        values.put(KEY_BAGS, rcnStock.getBag());
        values.put(KEY_QTY, rcnStock.getQuantity());
        db.insert(TABLE_RCNSTOCK, null, values);
        db.close();
    }

    public void addLastStoredDate(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);
        db.insert(TABLE_RCNSTOCKLASTSTOREDDATE, null, values);
        db.close();
    }


    public List<RcnStock> getAllStocks() {
        List<RcnStock> rcnStockList = new ArrayList<RcnStock>();
        String selectQuery = "SELECT  * FROM " + TABLE_RCNSTOCK;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                RcnStock rcnStock = new RcnStock();
                rcnStock.setTag(cursor.getString(0));
                rcnStock.setBag(Integer.parseInt(cursor.getString(1)));
                rcnStock.setQuantity(Integer.parseInt(cursor.getString(2)));
                rcnStockList.add(rcnStock);
            } while (cursor.moveToNext());
        }
        return rcnStockList;
    }

    public String getLastStoredDate(){
        String selectQuery = "SELECT  * FROM " + TABLE_RCNSTOCKLASTSTOREDDATE;
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
        db.delete(TABLE_RCNSTOCK, KEY_TAG + " = ?",
                new String[] { String.valueOf(tinStock.getGradeName()) });
        db.close();
    }

    public void deleteDate(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RCNSTOCKLASTSTOREDDATE, KEY_DATE + " = ?",
                new String[] { String.valueOf(date) });
        db.close();
    }

    public int getRCNStockCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RCNSTOCK;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void ClearAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+TABLE_RCNSTOCK);
        db.execSQL("delete from "+TABLE_RCNSTOCKLASTSTOREDDATE);
        db.close();
    }
}
