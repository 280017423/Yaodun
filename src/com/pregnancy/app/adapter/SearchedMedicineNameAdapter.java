package com.pregnancy.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pregnancy.app.R;
import com.pregnancy.app.model.MedicineBean;

public class SearchedMedicineNameAdapter extends BaseAdapter {
	Context context;
	List<MedicineBean> list;

	public SearchedMedicineNameAdapter(Context context, List<MedicineBean> list) {
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
			convertView = View.inflate(context, R.layout.item_medicine_name_simple, null);
		}
		MedicineBean item = list.get(position);
		TextView tvName = (TextView) convertView.findViewById(R.id.tv);
		tvName.setText(item.getDrugname());
		return convertView;
	}
}
