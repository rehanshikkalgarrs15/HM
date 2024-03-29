package com.example.rehanr.hmcashew.Activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
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

import com.example.rehanr.hmcashew.Adapters.KernalStockAdapter;
import com.example.rehanr.hmcashew.Database.FactoryReportDBHandler;
import com.example.rehanr.hmcashew.Fragments.DealerPendingPaymentsFragment;
import com.example.rehanr.hmcashew.Fragments.DealerStockFragment;
import com.example.rehanr.hmcashew.Fragments.KernalRatesFragment;
import com.example.rehanr.hmcashew.Fragments.KernalStockFragment;
import com.example.rehanr.hmcashew.Fragments.RCNStockFragment;
import com.example.rehanr.hmcashew.Interfaces.AlertMagnatic;
import com.example.rehanr.hmcashew.Models.FactoryReport;
import com.example.rehanr.hmcashew.Models.TinStock;
import com.example.rehanr.hmcashew.Models.User;
import com.example.rehanr.hmcashew.Parsers.FactoryReportParser;
import com.example.rehanr.hmcashew.Preferences.LoginPreferences;
import com.example.rehanr.hmcashew.R;
import com.example.rehanr.hmcashew.Serverutils.ServerRequests;
import com.example.rehanr.hmcashew.Utils.NetworkUtils;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.rehanr.hmcashew.Services.AlertDialogGeneric.getConfirmDialog;

public class MainActivity extends BaseActivity {

    TextView userEmailTV,navigationTitleTV,dateTextTV,moreCuttingTV,moreGradingTV;
    RelativeLayout moreCuttingLayoutRL,moreGradingLayoutRL;;
    ImageView hamburgerIV,dateImageIV;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    User user;
    LoginPreferences loginPreferences;
    Calendar calendar ;
    DatePickerDialog datePickerDialog ;
    int Year, Month, Day ;
    String selectedDATE = "";


    //factory report layout details
    TextView employeeCuttingTV,employeeGradingTV,employeeStaffTV,employeeTotalTV,
            boilingTV,cuttingGarTV,cuttingTukdaTV,cuttingTotalTV,cuttingTukdaPercentageTV,
            cuttingAveragePerEmployeeTV,cuttingCostPerKGTV,gradingGarTV,gradingTukdaTV,
            gradingTotalTV,gradingAveragePerEmployeeTV,gradingCostPerKGTV,
            packedTinsOpStockTV,packedTinsNewPackedTV,packedTinsSaleTV,packedTinsCloseStockTV,
            rcnStockRawCashewTV;
    LinearLayout factoryReportLayoutLL;
    RelativeLayout loadingLayoutRL,nodataLayoutRL;

    FactoryReportDBHandler databaseHandler = new FactoryReportDBHandler(MainActivity.this);
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

        employeeCuttingTV = (TextView)findViewById(R.id.cuttingnumber);
        employeeGradingTV = (TextView)findViewById(R.id.gradingnumber);
        employeeStaffTV = (TextView)findViewById(R.id.staffnumber);
        employeeTotalTV = (TextView)findViewById(R.id.totalnumber);

        boilingTV = (TextView)findViewById(R.id.boilingnumber);
        cuttingGarTV = (TextView)findViewById(R.id.garnumber);
        cuttingTukdaTV = (TextView)findViewById(R.id.tukdanumber);
        cuttingTotalTV = (TextView)findViewById(R.id.totalcuttingnumber);
        cuttingTukdaPercentageTV = (TextView)findViewById(R.id.tukdapercentnumber);
        cuttingAveragePerEmployeeTV = (TextView)findViewById(R.id.average_emp_number);
        cuttingCostPerKGTV = (TextView)findViewById(R.id.costperkgnumber);

        gradingGarTV = (TextView)findViewById(R.id.gradinggarnumber);
        gradingTukdaTV = (TextView)findViewById(R.id.gradingtukdanumber);
        gradingTotalTV = (TextView)findViewById(R.id.gradingtotalcuttingnumber);
        gradingAveragePerEmployeeTV = (TextView)findViewById(R.id.grading_average_emp_number);
        gradingCostPerKGTV = (TextView)findViewById(R.id.gradingcostperkgnumber);

        packedTinsOpStockTV = (TextView)findViewById(R.id.opstocktvnumber);
        packedTinsNewPackedTV = (TextView)findViewById(R.id.newpackednumber);
        packedTinsSaleTV = (TextView)findViewById(R.id.salenumber);
        packedTinsCloseStockTV = (TextView)findViewById(R.id.closeorstocknumber);
        rcnStockRawCashewTV = (TextView)findViewById(R.id.rcnrawcashewnumber);

        factoryReportLayoutLL = (LinearLayout)findViewById(R.id.factoryreportlayout);
        loadingLayoutRL = (RelativeLayout)findViewById(R.id.factoryreportprogresslayout);
        nodataLayoutRL = (RelativeLayout)findViewById(R.id.factoryreportretrylayout);

        loginPreferences = new LoginPreferences(MainActivity.this);
        user = loginPreferences.getUser();
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);

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

        //Default load Factory Report data of todays date
        //loadTodaysFactoryReport();
        checkInternetConnectionAndLoadData("",0);

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
                    /*case R.id.deakerstock:
                        startActivity(new Intent(MainActivity.this,DealerStockFragment.class));
                        break;*/
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
                datePicker();
            }
        });

        dateImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });


        //on retry clicked
        nodataLayoutRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFactoryReport(selectedDATE);
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

    private void checkInternetConnectionAndLoadData(String date, int code) {
        /*check the internet connection
         * if NOT CONNECTED then load stored data
         * else load fetched data
         */
        factoryReportLayoutLL.setVisibility(View.GONE);
        nodataLayoutRL.setVisibility(View.GONE);
        loadingLayoutRL.setVisibility(View.GONE);
        if (!NetworkUtils.isConnected(MainActivity.this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Load stored data!!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //handling ok button on click
                            if (databaseHandler.getFactoryReportCount() <= 0){
                                factoryReportLayoutLL.setVisibility(View.GONE);
                                nodataLayoutRL.setVisibility(View.GONE);
                                loadingLayoutRL.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this,"No Data was Stored..",Toast.LENGTH_LONG).show();
                            }
                            else {
                                factoryReportLayoutLL.setVisibility(View.VISIBLE);
                                FactoryReport factoryReport = databaseHandler.getFactoryReport();
                                displayDataOnLayout(factoryReport);
                                String laststoreddate = databaseHandler.getLastStoredDate();
                                dateTextTV.setText("Factory Report As on\n\t\t" + changeDateFormat(laststoreddate));
                            }
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else if (code  == 0){
            //by default load todays Kernal stock
            loadTodaysFactoryReport();
        }
        else if (code  == 1){
            //load on date changed
            loadFactoryReport(date);
        }
    }

    private void datePicker() {

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

    //fetch todays date
    private void loadTodaysFactoryReport() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todaysDate = df.format(c.getTime());
        selectedDATE = todaysDate;
        Log.e("Todays Date",todaysDate);
        loadFactoryReport(todaysDate);
    }

    private void loadFactoryReport(final String selectedDate) {
        new AsyncTask<Void, Void, JSONObject>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                factoryReportLayoutLL.setVisibility(View.GONE);
                nodataLayoutRL.setVisibility(View.GONE);
                loadingLayoutRL.setVisibility(View.VISIBLE);

                //change date in toolbar
                String dateString = changeDateFormat(selectedDate);
                dateTextTV.setText(dateString);
            }

            @Override
            protected JSONObject doInBackground(Void... params) {
                return new ServerRequests().loadFactoryReport(selectedDate);
            }

            @Override
            protected void onPostExecute(final JSONObject response) {
                super.onPostExecute(response);
                try {
                    if (response != null) {
                        if (response.getString("status").equals("success")){
                            FactoryReport factoryReport = new  FactoryReportParser().parse(response.getJSONObject("factoryreport"));
                            String lastfetcheddate = response.getString("date");
                            //first clear previous data
                            //add new data
                            databaseHandler.ClearAll();
                            databaseHandler.addFactoryReport(factoryReport);
                            databaseHandler.addLastStoredDate(lastfetcheddate);
                            Log.e("FR - Last fetched Date" , lastfetcheddate);

                            factoryReportLayoutLL.setVisibility(View.VISIBLE);
                            nodataLayoutRL.setVisibility(View.GONE);
                            loadingLayoutRL.setVisibility(View.GONE);
                            //display data on layout
                            displayDataOnLayout(factoryReport);
                        }
                        else if (response.getString("status").equals("failed")){
                            factoryReportLayoutLL.setVisibility(View.GONE);
                            nodataLayoutRL.setVisibility(View.VISIBLE);
                            loadingLayoutRL.setVisibility(View.GONE);
                            getConfirmDialog(MainActivity.this,getString(R.string.emptylist), getString(R.string.loadstoreddata), getString(R.string.yes), getString(R.string.no), false,
                                    new AlertMagnatic() {

                                        @Override
                                        public void PositiveMethod(final DialogInterface dialog, final int id) {
                                            nodataLayoutRL.setVisibility(View.GONE);
                                            factoryReportLayoutLL.setVisibility(View.VISIBLE);
                                            if (databaseHandler.getFactoryReportCount() <= 0) {
                                                factoryReportLayoutLL.setVisibility(View.GONE);
                                                nodataLayoutRL.setVisibility(View.VISIBLE);
                                                loadingLayoutRL.setVisibility(View.GONE);
                                                Toast.makeText(MainActivity.this,"No Data was Stored..",Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                FactoryReport factoryReport = databaseHandler.getFactoryReport();
                                                displayDataOnLayout(factoryReport);
                                                String laststoreddate = databaseHandler.getLastStoredDate();
                                                dateTextTV.setText("Factory Report As on\n\t\t" + changeDateFormat(laststoreddate));
                                            }
                                        }

                                        @Override
                                        public void NegativeMethod(DialogInterface dialog, int id) {
                                            factoryReportLayoutLL.setVisibility(View.GONE);
                                            nodataLayoutRL.setVisibility(View.VISIBLE);
                                            loadingLayoutRL.setVisibility(View.GONE);
                                        }
                                    });
                        }
                        else if (response.getString("status").equals("datechanged")){
                            final FactoryReport factoryReport = new  FactoryReportParser().parse(response.getJSONObject("factoryreport"));
                            final String lastfetcheddate = response.getString("date");
                            //if recent date data is available
                            getConfirmDialog(MainActivity.this,getString(R.string.nodataonselecteddate), getString(R.string.loadrecentdata), getString(R.string.yes), getString(R.string.no), false,
                                    new AlertMagnatic() {

                                        @Override
                                        public void PositiveMethod(final DialogInterface dialog, final int id) {
                                            databaseHandler.ClearAll();
                                            databaseHandler.addFactoryReport(factoryReport);
                                            databaseHandler.addLastStoredDate(lastfetcheddate);
                                            Log.e("FR - Last fetched Date" , lastfetcheddate);

                                            factoryReportLayoutLL.setVisibility(View.VISIBLE);
                                            nodataLayoutRL.setVisibility(View.GONE);
                                            loadingLayoutRL.setVisibility(View.GONE);
                                            //display data on layout
                                            displayDataOnLayout(factoryReport);
                                            //change date on toolbar
                                            String changeddataDate = changeDateFormat(lastfetcheddate);
                                            dateTextTV.setText("Factory Report as on\n\t\t" + changeddataDate);
                                        }

                                        @Override
                                        public void NegativeMethod(DialogInterface dialog, int id) {
                                            factoryReportLayoutLL.setVisibility(View.GONE);
                                            nodataLayoutRL.setVisibility(View.VISIBLE);
                                            loadingLayoutRL.setVisibility(View.GONE);
                                        }
                                    });

                        }
                    } else {
                        Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                        factoryReportLayoutLL.setVisibility(View.GONE);
                        nodataLayoutRL.setVisibility(View.VISIBLE);
                        loadingLayoutRL.setVisibility(View.GONE);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        }.execute();
    }


    //this function displays data on the Layout
    private void displayDataOnLayout(FactoryReport factoryReport) {
        DecimalFormat df = new DecimalFormat("#.##");
        double totalEmployee,totalcutting,cuttingtukdapercentage,totalgrading;
        totalEmployee = Double.parseDouble(factoryReport.getEmployeeCutting()) +
                Double.parseDouble(factoryReport.getEmployeeGrading()) + Double.parseDouble(factoryReport.getEmployeeStaff());
        totalcutting = Double.parseDouble(factoryReport.getCuttingGar()) + Double.parseDouble(factoryReport.getCuttingTukda());
        cuttingtukdapercentage = (Double.parseDouble(factoryReport.getCuttingTukda()) / Double.parseDouble(factoryReport.getCuttingGar()))*100;
        totalgrading = Double.parseDouble(factoryReport.getGradingGar()) + Double.parseDouble(factoryReport.getGradingTukda());

        employeeCuttingTV.setText(factoryReport.getEmployeeCutting());
        employeeGradingTV.setText(factoryReport.getEmployeeGrading());
        employeeStaffTV.setText(factoryReport.getEmployeeStaff());
        employeeTotalTV.setText(String.valueOf(totalEmployee));

        boilingTV.setText(factoryReport.getBoling());
        cuttingGarTV.setText(factoryReport.getCuttingGar());
        cuttingTukdaTV.setText(factoryReport.getCuttingTukda());
        cuttingTotalTV.setText(String.valueOf(totalcutting));
        cuttingTukdaPercentageTV.setText(String.valueOf(df.format(cuttingtukdapercentage)));
        cuttingAveragePerEmployeeTV.setText(factoryReport.getCuttingAveragePerEmployee());
        cuttingCostPerKGTV.setText(factoryReport.getCuttingCostPerKG());

        gradingGarTV.setText(factoryReport.getGradingGar());
        gradingTukdaTV.setText(factoryReport.getGradingTukda());
        gradingTotalTV.setText(String.valueOf(totalgrading));
        gradingAveragePerEmployeeTV.setText(factoryReport.getGradingAveragePerEmployee());
        gradingCostPerKGTV.setText(factoryReport.getGradingCostPerKG());

        packedTinsOpStockTV.setText(factoryReport.getPackedTinsOpStock());
        packedTinsNewPackedTV.setText(factoryReport.getPackedTinsNewPacked());
        packedTinsSaleTV.setText(factoryReport.getPackedTinsSale());
        packedTinsCloseStockTV.setText(factoryReport.getPackedTinsCloseStock());

        rcnStockRawCashewTV.setText(factoryReport.getRcnRawCashew());
    }


    //sets the Emailid of user in the navigation header
    private void init(View view) {
        userEmailTV = (TextView)view.findViewById(R.id.useremail);
        if(user.getEmail() != null)
            userEmailTV.setText(user.getEmail());
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
