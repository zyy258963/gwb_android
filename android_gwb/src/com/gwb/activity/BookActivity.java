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

import com.artifex.mupdfdemo.R;
import com.gwb.activity.pojo.Books;
import com.gwb.utils.ConstantParams;
import com.gwb.utils.DownloadUtils;
import com.gwb.utils.FastjsonTools;
import com.gwb.utils.HttpHelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BookActivity extends BaseActivity {

	private List<Books> bookList = new ArrayList<Books>();
	private ListView listViewBook = null;
	private TextView tvNoResult = null;
	private ProgressDialog dialog;
	private Handler handler = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ApplicationManager.add(this);
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(false);
		dialog.setProgress(0);
		dialog.setMax(100);

		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if (msg.what >=100) {
					dialog.cancel();
					dialog.dismiss();
				}
				dialog.setProgress(msg.what);
				super.handleMessage(msg);
			}
		};
		
		new BookAsynTask().execute();
	}

	private void initLayout() {
		setContentView(R.layout.activity_book);
		listViewBook = (ListView) findViewById(R.id.listView_books);

		tvNoResult = (TextView) findViewById(R.id.tv_no_booklist);

		TextView textView = (TextView) findViewById(R.id.textView_book_menu);
		textView.setTextSize(ConstantParams.SIZE_MAJOR_TEXT_VIEW);
		textView.setHeight(ConstantParams.SIZE_MAJOR_TEXT_VIEW_HEIGHT);
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		textView.setText(ConstantParams.CURRENT_CATEGORY_NAME + ">>"
				+ ConstantParams.CURRENT_CLASS_NAME);

		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Log.i("PDF", bookList.toString());
		if (bookList != null && !"".equals(bookList) && bookList.size() > 0) {
			Log.i("BookActivity", "----- 1");
			tvNoResult.setVisibility(View.GONE);
			for (int i = 0; i < bookList.size(); i++) {
				Map<String, Object> item = new HashMap<String, Object>();
				Books book = bookList.get(i);
				item.put(ConstantParams.COLUMN_BOOK_NAME, book.getBookName());
				data.add(item);
			}

			listViewBook.setAdapter(new SimpleAdapter(this, data,
					R.layout.book_list,
					new String[] { ConstantParams.COLUMN_BOOK_NAME },
					new int[] { R.id.book_list_item_name }));

			// 为ListView设置列表项点击监听器
			listViewBook.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						final int position, final long id) {
					ConstantParams.CURRENT_BOOK_ID = bookList.get(position)
							.getBookId();
					ConstantParams.CURRENT_BOOK_NAME = bookList.get(position)
							.getBookName();
					String path = ConstantParams.URL_DOWN_PDF_BASE
							+ bookList.get(position).getBookUrl();
					Log.i("PDF", "------------------" + path);
					path = path.replace("\\", "/");
					
					SharedPreferences sp = getApplicationContext().getSharedPreferences(
							ConstantParams.SHARED_PREFERENCE_NAME,
							Context.MODE_PRIVATE);
					
					String localPath = sp.getString(ConstantParams.FIELD_FAVOURITE_ID+ConstantParams.CURRENT_BOOK_ID,"");
					if (localPath != null && !"".equals(localPath)) {
						showPdf(localPath);
					}else {
						new downLoadFileAsyn().execute(path);
					}
				}
			});
		} else {
			Log.i("BookActivity", "-----");
			tvNoResult.setVisibility(View.VISIBLE);
		}

	}

	@SuppressLint("NewApi")
	private class BookAsynTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... arg0) {
			Intent intent = getIntent();
			int cid = intent.getIntExtra(ConstantParams.FIELD_CLASS_ID, 0);
			// int cid = 2;
			if (cid != 0) {
				ConstantParams.CURRENT_CLASS_ID = cid;
				String jsonString = null;
				String url = ConstantParams.URL_GET_BOOKS + "&"
						+ ConstantParams.FIELD_TELEPHONE + "="
						+ ConstantParams.CURRENT_TELEPHONE + "&"
						+ ConstantParams.FIELD_MAC_ADDRESS + "="
						+ ConstantParams.CURRENT_MACADDRESS + "&"
						+ ConstantParams.FIELD_CLASS_ID + "="
						+ ConstantParams.CURRENT_CLASS_ID;
				jsonString = HttpHelper.sendGetMessage(url, "utf-8");
				Log.i("BookActivty", jsonString);
				Log.i("BookActivty", url);
				bookList = FastjsonTools.getContentListPojos(jsonString,
						Books.class);

			} else {
				ConstantParams.CURRENT_CATEGORY_ID = 0;
				bookList = null;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			initLayout();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ApplicationManager.remove(this);
	}

	@SuppressLint("NewApi")
	private class downLoadFileAsyn extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@SuppressWarnings("deprecation")
		@Override
		protected Boolean doInBackground(String... params) {
			Log.i("BookActivity", "inbackground:" + params[0]);
			ConstantParams.TEMP_FILE = null;
			try {
				getTempFile(params[0]);
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			// Log.i("PDF", ConstantParams.TEMP_FILE.getAbsolutePath());
			if (result && ConstantParams.TEMP_FILE != null
					&& ConstantParams.TEMP_FILE.length() > 0) {
				showPdf(ConstantParams.TEMP_FILE_PATH);
			} else {
				AlertDialog dialog = new AlertDialog.Builder(BookActivity.this)
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
			}
		}
	}
	
	public void getTempFile(String uriPath) throws Exception{
		URL url;
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		InputStream is = null;
		int size =0;
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
//				ConstantParams.TEMP_FILE = null;
			}else {
				byte buffer[] = new byte[1024];
				int len;
				while ((len = bis.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					total += len;
					
					Message msg = new Message();
					msg.what = total*100/size;
					handler.sendMessage(msg);
					
				}
				ConstantParams.TEMP_FILE = new File(ConstantParams.TEMP_FILE_PATH);
			}
			
			Log.i("PDF", "ConstantParams.TEMP_FILE " + ConstantParams.TEMP_FILE.getAbsolutePath());
			Log.i("DownloadUtils", "fos:" + fos + ": bis:"+bis+": is:"+is + ": total:" +total);
			
			
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
				if (fos!=null) {
					fos.close();
				}
				if (bis!=null) {
					bis.close();
				}
				if (is!=null) {
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
