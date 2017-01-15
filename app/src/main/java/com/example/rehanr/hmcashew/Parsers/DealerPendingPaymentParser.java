package com.example.rehanr.hmcashew.Parsers;

import com.example.rehanr.hmcashew.Models.DealerPendingPayment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rehan r on 15-01-2017.
 */
public class DealerPendingPaymentParser {

    String PPdate,DealerName,BillNo,Billdate;
    int totalQuantity;
    double Billamount,PaymentMade,PendingAmount;

    public DealerPendingPayment parse(JSONObject object){
        DealerPendingPayment dealerPendingPayment = null;
        try {
            if (object.has("ppdate")) {
                PPdate = object.getString("ppdate");
            }

            if (object.has("dealername")) {
                DealerName = object.getString("dealername");
            }

            if (object.has("billno")) {
                BillNo = object.getString("billno");
            }

            if (object.has("billdate")) {
                Billdate = object.getString("billdate");
            }

            if (object.has("totalqty")) {
                totalQuantity = object.getInt("totalqty");
            }

            if (object.has("billamount")) {
                Billamount = object.getDouble("billamount");
            }

            if (object.has("paymentmade")) {
                PaymentMade = object.getDouble("paymentmade");
            }

            if (object.has("pendingamount")) {
                PendingAmount = object.getDouble("pendingamount");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        dealerPendingPayment = new DealerPendingPayment(PPdate,DealerName,BillNo,Billdate,totalQuantity,Billamount,PaymentMade,PendingAmount);
        return dealerPendingPayment;
    }

    public List<DealerPendingPayment> parse(JSONArray jsonArray){
        List<DealerPendingPayment> pendingPayments = new ArrayList<>();
        for (int i = 0;i < jsonArray.length(); i++){
            try {
                pendingPayments.add(parse(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return pendingPayments;
    }
}
