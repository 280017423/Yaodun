/**
 * @Title: DishEmptyAdapter.java
 * @Project DCB
 * @Package com.pdw.dcb.ui.adapter
 * @Description: 沽清列表
 * @author zeng.ww
 * @date 2012-12-10 下午04:37:29
 * @version V1.0
 */
package com.pregnancy.app.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pregnancy.app.R;
import com.pregnancy.app.model.DoctorModel;
import com.qianjiang.framework.imageloader.core.DisplayImageOptions;
import com.qianjiang.framework.imageloader.core.ImageLoader;
import com.qianjiang.framework.imageloader.core.display.SimpleBitmapDisplayer;
import com.qianjiang.framework.util.StringUtil;

/**
 * 药师适配器
 * 
 * @author zou.sq
 * @since 2013-03-12 下午04:37:29
 * @version 1.0
 */
public class MyDoctorAdapter extends BaseAdapter {
	private List<DoctorModel> mDoctorModels;
	private Context mContext;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private OnClickListener mListener;

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
	public MyDoctorAdapter(Activity context, List<DoctorModel> dataList, ImageLoader loader, OnClickListener listener) {
		this.mContext = context;
		this.mDoctorModels = dataList;
		this.mImageLoader = loader;
		mListener = listener;
		mOptions = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc()
				.displayer(new SimpleBitmapDisplayer()).build();
	}

	@Override
	public int getCount() {
		if (mDoctorModels != null && !mDoctorModels.isEmpty()) {
			return mDoctorModels.size();
		}
		return 0;
	}

	@Override
	public DoctorModel getItem(int position) {
		if (mDoctorModels != null) {
			return mDoctorModels.get(position);
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
			convertView = View.inflate(mContext, R.layout.view_doctor_item, null);
			holder.mIvImg = (ImageView) convertView.findViewById(R.id.iv_doctor_img);
			holder.mTvName = (TextView) convertView.findViewById(R.id.tv_doctor_name);
			holder.mTvProfessional = (TextView) convertView.findViewById(R.id.tv_doctor_professional);
			holder.mTvDescription = (TextView) convertView.findViewById(R.id.tv_doctor_description);
			holder.mBtnAttention = (Button) convertView.findViewById(R.id.btn_attention);
			convertView.setTag(holder);
		} else {
			holder = (viewHode) convertView.getTag();
		}
		holder.mBtnAttention.setVisibility(View.VISIBLE);
		DoctorModel model = getItem(position);
		if (null != mListener) {
			holder.mBtnAttention.setTag(model);
			holder.mBtnAttention.setOnClickListener(mListener);
		}
		String imgUrl = model.getImg();
		if (!StringUtil.isNullOrEmpty(imgUrl)) {
			mImageLoader.displayImage(imgUrl, holder.mIvImg, mOptions);
		}
		holder.mTvName.setText(model.getDoctorName());
		holder.mTvProfessional.setText(model.getProfessional());
		holder.mTvDescription.setText(model.getDescription());

		return convertView;
	}

	class viewHode {
		ImageView mIvImg;
		TextView mTvName;
		TextView mTvProfessional;
		TextView mTvDescription;
		Button mBtnAttention;
	}
}
