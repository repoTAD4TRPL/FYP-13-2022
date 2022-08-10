package com.example.apprute.model;

import com.google.gson.annotations.SerializedName;

public class PortRoute {

	@SerializedName("harbor_end")
	private int harborEnd;

	@SerializedName("harbor_start")
	private int harborStart;

	@SerializedName("id")
	private int id;

	@SerializedName("routeName")
	private String routeName;

	public int getHarborEnd(){
		return harborEnd;
	}

	public int getHarborStart(){
		return harborStart;
	}

	public int getId(){
		return id;
	}

	public String getRouteName(){
		return routeName;
	}
}