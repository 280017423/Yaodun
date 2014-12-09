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
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qianjiang.framework.imageloader.core.DisplayImageOptions;
import com.qianjiang.framework.imageloader.core.ImageLoader;
import com.qianjiang.framework.imageloader.core.display.SimpleBitmapDisplayer;
import com.qianjiang.framework.util.StringUtil;
import com.yaodun.app.R;
import com.yaodun.app.model.DoctorModel;

/**
 * 药师适配器
 * 
 * @author zou.sq
 * @since 2013-03-12 下午04:37:29
 * @version 1.0
 */
public class DoctorAdapter extends BaseAdapter {
	private List<DoctorModel> mVedioModels;
	private Context mContext;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;

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
	public DoctorAdapter(Activity context, List<DoctorModel> dataList, ImageLoader loader) {
		this.mContext = context;
		this.mVedioModels = dataList;
		this.mImageLoader = loader;
		mOptions = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc()
				.displayer(new SimpleBitmapDisplayer()).build();
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
	}

	@Override
	public int getCount() {
		if (mVedioModels != null && !mVedioModels.isEmpty()) {
			return mVedioModels.size();
		}
		return 0;
	}

	@Override
	public DoctorModel getItem(int position) {
		if (mVedioModels != null) {
			return mVedioModels.get(position);
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
			convertView.setTag(holder);
		} else {
			holder = (viewHode) convertView.getTag();
		}

		DoctorModel model = getItem(position);
		String imgUrl = model.getImg();
		if (!StringUtil.isNullOrEmpty(imgUrl)) {
			mImageLoader.displayImage(imgUrl, holder.mIvImg, mOptions);
		}
		String name = model.getDoctorName();
		if (StringUtil.isNullOrEmpty(name)) {
			holder.mTvName.setVisibility(View.GONE);
		} else {
			holder.mTvName.setVisibility(View.VISIBLE);
			holder.mTvName.setText(name);
		}
		holder.mTvProfessional.setText(model.getProfessional());
		holder.mTvDescription.setText(model.getDescription());

		return convertView;
	}

	class viewHode {
		ImageView mIvImg;
		TextView mTvName;
		TextView mTvProfessional;
		TextView mTvDescription;
	}
}
