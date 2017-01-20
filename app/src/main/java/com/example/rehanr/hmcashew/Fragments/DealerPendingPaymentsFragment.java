package com.example.rehanr.hmcashew.Fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.rehanr.hmcashew.Activities.BaseActivity;
import com.example.rehanr.hmcashew.Activities.DealerPendingPaymentActivity;
import com.example.rehanr.hmcashew.Adapters.AllDealerPendingPaymentAdapter;
import com.example.rehanr.hmcashew.Adapters.KernalStockAdapter;
import com.example.rehanr.hmcashew.Models.DealerPendingPayment;
import com.example.rehanr.hmcashew.Models.TinStock;
import com.example.rehanr.hmcashew.Parsers.AllDealerPendingPaymentParser;
import com.example.rehanr.hmcashew.Parsers.KernalStockParser;
import com.example.rehanr.hmcashew.R;
import com.example.rehanr.hmcashew.Serverutils.ServerRequests;
import com.example.rehanr.hmcashew.Utils.NetworkUtils;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DealerPendingPaymentsFragment extends BaseActivity {

    TextView navigationItemNameTV,dateTV,totalpendingAmountTV;
    ImageView dateIV;
    Toolbar toolbar;
    RelativeLayout progressLayoutRL,poorConnectionLayoutRL,totalpendingpaymentlayout;
    RecyclerView recyclerView;
    private AllDealerPendingPaymentAdapter mAdapter;


    //for date picker
    Calendar calendar ;
    DatePickerDialog datePickerDialog ;
    int Year, Month, Day ;
    String selectedDATE = "";
    List<DealerPendingPayment> pendingPaymentList = new ArrayList<>();

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dealer_pending_payments);
        applyFont(DealerPendingPaymentsFragment.this,findViewById(R.id.baselayout));
        //set the toolbar to Activity
        toolbar = (Toolbar)findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        navigationItemNameTV = (TextView)findViewById(R.id.navigationtitle);
        progressLayoutRL = (RelativeLayout)findViewById(R.id.progresslayout);
        poorConnectionLayoutRL = (RelativeLayout)findViewById(R.id.retrylayout);
        totalpendingpaymentlayout = (RelativeLayout)findViewById(R.id.totalpendingpaymentlayout);
        dateTV = (TextView)findViewById(R.id.textdate);
        dateIV = (ImageView)findViewById(R.id.imagedate);
        totalpendingAmountTV = (TextView)findViewById(R.id.totalpendingpaymentamount);

        //display the backbutton on toolbar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set toolbar name
        navigationItemNameTV.setText("Pending Payment");


        //for date picker dialog
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DealerPendingPaymentOnDateChange();
            }
        });

        dateIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DealerPendingPaymentOnDateChange();
            }
        });

        //on retry clicked
        poorConnectionLayoutRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDealerPendingPayments(selectedDATE);
            }
        });

        //call this function every time when data is changed 0 - default, 1- changed date
        checkInternetConnectionAndLoadData("",0);



        //handling recycler item click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(DealerPendingPaymentsFragment.this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                DealerPendingPayment dealer = pendingPaymentList.get(position);
                Intent intent = new Intent(DealerPendingPaymentsFragment.this, DealerPendingPaymentActivity.class);
                intent.putExtra("dealername",dealer.getDealerName());
                intent.putExtra("date", selectedDATE);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void DealerPendingPaymentOnDateChange() {


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

    private void checkInternetConnectionAndLoadData(String selectedDATE, int code) {

        /*check the internet connection
         * if NOT CONNECTED then load stored data
         * else load fetched data
         */
        totalpendingpaymentlayout.setVisibility(View.GONE);
        progressLayoutRL.setVisibility(View.GONE);
        poorConnectionLayoutRL.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        if (!NetworkUtils.isConnected(DealerPendingPaymentsFragment.this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Loading Previous Data!!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //handling ok button on click
                        }});
        }
        else if (code  == 0){
            //by default load todays Dealer Pending Payments
            loadTodaysDealerPendingPayments();
        }
        else if (code  == 1){
            //load on date changed
            loadDealerPendingPayments(selectedDATE);
        }
    }

    private void loadDealerPendingPayments(final String selectedDATE) {
        new AsyncTask<Void, Void, JSONObject>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressLayoutRL.setVisibility(View.VISIBLE);
                poorConnectionLayoutRL.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                totalpendingpaymentlayout.setVisibility(View.GONE);
                //change date in toolbar
                String dateformat = changeDateFormat(selectedDATE);
                dateTV.setText(dateformat);
            }

            @Override
            protected JSONObject doInBackground(Void... params) {
                return new ServerRequests().loadDealerPendingPayments(selectedDATE);
            }

            @Override
            protected void onPostExecute(JSONObject responseObject) {
                super.onPostExecute(responseObject);

                //handle json reposne object
                List<DealerPendingPayment> list = new ArrayList<>();
                //make this response ad lastfetched data based on date
                String lastfetcheddate = "";
                try {
                    if (responseObject != null) {
                        if (responseObject.getString("status").equals("success")) {
                            list = new AllDealerPendingPaymentParser().parse(responseObject.getJSONArray("result"));
                            pendingPaymentList = list;
                            lastfetcheddate = responseObject.getString("date");
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                progressLayoutRL.setVisibility(View.GONE);
                Log.e("list",list.toString());
                mAdapter = new AllDealerPendingPaymentAdapter(list,DealerPendingPaymentsFragment.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DealerPendingPaymentsFragment.this.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                if (list.size() == 0){
                    totalpendingpaymentlayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    poorConnectionLayoutRL.setVisibility(View.VISIBLE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    poorConnectionLayoutRL.setVisibility(View.GONE);
                    totalpendingpaymentlayout.setVisibility(View.VISIBLE);

                    //set total pending payment amount
                    double totalpending = 0;
                    for (DealerPendingPayment pending : list){
                        totalpending += pending.getPendingAmount();
                    }
                    totalpendingAmountTV.setText(String.valueOf(totalpending));
                }

            }
        }.execute();

    }




    //todays date
    private void loadTodaysDealerPendingPayments() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todaysDate = df.format(c.getTime());
        Log.e("Todays Date",todaysDate);
        selectedDATE = todaysDate;
        loadDealerPendingPayments(todaysDate);
    }


    private String changeDateFormat(String selectedDATE) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-DD");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMMM-yyyy");
        Date date = null;
        try {
            date = format1.parse(selectedDATE);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String dateString = format2.format(date);
        dateString = dateString.replace("-", " ");
        return dateString;
    }


    //finish the activity when toobar back button is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private DealerPendingPaymentsFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final DealerPendingPaymentsFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
