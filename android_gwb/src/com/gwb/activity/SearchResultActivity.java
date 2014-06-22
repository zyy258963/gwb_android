package com.gwb.activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchResultActivity extends BaseActivity {
	// private TextView tvKeywords;
	private Button btnSearch;
	private TextView tvSearchResult;
	private AutoCompleteTextView autoKeywords;
	private ListView listViewSearchResult;
	private List<Books> bookList = new ArrayList<Books>();
//	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

	private ProgressDialog dialog;
	private Handler handler;
	
	@SuppressLint("HandlerLeak") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		// ApplicationManager.add(this);

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
		

		// tvKeywords = (TextView) findViewById(R.id.editText_searchResult);
		autoKeywords = (AutoCompleteTextView) findViewById(R.id.autoEditText_searchResult);
		btnSearch = (Button) findViewById(R.id.btn_searchResult);
		tvSearchResult = (TextView) findViewById(R.id.textView_searchResult);
		listViewSearchResult = (ListView) findViewById(R.id.listView_searchResult);
		tvSearchResult.setVisibility(View.GONE);

		initAutoComplete("searchHistory", autoKeywords);
		
		btnSearch.setHeight(ConstantParams.SIZE_ROW - 5);
		btnSearch.setTextSize(ConstantParams.SIZE_TOP_TEXT);
		// tvKeywords.setTextSize(ConstantParams.SIZE_TOP_TEXT);
		// tvKeywords.setHeight(ConstantParams.SIZE_ROW);
		autoKeywords.setTextSize(ConstantParams.SIZE_TOP_TEXT);
		autoKeywords.setHeight(ConstantParams.SIZE_ROW);

		btnSearch.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveHistory("searchHistory", autoKeywords);
			
				new bookListAsynTask().execute();
			}
		});

	}

	private void initLayout() {
		initAutoComplete("searchHistory", autoKeywords);
Log.i("PDF", "bookList:----------" +bookList);
		if (bookList != null && !"".equals(bookList) && bookList.size() > 0) {
			tvSearchResult.setVisibility(View.GONE);
			// for (int i = 0; i < bookList.size(); i++) {
			// Map<String, Object> item = new HashMap<String, Object>();
			// Books book = bookList.get(i);
			// item.put(ConstantParams.COLUMN_BOOK_NAME, book.getBookName());
			// data.add(item);
			// }

			listViewSearchResult.setAdapter(new BookListAdapter(
					SearchResultActivity.this, bookList));

			// 为ListView设置列表项点击监听器
			listViewSearchResult
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, final int position, final long id) {							
							AlertDialog alertDialog = new AlertDialog.Builder(
									SearchResultActivity.this).setTitle("提示")
									.setMessage("继续下载将会产生流量，是否继续？")
									.setPositiveButton("继续", new OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialogInterface,
												int arg1) {
											dialogInterface.dismiss();
											ConstantParams.CURRENT_BOOK_ID = bookList
													.get(position).getBookId();
											ConstantParams.CURRENT_BOOK_NAME = bookList
													.get(position).getBookName();
											String path = ConstantParams.URL_DOWN_PDF_BASE
													+ bookList.get(position).getBookUrl();
											Log.i("PDF", "------------------" + path);
											path = path.replace("\\", "/");

											
											
											// 先判断本地有无已下载的文件
											File file = new File(ConstantParams.FILE_STORE_PATH,
													ConstantParams.FIELD_FAVOURITE_ID
															+ ConstantParams.CURRENT_BOOK_ID + ".pdf");
											if (file.exists()) {
												showPdf(file.getAbsolutePath());
											} else {
												new downLoadFileAsyn().execute(path);
											}
											
//											SharedPreferences sp = getSharedPreferences(
//													ConstantParams.SHARED_PREFERENCE_NAME,
//													Context.MODE_PRIVATE);
//
//											String localPath = sp.getString(ConstantParams.FIELD_FAVOURITE_ID
//																	+ ConstantParams.CURRENT_BOOK_ID,"");
//											if (localPath != null
//													&& !"".equals(localPath)) {
//												showPdf(localPath);
//											} else {
//												new downLoadFileAsyn().execute(path);
//											}

										}
									}).setNegativeButton("取消", new OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialogInterface,
												int arg1) {
											// TODO Auto-generated method stub
											dialogInterface.dismiss();
										}
									}).create();
							alertDialog.show();
						}
					});

		} else {
			tvSearchResult.setVisibility(View.VISIBLE);
			listViewSearchResult.setAdapter(null);
		}

	}

	/**
	 * 把指定AutoCompleteTextView中内容保存到sharedPreference中指定的字符段
	 * 
	 * @param field
	 *            保存在sharedPreference中的字段名
	 * @param autoCompleteTextView
	 *            要操作的AutoCompleteTextView
	 */
	private void saveHistory(String field,
			AutoCompleteTextView autoCompleteTextView) {
		String text = autoCompleteTextView.getText().toString();
		SharedPreferences sp = getSharedPreferences(
				ConstantParams.SHARED_PREFERENCE_NAME, 0);
		String longhistory = sp.getString(field, "nothing");
		if (!longhistory.contains(text + ",")) {
			StringBuilder sb = new StringBuilder(longhistory);
			sb.insert(0, text + ",");
			sp.edit().putString("searchHistory", sb.toString()).commit();
		}
	}

	/**
	 * 初始化AutoCompleteTextView，最多显示5项提示，使 AutoCompleteTextView在一开始获得焦点时自动提示
	 * 
	 * @param field
	 *            保存在sharedPreference中的字段名
	 * @param autoCompleteTextView
	 *            要操作的AutoCompleteTextView
	 */
	private void initAutoComplete(String field,
			AutoCompleteTextView autoCompleteTextView) {
		SharedPreferences sp = getSharedPreferences(
				ConstantParams.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
		if (sp != null) {
			String longhistory = sp.getString("searchHistory", "");
			if (!"".equals(longhistory)) {
				String[] histories = longhistory.split(",");
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_dropdown_item_1line, histories);
				// 只保留最近的10条的记录
				if (histories.length > 10) {
					String[] newHistories = new String[10];
					System.arraycopy(histories, 0, newHistories, 0, 10);
//					histories = newHistories;
					adapter = new ArrayAdapter<String>(this,
							android.R.layout.simple_dropdown_item_1line, newHistories);
//					adapter = new ArrayAdapter<String>(this, resource)
				}
				autoCompleteTextView.setAdapter(adapter);
//				autoCompleteTextView.setAdapter(new AutoTextAdapter(this, histories));
				autoCompleteTextView
						.setOnFocusChangeListener(new OnFocusChangeListener() {
							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								AutoCompleteTextView view = (AutoCompleteTextView) v;
								if (hasFocus) {
									view.showDropDown();
								}
							}
						});
			}
			
		}
		
	}

	@SuppressLint("NewApi")
	private class bookListAsynTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... arg0) {
			// String keywords = tvKeywords.getText().toString();
			String keywords = autoKeywords.getText().toString();
			String jsonString = null;
			String url = null;
			try {
				url = ConstantParams.URL_SEARCH_BOOKS
						+ "&"
						+ ConstantParams.FIELD_TELEPHONE
						+ "="
						+ ConstantParams.CURRENT_TELEPHONE
						+ "&"
						+ ConstantParams.FIELD_MAC_ADDRESS
						+ "="
						+ ConstantParams.CURRENT_MACADDRESS
						+ "&"
						+ ConstantParams.FIELD_USER_ID
						+ "="
						+ ConstantParams.CURRENT_USER_ID
						+ "&"
						+ ConstantParams.FIELD_KEYWORDS
						+ "="
						+ URLEncoder.encode(
								URLEncoder.encode(keywords, "UTF-8"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// + URLEncoder.encode(keywords, "UTF-8");
			try {
				jsonString = HttpHelper.sendGetMessage(url, "utf-8");
				Log.i("bookListActivty", jsonString);
				Log.i("bookListActivty", url);
				bookList = FastjsonTools.getContentListPojos(jsonString,
						Books.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}

		@Override
		protected void onPreExecute() {
			Log.i("SearchResultActivity", "set booklist null :" + bookList);
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

//	@SuppressLint("NewApi")
//	private class downLoadFileAsyn extends AsyncTask<String, Void, Boolean> {
//
//		@Override
//		protected void onPreExecute() {
//			dialog.show();
//			super.onPreExecute();
//		}
//
//		@SuppressWarnings("deprecation")
//		@Override
//		protected Boolean doInBackground(String... params) {
//			Log.i("BookActivity", "inbackground:" + params[0]);
//			ConstantParams.TEMP_FILE = null;
//
//			try {
//				DownloadUtils.getTempFile(params[0]);
//				return true;
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return false;
//			}
//
//		}
//
//		@Override
//		protected void onPostExecute(Boolean result) {
//			dialog.dismiss();
//			// Log.i("PDF", ConstantParams.TEMP_FILE.getAbsolutePath());
//			if (result && ConstantParams.TEMP_FILE != null
//					&& ConstantParams.TEMP_FILE.length() > 0) {
//				showPdf(ConstantParams.TEMP_FILE_PATH);
//			} else {
//				AlertDialog dialog = new AlertDialog.Builder(
//						SearchResultActivity.this).setTitle("提示框")
//						.setMessage(R.string.prompt_error_file)
//						.setPositiveButton("确定", new OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								// TODO Auto-generated method stub
//								dialog.dismiss();
//							}
//						}).create();
//				dialog.show();
//			}
//		}
//	}
	
	
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
				AlertDialog dialog = new AlertDialog.Builder(SearchResultActivity.this)
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
			connection.setConnectTimeout(3000);
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

			Log.i("PDF",
					"ConstantParams.TEMP_FILE "
							+ ConstantParams.TEMP_FILE.getAbsolutePath());
			Log.i("DownloadUtils", "fos:" + fos + ": bis:" + bis + ": is:" + is
					+ ": total:" + total);

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
