package com.example.rehanr.hmcashew.Fragments;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.example.rehanr.hmcashew.Activities.BaseActivity;
import com.example.rehanr.hmcashew.Adapters.KernalStockAdapter;
import com.example.rehanr.hmcashew.Database.KernalStockDBHandler;
import com.example.rehanr.hmcashew.Models.TinStock;
import com.example.rehanr.hmcashew.Parsers.KernalStockParser;
import com.example.rehanr.hmcashew.R;
import com.example.rehanr.hmcashew.Serverutils.ServerRequests;
import com.example.rehanr.hmcashew.Utils.NetworkUtils;

import org.json.JSONObject;

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
    String selectedDATE = "";


    KernalStockDBHandler databaseHandler = new KernalStockDBHandler(KernalStockFragment.this);

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


        //on retry clicked
        poorConnectionLayoutRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadKernalStock(selectedDATE);
            }
        });

        //call this function every time when data is changed 0 - default, 1- changed date
        checkInternetConnectionAndLoadData("",0);
    }

    private void checkInternetConnectionAndLoadData(String selectDATE,int code) {

        /*check the internet connection
         * if NOT CONNECTED then load stored data
         * else load fetched data
         */
        totaltinsLayoutLL.setVisibility(View.GONE);
        progressLayoutRL.setVisibility(View.GONE);
        poorConnectionLayoutRL.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        totaltinsLayoutLL.setVisibility(View.GONE);
        if (!NetworkUtils.isConnected(KernalStockFragment.this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Loading Previous Data!!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //handling ok button on click
                            if (databaseHandler.getKernalStockCount() <= 0){
                                Toast.makeText(KernalStockFragment.this,"No Data was Stored..",Toast.LENGTH_LONG).show();
                            }
                            else{
                                //if data is stored in sqlite
                                List<TinStock> tinStocks = new ArrayList<TinStock>();
                                tinStocks = databaseHandler.getAllStocks();
                                if (tinStocks.size() == 0){
                                    progressLayoutRL.setVisibility(View.GONE);
                                    poorConnectionLayoutRL.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    totaltinsLayoutLL.setVisibility(View.GONE);
                                    Toast.makeText(KernalStockFragment.this,"No Data was Stored..",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    progressLayoutRL.setVisibility(View.GONE);
                                    poorConnectionLayoutRL.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    totaltinsLayoutLL.setVisibility(View.VISIBLE);
                                    mAdapter = new KernalStockAdapter(tinStocks, KernalStockFragment.this);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(KernalStockFragment.this.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();

                                    //count total tins
                                    int totalTins = 0;
                                    for (TinStock tinStock : tinStocks){
                                        totalTins+=tinStock.getTins();
                                    }
                                    totalTinsTV.setText(String.valueOf(totalTins)+" Tins");

                                    String laststoredDate = databaseHandler.getLastStoredDate();
                                    dateTV.setText("Kernal Stock As on \n\t\t" + changeDateFormat(laststoredDate));
                                }
                            }
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else if (code  == 0){
            //by default load todays Kernal stock
            loadTodaysKernalStock();
        }
        else if (code  == 1){
            //load on date changed
            loadKernalStock(selectDATE);
        }
    }

    private void loadTodaysKernalStock() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todaysDate = df.format(c.getTime());
        Log.e("Todays Date",todaysDate);
        selectedDATE = todaysDate;
        loadKernalStock(todaysDate);
    }


    //handling when date is changed
    private void KernalStockOnDateChange() {


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Log.e("selected date",dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        String selectedDate = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        selectedDATE = selectedDate;
                        checkInternetConnectionAndLoadData(selectedDATE,1);
                    }
                }, Year, Month, Day);
        datePickerDialog.show();
    }


    //loads data when date is changed
    private void loadKernalStock(final String selectedDate) {
            new AsyncTask<Void, Void, JSONObject>(){

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressLayoutRL.setVisibility(View.VISIBLE);
                    poorConnectionLayoutRL.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    totaltinsLayoutLL.setVisibility(View.GONE);
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
                protected JSONObject doInBackground(Void... params) {
                    return new ServerRequests().loadKernalStock(selectedDate);
                }

                @Override
                protected void onPostExecute(JSONObject responseObject) {
                    super.onPostExecute(responseObject);

                    //handle json reposne object

                    List<TinStock> list = new ArrayList<>();
                    //make this response ad lastfetched data based on date
                    String lastfetcheddate = "";
                    try {
                        if (responseObject != null) {
                            if (responseObject.getString("status").equals("success")) {
                                list = new KernalStockParser().parse(responseObject.getJSONArray("result"));
                                lastfetcheddate = responseObject.getString("date");
                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

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


                        //store the LISTOFKERNALSTOCK in sqlite DB,
                        /*
                            * first clearAll the previously stored data from the table
                            * add all the new fetched items to the table 1
                            * add the date to table 2
                         */

                        //1 - clear all the table1,2 data
                        databaseHandler.ClearAll();

                        //2 - store new fetched data in the table1
                        for (TinStock stock : list){
                            databaseHandler.addStock(stock);
                        }

                        //3 - adding date to table 2
                        Log.e("Last fetched date",lastfetcheddate);
                        databaseHandler.addLastStoredDate(lastfetcheddate);
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

    public String changeDateFormat(String selectedDate){
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-DD");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMMM-yyyy");
        Date date = null;
        try {
            date = format1.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateString = format2.format(date);
        dateString = dateString.replace("-", " ");
        return dateString;
    }
}
