package com.example.rehanr.hmcashew.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rehanr.hmcashew.DatePicker.DatePickerActivity;
import com.example.rehanr.hmcashew.Fragments.DealerPendingPaymentsFragment;
import com.example.rehanr.hmcashew.Fragments.DealerStockFragment;
import com.example.rehanr.hmcashew.Fragments.FactoryReportFragment;
import com.example.rehanr.hmcashew.Fragments.KernalRatesFragment;
import com.example.rehanr.hmcashew.Fragments.KernalStockFragment;
import com.example.rehanr.hmcashew.Fragments.RCNStockFragment;
import com.example.rehanr.hmcashew.Models.User;
import com.example.rehanr.hmcashew.Preferences.LoginPreferences;
import com.example.rehanr.hmcashew.R;

import java.util.Calendar;

public class MainActivity extends BaseActivity {

    TextView userEmailTV,navigationTitleTV,dateTextTV,moreCuttingTV,moreGradingTV;
    RelativeLayout moreCuttingLayoutRL,moreGradingLayoutRL;;
    ImageView hamburgerIV,dateImageIV;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    User user;
    LoginPreferences loginPreferences;
    private static final int REQ_PICKUP_DATE = 4;

    public static final String DATE = "date";
    public static final String MONTH = "month";
    public static final String YEAR = "year";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moreCuttingTV = (TextView)findViewById(R.id.morecutting);
        moreCuttingLayoutRL = (RelativeLayout)findViewById(R.id.morecuttinglayout);
        moreGradingTV = (TextView)findViewById(R.id.moregrading);
        moreGradingLayoutRL = (RelativeLayout)findViewById(R.id.moregradinglayout);
        applyFont(MainActivity.this,findViewById(R.id.baselayout));
        hamburgerIV = (ImageView)findViewById(R.id.hamburgericon);
        navigationTitleTV = (TextView)findViewById(R.id.navigationtitle) ;
        dateTextTV = (TextView)findViewById(R.id.textdate);
        dateImageIV = (ImageView)findViewById(R.id.imagedate);
        toolbar = (Toolbar)findViewById(R.id.toobar);
        drawerLayout = (DrawerLayout) findViewById(R.id.baselayout);
        navigationView = (NavigationView)findViewById(R.id.navigation_drawer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        navigationTitleTV.setText("Factory Report");


        loginPreferences = new LoginPreferences(MainActivity.this);
        user = loginPreferences.getUser();


        //get the navigation header view to show Email Id of LoggedIn user
        View view = navigationView.getHeaderView(0);
        hamburgerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        //this function sets the emailid of logged In user
        init(view);


        //handling navigation item click listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.rcnstock:
                        startActivity(new Intent(MainActivity.this,RCNStockFragment.class));
                        break;
                    case R.id.kernalrates:
                        startActivity(new Intent(MainActivity.this,KernalRatesFragment.class));
                        break;
                    case R.id.kernalstock:
                        startActivity(new Intent(MainActivity.this,KernalStockFragment.class));
                        break;
                    case R.id.deakerstock:
                        startActivity(new Intent(MainActivity.this,DealerStockFragment.class));
                        break;
                    case R.id.dealerpendingpayments:
                        startActivity(new Intent(MainActivity.this,DealerPendingPaymentsFragment.class));
                        break;
                    case R.id.factoryreport:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });


        //handling when textdate is clicked
        dateTextTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DatePickerActivity.class);
                Calendar calendar = Calendar.getInstance();
                intent.putExtra(MONTH, calendar.get(Calendar.MONTH));
                intent.putExtra(YEAR, calendar.get(Calendar.YEAR));
                if (calendar.get(Calendar.HOUR) >= 21) {
                    intent.putExtra(DATE, calendar.get(Calendar.DATE) + 1);
                } else {
                    intent.putExtra(DATE, calendar.get(Calendar.DATE));
                }
                startActivityForResult(intent, REQ_PICKUP_DATE);
            }
        });

        dateImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //handling more cutting onclick listener
        moreCuttingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("visibility",String.valueOf(moreCuttingLayoutRL.getVisibility()));
                if (moreCuttingLayoutRL.getVisibility() == View.VISIBLE){
                    moreCuttingLayoutRL.setVisibility(View.GONE);
                    moreCuttingTV.setText("more");
                    moreCuttingTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down,0,0,0);
                }
                else if (moreCuttingLayoutRL.getVisibility() == View.GONE){
                    moreCuttingLayoutRL.setVisibility(View.VISIBLE);
                    moreCuttingTV.setText("less");
                    moreCuttingTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_up,0,0,0);
                }
            }
        });

        //handling more grading onclick listener
        moreGradingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("visibility",String.valueOf(moreGradingLayoutRL.getVisibility()));
                if (moreGradingLayoutRL.getVisibility() == View.VISIBLE){
                    moreGradingLayoutRL.setVisibility(View.GONE);
                    moreGradingTV.setText("more");
                    moreGradingTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down,0,0,0);
                }
                else if (moreGradingLayoutRL.getVisibility() == View.GONE){
                    moreGradingLayoutRL.setVisibility(View.VISIBLE);
                    moreGradingTV.setText("less");
                    moreGradingTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_up,0,0,0);
                }
            }
        });

    }




    //sets the Emailid of user in the navigation header
    private void init(View view) {
        userEmailTV = (TextView)view.findViewById(R.id.useremail);
        if(user.getEmail() != null)
            userEmailTV.setText(user.getEmail());
    }





}
