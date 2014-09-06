package com.yifa.health_manage.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

public class WebServiceParmas {

	public static final int HTTP_POST = 0;
	public static final int HTTP_PUT = 1;
	public static final int HTTP_DELETE = 2;
	public static final int HTTP_GET = 3;

	public static final int TEST = 0x2362;
	public static final int LOGIN = TEST + 1;
	public static final int REGISTER = LOGIN + 1;
	public static final int FIND_PASS = REGISTER + 1;
	public static final int SET_NEW_PASS = FIND_PASS + 1;
	public static final int BIND_DEVICE = SET_NEW_PASS + 1;

	
	
	
	
	
	private static final String Url = "http://112.124.126.43/health/json.php";

	/**
	 * type:login username: xxxx, pwd: xxxx
	 * */
	public static Object login(int httpType, String[] parmas) {
		String url = "http://112.124.126.43/health/json.php";
		Object obj = null;
		JSONObject entity = new JSONObject();
		try {
			entity.put("type", "login");
			entity.put("username", parmas[0]);
			entity.put("pwd", parmas[1]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		obj = Connection.getConnection(Url, httpType, entity);
		return obj;
	}

	/**
	 * 找回密码 { type:resetpwd, username: xxxx, pwd: xxxx }
	 * */
	public static Object resetPassword(int httpType, String[] parmas) {
		Object obj = null;
		JSONObject entity = new JSONObject();
		try {
			entity.put("type", "resetpwd");
			entity.put("username", parmas[0]);
//			entity.put("pwd", parmas[1]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj = Connection.getConnection(Url, httpType, entity);

		return obj;
	}

	/**
	 * 设置新密码 { type:changepwd, username: xxxx, oldpwd: xxxx newpwd:xxxx }
	 * */
	public static Object changePassword(int httpType, String[] parmas) {
		Object obj = null;
		JSONObject entity = new JSONObject();
		try {
			entity.put("type", "changepwd");
			entity.put("username", parmas[0]);
			entity.put("oldpwd", parmas[1]);
			entity.put("newpwd", parmas[2]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj = Connection.getConnection(Url, httpType, entity);

		return obj;
	}

	/**
	 * 注册 （type:signup, username: xxxx, newpwd:xxxx }
	 * */
	public static Object register(int httpType, String[] parmas) {
		Object obj = null;
		JSONObject entity = new JSONObject();
		try {
			entity.put("type", "signup");
			entity.put("username", parmas[0]);
			entity.put("pwd", parmas[1]);
			entity.put("email", parmas[2]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj = Connection.getConnection(Url, httpType, entity);

		return obj;
	}

	/**
	 * 绑定设备 { type:binddev, username:xxx, device_type:xxxxe, device_sn: xxxx }
	 * */
	public static Object bindDevices(int httpType, String[] parmas) {
		Object obj = null;
		JSONObject entity = new JSONObject();
		try {
			entity.put("type", "binddev");
			entity.put("username", parmas[0]);
			entity.put("device_type", parmas[0]);
			entity.put("device_sn", parmas[1]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj = Connection.getConnection(Url, httpType, entity);

		return obj;
	}

	/**
	 * 获取用户的设备 { type:getdev, username: device_type:xxxxe, device_sn: xxxx }
	 * 
	 * if($device_type === 'blood_presure') else if ($device_type ===
	 * 'blood_glucose')
	 * */
	public static Object getDevices(int httpType, String[] parmas) {
		Object obj = null;
		JSONObject entity = new JSONObject();
		try {
			entity.put("type", "getdev");
			entity.put("username", parmas[0]);
			entity.put("device_type", parmas[1]);
			entity.put("device_sn", parmas[2]);
			entity.put("device_type", parmas[3]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj = Connection.getConnection(Url, httpType, entity);

		return obj;
	}

	/**
	 * 设备管理中获得设备及亲友 type:getrelative, username: xxx device_type:xxxx,
	 **/
	public static Object getDevicesFriend(int httpType, String[] parmas) {
		Object obj = null;
		JSONObject entity = new JSONObject();
		try {
			entity.put("type", "getrelative");
			entity.put("username", parmas[0]);
			entity.put("device_type", parmas[1]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj = Connection.getConnection(Url, httpType, entity);

		return obj;
	}

	/**
	 * 删除设备或亲友 { type:deletedevice, username: xxx device_type:xxxx, relative: id
	 * }
	 **/
	public static Object deleteDevicesFriend(int httpType, String[] parmas) {
		Object obj = null;
		JSONObject entity = new JSONObject();
		try {
			entity.put("type", "deletedevice");
			entity.put("username", parmas[0]);
			entity.put("device_type", parmas[1]);
			entity.put("relative", parmas[2]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj = Connection.getConnection(Url, httpType, entity);

		return obj;
	}

	/**
	 * 根据设备型号得到用户数据 { type:getdata, username: xxx device_type:xxxx, relative:
	 * id, datatype: xxx, }
	 **/
	public static Object deleteDevicesData(int httpType, String[] parmas) {
		Object obj = null;
		JSONObject entity = new JSONObject();
		try {
			entity.put("type", "getdata");
			entity.put("username", parmas[0]);
			entity.put("device_type", parmas[1]);
			entity.put("relative", parmas[2]);
			entity.put("datatype", parmas[3]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj = Connection.getConnection(Url, httpType, entity);

		return obj;
	}

	/**
	 * 当日数据 { type:latestdata, username: xxx device_type:xxxx, relative: id,
	 * datatype: latest, }
	 **/
	public static Object deleteDevicesDayData(int httpType, String[] parmas) {
		Object obj = null;
		JSONObject entity = new JSONObject();
		try {
			entity.put("type", "latestdata");
			entity.put("username", parmas[0]);
			entity.put("device_type", parmas[1]);
			entity.put("relative", parmas[2]);
			entity.put("datatype", parmas[3]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj = Connection.getConnection(Url, httpType, entity);

		return obj;
	}

	/**
	 * 得到统一设备下的所有亲友 { type:getrelative, username: xxx, device_type:xxxx, }
	 **/
	public static Object deleteDevicesAllfriend(int httpType, String[] parmas) {
		Object obj = null;
		JSONObject entity = new JSONObject();
		try {
			entity.put("type", "getrelative");
			entity.put("username", parmas[0]);
			entity.put("device_type", parmas[1]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj = Connection.getConnection(Url, httpType, entity);

		return obj;
	}

	/**
	 * 修改亲友 { type:modify_rative, device_type:xxx, relative_id,xxxx,(search by
	 * id) relative_name,xxx(for change) }
	 **/
	public static Object changeDeviceFriend(int httpType, String[] parmas) {
		Object obj = null;
		JSONObject entity = new JSONObject();
		try {
			entity.put("type", "modify_rative");
			entity.put("device_type", parmas[0]);
			entity.put("relative_id", parmas[1]);
			entity.put("relative_name", parmas[2]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj = Connection.getConnection(Url, httpType, entity);

		return obj;
	}

}