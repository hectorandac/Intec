package com.dragon.intec.objects;/*
 * Created by HOME on 9/13/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Calendar {

    private ArrayList<String[]> calendarRows = new ArrayList<>();

    public static final int TYPE_QUARTER = 0;
    public static final int TYPE_SELECTION = 1;
    public static final int TYPE_ANNUAL = 2;
    public static final int TYPE_FINANCE = 3;

    private static final String keyObject_1 = "CALENDAR_1";
    private static final String keyObject_2 = "CALENDAR_2";
    private static final String keyObject_3 = "CALENDAR_3";
    private static final String keyObject_4 = "CALENDAR_4";


    Activity activity;
    int type = 0;

    public Calendar(Activity activity, int type) {
        this.activity = activity;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<String[]> getCalendarRows() {
        return calendarRows;
    }

    public Calendar setCalendarRows(ArrayList<String[]> calendarRows) {
        this.calendarRows = calendarRows;
        return this;
    }

    public void addRow (String[] row){
        calendarRows.add(row);
    }

    public void removeRow (String[] row){
        calendarRows.remove(row);
    }

    public String[] getRow (int index){
        return calendarRows.get(index);
    }

    public boolean getData() throws IOException, JSONException {

        boolean internetConnection = true;
        boolean returner = false;

        String designedKey = "";
        int column_count = 0;

        switch (type){
            case TYPE_QUARTER:
                designedKey = keyObject_1;
                column_count = 4;
                break;
            case TYPE_SELECTION:
                designedKey = keyObject_2;
                column_count = 2;
                break;
            case TYPE_ANNUAL:
                designedKey = keyObject_3;
                column_count = 5;
                break;
            case TYPE_FINANCE:
                designedKey = keyObject_4;
                column_count = 1;
                break;
        }

        if(!internetConnection){

            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
            String jsonOBJ = sharedPref.getString(designedKey, "");
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonOBJ);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(jsonObject != null) {

                JSONArray jsonArray = jsonObject.getJSONArray("rows");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONArray row = jsonArray.getJSONArray(i);
                    String[] columns = new String[column_count];

                    for (int j = 0; j < row.length(); j++){
                        columns[j] = row.getString(j);
                    }

                    addRow(columns);
                }

                returner = true;
            }


        }else{
            //Testing
            String url = "http://intecapp.azurewebsites.net/api/calendar?t={"+type+"}";
            JSONObject jsonObject = readJsonFromUrl(url);

            if (jsonObject != null) {

                JSONArray jsonArray = jsonObject.getJSONArray("rows");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONArray row = jsonArray.getJSONArray(i);
                    String[] columns = new String[column_count];

                    for (int j = 0; j < row.length(); j++){
                        columns[j] = row.getString(j);
                    }

                    addRow(columns);
                }

                returner = true;

                //Then save it
                saveToJSON(activity, designedKey);
            }
        }

        return returner;

    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        } finally {
            is.close();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public void saveToJSON(Activity activity, String keyObject){

        String jsonOBJ = "";
        JSONObject jsonObject= new JSONObject();
        try {

            ArrayList<String[]> rows = getCalendarRows();

            JSONArray jsonArrayRows = new JSONArray();
            for (String[] row : rows) {

                JSONArray jsonArrayRow = new JSONArray();
                for(String val : row) {
                    jsonArrayRow.put(val);
                }

                jsonArrayRows.put(jsonArrayRow);
            }

            jsonObject.put("rows", jsonArrayRows);

            jsonOBJ = jsonObject.toString();
            Log.i("USER_json", jsonOBJ);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(keyObject, jsonOBJ);
        editor.apply();
    }
}
