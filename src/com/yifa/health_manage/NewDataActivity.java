package com.yifa.health_manage;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yifa.health_manage.model.NewDataInfo;
import com.yifa.health_manage.util.SharePrefenceUtils;
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;

/**
 * 最新数据
 * */
public class NewDataActivity extends Activity {

	private String friendId = "12345";
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.obj.toString().equalsIgnoreCase("")) {
				return;
			}
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(msg.obj.toString());
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				if (jsonArray == null)
					return;
				Gson gson = new Gson();

				Type type = new TypeToken<List<NewDataInfo>>() {
				}.getType();
				List<NewDataInfo> mList = gson.fromJson(jsonArray.toString(),
						type);
				initData(mList);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
	};

	private TextView data1, blood_xh, blood_xl, blood_lv, blood_t, data2,
			data3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_data);
		initView();
		new WebServiceUtils(this, mHandler).sendExecuteNo(
				new String[] {
						SharePrefenceUtils.getAccount(NewDataActivity.this),
						friendId }, WebServiceParmas.NEW_DATA,
				WebServiceParmas.HTTP_POST);
	}

	private void initView() {
		data1 = (TextView) findViewById(R.id.data1);
		data2 = (TextView) findViewById(R.id.data2);
		data3 = (TextView) findViewById(R.id.data3);
		blood_xh = (TextView) findViewById(R.id.blood_xh);
		blood_xl = (TextView) findViewById(R.id.blood_xl);
		blood_lv = (TextView) findViewById(R.id.blood_lv);
		blood_t = (TextView) findViewById(R.id.blood_t);
	}

	private void initData(List<NewDataInfo> mList) {

		if (mList.get(0).getDatetime()!=null) {
			data1.setText(mList.get(0).getDatetime());
			blood_xh.setText(mList.get(0).getHigh_value());
			blood_xl.setText(mList.get(0).getLow_value());
		}
		if (mList.get(1).getDatetime()!=null) {
			data2.setText(mList.get(1).getDatetime());
			blood_t.setText(mList.get(1).getValue());
		}
		if (mList.get(2).getDatetime()!=null) {
			data3.setText(mList.get(2).getDatetime());
			blood_lv.setText(mList.get(2).getValue());
		}

	}
}
