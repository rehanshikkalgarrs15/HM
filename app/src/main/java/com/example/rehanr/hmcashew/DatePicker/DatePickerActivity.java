package com.example.rehanr.hmcashew.DatePicker;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;

import com.example.rehanr.hmcashew.Activities.BaseActivity;
import com.example.rehanr.hmcashew.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerActivity extends BaseActivity {


    int year,month, date;
    TextView dateTv;

    public static final String DATE = "date";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    enum Months {JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        Calendar calendar = Calendar.getInstance();

        year = getIntent().getIntExtra(YEAR, calendar.get(Calendar.YEAR));
        month = getIntent().getIntExtra(MONTH,calendar.get(Calendar.MONTH));
        date = getIntent().getIntExtra(DATE, calendar.get(Calendar.DATE));

        Calendar dateCalender = Calendar.getInstance();

        Log.e("minimum date", new SimpleDateFormat("dd MMM yyyy hh:mm:ss a", Locale.getDefault()).format(dateCalender.getTimeInMillis()));

        DatePickerDialog datePickerDialog = new DatePickerDialog(DatePickerActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.e("Date Selected", dayOfMonth + " " + monthOfYear + " " + year);

            }
        },year,month,date);
        datePickerDialog.setCanceledOnTouchOutside(true);
        datePickerDialog.getDatePicker().setMinDate(dateCalender.getTimeInMillis());
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        datePickerDialog.show();

    }
}
