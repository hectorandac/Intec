package com.dragon.intec.objects;

/*
 * Created by Hector Acosta on 10/20/2016.
 */

import android.app.Activity;
import android.content.SharedPreferences;

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
    private Activity activity;

    private static final String keyToken = "TOKEN";

    public ProgramPensum(Activity activity){
        this.activity = activity;
    }

    public class PensSignature extends Signature{

        private int quarterIndex = 0;

        PensSignature(String id, String code, String name, String cr, String prerequisite, String area, String req_cred, String uriPDF, int quarterIndex) {
            super(id, code, name, cr, prerequisite, area, req_cred, uriPDF);

            this.quarterIndex = quarterIndex;

        }

        PensSignature(){}

        int getQuarterIndex() {
            return quarterIndex;
        }

        public void setQuarterIndex(int quarterIndex) {
            this.quarterIndex = quarterIndex;
        }
    }

    public HashMap<String, List<PensSignature>> getPensum(int id) throws IOException, JSONException {

        SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
        String token = sharedPref.getString(keyToken, "");

        JSONArray jsonArray = new TokenRequester(token).getArray("http://angularjsauthentication20161012.azurewebsites.net/api/pensum?id="+id, "GET");
        List<PensSignature> pensSignatures = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            PensSignature pensSignature = new PensSignature(jsonObject.optString("id"),
                    jsonObject.optString("code"),
                    jsonObject.optString("nameClass"),
                    jsonObject.optString("cre"),
                    jsonObject.optString("preRequirements"),
                    jsonObject.optString("area"),
                    jsonObject.optString("PreRequirementCredit"),
                    jsonObject.optString("uriPdf"),
                    jsonObject.optInt("quarterIndex"));

            pensSignatures.add(pensSignature);
        }

        List<Integer> indexQuarters = getIndexes(pensSignatures);
        return createHash(indexQuarters, pensSignatures);
    }

    private List<Integer> getIndexes(List<PensSignature> pensSignatures){

        List<Integer> indexes = new ArrayList<>();

        for(PensSignature pensSignature : pensSignatures){
            if(!indexes.contains(pensSignature.getQuarterIndex())){
                indexes.add(pensSignature.getQuarterIndex());
            }
        }

        return indexes;
    }

    public List<String> quarters = new ArrayList<>();

    private HashMap<String, List<PensSignature>> createHash(List<Integer> indexes, List<PensSignature> map){
        HashMap<String, List<PensSignature>> hash = new HashMap<>();

        for(Integer index : indexes){

            List<PensSignature> pensSignatures = new ArrayList<>();
            PensSignature titlePensum = new PensSignature();
            pensSignatures.add(titlePensum);

            for(PensSignature pensSignature : map){
                if(pensSignature.getQuarterIndex() == index){
                    pensSignatures.add(pensSignature);
                }
            }

            String heading = activity.getResources().getString(R.string.quarter_s) + " " + index;
            quarters.add(heading);

            hash.put(heading, pensSignatures);

        }

        return hash;
    }

}
