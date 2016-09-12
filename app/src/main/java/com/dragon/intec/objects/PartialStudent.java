package com.dragon.intec.objects;/*
 * Created by HOME on 9/11/2016.
 */

public class PartialStudent {

    private String name = "";
    private String id = "";

    public PartialStudent(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PartialStudent setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public PartialStudent setId(String id) {
        this.id = id;
        return this;
    }
}
