/**
 * 
 */
package com.yifa.health_manage.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * @author user
 * 
 */
public class Connection {

	private static String md5Key = "491C12AF7EE5C2E3652FC72328512663";

	private static String md5code = null;

	public static String ver = "";

	private static String getHttpContent(String url, int type, JSONObject entity) {
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 5000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpRequestBase http = null;
		try {
			http = getHttpType(type, url, entity);
		} catch (Exception e1) {
			e1.printStackTrace();
			return -1 + "";
		}
		http.setHeader("Content-Type", "application/json");
		http.setHeader("charset", "utf-8");
		String responseBody = "";
		HttpResponse response = null;
		try {
			response = httpclient.execute(http);
			if (response.getStatusLine().getStatusCode() == 200) {
				responseBody = EntityUtils.toString(response.getEntity(),
						"UTF-8");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			responseBody = "超时";
		} catch (IOException e) {// 超时
			e.printStackTrace();
			responseBody = "超时";
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		// StringBuilder result = new StringBuilder();
		// InputStream stream = null;
		// BufferedReader reader = null;
		// try {
		// response = httpclient.execute(http);
		// stream = response.getEntity().getContent();
		// reader = new BufferedReader(new InputStreamReader(stream,
		// HTTP.UTF_8));
		// String line = "";
		// while ((line = reader.readLine()) != null) {
		// result.append(line);
		// }
		// responseBody = result.toString();
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// if (reader != null)
		// reader.close();
		// if (stream != null)
		// stream.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// httpclient.getConnectionManager().shutdown();
		// }
		return responseBody;
	}

	private static String getHttpContent(String url, int type) {
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 5000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpRequestBase http = null;
		try {
			http = getHttpType(type, url);
		} catch (Exception e1) {
			e1.printStackTrace();
			return -1 + "";
		}
		http.setHeader("Content-Type", "application/json");
		http.setHeader("charset", "utf-8");
		String responseBody = "";
		HttpResponse response = null;
		try {
			response = httpclient.execute(http);
			responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			responseBody = "超时";
		} catch (IOException e) {// 超时
			e.printStackTrace();
			responseBody = "超时";
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return responseBody;
	}

	private static String getHttpContent(String url, int type, HttpEntity entity) {
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 5000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpRequestBase requestBasePost = null;
		try {
			requestBasePost = getHttpType(type, url, entity);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		requestBasePost.setHeader("Connection", "Keep-Alive");
		requestBasePost.setHeader("Content-Type", "application/json");
		requestBasePost.setHeader("charset", "utf-8");
		requestBasePost.setHeader("Content-Type",
				"multipart/form-data; boundary=" + "--%@");
		requestBasePost.setHeader("User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
		// requestBasePost.setRequestProperty("Content-Type",
		// "multipart/form-data; boundary=" + BOUNDARY);
		String responseBody = "";
		HttpResponse response = null;
		try {
			response = httpclient.execute(requestBasePost);
			responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			responseBody = "超时";
		} catch (IOException e) {// 超时
			e.printStackTrace();
			responseBody = "超时";
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return responseBody;
	}

	public static synchronized String getConnection(String url, int type,
			JSONObject entity) {
		Log.d("------", entity.toString());
		return getHttpContent(url, type, entity);
	}

	public static synchronized String getConnection(String url, int type) {
		return getHttpContent(url, type);
	}

	public static synchronized String getConnection(String url, int type,
			HttpEntity entity) {
		return getHttpContent(url, type, entity);
	}

	private static HttpRequestBase getHttpType(int type, String url,
			JSONObject entity) throws UnsupportedEncodingException,
			JSONException {
		switch (type) {
		case WebServiceParmas.HTTP_POST:// post
			HttpPost requestBasePost = new HttpPost(url);
			if (entity != null) {
				StringEntity reqEntity = new StringEntity(entity.toString(),
						HTTP.UTF_8);
				requestBasePost.setEntity(reqEntity);
			}
			return requestBasePost;
		case WebServiceParmas.HTTP_PUT:// put
			HttpPut requestBasePut = new HttpPut(url);
			if (entity != null) {
				StringEntity reqEntity = new StringEntity(entity.toString(),
						HTTP.UTF_8);
				// 设置类型
				requestBasePut.setEntity(reqEntity);
			}
			return requestBasePut;
		case WebServiceParmas.HTTP_DELETE:
			HttpDelete requestBaseDelete = new HttpDelete(url);
			return requestBaseDelete;
		default:
			HttpGet requestBaseGet = new HttpGet(url);
			return requestBaseGet;
		}
	}

	private static HttpRequestBase getHttpType(int type, String url)
			throws UnsupportedEncodingException, JSONException {
		switch (type) {
		case WebServiceParmas.HTTP_POST:// post
			HttpPost requestBasePost = new HttpPost(url);
			return requestBasePost;
		case WebServiceParmas.HTTP_PUT:// put
			HttpPut requestBasePut = new HttpPut(url);
			return requestBasePut;
		case WebServiceParmas.HTTP_DELETE:
			HttpDelete requestBaseDelete = new HttpDelete(url);
			return requestBaseDelete;
		default:
			HttpGet requestBaseGet = new HttpGet(url);
			return requestBaseGet;
		}
	}

	private static HttpRequestBase getHttpType(int type, String url,
			HttpEntity entity) throws UnsupportedEncodingException,
			JSONException {
		switch (type) {
		case WebServiceParmas.HTTP_POST:// post
			HttpPost requestBasePost = new HttpPost(url);
			if (entity != null) {
				StringEntity reqEntity = new StringEntity(entity.toString(),
						HTTP.UTF_8);
				requestBasePost.setEntity(reqEntity);
			}
			return requestBasePost;
		case WebServiceParmas.HTTP_PUT:// put
			HttpPut requestBasePut = new HttpPut(url);
			if (entity != null) {
				StringEntity reqEntity = new StringEntity(entity.toString(),
						HTTP.UTF_8);
				// 设置类型
				requestBasePut.setEntity(reqEntity);
			}
			return requestBasePut;
		case WebServiceParmas.HTTP_DELETE:
			HttpDelete requestBaseDelete = new HttpDelete(url);
			return requestBaseDelete;
		default:
			HttpGet requestBaseGet = new HttpGet(url);
			return requestBaseGet;
		}
	}

}