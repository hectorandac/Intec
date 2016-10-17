package com.dragon.intec.components;

/*
 * Created by hecto on 10/16/2016.
 */

import android.content.Context;

import com.dragon.intec.R;

public class CubInfoConstant {

    public static String location(int identifier, Context context) {
        String returner = "";

        if (identifier < 5)
            returner = context.getResources().getString(R.string.location_1);
        else if (identifier < 9)
            returner = context.getResources().getString(R.string.location_2);
        else {
            returner = context.getResources().getString(R.string.location_3);
        }

        return returner;
    }

    public static String Status(int identifier, Context context) {
        String returner = "";

        switch (identifier){
            case 0:
                returner = context.getResources().getString(R.string.status_1); break;
            case 1:
                returner = context.getResources().getString(R.string.status_2); break;
            case 2:
                returner = context.getResources().getString(R.string.status_3); break;
            case 3:
                returner = context.getResources().getString(R.string.status_4); break;
        }

        return returner;
    }
}