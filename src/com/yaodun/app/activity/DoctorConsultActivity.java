package com.yaodun.app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.qianjiang.framework.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjiang.framework.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qianjiang.framework.widget.pulltorefresh.PullToRefreshListView;
import com.yaodun.app.R;

/**
 * 药师咨询界面
 * 
 * @author zou.sq
 */
public class DoctorConsultActivity extends YaodunActivityBase implements OnClickListener {

	private View mEmptyView;
	private PullToRefreshListView mPullToRefreshListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_consult);
		initView();
	}

	private void initView() {
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.text_doctor_consult);

		mEmptyView = View.inflate(this, R.layout.view_empty_layout, null);
		TextView tvEmptyToast = (TextView) mEmptyView.findViewById(R.id.tv_empty_content);
		tvEmptyToast.setText(R.string.tips_doctor_consule_empty_view);

		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_doctor_consult);
		ListView listView = mPullToRefreshListView.getRefreshableView();
		listView.setCacheColorHint(getResources().getColor(R.color.transparent));
		listView.setFadingEdgeLength(0);
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2() {
			@Override
			public void onPullDownToRefresh() {
			}

			@Override
			public void onPullUpToRefresh() {
			}
		});
		mPullToRefreshListView.setHeaderVisible(true);
		mPullToRefreshListView.setMode(Mode.PULL_DOWN_TO_REFRESH);
		mPullToRefreshListView.setIsShowHeaderFresh(true);
		mPullToRefreshListView.setMode(Mode.BOTH);
		// listView.setAdapter(mHistoryMsgAdapter);
		// listView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			default:
				break;
		}
	}

}
