package com.dragon.intec.objects;

/*
 * Created by HOME on 9/15/2016.
 */

import android.app.Activity;

public class ClassRoom {

    Activity activity;

    private int id = 0;
    private String type = "";
    private String sec = "";
    private String room = "";
    private String teacher = "";
    private String[] mon = new String[2];
    private String[] tue = new String[2];
    private String[] wed = new String[2];
    private String[] thu = new String[2];
    private String[] fri = new String[2];
    private String[] sat = new String[2];
    private String area = "";
    private String code = "";
    private String name = "";

    public ClassRoom (){

    }

    public int getId() {
        return id;
    }

    public ClassRoom setId(int id) {
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

    public ClassRoom setSec(String sec) {
        this.sec = sec;
        return this;
    }

    public String getRoom() {
        return room;
    }

    public ClassRoom setRoom(String room) {
        this.room = room;
        return this;
    }

    public String getTeacher() {
        return teacher;
    }

    public ClassRoom setTeacher(String teacher) {
        this.teacher = teacher;
        return this;
    }

    public String[] getMon() {
        return mon;
    }

    public ClassRoom setMon(String[] mon) {
        this.mon = mon;
        return this;
    }

    public String[] getTue() {
        return tue;
    }

    public ClassRoom setTue(String[] tue) {
        this.tue = tue;
        return this;
    }

    public String[] getWed() {
        return wed;
    }

    public ClassRoom setWed(String[] wed) {
        this.wed = wed;
        return this;
    }

    public String[] getThu() {
        return thu;
    }

    public ClassRoom setThu(String[] thu) {
        this.thu = thu;
        return this;
    }

    public String[] getFri() {
        return fri;
    }

    public ClassRoom setFri(String[] fri) {
        this.fri = fri;
        return this;
    }

    public String[] getSat() {
        return sat;
    }

    public ClassRoom setSat(String[] sat) {
        this.sat = sat;
        return this;
    }

    public String getArea() {
        return area;
    }

    public ClassRoom setArea(String area) {
        this.area = area;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ClassRoom setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public ClassRoom setName(String name) {
        this.name = name;
        return this;
    }
}
