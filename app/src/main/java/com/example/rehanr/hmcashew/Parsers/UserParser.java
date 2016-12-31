package com.example.rehanr.hmcashew.Parsers;

import com.example.rehanr.hmcashew.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rehan r on 31-12-2016.
 */

//UserParser Class to Parser the resposne
public class UserParser {

    String email,password;
    User user;
    public User parser(JSONObject object){

        try {
            if (object.has("email")){
                    email = object.getString("email");
            }

            if (object.has("password")){
                password = object.getString("password");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        user = new User(email,password);
        return user;
    }
}
