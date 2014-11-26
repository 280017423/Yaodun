package com.yaodun.app.util;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.yaodun.app.listener.OnPopDismissListener;

/**
 * PoPWindow 弹出窗口控件
 * 
 * @author zou.sq
 * 
 */
public class PopWindowUtil {
	private View mMenuView;
	private PopupWindow mPopupWindow;
	private View mView;
	private OnPopDismissListener mListener;

	/**
	 * @param menuView
	 *            需要显示的view
	 * @param view
	 *            PopWindow相对位置的视图
	 * @param listener
	 *            PopWindow消失监听
	 */
	public PopWindowUtil(View menuView, View view, OnPopDismissListener listener) {
		this.mMenuView = menuView;
		mView = view;
		mListener = listener;
		initView();
	}

	/**
	 * 初始化
	 */
	private void initView() {
		mPopupWindow = new PopupWindow(mMenuView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mPopupWindow.setContentView(mMenuView);
		mPopupWindow.setFocusable(true);
		// 点击popupwindow窗口之外的区域popupwindow消失
		ColorDrawable dw = new ColorDrawable(0x00);
		mPopupWindow.setBackgroundDrawable(dw);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				if (null != mListener) {
					mListener.onDismiss();
				}
			}
		});
	}

	/**
	 * 用来显示和关闭PopWindow
	 */
	public void changeStatus() {
		if (null == mPopupWindow) {
			return;
		}
		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		} else {
			mPopupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
		}
	}

	/**
	 * 消失Pop
	 */
	public void dissmiss() {
		if (null == mPopupWindow) {
			return;
		}
		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

	/**
	 * 显示Pop
	 */
	public void show() {
		if (null == mPopupWindow) {
			return;
		}
		if (!mPopupWindow.isShowing()) {
			mPopupWindow.showAsDropDown(mView);
		}
	}

}