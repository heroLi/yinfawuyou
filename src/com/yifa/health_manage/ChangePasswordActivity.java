package com.yifa.health_manage;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yifa.health_manage.model.ResultResponse;
import com.yifa.health_manage.util.AndroidUtils;
import com.yifa.health_manage.util.SharePrefenceUtils;
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;

/**
 * 修改密码
 * */
public class ChangePasswordActivity extends Activity implements OnClickListener {

	private EditText oldPass, newPass, newPassTwo;
	private Button commit;

	private TextView title;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

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
					AndroidUtils.showToast(ChangePasswordActivity.this,
							"密码修改成功");
					finish();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		initView();
	}

	private void initView() {
		oldPass = (EditText) findViewById(R.id.change_oldpass);
		newPass = (EditText) findViewById(R.id.change_newpass);
		newPassTwo = (EditText) findViewById(R.id.change_newpass_two);
		title = (TextView) findViewById(R.id.activity_top_title);
		title.setText("修改密码");
		commit = (Button) findViewById(R.id.commit);
		commit.setOnClickListener(this);
		title.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commit:
			if (isVerify()) {

				new WebServiceUtils(ChangePasswordActivity.this, mHandler)
						.sendExecuteNo(
								new String[] {
										SharePrefenceUtils
												.getAccount(ChangePasswordActivity.this),
										oldPass.getText().toString().trim(),
										newPass.getText().toString().trim() },
								WebServiceParmas.SET_NEW_PASS,
								WebServiceParmas.HTTP_POST);
			}
			break;
		case R.id.activity_top_title:
			finish();
			break;
		default:
			break;
		}

	}

	private boolean isVerify() {
		if (newPass.getText().toString().trim().equalsIgnoreCase("")) {
			AndroidUtils.showToast(this, "密码不能为空");
			return false;
		}
		if (oldPass.getText().toString().trim().equalsIgnoreCase("")) {
			AndroidUtils.showToast(this, "密码不能为空");
			return false;
		}
		if (!newPass.getText().toString().trim()
				.equalsIgnoreCase(newPassTwo.getText().toString().trim())) {
			AndroidUtils.showToast(this, "两次密码不一致");
			return false;
		}
		return true;
	}
}
