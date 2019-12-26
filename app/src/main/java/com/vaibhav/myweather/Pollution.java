package com.vaibhav.myweather;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Pollution{

	@SerializedName("aqius")
	private int aqius;

	@SerializedName("maincn")
	private String maincn;

	@SerializedName("ts")
	private String ts;

	@SerializedName("mainus")
	private String mainus;

	@SerializedName("aqicn")
	private int aqicn;

	public void setAqius(int aqius){
		this.aqius = aqius;
	}

	public int getAqius(){
		return aqius;
	}

	public void setMaincn(String maincn){
		this.maincn = maincn;
	}

	public String getMaincn(){
		return maincn;
	}

	public void setTs(String ts){
		this.ts = ts;
	}

	public String getTs(){
		return ts;
	}

	public void setMainus(String mainus){
		this.mainus = mainus;
	}

	public String getMainus(){
		return mainus;
	}

	public void setAqicn(int aqicn){
		this.aqicn = aqicn;
	}

	public int getAqicn(){
		return aqicn;
	}

	@Override
 	public String toString(){
		return 
			"Pollution{" + 
			"aqius = '" + aqius + '\'' + 
			",maincn = '" + maincn + '\'' + 
			",ts = '" + ts + '\'' + 
			",mainus = '" + mainus + '\'' + 
			",aqicn = '" + aqicn + '\'' + 
			"}";
		}
}