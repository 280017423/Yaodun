package com.pregnancy.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pregnancy.app.R;
import com.pregnancy.app.model.MedicineCheckRuleBean;

public class DetectRuleAdapter extends BaseAdapter {
	Context context;
	List<MedicineCheckRuleBean> list;

	public DetectRuleAdapter(Context context, List<MedicineCheckRuleBean> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_detect_rule, null);
		}
		MedicineCheckRuleBean item = list.get(position);

		TextView tv1 = (TextView) convertView.findViewById(R.id.tv1);
		tv1.setText(item.title);
		TextView tv2 = (TextView) convertView.findViewById(R.id.tv2);
		tv2.setText(item.data);
		return convertView;
	}
}
