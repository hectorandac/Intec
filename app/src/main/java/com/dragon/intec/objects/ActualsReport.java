package com.dragon.intec.objects;

/*
 * Created by hecto on 11/9/2016.
 */

public class ActualsReport {
    private String signatureName;
    private String signatureCode;
    private String section;
    private String observation;
    private int  id;
    private double grade;
    private int credits;
    private int cicle;

    public int getCicle() {
        return cicle;
    }

    public void setCicle(int cicle) {
        this.cicle = cicle;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getSignatureName() {
        return signatureName;
    }

    public void setSignatureName(String signatureName) {
        this.signatureName = signatureName;
    }

    public String getSignatureCode() {
        return signatureCode;
    }

    public void setSignatureCode(String signatureCode) {
        this.signatureCode = signatureCode;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        try {
            this.grade = Double.parseDouble(grade);
        }catch (Exception ex){
            ex.printStackTrace();
            this.grade = 0.0;
        }
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
