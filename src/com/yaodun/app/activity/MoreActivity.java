package com.yaodun.app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yaodun.app.R;
import com.yaodun.app.manager.UserMgr;

/**
 * 更多界面
 * 
 * @author zou.sq
 */
public class MoreActivity extends YaodunActivityBase implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		initView();
	}

	private void initView() {
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.text_more);
		LinearLayout llRight = (LinearLayout) findViewById(R.id.title_with_back_title_btn_right);
		llRight.setOnClickListener(this);
		TextView btnRight = (TextView) findViewById(R.id.tv_title_with_right);
		btnRight.setText(R.string.button_logout);
		btnRight.setBackgroundResource(R.drawable.btn_logout_selector);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_more_collect:
			case R.id.rl_more_share:
			case R.id.rl_more_introduce:
			case R.id.rl_more_about:
			case R.id.rl_more_suggest:
			case R.id.rl_more_declare:
			case R.id.rl_more_check_version:
				Toast.makeText(MoreActivity.this, "正在开发中...", Toast.LENGTH_LONG).show();
				break;
			case R.id.title_with_back_title_btn_right:
				logout();
				break;
			default:
				break;
		}
	}

	private void logout() {
		UserMgr.logout();
		MainActivityGroup activityGroup = (MainActivityGroup) getParent();
		activityGroup.initStartIndex();
	}

}
