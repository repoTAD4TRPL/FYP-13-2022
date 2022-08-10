package com.example.apprute.model;

import com.google.gson.annotations.SerializedName;

public class Route {

	@SerializedName("session")
	private String session;

	@SerializedName("time")
	private String time;

	@SerializedName("routeName")
	private String routeName;

	public String getSession(){
		return session;
	}

	public String getTime(){
		return time;
	}

	public String getRouteName(){
		return routeName;
	}
}