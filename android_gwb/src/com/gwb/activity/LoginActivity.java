package com.gwb.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.artifex.mupdfdemo.R;
import com.gwb.activity.pojo.HeaderVo;
import com.gwb.activity.pojo.Users;
import com.gwb.utils.ApplicationManager;
import com.gwb.utils.DensityUtil;
import com.gwb.utils.FastjsonTools;
import com.gwb.utils.HttpHelper;
import com.gwb.utils.ConstantParams;

import android.R.integer;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends BaseActivity {
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;
	private TelephonyManager phoneMgr = null;

	// Values for email and password at the time of the login attempt.
	private String mUsername;
	// private String mPassword;
	private String macAddress;

	// UI references.
	private EditText mUsernameView;
	// private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		ApplicationManager.add(LoginActivity.this);

		// 获得手机管理的manager
		phoneMgr = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);

		macAddress = phoneMgr.getDeviceId();
		
		
		// Set up the login form.
		mUsernameView = (EditText) findViewById(R.id.username);
		/*
		 * 
		 * 如果有SIM卡的信息，那么直接读取手机号到用户名框，如果没有则自己输入
		 */
		// if (phoneMgr.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE ) {
		// mUsernameView.setEnabled(false);
		// mUsernameView.setText(phoneMgr.getLine1Number());
		// }

		// mPasswordView = (EditText) findViewById(R.id.password);
		// mPasswordView
		// .setOnEditorActionListener(new TextView.OnEditorActionListener() {
		// @Override
		// public boolean onEditorAction(TextView textView, int id,
		// KeyEvent keyEvent) {
		// if (id == R.id.login || id == EditorInfo.IME_NULL) {
		// attemptLogin();
		// return true;
		// }
		// return false;
		// }
		// });

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// 点击登录的时候看看是3g还是 wifi如果是
						attemptLogin();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mUsernameView.setError(null);
		// mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mUsername = mUsernameView.getText().toString();
		// mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		// if (TextUtils.isEmpty(mPassword)) {
		// mPasswordView.setError(getString(R.string.error_field_required));
		// focusView = mPasswordView;
		// cancel = true;
		// }

		// Check for a valid email address.
		if (TextUtils.isEmpty(mUsername)) {
			mUsernameView.setError(getString(R.string.error_field_required));
			focusView = mUsernameView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			if (checkNetWorkStatus(LoginActivity.this)) {
				// Simulate network access.
				mLoginStatusMessageView
						.setText(R.string.login_progress_signing_in);
				showProgress(true);
				mAuthTask = new UserLoginTask();
				mAuthTask.execute((Void) null);
			} else {
				Dialog dialog = new AlertDialog.Builder(LoginActivity.this)
						.setTitle("提示信息").setMessage("当前未联网，不能访问服务器！请联网操作。")
						.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

							}
						}).create();
				dialog.show();
			}
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	@SuppressLint("NewApi")
	public class UserLoginTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			Map<String, String> postParam = new HashMap<String, String>();
			postParam.put("type", "appLogin");
			postParam.put("telephone", mUsername);
			postParam.put("macAddress", macAddress);
			String jsonStr = HttpHelper.sendPostMessage(
					ConstantParams.URL_LOGIN, postParam, "utf-8");
			Log.i("LoginActivity", "login return  : " + jsonStr);
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
			mAuthTask = null;
			showProgress(false);
			Log.i("LoginActivity", "login::" + result);
			if ("success".equals(result)) {

				// 讲电话和MAC地址存储到本地
				SharedPreferences sp = getApplicationContext().getSharedPreferences(
						ConstantParams.SHARED_PREFERENCE_NAME,
						Context.MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.putString(ConstantParams.FIELD_TELEPHONE, mUsername);
				editor.putString(ConstantParams.FIELD_MAC_ADDRESS, macAddress);
				editor.commit();
				
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, MenuActivity.class);
				startActivity(intent);
			} else if ("fail".equals(result)) {
				mUsernameView
						.setError(getString(R.string.error_incorrect_telephone));
				mUsernameView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
					.setMessage("确定要退出程序？")
					.setPositiveButton(
							"确定",
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// finish();
									// System.exit(0);
									ApplicationManager.finishProgram();
								}
							})
					.setNegativeButton(
							"取消",
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}

							}).create();
			dialog.show();
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ApplicationManager.remove(LoginActivity.this);
	}

}
