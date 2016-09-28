package com.dragon.intec.objects;/*
 * Created by HOME on 9/26/2016.
 */

import java.util.List;

public class Quarter {

    private String name;
    private List<ClassRoom> classRooms;

    public Quarter(String name, List<ClassRoom> classRooms){
        this.name = name;
        this.classRooms = classRooms;
    }

    public List<Signature> getSignatures(){

        for(ClassRoom classRoom : classRooms){

        }

        return null;
    }

}
