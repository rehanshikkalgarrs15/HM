package com.example.rehanr.hmcashew.Serverutils;

import android.util.Log;

import com.example.rehanr.hmcashew.Models.TinStock;
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

    public List<TinStock> loadKernalStock(String selectedDate) {
        List<TinStock> tinStockList = new ArrayList<>();
        JSONObject request = new JSONObject();
        JSONObject responseObject = null;
        try {
            request.put("date",selectedDate);
            Log.e("<----REQUEST---->",request.toString());
            String response = clientWrapper.doPostRequest(Urls.BASEURL + Urls.KERNALSTOCK,request.toString());
            responseObject = new JSONObject(response);
            if (responseObject.getString("status").equals("success")){
                tinStockList = new KernalStockParser().parse(responseObject.getJSONArray("result"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tinStockList;
    }


}
