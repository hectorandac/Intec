package com.dragon.intec.objects;
/*
 * Created by Hector Acosta on 11/2/2016.
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.dragon.intec.components.TokenRequester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GradesRev {

    private static final String keyToken = "TOKEN";

    private int consultId;
    private String signature = "";
    private int grade = 0;
    private String teacher = "";
    private Date timing = null;
    private int status;

    public GradesRev() {
    }

    public int getConsultId() {
        return consultId;
    }

    public void setConsultId(int consultId) {
        this.consultId = consultId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Date getTiming() {
        return timing;
    }

    public void setTiming(Date timing) {
        this.timing = timing;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public GradesRev[] getRevisions(Activity activity) throws IOException, JSONException, ParseException {

        SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
        String token = sharedPref.getString(keyToken, "");

        JSONArray jsonArray = new TokenRequester(token).getArray("http://angularjsauthentication20161012.azurewebsites.net/api/GradesRev");

        List<GradesRev> gradesRevs = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            GradesRev gradesRev = new GradesRev();

            String dateStr = jsonObject.optString("timing").replace('T', ' ');
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = sdf.parse(dateStr);

            Log.i("DATE##", date.toString());

            gradesRev.setSignature(jsonObject.optString("signature"));
            gradesRev.setGrade(jsonObject.optInt("grade"));
            gradesRev.setTeacher(jsonObject.optString("teacher"));
            gradesRev.setTiming(date);
            gradesRev.setStatus(jsonObject.optInt("status"));
            gradesRev.setConsultId(jsonObject.optInt("consultId"));

            gradesRevs.add(gradesRev);

        }

        return gradesRevs.toArray(new GradesRev[gradesRevs.size()]);
    }
}
