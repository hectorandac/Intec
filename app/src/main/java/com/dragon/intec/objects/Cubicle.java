package com.dragon.intec.objects;/*
 * Created by HOME on 9/11/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.dragon.intec.components.TokenRequester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Cubicle {

    private static final String keyToken = "TOKEN";
    private static final String keyObject = "CUBICLE";

    private String uniqueId = "";
    private String number = "";
    private int reserved_hour = 0;
    private String duration = "";
    private String status = "";
    private ArrayList<PartialStudent> students = new ArrayList<>();

    private Activity activity;

    public Cubicle(Activity activity) {
        this.activity = activity;
    }

    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    public String getDuration() {
        return duration;
    }

    private Cubicle setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getNumber() {
        return number;
    }

    private Cubicle setNumber(String number) {
        this.number = number;
        return this;
    }

    public int getReserved_hour() {
        return reserved_hour;
    }

    private Cubicle setReserved_hour(int reserved_hour) {
        this.reserved_hour = reserved_hour;
        return this;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    private void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    private String getLocation() {
        return "";
    }

    public ArrayList<PartialStudent> getStudents() {
        return students;
    }

    private Cubicle setStudents(ArrayList<PartialStudent> students) {
        this.students = students;
        return this;
    }

    public Cubicle[] availableList() throws IOException, JSONException {

        ArrayList<Cubicle> cubicles = new ArrayList<>();

        SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
        String token = sharedPref.getString(keyToken, "");

        JSONArray jsonArray = new TokenRequester(token).getArray("http://angularjsauthentication20161012.azurewebsites.net/api/cubicle/availability", "POST");

        for (int obj = 0; obj < jsonArray.length(); obj++) {
            JSONObject object = jsonArray.getJSONObject(obj);
            Cubicle cubicle = new Cubicle(activity);
            cubicle.setUniqueId(object.optString("uniqueId"));
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

    public boolean makeReservationIntent(Cubicle[] cubiclesTry, int hour, PartialStudent[] partialStudents, int identifier) throws Exception {

        new sendForm().execute(cubiclesTry, hour, partialStudents, identifier);

        return false;
    }

    private class sendForm extends AsyncTask<Object, Void, Boolean> {

        Cubicle[] cubiclesTry;
        int hour;
        PartialStudent[] partialStudents;
        int identifier;

        @Override
        protected Boolean doInBackground(Object... params) {

            cubiclesTry = (Cubicle[]) params[0];
            hour = (int) params[1];
            partialStudents = (PartialStudent[]) params[2];
            identifier = (int) params[3];

            for(int i = 0; i < identifier; i++) {
                hour = hour + i;

                ArrayList<Cubicle> finalList = new ArrayList<>();

                for(Cubicle cubicle : cubiclesTry){
                    if(cubicle.getReserved_hour() == hour){
                        finalList.add(cubicle);
                    }
                }

                for (Cubicle cubicle : finalList) {

                    SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
                    String token = sharedPref.getString(keyToken, "");

                    JSONObject cubicleObj = new JSONObject();

                    try {
                        cubicleObj.put("uniqueId", cubicle.getUniqueId());
                        cubicleObj.put("number", cubicle.getNumber());
                        cubicleObj.put("reservedHour", cubicle.getReserved_hour());
                        cubicleObj.put("duration", cubicle.getDuration());
                        cubicleObj.put("status", 3);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONArray students = new JSONArray();
                    JSONObject jsonObject;
                    try {
                        jsonObject = new TokenRequester(token).getJSONObject("http://angularjsauthentication20161012.azurewebsites.net/api/user");
                        JSONObject studentMainOBJ = new JSONObject();

                        studentMainOBJ.put("name", jsonObject.optString("nameMain"));
                        studentMainOBJ.put("id", jsonObject.optString("id"));

                        students.put(studentMainOBJ);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                    for (PartialStudent partialStudent : partialStudents) {

                        JSONObject studentOBJ = new JSONObject();

                        try {
                            studentOBJ.put("name", partialStudent.getName());
                            studentOBJ.put("id", partialStudent.getId());
                        } catch (Exception a) {
                            a.printStackTrace();
                        }

                        students.put(studentOBJ);
                    }

                    try {
                        cubicleObj.put("students", students);
                        Log.i("OBJson##", cubicleObj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String respond = null;
                    try {
                        respond = new TokenRequester(token).makeRequest("http://angularjsauthentication20161012.azurewebsites.net/api/cubicle/reserve", cubicleObj);
                        Log.i("RESPOND##", respond);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    assert respond != null;
                    if (respond.equals("true")) {
                        break;
                    }
                }
            }

            return null;
        }
    }

    public boolean getData() throws IOException, JSONException {

        boolean returner = false;

        SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
        String token = sharedPref.getString(keyToken, "");

        JSONObject jsonObject = new TokenRequester(token).getJSONObject("http://angularjsauthentication20161012.azurewebsites.net/api/cubicle");

        if (jsonObject != null) {
            setUniqueId(jsonObject.optString("uniqueId"));
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

        return returner;
    }

    private void saveToJSON(Activity activity){

        String jsonOBJ = "";
        JSONObject jsonObject= new JSONObject();
        try {
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
