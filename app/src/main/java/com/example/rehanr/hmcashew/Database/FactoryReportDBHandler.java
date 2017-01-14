package com.example.rehanr.hmcashew.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rehanr.hmcashew.Models.FactoryReport;
import com.example.rehanr.hmcashew.Models.TinStock;

/**
 * Created by rehan r on 14-01-2017.
 */
public class FactoryReportDBHandler  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cashewdb1";
    private static final String TABLE_FATORYREPORT = "factoryreport";
    private static final String TABLE_FACTORYREPORTLASTSTOREDDATE = "factoryreportstoreddate";

    // fatory Table Columns names
    private static final String KEY_EMP_CUTTING = "emp_cutting";
    private static final String KEY_EMP_GRADING = "emp_grading";
    private static final String KEY_EMP_STAFF = "emp_staff";

    private static final String KEY_BOILING = "boiling";

    private static final String KEY_CUTTING_GAR = "cutting_gar";
    private static final String KEY_CUTTING_TUKDA = "cutting_tukda";
    private static final String KEY_CUTTING_AVERAGE_EMP = "cutting_avg_emp";
    private static final String KEY_CUTTING_COSTPERKG = "cutting_costperkg";


    private static final String KEY_GRADING_GAR = "grading_gar";
    private static final String KEY_GRADING_TUKDA = "grading_tukda";
    private static final String KEY_GRADING_AVERAGE_EMP = "grading_avg_emp";
    private static final String KEY_GRADING_COSTPERKG = "grading_costperkg";


    private static final String KEY_PACKED_OPSTOCK = "packed_opstock";
    private static final String KEY_PACKED_NEW_PACKED = "packed_newpacked";
    private static final String KEY_PACKED_SALE = "packed_sale";
    private static final String KEY_PACKED_CLOSE = "packed_close";

    private static final String KEY_RCN_RAWCASHEW = "rcn_rawcashew";

    //for 2nd table
    private static final String KEY_DATE = "date";



    public FactoryReportDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FACTORY_REPORT_TABLE = "CREATE TABLE " + TABLE_FATORYREPORT + "("
                + KEY_EMP_CUTTING + " REAL," + KEY_EMP_GRADING + " REAL," +
                KEY_EMP_STAFF + " REAL," +KEY_BOILING + " REAL," +
                KEY_CUTTING_GAR + " REAL," +KEY_CUTTING_TUKDA + " REAL," +
                KEY_CUTTING_AVERAGE_EMP + " REAL," +KEY_CUTTING_COSTPERKG + " REAL," +
                KEY_GRADING_GAR + " REAL," +KEY_GRADING_TUKDA + " REAL," +
                KEY_GRADING_AVERAGE_EMP + " REAL," +KEY_GRADING_COSTPERKG + " REAL," +
                KEY_PACKED_OPSTOCK + " REAL," +KEY_PACKED_NEW_PACKED + " REAL," +
                KEY_PACKED_SALE + " REAL," +KEY_PACKED_CLOSE + " REAL," +
                KEY_RCN_RAWCASHEW + " REAL" + ")";
        db.execSQL(CREATE_FACTORY_REPORT_TABLE);

        String CREATE_STOREDATE_TABLE = "CREATE TABLE " + TABLE_FACTORYREPORTLASTSTOREDDATE + "("
                + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_STOREDATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FATORYREPORT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACTORYREPORTLASTSTOREDDATE);
        onCreate(db);
    }

    public void addFactoryReport(FactoryReport factoryReport) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_EMP_CUTTING, factoryReport.getEmployeeCutting());
        values.put(KEY_EMP_GRADING, factoryReport.getEmployeeGrading() );
        values.put(KEY_EMP_STAFF, factoryReport.getEmployeeStaff() );
        values.put(KEY_BOILING, factoryReport.getBoling());
        values.put(KEY_CUTTING_GAR, factoryReport.getCuttingGar());
        values.put(KEY_CUTTING_TUKDA, factoryReport.getCuttingTukda());
        values.put(KEY_CUTTING_AVERAGE_EMP, factoryReport.getCuttingAveragePerEmployee());
        values.put(KEY_CUTTING_COSTPERKG, factoryReport.getCuttingCostPerKG());
        values.put(KEY_GRADING_GAR, factoryReport.getGradingGar());
        values.put(KEY_GRADING_TUKDA, factoryReport.getGradingTukda());
        values.put(KEY_GRADING_AVERAGE_EMP, factoryReport.getGradingAveragePerEmployee());
        values.put(KEY_GRADING_COSTPERKG, factoryReport.getGradingCostPerKG());
        values.put(KEY_PACKED_OPSTOCK, factoryReport.getPackedTinsOpStock());
        values.put(KEY_PACKED_NEW_PACKED, factoryReport.getPackedTinsNewPacked());
        values.put(KEY_PACKED_SALE, factoryReport.getPackedTinsSale());
        values.put(KEY_PACKED_CLOSE, factoryReport.getPackedTinsCloseStock());
        values.put(KEY_RCN_RAWCASHEW, factoryReport.getRcnRawCashew());

        db.insert(TABLE_FATORYREPORT, null, values);
        db.close();
    }

    public void addLastStoredDate(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);
        db.insert(TABLE_FACTORYREPORTLASTSTOREDDATE, null, values);
        db.close();
    }

    public FactoryReport getFactoryReport(){
        String selectQuery = "SELECT  * FROM " + TABLE_FATORYREPORT;
        String employeeCutting,employeeGrading,employeeStaff,employeeTotal,
                boiling,
                cuttingGar,cuttingTukda,cuttingTotal,cuttingTukdaPercentage,cuttingAveragePerEmployee,cuttingCostPerKG,
                gradingGar,gradingTukda,gradingTotal,gradingAveragePerEmployee,gradingCostPerKG,
                packedTinsOpStock,packedTinsNewPacked,packedTinsSale,packedTinsCloseStock,
                rcnRawCashew;
        SQLiteDatabase db = this.getWritableDatabase();
        FactoryReport factoryReport = new FactoryReport();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                employeeCutting = cursor.getString(0);
                employeeGrading = cursor.getString(1);
                employeeStaff = cursor.getString(2);

                boiling = cursor.getString(3);

                cuttingGar = cursor.getString(4);
                cuttingTukda = cursor.getString(5);
                cuttingAveragePerEmployee = cursor.getString(6);
                cuttingCostPerKG = cursor.getString(7);


                gradingGar = cursor.getString(8);
                gradingTukda = cursor.getString(9);
                gradingAveragePerEmployee = cursor.getString(10);
                gradingCostPerKG = cursor.getString(11);


                packedTinsOpStock = cursor.getString(12);
                packedTinsNewPacked = cursor.getString(13);
                packedTinsSale = cursor.getString(14);
                packedTinsCloseStock = cursor.getString(15);

                rcnRawCashew = cursor.getString(16);

                //creating factoryreport object and add values using getters and setters
                factoryReport.setEmployeeCutting(employeeCutting);
                factoryReport.setEmployeeGrading(employeeGrading);
                factoryReport.setEmployeeStaff(employeeStaff);
                factoryReport.setEmployeeTotal(String.valueOf(Double.parseDouble(employeeCutting) + Double.parseDouble(employeeGrading) + Double.parseDouble(employeeStaff)));

                factoryReport.setBoling(boiling);

                factoryReport.setCuttingGar(cuttingGar);
                factoryReport.setCuttingTukda(cuttingTukda);
                factoryReport.setCuttingTotal(String.valueOf(Double.parseDouble(cuttingGar) + Double.parseDouble(cuttingTukda)));
                factoryReport.setCuttingTukdaPercentage(String.valueOf((Double.parseDouble(cuttingTukda) / Double.parseDouble (cuttingGar) * 100)));
                factoryReport.setCuttingAveragePerEmployee(cuttingAveragePerEmployee);
                factoryReport.setCuttingCostPerKG(cuttingCostPerKG);

                factoryReport.setGradingGar(gradingGar);
                factoryReport.setGradingTukda(gradingTukda);
                factoryReport.setGradingTotal(String.valueOf(Double.parseDouble(gradingGar) + Double.parseDouble(gradingTukda)));
                factoryReport.setGradingAveragePerEmployee(gradingAveragePerEmployee);
                factoryReport.setGradingCostPerKG(gradingCostPerKG);

                factoryReport.setPackedTinsOpStock(packedTinsOpStock);
                factoryReport.setPackedTinsNewPacked(packedTinsNewPacked);
                factoryReport.setPackedTinsSale(packedTinsSale);
                factoryReport.setPackedTinsCloseStock(packedTinsCloseStock);

                factoryReport.setRcnRawCashew(rcnRawCashew);



            } while (cursor.moveToNext());
        }
        return factoryReport;
    }


    public String getLastStoredDate(){
        String selectQuery = "SELECT  * FROM " + TABLE_FACTORYREPORTLASTSTOREDDATE;
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


    public void ClearAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+TABLE_FATORYREPORT);
        db.execSQL("delete from "+TABLE_FACTORYREPORTLASTSTOREDDATE);
        db.close();
    }

    public int getFactoryReportCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FATORYREPORT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
