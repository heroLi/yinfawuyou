package com.yifa.health_manage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yifa.health_manage.adapter.DevicesAdapter;
import com.yifa.health_manage.db.DBManager;
import com.yifa.health_manage.model.DeviceFriendName;
import com.yifa.health_manage.model.DeviceInfo;
import com.yifa.health_manage.model.DevicesListInfo;
import com.yifa.health_manage.model.ResultResponse;
import com.yifa.health_manage.model.UserInfo;
import com.yifa.health_manage.util.AndroidUtils;
import com.yifa.health_manage.util.MyLoger;
import com.yifa.health_manage.util.SharePrefenceUtils;
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;

/**
 * 设备列表
 * */
public class DeviceListActivity extends Activity implements OnClickListener {

	private String deviceType = "blood_presure";

	private ListView myList;

	private static int CHOICE_PHOTO = 0;
	private static int CHOICE_CAMERA = 1;

	private DevicesAdapter adapter;

	private LinearLayout layout1, layout2;

	private TextView deviceId1, deviceId2, title;

	private EditText friend1, friend2, friend11, friend12;

	private CheckBox deviceCheck1, deviceCheck2;

	private int friendType = 0;

	private String friendName1, friendName2, friendName3, friendName4;

	private Button delete;

	private int isSum = 0;

	private UserInfo userInfo;

	private Bitmap bit;

	private String picturePath;

	private ImageButton addDevice;

	private String path;

	private String device_id;

	private DBManager dbManager = null;

	private DevicesListInfo listnew;

	private ImageView friendImage1, friendImage2, friendImage3, friendImage4,
			friendClick1, friendClick2, friendClick3, friendClick4;

	private MyLoger loger = MyLoger.getInstence("DeviceListActivity");

	private List<DeviceInfo> list = new ArrayList<DeviceInfo>();

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.obj.toString().equalsIgnoreCase("")) {
				return;
			}
			loger.d(msg.obj.toString());
			switch (msg.what) {
			case WebServiceParmas.GET_DEVICE_FRIEND:
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(msg.obj.toString());
					Gson gson = new Gson();
					Type type = new TypeToken<DevicesListInfo>() {
					}.getType();
					listnew = gson.fromJson(jsonObject.toString(),
							DevicesListInfo.class);

					// Map<String, UserInfo> maps = SharePrefenceUtils
					// .getUsetInfoList(DeviceListActivity.this);
					if (listnew == null || listnew.getData().size() <= 0) {
						AndroidUtils.showToast(DeviceListActivity.this, "请添加用户设备");
						addDevice.setVisibility(View.VISIBLE);
						layout1.setVisibility(View.GONE);
						layout2.setVisibility(View.GONE);
						isSum = 0;
					} else {
						if (listnew.getData().get(0).getRelative() == null
								|| listnew.getData().get(0).getRelative()
										.size() <= 0) {
							addDevice.setVisibility(View.VISIBLE);
							return;
						}
						if (deviceType.equalsIgnoreCase("blood_glucose")) {
							if (SharePrefenceUtils
									.getSugarFriendId(DeviceListActivity.this)
									.getId().equalsIgnoreCase("")) {
								listnew.getData()
										.get(0)
										.getRelative()
										.get(0)
										.setDevice_sn(
												listnew.getData().get(0)
														.getDevice_sn());
								SharePrefenceUtils.saveSugarFriendId(
										DeviceListActivity.this, listnew
												.getData().get(0).getRelative()
												.get(0));
							}
						} else {
							if (SharePrefenceUtils
									.getPressureFriendId(
											DeviceListActivity.this).getId()
									.equalsIgnoreCase("")) {
								listnew.getData()
										.get(0)
										.getRelative()
										.get(0)
										.setDevice_sn(
												listnew.getData().get(0)
														.getDevice_sn());
								SharePrefenceUtils.savePressureFriendId(
										DeviceListActivity.this, listnew
												.getData().get(0).getRelative()
												.get(0));
							}
						}

						List<UserInfo> mList = new ArrayList<UserInfo>();
						int i = 0;
						for (DeviceInfo info : listnew.getData()) {
							if (i != 0)
								i = 2;
							for (DeviceFriendName name : info.getRelative()) {
								UserInfo userInfo = new UserInfo();
								userInfo.setDevice_sn(info.getDevice_sn());
								userInfo.setFriend_id(name.getId());
								userInfo.setName(name.getName());
								userInfo.setType(deviceType);
								userInfo.setLayoutId(i + "");
								i++;
								mList.add(userInfo);
							}
						}

						List<UserInfo> agoList = dbManager.quaryAll(deviceType);
						if (agoList.size() <= 0) {
							dbManager.insertAll(mList);
						} else {
							for (UserInfo userInfo : mList) {
								if (!userInfo.getDevice_sn().equalsIgnoreCase(
										"")
										&& !userInfo.getFriend_id()
												.equalsIgnoreCase("")) {
									if (dbManager.quaryId(
											userInfo.getDevice_sn(),
											userInfo.getFriend_id()) == null) {
										dbManager.insert(deviceType,
												userInfo.getDevice_sn(),
												userInfo.getFriend_id(),
												userInfo);
									} else {
										if (!dbManager
												.quaryId(
														userInfo.getDevice_sn(),
														userInfo.getFriend_id())
												.getLayoutId()
												.equalsIgnoreCase(
														userInfo.getLayoutId())) {
											dbManager.updateType(userInfo);
										}
									}
								}
							}

						}

						if (listnew.getData().size() == 2) {
							addDevice.setVisibility(View.VISIBLE);
							layout2.setVisibility(View.VISIBLE);
							layout1.setVisibility(View.VISIBLE);
							if (listnew.getData().get(0).getRelative().size() <= 0) {
								return;
							}
							deviceId1.setText(listnew.getData().get(0)
									.getDevice_sn());
							friend1.setText(listnew.getData().get(0)
									.getRelative().get(0).getName());
							if (listnew.getData().get(1).getRelative().size() > 0) {
								friend11.setText(listnew.getData().get(1)
										.getRelative().get(0).getName());
								deviceId2.setText(listnew.getData().get(1)
										.getDevice_sn());
							}
							if (listnew.getData().get(0).getRelative().size() == 2) {
								friend2.setText(listnew.getData().get(0)
										.getRelative().get(1).getName());
								if (listnew.getData().get(1).getRelative()
										.size() > 0) {
									friend12.setText(listnew.getData().get(1)
											.getRelative().get(1).getName());
									isSum = 2;
								}

							}
							// layout1.setOnLongClickListener(DeviceListActivity.this);
							// layout2.setOnLongClickListener(DeviceListActivity.this);

						} else if (listnew.getData().size() == 1) {
							addDevice.setVisibility(View.VISIBLE);
							if (listnew.getData().get(0).getRelative().size() <= 0) {
								return;
							}
							layout2.setVisibility(View.INVISIBLE);
							layout1.setVisibility(View.VISIBLE);
							isSum = 1;
							deviceId1.setText(listnew.getData().get(0)
									.getDevice_sn());
							friend1.setText(listnew.getData().get(0)
									.getRelative().get(0).getName());
							friend2.setText(listnew.getData().get(0)
									.getRelative().get(1).getName());
							// layout1.setOnLongClickListener(DeviceListActivity.this);

						} else {
							addDevice.setVisibility(View.VISIBLE);
							isSum = 1;
							layout1.setVisibility(View.GONE);
							layout2.setVisibility(View.GONE);
							return;
						}

						// --------
						if (listnew.getData().get(0).getRelative().size() == 1) {
							UserInfo user1 = dbManager.quaryId(listnew
									.getData().get(0).getDevice_sn(), listnew
									.getData().get(0).getRelative().get(0)
									.getId());
							if (user1 != null) {
								if (!user1.getImageUrl().equalsIgnoreCase("")) {
									byte[] b = Base64.decode(user1
											.getImageUrl().getBytes(),
											Base64.DEFAULT);
									Bitmap bitmap = BitmapFactory
											.decodeByteArray(b, 0, b.length);
									if (bitmap != null)
										friendImage1.setImageBitmap(bitmap);
								}
							}
							friend1.setText(user1.getName());

						} else {
							UserInfo user1 = dbManager.quaryId(listnew
									.getData().get(0).getDevice_sn(), listnew
									.getData().get(0).getRelative().get(0)
									.getId());
							if (user1 != null) {
								if (!user1.getImageUrl().equalsIgnoreCase("")) {
									byte[] b = Base64.decode(user1
											.getImageUrl().getBytes(),
											Base64.DEFAULT);
									Bitmap bitmap = BitmapFactory
											.decodeByteArray(b, 0, b.length);
									if (bitmap != null)
										friendImage1.setImageBitmap(bitmap);
								}
							}
							friend1.setText(user1.getName());

							UserInfo user2 = dbManager.quaryId(listnew
									.getData().get(0).getDevice_sn(), listnew
									.getData().get(0).getRelative().get(1)
									.getId());
							if (user2 != null) {
								if (!user2.getImageUrl().equalsIgnoreCase("")) {
									byte[] b = Base64.decode(user2
											.getImageUrl().getBytes(),
											Base64.DEFAULT);
									Bitmap bitmap = BitmapFactory
											.decodeByteArray(b, 0, b.length);
									if (bitmap != null)
										friendImage2.setImageBitmap(bitmap);
								}
							}
							friend2.setText(user2.getName());
						}
						if (listnew.getData().size() == 2) {
							if (listnew.getData().get(1).getRelative().size() == 1) {
								UserInfo user1 = dbManager.quaryId(listnew
										.getData().get(1).getDevice_sn(),
										listnew.getData().get(1).getRelative()
												.get(0).getId());
								if (user1 != null) {
									if (!user1.getImageUrl().equalsIgnoreCase(
											"")) {
										byte[] b = Base64.decode(user1
												.getImageUrl().getBytes(),
												Base64.DEFAULT);
										Bitmap bitmap = BitmapFactory
												.decodeByteArray(b, 0, b.length);
										if (bitmap != null)
											friendImage3.setImageBitmap(bitmap);
									}
								}
								friend11.setText(user1.getName());
							} else if (listnew.getData().get(1).getRelative()
									.size() == 2) {

								UserInfo user1 = dbManager.quaryId(listnew
										.getData().get(1).getDevice_sn(),
										listnew.getData().get(1).getRelative()
												.get(0).getId());
								if (user1 != null) {
									if (!user1.getImageUrl().equalsIgnoreCase(
											"")) {
										byte[] b = Base64.decode(user1
												.getImageUrl().getBytes(),
												Base64.DEFAULT);
										Bitmap bitmap = BitmapFactory
												.decodeByteArray(b, 0, b.length);
										if (bitmap != null)
											friendImage3.setImageBitmap(bitmap);
									}
								}
								friend11.setText(user1.getName());
								UserInfo user2 = dbManager.quaryId(listnew
										.getData().get(1).getDevice_sn(),
										listnew.getData().get(1).getRelative()
												.get(1).getId());
								if (user2 != null) {
									if (!user2.getImageUrl().equalsIgnoreCase(
											"")) {
										byte[] b = Base64.decode(user2
												.getImageUrl().getBytes(),
												Base64.DEFAULT);
										Bitmap bitmap = BitmapFactory
												.decodeByteArray(b, 0, b.length);
										if (bitmap != null)
											friendImage4.setImageBitmap(bitmap);
									}
								}
								friend12.setText(user2.getName());
							}
						}

						UserInfo current = null;
						if (deviceType.equalsIgnoreCase("blood_glucose")) {
							current = dbManager.quaryId(SharePrefenceUtils
									.getSugarFriendId(DeviceListActivity.this)
									.getDevice_sn(), SharePrefenceUtils
									.getSugarFriendId(DeviceListActivity.this)
									.getId());
						} else {
							current = dbManager.quaryId(
									SharePrefenceUtils.getPressureFriendId(
											DeviceListActivity.this)
											.getDevice_sn(),
									SharePrefenceUtils.getPressureFriendId(
											DeviceListActivity.this).getId());
						}
						if (current == null) {
							return;
						}
						int clickid = Integer.valueOf(current.getLayoutId());
						switch (clickid) {
						case 0:
							friendClick1
									.setBackgroundResource(R.drawable.click_ok);
							break;
						case 1:
							friendClick2
									.setBackgroundResource(R.drawable.click_ok);
							break;
						case 2:
							friendClick3
									.setBackgroundResource(R.drawable.click_ok);
							break;
						case 3:
							friendClick4
									.setBackgroundResource(R.drawable.click_ok);
							break;

						default:
							break;
						}

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				friendName1 = friend1.getText().toString().trim();
				friendName2 = friend2.getText().toString().trim();
				friendName3 = friend11.getText().toString().trim();
				friendName4 = friend12.getText().toString().trim();
				break;

			case WebServiceParmas.DELETE_DEVICE:
				try {
					JSONObject jsonObject2 = new JSONObject(msg.obj.toString());
					Gson gson = new Gson();
					ResultResponse response = gson.fromJson(
							jsonObject2.toString(), ResultResponse.class);
					if (response.isResult()) {
						UserInfo delete = new UserInfo();
						delete.setType(deviceType);
						delete.setDevice_sn(device_id);
						dbManager.deleteDevice(delete);
						if (deviceType.equalsIgnoreCase("blood_presure")) {
							SharePrefenceUtils
									.getPressureFriendId(DeviceListActivity.this);
							if (SharePrefenceUtils
									.getPressureFriendId(
											DeviceListActivity.this)
									.getDevice_sn().equalsIgnoreCase(device_id)) {
								SharePrefenceUtils.savePressureFriendId(
										DeviceListActivity.this,
										new DeviceFriendName());
							}
						} else {
							SharePrefenceUtils
									.getSugarFriendId(DeviceListActivity.this);
							if (SharePrefenceUtils
									.getSugarFriendId(DeviceListActivity.this)
									.getDevice_sn().equalsIgnoreCase(device_id)) {
								SharePrefenceUtils.saveSugarFriendId(
										DeviceListActivity.this,
										new DeviceFriendName());
							}
						}
						// finish();
						new WebServiceUtils(DeviceListActivity.this, mHandler)
								.sendExecuteNo(
										new String[] {
												SharePrefenceUtils
														.getAccount(DeviceListActivity.this),
												deviceType },
										WebServiceParmas.GET_DEVICE_FRIEND,
										WebServiceParmas.HTTP_POST);
					}
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
		setContentView(R.layout.activity_devices_layout_new);
		initView();
		userInfo = new UserInfo();
		dbManager = new DBManager(this);
		deviceType = getIntent().getStringExtra("type");
		if (deviceType.equalsIgnoreCase("blood_presure")) {
			title.setText("血压计");
		} else {
			title.setText("血糖仪");
		}
		path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
				+ "image.jpg";
		new WebServiceUtils(DeviceListActivity.this, mHandler).sendExecuteNo(
				new String[] {
						SharePrefenceUtils.getAccount(DeviceListActivity.this),
						deviceType }, WebServiceParmas.GET_DEVICE_FRIEND,
				WebServiceParmas.HTTP_POST);

		title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				List<UserInfo> info = dbManager.quaryAll(deviceType);
				for (int i = 0; i < info.size(); i++) {
					switch (i) {
					case 0:
						info.get(i)
								.setName(friend1.getText().toString().trim());
						break;
					case 1:
						info.get(i)
								.setName(friend2.getText().toString().trim());
						break;
					case 2:
						info.get(i).setName(
								friend11.getText().toString().trim());
						break;
					case 3:
						info.get(i).setName(
								friend12.getText().toString().trim());
						break;

					default:
						break;
					}

				}
				dbManager.updateName(info);
				finish();

			}
		});

	}

	private void initView() {
		// myList = (ListView) findViewById(R.id.myList);
		// adapter = new DevicesAdapter(this, list);
		// myList.setAdapter(adapter);
		// myList.setOnItemLongClickListener(this);

		layout1 = (LinearLayout) findViewById(R.id.layout1);
		layout2 = (LinearLayout) findViewById(R.id.layout2);
		title = (TextView) findViewById(R.id.activity_top_title);
		deviceId1 = (TextView) findViewById(R.id.device_id);
		deviceId2 = (TextView) findViewById(R.id.device_id2);
		friend1 = (EditText) findViewById(R.id.device_friend1_name);
		friend2 = (EditText) findViewById(R.id.device_friend2_name);
		friend11 = (EditText) findViewById(R.id.device1_friend1_name);
		friend12 = (EditText) findViewById(R.id.device2_friend2_name);
		delete = (Button) findViewById(R.id.commit);
		deviceCheck1 = (CheckBox) findViewById(R.id.device_check);
		deviceCheck2 = (CheckBox) findViewById(R.id.device2_check2);
		friendImage1 = (ImageView) findViewById(R.id.friend_image1);
		friendImage2 = (ImageView) findViewById(R.id.friend_image2);
		friendImage3 = (ImageView) findViewById(R.id.friend_image3);
		friendImage4 = (ImageView) findViewById(R.id.friend_image4);
		friendClick1 = (ImageView) findViewById(R.id.device_check1_im);
		friendClick2 = (ImageView) findViewById(R.id.device_check2_im);
		friendClick3 = (ImageView) findViewById(R.id.device2_check1_im);
		friendClick4 = (ImageView) findViewById(R.id.device2_check2_im);

		addDevice = (ImageButton) findViewById(R.id.activity_top_menu2);
		addDevice.setBackgroundResource(R.drawable.list_icon_no);

		friendImage1.setOnClickListener(this);
		friendImage2.setOnClickListener(this);
		friendImage3.setOnClickListener(this);
		friendImage4.setOnClickListener(this);
		addDevice.setOnClickListener(this);
		delete.setOnClickListener(this);
		friend1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				friend1.setFocusable(true);
				friend1.setFocusableInTouchMode(true);
				return false;
			}
		});
		friend2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				friend2.setFocusable(true);
				friend2.setFocusableInTouchMode(true);
				return false;
			}
		});
		friend11.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				friend11.setFocusable(true);
				friend11.setFocusableInTouchMode(true);
				return false;
			}
		});
		friend12.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				friend12.setFocusable(true);
				friend12.setFocusableInTouchMode(true);
				return false;
			}
		});
		friend1.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				loger.d("friend1------" + hasFocus);
				loger.d("friend1------" + hasFocus);
				if (!hasFocus) {
					if (friend1.getText().toString().trim()
							.equalsIgnoreCase("")) {
						friend1.setText(friendName1);
					}
				}
			}
		});
		friend2.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if (friend2.toString().trim().equalsIgnoreCase("")) {
						friend2.setText(friendName2);
					}
				}
			}
		});
		friend11.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if (friend11.toString().trim().equalsIgnoreCase("")) {
						friend11.setText(friendName3);
					}
				}
			}
		});
		friend12.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if (friend12.toString().trim().equalsIgnoreCase("")) {
						friend12.setText(friendName4);
					}
				}
			}
		});
	}

	// public boolean onLongClick(View v) {
	// if (isSum == 1) {
	// delete.setVisibility(View.VISIBLE);
	// deviceCheck1.setVisibility(View.VISIBLE);
	// } else {
	// delete.setVisibility(View.VISIBLE);
	// deviceCheck1.setVisibility(View.VISIBLE);
	// deviceCheck2.setVisibility(View.VISIBLE);
	//
	// }
	// deviceCheck1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	//
	// @Override
	// public void onCheckedChanged(CompoundButton buttonView,
	// boolean isChecked) {
	// if (isChecked) {
	// device_id = listnew.getData().get(0).getDevice_sn();
	// deviceCheck2.setChecked(false);
	// }
	//
	// }
	// });
	// deviceCheck2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	//
	// @Override
	// public void onCheckedChanged(CompoundButton buttonView,
	// boolean isChecked) {
	// if (isChecked) {
	// device_id = listnew.getData().get(1).getDevice_sn();
	// deviceCheck1.setChecked(false);
	// }
	// }
	// });
	// delete.setOnClickListener(this);
	// return false;
	// }

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 11 && requestCode == 1) {
			new WebServiceUtils(DeviceListActivity.this, mHandler).sendExecute(
					new String[] {
							SharePrefenceUtils
									.getAccount(DeviceListActivity.this),
							deviceType }, WebServiceParmas.GET_DEVICE_FRIEND,
					WebServiceParmas.HTTP_POST, "加载中...");
			return;
		}

		if (resultCode == RESULT_OK && requestCode == CHOICE_PHOTO) {
			Uri uri = data.getData();
			startPhotoZoom(uri);

		} else if (resultCode == RESULT_OK && requestCode == CHOICE_CAMERA) {
			if (bit != null) {
				bit.recycle();
				bit = null;
			}
			startPhotoZoom(Uri.fromFile(new File(path)));
		} else {
			if (data != null) {
				setPicToView(data);
			}
		}
	}

	@Override
	public void onBackPressed() {
		List<UserInfo> info = dbManager.quaryAll(deviceType);
		for (int i = 0; i < info.size(); i++) {
			switch (i) {
			case 0:
				info.get(i).setName(friend1.getText().toString().trim());
				break;
			case 1:
				info.get(i).setName(friend2.getText().toString().trim());
				break;
			case 2:
				info.get(i).setName(friend11.getText().toString().trim());
				break;
			case 3:
				info.get(i).setName(friend12.getText().toString().trim());
				break;

			default:
				break;
			}

		}
		dbManager.updateName(info);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commit:
			if (deviceCheck1.isChecked()) {
				new WebServiceUtils(DeviceListActivity.this, mHandler)
						.sendExecute(
								new String[] {
										SharePrefenceUtils
												.getAccount(DeviceListActivity.this),
										deviceType, device_id },
								WebServiceParmas.DELETE_DEVICE,
								WebServiceParmas.HTTP_POST, "加载中...");
				if (deviceCheck2.isChecked()) {
					new WebServiceUtils(DeviceListActivity.this, mHandler)
							.sendExecute(
									new String[] {
											SharePrefenceUtils
													.getAccount(DeviceListActivity.this),
											deviceType, device_id },
									WebServiceParmas.DELETE_DEVICE,
									WebServiceParmas.HTTP_POST, "加载中...");
				}
			} else if (deviceCheck2.isChecked()) {
				new WebServiceUtils(DeviceListActivity.this, mHandler)
						.sendExecute(
								new String[] {
										SharePrefenceUtils
												.getAccount(DeviceListActivity.this),
										deviceType, device_id },
								WebServiceParmas.DELETE_DEVICE,
								WebServiceParmas.HTTP_POST, "加载中...");
			}

			break;
		case R.id.friend_image1:
			friendType = 0;
			showDialog();
			break;
		case R.id.friend_image2:
			friendType = 1;
			showDialog();
			break;
		case R.id.friend_image3:
			friendType = 2;
			showDialog();
			break;
		case R.id.friend_image4:
			friendType = 3;
			showDialog();
			break;
		case R.id.activity_top_menu2:

			showMenuDialog(deviceType);
			break;

		default:
			break;
		}

	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
		 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);

			/**
			 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似
			 */
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			byte[] b = stream.toByteArray();

			String iamgeUr = new String(
					Base64.encodeToString(b, Base64.DEFAULT));
			userInfo.setImageUrl(iamgeUr);

			switch (friendType) {
			case 0:
				friendImage1.setImageBitmap(photo);
				userInfo.setName(friend1.getText().toString().trim()
						.equalsIgnoreCase("") ? "亲友一" : friend1.getText()
						.toString().trim());// 亲友名字
				userInfo.setDevice_sn(listnew.getData().get(0).getDevice_sn());
				userInfo.setFriend_id(listnew.getData().get(0).getRelative()
						.get(0).getId());
				break;
			case 1:
				friendImage2.setImageBitmap(photo);
				userInfo.setName(friend2.getText().toString().trim()
						.equalsIgnoreCase("") ? "亲友二" : friend1.getText()
						.toString().trim());// 亲友名字
				userInfo.setDevice_sn(listnew.getData().get(0).getDevice_sn());
				userInfo.setFriend_id(listnew.getData().get(0).getRelative()
						.get(1).getId());
				break;
			case 2:
				friendImage3.setImageBitmap(photo);
				userInfo.setName(friend11.getText().toString().trim()
						.equalsIgnoreCase("") ? "亲友一" : friend1.getText()
						.toString().trim());// 亲友名字
				userInfo.setDevice_sn(listnew.getData().get(1).getDevice_sn());
				userInfo.setFriend_id(listnew.getData().get(1).getRelative()
						.get(0).getId());
				break;
			case 3:
				friendImage4.setImageBitmap(photo);
				userInfo.setName(friend12.getText().toString().trim()
						.equalsIgnoreCase("") ? "亲友二" : friend1.getText()
						.toString().trim());// 亲友名字
				userInfo.setDevice_sn(listnew.getData().get(1).getDevice_sn());
				userInfo.setFriend_id(listnew.getData().get(1).getRelative()
						.get(1).getId());
				break;

			default:
				break;
			}
			dbManager.update(userInfo);
			// SharePrefenceUtils.saveUserInfoList(DeviceListActivity.this,
			// maps);

			/*
			 * ByteArrayOutputStream stream = new ByteArrayOutputStream();
			 * photo.compress(Bitmap.CompressFormat.JPEG, 60, stream); byte[] b
			 * = stream.toByteArray(); // 将图片流以字符串形式存储下来
			 * 
			 * tp = new String(Base64Coder.encodeLines(b));
			 * 这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了， 服务器处理的方法是服务器那边的事了，吼吼
			 * 
			 * 如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换 为我们可以用的图片类型就OK啦...吼吼
			 * Bitmap dBitmap = BitmapFactory.decodeFile(tp); Drawable drawable
			 * = new BitmapDrawable(dBitmap);
			 */
			// userPhoto.setImageBitmap(photo);
		}
	}

	private void showDialog() {
		final Dialog dialog = new Dialog(this, R.style.ThemeDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_image,
				null);
		dialog.setContentView(view);
		TextView photo = (TextView) view.findViewById(R.id.photo);
		TextView image = (TextView) view.findViewById(R.id.image);
		photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// path为保存图片的路径，执行完拍照以后能保存到指定的路径下
				File file = new File(path);
				Uri imageUri = Uri.fromFile(file);
				// Uri imageUri = Uri.fromFile(file);

				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CHOICE_CAMERA);
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		});
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
				intent2.addCategory(Intent.CATEGORY_OPENABLE);
				intent2.setType("image/*");
				startActivityForResult(intent2, CHOICE_PHOTO);
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		});
		dialog.show();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dbManager.close();
	}

	private Dialog dialog = null;

	@SuppressLint("InflateParams")
	private void showMenuDialog(String type) {
		if (type.equalsIgnoreCase("heart_rate")) {
			type = "blood_presure";
		}
		final List<UserInfo> agoList = dbManager.quaryAll(type);
		dialog = new Dialog(DeviceListActivity.this, R.style.ThemeDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_devices, null, false);
		dialog.setContentView(view);
		dialog.show();
		TextView autoAdd = (TextView) view.findViewById(R.id.auto);
		TextView handAdd = (TextView) view.findViewById(R.id.hand);
		TextView deletebutton = (TextView) view.findViewById(R.id.delete);
		autoAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (agoList.size() > 2) {
					AndroidUtils.showToast(DeviceListActivity.this, "最多绑定2个设备");
					return;
				}
				Intent intent = new Intent(DeviceListActivity.this,
						MipcaActivityCapture.class);
				// Intent intent = new Intent(this, CaptureActivity.class);
				intent.putExtra("deviceType", deviceType);
				startActivityForResult(intent, 1);
				dialog.dismiss();
			}
		});
		handAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (agoList.size() > 2) {
					AndroidUtils.showToast(DeviceListActivity.this, "最多绑定2个设备");
					return;
				}
				Intent resultIntent = new Intent(DeviceListActivity.this,
						AddDeviceActivity.class);
				resultIntent.putExtra("deviceType", deviceType);
				startActivityForResult(resultIntent, 1);
				dialog.dismiss();
			}
		});
		deletebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isSum == 1) {
					delete.setVisibility(View.VISIBLE);
					deviceCheck1.setVisibility(View.VISIBLE);
				} else {
					delete.setVisibility(View.VISIBLE);
					deviceCheck1.setVisibility(View.VISIBLE);
					deviceCheck2.setVisibility(View.VISIBLE);

				}
				deviceCheck1
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									device_id = listnew.getData().get(0)
											.getDevice_sn();
									deviceCheck2.setChecked(false);
								}

							}
						});
				deviceCheck2
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									device_id = listnew.getData().get(1)
											.getDevice_sn();
									deviceCheck1.setChecked(false);
								}
							}
						});
				dialog.dismiss();
			}
		});

	}
}
