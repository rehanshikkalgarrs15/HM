package com.example.rehanr.hmcashew.Serverutils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.rehanr.hmcashew.Models.KernalRates;
import com.example.rehanr.hmcashew.Models.TinStock;
import com.example.rehanr.hmcashew.Parsers.KernalRatesParser;
import com.example.rehanr.hmcashew.Parsers.KernalStockParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rehan r on 31-12-2016.
 */
public class ServerRequests {

    HttpClientWrapper clientWrapper;
    public ServerRequests(){
        clientWrapper = new HttpClientWrapper();
    }

    public JSONObject loginUser(String email, String password) {

        JSONObject responseObject = new JSONObject();
        try {
            JSONObject request = new JSONObject();
            request.put("email", email);
            request.put("password", password);
            String response = clientWrapper.doPostRequest(Urls.BASEURL+Urls.LOGINURL,request.toString());
            responseObject = new JSONObject(response);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseObject;

    }

    public JSONObject loadKernalStock(String selectedDate) {
        JSONObject request = new JSONObject();
        JSONObject responseObject = null;
        try {
            request.put("date",selectedDate);
            Log.e("<----REQUEST---->",request.toString());
            String response = clientWrapper.doPostRequest(Urls.BASEURL + Urls.KERNALSTOCK,request.toString());
            responseObject = new JSONObject(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseObject;
    }


    public JSONObject loadFactoryReport(String selectedDate) {
        JSONObject request = new JSONObject();
        JSONObject responseobject = null;
        try{
            request.put("date",selectedDate);
            Log.e("<----REQUEST---->",request.toString());
            String response = clientWrapper.doPostRequest(Urls.BASEURL + Urls.FACTORYREPORT,request.toString());
            responseobject = new JSONObject(response);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return responseobject;
    }

    public List<KernalRates> loadKernalRates(Context context) {
        List<KernalRates> kernalRates  = new ArrayList<>();
        JSONObject responseObject = null;
        try {
            String response = clientWrapper.doGetRequest(context, Urls.BASEURL + Urls.KERNALRATES);
            responseObject = new JSONObject(response);
            if (responseObject != null) {
                if (responseObject.getString("status").equals("success")) {
                    kernalRates = new KernalRatesParser().parse(responseObject.getJSONArray("result"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kernalRates;
    }

    public JSONObject loadDealerPendingPayments(String selectedDATE) {

        JSONObject request = new JSONObject();
        JSONObject responseObject = null;
        try {
            request.put("date",selectedDATE);
            Log.e("<----REQUEST---->",request.toString());
            String response = clientWrapper.doPostRequest(Urls.BASEURL + Urls.ALLDEALERPENDINGPAYMENTS,request.toString());
            responseObject = new JSONObject(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseObject;
    }

    public JSONObject loadPendingPaymentOfParticularDealer(String dealername, String date) {
        JSONObject request = new JSONObject();
        JSONObject responseObject = null;
        try {
            request.put("date",date);
            request.put("dealername",dealername);
            Log.e("<----REQUEST---->",request.toString());
            String response = clientWrapper.doPostRequest(Urls.BASEURL + Urls.DEALERPENDINGPAYMENT,request.toString());
            responseObject = new JSONObject(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseObject;
    }

    public JSONObject loadRCNStock(String selectedDate) {
        JSONObject request = new JSONObject();
        JSONObject responseObject = null;
        try {
            request.put("date",selectedDate);
            Log.e("<----REQUEST---->",request.toString());
            String response = clientWrapper.doPostRequest(Urls.BASEURL + Urls.RCNSTOCK,request.toString());
            responseObject = new JSONObject(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseObject;
    }
}
