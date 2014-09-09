package com.yifa.health_manage.model;

import java.io.Serializable;
import java.util.List;

public class DeviceInfo implements Serializable{

	private String device_type;
	private String device_sn;
	private List<DeviceFriendName> relative;

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public String getDevice_sn() {
		return device_sn;
	}

	public void setDevice_sn(String device_sn) {
		this.device_sn = device_sn;
	}

	public List<DeviceFriendName> getRelative() {
		return relative;
	}

	public void setRelative(List<DeviceFriendName> relative) {
		this.relative = relative;
	}
}
