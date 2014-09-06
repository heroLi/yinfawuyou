package com.yifa.health_manage.fragment;

import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.yifa.health_manage.R;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * 图表界面
 * **/
public class ChartFragment extends Fragment {

	private String title = "线性统计图示例";
	private XYSeries series_high;// XY数据点 用于提供绘制的点集合的数据
	private XYSeries series_low;// XY数据点
	private XYMultipleSeriesDataset mDataset;// XY轴数据集存放XYSeries
	private GraphicalView mViewChart;// 用于显示现行统计图
	private XYMultipleSeriesRenderer renderer;// 线性统计图主描绘器
	private LinearLayout mLayout;
	private int X = 10;// X数据集大小
	private int Y = 200;//

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chart, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mLayout = (LinearLayout) view.findViewById(R.id.chart_layout);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	private void initData() {
		series_high = new XYSeries("高压");
		Random random = new Random();
		for (int i = 0; i < 7; i++) {
			series_high.add(i + 1, random.nextInt(9) + 60);
		}
		series_low = new XYSeries("低压");
		for (int i = 0; i < 7; i++) {
			series_low.add(i + 1, random.nextInt(9) + 80);
		}

		mDataset = new XYMultipleSeriesDataset();
		mDataset.addSeries(series_high);
		mDataset.addSeries(series_low);

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
		renderer.addSeriesRenderer(r2);

		final GraphicalView view = ChartFactory.getLineChartView(getActivity(),
				mDataset, renderer);
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
		renderer.setMargins(new int[] { 10, 50, 10, 10 });
		// 设置边距的颜色
		renderer.setMarginsColor(R.color.app_green);

		renderer.setYTitle("mm\nhg");
		renderer.setXTitle("");
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
