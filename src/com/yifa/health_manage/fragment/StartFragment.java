package com.yifa.health_manage.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yifa.health_manage.ChartActivity;
import com.yifa.health_manage.FrontPagerWebactivity;
import com.yifa.health_manage.Main_board_Activity;
import com.yifa.health_manage.NewDataActivity;
import com.yifa.health_manage.R;
import com.yifa.health_manage.model.ImageResponse;
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;

/**
 * 首页
 * */
public class StartFragment extends Fragment {

	public StartFragment(FinalBitmap Bitmap) {
		this.finalBitmap =Bitmap;
	}

	/**
	 * ViewPager
	 */
	private ViewPager viewPager;

	private List<ImageView> pagers = new ArrayList<ImageView>();// 圆点
	private List<ImageResponse> images = new ArrayList<ImageResponse>();

	/**
	 * 装点点的ImageView数组
	 */
	private ImageView[] tips;

	private FinalBitmap finalBitmap;

	/**
	 * 装ImageView数组
	 */
	private ImageView[] mImageViews;
	private MyViewAdapter adapter;

	private LinearLayout imagesLayout;// 圆点viewgroup

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.obj.toString().equalsIgnoreCase("")) {
				return;
			}
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(msg.obj.toString());

				Gson gson = new Gson();
				Type type = new TypeToken<List<ImageResponse>>() {
				}.getType();
				JSONArray array = jsonObject.getJSONArray("data");
				List<ImageResponse> response = gson.fromJson(
						array.toString(), type);
				adapter.setImages(response);
				adapter.notifyDataSetChanged();
				initPoint();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_start, container, false);
		initView(view);
		new WebServiceUtils(getActivity(), mHandler).sendExecuteNo(
				new String[] { "1" }, WebServiceParmas.GET_IMAGE_URL,
				WebServiceParmas.HTTP_POST);
		return view;
	}

	private void initView(View view) {
		imagesLayout = (LinearLayout) view.findViewById(R.id.viewGroup);
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);

		adapter = new MyViewAdapter(images);
		viewPager.setOnPageChangeListener(new ViewpagerOnChangeListener());
		viewPager.setAdapter(adapter);

		// 血压
		view.findViewById(R.id.blood_p).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								ChartActivity.class);
						intent.putExtra("device_type", "blood_presure");
						startActivity(intent);

					}
				});
		// 心率
		view.findViewById(R.id.blood_heart).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								ChartActivity.class);
						intent.putExtra("device_type", "heart_rate");
						startActivity(intent);

					}
				});
		// 血糖
		view.findViewById(R.id.blood_suger).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								ChartActivity.class);
						intent.putExtra("device_type", "blood_glucose");
						startActivity(intent);

					}
				});
		view.findViewById(R.id.blood_new).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(getActivity(),
								NewDataActivity.class));

					}
				});
	}

	class ViewpagerOnChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			changeImagePager(arg0);
		}
	}

	private void initPoint() {
		if (null == getActivity()) {
			return;
		}
		imagesLayout.removeAllViews();
		pagers.clear();
		if (adapter.getCount() <= 0) {
			return;
		}
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(10, 0, 0, 0);
		for (int i = 0; i < adapter.getCount(); i++) {
			if (getActivity() == null) {
				return;
			}
			ImageView pager = new ImageView(getActivity());
			if (i == 0) {
				pager.setBackgroundResource(R.drawable.page_ok);
			} else {
				pager.setBackgroundResource(R.drawable.page_no);
				pager.setLayoutParams(lp);
			}
			imagesLayout.addView(pager);
			pagers.add(pager);
		}
	}

	class MyViewAdapter extends PagerAdapter {
		private LayoutInflater inflater;
		private List<ImageResponse> imagelist;

		public MyViewAdapter(List<ImageResponse> image) {
			inflater = getActivity().getLayoutInflater();
			this.imagelist = image;
		}

		@Override
		public int getCount() {
			return imagelist.size();
		}

		public void setImages(List<ImageResponse> image) {
			if (image != null && image.size() > 0) {
				imagelist.clear();
				imagelist = image;
			}
			this.notifyDataSetChanged();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View View = inflater.inflate(R.layout.item_image, container, false);
			ImageView imageView = (ImageView) View.findViewById(R.id.image);
			final ImageResponse response = imagelist.get(position);
			finalBitmap.display(imageView, response.getPath());
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
							FrontPagerWebactivity.class);
					intent.putExtra("url", response.getUrl());
					startActivity(intent);
				}
			});
			// imageView.setBackgroundResource(R.drawable.focus);
			container.addView(View, 0);
			return View;
		}

		public CharSequence getPageTitle(int position) {
			return String.valueOf(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

	}

	class ViewHolder {
		public ImageView imageView;
	}

	private void changeImagePager(int arg) {
		for (int i = 0; i < pagers.size(); i++) {
			if (i == arg) {
				((ImageView) pagers.get(i))
						.setBackgroundResource(R.drawable.page_ok);
			} else {
				((ImageView) pagers.get(i))
						.setBackgroundResource(R.drawable.page_no);
			}
		}
	}
}
