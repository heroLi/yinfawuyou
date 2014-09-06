package com.yifa.health_manage;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.yifa.health_manage.model.ResultResponse;
import com.yifa.health_manage.util.SharePrefenceUtils;
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 3000; //

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case WebServiceParmas.LOGIN:
				if (msg.obj.toString().equalsIgnoreCase("")) {
					Intent mainIntent = new Intent(SplashActivity.this,
							LoginActivity.class);
					SplashActivity.this.startActivity(mainIntent);
				}
				try {
					JSONObject jsonObject = new JSONObject(msg.obj.toString());
					Gson gson = new Gson();
					// Type type = new TypeToken<ResultResponse>() {
					// }.getType();
					// List<ResultResponse> response = gson.fromJson(
					// jsonObject.toString(), type);
					ResultResponse response = gson.fromJson(
							jsonObject.toString(), ResultResponse.class);
					if (response.isResult()) {
						startActivity(new Intent(SplashActivity.this,
								Main_board_Activity.class));
					} else {
						Intent mainIntent = new Intent(SplashActivity.this,
								LoginActivity.class);
						SplashActivity.this.startActivity(mainIntent);
					}
					SplashActivity.this.finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_layout);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (!SharePrefenceUtils.getAutoLogin(SplashActivity.this)) {
					Intent mainIntent = new Intent(SplashActivity.this,
							LoginActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					SplashActivity.this.finish();
				} else {
					new WebServiceUtils(SplashActivity.this, handler).sendExecuteNo(
							new String[] {
									SharePrefenceUtils
											.getAccount(SplashActivity.this),
									SharePrefenceUtils
											.getPassword(SplashActivity.this) },
							WebServiceParmas.LOGIN, WebServiceParmas.HTTP_POST);
				}
			}

		}, SPLASH_DISPLAY_LENGHT);

	}
}