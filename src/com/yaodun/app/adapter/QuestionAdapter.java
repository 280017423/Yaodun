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
import com.yaodun.app.model.QuestionModel;

/**
 * 视频列表适配器
 * 
 * @author zou.sq
 * @since 2013-03-12 下午04:37:29
 * @version 1.0
 */
public class QuestionAdapter extends BaseAdapter {
	private List<QuestionModel> mQuestionDetailModels;
	private Context mContext;

	/**
	 * 实例化对象
	 * 
	 * @param context
	 *            上下文
	 * @param dataList
	 *            数据列表
	 */
	public QuestionAdapter(Activity context, List<QuestionModel> dataList) {
		this.mContext = context;
		this.mQuestionDetailModels = dataList;
	}

	@Override
	public int getCount() {
		if (mQuestionDetailModels != null && !mQuestionDetailModels.isEmpty()) {
			return mQuestionDetailModels.size();
		}
		return 0;
	}

	@Override
	public QuestionModel getItem(int position) {
		if (mQuestionDetailModels != null) {
			return mQuestionDetailModels.get(position);
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
			convertView = View.inflate(mContext, R.layout.view_question_detail_item, null);
			holder.mTvContent = (TextView) convertView.findViewById(R.id.tv_question_content);
			holder.mTvDate = (TextView) convertView.findViewById(R.id.tv_question_date);
			convertView.setTag(holder);
		} else {
			holder = (viewHode) convertView.getTag();
		}
		QuestionModel model = getItem(position);
		holder.mTvContent.setText(model.getDescription());
		holder.mTvDate.setText(model.getDisplayTime());

		return convertView;
	}

	class viewHode {
		TextView mTvContent;
		TextView mTvDate;
	}
}
