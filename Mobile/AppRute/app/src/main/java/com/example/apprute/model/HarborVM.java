package com.example.apprute.model;

import com.google.gson.annotations.SerializedName;

public class HarborVM {
    @SerializedName("harborName")
    private String harborName;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    public String getHarborName(){
        return harborName;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

}
