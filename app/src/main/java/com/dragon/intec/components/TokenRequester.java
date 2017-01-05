package com.dragon.intec.components;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/*
 * Created by hecto on 10/16/2016.
 */

@SuppressWarnings("deprecation")
public class TokenRequester {

    private String token = "";

    public TokenRequester(String token) {
        this.token = token;
    }

    public JSONObject getJSONObject(String url) throws IOException, JSONException {

        HttpClient client = new DefaultHttpClient(new BasicHttpParams());
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Authorization", token);
        HttpResponse response  = client.execute(httpGet);

        String object = EntityUtils.toString(response.getEntity());
        Log.i("HEY&&S##", object);

        JSONObject returner = null;
        if(!object.equals("null")){
            returner = new JSONObject(object);
        }

        return returner;
    }

    public  JSONArray getArray(String url, String type) throws IOException, JSONException {
        if(type.toUpperCase().equals("GET")){
            return getArray_1(url);
        }else if(type.toUpperCase().equals("POST")){
            return getArray_2(url);
        }else {
            return null;
        }
    }

    private JSONArray getArray_1(String url) throws IOException, JSONException {

        HttpClient clientAlert = new DefaultHttpClient(new BasicHttpParams());
        HttpGet httpGetAlert = new HttpGet(url);
        httpGetAlert.addHeader("Authorization", token);
        HttpResponse responseAlert = clientAlert.execute(httpGetAlert);

        String array = EntityUtils.toString(responseAlert.getEntity());
        return new JSONArray(array);
    }

    private JSONArray getArray_2(String url) throws IOException, JSONException {

        HttpClient clientAlert = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httpGetAlert = new HttpPost(url);
        httpGetAlert.addHeader("Authorization", token);
        HttpResponse responseAlert = clientAlert.execute(httpGetAlert);

        String array = EntityUtils.toString(responseAlert.getEntity());
        return new JSONArray(array);
    }

    public String makeRequest(String url, JSONObject obj) throws Exception
    {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(url);
        StringEntity se = new StringEntity(obj.toString());
        httpost.setEntity(se);
        httpost.setHeader("Authorization", token);
        httpost.setHeader("Content-type", "application/json");

        //Handles what is returned from the page
        HttpResponse response = httpclient.execute(httpost);
        return EntityUtils.toString(response.getEntity());
    }

    public Bitmap getImageFromUrl(String url, String optStr) throws IOException, JSONException {

        JSONObject obj = getJSONObject(url);
        String imageByteArray = obj.optString(optStr);
        byte[] byteArray = Base64.decode(imageByteArray, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public void postJSONObject(String url, JSONObject obj) throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(url);
        StringEntity se = new StringEntity(obj.toString());
        httpost.setEntity(se);
        httpost.setHeader("Authorization", token);
        httpost.setHeader("Content-type", "application/json");
        httpclient.execute(httpost);
    }

    public void postJSONArray(String url, JSONArray obj) throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(url);
        StringEntity se = new StringEntity(obj.toString());
        httpost.setEntity(se);
        httpost.setHeader("Authorization", token);
        httpost.setHeader("Content-type", "application/json");
        httpclient.execute(httpost);
    }

    public void postNoneReturn(String url) throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(url);
        httpost.setHeader("Authorization", token);
        httpclient.execute(httpost);
    }

    public String postObject(String url) throws IOException, JSONException {

        HttpClient clientAlert = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httpGetAlert = new HttpPost(url);
        httpGetAlert.addHeader("Authorization", token);
        HttpResponse responseAlert = clientAlert.execute(httpGetAlert);

        String object = EntityUtils.toString(responseAlert.getEntity());
        Log.i("HEY&&S##", object);

        return object;
    }

}
