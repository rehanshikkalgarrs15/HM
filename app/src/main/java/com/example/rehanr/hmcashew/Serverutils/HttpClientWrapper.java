package com.example.rehanr.hmcashew.Serverutils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by rehan r on 31-12-2016.
 */
public class HttpClientWrapper {

    /**
     * Used OKhttp3 Library to handle all ServerRequests
     * http://square.github.io/okhttp/
     */
    OkHttpClient client;

    public HttpClientWrapper(){
        client = new OkHttpClient();
    }


    //to handle all POST Requests
    public String doUnAuthPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Headers headers = new Headers.Builder().build();
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        Log.e("--> POST <--",url + " :: " + json + " :: " + responseString);
        return responseString;
    }

    //to handle all GET Requests
    public String doGetRequest(Context context, String url) throws IOException{
        Headers headers = new Headers.Builder().build();
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();

        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        Log.e("--> GET <--", url + " :: " + responseString);
        return responseString;
    }

}
