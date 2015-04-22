package com.yifa.health_manage;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
	private List<NewDataInfo> mData = new ArrayList<NewDataInfo>();
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
					if (mList.size() <= 0)
						return;
					if (isFrist) {
						mData.addAll(mList);
					} else {
						mData.set(1, mList.get(1));
					}
					// initData(mList);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				if (isFrist) {
					initData(mData);
				}
				if (!SharePrefenceUtils
						.getPressureFriendId(NewDataActivity.this).getId()
						.equalsIgnoreCase("")) {

					if (isFlag) {
						new WebServiceUtils(NewDataActivity.this, mHandler)
								.sendExecuteNo(
										new String[] {
												SharePrefenceUtils
														.getAccount(NewDataActivity.this),
												SharePrefenceUtils
														.getPressureFriendId(
																NewDataActivity.this)
														.getId() },
										WebServiceParmas.NEW_DATA_2,
										WebServiceParmas.HTTP_POST);
						isFlag = false;
					}
				}

				break;
			case WebServiceParmas.NEW_DATA_2:
				if (msg.obj.toString().equalsIgnoreCase("")) {
					return;
				}
				JSONObject jsonObject2;
				try {
					jsonObject2 = new JSONObject(msg.obj.toString());
					JSONArray jsonArray = jsonObject2.getJSONArray("data");
					if (jsonArray == null)
						return;
					Gson gson = new Gson();
					Type type = new TypeToken<List<NewDataInfo>>() {
					}.getType();
					List<NewDataInfo> mList = gson.fromJson(
							jsonArray.toString(), type);
					if (mList.size() <= 0)
						return;
					if (!isFrist) {
						mData.addAll(mList);
					} else {
						mData.set(0, mList.get(0));
						if (mList.size() <= 2) {
							// mData.set(2, "0");
						} else
							mData.set(2, mList.get(2));

					}
					// initData(mList);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (isFrist) {
					initData(mData);
				}
				if (!SharePrefenceUtils.getSugarFriendId(NewDataActivity.this)
						.getId().equalsIgnoreCase("")) {

					if (isFlag) {
						new WebServiceUtils(NewDataActivity.this, mHandler)
								.sendExecuteNo(
										new String[] {
												SharePrefenceUtils
														.getAccount(NewDataActivity.this),
												SharePrefenceUtils
														.getSugarFriendId(
																NewDataActivity.this)
														.getId() },
										WebServiceParmas.NEW_DATA,
										WebServiceParmas.HTTP_POST);
						isFlag = true;
					}
				}

				break;

			default:
				break;
			}

		};
	};

	private TextView data1, blood_xh, blood_xl, blood_lv, blood_t, data2,
			data3, rightText;

	private TextView xy1, xy2, xy3, xy4, xy5, xy6, xt1, xt2, xt3, xl1, xl2,
			xl3, title;

	private RelativeLayout layout;
	private boolean isFlag = true;

	private boolean isFrist = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_data);
		initView();
		if (!SharePrefenceUtils.getSugarFriendId(this).getId()
				.equalsIgnoreCase("")) {
			isFrist = true;
			new WebServiceUtils(this, mHandler).sendExecuteNo(new String[] {
					SharePrefenceUtils.getAccount(NewDataActivity.this),
					SharePrefenceUtils.getSugarFriendId(this).getId() },
					WebServiceParmas.NEW_DATA, WebServiceParmas.HTTP_POST);
		} else if (!SharePrefenceUtils.getPressureFriendId(this).getId()
				.equalsIgnoreCase("")) {
			isFrist = false;
			new WebServiceUtils(this, mHandler).sendExecuteNo(new String[] {
					SharePrefenceUtils.getAccount(NewDataActivity.this),
					SharePrefenceUtils.getPressureFriendId(this).getId() },
					WebServiceParmas.NEW_DATA_2, WebServiceParmas.HTTP_POST);
		}

	}

	private void initView() {
		rightText = (TextView) findViewById(R.id.add_devices);
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
		rightText.setText("刷新");
		rightText.setVisibility(View.VISIBLE);
		rightText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mData.clear();
				if (!SharePrefenceUtils.getSugarFriendId(NewDataActivity.this)
						.getId().equalsIgnoreCase("")) {
					isFrist = true;
					new WebServiceUtils(NewDataActivity.this, mHandler).sendExecuteNo(
							new String[] {
									SharePrefenceUtils
											.getAccount(NewDataActivity.this),
									SharePrefenceUtils.getSugarFriendId(
											NewDataActivity.this).getId() },
							WebServiceParmas.NEW_DATA,
							WebServiceParmas.HTTP_POST);
				} else if (!SharePrefenceUtils
						.getPressureFriendId(NewDataActivity.this).getId()
						.equalsIgnoreCase("")) {
					isFrist = false;
					new WebServiceUtils(NewDataActivity.this, mHandler).sendExecuteNo(
							new String[] {
									SharePrefenceUtils
											.getAccount(NewDataActivity.this),
									SharePrefenceUtils.getPressureFriendId(
											NewDataActivity.this).getId() },
							WebServiceParmas.NEW_DATA_2,
							WebServiceParmas.HTTP_POST);
				}
			}
		});
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
		DecimalFormat decimalFormat = new DecimalFormat("#.#");

		for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i).getData_type().equalsIgnoreCase("blood_presure")) {

				if (mList.get(i).getDatetime() != null) {
					data1.setText(mList.get(i).getDatetime());
					blood_xh.setText(mList.get(i).getHigh_value());
					blood_xl.setText(mList.get(i).getLow_value());
					level = AndroidUtils.getBloodLevel(i,
							Integer.valueOf(mList.get(i).getHigh_value()));
					switch (level) {
					case 0:
						xy1.setBackgroundResource(R.drawable.newdata_201);
						break;
					case 1:
						xy2.setBackgroundResource(R.drawable.newdata_202);
						break;
					case 2:
						xy3.setBackgroundResource(R.drawable.newdata_203);
						break;
					case 3:
						xy4.setBackgroundResource(R.drawable.newdata_204);
						break;
					case 4:
						xy5.setBackgroundResource(R.drawable.newdata_205);
						break;
					case 5:
						xy6.setBackgroundResource(R.drawable.newdata_206);
						break;
					default:
						break;
					}
				}
			} else if (mList.get(i).getData_type()
					.equalsIgnoreCase("blood_glucose")) {

				if (mList.get(i).getDatetime() != null) {
					data2.setText(mList.get(i).getDatetime());
					blood_t.setText(decimalFormat.format(Double.valueOf(mList
							.get(i).getValue()) / 18));
					level = AndroidUtils.getBloodLevel(i,
							Integer.valueOf(mList.get(i).getValue()));
					switch (level) {
					case 0:
						xt1.setBackgroundResource(R.drawable.newdata_301);
						break;
					case 1:
						xt2.setBackgroundResource(R.drawable.newdata_302);
						break;
					case 2:
						xt3.setBackgroundResource(R.drawable.newdata_303);
						break;
					default:
						break;
					}
				}

			} else if (mList.get(i).getData_type()
					.equalsIgnoreCase("heart_rate")) {

				if (mList.get(i).getDatetime() != null) {
					data3.setText(mList.get(i).getDatetime());
					blood_lv.setText(mList.get(i).getValue());
					level = AndroidUtils.getBloodLevel(i,
							Integer.valueOf(mList.get(i).getValue()));
					switch (level) {
					case 0:
						xl2.setBackgroundResource(R.drawable.newdata_101);
						break;
					case 1:
						xl1.setBackgroundResource(R.drawable.newdata_102);
						break;
					case 2:
						xl3.setBackgroundResource(R.drawable.newdata_103);
						break;
					default:
						break;
					}
				}

			}
		}

	}
}
