package com.lvxudong.advancesetting;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

public class OperateInfoActivity extends Activity {

	private TextView info;
	private String callState;
	private String DataActivity;
	private String DataState;
	private String NetworkType;
	private String PhoneType;
	private String SimState;
	private PhoneStateListener listener;
	TelephonyManager tm;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		info = (TextView) findViewById(R.id.textInfo);
		listener = new PhoneStateListener() {
			@Override
			public void onServiceStateChanged(ServiceState serviceState) {
				// TODO Auto-generated method stub
				super.onServiceStateChanged(serviceState);
				reflash();
			
			}

			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				// TODO Auto-generated method stub
				super.onCallStateChanged(state, incomingNumber);
				reflash();
			}

			@Override
			public void onDataConnectionStateChanged(int state) {
				// TODO Auto-generated method stub
				super.onDataConnectionStateChanged(state);
				reflash();
			}
		};
		tm.listen(listener, PhoneStateListener.LISTEN_SERVICE_STATE
				| PhoneStateListener.LISTEN_CALL_STATE
				| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
		reflash();
	}

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		tm.listen(listener, PhoneStateListener.LISTEN_SERVICE_STATE
				| PhoneStateListener.LISTEN_CALL_STATE
				| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
	}


	public void reflash() {
		switch (tm.getCallState()) {
		case TelephonyManager.CALL_STATE_IDLE:
			callState = "空闲";
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			callState = "摘机";
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			callState = "来电";
			break;
		}

		switch (tm.getDataState()) {
		case TelephonyManager.DATA_CONNECTED:
			DataState = "已连接";
			break;
		case TelephonyManager.DATA_CONNECTING:
			DataState = "正在连接";
			break;
		case TelephonyManager.DATA_DISCONNECTED:
			DataState = "已断开连接";
			break;
		case TelephonyManager.DATA_SUSPENDED:
			DataState = "挂起";
			break;
		default:
			break;
		}

		switch (tm.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			NetworkType = "1xRTT";
			break;
		case TelephonyManager.NETWORK_TYPE_CDMA:
			NetworkType = "CDMA";
			break;
		case TelephonyManager.NETWORK_TYPE_EDGE:
			NetworkType = "EDGE";
			break;
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			NetworkType = "EHRPD";
			break;
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			NetworkType = "EVDO_0";
			break;
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			NetworkType = "EVDO_A";
			break;
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			NetworkType = "EVDO_B";
			break;
		case TelephonyManager.NETWORK_TYPE_GPRS:
			NetworkType = "GPRS";
			break;
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			NetworkType = "HSDPA";
			break;
		case TelephonyManager.NETWORK_TYPE_HSPA:
			NetworkType = "HSPA";
			break;
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			NetworkType = "HSPAP";
			break;
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			NetworkType = "HSUPA";
			break;
		case TelephonyManager.NETWORK_TYPE_IDEN:
			NetworkType = "IDEN";
			break;
		case TelephonyManager.NETWORK_TYPE_LTE:
			NetworkType = "LTE";
			break;
		case TelephonyManager.NETWORK_TYPE_UMTS:
			NetworkType = "UMTS";
			break;
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			NetworkType = "未知";
			break;
		}

		switch (tm.getPhoneType()) {
		case TelephonyManager.PHONE_TYPE_CDMA:
			PhoneType = "CDMA";
			break;
		case TelephonyManager.PHONE_TYPE_GSM:
			PhoneType = "GSM";
			break;
		case TelephonyManager.PHONE_TYPE_NONE:
			PhoneType = "NONE";
			break;
		case TelephonyManager.PHONE_TYPE_SIP:
			PhoneType = "SIP";
			break;
		}

		switch (tm.getSimState()) {
		case TelephonyManager.SIM_STATE_ABSENT:
			SimState = "ABSENT";
			break;
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			SimState = "NETWORK_LOCKED";
			break;
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			SimState = "PIN_REQUIRED";
			break;
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			SimState = "PUK_REQUIRED";
			break;
		case TelephonyManager.SIM_STATE_READY:
			SimState = "READY";
			break;
		case TelephonyManager.SIM_STATE_UNKNOWN:
			SimState = "UNKNOWN";
			break;
		}

		String str = "电话状态: " + callState + "\n" 
				+ "数据状态: " + DataState + "\n"
				+ "MEID/ESN: " + tm.getDeviceId() + "\n"
				+ "电话号码: " + tm.getLine1Number() + "\n"
				+ "当前网络国家: " + tm.getNetworkCountryIso() + "\n"
				+ "当前运营商代码: " + tm.getNetworkOperator() + "\n"
				+ "当前运营商名称: " + tm.getNetworkOperatorName() + "\n"
				+ "网络类型: " + NetworkType + "\n" 
				+ "手机类型: "+ PhoneType + "\n" 
				+ "SIM国家: " + tm.getSimCountryIso()+ "\n" 
				+ "SIM运营商代码: " + tm.getSimOperator() + "\n"
				+ "SIM运营商名称: " + tm.getSimOperatorName() + "\n"
				+ "ICCID: " + tm.getSimSerialNumber() + "\n" 
				+ "SIM状态: " + SimState + "\n" 
				+ "IMSI: " + tm.getSubscriberId() + "\n"
				+ "是否有SIM卡: " + tm.hasIccCard() + "\n"
				+ "漫游: " + tm.isNetworkRoaming();
		info.setText(str);
	}

	class myBroadcastReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "rec", Toast.LENGTH_LONG);
		}

	}
}