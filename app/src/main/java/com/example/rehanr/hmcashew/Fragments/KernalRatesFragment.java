package com.example.rehanr.hmcashew.Fragments;


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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rehanr.hmcashew.Activities.BaseActivity;
import com.example.rehanr.hmcashew.Adapters.KernalRatesAdapter;
import com.example.rehanr.hmcashew.Adapters.KernalStockAdapter;
import com.example.rehanr.hmcashew.Models.KernalRates;
import com.example.rehanr.hmcashew.Models.TinStock;
import com.example.rehanr.hmcashew.R;
import com.example.rehanr.hmcashew.Serverutils.ServerRequests;

import java.util.ArrayList;
import java.util.List;

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
        loadKernalRates();
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
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    retryLayoutRL.setVisibility(View.GONE);
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

}
