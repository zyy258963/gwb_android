package com.gwb.activity;

import java.util.HashMap;
import java.util.Map;

import com.artifex.mupdfdemo.R;
import com.gwb.activity.pojo.HeaderVo;
import com.gwb.activity.pojo.Users;
import com.gwb.utils.ConstantParams;
import com.gwb.utils.FastjsonTools;
import com.gwb.utils.HttpHelper;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class MainActivity extends BaseActivity {

	private ConnectivityManager connManager = null;
	private int isSucc = 0;

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
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(3000);
		view.startAnimation(aa);
		// 给动画添加监听方法
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				Log.i("PDF", " isSuccess： "+isSucc);
				attemptLogin();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {
				if (info_wifi.isConnected()) {
//					attemptLogin();
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
									attemptLogin();
								}
							}).setNegativeButton("退出", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									MainActivity.this.finish();
								}
							}).create();
					dialog.show();
				}else {
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

		});

	}

	protected void attemptLogin() {
		// TODO Auto-generated method stub
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				ConstantParams.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		String telephone = sp.getString(ConstantParams.FIELD_TELEPHONE, "");
		String macAddress = sp.getString(ConstantParams.FIELD_MAC_ADDRESS, "");
		new UserLoginTask().execute(telephone, macAddress);
	}

	/**
	 * 跳转到主角面的方法
	 */
	private void redirectToLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 跳转到主角面的方法
	 */
	private void redirectToMenu() {
		Intent intent = new Intent(this, MenuActivity.class);
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

	@SuppressLint("NewApi")
	public class UserLoginTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			// TODO: attempt authentication against a network service.
			Map<String, String> postParam = new HashMap<String, String>();
			postParam.put("type", "appLogin");
			postParam.put("telephone", params[0]);
			postParam.put("macAddress", params[1]);
			String jsonStr = HttpHelper.sendPostMessage(
					ConstantParams.URL_LOGIN, postParam, "utf-8");
			if (jsonStr != null && !"".equals(jsonStr)) {
				// 此处先判断 heeader 中的 code是否正确
				HeaderVo headerVo = FastjsonTools.getHeader(jsonStr);
				if (headerVo != null && "1".equals(headerVo.getCode())) {
					Users user = FastjsonTools.getContentPojo(jsonStr,
							Users.class);
					if (user != null && !"".equals(user)) {
						Log.i("LoginActivity", "login::  1 ");
						System.out.println("userId :::" + user.getUserId());
						ConstantParams.CURRENT_USER_ID = user.getUserId();
						ConstantParams.CURRENT_USER_NAME = user.getUserName();
						ConstantParams.CURRENT_MACADDRESS = user
								.getMacAddress();
						ConstantParams.CURRENT_TELEPHONE = user.getTelephone();
						return "success";
					} else {
						Log.i("LoginActivity", "login::  2 ");
						return "fail";
					}
				} else {
					return "fail";
				}
			} else {
				return "fail";
			}

		}

		@Override
		protected void onPostExecute(final String result) {
			Log.i("LoginActivity", "login::" + result);
			if ("success".equals(result)) {
				redirectToMenu();
			} else if ("fail".equals(result)) {
				redirectToLogin();
			}
			Log.i("PDF", " isSuccess： "+isSucc);
		}

	}

}
