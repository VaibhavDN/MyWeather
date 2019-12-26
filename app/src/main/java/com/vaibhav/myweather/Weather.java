package com.vaibhav.myweather;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Weather{

	@SerializedName("pr")
	private int pr;

	@SerializedName("ic")
	private String ic;

	@SerializedName("tp")
	private int tp;

	@SerializedName("ws")
	private double ws;

	@SerializedName("hu")
	private int hu;

	@SerializedName("wd")
	private int wd;

	@SerializedName("ts")
	private String ts;

	public void setPr(int pr){
		this.pr = pr;
	}

	public int getPr(){
		return pr;
	}

	public void setIc(String ic){
		this.ic = ic;
	}

	public String getIc(){
		return ic;
	}

	public void setTp(int tp){
		this.tp = tp;
	}

	public int getTp(){
		return tp;
	}

	public void setWs(double ws){
		this.ws = ws;
	}

	public double getWs(){
		return ws;
	}

	public void setHu(int hu){
		this.hu = hu;
	}

	public int getHu(){
		return hu;
	}

	public void setWd(int wd){
		this.wd = wd;
	}

	public int getWd(){
		return wd;
	}

	public void setTs(String ts){
		this.ts = ts;
	}

	public String getTs(){
		return ts;
	}

	@Override
 	public String toString(){
		return 
			"Weather{" + 
			"pr = '" + pr + '\'' + 
			",ic = '" + ic + '\'' + 
			",tp = '" + tp + '\'' + 
			",ws = '" + ws + '\'' + 
			",hu = '" + hu + '\'' + 
			",wd = '" + wd + '\'' + 
			",ts = '" + ts + '\'' + 
			"}";
		}
}