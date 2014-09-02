package com.yifa.health_manage.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * ��װ�鿴�������͵Ĺ�����
 * 
 * @author
 * 
 */
public class NetworkUtil {

	/** û�п������� */
	public static final int NO_NET_CONNECT = -1;

	/** wap������� */
	public static final int WAP_CONNECTED = 0;

	/** net�� ���� */
	public static final int NET_CONNECTED = 1;

	/** wifi������� */
	public static final int WIFI_CONNECT = 2;

	private static ConnectivityManager mConnMgr = null;

	private static WifiManager wifiManager = null;

	/**
	 * �鿴������������
	 * 
	 * @return int
	 */
	public static int getNetworkType(Context context) {

		mConnMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		int wifiState = wifiManager.getWifiState();
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (wifiInfo.getNetworkId() != -1
				&& (wifiState == WifiManager.WIFI_STATE_ENABLED)) {
			return WIFI_CONNECT;
		} else {
			NetworkInfo netInfo = mConnMgr
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (netInfo == null || !netInfo.isConnected()
					|| !netInfo.isAvailable()) {
				return NO_NET_CONNECT;
			} else {
				String name = netInfo.getExtraInfo();
				if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE
						&& name.toLowerCase().equals("cmwap")) {
					return WAP_CONNECTED;
				} else {
					return NET_CONNECTED;
				}
			}
		}
	}
}
