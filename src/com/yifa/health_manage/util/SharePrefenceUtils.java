package com.yifa.health_manage.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import com.yifa.health_manage.model.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

public class SharePrefenceUtils {

	/**
	 * 保存账号
	 * */
	public static void saveAccount(Context c, String name) {

		SharedPreferences preferences = c.getSharedPreferences("confirm_info",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("account", name);
		editor.commit();
	}

	public static String getAccount(Context c) {

		SharedPreferences preferences = c.getSharedPreferences("confirm_info",
				Context.MODE_PRIVATE);
		return preferences.getString("account", "");
	}

	/**
	 * 保存密码
	 * */
	public static void savePassword(Context c, String name) {

		SharedPreferences preferences = c.getSharedPreferences("confirm_info",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("password", name);
		editor.commit();
	}

	public static String getPassword(Context c) {

		SharedPreferences preferences = c.getSharedPreferences("confirm_info",
				Context.MODE_PRIVATE);
		return preferences.getString("password", "");
	}

	/**
	 * 是否自动登录
	 * */
	public static void saveAutoLogin(Context c, boolean name) {

		SharedPreferences preferences = c.getSharedPreferences("confirm_info",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("AutoLogin", name);
		editor.commit();
	}

	public static boolean getAutoLogin(Context c) {

		SharedPreferences preferences = c.getSharedPreferences("confirm_info",
				Context.MODE_PRIVATE);
		return preferences.getBoolean("AutoLogin", false);
	}

	/**
	 * 保存验证码
	 * */
	public static void saveVer(Context c, String name) {

		SharedPreferences preferences = c.getSharedPreferences("confirm_info",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("ver", name);
		editor.commit();
	}

	public static String getVer(Context c) {

		SharedPreferences preferences = c.getSharedPreferences("confirm_info",
				Context.MODE_PRIVATE);
		return preferences.getString("ver", "");
	}

	/**
	 * 保存用户信息
	 * */
	public static void saveUserInfo(Context c, UserInfo user) {

		SharedPreferences preferences = c.getSharedPreferences("confirm_info",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		String userS = null;
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(output);
			outputStream.writeObject(user);
			userS = Base64.encodeToString(output.toByteArray(), Base64.DEFAULT);
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editor.putString("UserInfo", userS);
		editor.commit();
	}

	public static UserInfo getUsetInfo(Context c) {

		SharedPreferences preferences = c.getSharedPreferences("confirm_info",
				Context.MODE_PRIVATE);

		String s = preferences.getString("UserInfo", "");
		if (s.equalsIgnoreCase("")) {
			return null;
		}
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				Base64.decode(s.getBytes(), Base64.DEFAULT));
		ObjectInputStream inputStream2;
		try {
			inputStream2 = new ObjectInputStream(inputStream);
			UserInfo user = (UserInfo) inputStream2.readObject();
			return user;
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
