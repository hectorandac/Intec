package com.dragon.intec.objects;/*
 * Created by HOME on 8/25/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.AsynchronousCloseException;
import java.util.ArrayList;

public class Student {

    private String token;
    private String secret;
    private ArrayList<Object> information;
    private static final String keyToken = "TOKEN";
    private static final String keySecret = "SECRETE";
    private static final String keyObject = "STUDENT";
    private Activity activity;

    public boolean internetConnection = false;

    public Student(String token, String secret, Activity activity){
        this.token = token;
        this.secret = secret;
        this.activity = activity;

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(keyToken, token);
        editor.putString(keySecret, secret);

        editor.apply();
    }

    public ArrayList<Object> getData() throws IOException {

        information = new ArrayList<>();

        if(!internetConnection){

            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
            String jsonOBJ = sharedPref.getString(keyObject, "");
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonOBJ);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            assert jsonObject != null;
            information.add(jsonObject.optString("id"));
            information.add(jsonObject.optString("name"));
            information.add(jsonObject.optString("academic_condition"));
            information.add(jsonObject.optString("quarter"));
            information.add(jsonObject.optString("last_condition"));
            information.add(jsonObject.optString("quarter_index"));
            information.add(jsonObject.optString("general_index"));
            information.add(jsonObject.optString("validated_credits"));
            information.add(jsonObject.optString("approved_credits"));
            information.add(jsonObject.optString("approved_quarters"));
            information.add(jsonObject.optString("alerts"));
        }else{
            //Testing
            URL url = new URL("http://coolsite.com/coolstuff.js");
            InputStream in = url.openStream();
            InputStreamReader reader = new InputStreamReader(in);
            //Get json from server parse it and add it to the list
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject("-----------");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            assert jsonObject != null;
            information.add(jsonObject.optString("id"));
            information.add(jsonObject.optString("name"));
            information.add(jsonObject.optString("academic_condition"));
            information.add(jsonObject.optString("quarter"));
            information.add(jsonObject.optString("last_condition"));
            information.add(jsonObject.optString("quarter_index"));
            information.add(jsonObject.optString("general_index"));
            information.add(jsonObject.optString("validated_credits"));
            information.add(jsonObject.optString("approved_credits"));
            information.add(jsonObject.optString("approved_quarters"));
            information.add(jsonObject.optString("alerts"));
            //Then save it
            saveToJSON(activity);
        }
        return information;
    }

    public void saveToJSON(Activity activity){

        String jsonOBJ = "";
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("id", information.get(0));
            jsonObject.put("name", information.get(1));
            jsonObject.put("academic_condition", information.get(2));
            jsonObject.put("quarter", information.get(3));
            jsonObject.put("last_condition", information.get(4));
            jsonObject.put("quarter_index", information.get(5));
            jsonObject.put("general_index", information.get(6));
            jsonObject.put("validated_credits", information.get(7));
            jsonObject.put("approved_credits", information.get(8));
            jsonObject.put("approved_quarters", information.get(9));
            jsonObject.put("alerts", information.get(10));

            jsonOBJ = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(keyObject, jsonOBJ);
        editor.apply();
    }





}
