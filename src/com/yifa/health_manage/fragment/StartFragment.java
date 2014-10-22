package com.yifa.health_manage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yifa.health_manage.Blood_pressure_Activity;
import com.yifa.health_manage.ChartActivity;
import com.yifa.health_manage.NewDataActivity;
import com.yifa.health_manage.R;

/**
 * 首页
 * */
public class StartFragment extends Fragment {

	/**
	 * ViewPager
	 */
	private ViewPager viewPager;

	/**
	 * 装点点的ImageView数组
	 */
	private ImageView[] tips;

	/**
	 * 装ImageView数组
	 */
	private ImageView[] mImageViews;

	/**
	 * 图片资源id
	 */
	private int[] imgIdArray;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_start, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		ViewGroup group = (ViewGroup) view.findViewById(R.id.viewGroup);
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		// 载入图片资源ID
		imgIdArray = new int[] { R.drawable.home_top_img3,
				R.drawable.home_top_img3,
				R.drawable.home_top_img3 };

		// 将点点加入到ViewGroup中
		tips = new ImageView[imgIdArray.length];
		for (int i = 0; i < tips.length; i++) {
			ImageView imageView = new ImageView(getActivity());
			imageView.setLayoutParams(new LayoutParams(10, 10));
			tips[i] = imageView;
			if (i == 0) {
				tips[i].setBackgroundResource(R.drawable.page_ok);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_no);
			}

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 5;
			layoutParams.rightMargin = 5;
			group.addView(imageView, layoutParams);
		}

		// 将图片装载到数组中
		mImageViews = new ImageView[imgIdArray.length];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(getActivity());
			mImageViews[i] = imageView;
			imageView.setBackgroundResource(imgIdArray[i]);
		}

		// 设置Adapter
		viewPager.setAdapter(new MyAdapter());
		// 设置监听，主要是设置点点的背景
		// viewPager.setOnPageChangeListener(this);

		// 设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
		viewPager.setCurrentItem((mImageViews.length) * 100);
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

	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			// ((ViewPager)container).removeView(mImageViews[position %
			// mImageViews.length]);

		}

		/**
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			try {
				((ViewPager) container).addView(mImageViews[position
						% mImageViews.length], 0);
			} catch (Exception e) {
				// handler something
			}
			return mImageViews[position % mImageViews.length];
		}

	}
}
