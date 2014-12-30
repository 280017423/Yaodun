package com.yaodun.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.yaodun.app.R;
import com.yaodun.app.model.QueryType;
import com.yaodun.app.util.ConstantSet;

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
	}

	@Override
	public void onClick(View v) {
		int queryType = 0;
		switch (v.getId()) {
			case R.id.iv_people:
				queryType = QueryType.medicine_dazhong;
				break;
			case R.id.layout_kid:
				queryType = QueryType.medicine_ertong;
				break;
			case R.id.layout_avoid:
				queryType = QueryType.medicine_jinji;
				break;
			case R.id.layout_antibiotic:
				queryType = QueryType.medicine_kangshengsu;
				break;
			case R.id.layout_old:
				queryType = QueryType.medicine_laoren;
				break;
			case R.id.layout_repeat:
				queryType = QueryType.medicine_chongfu;
				break;
			case R.id.layout_pregnant:
				queryType = QueryType.medicine_yunfu;
				break;
			default:
				break;
		}
		goSearch(queryType);
	}

	private void goSearch(int queryType) {
		Intent intent = new Intent(this, YaodunSearchActivity.class);
		intent.putExtra(ConstantSet.EXTRA_JUMP_TYPE, queryType);
		startActivity(intent);
	}
}
