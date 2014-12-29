package com.yaodun.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.yaodun.app.R;

/**
 * YaodunActivityGroup界面
 * 
 * @author zou.sq
 */
public class YaodunActivityGroup extends YaodunActivityBase {

	RelativeLayout mRlContainer;
	Intent classifyIntent;
	View classifyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yaodun);
		initView();
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		mRlContainer = (RelativeLayout) findViewById(R.id.layout_container);
		classifyIntent = new Intent(this, YaodunClassifyActivity.class);
		classifyView = getLocalActivityManager().startActivity(classifyIntent.getComponent().getShortClassName(),
				classifyIntent).getDecorView();

		mRlContainer.removeAllViews();
		mRlContainer.addView(classifyView, new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

}
