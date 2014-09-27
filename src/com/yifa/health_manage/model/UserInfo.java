package com.yifa.health_manage.model;

import java.io.Serializable;

/**
 * 用户信息
 * */
public class UserInfo implements Serializable {
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
	private String imageUrl = "";
	private String device_sn = "";
	private String Friend_id = "";
	private String type = "";
	private String layoutId = "";

	public String getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDevice_sn() {
		return device_sn;
	}

	public void setDevice_sn(String device_sn) {
		this.device_sn = device_sn;
	}

	public String getFriend_id() {
		return Friend_id;
	}

	public void setFriend_id(String friend_id) {
		Friend_id = friend_id;
	}

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
