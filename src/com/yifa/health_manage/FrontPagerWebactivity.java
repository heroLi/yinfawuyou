package com.yifa.health_manage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FrontPagerWebactivity extends Activity {

	private WebView webView;

	private LinearLayout noNet;

	private Button refreshData;

	private RelativeLayout bar;

	private TextView title;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_product);
//		title = (TextView) findViewById(R.id.activity_top_title);
		webView = (WebView) findViewById(R.id.myWeb);
//		title.setText("活动页面");


		initWeb();

		String url = getIntent().getStringExtra("url");
		webView.loadUrl(url);
//		title.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
	}

	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	private void initWeb() {

		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDisplayZoomControls(false);
//		webView.getSettings().setAllowFileAccessFromFileURLs(true);
		webView.getSettings().setDatabaseEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.setDownloadListener(new MyWebViewDownLoadListener());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	private class MyWebViewDownLoadListener implements DownloadListener {  
		  
        @Override  
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,  
                                    long contentLength) {  
            Uri uri = Uri.parse(url);  
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
            startActivity(intent);  
        }  
  
    }  
	@Override
	protected void onResume() {
		super.onResume();
		webView.requestFocus();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private boolean isError = false;

}
