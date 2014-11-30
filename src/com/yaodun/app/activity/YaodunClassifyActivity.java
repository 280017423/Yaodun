package com.yaodun.app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.yaodun.app.R;

/**
 * 药盾-分类
 * 
 * @author zou.sq
 */
public class YaodunClassifyActivity extends YaodunActivityBase implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yaodun_classify);
		initView();
	}

	private void initView() {
		View btnPeople = findViewById(R.id.iv_people);
		btnPeople.setOnClickListener(this);
		View layoutKid = findViewById(R.id.layout_kid);
		layoutKid.setOnClickListener(this);
		View layoutPregnant = findViewById(R.id.layout_pregnant);
		layoutPregnant.setOnClickListener(this);
		View layoutOld = findViewById(R.id.layout_old);
		layoutOld.setOnClickListener(this);
		View layoutRepeat = findViewById(R.id.layout_repeat);
		layoutRepeat.setOnClickListener(this);
		View layoutAvoid = findViewById(R.id.layout_avoid);
		layoutAvoid.setOnClickListener(this);
		View layoutAntibiotic = findViewById(R.id.layout_antibiotic);
		layoutAntibiotic.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_people:
				break;
			default:
				break;
		}
		goSearch();
	}

	void goSearch() {
		YaodunActivityGroup parent = (YaodunActivityGroup) getParent();
		parent.goSearch();
	}
}
