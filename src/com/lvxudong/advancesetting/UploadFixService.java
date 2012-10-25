package com.lvxudong.advancesetting;

import java.io.DataOutputStream;
import java.io.IOException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

public class UploadFixService extends Service {

	private BroadcastReceiver receiver = null;
	private ConnectivityManager connectivityManager = null;
	private NetworkInfo networkInfo = null;
	Process process = null;
	DataOutputStream out = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		try {
			process = Runtime.getRuntime().exec("su");
			out = new DataOutputStream(process.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				networkInfo = connectivityManager.getActiveNetworkInfo();
				if (networkInfo != null && networkInfo.isConnected() && networkInfo.getTypeName().equals("mobile"))
					try {
						out.writeBytes("echo 1444 > /sys/class/net/cdma_rmnet4/mtu" + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		};
		registerReceiver(receiver, filter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		process.destroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
