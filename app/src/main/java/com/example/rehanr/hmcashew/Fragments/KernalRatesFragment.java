package com.example.rehanr.hmcashew.Fragments;


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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rehanr.hmcashew.Activities.BaseActivity;
import com.example.rehanr.hmcashew.Adapters.KernalRatesAdapter;
import com.example.rehanr.hmcashew.Adapters.KernalStockAdapter;
import com.example.rehanr.hmcashew.Adapters.RcnStockAdapter;
import com.example.rehanr.hmcashew.Database.KernalRatesDBHandler;
import com.example.rehanr.hmcashew.Database.KernalStockDBHandler;
import com.example.rehanr.hmcashew.Interfaces.AlertMagnatic;
import com.example.rehanr.hmcashew.Models.KernalRates;
import com.example.rehanr.hmcashew.Models.RcnStock;
import com.example.rehanr.hmcashew.Models.TinStock;
import com.example.rehanr.hmcashew.R;
import com.example.rehanr.hmcashew.Serverutils.ServerRequests;
import com.example.rehanr.hmcashew.Utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.rehanr.hmcashew.Services.AlertDialogGeneric.getConfirmDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class KernalRatesFragment extends BaseActivity {



    TextView navigationItemNameTV;
    Toolbar toolbar;
    RelativeLayout progressLayoutRL,retryLayoutRL;
    private List<KernalRates> kernalRates = new ArrayList<>();
    private RecyclerView recyclerView;
    private KernalRatesAdapter mAdapter;

    KernalRatesDBHandler databaseHandler = new KernalRatesDBHandler(KernalRatesFragment.this);
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_kernal_rates);
        applyFont(KernalRatesFragment.this,findViewById(R.id.baselayout));

        navigationItemNameTV = (TextView)findViewById(R.id.navigationitemname);

        //set the toolbar to Activity
        toolbar = (Toolbar)findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //display the backbutton on toolbar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set toolbar name
        navigationItemNameTV.setText("Kernal Rates");

        progressLayoutRL = (RelativeLayout)findViewById(R.id.progresslayout);
        retryLayoutRL = (RelativeLayout)findViewById(R.id.retrylayout);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        //call this function to load contents
        checkIntenetConnection();
    }


    public void checkIntenetConnection(){
        /*check the internet connection
         * if NOT CONNECTED then load stored data
         * else load fetched data
         */
        retryLayoutRL.setVisibility(View.GONE);
        progressLayoutRL.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        if (!NetworkUtils.isConnected(KernalRatesFragment.this)){
             getConfirmDialog(KernalRatesFragment.this,getString(R.string.nointernet), getString(R.string.loadstoreddata), getString(R.string.yes), getString(R.string.no), false,
                     new AlertMagnatic() {
                         @Override
                         public void PositiveMethod(DialogInterface dialog, int id) {
                             displaydataonLayout();
                         }

                         @Override
                         public void NegativeMethod(DialogInterface dialog, int id) {
                            Toast.makeText(KernalRatesFragment.this,"No",Toast.LENGTH_LONG).show();
                             retryLayoutRL.setVisibility(View.VISIBLE);
                         }
                     });
        }
        else{
            loadKernalRates();
        }
    }



    private void loadKernalRates() {
        new AsyncTask<Void, Void, List<KernalRates>>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressLayoutRL.setVisibility(View.VISIBLE);
                retryLayoutRL.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }

            @Override
            protected List<KernalRates> doInBackground(Void... params) {
                return new ServerRequests().loadKernalRates(KernalRatesFragment.this);
            }

            @Override
            protected void onPostExecute(List<KernalRates> list) {
                super.onPostExecute(list);
                progressLayoutRL.setVisibility(View.GONE);
                Log.e("list",list.toString());
                mAdapter = new KernalRatesAdapter(list,KernalRatesFragment.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(KernalRatesFragment.this.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                if (list.size() == 0){
                    recyclerView.setVisibility(View.GONE);
                    retryLayoutRL.setVisibility(View.VISIBLE);

                    getConfirmDialog(KernalRatesFragment.this,getString(R.string.nodata), getString(R.string.loadstoreddata), getString(R.string.yes), getString(R.string.no), false,
                            new AlertMagnatic() {

                                @Override
                                public void PositiveMethod(final DialogInterface dialog, final int id) {
                                    displaydataonLayout();
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {
                                    Toast.makeText(KernalRatesFragment.this,"NO",Toast.LENGTH_LONG).show();
                                    retryLayoutRL.setVisibility(View.VISIBLE);
                                }
                            });
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    retryLayoutRL.setVisibility(View.GONE);

                    //store the LISTOFKERNALRATES in sqlite DB,
                        /*
                            * first clearAll the previously stored data from the table
                            * add all the new fetched items to the table 1
                        */

                    //1 - clear all the table1,2 data
                    databaseHandler.ClearAll();

                    //2 - store new fetched data in the table1
                    for (KernalRates rates : list){
                        databaseHandler.addKernalRates(rates);
                    }
                }
            }
        }.execute();

    }

    private void displaydataonLayout() {

        //handling ok button on click
        if (databaseHandler.getKernalRatesCount() <= 0) {
            progressLayoutRL.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(KernalRatesFragment.this, "No Data was Stored..", Toast.LENGTH_LONG).show();
        } else {
            //if data is stored in sqlite
            List<KernalRates> kernalRatesList = new ArrayList<KernalRates>();
            kernalRatesList = databaseHandler.getAllStocks();

            progressLayoutRL.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            retryLayoutRL.setVisibility(View.GONE);
            mAdapter = new KernalRatesAdapter(kernalRatesList, KernalRatesFragment.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(KernalRatesFragment.this.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            }
        }



    //finish the activity when toobar back button is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

}
