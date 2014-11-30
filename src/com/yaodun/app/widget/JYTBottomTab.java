package com.yaodun.app.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;

import com.qianjiang.framework.model.BottomItem;
import com.qianjiang.framework.widget.BottomTab;
import com.yaodun.app.R;
import com.yaodun.app.app.ZouApplication;

/**
 * 自定义TabItem
 * 
 * @author zou.sq
 */
public class JYTBottomTab extends BottomTab {

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文对象
	 */
	public JYTBottomTab(Context context) {
		super(context);
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文对象
	 * @param attrs
	 *            属性集
	 */
	public JYTBottomTab(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected List<BottomItem> initBottomItems() {
		List<BottomItem> bottomItems = new ArrayList<BottomItem>();
		bottomItems.add(new BottomItem(R.drawable.yaodun_icon_selector, ZouApplication.CONTEXT
				.getString(R.string.bottom_tab_yaodun)));
		bottomItems.add(new BottomItem(R.drawable.medicine_knowledge_icon_selector, ZouApplication.CONTEXT
				.getString(R.string.bottom_tab_medicine_knowledge)));
		bottomItems.add(new BottomItem(R.drawable.doctor_consult_icon_selector, ZouApplication.CONTEXT
				.getString(R.string.text_doctor_consult)));
		bottomItems.add(new BottomItem(R.drawable.more_icon_selector, ZouApplication.CONTEXT
				.getString(R.string.bottom_tab_more)));
		return bottomItems;
	}

	@Override
	protected int getBackGroupRes() {
		return R.drawable.tab_normal_bg;
	}

	@Override
	protected int getSelectedViewRes() {
		return R.drawable.tab_selecte_bg;
	}

	@Override
	protected int getSelectedTextColorRes() {
		return R.color.tv_tab_selecte_color;
	}

	@Override
	protected int getNormalTextColorRes() {
		return R.color.tv_tab_normal_color;
	}

}
