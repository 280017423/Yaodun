package com.yaodun.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.qianjiang.framework.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjiang.framework.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qianjiang.framework.widget.pulltorefresh.PullToRefreshListView;
import com.yaodun.app.R;
import com.yaodun.app.adapter.DoctorAdapter;
import com.yaodun.app.authentication.ActionProcessor;
import com.yaodun.app.authentication.ActionResult;
import com.yaodun.app.listener.IActionListener;
import com.yaodun.app.model.DoctorModel;
import com.yaodun.app.req.DoctorReq;
import com.yaodun.app.util.ConstantSet;

/**
 * 药师咨询界面
 * 
 * @author zou.sq
 */
public class MyAttentionDoctorsActivity extends YaodunActivityBase implements OnClickListener, OnItemClickListener {

	private static final int GET_DATA_SUCCESSED = 0;
	private static final int GET_DATA_FAIL = 1;
	private static final int PULL_DOWN = 0;
	private static final int PULL_UP = 1;
	private View mEmptyView;
	private PullToRefreshListView mPullToRefreshListView;
	private int mPage;
	private boolean mIsGettingData;
	private List<DoctorModel> mDoctorModels;
	private DoctorAdapter mDoctorAdapter;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			mPullToRefreshListView.onRefreshComplete();
			ActionResult result = (ActionResult) msg.obj;
			if (result != null) {
				switch (msg.what) {
					case GET_DATA_SUCCESSED:
						List<DoctorModel> moreList = (List<DoctorModel>) result.ResultObject;
						if (moreList != null) {
							if (PULL_DOWN == msg.arg1) {
								mDoctorModels.clear();
								mDoctorModels.addAll(0, moreList);
							} else {
								mPage++;
								mDoctorModels.addAll(moreList);
							}
							if (moreList.size() < ConstantSet.INFO_NUM_IN_ONE_PAGE) {
								mPullToRefreshListView.setNoMoreData();
								mPullToRefreshListView.onRefreshComplete(false);
							} else {
								mPullToRefreshListView.onRefreshComplete(true);
							}
							mPullToRefreshListView.setMode(Mode.BOTH);
						}
						if (mDoctorAdapter != null) {
							mDoctorAdapter.notifyDataSetChanged();
						}
						break;
					case GET_DATA_FAIL:
						showErrorMsg((ActionResult) result);
						break;
					default:
						break;
				}
			}
			mIsGettingData = false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_consult);
		initVariable();
		initView();
	}

	private void initVariable() {
		mDoctorModels = new ArrayList<DoctorModel>();
		mDoctorAdapter = new DoctorAdapter(this, mDoctorModels, mImageLoader);
	}

	private void initView() {
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.text_my_attention);

		mEmptyView = View.inflate(this, R.layout.view_empty_layout, null);
		TextView tvEmptyToast = (TextView) mEmptyView.findViewById(R.id.tv_empty_content);
		tvEmptyToast.setText(R.string.tips_doctor_consule_empty_view);

		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_doctor_consult);
		ListView listView = mPullToRefreshListView.getRefreshableView();
		listView.setCacheColorHint(getResources().getColor(R.color.transparent));
		listView.setFadingEdgeLength(0);
		listView.setAdapter(mDoctorAdapter);
		listView.setOnItemClickListener(this);
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2() {
			@Override
			public void onPullDownToRefresh() {
				mPage = 1;
				getDoctorModels(PULL_DOWN);
			}

			@Override
			public void onPullUpToRefresh() {
				getDoctorModels(PULL_UP);
			}
		});
		mPullToRefreshListView.setHeaderVisible(true);
		mPullToRefreshListView.setIsShowHeaderFresh(true);
		mPullToRefreshListView.setMode(Mode.BOTH);
	}

	private void getDoctorModels(final int pullStatus) {
		if (mIsGettingData) {
			return;
		}
		mIsGettingData = true;
		new ActionProcessor().startAction(MyAttentionDoctorsActivity.this, new IActionListener() {

			@Override
			public void onSuccess(ActionResult result) {
				sendHandler(GET_DATA_SUCCESSED, pullStatus, result);
			}

			@Override
			public void onError(ActionResult result) {
				sendHandler(GET_DATA_FAIL, pullStatus, result);
			}

			@Override
			public ActionResult onAsyncRun() {
				return DoctorReq.getAttentionDoctorList(mPage);
			}
		});
	}

	private void sendHandler(int what, int status, ActionResult result) {
		Message msg = mHandler.obtainMessage();
		msg.what = what;
		msg.arg1 = status;
		msg.obj = result;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			default:
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		DoctorModel model = (DoctorModel) parent.getAdapter().getItem(position);
		if (null != model) {
			Intent intent = new Intent(this, MyDoctorDetailActivity.class);
			intent.putExtra(ConstantSet.EXTRA_DOCTORMODEL, model);
			startActivity(intent);
		}
	}

}
