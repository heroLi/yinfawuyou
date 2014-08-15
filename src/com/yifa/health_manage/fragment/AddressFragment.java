package com.yifa.health_manage.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yifa.health_manage.R;
import com.yifa.health_manage.service.HandlerThreads;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddressFragment extends Fragment{

    private String title = "线性统计图示例";  
    private TimeSeries series_high;// XY数据点  
    private TimeSeries series_low;// XY数据点
    private XYMultipleSeriesDataset mDataset;// XY轴数据集  
    private GraphicalView mViewChart;// 用于显示现行统计图  
    private XYMultipleSeriesRenderer mXYRenderer;// 线性统计图主描绘器   
    private LinearLayout mLayout;  
    private int X = 10;// X数据集大小  
    private int Y = 200;// 
    
    
    private Handler mHandler; 
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.data_chart_layout, container, false);
		 mLayout = (LinearLayout) root.findViewById(R.id.chart);// 这里获得xy_chart的布局，下面会把图表画在这个布局里面  
		return root; 
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		 //draw chart
	   
		series_high =  new TimeSeries("收缩压");// 这个类用来放置曲线上的所有点，是一个点的集合，根据这些点画出曲线  
		series_low =  new TimeSeries("舒张压");
        mDataset = new XYMultipleSeriesDataset(); // 创建一个数据集的实例，这个数据集将被用来创建图表  
        mDataset.addSeries(series_high);// 将点集添加到这个数据集中  
       // mDataset.addSeries(1,series_low);// 将点集添加到这个数据集中
        int color = Color.RED;// 设置颜色  
        PointStyle style = PointStyle.CIRCLE;// 设置外观周期性显示  
        mXYRenderer = buildRenderer(color, style, true);  
        mXYRenderer.setShowGrid(true);// 显示表格  
        mXYRenderer.setGridColor(Color.GREEN);// 据说绿色代表健康色调，不过我比较喜欢灰色  
        mXYRenderer.setXLabels(20);  
        mXYRenderer.setYLabels(10);  
        mXYRenderer.setYLabelsAlign(Align.RIGHT);// 右对齐  
        mXYRenderer.setAxisTitleTextSize(19);  
        mXYRenderer.setXLabelsAlign(Align.CENTER);
        mXYRenderer.setXLabelsPadding(10);
        mXYRenderer.setXLabelsAngle(-30.0f);
        mXYRenderer.setChartTitleTextSize(23);  
        mXYRenderer.setLabelsTextSize(18);  
        mXYRenderer.setLegendTextSize(18);   
        //in this order: top, left, bottom, right  
        mXYRenderer.setMargins(new int[] {40, 70, 55, 0}); 
        // mXYRenderer.setPointSize((float) 2);  
        mXYRenderer.setShowLegend(true);// 不显示图例  
        mXYRenderer.setZoomEnabled(false);  
        mXYRenderer.setPanEnabled(true, false);  
        mXYRenderer.setClickEnabled(false);  
        
        setChartSettings(mXYRenderer, title, "测量时间", "血压值", 0, X, 0, Y,  
                Color.WHITE, Color.WHITE);// 这个是采用官方APIdemo提供给的方法  
                                            // 设置好图表的样式  

        mHandler = new Handler() {
	        public void handleMessage (Message msg) {//此方法在ui线程运行
	            switch(msg.what) {
	            case -1:
	                
	                Toast.makeText(getActivity(), getActivity().getString(R.string.network_error), Toast.LENGTH_LONG).show();
	                break;

	            default:
	            	
	                HashMap<String, String> item = new HashMap<String, String>(); 
	                ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
	                item.put("update_datetime","测量时间");
                    item.put("high_value","收缩压");
                    item.put("low_value","舒张压");
	                data.add(item);
	                try { 

	                    JSONArray jsonObjs = new JSONObject(msg.getData().getString("blood")).getJSONArray("dataList"); 

	                    for(int i = 0; i < jsonObjs.length() ; i++){ 
	                    	HashMap<String,String> map=new HashMap<String,String>();
	                        JSONObject jsonObj = jsonObjs.getJSONObject(i); 
	                        series_high.add(StrToDate(jsonObj.getString("update_datetime")),Double.parseDouble(jsonObj.getString("high_value")));
	                        series_high.add(StrToDate(jsonObj.getString("update_datetime")),Double.parseDouble(jsonObj.getString("low_value")));
	                    } 

	                } catch (JSONException e) { 
	                    System.out.println("Jsons parse error !"); 
	                    e.printStackTrace(); 
	                } 
	                String[] types = new String[] { LineChart.TYPE,LineChart.TYPE};
	                mViewChart = ChartFactory.getTimeChartView(getActivity(), mDataset, mXYRenderer,  "yy/MM/dd HH:mm");// 通过ChartFactory生成图表  
	                mViewChart.setBackgroundColor(0xff777777);
	                mLayout.addView(mViewChart, new LayoutParams(LayoutParams.MATCH_PARENT,  
	                        LayoutParams.MATCH_PARENT));// 将图表添加到布局中去  
	                
	                break;
	            }
	        }
        };
        HandlerThreads.GetblooddataThread thread = new HandlerThreads.GetblooddataThread(mHandler,12,"fdf");
	    thread.start();
	}
	private void updateChart(String date, double y) {// 主要工作是刷新整个统计图  
	   
            mDataset.removeSeries(series_high);// 移除数据集中旧的点集  
            series_high.clear();// 点集先清空，为了做成新的点集而准备  
            series_high.add(StrToDate(date), y);
      
            
            mDataset.addSeries(series_high);// 在数据集中添加新的点集  
          //  mViewChart.invalidate();// 视图更新，没有这一步，曲线不会呈现动态   先写数据, 再更新, 不用这个.
            
	     }  
	
	 protected XYMultipleSeriesRenderer buildRenderer(int color,  
	            PointStyle style, boolean fill) {// 设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等  
	        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();  
	        XYSeriesRenderer r = new XYSeriesRenderer();  
	        r.setColor(color);  
	        r.setPointStyle(style);  
	        r.setFillPoints(fill);  
	        r.setLineWidth(3);  
	        renderer.addSeriesRenderer(r);  
	  
	        return renderer;  
	    }  
	  
	    protected void setChartSettings(XYMultipleSeriesRenderer renderer,  
	            String title, String xTitle, String yTitle, double xMin,  
	            double xMax, double yMin, double yMax, int axesColor,  
	            int labelsColor) {// 设置主描绘器的各项属性，详情可阅读官方API文档  
	        renderer.setChartTitle(title);  
	        renderer.setXTitle(xTitle);  
	        renderer.setYTitle(yTitle);  
	     //   renderer.setXAxisMin(xMin);  
	      //  renderer.setXAxisMax(xMax);  
	        renderer.setYAxisMin(yMin);  
	        renderer.setYAxisMax(yMax);  
	        renderer.setAxesColor(axesColor);  
	        renderer.setLabelsColor(labelsColor);  
	        
	    }  
	    
	    /**
	    * 字符串转换成日期
	    * @param str
	    * @return date
	    */
	    public static Date StrToDate(String str) {
	      
	       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	       Date date = null;
	       try {
	        date = format.parse(str);
	       } catch (ParseException e) {
	        e.printStackTrace();
	       }
	       return date;
	    }
}
