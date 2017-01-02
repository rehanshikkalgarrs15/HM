package com.example.rehanr.hmcashew.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.example.rehanr.hmcashew.Activities.BaseActivity;
import com.example.rehanr.hmcashew.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class DealerPendingPaymentsFragment extends BaseActivity {


    //PullRefreshLayout pullRefreshLayout;
    TextView navigationItemNameTV;
    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dealer_pending_payments);
        applyFont(DealerPendingPaymentsFragment.this,findViewById(R.id.baselayout));

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.baselayout);
        navigationItemNameTV = (TextView)findViewById(R.id.navigationitemname);
       // pullRefreshLayout = (PullRefreshLayout)findViewById(R.id.baselayout);
        //set the toolbar to Activity
        toolbar = (Toolbar)findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //display the backbutton on toolbar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set toolbar name
        navigationItemNameTV.setText("Dealer Pending Payment");

        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.GRAY,Color.MAGENTA);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("pulled","yes");
                CountDownTimer countDownTimer = new CountDownTimer(5000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        // refresh complete
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }.start();

            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }



    //finish the activity when toobar back button is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (swipeRefreshLayout !=null) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.destroyDrawingCache();
            swipeRefreshLayout.clearAnimation();
        }
    }
}


//pull to referesh
        /*
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("pulled","yes");

                CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        // refresh complete
                        pullRefreshLayout.setRefreshing(false);
                    }
                }.start();
            }


        });*/