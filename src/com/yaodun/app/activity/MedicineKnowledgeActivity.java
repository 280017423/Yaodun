package com.yaodun.app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yaodun.app.R;
import com.yaodun.app.adapter.TabFragmentPagerAdapter;
import com.yaodun.app.widget.LineTabIndicator;

/**
 * 用药知识界面
 * 
 * @author zou.sq
 */
public class MedicineKnowledgeActivity extends FragmentActivity implements OnClickListener {

	private LineTabIndicator mLineTabIndicator;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_medicine_knowledge);
		initView();
	}

	private void initView() {
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.bottom_tab_medicine_knowledge);
		mLineTabIndicator = (LineTabIndicator) findViewById(R.id.line_tab_indicator);
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mViewPager.setAdapter(new TabFragmentPagerAdapter(this, getSupportFragmentManager()));
		mLineTabIndicator.setViewPager(mViewPager);
	}

	@Override
	public void onClick(View v) {

	}

}
