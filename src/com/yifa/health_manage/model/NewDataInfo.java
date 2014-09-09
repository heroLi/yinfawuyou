package com.yifa.health_manage.model;

public class NewDataInfo {

	// high_value:123,low_value:76
		
		private String datetime;
		private String high_value;
		private String low_value;
		private String value;
		private String data_type;
		private String device_sn;
		public String getDatetime() {
			return datetime;
		}
		public void setDatetime(String datetime) {
			this.datetime = datetime;
		}
		public String getData_type() {
			return data_type;
		}
		public void setData_type(String data_type) {
			this.data_type = data_type;
		}
		public String getDevice_sn() {
			return device_sn;
		}
		public void setDevice_sn(String device_sn) {
			this.device_sn = device_sn;
		}
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
