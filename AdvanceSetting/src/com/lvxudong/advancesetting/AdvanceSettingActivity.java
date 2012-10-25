package com.lvxudong.advancesetting;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.util.Log;

public class AdvanceSettingActivity extends PreferenceActivity implements
		OnPreferenceChangeListener, OnPreferenceClickListener {

	private SwitchPreference diagSwitch;
	private CheckBoxPreference uploadfix;
	private Process process = null;
	private DataOutputStream execout = null;
	private BufferedReader execin = null;
	private InputStreamReader exeerrin = null;
	private Boolean isRoot = false;
	private String TAG = "lvxudong";
	private Preference repair3G = null;
	private Preference about = null;
	private boolean execerrok = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.advancesetting);
		diagSwitch = (SwitchPreference) findPreference("diagSwitch");
		diagSwitch.setOnPreferenceChangeListener(this);
		uploadfix = (CheckBoxPreference) findPreference("uploadfix");
		uploadfix.setOnPreferenceChangeListener(this);
		uploadfix.setChecked(isWorked());
		repair3G = (Preference) findPreference("repair3g");
		repair3G.setOnPreferenceClickListener(this);
		about = (Preference) findPreference("about");
		about.setOnPreferenceClickListener(this);
		openProcessStream();
		diagSwitch.setChecked(readDiagMode());
	}

	private boolean checkbusybox() {
		try {
			execout.writeBytes("busybox\n");
			execerrok = exeerrin.ready();
		} catch (IOException e) {
			return false;
		}
		return !execerrok;

	}

	private void openProcessStream() {
		try {
			process = Runtime.getRuntime().exec("su");
			execin = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			execout = new DataOutputStream(process.getOutputStream());
			exeerrin = new InputStreamReader(process.getErrorStream());
		} catch (Exception e) {
			e.printStackTrace();
			isRoot = false;
		}
		if (process != null && execin != null && execout != null)
			isRoot = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		diagSwitch.setChecked(readDiagMode());
		uploadfix.setChecked(isWorked());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		process.destroy();
		try {
			execin.close();
			execout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		if (preference.getKey().equals("diagSwitch")) {
			changDiagMode();
			return true;
		}
		if (preference.getKey().equals("uploadfix")) {
			Intent intent = new Intent();
			intent.setClass(this, UploadFixService.class);
			if (!isWorked()) {
				startService(intent);
			} else
				stopService(intent);
			uploadfix.setChecked(isWorked());
			return true;
		}
		return false;
	}

	public boolean onPreferenceClick(Preference preference) {
		if (preference.getKey().equals("repair3g")) {
			if (repair3g())
				new AlertDialog.Builder(this)
						.setTitle("提示")
						.setMessage("\t\t修复成功,需要重启才能生效！")
						.setPositiveButton("现在重启", new OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									execout.writeBytes("reboot\n");
									dialog.dismiss();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						})
						.setNegativeButton("稍后手动重启", new OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						})
						.setIcon(
								getResources().getDrawable(
										android.R.drawable.ic_dialog_alert))
						.create().show();
			return true;
		}
		if (preference.getKey().equals("about")) {

			new AlertDialog.Builder(this)
					.setTitle(R.string.about)
					.setMessage(
							"Galaxy Nexus CDMA 工具箱 V "
									+ getResources()
											.getString(R.string.version)
											.toString()
									+ " \n\t\t By lvxudong@diypda.com")
					.setPositiveButton("确定", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.setIcon(
							getResources().getDrawable(
									android.R.drawable.ic_dialog_info))
					.create().show();

			return true;
		}
		return false;
	}

	private boolean readDiagMode() {
		try {
			execout.writeBytes("cat /sys/devices/tuna_otg/usb_sel" + "\n");
			execout.flush();
			String str = execin.readLine();
			if (str.equals("MODEM"))
				return true;
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "Read Diag Mode Fail");
		}
		return false;
	}

	private void changDiagMode() {

		try {
			if (readDiagMode()) {
				diagSwitch.setEnabled(false);
				execout.writeBytes("echo 0 > /sys/class/android_usb/android0/enable"
						+ "\n");
				execout.flush();
				execout.writeBytes("echo PDA > /sys/devices/tuna_otg/usb_sel"
						+ "\n");
				execout.flush();
				execout.writeBytes("echo 1 > /sys/class/android_usb/android0/enable"
						+ "\n");
				execout.flush();
			} else {
				diagSwitch.setEnabled(false);
				execout.writeBytes("echo 0 > /sys/class/android_usb/android0/enable"
						+ "\n");
				execout.flush();
				execout.writeBytes("echo MODEM > /sys/devices/tuna_otg/usb_sel"
						+ "\n");
				execout.flush();
				execout.writeBytes("echo 1 > /sys/class/android_usb/android0/enable"
						+ "\n");
				execout.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		diagSwitch.setChecked(readDiagMode());
		diagSwitch.setEnabled(true);
	}

	private boolean repair3g() {

		if (checkbusybox())
			new AlertDialog.Builder(this)
					.setTitle(R.string.about)
					.setMessage(
							"Galaxy Nexus CDMA 工具箱 V "
									+ getResources()
											.getString(R.string.version)
											.toString()
									+ " \n\t\t By lvxudong@diypda.com")
					.setPositiveButton("确定", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.setIcon(
							getResources().getDrawable(
									android.R.drawable.ic_dialog_info))
					.create().show();

		InputStream in = null;
		FileUtil fileUtils = new FileUtil();
		try {
			in = getAssets().open("metadata_3");
			fileUtils.write2SD("lvxudong", "metadata_3", in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		try {
			execout.writeBytes("busybox mount -o remount /system -rw" + "\n");
			execout.writeBytes("busybox chmod 777 /system/vendor/lib/libsec-ril_lte.so"
					+ "\n");
			execout.writeBytes("busybox cp -f /sdcard/lvxudong/metadata_3 /system/vendor/lib/libsec-ril_lte.so"
					+ "\n");
			execout.writeBytes("busybox chmod 0644 /system/vendor/lib/libsec-ril_lte.so"
					+ "\n");
			execout.writeBytes("busybox mount -o remount /system -r" + "\n");
			execout.writeBytes("busybox rm -rf /sdcard/lvxudong" + "\n");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean isWorked() {
		ActivityManager myManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List <RunningServiceInfo> runningService =  myManager.getRunningServices(70);
		for (RunningServiceInfo info : runningService) {
			if (info.service.getClassName().toString()
					.equals("com.lvxudong.advancesetting.UploadFixService")) {
				return true;
			}
		}
		return false;
	}

}