package com.gwb.activity;

import java.util.ArrayList;
import java.util.List;

import com.artifex.mupdfdemo.R;
import com.gwb.activity.pojo.BookCategory;
import com.gwb.utils.ApplicationManager;
import com.gwb.utils.ConstantParams;
import com.gwb.utils.DensityUtil;
import com.gwb.utils.FastjsonTools;
import com.gwb.utils.HttpHelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
	private BookCategory category = new BookCategory();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ApplicationManager.add(MenuActivity.this);
//		setContentView(R.layout.activity_main);
		new MenuAsynTask().execute();

	}

	@SuppressLint("NewApi")
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
		editText.setTextSize(ConstantParams.SIZE_MENU_EDIT_TEXT);
		editText.setHeight(ConstantParams.SIZE_MENU_BTN_HEIGHT);
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
				ConstantParams.SIZE_MENU_BTN_HEIGHT * 3));

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(2, -1, 2, 0);

		LinearLayout layout_menu = new LinearLayout(MenuActivity.this);
		layout_menu.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < cateList.size(); i++) {
			Log.i("MenuActivity", "---->>>" + String.valueOf(i));
			category = cateList.get(i);
			final Button button = new Button(this);
			button.setId(category.getCategoryId());
			button.setText(category.getCategoryName());
			button.setTextSize(ConstantParams.SIZE_MENU_BTN_TEXT);
			button.setHeight(ConstantParams.SIZE_MENU_BTN_HEIGHT);
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
		// 增加我的常用文档 button
		Button btnFavorite = new Button(this);
		btnFavorite.setText(R.string.title_activity_favorite);
		btnFavorite.setTextSize(ConstantParams.SIZE_MENU_BTN_TEXT);
		btnFavorite.setHeight(ConstantParams.SIZE_MENU_BTN_HEIGHT);
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

			jsonString = HttpHelper.sendGetMessage(
					ConstantParams.URL_GET_CATEGORYS + "&"
							+ ConstantParams.FIELD_TELEPHONE + "="
							+ ConstantParams.CURRENT_TELEPHONE + "&"
							+ ConstantParams.FIELD_MAC_ADDRESS + "="
							+ ConstantParams.CURRENT_MACADDRESS + "&"
							+ ConstantParams.FIELD_USER_ID + "="
							+ ConstantParams.CURRENT_USER_ID, "utf-8");

			// String str =
			// "{\"content\":[{\"categoryId\":1,\"categoryName\":\"专业规范标准\"},{\"categoryId\":2,\"categoryName\":\"专业管理文件\"},{\"categoryId\":3,\"categoryName\":\"单位行政文件\"},{\"categoryId\":4,\"categoryName\":\"上级单位文件\"},{\"categoryId\":5,\"categoryName\":\"最新各类文档\"}],\"header\":{\"code\":\"1\",\"msg\":\"SUCCESS\"}}";

			// cateList = FastjsonTools.getContentListPojos(str,
			// BookCategory.class);
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
