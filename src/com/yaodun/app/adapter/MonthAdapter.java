package com.yaodun.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yaodun.app.R;

public class MonthAdapter extends BaseAdapter {
	String[] list;
	private Context mContext;

	public MonthAdapter(Context context) {
		mContext = context;
		this.list = context.getResources().getStringArray(R.array.month);
	}

	@Override
	public int getCount() {
		return list.length;
	}

	@Override
	public String getItem(int position) {
		return list[position];
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHode holder = new ViewHode();
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.view_month_item, null);
			holder.mTvMonth = (TextView) convertView.findViewById(R.id.tv_month_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHode) convertView.getTag();
		}
		String month = list[position];
		holder.mTvMonth.setText(month);
		return convertView;
	}

	class ViewHode {
		TextView mTvMonth;
	}
}
