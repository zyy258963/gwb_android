package com.gwb.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.artifex.mupdfdemo.R;
import com.gwb.activity.pojo.Books;
import com.gwb.activity.pojo.FavouriteBook;
import com.gwb.utils.ConstantParams;
import com.gwb.utils.DownloadUtils;
import com.gwb.utils.FastjsonTools;
import com.gwb.utils.HttpHelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchResultActivity extends BaseActivity {
	private TextView tvKeywords;
	private Button btnSearch;
	private TextView tvSearchResult;
	private ListView listViewSearchResult;
	private List<Books> bookList = new ArrayList<Books>();
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		// ApplicationManager.add(this);

		dialog = new ProgressDialog(this);
		dialog.setCancelable(false);

		Log.i("PDF", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa ");
		tvKeywords = (TextView) findViewById(R.id.editText_searchResult);
		btnSearch = (Button) findViewById(R.id.btn_searchResult);
		tvSearchResult = (TextView) findViewById(R.id.textView_searchResult);
		listViewSearchResult = (ListView) findViewById(R.id.listView_searchResult);
		tvSearchResult.setVisibility(View.GONE);

		btnSearch.setHeight(ConstantParams.SIZE_MENU_BTN_HEIGHT -5 );
		btnSearch.setTextSize(ConstantParams.SIZE_MENU_BTN_TEXT);
		tvKeywords.setTextSize(ConstantParams.SIZE_MENU_EDIT_TEXT);
		tvKeywords.setHeight(ConstantParams.SIZE_MENU_BTN_HEIGHT);

		

		btnSearch.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new bookListAsynTask().execute();
			}
		});

	}

	private void initLayout() {
		data.clear();
		if (bookList != null && !"".equals(bookList) && bookList.size() > 0) {
			tvSearchResult.setVisibility(View.GONE);
			for (int i = 0; i < bookList.size(); i++) {
				Map<String, Object> item = new HashMap<String, Object>();
				Books book = bookList.get(i);
				item.put(ConstantParams.COLUMN_BOOK_NAME, book.getBookName());
				data.add(item);
			}

			listViewSearchResult.setAdapter(new SimpleAdapter(this, data,
					android.R.layout.simple_list_item_1,
					new String[] { ConstantParams.COLUMN_BOOK_NAME },
					new int[] { android.R.id.text1 }));

			// 为ListView设置列表项点击监听器

			// 为ListView设置列表项点击监听器
			listViewSearchResult
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, final int position, final long id) {
							ConstantParams.CURRENT_BOOK_ID = bookList.get(
									position).getBookId();
							ConstantParams.CURRENT_BOOK_NAME = bookList.get(
									position).getBookName();
							String path = ConstantParams.URL_DOWN_PDF_BASE
									+ bookList.get(position).getBookUrl();
							Log.i("PDF", "------------------" + path);
							new downLoadFileAsyn().execute(path);
						}
					});

		} else {
			tvSearchResult.setVisibility(View.VISIBLE);
		}

	}

	@SuppressLint("NewApi")
	private class bookListAsynTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... arg0) {
			String keywords = tvKeywords.getText().toString();

			String jsonString = null;
			String url = null;
			// try {
			try {
				url = ConstantParams.URL_SEARCH_BOOKS + "&"
						+ ConstantParams.FIELD_TELEPHONE + "="
						+ ConstantParams.CURRENT_TELEPHONE + "&"
						+ ConstantParams.FIELD_MAC_ADDRESS + "="
						+ ConstantParams.CURRENT_MACADDRESS + "&"
						+ ConstantParams.FIELD_USER_ID + "="
						+ ConstantParams.CURRENT_USER_ID + "&"
						+ ConstantParams.FIELD_KEYWORDS + "=" + URLEncoder.encode(URLEncoder.encode(keywords, "UTF-8"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// + URLEncoder.encode(keywords, "UTF-8");
			jsonString = HttpHelper.sendGetMessage(url, "utf-8");
			Log.i("bookListActivty", jsonString);
			Log.i("bookListActivty", url);
			bookList = FastjsonTools.getContentListPojos(jsonString,
					Books.class);
			// } catch (UnsupportedEncodingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// return false;
			// }
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

	@SuppressLint("NewApi")
	private class AddFavoriteAsynTask extends AsyncTask<Integer, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			// 增加我喜欢的文档的时候，只需要知道是否添加成功即可，
			String jsonString1 = null;
			String addurl = ConstantParams.URL_ADD_FAVOURITE_BOOKS + "&"
					+ ConstantParams.FIELD_TELEPHONE + "="
					+ ConstantParams.CURRENT_TELEPHONE + "&"
					+ ConstantParams.FIELD_MAC_ADDRESS + "="
					+ ConstantParams.CURRENT_MACADDRESS + "&"
					+ ConstantParams.FIELD_USER_ID + "="
					+ ConstantParams.CURRENT_USER_ID + "&"
					+ ConstantParams.FIELD_BOOK_ID + "=" + params[0];
			// success - 成功 。。 fail 。。。 失败
			jsonString1 = HttpHelper.sendGetMessage(addurl, "utf-8");
			// Looper.prepare();
			Log.i("PDF", "addurl ：>" + addurl + "<");
			Log.i("PDF", "添加返回的数据：>" + jsonString1 + "<");
			if (jsonString1 != null && !"".equals(jsonString1)
					&& jsonString1.contains("success")) {
				return true;
			} else {
				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {
			Log.i("BookActivity", "result : " + result);
			if (result) {
				Toast.makeText(SearchResultActivity.this, "添加成功",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(SearchResultActivity.this, "添加出错，请联系管理员。",
						Toast.LENGTH_SHORT).show();
			}

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
				DownloadUtils.getTempFile(params[0]);
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
}
