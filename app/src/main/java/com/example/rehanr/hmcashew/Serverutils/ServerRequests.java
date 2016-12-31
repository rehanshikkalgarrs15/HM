package com.example.rehanr.hmcashew.Serverutils;

import org.json.JSONObject;

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
}
