package com.example.rehanr.hmcashew.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by rehan r on 12-01-2017.
 */
public class SnackBar {

    public static void popifNotConnected(final CoordinatorLayout coordinatorLayout){
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Not Connected to Internet", Snackbar.LENGTH_LONG)
                .setAction("ENABLE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });

        snackbar.show();
    }
}
