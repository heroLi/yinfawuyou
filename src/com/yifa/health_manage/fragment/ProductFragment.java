package com.yifa.health_manage.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.yifa.health_manage.R;

/**
 * 产品
 * **/
public class ProductFragment extends Fragment {

	private WebView webView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_product, container,
				false);
		initWeb(view);
		return view;
	}

	private void initWeb(View view) {
		webView = (WebView) view.findViewById(R.id.myWeb);
		webView.loadUrl("http://121.40.172.222/health/ad.php");
	}
}
