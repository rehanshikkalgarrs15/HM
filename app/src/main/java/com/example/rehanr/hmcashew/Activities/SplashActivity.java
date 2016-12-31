package com.example.rehanr.hmcashew.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rehanr.hmcashew.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        applyFont(SplashActivity.this,findViewById(R.id.baselayout));
    }
}
