package com.yifa.health_manage.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.yifa.health_manage.model.ResultResponse;

public class WebServiceUtils extends AsyncTask<String, Integer, Object> {

	private Handler mHandler;
	private Context mContext;
	private int faceNo;
	private int httpType;
	private String[] params;
	private String titleString = "";
	private boolean isDialog = false;
	
	private MyLoger loger = MyLoger.getInstence("WebServiceUtils");

	private ProgressDialog progressDialog = null;

	public WebServiceUtils() {
		// TODO Auto-generated constructor stub
	}

	public WebServiceUtils(Context context, Handler handler) {
		this.mContext = context;
		this.mHandler = handler;
	}

	public void sendExecute(int faceNo) {
		this.faceNo = faceNo;
		this.execute();
	}

	/**
	 * parmas 请求参数 faceNo 请求方法的标记 httpType 请求的方式 有progressDiolg 传递title参数
	 * */
	public void sendExecute(String[] params, int faceNo, int httpType,
			String title) {
		this.faceNo = faceNo;
		this.httpType = httpType;
		this.params = params;
		this.titleString = title;
		isDialog = true;
		this.execute(params);
	}

	/**
	 * parmas 请求参数 faceNo 请求方法的标记 httpType 请求的方式 无progressDiolg
	 * */
	public void sendExecuteNo(String[] params, int faceNo, int httpType) {
		this.faceNo = faceNo;
		this.httpType = httpType;
		this.params = params;
		isDialog = false;
		this.execute(params);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (isDialog) {
			progressDialog = ProgressDialog.show(mContext, "提示", titleString);
		}
	}

	@Override
	protected Object doInBackground(String... arg0) {
		Object obj = null;
		if (NetworkUtil.getNetworkType(mContext) == NetworkUtil.NO_NET_CONNECT) {
			obj = "无网络";
		} else {
			obj = doInBackGround(faceNo);
		}
		return obj;
	}

	@Override
	protected void onPostExecute(Object result) {
		if (progressDialog != null && isDialog) {
			progressDialog.dismiss();
		}
		if (result == null) {
			AndroidUtils.showToast(mContext, "请连接网络");
			mHandler.sendEmptyMessage(-1);// 请求错误
			return;
		}
		if ("超时".equalsIgnoreCase(result.toString())) {
			AndroidUtils.showToast(mContext, "请连接网络");
			mHandler.sendEmptyMessage(-1);// 请求超时
			return;
		}
		if ("无网络".equalsIgnoreCase(result.toString())) {
			AndroidUtils.showToast(mContext, "请连接网络");
			mHandler.sendEmptyMessage(-1);// 无网络
			return;
		}
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(result.toString());
			Gson gson = new Gson();
			ResultResponse response = gson.fromJson(jsonObject.toString(),
					ResultResponse.class);
			if (response.isResult()) {
			}
			loger.d(result.toString());
//			AndroidUtils.showToast(mContext, response.getInfo());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Message message = new Message();
		message.what = this.faceNo;
		message.obj = result;
		mHandler.sendMessage(message);
	}

	private Object doInBackGround(int faceNo) {
		Object obj = null;
		switch (faceNo) {
		case WebServiceParmas.LOGIN:
			obj = WebServiceParmas.login(httpType, params);
			break;
		case WebServiceParmas.REGISTER:
			obj = WebServiceParmas.register(httpType, params);
			break;
		case WebServiceParmas.FIND_PASS:
			obj = WebServiceParmas.resetPassword(httpType, params);
			break;
		case WebServiceParmas.SET_NEW_PASS:
			obj = WebServiceParmas.changePassword(httpType, params);
			break;
		case WebServiceParmas.GET_DEVICE_FRIEND:
			obj = WebServiceParmas.getDevicesFriend(httpType, params);
			break;
		case WebServiceParmas.BIND_DEVICE:
			obj = WebServiceParmas.bindDevices(httpType, params);
			break;
		case WebServiceParmas.DELETE_DEVICE:
			obj = WebServiceParmas.deleteDevicesFriend(httpType, params);
			break;
		case WebServiceParmas.NEW_DATA:
			obj = WebServiceParmas.getDayData(httpType, params);
			break;
		case WebServiceParmas.GET_BLOOD_DATA:
			obj = WebServiceParmas.getDevicesData(httpType, params);
			break;
		case WebServiceParmas.GET_IMAGE_URL:
			obj = WebServiceParmas.getImageURl(httpType, params);
			break;

		default:
			break;
		}

		return obj;
	}
}
