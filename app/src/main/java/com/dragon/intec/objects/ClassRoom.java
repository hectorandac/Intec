package com.dragon.intec.objects;

/*
 * Created by HOME on 9/15/2016.
 */

import android.app.Activity;
import android.content.SharedPreferences;

import com.dragon.intec.components.TokenRequester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ClassRoom {

    private String id = "";
    private String type = "";
    private String code = "";
    private String name = "";
    private String sec = "";
    private String room = "";
    private String teacher = "";
    private String[] mon = new String[2];
    private String[] tue = new String[2];
    private String[] wed = new String[2];
    private String[] thu = new String[2];
    private String[] fri = new String[2];
    private String[] sat = new String[2];
    private String grades = "";
    private int space = 0;
    private String id_code_1 = "";
    private String id_code_2 = "";

    private static final String keyToken = "TOKEN";

    public String getGrades() {
        return grades;
    }

    void setGrades(String grades) {
        this.grades = grades;
    }

    ClassRoom(){}

    public String getId() {
        return id;
    }

    ClassRoom setId(String id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public ClassRoom setType(String type) {
        this.type = type;
        return this;
    }

    public String getSec() {
        return sec;
    }

    ClassRoom setSec(String sec) {
        this.sec = sec;
        return this;
    }

    public String getRoom() {
        return room;
    }

    ClassRoom setRoom(String room) {
        this.room = room;
        return this;
    }

    public String getTeacher() {
        return teacher;
    }

    ClassRoom setTeacher(String teacher) {
        this.teacher = teacher;
        return this;
    }

    public String[] getMon() {
        return mon;
    }

    ClassRoom setMon(String[] mon) {
        this.mon = mon;
        return this;
    }

    public String[] getTue() {
        return tue;
    }

    ClassRoom setTue(String[] tue) {
        this.tue = tue;
        return this;
    }

    public String[] getWed() {
        return wed;
    }

    ClassRoom setWed(String[] wed) {
        this.wed = wed;
        return this;
    }

    public String[] getThu() {
        return thu;
    }

    ClassRoom setThu(String[] thu) {
        this.thu = thu;
        return this;
    }

    public String[] getFri() {
        return fri;
    }

    ClassRoom setFri(String[] fri) {
        this.fri = fri;
        return this;
    }

    public String[] getSat() {
        return sat;
    }

    ClassRoom setSat(String[] sat) {
        this.sat = sat;
        return this;
    }

    public String getCode() {
        return code;
    }

    ClassRoom setCode(String code) {
        this.code = code;
        return this;
    }

    public String getId_code_1() {
        return id_code_1;
    }

    private void setId_code_1(String id_code_1) {
        this.id_code_1 = id_code_1;
    }

    public String getId_code_2() {
        return id_code_2;
    }

    private void setId_code_2(String id_code_2) {
        this.id_code_2 = id_code_2;
    }

    public int getSpace() {
        return space;
    }

    private void setSpace(int space) {
        this.space = space;
    }

    public String getName() {
        return name;
    }

    public ClassRoom setName(String name) {
        this.name = name;
        return this;
    }

    public String fixedTime(String start, String end) {
        String returner = "";

        if(start.length() > 0 && end.length() > 0)
            returner = start + "/" + end;

        return returner;
    }

    public static String fixedTime(String[] data) {
        String returner = "";
        String start = data[0];
        String end = data[1];

        if(start.length() > 0 && end.length() > 0)
            returner = start + "/" + end;

        return returner;
    }

    private static String[] timeToArray(String time) {
        String[] timeArr = {"", ""};

        if (time.contains("/"))
            timeArr = time.split("/");

        return timeArr;
    }

    public static ArrayList<ClassRoom> getClassrooms(Activity activity, String id) throws IOException, JSONException {

        SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
        String token = sharedPref.getString(keyToken, "");

        ArrayList<ClassRoom> classrooms = new ArrayList<>();

        JSONArray jsonClassrooms = new TokenRequester(token).getArray("http://angularjsauthentication20161012.azurewebsites.net/api/classes?id=" + id, "GET");
        for(int i = 0; i < jsonClassrooms.length(); i++){
            JSONObject classroom = jsonClassrooms.getJSONObject(i);
            ClassRoom myClassroom = new ClassRoom();

            myClassroom.setId(classroom.optString("id"));
            myClassroom.setType(classroom.optString("type"));
            myClassroom.setCode(classroom.optString("code"));
            myClassroom.setSec(classroom.optString("section"));
            myClassroom.setRoom(classroom.optString("room"));
            myClassroom.setTeacher(classroom.optString("teacher"));
            myClassroom.setMon(timeToArray(classroom.optString("monday")));
            myClassroom.setTue(timeToArray(classroom.optString("tuesday")));
            myClassroom.setWed(timeToArray(classroom.optString("wednesday")));
            myClassroom.setThu(timeToArray(classroom.optString("thursday")));
            myClassroom.setFri(timeToArray(classroom.optString("friday")));
            myClassroom.setSat(timeToArray(classroom.optString("saturday")));

            classrooms.add(myClassroom);
        }

        return classrooms;
    }

    public static ClassRoom parseToClassRoom(JSONObject classroom){

        ClassRoom myClassroom = new ClassRoom();

        myClassroom.setId(classroom.optString("id"));
        myClassroom.setName(classroom.optString("name"));
        myClassroom.setType(classroom.optString("type"));
        myClassroom.setCode(classroom.optString("code"));
        myClassroom.setSec(classroom.optString("section"));
        myClassroom.setRoom(classroom.optString("room"));
        myClassroom.setTeacher(classroom.optString("teacher"));
        myClassroom.setMon(timeToArray(classroom.optString("monday")));
        myClassroom.setTue(timeToArray(classroom.optString("tuesday")));
        myClassroom.setWed(timeToArray(classroom.optString("wednesday")));
        myClassroom.setThu(timeToArray(classroom.optString("thursday")));
        myClassroom.setFri(timeToArray(classroom.optString("friday")));
        myClassroom.setSat(timeToArray(classroom.optString("saturday")));
        myClassroom.setSpace(classroom.optInt("space"));
        myClassroom.setId_code_1(classroom.optString("classId"));
        myClassroom.setId_code_2(classroom.optString("classId_2"));

        return myClassroom;
    }


}
