package com.yifa.health_manage.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

public class AndroidUtils {

	/**
	 * 提示
	 * */
	public static void showToast(Context c, String message) {
		Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 是否是邮箱
	 * */
	public static boolean isEmail(String message) {
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(message);
		return matcher.matches();
	}

	/**
	 * 4位验证码
	 * */
	public static String getVerification() {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			sb.append(random.nextInt(9));
		}

		return sb.toString();
	}

	/**
	 * 获取时间
	 * */
	@SuppressLint("SimpleDateFormat")
	public static String getTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date data = new Date(System.currentTimeMillis());
		String time = dateFormat.format(data);
		return time;
	}

	/**
	 * 得到几天前的时间
	 */
	public static String getDateBefore( int day) {
		Date data = new Date(System.currentTimeMillis());
		Calendar now = Calendar.getInstance();
		now.setTime(data);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String time = dateFormat.format(now.getTime());
		return time;
	}

	/**
	 * 得到几天后的时间
	 * 
	 */
	public static String getDateAfter(int day) {
		Date data = new Date(System.currentTimeMillis());
		Calendar now = Calendar.getInstance();
		now.setTime(data);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String time = dateFormat.format(now.getTime());
		return time;
	}
}
