package com.example.rehanr.hmcashew.Services;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.rehanr.hmcashew.Interfaces.AlertMagnatic;

/**
 * Created by rehan r on 20-01-2017.
 */
public class AlertDialogGeneric {

    public static void getConfirmDialog(Context mContext,String title, String msg, String positiveBtnCaption, String negativeBtnCaption, boolean isCancelable, final AlertMagnatic target) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);


        builder.setTitle(title).setMessage(msg).setCancelable(false).setPositiveButton(positiveBtnCaption, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                target.PositiveMethod(dialog, id);
            }
        }).setNegativeButton(negativeBtnCaption, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                target.NegativeMethod(dialog, id);
            }
        });

        AlertDialog alert = builder.create();
        alert.setCancelable(isCancelable);
        alert.show();
        if (isCancelable) {
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    target.NegativeMethod(null, 0);
                }
            });
        }
    }
}
