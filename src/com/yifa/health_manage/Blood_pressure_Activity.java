package com.yifa.health_manage;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.yifa.health_manage.fragment.AddressFragment;
import com.yifa.health_manage.fragment.FindFragment;
import com.yifa.health_manage.fragment.MeFragment;
import com.yifa.health_manage.fragment.WeiXinFragment;
import com.yifa.health_manage.service.HandlerThreads;

public class Blood_pressure_Activity extends FragmentActivity   {

	private Handler mHandler; 
	private static android.support.v4.app.FragmentManager fMgr;  
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blood_pressure_layout);
		
		//获取FragmentManager实例
				fMgr = getSupportFragmentManager();
				initFragment();
				dealBottomButtonsClickEvent();

		 
		 
		 
		mHandler = new Handler() {
	        public void handleMessage (Message msg) {//此方法在ui线程运行
	            switch(msg.what) {
	            case -1:
	                
	                Toast.makeText(getApplication(), getApplication().getString(R.string.network_error), Toast.LENGTH_LONG).show();
	                break;

	            default:
	                ListView listView = (ListView) findViewById(R.id.listView);
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
	                        map.put("update_datetime",jsonObj.getString("update_datetime"));
	                        map.put("high_value",jsonObj.getString("high_value"));
	                        map.put("low_value",jsonObj.getString("low_value"));
	                        data.add(map);
	                    } 

	                } catch (JSONException e) { 
	                    System.out.println("Jsons parse error !"); 
	                    e.printStackTrace(); 
	                } 

	                SimpleAdapter adapter = new SimpleAdapter
	                		(Blood_pressure_Activity.this,
	                		data,
	                		R.layout.item,
	                		new String[]{"update_datetime", "high_value", "low_value"},
	                		new int[]{R.id.blood_pressure_date, R.id.blood_pressure_high, R.id.blood_pressure_low});
	                
	               listView.setAdapter(adapter); 
	                
	                break;
	            }
	        }
	    };
	    
	    HandlerThreads.GetblooddataThread thread = new HandlerThreads.GetblooddataThread(mHandler,12,"fdf");
	    thread.start();
	    
	   
	    
	}
	
	/**
	 * 初始化首个Fragment
	 */
	private void initFragment() {
		android.support.v4.app.FragmentTransaction ft = fMgr.beginTransaction();
		WeiXinFragment weiXinFragment = new WeiXinFragment();
		ft.add(R.id.fragmentRoot, weiXinFragment, "weiXinFragment");
		ft.addToBackStack("weiXinFragment");
		ft.commit();		
	}
	/**
	 * 处理底部点击事件
	 */
	private void dealBottomButtonsClickEvent() { 
		 findViewById(R.id.rbWeiXin).setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(fMgr.findFragmentByTag("weiXinFragment")!=null && fMgr.findFragmentByTag("weiXinFragment").isVisible()) {
					return;
				}
				popAllFragmentsExceptTheBottomOne();

			}
		});
		findViewById(R.id.rbAddress).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popAllFragmentsExceptTheBottomOne();
				android.support.v4.app.FragmentTransaction ft = fMgr.beginTransaction();
				ft.hide(fMgr.findFragmentByTag("weiXinFragment"));
				AddressFragment sf = new AddressFragment();
				ft.add(R.id.fragmentRoot, sf, "AddressFragment");
				ft.addToBackStack("AddressFragment");
				ft.commit();
				
			}
		});
		findViewById(R.id.rbFind).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popAllFragmentsExceptTheBottomOne();
				android.support.v4.app.FragmentTransaction ft = fMgr.beginTransaction();
				ft.hide(fMgr.findFragmentByTag("weiXinFragment"));
				FindFragment sf = new FindFragment();
				ft.add(R.id.fragmentRoot, sf, "AddressFragment");
				ft.addToBackStack("FindFragment");
				ft.commit();
			}
		});
		findViewById(R.id.rbMe).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popAllFragmentsExceptTheBottomOne();
				android.support.v4.app.FragmentTransaction ft = fMgr.beginTransaction();
				ft.hide(fMgr.findFragmentByTag("weiXinFragment"));
				MeFragment sf = new MeFragment();
				ft.add(R.id.fragmentRoot, sf, "MeFragment");
				ft.addToBackStack("MeFragment");
				ft.commit();
			}
		});
	}
	
	/**
	 * 从back stack弹出所有的fragment，保留首页的那个
	 */
	public static void popAllFragmentsExceptTheBottomOne() {
		for (int i = 0, count = fMgr.getBackStackEntryCount() - 1; i < count; i++) {
			fMgr.popBackStack();
		}
	}
	//点击返回按钮
	@Override
	public void onBackPressed() {
		if(fMgr.findFragmentByTag("weiXinFragment")!=null && fMgr.findFragmentByTag("weiXinFragment").isVisible()) {
			this.finish();
		} else {
			super.onBackPressed();
		}
	}

	
	  
    
}
