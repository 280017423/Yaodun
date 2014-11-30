package com.yaodun.app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.yaodun.app.R;

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
			default:
				break;
		}
	}

}
