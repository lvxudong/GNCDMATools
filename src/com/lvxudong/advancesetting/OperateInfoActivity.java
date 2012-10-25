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
			callState = "����";
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			callState = "ժ��";
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			callState = "����";
			break;
		}

		switch (tm.getDataState()) {
		case TelephonyManager.DATA_CONNECTED:
			DataState = "������";
			break;
		case TelephonyManager.DATA_CONNECTING:
			DataState = "��������";
			break;
		case TelephonyManager.DATA_DISCONNECTED:
			DataState = "�ѶϿ�����";
			break;
		case TelephonyManager.DATA_SUSPENDED:
			DataState = "����";
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
			NetworkType = "δ֪";
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

		String str = "�绰״̬: " + callState + "\n" 
				+ "����״̬: " + DataState + "\n"
				+ "MEID/ESN: " + tm.getDeviceId() + "\n"
				+ "�绰����: " + tm.getLine1Number() + "\n"
				+ "��ǰ�������: " + tm.getNetworkCountryIso() + "\n"
				+ "��ǰ��Ӫ�̴���: " + tm.getNetworkOperator() + "\n"
				+ "��ǰ��Ӫ������: " + tm.getNetworkOperatorName() + "\n"
				+ "��������: " + NetworkType + "\n" 
				+ "�ֻ�����: "+ PhoneType + "\n" 
				+ "SIM����: " + tm.getSimCountryIso()+ "\n" 
				+ "SIM��Ӫ�̴���: " + tm.getSimOperator() + "\n"
				+ "SIM��Ӫ������: " + tm.getSimOperatorName() + "\n"
				+ "ICCID: " + tm.getSimSerialNumber() + "\n" 
				+ "SIM״̬: " + SimState + "\n" 
				+ "IMSI: " + tm.getSubscriberId() + "\n"
				+ "�Ƿ���SIM��: " + tm.hasIccCard() + "\n"
				+ "����: " + tm.isNetworkRoaming();
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