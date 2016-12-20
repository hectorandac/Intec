package com.dragon.intec.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * Created by hecto on 12/16/2016.
 */

public class SignatureClasses extends Signature {

    private ClassRoom[] classes = null;

    public ClassRoom[] getClasses() {
        return classes;
    }

    public void setClasses(ClassRoom[] classes) {
        this.classes = classes;
    }

    public static SignatureClasses parseToSignatureClasses(JSONObject signatures_classes) throws JSONException {
        SignatureClasses mySignature = new SignatureClasses();

        mySignature.setId(signatures_classes.optString("id"));
        mySignature.setCode(signatures_classes.optString("code"));
        mySignature.setName(signatures_classes.optString("nameClass"));
        mySignature.setArea(signatures_classes.optString("area"));
        mySignature.setCr(signatures_classes.optString("cre"));
        mySignature.setPrerequisite(signatures_classes.optString("preRequirements"));
        mySignature.setReq_cred(signatures_classes.optString("preRequirementCredits"));

        JSONArray classesArray = signatures_classes.getJSONArray("classes");
        mySignature.classes = new ClassRoom[classesArray.length()];
        for(int i = 0; i < classesArray.length(); i++){
            mySignature.classes[i] = ClassRoom.parseToClassRoom(classesArray.getJSONObject(i));
        }


        return mySignature;
    }

}
