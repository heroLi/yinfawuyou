package com.yifa.health_manage;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.yifa.health_manage.model.ResultResponse;
import com.yifa.health_manage.util.AndroidUtils;
import com.yifa.health_manage.util.SharePrefenceUtils;
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;
import com.yifa.health_manage.util.YhyyUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FeedActivity extends Activity {

	private TextView title;
	private EditText titleEdit, messEdit;
	private Button nextButton;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == WebServiceParmas.FEED_BACK) {
				if (msg.obj.toString().equalsIgnoreCase("")) {
					return;
				}
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(msg.obj.toString());

					Gson gson = new Gson();
					ResultResponse response = gson.fromJson(jsonObject.toString(),
							ResultResponse.class);
					if (response.isResult()) {
						AndroidUtils.showToast(FeedActivity.this,
								"提交成功");
						finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);
		initView();
		setListener();
	}

	private void initView() {
		titleEdit = (EditText) findViewById(R.id.feed_title);
		messEdit = (EditText) findViewById(R.id.feed_mes);
		nextButton = (Button) findViewById(R.id.login_btn_no);
		title = (TextView) findViewById(R.id.activity_top_title);
		title.setText("意见反馈");
		title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setListener() {
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (titleEdit.getText().toString().trim().equalsIgnoreCase("")) {
					AndroidUtils.showToast(FeedActivity.this, "请输入标题");
					return;
				} else if (messEdit.getText().toString().trim().length() < 5) {
					AndroidUtils.showToast(FeedActivity.this, "请输入至少5个字符");
					return;
				} else if (messEdit.getText().toString().trim()
						.equalsIgnoreCase("")) {
					AndroidUtils.showToast(FeedActivity.this, "请输入内容");
					return;
				} else if (messEdit.getText().toString().trim().length() < 5) {
					AndroidUtils.showToast(FeedActivity.this, "请输入至少5个字符");
					return;
				}
				new WebServiceUtils(FeedActivity.this, mHandler).sendExecute(
						new String[] {
								SharePrefenceUtils.getAccount(FeedActivity.this),
								titleEdit.getText().toString().trim(),
								messEdit.getText().toString().trim() },
						WebServiceParmas.FEED_BACK, WebServiceParmas.HTTP_POST,
						"加载中...");
			}
		});

	}
}
