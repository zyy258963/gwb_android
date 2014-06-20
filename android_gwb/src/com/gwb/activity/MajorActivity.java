package com.gwb.activity;

import java.util.ArrayList;
import java.util.List;

import com.artifex.mupdfdemo.R;
import com.gwb.activity.pojo.BookClass;
import com.gwb.utils.ApplicationManager;
import com.gwb.utils.ConstantParams;
import com.gwb.utils.FastjsonTools;
import com.gwb.utils.HttpHelper;

import android.R.integer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class MajorActivity extends BaseActivity {
	public List<BookClass> classList = new ArrayList<BookClass>();
	private TextView txNoResult = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		// ApplicationManager.add(this);
		new MajorAsynTask().execute();
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	private void initLayout() {
		// 用代码创建布局文件
		// 获得layout 对象
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.activity_major, null);

		TextView textView = (TextView) layout
				.findViewById(R.id.textView_major_menu);
		textView.setTextSize(ConstantParams.SIZE_TOP_TEXT);
		textView.setHeight(ConstantParams.SIZE_ROW);
		textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		textView.setText("公文包 >>" + ConstantParams.CURRENT_CATEGORY_NAME);

		// 顶部目录和搜索
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
					intent.setClass(MajorActivity.this,
							SearchResultActivity.class);
					startActivity(intent);
				}
				return false;
			}
		});

		ImageView imageView = (ImageView) layout.findViewById(R.id.imageViewAd);
		imageView.setLayoutParams(new LayoutParams(getScreenWidth(),
				ConstantParams.SIZE_ROW * 2));

		ScrollView scrollView = (ScrollView) layout
				.findViewById(R.id.scrollView_major);
		if (classList != null && !"".equals(classList) && classList.size() > 0) {
			TableLayout tableLayout = new TableLayout(this);
			
			tableLayout.setColumnStretchable(0, false);
			tableLayout.setColumnStretchable(1, false);
			tableLayout.setColumnShrinkable(0, false);
			tableLayout.setColumnShrinkable(1, false);

			LayoutParams layoutParams1 = new LayoutParams(0);
			layoutParams1.setMargins(2, 2, 2, 2);
			layoutParams1.gravity = Gravity.CENTER_VERTICAL;
			layoutParams1.weight=1;
			layoutParams1.width = 0;
			LayoutParams layoutParams2 = new LayoutParams(1);
			layoutParams2.setMargins(2, 2, 2, 2);
			layoutParams2.weight = 1;
			layoutParams2.width = 0;
			layoutParams2.gravity = Gravity.CENTER_VERTICAL;
			for (int i = 0, len = classList.size(); i < len;) {
				// 在循环中遍历所有专业名称进行添加
				TableRow row = new TableRow(this);

				final BookClass bookClass1 = classList.get(i);

				Button button1 = new Button(this);
				Button button2 = new Button(this);
				button1.setSingleLine(false);
//				button1.setWidth(ConstantParams.SIZE_MAJOR_BTN_WIDTH);
//				button1.setWidth(0);
				button1.setTextSize(ConstantParams.SIZE_TOP_TEXT );
				button1.setTextColor(Color.WHITE);
				button1.setBackgroundResource(R.drawable.button_style);
				button1.setText(bookClass1.getClassName());
				button1.setHeight(ConstantParams.SIZE_ROW);
				button1.setPadding(2, 1, 2, 1);
				button1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(MajorActivity.this, BookActivity.class);
						ConstantParams.CURRENT_CLASS_NAME = bookClass1.getClassName();
						intent.putExtra(ConstantParams.FIELD_CLASS_ID,
								bookClass1.getClassId());
						startActivity(intent);
					}
				});
				row.addView(button1, layoutParams1);
				if (i + 1 < len) {
					final BookClass bookClass2 = classList.get(i + 1);
					button2.setSingleLine(false);
					button2.setTextColor(Color.WHITE);
					button2.setBackgroundResource(R.drawable.button_style);
//					button2.setWidth(ConstantParams.SIZE_MAJOR_BTN_WIDTH);
					button2.setHeight(ConstantParams.SIZE_ROW);
					button2.setTextSize(ConstantParams.SIZE_TOP_TEXT );
					button2.setText(bookClass2.getClassName());
					button2.setPadding(2, 1, 2, 1);
					button2.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setClass(MajorActivity.this,
									BookActivity.class);
							ConstantParams.CURRENT_CLASS_NAME = bookClass2.getClassName();
							intent.putExtra(ConstantParams.FIELD_CLASS_ID,
									bookClass2.getClassId());
							startActivity(intent);
						}
					});
					row.addView(button2, layoutParams2);
				}
				tableLayout.addView(row);
				i = i + 2;
			}
			scrollView.addView(tableLayout);
		}
		
		// scrollView.addView(layout);
		this.setContentView(layout);
		
		txNoResult = (TextView) findViewById(R.id.tv_major_noresult);
		txNoResult.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
		if (classList != null && !"".equals(classList) && classList.size() > 0) {
			txNoResult.setVisibility(View.GONE);
		} else {
			txNoResult.setVisibility(View.VISIBLE);
		}
		
		
	}

	@SuppressLint("NewApi")
	private class MajorAsynTask extends AsyncTask<Void, Void, Boolean> {
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			turnDialog.show();
		}
		
		@Override
		protected Boolean doInBackground(Void... arg0) {
			Intent intent = getIntent();
			int cid = intent.getIntExtra(ConstantParams.FIELD_CATEGORY_ID, 0);
			// int cid = 1;
			if (cid != 0) {
				ConstantParams.CURRENT_CATEGORY_ID = cid;
				String jsonString = null;
				String url = ConstantParams.URL_GET_CLASS + "&"
						+ ConstantParams.FIELD_TELEPHONE + "="
						+ ConstantParams.CURRENT_TELEPHONE + "&"
						+ ConstantParams.FIELD_MAC_ADDRESS + "="
						+ ConstantParams.CURRENT_MACADDRESS + "&"
						+ ConstantParams.FIELD_CATEGORY_ID + "="
						+ ConstantParams.CURRENT_CATEGORY_ID;
				try {
					jsonString = HttpHelper.sendGetMessage(url, "utf-8");
					Log.i("PDF", "major  : " + jsonString);
					Log.i("PDF", "major  : " + url);
					classList = FastjsonTools.getContentListPojos(jsonString,
							BookClass.class);

					// String str =
					// "{\"content\":[{\"categoryId\":1,\"classId\":1,\"className\":\"规划\"},{\"categoryId\":1,\"classId\":2,\"className\":\"建筑\"},{\"categoryId\":1,\"classId\":3,\"className\":\"结构\"},{\"categoryId\":1,\"classId\":4,\"className\":\"暖通\"},{\"categoryId\":1,\"classId\":5,\"className\":\"其他综合\"},{\"categoryId\":1,\"classId\":16,\"className\":\"电气\"},{\"categoryId\":1,\"classId\":17,\"className\":\"装修\"},{\"categoryId\":1,\"classId\":18,\"className\":\"标准\"},{\"categoryId\":1,\"classId\":19,\"className\":\"下水道管理\"},{\"categoryId\":1,\"classId\":20,\"className\":\"通信\"},{\"categoryId\":1,\"classId\":21,\"className\":\"制造\"},{\"categoryId\":1,\"classId\":22,\"className\":\"专业化\"},{\"categoryId\":1,\"classId\":23,\"className\":\"电焊\"},{\"categoryId\":1,\"classId\":24,\"className\":\"这\"}],\"header\":{\"code\":\"1\",\"msg\":\"SUCCESS\"}}";
					// classList = FastjsonTools.getContentListPojos(str,
					// BookClass.class);
					Log.i("PDF", classList.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					classList = null;
				}
				
			} else {
				ConstantParams.CURRENT_CATEGORY_ID = 0;
				classList = null;
			}
			return true;
		}

		public MajorAsynTask() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPostExecute(Boolean result) {
			turnDialog.dismiss();
			initLayout();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ApplicationManager.remove(this);
	}
}
