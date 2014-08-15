package com.yifa.health_manage.common;

import android.os.Environment;

  


/**
 * 
 * @author track
 * @version 1.0
 */
public interface Constant { 
		public static final String CODE_SUCCESS = "0";
		
		public static final String ORDER_NEW = "timestamp";
		public static final String ORDER_RECOUNT = "recount";
		
		public static final String ORDER_PIC = "pic";
		
		public static final String FILE_TYPE_PIC = "1";
		
		public static final String WEIBO_TYPE_2 ="2";
		public static final int VALID_FAILED =408;
		public static final int ID_NO_EXIST =0;
		public static final int SUCCESS_FULL = 1;
		public static final int STATE_RECEIVED = 3;
		public static final int STATE_ON_PASSAGE = 0;
		public static final String FORMAT_TYPE = "UTF-8";
		public static final  String APP_NAME="Health";
		public static final  StringBuffer draft= new StringBuffer();
		//public static final String SERVER_URL ="http://mimiapi.sinaapp.com";
		//public static final String SERVER_URL ="http://192.168.1.11:8080/MIMI-webapp";
		public static final String SERVER_URL ="http://112.124.126.43";
		public static final  String IMAGE_DIR=Environment.getExternalStorageDirectory().getPath()+"/healthFielCache";
		
		public static class Config {
			public static final boolean DEVELOPER_MODE = false;
		}
		
}
