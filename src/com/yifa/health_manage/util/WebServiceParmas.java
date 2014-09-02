package com.yifa.health_manage.util;

import java.io.UnsupportedEncodingException;
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

	public static final int TEST = 1;

	public static Object uploadImage(int httpType, String[] parmas) {
		String url = "http://112.124.126.43/health/json.php";
		Object obj = null;
		obj = Connection.getConnection(url, httpType);
		return obj;
	}


	/**
	 * type:login username: xxxx, pwd: xxxx
	 * */
	public static Object login(int httpType, String[] parmas) {
		String url = "http://112.124.126.43/health/json.php";
		Object obj = null;
		JSONObject entity = new JSONObject();
		try {
			entity.put("type", "login");
			entity.put("username", "user");
			entity.put("pwd", "12345");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", "login"));
		params.add(new BasicNameValuePair("username", "user"));
		params.add(new BasicNameValuePair("pwd", "12345"));
		try {
			HttpEntity httpEntity = new UrlEncodedFormEntity(params,"utf-8");
			obj = Connection.getConnection(url, httpType, entity);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return obj;
	}

	public static Object getPicureInfo(int httpType, String[] parmas) {
		String url = "http://182.92.69.232/shengjing_app/picture.do";
		Object obj = null;
		JSONObject entity = new JSONObject();
		try {
			entity.put("id", parmas[0]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj = Connection.getConnection(url, httpType, entity);

		return obj;
	}

}
