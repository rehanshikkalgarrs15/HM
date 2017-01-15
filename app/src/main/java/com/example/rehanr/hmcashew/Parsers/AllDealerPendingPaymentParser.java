package com.example.rehanr.hmcashew.Parsers;

import com.example.rehanr.hmcashew.Models.DealerPendingPayment;
import com.example.rehanr.hmcashew.Models.TinStock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rehan r on 15-01-2017.
 */
public class AllDealerPendingPaymentParser {
    String DealerName;
    double PendingAmount;

    public DealerPendingPayment parse(JSONObject object){
        DealerPendingPayment dealerPendingPayment = null;
        try {
            if (object.has("name")) {
                DealerName = object.getString("name");
            }

            if (object.has("pendingamount")) {
                PendingAmount = object.getDouble("pendingamount");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        dealerPendingPayment = new DealerPendingPayment(DealerName,PendingAmount);
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
