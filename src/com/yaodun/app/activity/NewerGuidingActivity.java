package com.yaodun.app.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;

import com.qianjiang.framework.util.HttpClientUtil;
import com.yaodun.app.R;
import com.yaodun.app.adapter.NewerAdapter;
import com.yaodun.app.widget.AutoScrollViewPager;
import com.yaodun.app.widget.CircleFlowIndicator;

/**
 * 新手引导界面
 * 
 * @author zou.sq
 */
public class NewerGuidingActivity extends YaodunActivityBase {
	private static final int DIALOG_EXIT_APP = 1;
	private static final int COUNT = 3;

	private int mCurrentPosition;
	private AutoScrollViewPager mViewPager;
	private CircleFlowIndicator mCircleFlowIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newer_guiding);
		initVariables();
		findView();
	}

	private void findView() {
		mViewPager = (AutoScrollViewPager) findViewById(R.id.vp_newer_guide);
		mCircleFlowIndicator = (CircleFlowIndicator) findViewById(R.id.cfi_indicator);
		mCircleFlowIndicator.setCount(COUNT);
		mViewPager.setAdapter(new NewerAdapter(this));
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mCurrentPosition = position;
				mCircleFlowIndicator.setSeletion(position % COUNT);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private void initVariables() {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (mCurrentPosition <= 0) {
				showDialog(DIALOG_EXIT_APP);
			} else {
				mViewPager.setCurrentItem(mCurrentPosition - 1, true);
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_EXIT_APP:
				return createDialogBuilder(this, getString(R.string.button_text_tips),
						getString(R.string.exit_dialog_title), getString(R.string.button_text_no),
						getString(R.string.button_text_yes)).create(id);
			default:
				break;
		}
		return super.onCreateDialog(id);
	}

	@Override
	public void onNegativeBtnClick(int id, DialogInterface dialog, int which) {
		switch (id) {
			case DIALOG_EXIT_APP:
				HttpClientUtil.setCookieStore(null);
				finish();
				break;
			default:
				break;
		}
	}
}
