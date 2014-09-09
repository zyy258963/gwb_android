package com.gwb.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.artifex.mupdfdemo.R;
import com.umeng.analytics.MobclickAgent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class MainActivity extends BaseActivity {

	private ConnectivityManager connManager = null;
	// private UpdateManager updateManager = null;

	public ProgressDialog pBar;
	private Handler handler = new Handler();

	private int newVerCode = 0;
	private String newVerName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.activity_main, null);
		setContentView(view);
		
		// 获得手机联网信息，是3g还是wifi
		connManager = (ConnectivityManager) this
				.getSystemService(this.CONNECTIVITY_SERVICE);
		final NetworkInfo info_3g = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		final NetworkInfo info_wifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		// 渐变展示启动屏,这里通过动画来设置了开启应用程序的界面
		AlphaAnimation aa = new AlphaAnimation(1.0f, 1.0f);
		aa.setDuration(3000);
		view.startAnimation(aa);
		// 给动画添加监听方法
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				if (info_wifi.isConnected()) {
					redirectToLogin();
				} else if (info_3g.isConnected()) {
					// 提示当前使用的是3g 将产生较多流量
					AlertDialog dialog = new AlertDialog.Builder(
							MainActivity.this).setTitle("提示")
							.setMessage("当前使用的是3G网络，是否继续？")
							.setPositiveButton("继续", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									redirectToLogin();
								}
							}).setNegativeButton("退出", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									MainActivity.this.finish();
								}
							}).create();
					dialog.setCancelable(false);
					dialog.show();
				} else {
					// 提示当前使用的是3g 将产生较多流量
					AlertDialog dialog = new AlertDialog.Builder(
							MainActivity.this).setTitle("提示")
							.setMessage("当前未联网，请联网操作！")
							.setPositiveButton("退出", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									MainActivity.this.finish();
								}
							}).create();
					dialog.show();
				}

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(final Animation animation) {
				// 检查本地是否存有sharedpreference 的数据，如果有
			}
		});

	}

	/**
	 * 跳转到主角面的方法
	 */
	private void redirectToLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		MobclickAgent.updateOnlineConfig(getApplicationContext());
	}

	@SuppressLint("NewApi")
	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}