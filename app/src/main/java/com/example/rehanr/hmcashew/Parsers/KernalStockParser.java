package com.example.rehanr.hmcashew.Parsers;

import com.example.rehanr.hmcashew.Models.TinStock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rehan r on 04-01-2017.
 */
public class KernalStockParser {

    String gradeName;
    int tins;

    public TinStock parse(JSONObject object){
        TinStock tinStock = null;
        try {
            if (object.has("gradename")) {
                gradeName = object.getString("gradename");
            }

            if (object.has("tins")) {
                tins = object.getInt("tins");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        tinStock = new TinStock(gradeName,tins);
        return tinStock;
    }

    public List<TinStock> parse(JSONArray jsonArray){
        List<TinStock> stockList = new ArrayList<>();
        for (int i = 0;i < jsonArray.length(); i++){
            try {
                stockList.add(parse(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return stockList;
    }

}
