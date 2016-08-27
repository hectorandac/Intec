package com.dragon.intec.objects;/*
 * Created by HOME on 8/27/2016.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Signatures implements Parcelable{

    private String[][] signatures;

    public Signatures(String[][] signatures) {
        this.signatures = signatures;
    }

    public String[][] getSignatures() {
        return signatures;
    }

    public Signatures setSignatures(String[][] signatures) {
        this.signatures = signatures;
        return this;
    }

    protected Signatures(Parcel in) {
        String[][] array;
        int N = in.readInt();
        array = new String[N][12];
        for (int i=0; i<N; i++) {
            array[i] = in.createStringArray();
        }
        signatures = array;
    }

    public static final Creator<Signatures> CREATOR = new Creator<Signatures>() {
        @Override
        public Signatures createFromParcel(Parcel in) {
            return new Signatures(in);
        }

        @Override
        public Signatures[] newArray(int size) {
            return new Signatures[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int j) {
        final int N = signatures.length;
        parcel.writeInt(N);
        for (String[] signature : signatures) {
            parcel.writeStringArray(signature);
        }
    }
}
