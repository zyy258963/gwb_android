package com.gwb.activity;

import java.util.ArrayList;
import java.util.List;

import com.artifex.mupdfdemo.R;
import com.gwb.activity.pojo.BookCategory;
import com.gwb.utils.ApplicationManager;
import com.gwb.utils.ConstantParams;
//import com.gwb.utils.DensityUtil;
import com.gwb.utils.FastjsonTools;
import com.gwb.utils.HttpHelper;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

public class MenuActivity extends BaseActivity {

	public List<BookCategory> cateList = new ArrayList<BookCategory>();
	private BookCategory category = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ApplicationManager.add(MenuActivity.this);
		// setContentView(R.layout.activity_main);

		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				ConstantParams.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		String str = sp.getString(ConstantParams.FIELD_CATEGORY_LIST, "");
		if (str!=null && !"".equals(str)) {
			initLayout();
		}else {
			new MenuAsynTask().execute();
		}
		

	}

	@SuppressLint({ "NewApi", "ResourceAsColor" })
	private void initLayout() {
		// 获得layout 对象
		LayoutInflater inflater = (LayoutInflater) MenuActivity.this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.activity_menu, null);
		ScrollView scrollView = (ScrollView) layout
				.findViewById(R.id.scrollView_menu);

		// 搜索框事件
		EditText editText = (EditText) layout
				.findViewById(R.id.editText_search);
		editText.setTextSize(ConstantParams.SIZE_TOP_TEXT);
		editText.setHeight(ConstantParams.SIZE_ROW);
		editText.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					Intent intent = new Intent();
					intent.setClass(MenuActivity.this,
							SearchResultActivity.class);
					startActivity(intent);
				}
				return false;
			}
		});

		ImageView imageView = (ImageView) layout.findViewById(R.id.imageViewAd);
		imageView.setLayoutParams(new LayoutParams(getScreenWidth(),
				(int) (ConstantParams.SIZE_ROW * 2.8f)));

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(2, 0, 2, 4);

		LinearLayout layout_menu = new LinearLayout(MenuActivity.this);
		layout_menu.setOrientation(LinearLayout.VERTICAL);
		
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				ConstantParams.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		String str = sp.getString(ConstantParams.FIELD_CATEGORY_LIST, "");
		
		if (str !=null&&!"".equals(str) ) {
			cateList = new ArrayList<BookCategory>();
			String [] cateStr = str.split(";");
			for (int i = 0; i < cateStr.length; i++) {
				String caStr [] = cateStr[i].split(",");
				Log.i("LOG", cateStr[i]);
//				BookCategory cate = new BookCategory(Integer.parseInt(caStr[0]),cateStr[1]);
//				cateList.add(cate);
//			}
			
//			for (int i = 0; i < cateList.size(); i++) {
//				Log.i("MenuActivity", "---->>>" + String.valueOf(i));
//				category = cateList.get(i);	
//				Log.i("LOG", category.toString());
				final Button button = new Button(this);
//				button.setId(category.getCategoryId());
				button.setId(Integer.parseInt(caStr[0]));
				button.setTextColor(Color.WHITE);
				button.setBackgroundResource(R.drawable.button_style);
				button.setTextSize(ConstantParams.SIZE_MAIN_TEXT);
//				button.setText(category.getCategoryName());
				button.setText(caStr[1]);
				button.setHeight(ConstantParams.SIZE_ROW);
				button.setLayoutParams(layoutParams);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent();
						intent.setClass(MenuActivity.this, MajorActivity.class);
						ConstantParams.CURRENT_CATEGORY_NAME = button.getText()
								.toString();
						intent.putExtra(ConstantParams.FIELD_CATEGORY_ID,
								button.getId());
						startActivity(intent);
					}
				});
				layout_menu.addView(button);
			}
		}else {
			SharedPreferences sp1 = getApplicationContext().getSharedPreferences(
					ConstantParams.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
			Editor editor = sp1.edit();
			StringBuffer sb = new StringBuffer("");
			for (int i = 0; i < cateList.size(); i++) {
				Log.i("MenuActivity", "---->>>" + String.valueOf(i));
				category = cateList.get(i);
				sb.append(category.getCategoryId()+","+category.getCategoryName()+";");
				
				final Button button = new Button(this);
				button.setId(category.getCategoryId());
				button.setTextColor(Color.WHITE);
				button.setBackgroundResource(R.drawable.button_style);
				button.setTextSize(ConstantParams.SIZE_MAIN_TEXT);
				button.setText(category.getCategoryName());
				button.setHeight(ConstantParams.SIZE_ROW);
				button.setLayoutParams(layoutParams);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent();
						intent.setClass(MenuActivity.this, MajorActivity.class);
						ConstantParams.CURRENT_CATEGORY_NAME = button.getText()
								.toString();
						intent.putExtra(ConstantParams.FIELD_CATEGORY_ID,
								button.getId());
						startActivity(intent);
					}
				});
				layout_menu.addView(button);
			}

			editor.putString(ConstantParams.FIELD_CATEGORY_LIST, sb.toString());
			editor.commit();
			
		}
		
		// 增加我的常用文档 button
		Button btnFavorite = new Button(this);
		btnFavorite.setTextSize(ConstantParams.SIZE_MAIN_TEXT);
		btnFavorite.setTextColor(Color.WHITE);
		btnFavorite.setText(R.string.title_activity_favorite);
		btnFavorite.setBackgroundResource(R.drawable.button_style);
		btnFavorite.setHeight(ConstantParams.SIZE_ROW);
		btnFavorite.setLayoutParams(layoutParams);
		btnFavorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClass(MenuActivity.this, FavoriteActivity.class);
				startActivity(intent);
			}
		});
		layout_menu.addView(btnFavorite);

		scrollView.addView(layout_menu);
		MenuActivity.this.setContentView(layout);

	}

	@SuppressLint("NewApi")
	private class MenuAsynTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... arg0) {
			String jsonString = null;

			try {
				jsonString = HttpHelper.sendGetMessage(
						ConstantParams.URL_GET_CATEGORYS + "&"
								+ ConstantParams.FIELD_TELEPHONE + "="
								+ ConstantParams.CURRENT_TELEPHONE + "&"
								+ ConstantParams.FIELD_MAC_ADDRESS + "="
								+ ConstantParams.CURRENT_MACADDRESS + "&"
								+ ConstantParams.FIELD_USER_ID + "="
								+ ConstantParams.CURRENT_USER_ID, "utf-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cateList = FastjsonTools.getContentListPojos(jsonString,
					BookCategory.class);

			return true;
		}

		public MenuAsynTask() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPostExecute(Boolean result) {
			initLayout();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog dialog = new AlertDialog.Builder(MenuActivity.this)
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
		ApplicationManager.remove(MenuActivity.this);
	}

}
