package com.example.apprute.model;

import com.google.gson.annotations.SerializedName;

public class Harbor {

	@SerializedName("route_id")
	private int routeId;

	@SerializedName("latitude")
	private double latitude;

	@SerializedName("description")
	private String description;

	@SerializedName("harbor_Name")
	private String harborName;

	@SerializedName("location")
	private String location;

	@SerializedName("id")
	private int id;

	@SerializedName("longitude")
	private double longitude;

	public int getRouteId(){
		return routeId;
	}

	public double getLatitude(){
		return latitude;
	}

	public String getDescription(){
		return description;
	}

	public String getHarborName(){
		return harborName;
	}

	public String getLocation(){
		return location;
	}

	public int getId(){
		return id;
	}

	public double getLongitude(){
		return longitude;
	}
}