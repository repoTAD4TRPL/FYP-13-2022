package com.example.apprute.model;

import com.google.gson.annotations.SerializedName;

public class TouristAttraction {

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

	@SerializedName("category")
	private int category;

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

	public int getCategory(){
		return category;
	}

	public double getLongitude(){
		return longitude;
	}
}