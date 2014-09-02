package com.yifa.health_manage.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class WebServiceUtils extends AsyncTask<String, Integer, Object> {

	private Handler mHandler;
	private Context mContext;
	private int faceNo;
	private int httpType;
	private String[] params;
	private String titleString="";
	private boolean isDialog = false;
	
	private ProgressDialog progressDialog=null;

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
	 * parmas 请求参数 faceNo 请求方法的标记 httpType 请求的方式
	 * 有progressDiolg  传递title参数
	 * */
	public void sendExecute(String[] params, int faceNo, int httpType,String title) {
		this.faceNo = faceNo;
		this.httpType = httpType;
		this.params = params;
		this.titleString = title;
		isDialog = true;
		this.execute(params);
	}
	/**
	 * parmas 请求参数 faceNo 请求方法的标记 httpType 请求的方式
	 * 无progressDiolg 
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
		if(isDialog){
			progressDialog = ProgressDialog.show(mContext, "提示", titleString); 
		}
	}

	@Override
	protected Object doInBackground(String... arg0) {
		Object obj =null;
		if (NetworkUtil.getNetworkType(mContext) == NetworkUtil.NO_NET_CONNECT) {
			obj = "无网络";
		}else{
			obj = doInBackGround(faceNo);
		}
		return obj;
	}

	@Override
	protected void onPostExecute(Object result) {
		if(progressDialog!=null&&isDialog){
			progressDialog.dismiss();
		}
		if (result == null) {
			mHandler.sendEmptyMessage(-1);// 请求错误
			return;
		}
		if ("超时".equalsIgnoreCase(result.toString())) {
			mHandler.sendEmptyMessage(-2);// 请求超时
			return;
		}
		if ("无网络".equalsIgnoreCase(result.toString())) {
			mHandler.sendEmptyMessage(-3);// 无网络
			return;
		}
		Log.d("----------", result.toString());
		Message message = new Message();
		message.what = this.faceNo;
		message.obj = result;
		mHandler.sendMessage(message);
	}

	private Object doInBackGround(int faceNo) {
		Object obj = null;
		switch (faceNo) {
		case WebServiceParmas.TEST:
			obj = WebServiceParmas.login(httpType, params);
			break;
		

		default:
			break;
		}

		return obj;
	}
}
