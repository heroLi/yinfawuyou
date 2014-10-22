package com.yifa.health_manage;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yifa.health_manage.model.NewDataInfo;
import com.yifa.health_manage.util.AndroidUtils;
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
			switch (msg.what) {
			case WebServiceParmas.NEW_DATA:
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
					List<NewDataInfo> mList = gson.fromJson(
							jsonArray.toString(), type);
					initData(mList);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}

		};
	};

	private TextView data1, blood_xh, blood_xl, blood_lv, blood_t, data2,
			data3;

	private TextView xy1, xy2, xy3, xy4, xy5, xy6, xt1, xt2, xt3, xl1, xl2,
			xl3, title;

	private RelativeLayout layout;

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
		xy1 = (TextView) findViewById(R.id.xy_1);
		xy2 = (TextView) findViewById(R.id.xy_2);
		xy3 = (TextView) findViewById(R.id.xy_3);
		xy4 = (TextView) findViewById(R.id.xy_4);
		xy5 = (TextView) findViewById(R.id.xy_5);
		xy6 = (TextView) findViewById(R.id.xy_6);
		xt1 = (TextView) findViewById(R.id.xt_1);
		xt2 = (TextView) findViewById(R.id.xt_2);
		xt3 = (TextView) findViewById(R.id.xt_3);
		xl1 = (TextView) findViewById(R.id.xl_1);
		xl2 = (TextView) findViewById(R.id.xl_2);
		xl3 = (TextView) findViewById(R.id.xl_3);
		title = (TextView) findViewById(R.id.activity_top_title);
		layout = (RelativeLayout) findViewById(R.id.top);
		title.setText("最新数据");
		title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		layout.setBackgroundColor(Color.parseColor("#b240cb"));
	}

	@SuppressLint("ResourceAsColor")
	private void initData(List<NewDataInfo> mList) {
		int level = 0;

		if (mList.get(0).getDatetime() != null) {
			data1.setText(mList.get(0).getDatetime());
			blood_xh.setText(mList.get(0).getHigh_value());
			blood_xl.setText(mList.get(0).getLow_value());
			level = AndroidUtils.getBloodLevel(0,
					Integer.valueOf(mList.get(0).getHigh_value()));
			switch (level) {
			case 0:
				xy1.setBackgroundResource(R.drawable.newdata_text_bg_ok);
				break;
			case 1:
				xy2.setBackgroundResource(R.drawable.newdata_text_bg_ok);
				break;
			case 2:
				xy3.setBackgroundResource(R.drawable.newdata_text_bg_ok);
				break;
			case 3:
				xy4.setBackgroundResource(R.drawable.newdata_text_bg_ok);
				break;
			case 4:
				xy5.setBackgroundResource(R.drawable.newdata_text_bg_ok);
				break;
			case 5:
				xy6.setBackgroundResource(R.drawable.newdata_text_bg_ok);
				break;
			default:
				break;
			}
		}
		if (mList.get(1).getDatetime() != null) {
			data2.setText(mList.get(1).getDatetime());
			blood_t.setText(mList.get(1).getValue());
			level = AndroidUtils.getBloodLevel(1,
					Integer.valueOf(mList.get(1).getValue()));
			switch (level) {
			case 0:
				xt1.setBackgroundResource(R.drawable.newdata_text_bg_ok);
				break;
			case 1:
				xt2.setBackgroundResource(R.drawable.newdata_text_bg_ok);
				break;
			case 2:
				xt3.setBackgroundResource(R.drawable.newdata_text_bg_ok);
				break;
			default:
				break;
			}
		}
		if (mList.get(2).getDatetime() != null) {
			data3.setText(mList.get(2).getDatetime());
			blood_lv.setText(mList.get(2).getValue());
			level = AndroidUtils.getBloodLevel(2,
					Integer.valueOf(mList.get(2).getValue()));
			switch (level) {
			case 0:
				xl1.setBackgroundResource(R.drawable.newdata_text_bg_ok);
				break;
			case 1:
				xl2.setBackgroundResource(R.drawable.newdata_text_bg_ok);
				break;
			case 2:
				xl3.setBackgroundResource(R.drawable.newdata_text_bg_ok);
				break;
			default:
				break;
			}
		}

	}
}
