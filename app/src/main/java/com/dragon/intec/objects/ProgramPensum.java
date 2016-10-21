package com.dragon.intec.objects;

/*
 * Created by Hector Acosta on 10/20/2016.
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.dragon.intec.R;
import com.dragon.intec.components.TokenRequester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProgramPensum {

    int id;
    String name = "";
    Activity activity;

    private static final String keyToken = "TOKEN";

    public ProgramPensum(Activity activity){
        this.activity = activity;
    }

    public class PensumSignature extends Signature{

        private int quarterIndex = 0;

        public PensumSignature(String id, String code, String name, String cr, String prerequisite, String area, String req_cred, String uriPDF, int quarterIndex) {
            super(id, code, name, cr, prerequisite, area, req_cred, uriPDF);

            this.quarterIndex = quarterIndex;

        }

        public PensumSignature(){}

        int getQuarterIndex() {
            return quarterIndex;
        }

        public void setQuarterIndex(int quarterIndex) {
            this.quarterIndex = quarterIndex;
        }
    }

    public HashMap<String, List<PensumSignature>> getPensum(int id) throws IOException, JSONException {

        SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
        String token = sharedPref.getString(keyToken, "");

        JSONArray jsonArray = new TokenRequester(token).getArray("http://angularjsauthentication20161012.azurewebsites.net/api/pensum?id="+id);
        List<PensumSignature> pensumSignatures = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            PensumSignature pensumSignature = new PensumSignature(jsonObject.optString("id"),
                    jsonObject.optString("code"),
                    jsonObject.optString("nameClass"),
                    jsonObject.optString("cre"),
                    jsonObject.optString("preRequirements"),
                    jsonObject.optString("area"),
                    jsonObject.optString("PreRequirementCredit"),
                    jsonObject.optString("uriPdf"),
                    jsonObject.optInt("quarterIndex"));

            pensumSignatures.add(pensumSignature);
        }

        List<Integer> indexQuarters = getIndexes(pensumSignatures);
        return createHash(indexQuarters, pensumSignatures);
    }

    private List<Integer> getIndexes(List<PensumSignature> pensumSignatures){

        List<Integer> indexes = new ArrayList<>();

        for(PensumSignature pensumSignature : pensumSignatures){
            if(!indexes.contains(pensumSignature.getQuarterIndex())){
                indexes.add(pensumSignature.getQuarterIndex());
            }
        }

        return indexes;
    }

    public List<String> quarters = new ArrayList<>();

    private HashMap<String, List<PensumSignature>> createHash(List<Integer> indexes, List<PensumSignature> map){
        HashMap<String, List<PensumSignature>> hash = new HashMap<>();

        for(Integer index : indexes){

            List<PensumSignature> pensumSignatures = new ArrayList<>();
            PensumSignature titlePensum = new PensumSignature();
            pensumSignatures.add(titlePensum);

            for(PensumSignature pensumSignature : map){
                if(pensumSignature.getQuarterIndex() == index){
                    pensumSignatures.add(pensumSignature);
                }
            }

            String heading = activity.getResources().getString(R.string.quarter_s) + " " + index;
            quarters.add(heading);

            hash.put(heading, pensumSignatures);

        }

        return hash;
    }

}
