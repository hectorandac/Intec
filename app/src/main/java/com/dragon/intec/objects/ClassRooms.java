package com.dragon.intec.objects;/*
 * Created by HOME on 9/15/2016.
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

public class ClassRooms {

    private Activity activity;
    private static final String keyObject = "CLASSROOMS";
    private String url = "";
    private String name = null;

    private ArrayList<ClassRoom> classRooms = new ArrayList<>();

    public ClassRooms(Activity activity, String name) {
        this.activity = activity;
        url = "http://intecapp.azurewebsites.net/api/signature";
        if (name != null) {
            url = "http://intecapp.azurewebsites.net/api/signature?name="+name+"";
            name = name;
        }
    }

    public ArrayList<ClassRoom> getClassRooms() {
        return classRooms;
    }

    public ClassRooms setClassRooms(ArrayList<ClassRoom> classRooms) {
        this.classRooms = classRooms;
        return this;
    }

    public void addClassRoom(ClassRoom classRoom) {
        classRooms.add(classRoom);
    }

    public void removeClassRoom(ClassRoom classRoom) {
        classRooms.remove(classRoom);
    }

    public ClassRooms getByArea(String area) {
        ClassRooms classRoomsSend = new ClassRooms(null, null);

        for (ClassRoom classRoom : this.getClassRooms())
        {
            if (classRoom.getArea().equals(area)){
                classRoomsSend.addClassRoom(classRoom);
            }
        }

        return classRoomsSend;
    };

    public boolean getData() throws IOException, JSONException {

        boolean internetConnection = true;
        boolean returner = false;

        if(!internetConnection){

            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
            String jsonOBJ = sharedPref.getString(keyObject, "");
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(jsonOBJ);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(jsonArray != null) {

                for (int i = 0; i < jsonArray.length(); i++) {

                    ClassRoom classRoom = new ClassRoom();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    classRoom.setId(jsonObject.optInt("id"));
                    classRoom.setType(jsonObject.optString("type"));
                    classRoom.setSec(jsonObject.optString("sec"));
                    classRoom.setRoom(jsonObject.optString("aula"));
                    classRoom.setTeacher(jsonObject.optString("teacher"));

                    JSONArray mon = jsonObject.getJSONArray("mon");
                    JSONArray tue = jsonObject.getJSONArray("mon");
                    JSONArray wed = jsonObject.getJSONArray("mon");
                    JSONArray thu = jsonObject.getJSONArray("mon");
                    JSONArray fri = jsonObject.getJSONArray("mon");
                    JSONArray sat = jsonObject.getJSONArray("mon");

                    classRoom.setMon(new String[]{mon.optString(0), mon.optString(1)});
                    classRoom.setTue(new String[]{tue.optString(0), tue.optString(1)});
                    classRoom.setWed(new String[]{wed.optString(0), wed.optString(1)});
                    classRoom.setThu(new String[]{thu.optString(0), thu.optString(1)});
                    classRoom.setFri(new String[]{fri.optString(0), fri.optString(1)});
                    classRoom.setSat(new String[]{sat.optString(0), sat.optString(1)});

                    classRoom.setArea(jsonObject.optString("area"));
                    classRoom.setCode(jsonObject.optString("code"));
                    classRoom.setName(jsonObject.optString("name"));

                    String name = jsonObject.optString("name");

                    if(name.contains(this.name))
                        addClassRoom(classRoom);

                    if(this.name == null)
                        addClassRoom(classRoom);
                }

                returner = true;
            }

        }else{

            JSONArray jsonArray = readJsonFromUrl(url);
            Log.i("USER_json", jsonArray.toString());

            if(jsonArray != null) {

                for (int i = 0; i < jsonArray.length(); i++) {

                    ClassRoom classRoom = new ClassRoom();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    classRoom.setId(jsonObject.optInt("id"));
                    classRoom.setType(jsonObject.optString("type"));
                    classRoom.setSec(jsonObject.optString("sec"));
                    classRoom.setRoom(jsonObject.optString("aula"));
                    classRoom.setTeacher(jsonObject.optString("teacher"));

                    JSONArray mon = jsonObject.getJSONArray("mon");
                    JSONArray tue = jsonObject.getJSONArray("tue");
                    JSONArray wed = jsonObject.getJSONArray("wed");
                    JSONArray thu = jsonObject.getJSONArray("thu");
                    JSONArray fri = jsonObject.getJSONArray("fri");
                    JSONArray sat = jsonObject.getJSONArray("sat");

                    classRoom.setMon(new String[]{mon.optString(0), mon.optString(1)});
                    classRoom.setTue(new String[]{tue.optString(0), tue.optString(1)});
                    classRoom.setWed(new String[]{wed.optString(0), wed.optString(1)});
                    classRoom.setThu(new String[]{thu.optString(0), thu.optString(1)});
                    classRoom.setFri(new String[]{fri.optString(0), fri.optString(1)});
                    classRoom.setSat(new String[]{sat.optString(0), sat.optString(1)});

                    classRoom.setArea(jsonObject.optString("area"));
                    classRoom.setCode(jsonObject.optString("code"));
                    classRoom.setName(jsonObject.optString("name"));
                    classRoom.setCredits(jsonObject.optString("credits"));

                    addClassRoom(classRoom);
                }

                returner = true;
                //saveToJSON(activity, designedKey);
            }
        }

        return returner;
    }

    public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONArray(jsonText);
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
}
