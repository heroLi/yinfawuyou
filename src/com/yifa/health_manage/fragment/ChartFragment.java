package com.yifa.health_manage.fragment;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

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
import android.content.Context;
import android.content.Intent;
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
import com.yifa.health_manage.view.MyLinearLayout;

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

	private MyLinearLayout myLinearLayout;
	private MyLoger loger = MyLoger.getInstence("ChartFragment");

	private LinearLayout bloodLine;

	private String timeTitle = "2014";

	private int angel = 0;

	private int type = 0;// 0周 1月2年

	private TextView time_title, blood_text, blood_Flag, blood_mes, blood_time,
			blood_level, shareText;

	private int bgH, bgW, imageH, imageW;

	private float density;
	private LinearLayout linearLayoutTop;
	private ImageView arrow;

	private RelativeLayout topLayout;

	private FrameLayout chartLayout;

	private String startTime, endTime;

	private ImageButton leftButton, rightButton;

	private int monthChoice = 1;

	public ChartFragment(String type, int timeType) {
		this.deviceType = type;
		this.type = timeType;
	}

	// blood_presure 血压 blood_glucose 血糖
	private String timeType = "week", deviceType = "blood_glucose",
			device_sn = "123456", relative = "12345", startdate, enddate;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case WebServiceParmas.GET_BLOOD_DATA:
				loger.d(msg.obj.toString());
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
					List<BloodValuesInfo> mListNewData = new ArrayList<BloodValuesInfo>();

					int valuesHigh = 0;
					int valuesLow = 0;
					int count = 1;

					if (mList.size() <= 0) {
						ChartActivity cahrt = (ChartActivity) getActivity();
						cahrt.setListData(new ArrayList<BloodValuesInfo>());
						initData(new ArrayList<BloodValuesInfo>());
					} else {
						if (mList.size() == 1) {
							mListNewData.addAll(mList);
						}
						if (deviceType.equalsIgnoreCase("blood_presure")) {// 血压
							valuesHigh += Integer.valueOf(mList.get(0)
									.getHigh_value());
							valuesLow += Integer.valueOf(mList.get(0)
									.getLow_value());

							for (int i = 1; i < mList.size(); i++) {
								BloodValuesInfo bloodValuesInfo1 = mList
										.get(i - 1);
								BloodValuesInfo bloodValuesInfo2 = mList.get(i);
								String time = bloodValuesInfo1.getDatetime()
										.substring(
												0,
												bloodValuesInfo1.getDatetime()
														.indexOf(" ") == -1 ? 7
														: bloodValuesInfo1
																.getDatetime()
																.indexOf(" "));
								String time2 = bloodValuesInfo2.getDatetime()
										.substring(
												0,
												bloodValuesInfo2.getDatetime()
														.indexOf(" ") == -1 ? 7
														: bloodValuesInfo2
																.getDatetime()
																.indexOf(" "));
								if (time.equalsIgnoreCase(time2)) {
									count++;
									valuesHigh += Integer.valueOf(mList.get(i)
											.getHigh_value());
									valuesLow += Integer.valueOf(mList.get(i)
											.getLow_value());
									if (i == (mList.size() - 1)) {
										bloodValuesInfo2
												.setHigh_value((valuesHigh / count)
														+ "");
										bloodValuesInfo2
												.setLow_value((valuesLow / count)
														+ "");
										mListNewData.add(bloodValuesInfo2);
									}
								} else {
									bloodValuesInfo1
											.setHigh_value((valuesHigh / count)
													+ "");
									bloodValuesInfo1
											.setLow_value((valuesLow / count)
													+ "");
									mListNewData.add(bloodValuesInfo1);
									count = 1;
									valuesHigh += Integer
											.valueOf(bloodValuesInfo2
													.getHigh_value());
									valuesLow += Integer
											.valueOf(bloodValuesInfo2
													.getLow_value());
									if (i == (mList.size() - 1)) {
										mListNewData.add(bloodValuesInfo2);
									}
								}
							}
						} else {
							valuesHigh += Integer.valueOf(mList.get(0)
									.getValue());

							for (int i = 1; i < mList.size(); i++) {
								BloodValuesInfo bloodValuesInfo1 = mList
										.get(i - 1);
								BloodValuesInfo bloodValuesInfo2 = mList.get(i);
								String time = bloodValuesInfo1.getDatetime()
										.substring(
												0,
												bloodValuesInfo1.getDatetime()
														.indexOf(" ") == -1 ? 7
														: bloodValuesInfo1
																.getDatetime()
																.indexOf(" "));
								String time2 = bloodValuesInfo2.getDatetime()
										.substring(
												0,
												bloodValuesInfo2.getDatetime()
														.indexOf(" ") == -1 ? 7
														: bloodValuesInfo2
																.getDatetime()
																.indexOf(" "));
								if (time.equalsIgnoreCase(time2)) {

									valuesHigh += Integer.valueOf(mList.get(i)
											.getValue());
									count++;
									if (i == (mList.size() - 1)) {
										bloodValuesInfo2
												.setValue((valuesHigh / count)
														+ "");
										mListNewData.add(bloodValuesInfo2);
									}
								} else {
									bloodValuesInfo1
											.setValue((valuesHigh / count) + "");
									mListNewData.add(bloodValuesInfo1);
									count = 1;
									valuesHigh = Integer
											.valueOf(bloodValuesInfo2
													.getValue());
									if (i == (mList.size() - 1)) {
										mListNewData.add(bloodValuesInfo2);
									}

								}
							}

						}

						ChartActivity cahrt = (ChartActivity) getActivity();
						cahrt.setListData(mList);
						initData(mListNewData);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ChartActivity cahrt = (ChartActivity) getActivity();
					cahrt.setListData(new ArrayList<BloodValuesInfo>());
					initData(new ArrayList<BloodValuesInfo>());
				}
				time_title.setText(startTime + "-" + endTime);
				if (startTime.equalsIgnoreCase(endTime) && type == 2) {
					time_title.setText(startTime);
				}
				blood_time.setText(startTime);
//				blood_text.setText("0");
				break;
			case -1:
				AndroidUtils.showToast(getActivity(), "请连接网络");
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
		blood_level = (TextView) view.findViewById(R.id.blood_detail_level_tv);
		shareText = (TextView) view.findViewById(R.id.share_text);
		blood_mes = (TextView) view.findViewById(R.id.text);
		blood_time = (TextView) view.findViewById(R.id.time);
		myLinearLayout = (MyLinearLayout) view
				.findViewById(R.id.layout_halfRound);
		linearLayoutTop = (LinearLayout) view
				.findViewById(R.id.chart_layput_top);
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
			linearLayoutTop.setBackgroundColor(Color.parseColor("#1f65c4"));
			DisplayMetrics dm = getResources().getDisplayMetrics();
			if (dm.widthPixels < 700) {
				myLinearLayout.setBackImage(R.drawable.xinlv_group_small);
			} else
				myLinearLayout.setBackImage(R.drawable.xinlv_group);
			blood_Flag.setText("mmol/L");
			blood_mes.setText("血糖详情");

		} else if (deviceType.equalsIgnoreCase("blood_presure")) {
			relative = SharePrefenceUtils.getPressureFriendId(getActivity())
					.getId();
			device_sn = SharePrefenceUtils.getPressureFriendId(getActivity())
					.getDevice_sn();
			DisplayMetrics dm = getResources().getDisplayMetrics();
			if (dm.widthPixels < 700) {
				myLinearLayout.setBackImage(R.drawable.xueya_group_small);
			} else
				myLinearLayout.setBackImage(R.drawable.xueya_group);
			topLayout.setBackgroundColor(Color.parseColor("#ff5c3d"));
			chartLayout.setBackgroundColor(Color.parseColor("#ff5c3d"));
			linearLayoutTop.setBackgroundColor(Color.parseColor("#ff5c3d"));
			blood_mes.setText("血压详情");
			blood_Flag.setText("mmHg");
		} else if (deviceType.equalsIgnoreCase("heart_rate")) {
			relative = SharePrefenceUtils.getPressureFriendId(getActivity())
					.getId();
			device_sn = SharePrefenceUtils.getPressureFriendId(getActivity())
					.getDevice_sn();
			DisplayMetrics dm = getResources().getDisplayMetrics();
			if (dm.widthPixels < 700) {
				myLinearLayout.setBackImage(R.drawable.xinlv_group_small);
			} else
				myLinearLayout.setBackImage(R.drawable.xinlv_group);
			topLayout.setBackgroundColor(Color.parseColor("#e63a6c"));
			chartLayout.setBackgroundColor(Color.parseColor("#e63a6c"));
			linearLayoutTop.setBackgroundColor(Color.parseColor("#e63a6c"));
			blood_mes.setText("心率详情");
			blood_Flag.setText("BPM");
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		monthChoice = calendar.get(Calendar.MONTH);
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

		// initLineSet();
		initListener();
	}

	private void initListener() {
		leftButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		shareText.setOnClickListener(this);
		loadData();

	}

	public void loadData() {
		if (deviceType.equalsIgnoreCase("blood_glucose")) {
			relative = SharePrefenceUtils.getSugarFriendId(getActivity())
					.getId();
			device_sn = SharePrefenceUtils.getSugarFriendId(getActivity())
					.getDevice_sn();
		} else {
			relative = SharePrefenceUtils.getPressureFriendId(getActivity())
					.getId();
			device_sn = SharePrefenceUtils.getPressureFriendId(getActivity())
					.getDevice_sn();
		}
		if (!relative.equals("") && !device_sn.equals("")) {
			String start = startTime.replace(".", "-");
			String end = endTime.replace(".", "-");

			new WebServiceUtils(getActivity(), mHandler).sendExecute(
					new String[] {
							SharePrefenceUtils.getAccount(getActivity()),
							deviceType, device_sn, relative, timeType, start,
							end }, WebServiceParmas.GET_BLOOD_DATA,
					WebServiceParmas.HTTP_POST, "加载中...");
		} else {
			ChartActivity cahrt = (ChartActivity) getActivity();
			cahrt.setListData(new ArrayList<BloodValuesInfo>());
			initData(new ArrayList<BloodValuesInfo>());
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
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
				if (type == 0) {
					int a = initDateTime(mList.get(i).getDatetime());
					if (a == 0) {
						a = 7;
					} else if (a == 9) {
						a = 2;
					}
					series_high.add(a,
							Double.valueOf(mList.get(i).getValue()) / 18);
				} else {
					series_high.add(initDateTime(mList.get(i).getDatetime()),
							Double.valueOf(mList.get(i).getValue()) / 18);
				}
			}
			if (mList.size() > 0) {
				blood_text.setText(mList.get(mList.size() - 1).getValue());
				series_line.add(initDateTime(mList.get(0).getDatetime()), 2);
				series_line.add(initDateTime(mList.get(0).getDatetime()), 34);
				initBloodLevel(AndroidUtils.getBloodLevel(1,
						Integer.valueOf(mList.get(0).getValue())));
				// setImageShow(Integer.valueOf(mList.get(mList.size() - 1)
				// .getValue()));
				myLinearLayout.setReflush(angel);
			} else
				series_high.add(-10, -10);

		} else if (deviceType.equalsIgnoreCase("heart_rate")) {
			for (int i = 0; i < mList.size(); i++) {
				if (type == 0) {
					int a = initDateTime(mList.get(i).getDatetime());
					if (a == 0) {
						a = 7;
					} else if (a == 9) {
						a = 2;
					}
					series_high.add(a, Double.valueOf(mList.get(i).getValue()));
				} else {
					series_high.add(initDateTime(mList.get(i).getDatetime()),
							Double.valueOf(mList.get(i).getValue()));
				}

			}

			if (mList.size() > 0) {
				blood_text.setText(mList.get(0).getValue());
				series_line.add(initDateTime(mList.get(0).getDatetime()), 40);
				series_line.add(initDateTime(mList.get(0).getDatetime()), 195);
				initBloodLevel(AndroidUtils
						.getBloodLevel(0, Integer.valueOf(mList.get(
								mList.size() - 1).getValue())));
				myLinearLayout.setReflush(angel);
			} else
				series_high.add(-10, -10);

		} else {

			for (int i = 0; i < mList.size(); i++) {

				if (type == 0) {
					int a = initDateTime(mList.get(i).getDatetime());
					if (a == 0) {
						a = 7;
					} else if (a == 9) {
						a = 2;
					}
					series_high.add(a,
							Double.valueOf(mList.get(i).getHigh_value()));
					series_low.add(a,
							Double.valueOf(mList.get(i).getLow_value()));
				} else {
					series_high.add(initDateTime(mList.get(i).getDatetime()),
							Double.valueOf(mList.get(i).getHigh_value()));
					series_low.add(initDateTime(mList.get(i).getDatetime()),
							Double.valueOf(mList.get(i).getLow_value()));
				}
			}

			mDataset.addSeries(series_low);

			if (mList.size() > 0) {
				blood_text.setText(mList.get(mList.size() - 1).getHigh_value()
						+ "/" + mList.get(mList.size() - 1).getLow_value());
				series_line.add(initDateTime(mList.get(0).getDatetime()), 40);
				series_line.add(initDateTime(mList.get(0).getDatetime()), 195);
				initBloodLevel(AndroidUtils.getBloodLevel(0, Integer
						.valueOf(mList.get(mList.size() - 1).getHigh_value())));
				myLinearLayout.setReflush(angel);
			} else
				series_high.add(-10, -10);

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
		r2.setColor(Color.GREEN);
		r2.setPointStyle(PointStyle.TRIANGLE);
		r2.setPointStrokeWidth(20);
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
				DecimalFormat decimalFormat = new DecimalFormat("#.#");
				if (seriesSelection == null) {
				} else {
					series_line.clear();
					arrow.setVisibility(View.GONE);
					if (deviceType.equalsIgnoreCase("blood_presure")) {// 血ya
						XYSeries[] series = mDataset.getSeries();

						double high = 0.0, low = 0.0;
						for (XYSeries xySeries : series) {
							if (xySeries.getTitle().equalsIgnoreCase("高压")) {
								high = xySeries.getY(seriesSelection
										.getPointIndex());
								loger.d(seriesSelection.getPointIndex()
										+ "---high");
							}
							if (xySeries.getTitle().equalsIgnoreCase("低压")) {
								low = xySeries.getY(seriesSelection
										.getPointIndex());
								loger.d(seriesSelection.getPointIndex()
										+ "---low");

							}
						}

						blood_text.setText((int) high + "/" + (int) low);

						initBloodLevel(AndroidUtils
								.getBloodLevel(0, (int) high));
						series_line.add(seriesSelection.getXValue(), 40);
						series_line.add(seriesSelection.getXValue(), 195);
					} else if (deviceType.equalsIgnoreCase("blood_glucose")) {
						blood_text.setText(decimalFormat
								.format((Double) (seriesSelection.getValue())));
						series_line.add(seriesSelection.getXValue(), 2);
						series_line.add(seriesSelection.getXValue(), 34);
						initBloodLevel(AndroidUtils.getBloodLevel(1,
								(int) seriesSelection.getValue()));
					} else {
						blood_text.setText((int) seriesSelection.getValue()
								+ "");
						series_line.add(seriesSelection.getXValue(), 40);
						series_line.add(seriesSelection.getXValue(), 195);
						initBloodLevel(AndroidUtils.getBloodLevel(2,
								(int) seriesSelection.getValue()));
					}
					setOnClickTime((int) seriesSelection.getXValue());
					view.repaint();
					myLinearLayout.setReflush(angel);
					// setImageShow((int) seriesSelection.getValue());
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
		renderer.setMargins(new int[] { 0, 40, 0, 0 });// 上，左，xia 右
		// 设置边距的颜色
		renderer.setMarginsColor(Color.parseColor("#00ffffff"));
		// 设置xy轴线的颜色
		// renderer.setAxesColor(Color.parseColor("#00ffffff"));

		// 设置xy轴字的颜色
		renderer.setXLabelsColor(Color.parseColor("#ffffff"));
		renderer.setYLabelsColor(0, Color.parseColor("#ffffff"));

		renderer.setLegendHeight(20);
		// renderer.setYLabelsAlign(Paint.Align.RIGHT);
		// renderer.setYAxisAlign(Paint.Align.LEFT, 0);
		renderer.setYLabelsAlign(Paint.Align.RIGHT);// 右对齐
		renderer.setXLabelsAlign(Paint.Align.RIGHT);

		renderer.setYTitle("mm\nhg");
		renderer.setXTitle("");
		renderer.setPanLimits(new double[] { 0, 12, 2, 12 });
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
			renderer.setPanLimits(new double[] { 0, 8, 2, 12 });
			break;
		case 1:
			renderer.setXAxisMin(0);
			renderer.setXAxisMax(15);
			// 轴上数字的数量
			renderer.setXLabels(0);
			renderer.setYLabels(0);

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			int year = calendar.get(Calendar.YEAR);
			calendar.set(year, monthChoice, 1);
			int day = calendar.getActualMaximum(Calendar.DATE);
			//
			// renderer.addXTextLabel(1, "1");
			// renderer.addXTextLabel(2, "2");
			// renderer.addXTextLabel(3, "3");
			// renderer.addXTextLabel(4, "4");
			// renderer.addXTextLabel(5, "5");
			// renderer.addXTextLabel(6, "6");
			// renderer.addXTextLabel(7, "7");

			for (int i = 1; i <= day; i++) {
				renderer.addXTextLabel(i, i + "");
			}
			double maxX = day + 0.5;
			renderer.setPanLimits(new double[] { 0, maxX, 2, 12 });
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
			renderer.setPanLimits(new double[] { 0, 13, 2, 12 });
			break;

		default:
			break;
		}
		if (deviceType.equalsIgnoreCase("blood_glucose")) {// 血糖

			renderer.setYTitle("mmol/L");
			renderer.setYAxisMax(35.5);
			renderer.setYAxisMin(2);

			renderer.addYTextLabel(2, "2");
//			renderer.addYTextLabel(4, "4");
//			renderer.addYTextLabel(6, "6");
			renderer.addYTextLabel(8, "8");
//			renderer.addYTextLabel(10, "10");
//			renderer.addYTextLabel(12, "12");
			renderer.addYTextLabel(14, "14");
			renderer.addYTextLabel(20, "20");
			renderer.addYTextLabel(26, "26");
			renderer.addYTextLabel(32, "32");
			renderer.addYTextLabel(34, "34");
		} else if (deviceType.equalsIgnoreCase("heart_rate")) {
			renderer.setYTitle("BPM");
			renderer.setYAxisMax(195);
			renderer.setYAxisMin(40);

			renderer.addYTextLabel(40, "40");
			renderer.addYTextLabel(80, "80");
			renderer.addYTextLabel(120, "120");
			renderer.addYTextLabel(160, "160");
		} else {
			renderer.setYTitle("mm\nHg");
			renderer.setYAxisMax(195);
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

		renderer.setShowGrid(true);
		// renderer.setShowAxes(false);
		renderer.setPanEnabled(true); // 图表是否可以移动
		// renderer.setZoomEnabled(false); // 图表是否可以缩放

		renderer.setZoomEnabled(false, false);
		renderer.setZoomLimits(new double[] { 0, 0, 0, 0 });
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

	private int middle = 100;

	private int totol = 100;

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public void onClick(View v) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new DateFormat();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy");
		try {
			switch (v.getId()) {
			case R.id.chart_left:
				switch (type) {
				case 0:
					calendar.setTimeInMillis(simpleDateFormat.parse(startTime)
							.getTime());
					calendar.add(Calendar.DATE, -1);
					startTime = (String) dateFormat.format("yyyy.MM.dd",
							calendar);
					calendar.add(Calendar.DATE, -6);
					endTime = startTime;
					startTime = (String) dateFormat.format("yyyy.MM.dd",
							calendar);
					break;
				case 1:
					calendar.setTimeInMillis(simpleDateFormat.parse(startTime)
							.getTime());
					calendar.add(Calendar.DATE, -1);
					startTime = (String) dateFormat.format("yyyy.MM.dd",
							calendar);
					calendar.add(Calendar.DATE, 1);
					calendar.add(Calendar.MONTH, -1);
					monthChoice = calendar.get(Calendar.MONTH);

					// endTime = startTime;
					// startTime = (String) dateFormat.format("yyyy-MM-dd",
					// calendar);
					int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
					startTime = (String) dateFormat.format("yyyy.MM", calendar)
							+ "." + 01;
					endTime = (String) dateFormat.format("yyyy.MM", calendar)
							+ "." + day;

					break;
				case 2:

					calendar.setTimeInMillis(simpleDateFormat2.parse(startTime)
							.getTime());
					calendar.add(Calendar.YEAR, -1);
					startTime = (String) dateFormat.format("yyyy", calendar);
					endTime = startTime;

					break;

				default:
					break;
				}
				blood_time.setText(endTime);
				String start = startTime.replace(".", "-");
				String end = endTime.replace(".", "-");
				new WebServiceUtils(getActivity(), mHandler).sendExecute(
						new String[] {
								SharePrefenceUtils.getAccount(getActivity()),
								deviceType, device_sn, relative, timeType,
								start, end }, WebServiceParmas.GET_BLOOD_DATA,
						WebServiceParmas.HTTP_POST, "加载中...");
				break;
			case R.id.chart_right:

				switch (type) {
				case 0:
					calendar.setTimeInMillis(simpleDateFormat.parse(endTime)
							.getTime());
					calendar.add(Calendar.DATE, 1);
					endTime = (String) dateFormat
							.format("yyyy.MM.dd", calendar);
					calendar.add(Calendar.DATE, 6);
					startTime = endTime;
					endTime = (String) dateFormat
							.format("yyyy.MM.dd", calendar);
					break;
				case 1:
					calendar.setTimeInMillis(simpleDateFormat.parse(endTime)
							.getTime());
					calendar.add(Calendar.DATE, 1);
					endTime = (String) dateFormat
							.format("yyyy.MM.dd", calendar);
					calendar.add(Calendar.DATE, -1);
					calendar.add(Calendar.MONTH, 1);
					monthChoice = calendar.get(Calendar.MONTH);
					int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
					startTime = (String) dateFormat.format("yyyy.MM", calendar)
							+ "." + 01;
					endTime = (String) dateFormat.format("yyyy.MM", calendar)
							+ "." + day;
					break;
				case 2:
					calendar.setTimeInMillis(simpleDateFormat2.parse(endTime)
							.getTime());
					calendar.add(Calendar.YEAR, 1);
					endTime = (String) dateFormat.format("yyyy", calendar);
					startTime = endTime;
					break;

				default:
					break;
				}
				blood_time.setText(endTime);
				String start1 = startTime.replace(".", "-");
				String end1 = endTime.replace(".", "-");
				new WebServiceUtils(getActivity(), mHandler).sendExecute(
						new String[] {
								SharePrefenceUtils.getAccount(getActivity()),
								deviceType, device_sn, relative, timeType,
								start1, end1 },
						WebServiceParmas.GET_BLOOD_DATA,
						WebServiceParmas.HTTP_POST, "加载中...");
				break;
			case R.id.share_text:
				shareClient(getActivity(), "银发无忧", "我正在使用银发无忧App，您也快来用吧！请点击网址http://121.40.172.222/health2/site/app下载并使用此应用！");
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
		String start1 = startTime.replace(".", "-");
		String end1 = endTime.replace(".", "-");
		new WebServiceUtils(getActivity(), mHandler).sendExecute(new String[] {
				SharePrefenceUtils.getAccount(getActivity()), deviceType,
				device_sn, relative, timeType, start1, end1 },
				WebServiceParmas.GET_BLOOD_DATA, WebServiceParmas.HTTP_POST,
				"加载中...");
	}

	private void setOnClickTime(int click) {

		switch (type) {
		case 0:
			Calendar calendar0 = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat0 = new SimpleDateFormat(
					"yyyy.MM.dd");
			try {
				calendar0.setTimeInMillis(simpleDateFormat0.parse(startTime)
						.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			calendar0.add(Calendar.DAY_OF_WEEK, click - 1);
			DateFormat dateFormat0 = new DateFormat();
			String time0 = (String) dateFormat0.format("yyyy.MM.dd", calendar0);
			blood_time.setText(time0);
			break;
		case 1:
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy.MM.dd");
			try {
				calendar.setTimeInMillis(simpleDateFormat.parse(startTime)
						.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			calendar.set(Calendar.DAY_OF_MONTH, click);
			DateFormat dateFormat = new DateFormat();
			String time = (String) dateFormat.format("yyyy.MM.dd", calendar);
			blood_time.setText(time);

			break;
		case 2:
			Calendar calendar2 = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy");
			try {
				calendar2.setTimeInMillis(simpleDateFormat2.parse(startTime)
						.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			calendar2.set(Calendar.MONTH, click);
			DateFormat dateFormat2 = new DateFormat();
			String time2 = (String) dateFormat2.format("yyyy.MM", calendar2);
			blood_time.setText(time2);
			break;

		default:
			break;
		}

	}

	/**
	 * 时间
	 * */
	private int initDateTime(String time) {
		SimpleDateFormat dateFormat = null;
		switch (type) {
		case 0:
		case 1:
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			timeTitle = time.substring(0, time.indexOf(" "));
			break;
		case 2:
			dateFormat = new SimpleDateFormat("yyyy-MM");
			timeTitle = time;
			break;

		default:
			break;
		}

		Date date = null;
		try {
			date = dateFormat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		switch (type) {
		case 0:
			return calendar.get(Calendar.DAY_OF_WEEK) - 1;
		case 1:
			return calendar.get(Calendar.DAY_OF_MONTH);
		case 2:
			return calendar.get(Calendar.MONTH) + 1;

		default:
			break;
		}

		return 0;

	}

	private void initBloodLevel(int type) {
		if (deviceType.equalsIgnoreCase("blood_presure")) {
			switch (type) {
			case 0:
				blood_level.setText("理想");
				angel = 15;
				break;
			case 1:
				blood_level.setText("正常");
				angel = 60;
				break;
			case 2:
				blood_level.setText("偏高");
				angel = 95;
				break;
			case 3:
				blood_level.setText("轻度");
				angel = 105;
				break;
			case 4:
				blood_level.setText("中度");
				angel = 150;
				break;
			case 5:
				blood_level.setText("高度");
				angel = 170;
				break;

			default:
				break;
			}

		} else if (deviceType.equalsIgnoreCase("blood_glucose")) {
			switch (type) {
			case 0:
				blood_level.setText("理想");
				angel = 90;
				break;
			case 1:
				blood_level.setText("低血糖");
				angel = 20;
				break;
			case 2:
				blood_level.setText("高血糖");
				angel = 150;
				break;

			default:
				angel = 0;
				break;
			}
		} else {
			switch (type) {
			case 0:
				blood_level.setText("正常");
				angel = 90;
				break;
			case 1:
				blood_level.setText("偏慢");
				angel = 30;
				break;
			case 2:
				blood_level.setText("偏快");
				angel = 150;
				break;

			default:
				angel = 0;
				break;
			}
		}
	}

	/** 调用系统分享功能 */
	public void shareClient(Context context, String title, String message) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain"); // 分享发送的数据类型
		String msg = message;// 分享的内容
		shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
		context.startActivity(Intent.createChooser(shareIntent, title));// 目标应用选择对话框的标题
	}
}
