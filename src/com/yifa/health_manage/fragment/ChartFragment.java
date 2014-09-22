package com.yifa.health_manage.fragment;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yifa.health_manage.ChartActivity;
import com.yifa.health_manage.R;
import com.yifa.health_manage.model.BloodValuesInfo;
import com.yifa.health_manage.model.DeviceFriendName;
import com.yifa.health_manage.util.AndroidUtils;
import com.yifa.health_manage.util.MyLoger;
import com.yifa.health_manage.util.SharePrefenceUtils;
import com.yifa.health_manage.util.WebServiceParmas;
import com.yifa.health_manage.util.WebServiceUtils;

/**
 * 图表界面
 * **/
public class ChartFragment extends Fragment implements OnClickListener {

	private XYSeries series_high;// XY数据点 用于提供绘制的点集合的数据
	private XYSeries series_line;// XY数据点 用于提供绘制的点集合的数据
	private XYSeries series_low;// XY数据点
	private XYMultipleSeriesDataset mDataset;// XY轴数据集存放XYSeries
	private XYMultipleSeriesRenderer renderer;// 线性统计图主描绘器
	private LinearLayout mLayout;
	private MyLoger loger = MyLoger.getInstence("ChartFragment");

	private LinearLayout bloodLine;

	private String timeTitle = "2014";

	private int type = 0;// 0周 1月2年

	private TextView time_title, blood_text, blood_Flag, blood_mes, blood_time;

	private int bgH, bgW, imageH, imageW;

	private float density;
	private LinearLayout linearLayout;
	private ImageView arrow;

	private RelativeLayout topLayout;

	private FrameLayout chartLayout;

	private String startTime, endTime;

	private ImageButton leftButton, rightButton;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			setImageShow(80);
		};
	};

	public ChartFragment(String type, int timeType) {
		this.deviceType = type;
		this.type = timeType;
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
					ChartActivity cahrt = (ChartActivity) getActivity();
					cahrt.setListData(new ArrayList<BloodValuesInfo>());
					initData(new ArrayList<BloodValuesInfo>());
				}
				time_title.setText(startTime + "-" + endTime);
				if (startTime.equalsIgnoreCase(endTime)) {
					time_title.setText(startTime);
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

	@SuppressLint("ResourceAsColor")
	private void initView(View view) {
		mLayout = (LinearLayout) view.findViewById(R.id.chart_layout);
		bloodLine = (LinearLayout) view.findViewById(R.id.blood_line);
		time_title = (TextView) view.findViewById(R.id.timeTitle);
		blood_text = (TextView) view.findViewById(R.id.blood_detail_number_tv);
		blood_Flag = (TextView) view.findViewById(R.id.blood_detail_unit_tv);
		blood_mes = (TextView) view.findViewById(R.id.text);
		blood_time = (TextView) view.findViewById(R.id.time);
		linearLayout = (LinearLayout) view.findViewById(R.id.layout_halfRound);
		topLayout = (RelativeLayout) view.findViewById(R.id.topLayout);
		chartLayout = (FrameLayout) view.findViewById(R.id.chartlayout);
		arrow = (ImageView) view.findViewById(R.id.needle_iv);
		leftButton = (ImageButton) view.findViewById(R.id.chart_left);
		rightButton = (ImageButton) view.findViewById(R.id.chart_right);

		// DeviceFriendName name = new DeviceFriendName();
		// name.setId("12345");
		// name.setDevice_sn("123456");
		//
		// SharePrefenceUtils.saveSugarFriendId(getActivity(), name);
		// SharePrefenceUtils.savePressureFriendId(getActivity(), name);
		if (deviceType.equalsIgnoreCase("blood_glucose")) {
			relative = SharePrefenceUtils.getSugarFriendId(getActivity())
					.getId();
			device_sn = SharePrefenceUtils.getSugarFriendId(getActivity())
					.getDevice_sn();
			topLayout.setBackgroundColor(Color.parseColor("#1f65c4"));
			chartLayout.setBackgroundColor(Color.parseColor("#1f65c4"));
			linearLayout.setBackgroundResource(R.drawable.xuetang_group);
			blood_Flag.setText("mmol/L");
			blood_mes.setText("血糖详情");

		} else if (deviceType.equalsIgnoreCase("blood_presure")) {
			relative = SharePrefenceUtils.getPressureFriendId(getActivity())
					.getId();
			device_sn = SharePrefenceUtils.getPressureFriendId(getActivity())
					.getDevice_sn();
			topLayout.setBackgroundColor(Color.parseColor("#ff5c3d"));
			chartLayout.setBackgroundColor(Color.parseColor("#ff5c3d"));
			blood_mes.setText("血压详情");
			blood_Flag.setText("mmHg");
		} else if (deviceType.equalsIgnoreCase("heart_rate")) {
			relative = SharePrefenceUtils.getPressureFriendId(getActivity())
					.getId();
			device_sn = SharePrefenceUtils.getPressureFriendId(getActivity())
					.getDevice_sn();
			linearLayout.setBackgroundResource(R.drawable.xinlv_group);
			topLayout.setBackgroundColor(Color.parseColor("#e63a6c"));
			chartLayout.setBackgroundColor(Color.parseColor("#e63a6c"));
			blood_mes.setText("心率详情");
			blood_Flag.setText("BPM");
		}
		// 初始化时间
		initRequstTime(type);

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getActivity().getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		density = displayMetrics.density;
		if (startTime.equalsIgnoreCase(endTime)) {
			time_title.setText(startTime);
		}
		time_title.setText(startTime + "-" + endTime);

		initLineSet();
		initListener();
	}

	private void initListener() {
		leftButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);

		new WebServiceUtils(getActivity(), mHandler).sendExecute(new String[] {
				SharePrefenceUtils.getAccount(getActivity()), deviceType,
				device_sn, relative, timeType, startTime, endTime },
				WebServiceParmas.GET_BLOOD_DATA, WebServiceParmas.HTTP_POST,
				"加载中...");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	private void initData(List<BloodValuesInfo> mList) {
		mLayout.removeAllViews();
		series_line = new XYSeries("line");
		series_high = new XYSeries("高压");
		series_low = new XYSeries("低压");
		series_high.clear();
		series_low.clear();
		series_line.clear();
		mDataset = new XYMultipleSeriesDataset();
		if (deviceType.equalsIgnoreCase("blood_glucose")) {// 血糖
			for (int i = 0; i < mList.size(); i++) {
				series_high.add(i + 1, Double.valueOf(mList.get(i).getValue()));
			}
			if (mList.size() > 0) {
				blood_text.setText(mList.get(mList.size() - 1).getValue());
				series_line.add(mList.size(), 2);
				series_line.add(mList.size(), 12);
			}
		} else if (deviceType.equalsIgnoreCase("heart_rate")) {
			for (int i = 0; i < mList.size(); i++) {
				series_high.add(i + 1,
						Double.valueOf(mList.get(i).getValue()));
			}

			if (mList.size() > 0) {
				blood_text.setText(mList.get(mList.size() - 1).getValue()
						);
				series_line.add(mList.size(), 40);
				series_line.add(mList.size(), 160);
			}
		} else {

			for (int i = 0; i < mList.size(); i++) {
				series_high.add(i + 1,
						Double.valueOf(mList.get(i).getHigh_value()));
			}

			for (int i = 0; i < mList.size(); i++) {
				series_low.add(i + 1,
						Double.valueOf(mList.get(i).getLow_value()));
			}
			mDataset.addSeries(series_low);
			if (mList.size() > 0) {
				blood_text.setText(mList.get(mList.size() - 1).getHigh_value()
						+ "/" + mList.get(mList.size() - 1).getLow_value());
				series_line.add(mList.size(), 40);
				series_line.add(mList.size(), 160);
			}

		}
		mDataset.addSeries(series_high);
		mDataset.addSeries(series_line);

		// 曲线图的格式，包括颜色，值的范围，点和线的形状等等 都封装在
		// XYSeriesRender对象中，再将XYSeriesRender对象封装在 XYMultipleSeriesRenderer 对象中
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.WHITE);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setPointStrokeWidth(10);
		r.setFillPoints(true);
		XYSeriesRenderer r1 = new XYSeriesRenderer();
		r1.setColor(Color.WHITE);
		r1.setPointStyle(PointStyle.CIRCLE);
		r1.setPointStrokeWidth(10);
		r1.setFillPoints(true);
		XYSeriesRenderer r2 = new XYSeriesRenderer();
		r2.setColor(Color.WHITE);
		r2.setPointStyle(PointStyle.TRIANGLE);
		r2.setFillPoints(true);
		renderer = getRenderer();
		renderer.addSeriesRenderer(r);
		if (deviceType.equalsIgnoreCase("blood_presure")) {// 血ya
			renderer.addSeriesRenderer(r1);
		}
		renderer.addSeriesRenderer(r2);
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

					if (deviceType.equalsIgnoreCase("blood_presure")) {// 血ya
						XYSeries[] series = mDataset.getSeries();

						double high = 0.0, low = 0.0;
						for (XYSeries xySeries : series) {
							if (xySeries.getTitle().equalsIgnoreCase("高压")) {
								high = xySeries.getY(seriesSelection
										.getSeriesIndex());
							}
							if (xySeries.getTitle().equalsIgnoreCase("低压")) {
								low = xySeries.getY(seriesSelection
										.getSeriesIndex());
							}
						}

						blood_text.setText(high + "/" + low);
						series_line.add(seriesSelection.getXValue(), 40);
						series_line.add(seriesSelection.getXValue(), 160);
					} else if (deviceType.equalsIgnoreCase("blood_glucose")) {
						blood_text.setText(seriesSelection.getValue() + "");
						series_line.add(seriesSelection.getXValue(), 2);
						series_line.add(seriesSelection.getXValue(), 12);
					} else {
						blood_text.setText(seriesSelection.getValue() + "");
						series_line.add(seriesSelection.getXValue(), 40);
						series_line.add(seriesSelection.getXValue(), 160);
					}
					setOnClickTime((int) seriesSelection.getXValue());
					view.repaint();
					setImageShow((int) seriesSelection.getValue());
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

		switch (type) {
		case 0:
			// 轴上数字的数量
			renderer.setXLabels(0);
			renderer.setYLabels(0);

			renderer.setXAxisMax(8);
			renderer.setXAxisMin(0);
			renderer.addXTextLabel(1, "一");
			renderer.addXTextLabel(2, "二");
			renderer.addXTextLabel(3, "三");
			renderer.addXTextLabel(4, "四");
			renderer.addXTextLabel(5, "五");
			renderer.addXTextLabel(6, "六");
			renderer.addXTextLabel(7, "日");

			break;
		case 1:
			// 轴上数字的数量
			renderer.setXLabels(15);
			renderer.setYLabels(0);

			renderer.setXAxisMax(30);
			renderer.setXAxisMin(0);

			break;
		case 2:
			renderer.setXLabels(0);
			renderer.setYLabels(0);

			renderer.setXAxisMax(12);
			renderer.setXAxisMin(0);

			renderer.addXTextLabel(1, "1月");
			renderer.addXTextLabel(2, "2月");
			renderer.addXTextLabel(3, "3月");
			renderer.addXTextLabel(4, "4月");
			renderer.addXTextLabel(5, "5月");
			renderer.addXTextLabel(6, "6月");
			renderer.addXTextLabel(7, "7月");
			renderer.addXTextLabel(8, "8月");
			renderer.addXTextLabel(9, "9月");
			renderer.addXTextLabel(10, "10月");
			renderer.addXTextLabel(11, "11月");
			renderer.addXTextLabel(12, "12月");
			break;

		default:
			break;
		}
		if (deviceType.equalsIgnoreCase("blood_glucose")) {// 血糖

			renderer.setYTitle("mmol/L");
			renderer.setYAxisMax(12);
			renderer.setYAxisMin(2);

			renderer.addYTextLabel(2, "2");
			renderer.addYTextLabel(4, "4");
			renderer.addYTextLabel(6, "6");
			renderer.addYTextLabel(8, "8");
			renderer.addYTextLabel(10, "10");
			renderer.addYTextLabel(12, "12");
		} else if (deviceType.equalsIgnoreCase("heart_rate")) {
			renderer.setYTitle("BPM");
			renderer.setYAxisMax(160);
			renderer.setYAxisMin(40);

			renderer.addYTextLabel(40, "40");
			renderer.addYTextLabel(80, "80");
			renderer.addYTextLabel(120, "120");
			renderer.addYTextLabel(160, "160");
		} else {
			renderer.setYTitle("mm\nHg");
			renderer.setYAxisMax(160);
			renderer.setYAxisMin(40);

			renderer.addYTextLabel(40, "40");
			renderer.addYTextLabel(80, "80");
			renderer.addYTextLabel(120, "120");
			renderer.addYTextLabel(160, "160");
		}

		// renderer.setZoomRate(100.0F);
		renderer.setShowLegend(false);
		// renderer.setAntialiasing(true);
		// renderer.setShowAxes(true);
		renderer.setShowCustomTextGrid(true);
		renderer.setExternalZoomEnabled(false);
		// renderer.setFitLegend(true);
		// renderer.setXRoundedLabels(true);

		renderer.setShowGridX(true);
		// renderer.setShowAxes(false);
		renderer.setPanEnabled(true); // 图表是否可以移动
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

	private int totol = 100;

	private void setImageShow(int values) {
		if (deviceType.equalsIgnoreCase("blood_glucose")) {// xuetang

		} else if (deviceType.equalsIgnoreCase("heart_rate")) {

		} else {
			totol = 140;
		}

		double jiao = Math.asin((double) values / middle);

		jiao = Math.toDegrees(jiao);
		jiao = 180 * (values / totol);
		java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
		String s = df.format(jiao);

		float banjing = bgH - imageH - 30 * density;
		float hh = banjing * values / middle;
		double ww = Math.sqrt(banjing * banjing - (banjing * values / middle)
				* (banjing * values / middle));
		Log.d("demo", "hh" + hh);
		Log.d("demo", "ww" + ww);
		RotateAnimation animation = new RotateAnimation(0f, Float.valueOf(s),
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

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public void onClick(View v) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new DateFormat();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy");
		try {
			switch (v.getId()) {
			case R.id.chart_left:
				switch (type) {
				case 0:
					calendar.setTimeInMillis(simpleDateFormat.parse(startTime)
							.getTime());
					calendar.add(Calendar.DATE, -1);
					startTime = (String) dateFormat.format("yyyy-MM-dd",
							calendar);
					calendar.add(Calendar.DATE, -6);
					endTime = startTime;
					startTime = (String) dateFormat.format("yyyy-MM-dd",
							calendar);
					break;
				case 1:
					calendar.setTimeInMillis(simpleDateFormat.parse(startTime)
							.getTime());
					calendar.add(Calendar.DATE, -1);
					startTime = (String) dateFormat.format("yyyy-MM-dd",
							calendar);
					calendar.add(Calendar.MONTH, -1);
					endTime = startTime;
					startTime = (String) dateFormat.format("yyyy-MM-dd",
							calendar);
					break;
				case 2:

					calendar.setTimeInMillis(simpleDateFormat2.parse(startTime)
							.getTime());
					calendar.add(Calendar.YEAR, -1);
					endTime = startTime;
					startTime = (String) dateFormat.format("yyyy", calendar);

					break;

				default:
					break;
				}
				blood_time.setText(endTime);
				new WebServiceUtils(getActivity(), mHandler).sendExecute(
						new String[] {
								SharePrefenceUtils.getAccount(getActivity()),
								deviceType, device_sn, relative, timeType,
								startTime, endTime },
						WebServiceParmas.GET_BLOOD_DATA,
						WebServiceParmas.HTTP_POST, "加载中...");
				break;
			case R.id.chart_right:

				switch (type) {
				case 0:
					calendar.setTimeInMillis(simpleDateFormat.parse(endTime)
							.getTime());
					calendar.add(Calendar.DATE, 1);
					endTime = (String) dateFormat
							.format("yyyy-MM-dd", calendar);
					calendar.add(Calendar.DATE, 6);
					startTime = endTime;
					endTime = (String) dateFormat
							.format("yyyy-MM-dd", calendar);
					break;
				case 1:
					calendar.setTimeInMillis(simpleDateFormat.parse(endTime)
							.getTime());
					calendar.add(Calendar.DATE, 1);
					endTime = (String) dateFormat
							.format("yyyy-MM-dd", calendar);
					calendar.add(Calendar.MONTH, 1);
					startTime = endTime;
					endTime = (String) dateFormat
							.format("yyyy-MM-dd", calendar);
					break;
				case 2:
					calendar.setTimeInMillis(simpleDateFormat2.parse(endTime)
							.getTime());
					calendar.add(Calendar.YEAR, 1);
					startTime = endTime;
					endTime = (String) dateFormat.format("yyyy", calendar);
					break;

				default:
					break;
				}
				blood_time.setText(endTime);
				new WebServiceUtils(getActivity(), mHandler).sendExecute(
						new String[] {
								SharePrefenceUtils.getAccount(getActivity()),
								deviceType, device_sn, relative, timeType,
								startTime, endTime },
						WebServiceParmas.GET_BLOOD_DATA,
						WebServiceParmas.HTTP_POST, "加载中...");
				break;

			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initRequstTime(int type) {
		Calendar nowCalendar = Calendar.getInstance(Locale.CHINA);
		nowCalendar.setTimeInMillis(System.currentTimeMillis());

		switch (type) {
		case 0:
			int week = nowCalendar.get(Calendar.DAY_OF_WEEK);
			loger.d(week);
			timeType = "week";
			endTime = AndroidUtils.getTime();
			startTime = AndroidUtils.getDateBefore((week - 2));
			if (week == 1) {
				startTime = AndroidUtils.getDateBefore(6);
			}
			break;
		case 1:
			timeType = "month";
			int month = nowCalendar.get(Calendar.DAY_OF_MONTH);
			endTime = AndroidUtils.getTime();
			startTime = AndroidUtils.getDateBefore(month - 1);
			break;
		case 2:
			timeType = "year";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
			String year = dateFormat
					.format(new Date(System.currentTimeMillis()));
			startTime = year;
			endTime = year;
			break;

		default:
			break;
		}
		blood_time.setText(endTime);

	}

	public void reflashData(int type2) {
		this.type = type2;
		initRequstTime(type);

		new WebServiceUtils(getActivity(), mHandler).sendExecute(new String[] {
				SharePrefenceUtils.getAccount(getActivity()), deviceType,
				device_sn, relative, timeType, startTime, endTime },
				WebServiceParmas.GET_BLOOD_DATA, WebServiceParmas.HTTP_POST,
				"加载中...");
	}

	private void setOnClickTime(int click) {

		switch (type) {
		case 0:
		case 1:
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			try {
				calendar.setTimeInMillis(simpleDateFormat.parse(startTime)
						.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			calendar.add(Calendar.DAY_OF_MONTH, click);
			DateFormat dateFormat = new DateFormat();
			String time = (String) dateFormat.format("yyyy-MM-dd", calendar);
			blood_time.setText(time);
			break;
		case 2:
			blood_time.setText(startTime);
			break;

		default:
			break;
		}

	}
}
