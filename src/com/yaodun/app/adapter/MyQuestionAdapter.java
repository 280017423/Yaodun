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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianjiang.framework.imageloader.core.DisplayImageOptions;
import com.qianjiang.framework.imageloader.core.DisplayImageOptions.Builder;
import com.qianjiang.framework.imageloader.core.ImageLoader;
import com.qianjiang.framework.imageloader.core.display.RoundedBitmapDisplayer;
import com.qianjiang.framework.util.StringUtil;
import com.yaodun.app.R;
import com.yaodun.app.model.MyQuestionModel;

/**
 * 我的咨询列表适配器
 * 
 * @author zou.sq
 * @since 2013-03-12 下午04:37:29
 * @version 1.0
 */
public class MyQuestionAdapter extends BaseAdapter {
	private List<MyQuestionModel> mQuestionDetailModels;
	private static final int ROUND_PIXELS = 30;
	private Context mContext;
	private ImageLoader mImageLoader;
	private Builder mOptions;
	private String mImgUrl;

	/**
	 * 实例化对象
	 * 
	 * @param context
	 *            上下文
	 * @param dataList
	 *            数据列表
	 */
	public MyQuestionAdapter(Activity context, List<MyQuestionModel> dataList, ImageLoader imageLoader) {
		this.mContext = context;
		this.mQuestionDetailModels = dataList;
		this.mImageLoader = imageLoader;
		mOptions = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(mContext.getResources().getColor(R.color.white), ROUND_PIXELS));
	}

	public void setImgUrl(String imgUrl) {
		mImgUrl = imgUrl;
	}

	@Override
	public int getCount() {
		if (mQuestionDetailModels != null && !mQuestionDetailModels.isEmpty()) {
			return mQuestionDetailModels.size();
		}
		return 0;
	}

	@Override
	public MyQuestionModel getItem(int position) {
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
			convertView = View.inflate(mContext, R.layout.view_my_question_detail_item, null);
			holder.mLayoutIn = (RelativeLayout) convertView.findViewById(R.id.view_teacher_message_in);
			holder.mLayoutOut = (RelativeLayout) convertView.findViewById(R.id.view_teacher_message_out);
			holder.mTvContentIn = (TextView) convertView.findViewById(R.id.tv_question_content_in);
			holder.mTvDateIn = (TextView) convertView.findViewById(R.id.tv_question_date_in);
			holder.mIvIconIn = (ImageView) convertView.findViewById(R.id.iv_doctor_img_in);
			holder.mTvContentOut = (TextView) convertView.findViewById(R.id.tv_question_content_out);
			holder.mTvDateOut = (TextView) convertView.findViewById(R.id.tv_question_date_out);
			convertView.setTag(holder);
		} else {
			holder = (viewHode) convertView.getTag();
		}
		MyQuestionModel model = getItem(position);
		String time = model.getDisplayTime();
		// 1表示是药师,0:表示是用户
		if (1 == model.getUserType()) {
			holder.mLayoutIn.setVisibility(View.VISIBLE);
			holder.mLayoutOut.setVisibility(View.GONE);
			holder.mTvContentIn.setText(model.getReply());
			holder.mIvIconIn.setVisibility(View.VISIBLE);
			if (StringUtil.isNullOrEmpty(time)) {
				holder.mTvDateIn.setVisibility(View.GONE);
			} else {
				holder.mTvDateIn.setVisibility(View.VISIBLE);
				holder.mTvDateIn.setText(time);
			}
			mImageLoader.displayImage(mImgUrl, holder.mIvIconIn, mOptions.showImageForEmptyUri(R.drawable.ic_launcher)
					.build());
		} else {
			holder.mLayoutIn.setVisibility(View.GONE);
			holder.mLayoutOut.setVisibility(View.VISIBLE);
			if (StringUtil.isNullOrEmpty(time)) {
				holder.mTvDateOut.setVisibility(View.GONE);
			} else {
				holder.mTvDateOut.setVisibility(View.VISIBLE);
				holder.mTvDateOut.setText(time);
			}
			holder.mTvContentOut.setText(model.getReply());
		}

		return convertView;
	}

	class viewHode {
		TextView mTvContentIn;
		TextView mTvDateIn;
		ImageView mIvIconIn;
		TextView mTvContentOut;
		TextView mTvDateOut;
		RelativeLayout mLayoutIn;
		RelativeLayout mLayoutOut;
	}
}
