package com.example.apprute.model;

import com.google.gson.annotations.SerializedName;

public class Schedule {

    @SerializedName("session")
    private String session;

    @SerializedName("portRoute_id")
    private int portRouteId;

    @SerializedName("id")
    private int id;

    @SerializedName("time")
    private String time;

    @SerializedName("routeName")
    private String routeName;

    public String getSession(){
        return session;
    }

    public int getPortRouteId(){
        return portRouteId;
    }

    public int getId(){
        return id;
    }

    public String getTime(){
        return time;
    }

    public String getRouteName(){
        return routeName;
    }
}
