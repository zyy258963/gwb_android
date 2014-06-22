package com.gwb.activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.artifex.mupdfdemo.R;
import com.gwb.activity.pojo.Books;
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
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
	private Handler handler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);

		TextView textView = (TextView) findViewById(R.id.textView_favorite_title);
		textView.setTextSize(ConstantParams.SIZE_MAIN_TEXT);
		textView.setHeight(ConstantParams.SIZE_ROW);
		textView.setGravity(Gravity.CENTER);

		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(false);
		dialog.setProgress(0);
		dialog.setMax(100);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what >= 100) {
					dialog.cancel();
					dialog.dismiss();
				}
				dialog.setProgress(msg.what);
				super.handleMessage(msg);
			}
		};

		listViewFavorite = (ListView) findViewById(R.id.listView_favoriteBooks);
		tvPopResult = (TextView) findViewById(R.id.textView_favorite_noresult);
		new FavoriteListAsynTask().execute();

	}

	private void initLayout() {
//		data.clear();
		if (favoriteList != null && !"".equals(favoriteList)
				&& favoriteList.size() > 0) {
			tvPopResult.setVisibility(View.GONE);
//			for (int i = 0; i < favoriteList.size(); i++) {
//				Map<String, Object> item = new HashMap<String, Object>();
//				FavouriteBook book = favoriteList.get(i);
//				item.put(ConstantParams.COLUMN_BOOK_NAME, book.getBookName());
//				data.add(item);
//			}

			listViewFavorite.setAdapter(new FavouriteBookAdapter(
					FavoriteActivity.this, favoriteList));

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
							ConstantParams.CURRENT_BOOK_NAME = favoriteList
									.get(position).getBookName();
							String path = ConstantParams.URL_DOWN_PDF_BASE
									+ favoriteList.get(position).getBookUrl();
							Log.i("PDF", "------------------" + path);

							new downLoadFileAsyn().execute(path);

						}
					});
					btnFavourite.setOnClickListener(new View.OnClickListener() {

						@SuppressLint("NewApi")
						@Override
						public void onClick(View v) {
							int favId = favoriteList.get(position)
									.getFavouriteId();
							ConstantParams.CURRENT_BOOK_ID = favoriteList.get(
									position).getBookId();
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
			listViewFavorite.setAdapter(null);
		}
	}

	class FavouriteBookAdapter extends BaseAdapter {

		private LayoutInflater myInflater;
		private List<FavouriteBook> datas;

		public FavouriteBookAdapter(Context context, List<FavouriteBook> data) {
			this.myInflater = LayoutInflater.from(context);
			this.datas = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return datas.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup parent) {
			// TODO Auto-generated method stub
			contentView = myInflater.inflate(R.layout.book_list, null);
			TextView tv = (TextView) contentView
					.findViewById(R.id.book_list_item_name);
			tv.setTextSize(ConstantParams.SIZE_TOP_TEXT);
			tv.setText(datas.get(position).getBookName());
			tv.setHeight(ConstantParams.SIZE_ROW);
			return contentView;
		}

	}

	@SuppressLint("NewApi")
	private class FavoriteListAsynTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... arg0) {
//		此处不需要向服务器请求，直接在本地获得数据
			String jsonString = null;
			String url = ConstantParams.URL_LIST_FAVOURITE_BOOKS + "&"
					+ ConstantParams.FIELD_TELEPHONE + "="
					+ ConstantParams.CURRENT_TELEPHONE + "&"
					+ ConstantParams.FIELD_MAC_ADDRESS + "="
					+ ConstantParams.CURRENT_MACADDRESS + "&"
					+ ConstantParams.FIELD_USER_ID + "="
					+ ConstantParams.CURRENT_USER_ID;
			try {
				jsonString = HttpHelper.sendGetMessage(url, "utf-8");
				Log.i("favoriteListActivty", jsonString);
				Log.i("favoriteListActivty", url);
				favoriteList = FastjsonTools.getContentListPojos(jsonString,
						FavouriteBook.class);
				filterLocalFile(favoriteList);
				if (favoriteList != null && !"".equals(favoriteList)) {
					ConstantParams.CURRENT_FAVOURITE_BOOK_SIZE = favoriteList
							.size();
				} else {
					ConstantParams.CURRENT_FAVOURITE_BOOK_SIZE = 0;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	private void filterLocalFile(List<FavouriteBook> favoriteList) {
		SharedPreferences sp = getSharedPreferences(
				ConstantParams.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Set<String> set = sp.getStringSet(
				ConstantParams.FIELD_FAVOURITE_BOOK_SET, null);
		if (set != null) {
			for (int i = 0; i < favoriteList.size(); i++) {
				if (!set.contains(favoriteList.get(i).getBookId() + "")) {
					favoriteList.remove(i);
				}
			}
		} else {
			favoriteList = null;
		}
	}

	@SuppressLint("NewApi")
	private class DeleteFavoriteAsynTask extends
			AsyncTask<Integer, Void, Boolean> {
		// List<FavouriteBook> list = new ArrayList<FavouriteBook>();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			/** 删除的时候一共有以下几个步骤
			1. 通知服务器，根据返回结果进行判断   后修改为  删除本地收藏文件的时候不想服务器发信息
			2. 如果服务器正确返回，那么删除本地文件，删除favouriteset中的bookId，
			
			**/
//			String jsonString1 = null;
//			String deleteurl = ConstantParams.URL_DELETE_FAVOURITE_BOOKS + "&"
//					+ ConstantParams.FIELD_TELEPHONE + "="
//					+ ConstantParams.CURRENT_TELEPHONE + "&"
//					+ ConstantParams.FIELD_MAC_ADDRESS + "="
//					+ ConstantParams.CURRENT_MACADDRESS + "&"
//					+ ConstantParams.FIELD_FAVOURITE_ID + "=" + params[0];
//			// success - 成功 。。 fail 。。。 失败
//			try {
//				jsonString1 = HttpHelper.sendGetMessage(deleteurl, "utf-8");
//				// Looper.prepare();
//				Log.i("BookActivity", "删除返回的数据：>" + deleteurl + "<");
//				
//				if (jsonString1 != null && !"".equals(jsonString1)) {
//					favoriteList = FastjsonTools.getContentListPojos(
//							jsonString1, FavouriteBook.class);
//					filterLocalFile(favoriteList);
//					if (favoriteList != null
//							&& !"".equals(favoriteList)
//							&& favoriteList.size() < ConstantParams.CURRENT_FAVOURITE_BOOK_SIZE) {
//						return true;
//					} else {
//						return false;
//					}
//				} else {
//					return false;
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return false;
//			}
			
			return true;
			
		}

		@Override
		protected void onPostExecute(Boolean result) {
			Log.i("FavoriteActivity", "result : " + result);
			if (result) {
				try {
					File file = new File(ConstantParams.FILE_STORE_PATH,
							ConstantParams.FIELD_FAVOURITE_ID
									+ ConstantParams.CURRENT_BOOK_ID + ".pdf");
					if (file.exists()) {
						FileUtil.delFile(file.getAbsolutePath());
					}
					SharedPreferences sp = getSharedPreferences(
							ConstantParams.SHARED_PREFERENCE_NAME,
							Context.MODE_PRIVATE);
					Set<String> set = sp.getStringSet(
							ConstantParams.FIELD_FAVOURITE_BOOK_SET, null);
					if (set != null
							&& set.contains(ConstantParams.CURRENT_BOOK_ID + "")) {
						set.remove(ConstantParams.CURRENT_BOOK_ID + "");
					}
					Editor editor = sp.edit();
					editor.putStringSet(
							ConstantParams.FIELD_FAVOURITE_BOOK_SET, set);
//					editor.putString(ConstantParams.FIELD_FAVOURITE_ID
//							+ ConstantParams.CURRENT_BOOK_ID, "");
					editor.commit();
					filterLocalFile(favoriteList);
					Toast.makeText(FavoriteActivity.this, "移除成功",
							Toast.LENGTH_SHORT).show();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
		// ApplicationManager.remove(this);
	}

	@SuppressLint("NewApi")
	private class downLoadFileAsyn extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// bookId

			// 先判断本地有无已下载的文件
			File file = new File(ConstantParams.FILE_STORE_PATH,
					ConstantParams.FIELD_FAVOURITE_ID
							+ ConstantParams.CURRENT_BOOK_ID + ".pdf");
			if (file.exists()) {
				return file.getAbsolutePath();
			} else {
				Log.i("BookActivity", "inbackground:" + params[0]);
				ConstantParams.TEMP_FILE = null;
				try {
					getTempFile(params[0]);
					return "success";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "fail";
				}
			}

			// SharedPreferences sp =
			// getApplicationContext().getSharedPreferences(
			// ConstantParams.SHARED_PREFERENCE_NAME,
			// Context.MODE_PRIVATE);
			// String localFile =
			// sp.getString(ConstantParams.FIELD_FAVOURITE_ID+ConstantParams.CURRENT_BOOK_ID,"");

			// if (!"".equals(localFile)) {
			// return localFile;
			// }else {
			// Log.i("BookActivity", "inbackground:" + params[0]);
			// ConstantParams.TEMP_FILE = null;
			//
			// try {
			// getTempFile(params[0]);
			// return "success";
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// return "fail";
			// }
			// }
			//
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			// Log.i("PDF", ConstantParams.TEMP_FILE.getAbsolutePath());
			if ("success".equals(result) && ConstantParams.TEMP_FILE != null
					&& ConstantParams.TEMP_FILE.length() > 0) {
				showPdf(ConstantParams.TEMP_FILE_PATH);
			} else if ("fail".equals(result)) {
				AlertDialog dialog = new AlertDialog.Builder(
						FavoriteActivity.this).setTitle("提示框")
						.setMessage(R.string.prompt_error_file)
						.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).create();
				dialog.show();
			} else {
				showPdf(result);
			}
		}
	}

	public void getTempFile(String uriPath) throws Exception {
		URL url;
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		InputStream is = null;
		int size = 0;
		int total = 0;
		try {
			url = new URL(uriPath);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(5000);
			// 获得文件大小
			size = connection.getContentLength();
			System.out.println("size:" + size);
			try {
				is = connection.getInputStream();
				fos = new FileOutputStream(ConstantParams.TEMP_FILE_PATH);
				bis = new BufferedInputStream(is);
			} catch (Exception e) {
				throw e;
			}
			if (is == null) {
				// ConstantParams.TEMP_FILE = null;
			} else {
				byte buffer[] = new byte[1024];
				int len;
				while ((len = bis.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					total += len;

					Message msg = new Message();
					msg.what = total * 100 / size;
					handler.sendMessage(msg);

				}
				ConstantParams.TEMP_FILE = new File(
						ConstantParams.TEMP_FILE_PATH);
			}
			// 将文件村至本地并且存在 sharedPerference中
			File path = new File(ConstantParams.FILE_STORE_PATH);
			if (!path.exists()) {
				path.mkdirs();
			}

			File tarFile = new File(path, ConstantParams.FIELD_FAVOURITE_ID
					+ ConstantParams.CURRENT_BOOK_ID + ".pdf");
			if (!tarFile.exists()) {
				tarFile.createNewFile();
				FileUtil.copyFile(ConstantParams.TEMP_FILE, tarFile);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (bis != null) {
					bis.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		}
	}
}
