package com.example.rehanr.hmcashew.Fragments;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rehanr.hmcashew.Activities.BaseActivity;
import com.example.rehanr.hmcashew.Adapters.KernalStockAdapter;
import com.example.rehanr.hmcashew.Adapters.RcnStockAdapter;
import com.example.rehanr.hmcashew.Database.KernalStockDBHandler;
import com.example.rehanr.hmcashew.Database.RCNStockDBHandler;
import com.example.rehanr.hmcashew.Interfaces.AlertMagnatic;
import com.example.rehanr.hmcashew.Models.FactoryReport;
import com.example.rehanr.hmcashew.Models.RcnStock;
import com.example.rehanr.hmcashew.Models.TinStock;
import com.example.rehanr.hmcashew.Parsers.KernalStockParser;
import com.example.rehanr.hmcashew.Parsers.RcnStockParser;
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

import static com.example.rehanr.hmcashew.Services.AlertDialogGeneric.getConfirmDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class RCNStockFragment extends BaseActivity {


    TextView navigationItemNameTV,dateTV,totalbagsTV,totalquantityTV;
    ImageView dateIV;
    Toolbar toolbar;
    RelativeLayout progressLayoutRL,poorConnectionLayoutRL;
    LinearLayout totalrcnstockLayoutLL;

    private List<RcnStock> rcnStocks = new ArrayList<>();
    private RecyclerView recyclerView;
    private RcnStockAdapter mAdapter;

    //for date picker
    Calendar calendar ;
    DatePickerDialog datePickerDialog ;
    int Year, Month, Day ;
    String selectedDATE = "";


    RCNStockDBHandler databaseHandler = new RCNStockDBHandler(RCNStockFragment.this);

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rcnrates);

        applyFont(RCNStockFragment.this,findViewById(R.id.baselayout));

        toolbar = (Toolbar)findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        navigationItemNameTV = (TextView)findViewById(R.id.navigationtitle);
        progressLayoutRL = (RelativeLayout)findViewById(R.id.progresslayout);
        poorConnectionLayoutRL = (RelativeLayout)findViewById(R.id.retrylayout);
        totalrcnstockLayoutLL = (LinearLayout) findViewById(R.id.totalrcnstocklayout);
        dateTV = (TextView)findViewById(R.id.textdate);
        dateIV = (ImageView)findViewById(R.id.imagedate);
        totalbagsTV = (TextView)findViewById(R.id.totalbags);
        totalquantityTV = (TextView)findViewById(R.id.totalquantity);

        //display the backbutton on toolbar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set toolbar name
        navigationItemNameTV.setText("RCN Stock");


        //for date picker dialog
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCNStockOnDateChange();
            }
        });

        dateIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RCNStockOnDateChange();
            }
        });


        //on retry clicked
        poorConnectionLayoutRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRCNStock(selectedDATE);
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
        totalrcnstockLayoutLL.setVisibility(View.GONE);
        progressLayoutRL.setVisibility(View.GONE);
        poorConnectionLayoutRL.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        if (!NetworkUtils.isConnected(RCNStockFragment.this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Load Stored Data!!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //handling if data is locally stored or not
                            displaydataonLayout();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else if (code  == 0){
            //by default load todays RCN stock
            loadTodaysRCNStock();
        }
        else if (code  == 1){
            //load on date changed
            loadRCNStock(selectDATE);
        }
    }


    private void loadTodaysRCNStock() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todaysDate = df.format(c.getTime());
        Log.e("Todays Date",todaysDate);
        selectedDATE = todaysDate;
        loadRCNStock(todaysDate);
    }


    //handling when date is changed
    private void RCNStockOnDateChange() {


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

        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }


    //loads data when date is changed
    private void loadRCNStock(final String selectedDate) {
        new AsyncTask<Void, Void, JSONObject>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressLayoutRL.setVisibility(View.VISIBLE);
                poorConnectionLayoutRL.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                totalrcnstockLayoutLL.setVisibility(View.GONE);
                //change date in toolbar
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
                dateTV.setText(dateString);
            }

            @Override
            protected JSONObject doInBackground(Void... params) {
                return new ServerRequests().loadRCNStock(selectedDate);
            }

            @Override
            protected void onPostExecute(JSONObject responseObject) {
                super.onPostExecute(responseObject);

                //handle json reposne object
                List<RcnStock> list = new ArrayList<>();
                //make this response ad lastfetched data based on date
                String lastfetcheddate = "";
                try {
                    if (responseObject != null) {
                        if (responseObject.getString("status").equals("success")) {
                            list = new RcnStockParser().parse(responseObject.getJSONArray("result"));
                            lastfetcheddate = responseObject.getString("date");
                            displayDataNetConnectionOK(list, lastfetcheddate);
                        }
                        else if (responseObject.getString("status").equals("failed")){
                            recyclerView.setVisibility(View.GONE);
                            poorConnectionLayoutRL.setVisibility(View.VISIBLE);
                            totalrcnstockLayoutLL.setVisibility(View.GONE);
                            getConfirmDialog(RCNStockFragment.this,getString(R.string.emptylist), getString(R.string.loadstoreddata), getString(R.string.yes), getString(R.string.no), false,
                                    new AlertMagnatic() {

                                        @Override
                                        public void PositiveMethod(final DialogInterface dialog, final int id) {
                                             displaydataonLayout();
                                        }

                                        @Override
                                        public void NegativeMethod(DialogInterface dialog, int id) {
                                            recyclerView.setVisibility(View.GONE);
                                            poorConnectionLayoutRL.setVisibility(View.VISIBLE);
                                            totalrcnstockLayoutLL.setVisibility(View.GONE);
                                            progressLayoutRL.setVisibility(View.GONE);
                                        }
                                    });
                        }
                        else if (responseObject.getString("status").equals("datechanged")){
                            list = new RcnStockParser().parse(responseObject.getJSONArray("result"));
                            lastfetcheddate = responseObject.getString("date");
                            final List<RcnStock> finalList = list;
                            final String finalLastfetcheddate = lastfetcheddate;
                            getConfirmDialog(RCNStockFragment.this,getString(R.string.nodataonselecteddate), getString(R.string.loadrecentdata), getString(R.string.yes), getString(R.string.no), false,
                                    new AlertMagnatic() {

                                        @Override
                                        public void PositiveMethod(DialogInterface dialog, int id) {
                                            displayDataNetConnectionOK(finalList, finalLastfetcheddate);
                                            dateTV.setText("Rcn Stock As on\n\t\t"+changeDateFormat(finalLastfetcheddate));
                                        }

                                        @Override
                                        public void NegativeMethod(DialogInterface dialog, int id) {
                                            recyclerView.setVisibility(View.GONE);
                                            poorConnectionLayoutRL.setVisibility(View.VISIBLE);
                                            progressLayoutRL.setVisibility(View.GONE);
                                        }
                                    });
                        }

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
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


    public void displaydataonLayout(){

        //handling ok button on click
        if (databaseHandler.getRCNStockCount() <= 0) {
            Toast.makeText(RCNStockFragment.this, "No Data was Stored..", Toast.LENGTH_LONG).show();
            recyclerView.setVisibility(View.GONE);
            poorConnectionLayoutRL.setVisibility(View.VISIBLE);
            progressLayoutRL.setVisibility(View.GONE);
        } else {
            //if data is stored in sqlite
            List<RcnStock> rcnStockList = new ArrayList<RcnStock>();
            rcnStockList = databaseHandler.getAllStocks();
            if (rcnStockList.size() <= 0) {
                progressLayoutRL.setVisibility(View.GONE);
                poorConnectionLayoutRL.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                totalrcnstockLayoutLL.setVisibility(View.GONE);
                Toast.makeText(RCNStockFragment.this, "No Data was Stored..", Toast.LENGTH_LONG).show();
            } else {
                progressLayoutRL.setVisibility(View.GONE);
                poorConnectionLayoutRL.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                totalrcnstockLayoutLL.setVisibility(View.VISIBLE);
                mAdapter = new RcnStockAdapter(rcnStockList, RCNStockFragment.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RCNStockFragment.this.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                //count total tins
                int totalbags = 0,totalquantity = 0;
                for (RcnStock rcnStock : rcnStockList) {
                    totalbags+=rcnStock.getBag();
                    totalquantity+=rcnStock.getQuantity();
                }
                totalbagsTV.setText(String.valueOf(totalbags));
                totalquantityTV.setText(String.valueOf(totalquantity));

                String laststoredDate = databaseHandler.getLastStoredDate();
                dateTV.setText("RCN Stock As on \n\t\t" + changeDateFormat(laststoredDate));

            }
        }
    }



    private void displayDataNetConnectionOK(List<RcnStock> list, String lastfetcheddate) {
        progressLayoutRL.setVisibility(View.GONE);
        Log.e("list",list.toString());
        mAdapter = new RcnStockAdapter(list,RCNStockFragment.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RCNStockFragment.this.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        if (list.size() == 0){
            recyclerView.setVisibility(View.GONE);
            poorConnectionLayoutRL.setVisibility(View.VISIBLE);
            totalrcnstockLayoutLL.setVisibility(View.GONE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            poorConnectionLayoutRL.setVisibility(View.GONE);
            totalrcnstockLayoutLL.setVisibility(View.VISIBLE);

            //total all tins
            int totalbags = 0, totalquantity = 0;
            for (RcnStock stockList : list) {
                totalbags += stockList.getBag();
                totalquantity += stockList.getQuantity();
            }
            totalbagsTV.setText(String.valueOf(totalbags));
            totalquantityTV.setText(String.valueOf(totalquantity));


            //store the LISTOFRCNSTOCK in sqlite DB,
                        /*
                            * first clearAll the previously stored data from the table
                            * add all the new fetched items to the table 1
                            * add the date to table 2
                         */

            //1 - clear all the table1,2 data
            databaseHandler.ClearAll();

            //2 - store new fetched data in the table1
            for (RcnStock stock : list) {
                databaseHandler.addStock(stock);
            }

            //3 - adding date to table 2
            Log.e("Last fetched date", lastfetcheddate);
            databaseHandler.addLastStoredDate(lastfetcheddate);
        }
    }
}
