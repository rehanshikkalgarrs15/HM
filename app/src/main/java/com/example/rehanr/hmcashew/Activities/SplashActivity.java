package com.example.rehanr.hmcashew.Activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.rehanr.hmcashew.Preferences.LoginPreferences;
import com.example.rehanr.hmcashew.R;

public class SplashActivity extends BaseActivity {

    public static int LOGIN_REQ_CODE = 1;
    LoginPreferences loginPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loginPreferences = new LoginPreferences(SplashActivity.this);
        //applies font for all the views in the Splash Activity
        applyFont(SplashActivity.this,findViewById(R.id.baselayout));

        /*
         *start countdown timer for 3 second
         *to make any server calls before opening Main Activity
        */
        CountDownTimer countDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                //checking if user is already loggedIn
                //if loggedIn Open MainActivity else Open LoginActivity
                Log.e("Is_Logged_in",String.valueOf(loginPreferences.isLoggedIn()));
                if (loginPreferences.isLoggedIn()){
                    openMainActivity();
                }
                else{
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();

    }

    private void openMainActivity() {
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
