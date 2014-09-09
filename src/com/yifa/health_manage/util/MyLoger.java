package com.yifa.health_manage.util;

import android.util.Log;

public class MyLoger {

	private static MyLoger loger = null;

	private static boolean isFlag = true;

	private static String stringF = "yifawuyou";

	private static String nameF = "";
	
	private MyLoger(String name ){
		nameF = name;
	}

	public static MyLoger getInstence(String name) {
		if (loger == null) {
			loger = new MyLoger(name);
		}
		
		return loger;
	}

	public void d(Object message) {
		if (!isFlag)
			return;
		Log.d(stringF, nameF + "----" + message.toString());
	}
}
