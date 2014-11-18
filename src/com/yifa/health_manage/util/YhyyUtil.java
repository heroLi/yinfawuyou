package com.yifa.health_manage.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.yifa.health_manage.R;

public class YhyyUtil {


	/** 随机获取6位数字验证码 */
	public static String randomkey() {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			sb.append(random.nextInt(9) + 1);
		}
		return sb.toString();
		// return "111111";
	}

	/** 随机获取6位字母验证码 */
	public String randomKey() {
		String result = "";
		for (int i = 0; i < 6; i++) {
			int intVal = (int) (Math.random() * 26 + 97);
			result = result + (char) intVal;
		}
		return result;
	}

	public static long getPeriodOfValidity() {
		// 有效期时间段
		long interval = 1800000;
		long nowTime = System.currentTimeMillis();
		long endTime = nowTime + interval;
		return endTime;
	}

	public static long getPeriodOfValidity(long interval) {
		// 有效期时间段
		long nowTime = System.currentTimeMillis();
		long endTime = nowTime + interval;
		return endTime;
	}

	/** InputStream转化为String */
	public static String InputStreamTOString(InputStream in) throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[4096];
		int count = -1;
		while ((count = in.read(data, 0, 4096)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return new String(outStream.toByteArray(), "utf-8");
	}

	public static boolean isMobileNo(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isName(String mobiles) {
		Pattern p = Pattern.compile("^[\u4e00-\u9fa5a-zA-Z0-9_]{1,20}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 显示价格
	 * 
	 * */
	public static String formatPrice(String price) {
		if (price == null) {
			price = "1";
		} else if (price.equalsIgnoreCase("null")) {
			price = "1";
		} else if (price.equalsIgnoreCase(""))
			price = "1";

		StringBuffer sb = new StringBuffer();
		if (price.contains(".")) {
			int wherePoint = price.indexOf(".");
			String intPrice = price.substring(0, wherePoint);
			sb.append(intPrice);
		} else {
			sb.append(price);
		}
		return sb.toString();
	}

	/** 显示价格 */
	public static String formatPriceFloat(String price) {
		StringBuffer sb = new StringBuffer();
		if (price.contains(".")) {
			int wherePoint = price.indexOf(".");

			String intPrice = price.substring(0, wherePoint);
			String floatPrice = price.substring(wherePoint + 1);
			if (floatPrice.length() > 2) {
				floatPrice = floatPrice.substring(0, 2);
			} else if (floatPrice.length() == 1) {
				floatPrice = floatPrice + "0";
			} else if (floatPrice.length() == 0) {
				floatPrice = "00";
			}
			sb.append(intPrice).append(".").append(floatPrice);
		} else {
			sb.append(price).append(".00");
		}
		return sb.toString();
	}

	public static String formatBalance(String balance) {
		float balanceF = 0f;
		try {
			balanceF = Float.parseFloat(balance);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return String.valueOf((int) balanceF);
	}

	/** 显示5位订单号 */
	public static String formatOrderId(String orderId) {
		return orderId.substring(5);
	}

	//
	// public static String[] initCitys(String xml) {
	// List<CityList> citys = XmlFunctionUtil
	// .XmlParseCity(WebServiceLoadDataActivity.cacheCitys);
	// List<String> citys2 = new ArrayList<String>();
	// for (CityList cl : citys) {
	// citys2.add(cl.name);
	// }
	// return citys2.toArray(new String[citys2.size()]);
	// }

	/** 清除EditText中内容 */
	public static void clearEditText(EditText et) {
		int length = et.getText().length();
		et.getText().delete(0, length);
		et.clearFocus();
	}


	private static List<Activity> activityList = new ArrayList<Activity>();

	public static void addActivity(Activity app) {
		activityList.add(app);
	}

	public static void deleteActivity(Activity app) {
		activityList.remove(app);
	}

	public static void clearActivitys() {
		activityList.clear();
	}

	public static List<Activity> getActivityList() {
		return activityList;
	}

	private static String[] defaultAccountIds = { "$100000", "00000000",
			"99999999", "", "NULL" };

	public static boolean isBindCompany(String accountId) {
		if (accountId == null) {
			return false;
		}
		for (String id : defaultAccountIds) {
			if (id.equalsIgnoreCase(accountId)) {
				return false;
			}
		}
		return true;
	}

	/** 调用系统分享功能 */
	public static void shareClient(Context context, String title, String message) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain"); // 分享发送的数据类型
		String msg = message;// 分享的内容
		shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
		context.startActivity(Intent.createChooser(shareIntent, title));// 目标应用选择对话框的标题
	}

	public static String initPass(String name, String phone) {
		StringBuffer response = new StringBuffer();
		response.append(name).append("    ").append(phone);
		return response.toString();
	}

	// public static String getPaymentTypeDes(int type) {
	// switch (type) {
	// case 1:
	// return PaymentEnum.CASH.getType();
	// case 2:
	// return PaymentEnum.BLUE_CARD.getType();
	// case 4:
	// return PaymentEnum.COMPANY.getType();
	// case 20:
	// return PaymentEnum.HAI_CARD.getType();
	// case 13:
	// return PaymentEnum.ONLINE.getType();
	// }
	// return null;
	// }

	/** 是否夜间行车 */

	public static boolean isNightService(String startTime, String endTime) {
		if (startTime == null)
			startTime = "";
		if (endTime == null)
			endTime = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if (startTime.equalsIgnoreCase("") && endTime.equalsIgnoreCase(""))
			return false;

		if (!startTime.equalsIgnoreCase("") && endTime.equalsIgnoreCase("")) {
			Date start = null;
			try {
				start = sdf.parse(startTime);
				int hours = start.getHours();
				if (hours >= 0 && hours < 7) {
					return true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return false;
			}

		}

		if (startTime.equalsIgnoreCase("") && !endTime.equalsIgnoreCase("")) {
			Date end = null;
			try {
				end = sdf.parse(endTime);
				int hours = end.getHours();
				if (hours >= 0 && hours < 7) {
					return true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return false;
			}
		}

		if (!startTime.equalsIgnoreCase("") && !endTime.equalsIgnoreCase("")) {
			Date start1 = null;
			Date end1 = null;
			try {
				start1 = sdf.parse(startTime);
				end1 = sdf.parse(endTime);
				int hoursStart = start1.getHours();
				int hoursEnd = end1.getHours();
				if (hoursStart >= 0 && hoursStart < 7) {
					return true;
				}
				if (hoursEnd >= 0 && hoursEnd < 7) {
					return true;
				}

				if (hoursEnd >= hoursStart) {
					return false;
				} else {
					return true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return false;
			}

		}
		return false;
	}

	/** 验证是否为有效邮箱格式 */
	public static boolean isEmail(String email) {
		String filter = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(filter);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/** 验证是否为有效邮箱格式 */
	public static boolean isPassWord(String password) {
		String filter = "^[a-zA-Z]\\w{5,19}$";
		Pattern p = Pattern.compile(filter);
		Matcher m = p.matcher(password);
		return m.matches();
	}


	/**
	 * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDateLong(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

}
