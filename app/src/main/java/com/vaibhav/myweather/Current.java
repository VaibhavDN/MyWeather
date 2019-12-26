package com.vaibhav.myweather;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Current{

	@SerializedName("weather")
	private Weather weather;

	@SerializedName("pollution")
	private Pollution pollution;

	public void setWeather(Weather weather){
		this.weather = weather;
	}

	public Weather getWeather(){
		return weather;
	}

	public void setPollution(Pollution pollution){
		this.pollution = pollution;
	}

	public Pollution getPollution(){
		return pollution;
	}

	@Override
 	public String toString(){
		return 
			"Current{" + 
			"weather = '" + weather + '\'' + 
			",pollution = '" + pollution + '\'' + 
			"}";
		}
}