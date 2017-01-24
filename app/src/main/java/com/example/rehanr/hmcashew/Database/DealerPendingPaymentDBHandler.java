package com.example.rehanr.hmcashew.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rehanr.hmcashew.Models.DealerPendingPayment;
import com.example.rehanr.hmcashew.Models.RcnStock;
import com.example.rehanr.hmcashew.Models.TinStock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rehan r on 23-01-2017.
 */
public class DealerPendingPaymentDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cashewdb4";
    private static final String TABLE_DEALER_PENDING_PAYMENT = "pendingpayment";
    private static final String TABLE_DEALER_PENDING_PAYMENT_LASTSTOREDDATE = "pendingpaymentlaststoreddate";

    // product Table Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_AMOUNT = "amount";

    private static final String KEY_DATE = "date";


    public DealerPendingPaymentDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_DEALER_PENDING_PAYMENT + "("
                + KEY_NAME + " TEXT," + KEY_AMOUNT + " REAL" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        String CREATE_STOREDATE_TABLE = "CREATE TABLE " + TABLE_DEALER_PENDING_PAYMENT_LASTSTOREDDATE + "("
                + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_STOREDATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEALER_PENDING_PAYMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEALER_PENDING_PAYMENT_LASTSTOREDDATE);
        onCreate(db);
    }


    public void addDealerPendingPayment(DealerPendingPayment pendingPayment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pendingPayment.getDealerName());
        values.put(KEY_AMOUNT, pendingPayment.getPendingAmount());
        db.insert(TABLE_DEALER_PENDING_PAYMENT, null, values);
        db.close();
    }

    public void addLastStoredDate(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);
        db.insert(TABLE_DEALER_PENDING_PAYMENT_LASTSTOREDDATE, null, values);
        db.close();
    }


    public List<DealerPendingPayment> getAllPendingPayments() {
        List<DealerPendingPayment> dealerPendingPaymentList = new ArrayList<DealerPendingPayment>();
        String selectQuery = "SELECT  * FROM " + TABLE_DEALER_PENDING_PAYMENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DealerPendingPayment dealerPendingPayment = new DealerPendingPayment();
                dealerPendingPayment.setDealerName(cursor.getString(0));
                dealerPendingPayment.setPendingAmount(Integer.parseInt(cursor.getString(1)));
                dealerPendingPaymentList.add(dealerPendingPayment);
            } while (cursor.moveToNext());
        }
        return dealerPendingPaymentList;
    }

    public String getLastStoredDate(){
        String selectQuery = "SELECT  * FROM " + TABLE_DEALER_PENDING_PAYMENT_LASTSTOREDDATE;
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



    public void deletePendingPayment(DealerPendingPayment dealerPendingPayment) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DEALER_PENDING_PAYMENT, KEY_NAME + " = ?",
                new String[] { String.valueOf(dealerPendingPayment.getDealerName()) });
        db.close();
    }

    public void deleteDate(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DEALER_PENDING_PAYMENT_LASTSTOREDDATE, KEY_DATE + " = ?",
                new String[] { String.valueOf(date) });
        db.close();
    }

    public int getDealerPendingPaymentCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DEALER_PENDING_PAYMENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void ClearAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+TABLE_DEALER_PENDING_PAYMENT);
        db.execSQL("delete from "+TABLE_DEALER_PENDING_PAYMENT_LASTSTOREDDATE);
        db.close();
    }
}
