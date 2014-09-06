package com.yifa.health_manage;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.yifa.health_manage.model.UserInfo;
import com.yifa.health_manage.util.SharePrefenceUtils;

/**
 * 个人信息
 * */
public class RegisterMessageActivity extends Activity implements
		OnClickListener, OnCheckedChangeListener {

	private static int CHOICE_PHOTO = 0;
	private static int CHOICE_CAMERA = 1;
	private Button nextButton, topButton;
	private ImageView userPhoto;
	private Bitmap bit;
	private TextView title;

	private UserInfo userInfo;

	private RadioGroup saxGroup;

	private EditText nameEdit, briEdit, heightEdit, weightEdit, yaoEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_message_layout);
		userInfo = new UserInfo();
		initView();
		initLisenter();
		path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
				+ "image.jpg";
	}

	private void initView() {

		nextButton = (Button) findViewById(R.id.register_btn_next);
		topButton = (Button) findViewById(R.id.register_btn_no);
		userPhoto = (ImageView) findViewById(R.id.user_photo);
		title = (TextView) findViewById(R.id.activity_top_title);
		title.setText("个人信息");
		saxGroup = (RadioGroup) findViewById(R.id.sax_radio);
		userInfo.setSax("男");
		nameEdit = (EditText) findViewById(R.id.registr_name);
		briEdit = (EditText) findViewById(R.id.register_bri);
		heightEdit = (EditText) findViewById(R.id.resteger_height);
		weightEdit = (EditText) findViewById(R.id.resteger_weight);
		yaoEdit = (EditText) findViewById(R.id.resteger_yao);

	}

	private void initLisenter() {
		nextButton.setOnClickListener(this);
		topButton.setOnClickListener(this);
		userPhoto.setOnClickListener(this);
		saxGroup.setOnCheckedChangeListener(this);

		if (SharePrefenceUtils.getUsetInfo(this) != null) {
			if (SharePrefenceUtils.getUsetInfo(this).getImageUrl()
					.equalsIgnoreCase(""))
				return;
			byte[] b = Base64.decode(SharePrefenceUtils.getUsetInfo(this)
					.getImageUrl().getBytes(), Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
			if(bitmap!=null)
				userPhoto.setImageBitmap(bitmap);
		}

	}

	private String picturePath;
	private String path;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == CHOICE_PHOTO) {
			Uri uri = data.getData();
			startPhotoZoom(uri);

			// String[] filePathColumn = { MediaStore.Images.Media.DATA };
			//
			// Cursor cursor = getContentResolver().query(uri, filePathColumn,
			// null, null, null);
			// cursor.moveToFirst();
			//
			// int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			// picturePath = cursor.getString(columnIndex);
			// cursor.close();
			// if (bit != null) {
			// bit.recycle();
			// bit = null;
			// }
			// bit = BitmapFactory.decodeFile(picturePath);
			// userPhoto.setImageBitmap(bit);
		} else if (resultCode == RESULT_OK && requestCode == CHOICE_CAMERA) {
			if (bit != null) {
				bit.recycle();
				bit = null;
			}
			startPhotoZoom(Uri.fromFile(new File(path)));
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inJustDecodeBounds = false;
			//
			// options.outHeight = 1136;
			// options.outWidth = 640;
			//
			// bit = BitmapFactory.decodeFile(path);
			//
			// // bit = BitmapFactory
			// // .decodeFile(Environment
			// // .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
			// // + "/image.jpg");
			// Log.d("------------", bit.getHeight() + "--------" +
			// bit.getWidth());
			// userPhoto.setImageBitmap(bit);
		} else {
			if (data != null) {
				setPicToView(data);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_btn_next:
			userInfo.setName(nameEdit.getText().toString());
			userInfo.setBirsday(briEdit.getText().toString());
			userInfo.setHight(heightEdit.getText().toString());
			userInfo.setYaowei(yaoEdit.getText().toString());
			userInfo.setWight(weightEdit.getText().toString());
			SharePrefenceUtils.saveUserInfo(this, userInfo);
			startActivity(new Intent(this, BindDeviceActivity.class));

			break;
		case R.id.register_btn_no:
			finish();
			break;
		case R.id.user_photo:

			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			View view = LayoutInflater.from(this).inflate(
					R.layout.dialog_image, null);
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
			userPhoto.setImageBitmap(photo);;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.sax_nan:
			userInfo.setSax("男");
			break;
		case R.id.sax_nv:
			userInfo.setSax("女");
			break;

		default:
			break;
		}
	}

}
