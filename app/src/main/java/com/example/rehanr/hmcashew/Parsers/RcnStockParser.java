package com.example.rehanr.hmcashew.Parsers;

import com.example.rehanr.hmcashew.Models.RcnStock;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.helpers.LocatorImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rehan r on 20-01-2017.
 */
public class RcnStockParser {

    String tag;
    int bag,quantity;

    public RcnStock parse(JSONObject object){
        RcnStock rcnStock = null;
        try{
            if (object.has("tag")){
                tag = object.getString("tag");
            }

            if (object.has("bag")){
                bag = object.getInt("bag");
            }

            if (object.has("quantity")){
                quantity = object.getInt("quantity");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        rcnStock = new RcnStock(tag,bag,quantity);
        return rcnStock;
    }


    public List<RcnStock> parse(JSONArray array){

        List<RcnStock> rcnStocks = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                rcnStocks.add(parse(array.getJSONObject(i)));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return rcnStocks;
    }
}
