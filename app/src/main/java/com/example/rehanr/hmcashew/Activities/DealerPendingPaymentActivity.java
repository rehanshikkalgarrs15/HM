package com.example.rehanr.hmcashew.Activities;

import android.app.DatePickerDialog;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rehanr.hmcashew.Adapters.AllDealerPendingPaymentAdapter;
import com.example.rehanr.hmcashew.Adapters.DealerPendingPaymentAdapter;
import com.example.rehanr.hmcashew.Adapters.KernalStockAdapter;
import com.example.rehanr.hmcashew.Models.DealerPendingPayment;
import com.example.rehanr.hmcashew.Models.TinStock;
import com.example.rehanr.hmcashew.Parsers.AllDealerPendingPaymentParser;
import com.example.rehanr.hmcashew.Parsers.DealerPendingPaymentParser;
import com.example.rehanr.hmcashew.R;
import com.example.rehanr.hmcashew.Serverutils.ServerRequests;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DealerPendingPaymentActivity extends BaseActivity {

    TextView navigationItemNameTV,dateTV;
    Toolbar toolbar;
    RelativeLayout progressLayoutRL,poorConnectionLayoutRL;

    private List<DealerPendingPayment> pendingPayments = new ArrayList<>();
    private RecyclerView recyclerView;
    private DealerPendingPaymentAdapter mAdapter;
    String dealername,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_pending_payment);
        applyFont(DealerPendingPaymentActivity.this,findViewById(R.id.baselayout));
        //set the toolbar to Activity
        toolbar = (Toolbar)findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        navigationItemNameTV = (TextView)findViewById(R.id.navigationtitle);
        progressLayoutRL = (RelativeLayout)findViewById(R.id.progresslayout);
        poorConnectionLayoutRL = (RelativeLayout)findViewById(R.id.retrylayout);
        dateTV = (TextView)findViewById(R.id.textdate);

        //display the backbutton on toolbar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set toolbar name
        navigationItemNameTV.setText("Pending");

        //fetch values from prevous activity through Intent
        Intent intent = getIntent();
        dealername = intent.getStringExtra("dealername");
        date = intent.getStringExtra("date");

        //call this function to load contents
        loadPendingPayment(dealername,date);
    }

    private void loadPendingPayment(final String dealername, final String date) {
        new AsyncTask<Void,Void,JSONObject>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressLayoutRL.setVisibility(View.VISIBLE);
                poorConnectionLayoutRL.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);

                //change date in toolbar
                String dateformat = changeDateFormat(date);
                dateTV.setText("PPDate-" + dateformat);
            }

            @Override
            protected JSONObject doInBackground(Void... params) {
                return new ServerRequests().loadPendingPaymentOfParticularDealer(dealername,date);
            }

            @Override
            protected void onPostExecute(JSONObject responseObject) {
                super.onPostExecute(responseObject);

                //handle json reposne object
                List<DealerPendingPayment> list = new ArrayList<>();
                try {
                    if (responseObject != null) {
                        if (responseObject.getString("status").equals("success")) {
                            list = new DealerPendingPaymentParser().parse(responseObject.getJSONArray("result"));
                        }
                        else if (responseObject.getString("status").equals("failed")){
                            recyclerView.setVisibility(View.GONE);
                            poorConnectionLayoutRL.setVisibility(View.VISIBLE);
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                progressLayoutRL.setVisibility(View.GONE);
                Log.e("list",list.toString());
                mAdapter = new DealerPendingPaymentAdapter(list,DealerPendingPaymentActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DealerPendingPaymentActivity.this.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                if (list.size() == 0){
                    recyclerView.setVisibility(View.GONE);
                    poorConnectionLayoutRL.setVisibility(View.VISIBLE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    poorConnectionLayoutRL.setVisibility(View.GONE);
                }

            }
        }.execute();

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
}
