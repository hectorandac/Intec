package com.dragon.intec.objects;

/*
 * Created by HOME on 8/25/2016.
 */

import android.app.Activity;
import android.content.SharedPreferences;

import com.dragon.intec.components.TokenRequester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Student{

    private String token;
    private static final String keyToken = "TOKEN";
    private Activity activity;

    private String id;
    private String name;
    private String program;
    private String credits;
    private int programId;
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

    private void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
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

    private Student setQuarterIndex(double quarterIndex) {
        this.quarterIndex = quarterIndex;
        return this;
    }

    private Student setLastCondition(String lastCondition) {
        this.lastCondition = lastCondition;
        return this;
    }

    private Student setQuarter(String quarter) {
        this.quarter = quarter;
        return this;
    }

    private Student setAcademicCondition(String academic_condition) {
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

        SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
        String token = sharedPref.getString(keyToken, "");

        JSONObject jsonObject = new TokenRequester(token).getJSONObject("http://angularjsauthentication20161012.azurewebsites.net/api/user");

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
            setProgramId(jsonObject.optInt("programId"));
            setCredits(jsonObject.optString("credits"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray jsonArrayAlert = new TokenRequester(token).getArray("http://angularjsauthentication20161012.azurewebsites.net/api/alerts", "GET");

        ArrayList<String> vals = new ArrayList<>();
        for (int i = 0; i < jsonArrayAlert.length(); i++) {
            vals.add(jsonArrayAlert.getJSONObject(i).getString("alertText"));
        }
        setAlerts(vals.toArray(new String[vals.size()]));

        JSONArray jsonArraySignature = new TokenRequester(token).getArray("http://angularjsauthentication20161012.azurewebsites.net/api/classes", "GET");

        ArrayList<ClassRoom> classRooms = new ArrayList<>();
        for (int i = 0; i < jsonArraySignature.length(); i++) {
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
            int grades = jsonObjectSignature.optInt("grades");

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

            if (grades > 70) {
                classRoom.setGrades(grades + "");
            }

            classRooms.add(classRoom);
        }
        setSignatures(classRooms.toArray(new ClassRoom[classRooms.size()]));

    }
}
