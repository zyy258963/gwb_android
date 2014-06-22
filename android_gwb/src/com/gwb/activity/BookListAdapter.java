package com.gwb.activity;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.artifex.mupdfdemo.R;
import com.gwb.activity.pojo.Books;
import com.gwb.utils.ConstantParams;

public class BookListAdapter extends BaseAdapter {

	private LayoutInflater myInflater;
	private List<Books> datas ;

	public BookListAdapter(Context context, List<Books> data) {
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
		if (contentView == null) {
			contentView = myInflater.inflate(R.layout.book_list, null);
		}
		TextView tv = (TextView) contentView
				.findViewById(R.id.book_list_item_name);
		tv.setTextSize(ConstantParams.SIZE_TOP_TEXT);
		tv.setText(datas.get(position).getBookName());
		tv.setHeight(ConstantParams.SIZE_ROW);
		return contentView;
	}

}