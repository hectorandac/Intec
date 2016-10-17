package com.dragon.intec.objects;/*
 * Created by HOME on 8/25/2016.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dragon.intec.components.TokenRequester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class Student{

    private String token;
    private static final String keyToken = "TOKEN";
    private static final String keyObject = "STUDENT";
    private Activity activity;

    private String id;
    private String name;
    private String program;
    private String academicCondition;
    private String quarter;
    private String lastCondition;
    private double quarterIndex;
    private double generalIndex;
    private int validatedCredits;
    private int approvedCredits;
    private int approvedQuarters;
    private String[] alerts;
    private ClassRoom[] signatures;

    public String getToken() {
        return token;
    }

    public Student setToken(String token) {
        this.token = token;
        return this;
    }

    public ClassRoom[] getSignatures() {
        return signatures;
    }

    private Student setSignatures(ClassRoom[] signatures) {
        this.signatures = signatures;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProgram() {
        return program;
    }

    public String getAcademicCondition() {
        return academicCondition;
    }

    public String getQuarter() {
        return quarter;
    }

    public String getLastCondition() {
        return lastCondition;
    }

    public double getQuarterIndex() {
        return quarterIndex;
    }

    public double getGeneralIndex() {
        return generalIndex;
    }

    public int getValidatedCredits() {
        return validatedCredits;
    }

    public int getApprovedCredits() {
        return approvedCredits;
    }

    public int getApprovedQuarters() {
        return approvedQuarters;
    }

    public String[] getAlerts() {
        return alerts;
    }

    private Student setAlerts(String[] alerts) {
        this.alerts = alerts;
        return this;
    }

    private Student setApprovedQuarters(int approvedQuarters) {
        this.approvedQuarters = approvedQuarters;
        return this;
    }

    private Student setApprovedCredits(int approvedCredits) {
        this.approvedCredits = approvedCredits;
        return this;
    }

    private Student setValidatedCredits(int validatedCredits) {
        this.validatedCredits = validatedCredits;
        return this;
    }

    private Student setGeneralIndex(double generalIndex) {
        this.generalIndex = generalIndex;
        return this;
    }

    private Student setProgram(String program) {
        this.program = program;
        return this;
    }

    public Student setQuarterIndex(double quarterIndex) {
        this.quarterIndex = quarterIndex;
        return this;
    }

    public Student setLastCondition(String lastCondition) {
        this.lastCondition = lastCondition;
        return this;
    }

    public Student setQuarter(String quarter) {
        this.quarter = quarter;
        return this;
    }

    public Student setAcademicCondition(String academic_condition) {
        this.academicCondition = academic_condition;
        return this;
    }

    public Student setName(String name) {
        this.name = name;
        return this;
    }

    public Student setId(String id) {
        this.id = id;
        return this;
    }

    public Student(Activity activity){
        this.activity = activity;
    }

    public void getData() throws IOException, JSONException {

        boolean internetConnection = true;

        if(!internetConnection){

            //saveToJSONTEST(activity);

            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
            String token = sharedPref.getString(keyObject, "");
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(token);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            assert jsonObject != null;
            setId(jsonObject.optString("id"));
            setName(jsonObject.optString("name"));
            setProgram(jsonObject.optString("program"));
            setAcademicCondition(jsonObject.optString("academic_condition"));
            setQuarter(jsonObject.optString("quarter"));
            setLastCondition(jsonObject.optString("last_condition"));
            setQuarterIndex(Double.parseDouble(jsonObject.optString("quarter_index")));
            setGeneralIndex(Double.parseDouble(jsonObject.optString("general_index")));
            setValidatedCredits(Integer.parseInt(jsonObject.optString("validated_credits")));
            setApprovedCredits(Integer.parseInt(jsonObject.optString("approved_credits")));
            setApprovedQuarters(Integer.parseInt(jsonObject.optString("approved_quarters")));

            ArrayList<String> vals = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("alerts");
            for (int i = 0; i < jsonArray.length(); i++){
                vals.add(jsonArray.getString(i));
            }
            setAlerts(vals.toArray(new String[vals.size()]));

            JSONArray jsonArraySignatures = jsonObject.getJSONArray("signatures");
            String[][] signatures = new String[jsonArraySignatures.length()][12];
            for (int i = 0; i < jsonArraySignatures.length(); i++){
                for (int j = 0; j < 12; j++){
                    signatures[i][j] = jsonArraySignatures.getJSONArray(i).getString(j);
                }
            }
            //setSignatures(new Signatures(signatures));
            Log.i("USER_signatures", signatures[0][0]);


        }else{

            SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
            String token = sharedPref.getString(keyToken, "");

            JSONObject jsonObject = new TokenRequester(token).getObject("http://angularjsauthentication20161012.azurewebsites.net/api/user");

            try {
                setId(jsonObject.optString("id"));
                setProgram(jsonObject.optString("program"));
                setAcademicCondition(jsonObject.optString("academicCondition"));
                setQuarter(jsonObject.optString("startingQuarter"));
                setLastCondition(jsonObject.optString("lastCondition"));
                setQuarterIndex(Double.parseDouble(jsonObject.optString("quarterIndex")));
                setGeneralIndex(Double.parseDouble(jsonObject.optString("generalIndex")));
                setValidatedCredits(Integer.parseInt(jsonObject.optString("validatedCredits")));
                setApprovedCredits(Integer.parseInt(jsonObject.optString("approvedCredits")));
                setApprovedQuarters(Integer.parseInt(jsonObject.optString("quarterCount")));


            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONArray jsonArrayAlert = new TokenRequester(token).getArray("http://angularjsauthentication20161012.azurewebsites.net/api/alerts");

            ArrayList<String> vals = new ArrayList<>();
            for (int i = 0; i < jsonArrayAlert.length(); i++){
                vals.add(jsonArrayAlert.getJSONObject(i).getString("alertText"));
            }
            setAlerts(vals.toArray(new String[vals.size()]));

            JSONArray jsonArraySignature = new TokenRequester(token).getArray("http://angularjsauthentication20161012.azurewebsites.net/api/classes");

            ArrayList<ClassRoom> classRooms = new ArrayList<>();
            for (int i = 0; i < jsonArraySignature.length(); i++){
                JSONObject jsonObjectSignature = jsonArraySignature.getJSONObject(i);

                String code = jsonObjectSignature.optString("code");
                String name = jsonObjectSignature.optString("name");
                String section = jsonObjectSignature.optString("section");
                String room = jsonObjectSignature.optString("room");
                String teacher = jsonObjectSignature.optString("teacher");
                String mon = jsonObjectSignature.optString("monday");
                String tue = jsonObjectSignature.optString("tuesday");
                String wed = jsonObjectSignature.optString("wednesday");
                String thu = jsonObjectSignature.optString("thursday");
                String fri = jsonObjectSignature.optString("friday");
                String sat = jsonObjectSignature.optString("saturday");

                ClassRoom classRoom = new ClassRoom();
                classRoom.setCode(code);
                classRoom.setName(name);
                classRoom.setSec(section);
                classRoom.setRoom(room);
                classRoom.setTeacher(teacher);
                classRoom.setMon(mon.split("/"));
                classRoom.setTue(tue.split(""));
                classRoom.setWed(wed.split("/"));
                classRoom.setThu(thu.split("/"));
                classRoom.setFri(fri.split("/"));
                classRoom.setSat(sat.split("/"));

                classRooms.add(classRoom);
            }
            setSignatures(classRooms.toArray(new ClassRoom[classRooms.size()]));

            //Then save it
            saveToJSON(activity);
        }
    }

    public void saveToJSON(Activity activity){

        String token = "";
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("id", getId());
            jsonObject.put("name", getName());
            jsonObject.put("program", getProgram());
            jsonObject.put("academic_condition", getAcademicCondition());
            jsonObject.put("quarter", getQuarter());
            jsonObject.put("last_condition", getLastCondition());
            jsonObject.put("quarter_index", getQuarterIndex());
            jsonObject.put("general_index", getGeneralIndex());
            jsonObject.put("validated_credits", getValidatedCredits());
            jsonObject.put("approved_credits", getApprovedCredits());
            jsonObject.put("approved_quarters", getApprovedQuarters());

            /*String[] vals = getAlerts();
            JSONArray jsonArrayVal = new JSONArray();
            for (String val : vals) {
                jsonArrayVal.put(val);
            }

            jsonObject.put("alerts", jsonArrayVal);

            String[][] signatures = getSignatures().getSignatures();
            JSONArray jsonArraySignatures = new JSONArray();
            for (String[] signature : signatures) {
                JSONArray jsonArraySignature = new JSONArray();
                for (String field : signature){
                    jsonArraySignature.put(field);
                }
                jsonArraySignatures.put(jsonArraySignature);
            }

            jsonObject.put("signatures", jsonArraySignatures);*/

            token = jsonObject.toString();
            Log.i("USER_json", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(keyObject, token);
        editor.apply();
    }

    /*private void saveToJSONTEST(Activity activity){

        String token = "";
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("id", "1065948");
            jsonObject.put("name", "Hector Andres Acosta Pozo");
            jsonObject.put("program", "(IDS 2015) INGENIERIA DE SOFTWARE");
            jsonObject.put("academic_condition", "NORMAL");
            jsonObject.put("quarter", "AGOSTO - OCTUBRE");
            jsonObject.put("last_condition", "MAYO - JULIO");
            jsonObject.put("quarter_index", "4.00");
            jsonObject.put("general_index", "3.83");
            jsonObject.put("validated_credits", "0");
            jsonObject.put("approved_credits", "23");
            jsonObject.put("approved_quarters", "4");

            String[] vals = {"Hola", "Que tal??"};

            JSONArray jsonArray = new JSONArray();
            for (String val : vals) {
                jsonArray.put(val);
            }

            jsonObject.put("alerts", jsonArray);

            JSONArray jsonArray1 = new JSONArray();
            JSONArray jsonArray2 = new JSONArray();

            jsonArray1.put("CBF201");
            jsonArray1.put("PROBABILIDAD Y ESTADISTICA");
            jsonArray1.put("02");
            jsonArray1.put("AJ404");
            jsonArray1.put("09/11");
            jsonArray1.put("");
            jsonArray1.put("09/11");
            jsonArray1.put("");
            jsonArray1.put("14/16");
            jsonArray1.put("A");
            jsonArray1.put("JOSE ANTONIO SCOTT GUILLEARD DEL CARMEN SANTO ALFONSO");
            jsonArray1.put(true);

            jsonArray2.put(jsonArray1);
            jsonArray2.put(jsonArray1);
            jsonArray2.put(jsonArray1);
            jsonArray2.put(jsonArray1);

            jsonObject.put("signatures", jsonArray2);

            token = jsonObject.toString();
            Log.i("USER_json", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(keyObject, token);
        editor.apply();
    }*/

    @SuppressLint("CommitPrefEdits")
    public void deleteStudent(){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(keyObject);
        editor.commit();
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
