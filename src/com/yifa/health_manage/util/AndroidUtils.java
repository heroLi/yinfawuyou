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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		Date data = new Date(System.currentTimeMillis());
		String time = dateFormat.format(data);
		return time;
	}

	/**
	 * 得到几天前的时间
	 */
	public static String getDateBefore(int day) {
		Date data = new Date(System.currentTimeMillis());
		Calendar now = Calendar.getInstance();
		now.setTime(data);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		String time = dateFormat.format(now.getTime());
		return time;
	}

	/**
	 * 测试的返回值的等级
	 * 
	 * 血糖 0理想1可接受2不可接受
	 * 
	 * 血压L：0理想，1正常，2正常偏高，3轻度，4中度，5高度
	 * 
	 * 心率，0理想1偏高2偏低
	 * */
	public static int getBloodLevel(int type, int value) {

		switch (type) {
		case 0:// 血压
			if (value < 120) {
				return 0;
			} else if (120 <= value && value < 129) {
				return 1;
			} else if (130 <= value && value < 139) {
				return 2;
			} else if (140 <= value && value < 159) {
				return 3;
			} else if (160 <= value && value < 179) {
				return 4;
			} else if (180 <= value) {
				return 5;
			}
			break;
		case 1:// 血糖
			if (4 <= value && value < 8) {
				return 0;
			} else if (8 <= value && value < 11) {
				return 1;
			} else if (11 <= value) {
				return 2;
			}
			break;
		case 2:// 心率
			if (45 <= value && value < 100) {
				return 0;
			} else if (100 <= value) {
				return 2;
			} else if (value < 45) {
				return 1;
			}
			break;

		default:
			break;
		}

		return 0;
	}
}
