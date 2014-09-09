package com.yifa.health_manage.model;

import java.io.Serializable;

/**
 * 用户信息
 * */
public class UserInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String sax;
	private String birsday;
	private String hight;
	private String wight;
	private String yaowei;
	private String imageUrl="";
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSax() {
		return sax;
	}
	public void setSax(String sax) {
		this.sax = sax;
	}
	public String getBirsday() {
		return birsday;
	}
	public void setBirsday(String birsday) {
		this.birsday = birsday;
	}
	public String getHight() {
		return hight;
	}
	public void setHight(String hight) {
		this.hight = hight;
	}
	public String getWight() {
		return wight;
	}
	public void setWight(String wight) {
		this.wight = wight;
	}
	public String getYaowei() {
		return yaowei;
	}
	public void setYaowei(String yaowei) {
		this.yaowei = yaowei;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
