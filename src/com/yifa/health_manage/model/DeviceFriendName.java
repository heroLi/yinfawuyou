package com.yifa.health_manage.model;

import java.io.Serializable;

public class DeviceFriendName implements Serializable {
	private String name="";
	private String id="";
	
	private String device_sn="";

	public String getDevice_sn() {
		return device_sn;
	}

	public void setDevice_sn(String device_sn) {
		this.device_sn = device_sn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
