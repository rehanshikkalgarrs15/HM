package com.example.rehanr.hmcashew.Fragments;


import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rehanr.hmcashew.Activities.BaseActivity;
import com.example.rehanr.hmcashew.Adapters.KernalStockAdapter;
import com.example.rehanr.hmcashew.Models.TinStock;
import com.example.rehanr.hmcashew.R;
import com.example.rehanr.hmcashew.Serverutils.ServerRequests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class KernalStockFragment extends BaseActivity implements DatePickerDialog.OnDateSetListener{

    TextView navigationItemNameTV,dateTV,totalTinsTV;
    ImageView dateIV;
    Toolbar toolbar;
    RelativeLayout progressLayoutRL,poorConnectionLayoutRL, totaltinsLayoutLL;

    private List<TinStock> tinStockList = new ArrayList<>();
    private RecyclerView recyclerView;
    private KernalStockAdapter mAdapter;

    //for date picker

    Calendar calendar ;
    DatePickerDialog datePickerDialog ;
    int Year, Month, Day ;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_kernal_stock);
        applyFont(KernalStockFragment.this,findViewById(R.id.baselayout));
        //set the toolbar to Activity
        toolbar = (Toolbar)findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        navigationItemNameTV = (TextView)findViewById(R.id.navigationtitle);
        progressLayoutRL = (RelativeLayout)findViewById(R.id.progresslayout);
        poorConnectionLayoutRL = (RelativeLayout)findViewById(R.id.retrylayout);
        totaltinsLayoutLL = (RelativeLayout) findViewById(R.id.totalkernalstocktinslayout);
        dateTV = (TextView)findViewById(R.id.textdate);
        dateIV = (ImageView)findViewById(R.id.imagedate);
        totalTinsTV = (TextView)findViewById(R.id.totalkernalstocktins);

        //display the backbutton on toolbar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set toolbar name
        navigationItemNameTV.setText("Kernal Stock");


        //for date picker dialog
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KernalStockOnDateChange();
            }
        });

        dateIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KernalStockOnDateChange();
            }
        });

        //by default load todays Kernal stock
        loadTodaysKernalStock();
    }

    private void loadTodaysKernalStock() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todaysDate = df.format(c.getTime());
        Log.e("Todays Date",todaysDate);
        loadKernalStock(todaysDate);
    }


    //handling when date is changed
    private void KernalStockOnDateChange() {


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Log.e("selected date",dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        String selectedDate = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        loadKernalStock(selectedDate);
                    }
                }, Year, Month, Day);
        datePickerDialog.show();
    }


    //loads data when date is changed
    private void loadKernalStock(final String selectedDate) {
            new AsyncTask<Void, Void, List<TinStock>>(){

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressLayoutRL.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    //change date in toolbar
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-DD");
                    SimpleDateFormat format2 = new SimpleDateFormat("dd-MMMM-yyyy");
                    Date date = null;
                    try {
                        date = format1.parse(selectedDate);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String dateString = format2.format(date);
                    dateString = dateString.replace("-", " ");
                    dateTV.setText(dateString);
                }

                @Override
                protected List<TinStock> doInBackground(Void... params) {
                    return new ServerRequests().loadKernalStock(selectedDate);
                }

                @Override
                protected void onPostExecute(List<TinStock> list) {
                    super.onPostExecute(list);
                    progressLayoutRL.setVisibility(View.GONE);
                    Log.e("list",list.toString());
                    mAdapter = new KernalStockAdapter(list,KernalStockFragment.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(KernalStockFragment.this.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                    if (list.size() == 0){
                        recyclerView.setVisibility(View.GONE);
                        poorConnectionLayoutRL.setVisibility(View.VISIBLE);
                        totaltinsLayoutLL.setVisibility(View.GONE);
                    }
                    else{
                        recyclerView.setVisibility(View.VISIBLE);
                        poorConnectionLayoutRL.setVisibility(View.GONE);
                        totaltinsLayoutLL.setVisibility(View.VISIBLE);

                        //total all tins
                        int totalTins = 0;
                        for (TinStock stockList : list){
                            totalTins+=stockList.getTins();
                        }
                        totalTinsTV.setText(String.valueOf(totalTins)+" Tins");

                    }
                }
            }.execute();

    }

    //finish the activity when toobar back button is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }
}
