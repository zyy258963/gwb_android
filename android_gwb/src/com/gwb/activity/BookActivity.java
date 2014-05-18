package com.gwb.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.artifex.mupdfdemo.R;
import com.gwb.activity.pojo.Books;
import com.gwb.utils.ApplicationManager;
import com.gwb.utils.ConstantParams;
import com.gwb.utils.DownloadUtils;
import com.gwb.utils.FastjsonTools;
import com.gwb.utils.HttpHelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ApplicationManager.add(this);
		dialog = new ProgressDialog(this);
		dialog.setCancelable(false);
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
					new downLoadFileAsyn().execute(path);
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
	private class downLoadFileAsyn extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@SuppressWarnings("deprecation")
		@Override
		protected Void doInBackground(String... params) {
			Log.i("BookActivity", "inbackground:" + params[0]);
			ConstantParams.TEMP_FILE = null;
			
			DownloadUtils.getTempFile(params[0]);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			// Log.i("PDF", ConstantParams.TEMP_FILE.getAbsolutePath());
			if (ConstantParams.TEMP_FILE != null
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
}
