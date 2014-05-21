package com.gwb.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.artifex.mupdfdemo.R;
import com.gwb.activity.pojo.FavouriteBook;
import com.gwb.utils.ConstantParams;
import com.gwb.utils.DownloadUtils;
import com.gwb.utils.FastjsonTools;
import com.gwb.utils.FileUtil;
import com.gwb.utils.HttpHelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FavoriteActivity extends BaseActivity {

	private TextView tvPopResult = null;
	private ListView listViewFavorite;
	private List<FavouriteBook> favoriteList = null;
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

	private ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);

		TextView textView = (TextView)findViewById(R.id.textView_favorite_title);
		textView.setTextSize(ConstantParams.SIZE_MAJOR_TEXT_VIEW);
		textView.setHeight(ConstantParams.SIZE_MAJOR_TEXT_VIEW_HEIGHT);
		textView.setGravity(Gravity.CENTER);
		
		
		dialog = new ProgressDialog(this);
//		ApplicationManager.add(this);
		listViewFavorite = (ListView) findViewById(R.id.listView_favoriteBooks);
		tvPopResult = (TextView) findViewById(R.id.textView_favorite_noresult);
		new FavoriteListAsynTask().execute();

	}

	private void initLayout() {
		data.clear();
		if (favoriteList != null && !"".equals(favoriteList)
				&& favoriteList.size() > 0) {
			tvPopResult.setVisibility(View.GONE);
			for (int i = 0; i < favoriteList.size(); i++) {
				Map<String, Object> item = new HashMap<String, Object>();
				FavouriteBook book = favoriteList.get(i);
				item.put(ConstantParams.COLUMN_BOOK_NAME, book.getBookName());
				data.add(item);
			}

//			listViewFavorite.setAdapter(new SimpleAdapter(this, data,
//					android.R.layout.simple_list_item_1,
//					new String[] { ConstantParams.COLUMN_BOOK_NAME },
//					new int[] { android.R.id.text1 }));

			listViewFavorite.setAdapter(new SimpleAdapter(this, data,
					R.layout.book_favourite_list,
					new String[] { ConstantParams.COLUMN_BOOK_NAME },
					new int[] { R.id.book_favourite_list_item_name }));
			
			// 为ListView设置列表项点击监听器
			listViewFavorite.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						final int position, long id) {

					// 新改的
					final AlertDialog alertDialog = new AlertDialog.Builder(
							FavoriteActivity.this).create();
					alertDialog.show();
					Window window = alertDialog.getWindow();
					window.setContentView(R.layout.dialog);

					Button btnView = (Button) window
							.findViewById(R.id.btn_dialog_view);
					Button btnFavourite = (Button) window
							.findViewById(R.id.btn_dialog_delete_favourite);
					Button btnCancel = (Button) window
							.findViewById(R.id.btn_dialog_cancel);
					btnView.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
							ConstantParams.CURRENT_BOOK_ID = favoriteList.get(
									position).getBookId();
							ConstantParams.CURRENT_BOOK_NAME = favoriteList.get(position)
									.getBookName();
							String path = ConstantParams.URL_DOWN_PDF_BASE
									+ favoriteList.get(position).getBookUrl();
							Log.i("PDF", "------------------" + path);
							
							new downLoadFileAsyn().execute(path);

						}
					});
					btnFavourite.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							int favId = favoriteList.get(position).getFavouriteId();
							alertDialog.dismiss();
							new DeleteFavoriteAsynTask().execute(favId);

						}
					});
					btnCancel.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
						}
					});

				}
			});

		} else {
			tvPopResult.setVisibility(View.VISIBLE);
		}
	}

	@SuppressLint("NewApi")
	private class FavoriteListAsynTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... arg0) {
			String jsonString = null;
			String url = ConstantParams.URL_LIST_FAVOURITE_BOOKS + "&"
					+ ConstantParams.FIELD_TELEPHONE + "="
					+ ConstantParams.CURRENT_TELEPHONE + "&"
					+ ConstantParams.FIELD_MAC_ADDRESS + "="
					+ ConstantParams.CURRENT_MACADDRESS + "&"
					+ConstantParams.FIELD_USER_ID+"="
					+ ConstantParams.CURRENT_USER_ID;
			jsonString = HttpHelper.sendGetMessage(url, "utf-8");
			Log.i("favoriteListActivty", jsonString);
			Log.i("favoriteListActivty", url);
			favoriteList = FastjsonTools.getContentListPojos(jsonString,
					FavouriteBook.class);
			if (favoriteList != null && !"".equals(favoriteList)) {
				ConstantParams.CURRENT_FAVOURITE_BOOK_SIZE = favoriteList
						.size();
			} else {
				ConstantParams.CURRENT_FAVOURITE_BOOK_SIZE = 0;
			}

			return true;
		}


		@Override
		protected void onPreExecute() {
			Log.i("SearchResultActivity", "set favoriteList null :"
					+ favoriteList);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			initLayout();
		}
	}

	@SuppressLint("NewApi")
	private class DeleteFavoriteAsynTask extends
			AsyncTask<Integer, Void, Boolean> {
//		List<FavouriteBook> list = new ArrayList<FavouriteBook>();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			// 增加我喜欢的文档的时候，只需要知道是否添加成功即可，
			String jsonString1 = null;
			String deleteurl = ConstantParams.URL_DELETE_FAVOURITE_BOOKS + "&"
					+ ConstantParams.FIELD_TELEPHONE + "="
					+ ConstantParams.CURRENT_TELEPHONE + "&"
					+ ConstantParams.FIELD_MAC_ADDRESS + "="
					+ ConstantParams.CURRENT_MACADDRESS + "&"
					+ ConstantParams.FIELD_FAVOURITE_ID + "=" + params[0];
			// success - 成功 。。 fail 。。。 失败
			jsonString1 = HttpHelper.sendGetMessage(deleteurl, "utf-8");
			// Looper.prepare();
			Log.i("BookActivity", "删除返回的数据：>" + deleteurl + "<");
			Log.i("BookActivity", "favoriteList.size()：>" + favoriteList.size()
					+ "<\n" + " ConstantParams.CURRENT_FAVORITE_BOOK_SIZE：>"
					+ ConstantParams.CURRENT_FAVOURITE_BOOK_SIZE + "<");

			if (jsonString1 != null && !"".equals(jsonString1)) {
				favoriteList = FastjsonTools.getContentListPojos(jsonString1,
						FavouriteBook.class);
				if (favoriteList != null
						&& !"".equals(favoriteList)
						&& favoriteList.size() < ConstantParams.CURRENT_FAVOURITE_BOOK_SIZE) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {
			Log.i("FavoriteActivity", "result : " + result);
			if (result) {
				Toast.makeText(FavoriteActivity.this, "移除成功",
						Toast.LENGTH_SHORT).show();
				
				SharedPreferences sp = getApplicationContext().getSharedPreferences(
						ConstantParams.SHARED_PREFERENCE_NAME,
						Context.MODE_PRIVATE);
				try {
					FileUtil.delFile(sp.getString(ConstantParams.FIELD_FAVOURITE_ID+ConstantParams.CURRENT_BOOK_ID,""));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Editor editor= sp.edit();				
				editor.putString(ConstantParams.FIELD_FAVOURITE_ID+ConstantParams.CURRENT_BOOK_ID,"");
				editor.commit();
				
				
				
			} else {
				Toast.makeText(FavoriteActivity.this, "移除出错，请联系管理员。",
						Toast.LENGTH_SHORT).show();
			}
			initLayout();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		ApplicationManager.remove(this);
	}


	@SuppressLint("NewApi")
	private class downLoadFileAsyn extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@SuppressWarnings("deprecation")
		@Override
		protected String doInBackground(String... params) {
//			bookId   
			// 讲电话和MAC地址存储到本地
			SharedPreferences sp = getApplicationContext().getSharedPreferences(
					ConstantParams.SHARED_PREFERENCE_NAME,
					Context.MODE_PRIVATE);
			String localFile = sp.getString(ConstantParams.FIELD_FAVOURITE_ID+ConstantParams.CURRENT_BOOK_ID,"");
			
			if (!"".equals(localFile)) {
				return localFile;
			}else {
				Log.i("BookActivity", "inbackground:" + params[0]);
				ConstantParams.TEMP_FILE = null;
				
				try {
					DownloadUtils.getTempFile(params[0]);
					return "success";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "fail";
				}
			}
			
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			// Log.i("PDF", ConstantParams.TEMP_FILE.getAbsolutePath());
			if ("success".equals(result) && ConstantParams.TEMP_FILE != null
					&& ConstantParams.TEMP_FILE.length() > 0) {
				showPdf(ConstantParams.TEMP_FILE_PATH);
			} else if("fail".equals(result)){
				AlertDialog dialog = new AlertDialog.Builder(FavoriteActivity.this)
						.setTitle("提示框").setMessage(R.string.prompt_error_file)
						.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).create();
				dialog.show();
			}else {
				showPdf(result);
			}
		}
	}
}
