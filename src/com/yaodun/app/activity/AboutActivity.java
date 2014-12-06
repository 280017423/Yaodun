package com.yaodun.app.activity;

import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yaodun.app.R;

/**
 * 关于界面
 * 
 * @author zou.sq
 */
public class AboutActivity extends YaodunActivityBase implements OnClickListener {

	private LinearLayout mLlBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initView();
		setListener();
	}

	private void setListener() {
		mLlBack.setOnClickListener(this);
	}

	private void initView() {
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.text_about);
		mLlBack = (LinearLayout) findViewById(R.id.title_with_back_title_btn_left);
		TextView mTvBack = (TextView) findViewById(R.id.tv_title_with_back_left);
		mTvBack.setBackgroundResource(R.drawable.btn_back_bg);
		TextView tvVersion = (TextView) findViewById(R.id.tv_version_code);
		String versionName = getString(R.string.unknow_version_name);
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			tvVersion.setTextColor(Color.RED);
		}
		tvVersion.setText(getString(R.string.text_version_code, versionName));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_with_back_title_btn_left:
				finish();
				break;

			default:
				break;
		}
	}
}
