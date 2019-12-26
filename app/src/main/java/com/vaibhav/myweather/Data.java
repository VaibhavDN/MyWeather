package com.vaibhav.myweather;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Data{

	@SerializedName("country")
	private String country;

	@SerializedName("current")
	private Current current;

	@SerializedName("city")
	private String city;

	@SerializedName("location")
	private Location location;

	@SerializedName("state")
	private String state;

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setCurrent(Current current){
		this.current = current;
	}

	public Current getCurrent(){
		return current;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setLocation(Location location){
		this.location = location;
	}

	public Location getLocation(){
		return location;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"country = '" + country + '\'' + 
			",current = '" + current + '\'' + 
			",city = '" + city + '\'' + 
			",location = '" + location + '\'' + 
			",state = '" + state + '\'' + 
			"}";
		}
}