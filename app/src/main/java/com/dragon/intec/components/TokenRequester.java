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
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/*
 * Created by hecto on 10/16/2016.
 */

public class TokenRequester {

    private String token = "";

    public TokenRequester(String token) {
        this.token = token;
    }

    public JSONObject getObject(String url) throws IOException, JSONException {

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

    public JSONArray getArray(String url) throws IOException, JSONException {

        HttpClient clientAlert = new DefaultHttpClient(new BasicHttpParams());
        HttpGet httpGetAlert = new HttpGet(url);
        httpGetAlert.addHeader("Authorization", token);
        HttpResponse responseAlert = clientAlert.execute(httpGetAlert);

        String array = EntityUtils.toString(responseAlert.getEntity());

        return new JSONArray(array);
    }

    public JSONArray postGetArray(String url) throws IOException, JSONException {

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

    public Bitmap getUserProfile(String url) throws IOException, JSONException {

        JSONObject obj = getObject(url);
        String imageByteArray = obj.optString("image");
        byte[] byteArray = Base64.decode(imageByteArray, Base64.DEFAULT);
        //Bitmap bitmap = Bitmap.createScaledBitmap(bmp1, 120, 120, false);

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public void cancelCubicleRequest(String url, JSONObject obj) throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(url);
        StringEntity se = new StringEntity(obj.toString());
        httpost.setEntity(se);
        httpost.setHeader("Authorization", token);
        httpost.setHeader("Content-type", "application/json");
        HttpResponse response = httpclient.execute(httpost);
    }
}
