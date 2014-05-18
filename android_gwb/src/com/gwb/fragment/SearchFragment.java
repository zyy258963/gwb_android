package com.gwb.fragment;

import com.artifex.mupdfdemo.R;
import com.gwb.activity.SearchResultActivity;
import com.gwb.utils.ConstantParams;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SearchFragment extends Fragment{
	private Button btnSearch = null;
	private EditText editTextSearch = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_search, container, false);
		
//		btnSearch = (Button) v.findViewById(R.id.btn_search);
		editTextSearch = (EditText) v.findViewById(R.id.editText_search);

//		btnSearch.setHeight(ConstantParams.SIZE_MENU_BTN_HEIGHT);
//		btnSearch.setTextSize(ConstantParams.SIZE_MENU_BTN_TEXT);

		editTextSearch.setTextSize(ConstantParams.SIZE_MENU_EDIT_TEXT);
		editTextSearch.setHeight(ConstantParams.SIZE_MENU_BTN_HEIGHT);
//		editTextSearch.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				if (MotionEvent.ACTION_DOWN == event.getAction()) {
//					Intent intent = new Intent();
//					intent.setClass(getActivity(), SearchResultActivity.class);
//					startActivity(intent);
//				}
//				return true;
//			}
//		});
		
		return v;
	}
}
