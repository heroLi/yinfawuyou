package com.yifa.health_manage.model;

public class BloodValuesInfo {
// high_value:123,low_value:76
	
	private String datetime;
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	private String high_value;
	private String low_value;
	private String value;
	public String getHigh_value() {
		return high_value;
	}
	public void setHigh_value(String high_value) {
		this.high_value = high_value;
	}
	public String getLow_value() {
		return low_value;
	}
	public void setLow_value(String low_value) {
		this.low_value = low_value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
