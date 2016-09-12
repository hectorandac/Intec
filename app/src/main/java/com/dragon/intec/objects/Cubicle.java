package com.dragon.intec.objects;/*
 * Created by HOME on 9/11/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Cubicle {

    private String id = "";
    private String number = "";
    private int reserved_hour = 0;
    private String duration = "";
    private String location = "";
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

    public boolean getData() throws IOException, JSONException {

        boolean internetConnection = false;
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
            //Testing
            URL url = new URL("http://coolsite.com/coolstuff.js");
            InputStream in = url.openStream();
            InputStreamReader reader = new InputStreamReader(in);
            //Get json from server parse it and add it to the list
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(reader.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jsonObject != null) {
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

                //Then save it
                saveToJSON(activity);
                returner = true;
            }
        }

        return returner;
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
