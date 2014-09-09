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

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

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

/**
 * 图表界面
 * **/
public class ChartFragment extends Fragment {

	private XYSeries series_high;// XY数据点 用于提供绘制的点集合的数据
	private XYSeries series_low;// XY数据点
	private XYMultipleSeriesDataset mDataset;// XY轴数据集存放XYSeries
	private XYMultipleSeriesRenderer renderer;// 线性统计图主描绘器
	private LinearLayout mLayout;
	private MyLoger loger = MyLoger.getInstence("ChartFragment");
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

		new WebServiceUtils(getActivity(), mHandler).sendExecute(new String[] {
				SharePrefenceUtils.getAccount(getActivity()), deviceType,
				device_sn, relative, timeType, AndroidUtils.getDateBefore(3),
				AndroidUtils.getTime() }, WebServiceParmas.GET_BLOOD_DATA,
				WebServiceParmas.HTTP_POST, "加载中...");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	private void initData(List<BloodValuesInfo> mList) {

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

		// 曲线图的格式，包括颜色，值的范围，点和线的形状等等 都封装在
		// XYSeriesRender对象中，再将XYSeriesRender对象封装在 XYMultipleSeriesRenderer 对象中
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.WHITE);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		XYSeriesRenderer r2 = new XYSeriesRenderer();
		r2.setColor(Color.WHITE);
		r2.setPointStyle(PointStyle.CIRCLE);
		r2.setFillPoints(true);
		renderer = getRenderer();
		renderer.addSeriesRenderer(r);
		if (!deviceType.equalsIgnoreCase("blood_glucose")) {// 血糖
			renderer.addSeriesRenderer(r2);
		}
		// 点击
		renderer.setClickEnabled(true);
		renderer.setSelectableBuffer(40);

		final GraphicalView view = ChartFactory.getLineChartView(getActivity(),
				mDataset, renderer);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// handle the click event on the chart
				SeriesSelection seriesSelection = view
						.getCurrentSeriesAndPoint();

				if (seriesSelection == null) {
					Toast.makeText(getActivity(), "No chart element",
							Toast.LENGTH_SHORT).show();
				} else {
					// display information of the clicked point
					Toast.makeText(
							getActivity(),
							"Chart element in series index "
									+ seriesSelection.getSeriesIndex()
									+ " data point index "
									+ seriesSelection.getPointIndex()
									+ " was clicked"
									+ " closest point value X="
									+ seriesSelection.getXValue() + ", Y="
									+ seriesSelection.getValue(),
							Toast.LENGTH_SHORT).show();
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
		renderer.setBackgroundColor(R.color.app_green);
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
		renderer.setPointSize(5f);
		// 设置边距
		renderer.setMargins(new int[] { 20, 50, 10, 0 });// 上，左，xia 右
		// 设置边距的颜色
		renderer.setMarginsColor(R.color.app_green);

		renderer.setYTitle("mm\nhg");
		renderer.setXTitle("");

		renderer.setXAxisMax(8);
		renderer.setXAxisMin(0);
		renderer.setYAxisMax(160);
		renderer.setYAxisMin(40);
		// 轴上数字的数量
		renderer.setXLabels(0);
		renderer.setYLabels(4);

		renderer.addXTextLabel(1, "一");
		renderer.addXTextLabel(2, "二");
		renderer.addXTextLabel(3, "三");
		renderer.addXTextLabel(4, "四");
		renderer.addXTextLabel(5, "五");
		renderer.addXTextLabel(6, "六");
		renderer.addXTextLabel(7, "七");

		// renderer.addYTextLabel(40, "40");
		// renderer.addYTextLabel(80, "80");
		// renderer.addYTextLabel(120, "120");
		// renderer.addYTextLabel(160, "160");

		renderer.setShowGridX(true);
		// renderer.setShowAxes(false);
		renderer.setPanEnabled(false); // 图表是否可以移动
		renderer.setZoomEnabled(true); // 图表是否可以缩放
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
}
