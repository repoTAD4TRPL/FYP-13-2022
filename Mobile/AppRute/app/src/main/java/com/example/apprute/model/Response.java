package com.example.apprute.model;

import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("latitude")
	private double latitude;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("location")
	private String location;

	@SerializedName("id")
	private int id;

	@SerializedName("longitude")
	private double longitude;

	public double getLatitude(){
		return latitude;
	}

	public String getName(){
		return name;
	}

	public String getDescription(){
		return description;
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