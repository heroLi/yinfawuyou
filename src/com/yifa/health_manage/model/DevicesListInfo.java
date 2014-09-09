package com.yifa.health_manage.model;

import java.io.Serializable;
import java.util.List;

public class DevicesListInfo implements Serializable{
	private String type;
	private boolean result;
	private String info;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	private List<DeviceInfo> data;

	public List<DeviceInfo> getData() {
		return data;
	}

	public void setData(List<DeviceInfo> data) {
		this.data = data;
	}
}
