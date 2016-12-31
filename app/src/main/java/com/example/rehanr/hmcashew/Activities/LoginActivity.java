package com.example.rehanr.hmcashew.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rehanr.hmcashew.Models.User;
import com.example.rehanr.hmcashew.Parsers.UserParser;
import com.example.rehanr.hmcashew.Preferences.LoginPreferences;
import com.example.rehanr.hmcashew.R;
import com.example.rehanr.hmcashew.Serverutils.ServerRequests;
import com.example.rehanr.hmcashew.Utils.DialogUtils;

import org.json.JSONObject;

public class LoginActivity extends BaseActivity {

    EditText emailET,passwordET;
    Button loginBT;
    LoginPreferences loginPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        applyFont(LoginActivity.this,findViewById(R.id.baselayout));
        loginPreferences = new LoginPreferences(LoginActivity.this);
        emailET = (EditText)findViewById(R.id.email);
        passwordET = (EditText)findViewById(R.id.password);
        loginBT = (Button)findViewById(R.id.loginbutton);

        //LoginButton OnCLickListener
        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEntries();
            }
        });
    }

    //function to validate Email and Password
    private void validateEntries() {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        if ((!email.contains("@") && !email.contains(".")) && !(password.length() > 6)){
            Toast.makeText(LoginActivity.this,"Enter Valid Details",Toast.LENGTH_LONG).show();
        }
        else{
            loginUser(email,password);
        }
    }


    //creating a Thread (Async Task)
    /*
    3 methods
    * 1)onPreExecute() - before server call is made
    * 2)doInBackground() - at the time of server call
    * 3)onPostExecute() - after getting the response from the server
    */
    private void loginUser(final String email, final String password) {
        new AsyncTask<Void, Void, JSONObject>(){

            //Progress Dialog from Dialog Utils class
            DialogUtils progressDialog = new DialogUtils(LoginActivity.this,DialogUtils.Type.PROGRESS_DIALOG);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.showProgressDialog("Login","please wait...",false);
            }

            @Override
            protected JSONObject doInBackground(Void... params) {
                return new ServerRequests().loginUser(email,password);
            }

            @Override
            protected void onPostExecute(JSONObject response) {
                super.onPostExecute(response);
                progressDialog.dismissProgressDialog();
                /*
                    Response is of type -
                    * for correct details JSONObject Reponse
                        {
                          "status": "success",
                          "user": {
                            "email": "faizanmulla@gmail.com",
                            "password": "123456"
                          }
                        }

                   * for Incorrect details JSONObject Reponse
                        {
                          "status": "failed",
                          "result": "Invalid Details"
                        }
               */
                try {
                    if (response != null) {
                        //if status == success then parse the user Jsonobject
                        if (response.getString("status").equals("success")) {
                            Toast.makeText(LoginActivity.this,"Success",Toast.LENGTH_LONG).show();
                            //parse the JsonObject
                            User user = new UserParser().parser(response.getJSONObject("user"));
                            //storing details on LoginPreferences
                            loginPreferences.loginUser(user);
                            //open Main Activity
                            openMainActivity();
                        }
                        else if(response.getString("status").equals("failed")){
                            Toast.makeText(LoginActivity.this,response.getString("result"),Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this,R.string.wrong,Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        }.execute();
    }

    private void openMainActivity() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
