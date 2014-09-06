package com.yifa.health_manage.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.widget.Toast;

public class AndroidUtils {

	/**
	 * 提示
	 * */
	public static void showToast(Context c, String message) {
		Toast.makeText(c, message, Toast.LENGTH_LONG).show();
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

}
