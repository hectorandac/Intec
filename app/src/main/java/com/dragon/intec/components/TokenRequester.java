package com.dragon.intec.components;

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

/**
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

        return new JSONObject(object);
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
        //instantiates httpclient to make request
        DefaultHttpClient httpclient = new DefaultHttpClient();

        //url with the post data
        HttpPost httpost = new HttpPost(url);

        //passes the results to a string builder/entity
        StringEntity se = new StringEntity(obj.toString());

        //sets the post request as the resulting string
        httpost.setEntity(se);
        //sets a request header so the page receving the request
        //will know what to do with it
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");

        //Handles what is returned from the page
        HttpResponse response = httpclient.execute(httpost);
        return EntityUtils.toString(response.getEntity());
    }
}
