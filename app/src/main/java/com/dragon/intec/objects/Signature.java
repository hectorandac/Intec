package com.dragon.intec.objects;/*
 * Created by HOME on 9/26/2016.
 */

import android.app.Activity;
import android.content.SharedPreferences;

import com.dragon.intec.components.TokenRequester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Signature {

    private static final String keyToken = "TOKEN";

    private String id = "";
    private String code = "";
    private String name = "";
    private String cr = "";
    private String prerequisite = "";
    private String area = "";
    private String req_cred = "";
    private String uriPDF = "";

    public Signature(String code, String name, String cr, String prerequisite, String req_cred, String quarter, String uriPDF) {
        this.code = code;
        this.name = name;
        this.cr = cr;
        this.prerequisite = prerequisite;
        this.req_cred = req_cred;
        this.uriPDF = uriPDF;
    }

    private Signature(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCode() {
        return code;
    }

    private Signature setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public Signature setName(String name) {
        this.name = name;
        return this;
    }

    public String getCr() {
        return cr;
    }

    public Signature setCr(String cr) {
        this.cr = cr;
        return this;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public Signature setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
        return this;
    }

    public String getReq_cred() {
        return req_cred;
    }

    public Signature setReq_cred(String req_cred) {
        this.req_cred = req_cred;
        return this;
    }

    public String getUriPDF() {
        return uriPDF;
    }

    public Signature setUriPDF(String uriPDF) {
        this.uriPDF = uriPDF;
        return this;
    }

    public static ArrayList<Signature> getSignatures(Activity activity, String name) throws IOException, JSONException {

        SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
        String token = sharedPref.getString(keyToken, "");

        ArrayList<Signature> signatures = new ArrayList<>();

        JSONArray jsonSignatures = new TokenRequester(token).getArray("http://angularjsauthentication20161012.azurewebsites.net/api/signature?name=" + name);
        for(int i = 0; i < jsonSignatures.length(); i++){
            JSONObject signature = jsonSignatures.getJSONObject(i);
            Signature mySignature = new Signature();

            mySignature.setId(signature.optString("id"));
            mySignature.setCode(signature.optString("code"));
            mySignature.setName(signature.optString("nameClass"));
            mySignature.setArea(signature.optString("area"));
            mySignature.setCr(signature.optString("cre"));
            mySignature.setPrerequisite(signature.optString("preRequirements"));
            mySignature.setReq_cred(signature.optString("preRequirementCredits"));
            //(COMING SOON)mySignature.setUriPDF(signature.optString("uriPdf"));

            signatures.add(mySignature);
        }

        return signatures;
    }

}
