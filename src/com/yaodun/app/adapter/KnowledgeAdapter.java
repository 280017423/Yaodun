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
import android.widget.TextView;

import com.qianjiang.framework.imageloader.core.DisplayImageOptions;
import com.qianjiang.framework.imageloader.core.ImageLoader;
import com.qianjiang.framework.imageloader.core.display.SimpleBitmapDisplayer;
import com.qianjiang.framework.util.StringUtil;
import com.yaodun.app.R;
import com.yaodun.app.model.KnowledgeModel;

/**
 * 文章列表适配器
 * 
 * @author zou.sq
 * @since 2013-03-12 下午04:37:29
 * @version 1.0
 */
public class KnowledgeAdapter extends BaseAdapter {
	private static final String[] TITLE = new String[] { "综合", "妇女", "儿童", "老人", "抗生素" };
	private List<KnowledgeModel> mKnowledgeModels;
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
	public KnowledgeAdapter(Activity context, List<KnowledgeModel> dataList, ImageLoader loader) {
		this.mContext = context;
		this.mKnowledgeModels = dataList;
		this.mImageLoader = loader;
		mOptions = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc()
				.displayer(new SimpleBitmapDisplayer()).build();
	}

	@Override
	public int getCount() {
		if (mKnowledgeModels != null && !mKnowledgeModels.isEmpty()) {
			return mKnowledgeModels.size();
		}
		return 0;
	}

	@Override
	public KnowledgeModel getItem(int position) {
		if (mKnowledgeModels != null) {
			return mKnowledgeModels.get(position);
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
			convertView = View.inflate(mContext, R.layout.view_knowledge_item, null);
			holder.mIvImg = (ImageView) convertView.findViewById(R.id.iv_knowledge_img);
			holder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_knowledge_title);
			holder.mTvType = (TextView) convertView.findViewById(R.id.tv_knowledge_type);
			holder.mTvDate = (TextView) convertView.findViewById(R.id.tv_knowledge_date);
			holder.mTvCount = (TextView) convertView.findViewById(R.id.tv_knowledge_count);
			holder.mViewCount = convertView.findViewById(R.id.layout_count);
			convertView.setTag(holder);
		} else {
			holder = (viewHode) convertView.getTag();
		}

		KnowledgeModel vedioModel = getItem(position);
		String imgUrl = vedioModel.getImg();
		if (!StringUtil.isNullOrEmpty(imgUrl)) {
			mImageLoader.displayImage(imgUrl, holder.mIvImg, mOptions);
		}
		String title = vedioModel.getTitle();
		if (StringUtil.isNullOrEmpty(title)) {
			holder.mTvTitle.setVisibility(View.GONE);
		} else {
			holder.mTvTitle.setVisibility(View.VISIBLE);
			holder.mTvTitle.setText(title);
		}
		int type = vedioModel.getType();
		holder.mTvType.setText(TITLE[type - 1]);
		String date = vedioModel.getDisplayTime();
		holder.mTvDate.setText(date);
		int count = vedioModel.getCountDiscuss();
		if (0 == count) {
			holder.mViewCount.setVisibility(View.INVISIBLE);
		} else {
			holder.mViewCount.setVisibility(View.VISIBLE);
			holder.mTvCount.setText("" + count);
		}

		return convertView;
	}

	class viewHode {
		ImageView mIvImg;
		TextView mTvTitle;
		TextView mTvType;
		TextView mTvDate;
		TextView mTvCount;
		View mViewCount;
	}
}
