package com.gwb.activity;


import java.net.URLEncoder;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.artifex.mupdfdemo.R;
import com.gwb.activity.pojo.Books;
import com.gwb.utils.ApplicationManager;
import com.gwb.utils.ConstantParams;
import com.gwb.utils.DensityUtil;
import com.gwb.utils.FastjsonTools;
import com.gwb.utils.HttpHelper;
import com.umeng.analytics.MobclickAgent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {

	DisplayMetrics dm = new DisplayMetrics();
	public float textScale = 1.0f;
	public ProgressDialog turnDialog = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		ApplicationManager.add(BaseActivity.this);
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		textScale = getResources().getConfiguration().fontScale;
		initSize();
		turnDialog = new ProgressDialog(this);
	}

	private void initSize() {
		// densityUtil = new DensityUtil(getApplicationContext());
		ConstantParams.SIZE_ROW = getScreenHeight() / 10;
		ConstantParams.SIZE_BUTTON_WIDTH = getScreenWidth() / 2;
		Log.i("BASE", ConstantParams.SIZE_ROW+"-----getScreenHeight():" + getScreenHeight() +"---" +getScreenWidth());
		if (getScreenHeight() <= 800) { 
			ConstantParams.SIZE_TOP_TEXT = ConstantParams.SIZE_ROW / 4.0f/ textScale;
			ConstantParams.SIZE_MAIN_TEXT = ConstantParams.SIZE_ROW / 3.0f/ textScale;
		}else if (getScreenHeight() <= 1280) {
			ConstantParams.SIZE_TOP_TEXT = ConstantParams.SIZE_ROW / 5.0f / textScale;
			ConstantParams.SIZE_MAIN_TEXT = ConstantParams.SIZE_ROW / 4.0f / textScale;
			Log.i("BASE", "SIZE_TOP_TEXT:"+	ConstantParams.SIZE_TOP_TEXT+ " ----SIZE_MAIN_TEXT:" + ConstantParams.SIZE_MAIN_TEXT);
		}else if (getScreenHeight() <= 1920) {
			ConstantParams.SIZE_TOP_TEXT = ConstantParams.SIZE_ROW / 9.0f / textScale;
			ConstantParams.SIZE_MAIN_TEXT = ConstantParams.SIZE_ROW / 6.0f  / textScale;
			Log.i("BASE", "SIZE_TOP_TEXT:"+	ConstantParams.SIZE_TOP_TEXT+ " ----SIZE_MAIN_TEXT:" + ConstantParams.SIZE_MAIN_TEXT);
		}else {
			ConstantParams.SIZE_TOP_TEXT = ConstantParams.SIZE_ROW / 12.0f/ textScale;
			ConstantParams.SIZE_MAIN_TEXT = ConstantParams.SIZE_ROW / 8.0f/ textScale;
		}
		
		ConstantParams.SCREEN_WIDTH = getScreenWidth();
		ConstantParams.SCREEN_HEIGHT = getScreenHeight();

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.base, menu);
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "首页");
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "我的常用文档");
		menu.add(Menu.NONE, Menu.FIRST + 3, 3, "退出");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			Intent intent = new Intent();
			intent.setClass(this, MenuActivity.class);
			startActivity(intent);
			break;
		case Menu.FIRST + 2:
			Intent intent2 = new Intent();
			intent2.setClass(this, FavoriteActivity.class);
			startActivity(intent2);
			break;
		case Menu.FIRST + 3:
			showExitDialog();
			break;

		default:
			break;
		}
		return false;
	}

	public static boolean checkNetWorkStatus(Context context) {
		boolean result;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo != null && netinfo.isConnected()) {
			result = true;
			Log.i("NetStatus", "The net was connected");
		} else {
			result = false;
			Log.i("NetStatus", "The net was bad!");
		}
		return result;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ApplicationManager.remove(BaseActivity.this);
	}

	public int getScreenWidth() {
		return dm.widthPixels;
	}

	public int getScreenHeight() {
		return dm.heightPixels;
	}

	public void showExitDialog() {
		// AlertDialog dialog = new AlertDialog.Builder(this)
		// .setMessage("确定要退出程序？")
		// .setPositiveButton("确定",
		// new android.content.DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// // finish();
		// // System.exit(0);
		// // ApplicationManager.finishProgram();
		// }
		// })
		// .setNegativeButton("取消",
		// new android.content.DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// // TODO Auto-generated method stub
		// dialog.dismiss();
		// }
		//
		// }).create();
		// dialog.show();
	}

	private void showMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void showPdf(String path) {		
		Uri uri = Uri.parse(path);
		Intent intent = new Intent(this, MuPDFActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(uri);
		
		new logAsyn().execute();
		
		startActivity(intent);
	}
	
	@SuppressLint("NewApi") 
	private class logAsyn extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			String jsonString = null;
			try {
			String url = ConstantParams.URL_LOG_OPEN_DOC + "&"
					+ ConstantParams.FIELD_TELEPHONE + "="
					+ ConstantParams.CURRENT_TELEPHONE + "&"
					+ ConstantParams.FIELD_MAC_ADDRESS + "="
					+ ConstantParams.CURRENT_MACADDRESS + "&"
					+ ConstantParams.FIELD_KEYWORDS +"="
					+ URLEncoder.encode(
					URLEncoder.encode(ConstantParams.CURRENT_BOOK_NAME, "UTF-8"), "UTF-8");
			
				jsonString = HttpHelper.sendGetMessage(url, "utf-8");
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
		
	}
	
}
