/**
 * @Title: DishEmptyAdapter.java
 * @Project DCB
 * @Package com.pdw.dcb.ui.adapter
 * @Description: 沽清列表
 * @author zeng.ww
 * @date 2012-12-10 下午04:37:29
 * @version V1.0
 */
package com.yaodun.app.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yaodun.app.R;
import com.yaodun.app.model.ConsultListModel;

/**
 * 我的咨询适配器
 * 
 * @author zou.sq
 * @since 2013-03-12 下午04:37:29
 * @version 1.0
 */
public class ConsultAdapter extends BaseAdapter {
	private List<ConsultListModel> mConsultModels;
	private Context mContext;

	/**
	 * 实例化对象
	 * 
	 * @param context
	 *            上下文
	 * @param dataList
	 *            数据列表
	 * @param loader
	 *            图片加载器
	 */
	public ConsultAdapter(Activity context, List<ConsultListModel> dataList) {
		this.mContext = context;
		this.mConsultModels = dataList;
	}

	@Override
	public int getCount() {
		if (mConsultModels != null && !mConsultModels.isEmpty()) {
			return mConsultModels.size();
		}
		return 0;
	}

	@Override
	public ConsultListModel getItem(int position) {
		if (mConsultModels != null) {
			return mConsultModels.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHode holder = new viewHode();
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.view_consult_item, null);
			holder.mTvDescription = (TextView) convertView.findViewById(R.id.tv_description);
			holder.mTvDate = (TextView) convertView.findViewById(R.id.tv_replay_time);
			holder.mTvStatus = (TextView) convertView.findViewById(R.id.tv_statu);
			convertView.setTag(holder);
		} else {
			holder = (viewHode) convertView.getTag();
		}

		ConsultListModel model = getItem(position);
		holder.mTvDescription.setText(model.getDescription());
		holder.mTvDate.setText(model.getDisplayTime());
		int status = model.getReplyStatus();
		if (1 == status) {
			holder.mTvStatus.setText(R.string.has_reply);
			holder.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.consule_has_reply));
		} else {
			holder.mTvStatus.setText(R.string.has_not_reply);
			holder.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.consule_has_not_reply));
		}

		return convertView;
	}

	class viewHode {
		TextView mTvDescription;
		TextView mTvDate;
		TextView mTvStatus;
	}
}
