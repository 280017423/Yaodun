package com.yaodun.app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

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

			default:
				break;
		}
	}

}
