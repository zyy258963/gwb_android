package com.artifex.mupdfdemo;

import com.gwb.utils.ConstantParams;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ListView;

public class OutlineActivity extends ListActivity {
	OutlineItem mItems[];

	/** Called when the activity is first created. */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mItems = OutlineActivityData.get().items;
		setListAdapter(new OutlineAdapter(getLayoutInflater(), mItems));
		// Restore the position within the list from last viewing
		getListView().setSelection(OutlineActivityData.get().position);
		getListView().setDividerHeight(10);
		
//		Display d = getWindowManager().getDefaultDisplay();  //为获取屏幕宽、高  
        LayoutParams params = getWindow().getAttributes();  //获取对话框当前的参数值  
		params.height = (int)(ConstantParams.SCREEN_HEIGHT* 0.8);
		params.width = (int)(ConstantParams.SCREEN_WIDTH* 0.8);
		getWindow().setAttributes(params);
		getWindow().setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
		setResult(-1);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		OutlineActivityData.get().position = getListView()
				.getFirstVisiblePosition();
		setResult(mItems[position].page);
		finish();
	}
}
