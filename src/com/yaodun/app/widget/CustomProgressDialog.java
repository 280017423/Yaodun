package com.yaodun.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yaodun.app.R;

/**
 * 自定义加载对话框
 * 
 * @author zou.sq
 * 
 */
public class CustomProgressDialog extends Dialog {
	public enum DIALOG_DEFAULT_LAYOUT_TYPE {
		FORCE_UPDATE, EMPTY
	}

	private ProgressBar mProgressBar;
	private TextView mTvProgressText;

	public CustomProgressDialog(Activity activity) {
		super(activity);
	}

	/**
	 * 
	 * @param activity
	 *            上下文对象
	 * @param updateType
	 *            更新的类型，暂时只有一个DEFAULT
	 */
	public CustomProgressDialog(Activity activity, DIALOG_DEFAULT_LAYOUT_TYPE layoutType) {
		this(activity, R.style.Dialog);
		switch (layoutType) {
		// 强制升级的背景
			case FORCE_UPDATE:
				initForceUpdateLayout();
				break;
			// 默认为空背景
			default:
				break;
		}
	}

	public CustomProgressDialog(Activity activity, int theme) {
		super(activity, theme);
	}

	private void initForceUpdateLayout() {
		setCancelable(false);
		setCanceledOnTouchOutside(false);
		setContentView(R.layout.force_update_layout);
		mProgressBar = (ProgressBar) findViewById(R.id.progress);
		mTvProgressText = (TextView) findViewById(R.id.tv_dialog_layout_msg);
	}

	public ProgressBar getProgressBar() {
		return mProgressBar;
	}

	public TextView getProgressText() {
		return mTvProgressText;
	}

	public void setProgressMax() {

	}
}
