package com.yifa.health_manage.fragment;

import java.lang.reflect.Type;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yifa.health_manage.ChartActivity;
import com.yifa.health_manage.R;
import com.yifa.health_manage.model.BloodValuesInfo;
import com.yifa.health_manage.util.AndroidUtils;
import com.yifa.health_manage.util.MyLoger;
import com.yifa.health_manage.util.SharePrefenceUtils;
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 血压
 * */
public class PressureFeagment extends Fragment {

	private XYSeries series_high;// XY数据点 用于提供绘制的点集合的数据
	private XYSeries series_line;// XY数据点 用于提供绘制的点集合的数据
	private XYSeries series_low;// XY数据点
	private XYMultipleSeriesDataset mDataset;// XY轴数据集存放XYSeries
	private XYMultipleSeriesRenderer renderer;// 线性统计图主描绘器
	private LinearLayout mLayout;
	private MyLoger loger = MyLoger.getInstence("PressureFeagment");

	private LinearLayout bloodLine;

	private String timeTitle = "2014";

	private TextView time_title;

	private int bgH, bgW, imageH, imageW;

	private float density;
	private LinearLayout linearLayout;
	private ImageView arrow;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			setImageShow(80);
		};
	};

	public PressureFeagment(String type) {
		this.deviceType = type;
	}

	// blood_presure 血压 blood_glucose 血糖
	private String timeType = "week", deviceType = "blood_glucose",
			device_sn = "123456", relative = "12345", startdate, enddate;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			loger.d(msg.obj.toString());
			switch (msg.what) {
			case WebServiceParmas.GET_BLOOD_DATA:
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(msg.obj.toString());
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					if (jsonArray == null)
						return;
					Gson gson = new Gson();

					Type type = new TypeToken<List<BloodValuesInfo>>() {
					}.getType();
					List<BloodValuesInfo> mList = gson.fromJson(
							jsonArray.toString(), type);

					ChartActivity cahrt = (ChartActivity) getActivity();
					cahrt.setListData(mList);
					initData(mList);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			default:
				break;
			}

		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chart, container, false);
		initView(view);

		return view;
	}

	private void initView(View view) {
		mLayout = (LinearLayout) view.findViewById(R.id.chart_layout);
		bloodLine = (LinearLayout) view.findViewById(R.id.blood_line);
		time_title = (TextView) view.findViewById(R.id.timeTitle);
		linearLayout = (LinearLayout) view.findViewById(R.id.layout_halfRound);
		arrow = (ImageView) view.findViewById(R.id.needle_iv);
		if (deviceType.equalsIgnoreCase("blood_glucose")) {
			relative = SharePrefenceUtils.getSugarFriendId(getActivity());
		} else if (deviceType.equalsIgnoreCase("blood_presure")) {
			relative = SharePrefenceUtils.getPressureFriendId(getActivity());
		} else if (deviceType.equalsIgnoreCase("heart_rate")) {
			relative = SharePrefenceUtils.getHeartFriendId(getActivity());
		}

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getActivity().getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		density = displayMetrics.density;

		new WebServiceUtils(getActivity(), mHandler).sendExecute(new String[] {
				SharePrefenceUtils.getAccount(getActivity()), deviceType,
				device_sn, relative, timeType, AndroidUtils.getDateBefore(7),
				AndroidUtils.getTime() }, WebServiceParmas.GET_BLOOD_DATA,
				WebServiceParmas.HTTP_POST, "加载中...");

		timeTitle = AndroidUtils.getDateBefore(7) + "-"
				+ AndroidUtils.getTime();
		time_title.setText(timeTitle);

		initLineSet();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	private void initData(List<BloodValuesInfo> mList) {
		series_line = new XYSeries("line");
		series_high = new XYSeries("高压");
		mDataset = new XYMultipleSeriesDataset();
		if (deviceType.equalsIgnoreCase("blood_glucose")) {// 血糖
			for (int i = 0; i < mList.size(); i++) {
				series_high.add(i + 1, Double.valueOf(mList.get(i).getValue()));
			}

		} else {
			for (int i = 0; i < mList.size(); i++) {
				series_high.add(i + 1,
						Double.valueOf(mList.get(i).getHigh_value()));
			}
			series_low = new XYSeries("低压");
			for (int i = 0; i < mList.size(); i++) {
				series_low.add(i + 1,
						Double.valueOf(mList.get(i).getLow_value()));
			}
			mDataset.addSeries(series_low);
		}
		mDataset.addSeries(series_high);

		// 竖线
		series_line.add(mList.size(), 40);
		series_line.add(mList.size(), 160);
		mDataset.addSeries(series_line);

		// 曲线图的格式，包括颜色，值的范围，点和线的形状等等 都封装在
		// XYSeriesRender对象中，再将XYSeriesRender对象封装在 XYMultipleSeriesRenderer 对象中
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.WHITE);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setPointStrokeWidth(10);
		r.setFillPoints(true);
		XYSeriesRenderer r2 = new XYSeriesRenderer();
		r2.setColor(Color.WHITE);
		r2.setPointStyle(PointStyle.TRIANGLE);
		r2.setFillPoints(true);
		renderer = getRenderer();
		renderer.addSeriesRenderer(r);
		if (deviceType.equalsIgnoreCase("blood_glucose")) {// 血糖
			renderer.addSeriesRenderer(r2);// line
		} else {
			renderer.addSeriesRenderer(r2);
		}
		// 点击
		renderer.setClickEnabled(true);
		renderer.setSelectableBuffer(50);

		final GraphicalView view = ChartFactory.getLineChartView(getActivity(),
				mDataset, renderer);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// handle the click event on the chart
				SeriesSelection seriesSelection = view
						.getCurrentSeriesAndPoint();

				if (seriesSelection == null) {
				} else {
					series_line.clear();

					series_line.add(seriesSelection.getXValue(), 40);
					series_line.add(seriesSelection.getXValue(), 160);
					view.repaint();
				}
			}
		});
		mLayout.addView(view);

	}

	// 得到主渲染器，并对其各项属性进行设置
	@SuppressLint("ResourceAsColor")
	private XYMultipleSeriesRenderer getRenderer() {
		renderer = new XYMultipleSeriesRenderer();
		// 设置背景色是否启用
		renderer.setApplyBackgroundColor(true);
		// 设置背景色
		renderer.setBackgroundColor(Color.parseColor("#00ffffff"));
		// 设置x y轴标题字体大小
		renderer.setAxisTitleTextSize(18f);

		// 设置表格标题字体大小
		renderer.setChartTitleTextSize(25f);
		// 设置表格标题
		// renderer.setChartTitle("血压");
		// 设置标签字体大小
		renderer.setLabelsTextSize(20f);
		// 设置图例字体大小
		renderer.setLegendTextSize(15f);

		// 设置是否显示放大缩小按钮
		renderer.setZoomButtonsVisible(false);
		// 设置图表上显示点的大小
		renderer.setPointSize(6f);
		// 设置边距
		renderer.setMargins(new int[] { 60, 40, 0, 0 });// 上，左，xia 右
		// 设置边距的颜色
		renderer.setMarginsColor(Color.parseColor("#00ffffff"));
		// 设置xy轴线的颜色
		// renderer.setAxesColor(Color.parseColor("#00ffffff"));

		// 设置xy轴字的颜色
		renderer.setXLabelsColor(Color.parseColor("#ffffff"));
		renderer.setYLabelsColor(0, Color.parseColor("#ffffff"));

		renderer.setLegendHeight(20);
		renderer.setYLabelsAlign(Paint.Align.RIGHT);
		renderer.setYAxisAlign(Paint.Align.LEFT, 0);

		renderer.setYTitle("mm\nhg");
		renderer.setXTitle("");

		renderer.setXAxisMax(8);
		renderer.setXAxisMin(0);
		renderer.setYAxisMax(160);
		renderer.setYAxisMin(40);
		// 轴上数字的数量
		renderer.setXLabels(0);
		renderer.setYLabels(0);

		renderer.addXTextLabel(1, "一");
		renderer.addXTextLabel(2, "二");
		renderer.addXTextLabel(3, "三");
		renderer.addXTextLabel(4, "四");
		renderer.addXTextLabel(5, "五");
		renderer.addXTextLabel(6, "六");
		renderer.addXTextLabel(7, "七");

		renderer.addYTextLabel(40, "40");
		renderer.addYTextLabel(80, "80");
		renderer.addYTextLabel(120, "120");
		renderer.addYTextLabel(160, "160");

		// renderer.setZoomRate(100.0F);
		renderer.setShowLegend(false);
		// renderer.setAntialiasing(true);
		renderer.setShowAxes(true);
		// renderer.setShowCustomTextGrid(true);
		// renderer.setExternalZoomEnabled(true);
		// renderer.setFitLegend(true);
		// renderer.setXRoundedLabels(true);

		renderer.setShowGridX(true);
		// renderer.setShowAxes(false);
		renderer.setPanEnabled(false); // 图表是否可以移动
		renderer.setZoomEnabled(false); // 图表是否可以缩放
		renderer.setPanEnabled(false);
		// renderer.setLegendHeight(100); // 图标文字距离底边的高度

		// renderer.setYLabelsAlign(Align.RIGHT);// 右对齐
		// renderer.setXLabelsAlign(Align.LEFT);

		// create a new renderer for the new series创建一个新的渲染器的新系列，每一条的对象
		// XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
		//
		// // 设置新系列的属性
		// seriesRenderer.setPointStyle(PointStyle.CIRCLE);
		// seriesRenderer.setFillPoints(true);
		// seriesRenderer.setDisplayChartValues(true);
		// seriesRenderer.setDisplayChartValuesDistance(100);
		// seriesRenderer.setColor(Color.RED);
		// renderer.addSeriesRenderer(seriesRenderer);

		return renderer;
	}

	/**
	 * 得到住渲染器，并对其各项属性进行设置
	 * 
	 * @return
	 */
	public XYMultipleSeriesDataset getDataset() {
		mDataset = new XYMultipleSeriesDataset();
		String seriesTitle = "健康数据";
		XYSeries mSeries = new XYSeries(seriesTitle);
		for (int i = 0; i < 10; i++) {
			// 把坐标添加到当前序列中去
		}
		mDataset.addSeries(mSeries);
		return mDataset;
	}

	private void initLineSet() {

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				bgH = linearLayout.getHeight();
				bgW = linearLayout.getWidth();

				imageH = arrow.getHeight();
				imageW = arrow.getWidth();

				Log.d("demo", bgH + "");
				Log.d("demo", bgW + "");
				Log.d("demo", imageH + "");
				Log.d("demo", imageW + "");
				handler.sendEmptyMessage(0);
			}
		}, 1000);
	}

	private int middle = 100;

	private void setImageShow(int values) {
		if (deviceType.equalsIgnoreCase("blood_glucose")) {// xuetang

		} else if (deviceType.equalsIgnoreCase("heart_rate")) {

		}

		double jiao = Math.asin((double) values / middle);
		jiao = Math.toDegrees(jiao);
		java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
		String s = df.format(jiao);

		float banjing = bgH - imageH - 30 * density;
		float hh = banjing * values / middle;
		double ww = Math.sqrt(banjing * banjing - (banjing * values / middle)
				* (banjing * values / middle));
		Log.d("demo", "hh" + hh);
		Log.d("demo", "ww" + ww);
		RotateAnimation animation = new RotateAnimation(0f, +80.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);// 按中心旋转
		animation.setDuration(2000);
		// (bgH- imageH-30*density)
		// TranslateAnimation translateAnimation = new TranslateAnimation(0f,
		// (bgW
		// / 2 - imageW / 2 - 30 * density), 0f,
		// -(bgH - imageH * 3 / 2 - 30 * density));
		TranslateAnimation translateAnimation = new TranslateAnimation(0f,
				(float) ww, 0f, -hh);
		translateAnimation.setDuration(1000);
		AnimationSet animationSet = new AnimationSet(false);
		animationSet.addAnimation(animation);
		animationSet.addAnimation(translateAnimation);
		animationSet.setFillAfter(true);

		arrow.startAnimation(animationSet);
	}

}
