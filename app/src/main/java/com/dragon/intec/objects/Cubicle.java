package com.dragon.intec.objects;/*
 * Created by HOME on 9/11/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dragon.intec.components.TokenRequester;

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

public class Cubicle {

    private static final String keyToken = "TOKEN";

    private String id = "";
    private String number = "";
    private int reserved_hour = 0;
    private String duration = "";
    private String location = "";
    private String status = "";
    private ArrayList<PartialStudent> students = new ArrayList<>();

    private Activity activity;
    private static final String keyObject = "CUBICLE";

    public Cubicle(Activity activity) {
        this.activity = activity;
    }

    public String getId() {
        return id;
    }

    public Cubicle setId(String id) {
        this.id = id;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDuration() {
        return duration;
    }

    public Cubicle setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public Cubicle setNumber(String number) {
        this.number = number;
        return this;
    }

    public int getReserved_hour() {
        return reserved_hour;
    }

    public Cubicle setReserved_hour(int reserved_hour) {
        this.reserved_hour = reserved_hour;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Cubicle setLocation(String location) {
        this.location = location;
        return this;
    }

    public ArrayList<PartialStudent> getStudents() {
        return students;
    }

    public Cubicle setStudents(ArrayList<PartialStudent> students) {
        this.students = students;
        return this;
    }

    public void addStudent (PartialStudent student){
        students.add(student);
    }

    public void removeStudent (PartialStudent student){
        students.remove(student);
    }

    public Cubicle[] availableList() throws IOException, JSONException {

        ArrayList<Cubicle> cubicles = new ArrayList<>();

        SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
        String token = sharedPref.getString(keyToken, "");

        JSONArray jsonArray = new TokenRequester(token).postGetArray("http://angularjsauthentication20161012.azurewebsites.net/api/cubicle/availability");

        for (int obj = 0; obj < jsonArray.length(); obj++) {
            JSONObject object = jsonArray.getJSONObject(obj);
            Cubicle cubicle = new Cubicle(activity);
            cubicle.setId(object.optString("id"));
            cubicle.setNumber(object.optString("number"));
            cubicle.setReserved_hour(object.optInt("reservedHour"));

            cubicles.add(cubicle);
        }

        return cubicles.toArray(new Cubicle[cubicles.size()]);
    }

    public static Integer[] getAvailableHours(Cubicle[] cubicles){

        ArrayList<Integer> hours = new ArrayList<>();

        for (Cubicle cubicle : cubicles) {
            hours.add(cubicle.getReserved_hour());
        }

        return hours.toArray(new Integer[hours.size()]);
    }

    public void makeReservationIntent(Cubicle[] cubiclesTry, int hour) throws Exception {

        for(Cubicle cubicle : cubiclesTry){

            SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
            String token = sharedPref.getString(keyToken, "");

            JSONObject cubicleObj = new JSONObject();

            cubicleObj.put("id", cubicle.getId());
            cubicleObj.put("number", cubicle.getNumber());
            cubicleObj.put("reservedHour", cubicle.getReserved_hour());
            cubicleObj.put("duration", cubicle.getDuration());
            cubicleObj.put("status", 3);
            cubicleObj.put("students", null);

            String respond = new TokenRequester(token).makeRequest("http://angularjsauthentication20161012.azurewebsites.net/api/cubicle/reserve", cubicleObj);
            if (respond.equals("true")){
                break;
            }
        }
    }

    public boolean getData() throws IOException, JSONException {

        boolean internetConnection = true;
        boolean returner = false;

        if(!internetConnection){

            //saveToJSONTEST(activity);

            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
            String jsonOBJ = sharedPref.getString(keyObject, "");
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonOBJ);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(jsonObject != null) {
                setId(jsonObject.optString("id"));
                setNumber(jsonObject.optString("number"));
                setReserved_hour(Integer.parseInt(jsonObject.optString("reserved_hour")));
                setDuration(jsonObject.optString("duration"));
                setLocation(jsonObject.optString("location"));

                ArrayList<PartialStudent> students = new ArrayList<>();
                JSONArray jsonArray = jsonObject.getJSONArray("students");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonStudentPartial = jsonArray.getJSONObject(i);

                    String id = jsonStudentPartial.optString("id");
                    String name = jsonStudentPartial.optString("name");

                    PartialStudent studentPartial = new PartialStudent(id, name);
                    students.add(studentPartial);
                }

                setStudents(students);
                returner = true;
            }


        }else{

            SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
            String token = sharedPref.getString(keyToken, "");

            JSONObject jsonObject = new TokenRequester(token).getObject("http://angularjsauthentication20161012.azurewebsites.net/api/cubicle");

            if (jsonObject != null) {
                setId(jsonObject.optString("id"));
                setNumber(jsonObject.optString("number"));
                setReserved_hour(Integer.parseInt(jsonObject.optString("reservedHour")));
                setDuration(jsonObject.optString("duration"));
                setStatus(jsonObject.optString("status"));

                ArrayList<PartialStudent> students = new ArrayList<>();
                JSONArray jsonArray = jsonObject.getJSONArray("students");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonStudentPartial = jsonArray.getJSONObject(i);

                    String id = jsonStudentPartial.optString("id");
                    String name = jsonStudentPartial.optString("name");

                    PartialStudent studentPartial = new PartialStudent(id, name);
                    students.add(studentPartial);
                }

                setStudents(students);

                //Then save it
                saveToJSON(activity);
                returner = true;
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

    public void saveToJSON(Activity activity){

        String jsonOBJ = "";
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("id", getId());
            jsonObject.put("number", getNumber());
            jsonObject.put("reserved_hour", getReserved_hour());
            jsonObject.put("duration", getDuration());
            jsonObject.put("location", getLocation());

            ArrayList<PartialStudent> students = getStudents();

            JSONArray jsonArrayStudents = new JSONArray();
            for (PartialStudent student : students) {

                JSONObject studentJSON = new JSONObject();

                studentJSON.put("id", student.getId());
                studentJSON.put("name", student.getName());

                jsonArrayStudents.put(student);
            }

            jsonObject.put("alerts", jsonArrayStudents);

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
