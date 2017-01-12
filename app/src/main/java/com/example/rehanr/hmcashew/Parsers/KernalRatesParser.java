package com.example.rehanr.hmcashew.Parsers;

import com.example.rehanr.hmcashew.Models.KernalRates;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rehan r on 12-01-2017.
 */
public class KernalRatesParser {

    String gradeName,date1, date2;
    double rate1,rate2;

    public KernalRates parse(JSONObject object){

        KernalRates kernalRates = null;
        try{
            if (object.has("gradename")){
                gradeName = object.getString("gradename");
            }

            if (object.has("ratedate1")){
                date1 = object.getString("ratedate1");
            }

            if (object.has("ratedate2")){
                date2 = object.getString("ratedate2");
            }

            if (object.has("rate1")){
                rate1 = object.getInt("rate1");
            }


            if (object.has("rate2")){
                rate2 = object.getInt("rate2");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        kernalRates = new KernalRates(gradeName,date1,date2,rate1,rate2);
        return  kernalRates;
    }

    public List<KernalRates> parse(JSONArray array){

        List<KernalRates> list = new ArrayList<>();

        try{
            for (int i = 0; i < array.length(); i++){
                list.add(parse(array.getJSONObject(i)));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

}
