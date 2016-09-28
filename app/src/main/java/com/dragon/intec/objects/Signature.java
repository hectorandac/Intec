package com.dragon.intec.objects;/*
 * Created by HOME on 9/26/2016.
 */

public class Signature {

    private String code = "";
    private String signature = "";
    private String cr = "";
    private String prerequisite = "";
    private String req_cred = "";
    private String quarter = "";
    private String uriPDF = "";

    public Signature(String code, String signature, String cr, String prerequisite, String req_cred, String quarter, String uriPDF) {
        this.code = code;
        this.signature = signature;
        this.cr = cr;
        this.prerequisite = prerequisite;
        this.req_cred = req_cred;
        this.quarter = quarter;
        this.uriPDF = uriPDF;
    }

    public String getCode() {
        return code;
    }

    public Signature setCode(String code) {
        this.code = code;
        return this;
    }

    public String getSignature() {
        return signature;
    }

    public Signature setSignature(String signature) {
        this.signature = signature;
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

    public String getQuarter() {
        return quarter;
    }

    public Signature setQuarter(String quarter) {
        this.quarter = quarter;
        return this;
    }

    public String getUriPDF() {
        return uriPDF;
    }

    public Signature setUriPDF(String uriPDF) {
        this.uriPDF = uriPDF;
        return this;
    }
}
