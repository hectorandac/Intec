package com.dragon.intec.objects;/*
 * Created by HOME on 8/25/2016.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class Student implements Parcelable{

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
    private Signatures signatures;

    protected Student(Parcel in) {
        token = in.readString();
        id = in.readString();
        name = in.readString();
        program = in.readString();
        academicCondition = in.readString();
        quarter = in.readString();
        lastCondition = in.readString();
        quarterIndex = in.readDouble();
        generalIndex = in.readDouble();
        validatedCredits = in.readInt();
        approvedCredits = in.readInt();
        approvedQuarters = in.readInt();
        alerts = in.createStringArray();
        signatures = in.readParcelable(Signatures.class.getClassLoader());
    }

    public String getToken() {
        return token;
    }

    public Student setToken(String token) {
        this.token = token;
        return this;
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public Signatures getSignatures() {
        return signatures;
    }

    private Student setSignatures(Signatures signatures) {
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

    public Student(String token, Activity activity){
        this.token = token;
        this.activity = activity;

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(keyToken, token);

        editor.apply();
    }

    public void getData() throws IOException, JSONException {

        boolean internetConnection = true;

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
            setSignatures(new Signatures(signatures));
            Log.i("USER_signatures", signatures[0][0]);


        }else{

            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
            String jsonOBJ = sharedPref.getString(keyToken, "bearer 8N9DyxIJFfhjGawh07un8tQPEsSSO2R7Grx3gpaot_CXkt9B4Hsxoy4aU0zEy96ZuYUpco_CHcWm7X-sG75QwD8TfIn7GtM2nsA9RmtGyivKbNHngL_vw2jt2pS8iPiK_shXBqqvjPhyofxvSzjt3nPv7uOqfEr2Nbz-MN3jKf48SWLObC6kvc8Z8I5ugrtNEifEg4zszoX5TXtHJrEVUBOsohhMtgAIgcrpS8g5JYk");



            JSONObject jsonObject = null;

            HttpClient client = new DefaultHttpClient(new BasicHttpParams());
            HttpGet httpGet = new HttpGet("http://angularjsauthentication20161012.azurewebsites.net/api/user");
            httpGet.addHeader("Authorization", jsonOBJ);
            HttpResponse response = null;
            try {
                response = client.execute(httpGet);
            }catch (Exception e){

            }

            String a = EntityUtils.toString(response.getEntity());

            System.out.println("###" + a + jsonOBJ + "  ss");
            jsonObject = new JSONObject(a);

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

            /*ArrayList<String> vals = new ArrayList<>();
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
                    Log.i("USER_signatures", signatures[i][j]);
                }
            }
            setSignatures(new Signatures(signatures));*/

                //Then save it
                saveToJSON(activity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveToJSON(Activity activity){

        String jsonOBJ = "";
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

    /*private void saveToJSONTEST(Activity activity){

        String jsonOBJ = "";
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

            jsonOBJ = jsonObject.toString();
            Log.i("USER_json", jsonOBJ);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(keyObject, jsonOBJ);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(token);
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(program);
        parcel.writeString(academicCondition);
        parcel.writeString(quarter);
        parcel.writeString(lastCondition);
        parcel.writeDouble(quarterIndex);
        parcel.writeDouble(generalIndex);
        parcel.writeInt(validatedCredits);
        parcel.writeInt(approvedCredits);
        parcel.writeInt(approvedQuarters);
        parcel.writeStringArray(alerts);
        parcel.writeParcelable(signatures, i);
    }
}
