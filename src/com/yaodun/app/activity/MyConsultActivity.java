package com.yaodun.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import com.yaodun.app.adapter.ConsultAdapter;
import com.yaodun.app.authentication.ActionProcessor;
import com.yaodun.app.authentication.ActionResult;
import com.yaodun.app.listener.IActionListener;
import com.yaodun.app.model.ConsultListModel;
import com.yaodun.app.model.DoctorModel;
import com.yaodun.app.req.DoctorReq;
import com.yaodun.app.util.ConstantSet;

public class MyConsultActivity extends YaodunActivityBase implements OnClickListener, OnItemClickListener {
	private static final int REQUEST_CODE = 100;
	private static final int GET_DATA_SUCCESSED = 0;
	private static final int GET_DATA_FAIL = 1;
	private static final int PULL_DOWN = 0;
	private static final int PULL_UP = 1;
	private PullToRefreshListView mPullToRefreshListView;
	private ConsultAdapter mAdapter;
	private List<ConsultListModel> mKnowledgeModels;
	private int mPage;
	private boolean mIsGettingData;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			mPullToRefreshListView.onRefreshComplete();
			ActionResult result = (ActionResult) msg.obj;
			if (result != null) {
				switch (msg.what) {
					case GET_DATA_SUCCESSED:
						List<ConsultListModel> moreList = (List<ConsultListModel>) result.ResultObject;
						if (moreList != null) {
							if (PULL_DOWN == msg.arg1) {
								mKnowledgeModels.clear();
								mKnowledgeModels.addAll(0, moreList);
							} else {
								mPage++;
								mKnowledgeModels.addAll(moreList);
							}
							if (moreList.size() < ConstantSet.INFO_NUM_IN_ONE_PAGE) {
								mPullToRefreshListView.setNoMoreData();
								mPullToRefreshListView.onRefreshComplete(false);
							} else {
								mPullToRefreshListView.onRefreshComplete(true);
							}
							mPullToRefreshListView.setMode(Mode.BOTH);
						}
						if (mAdapter != null) {
							mAdapter.notifyDataSetChanged();
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
		setContentView(R.layout.activity_my_consult);
		initVariable();
		initView();
	}

	private void initVariable() {
		mKnowledgeModels = new ArrayList<ConsultListModel>();
		mAdapter = new ConsultAdapter(this, mKnowledgeModels);
	}

	public void initView() {
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.text_my_consult);
		View left = findViewById(R.id.title_with_back_title_btn_left);
		left.setOnClickListener(this);
		TextView tvLeft = (TextView) findViewById(R.id.tv_title_with_back_left);
		tvLeft.setBackgroundResource(R.drawable.btn_back_bg);

		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_my_consult);
		ListView listView = mPullToRefreshListView.getRefreshableView();
		listView.setAdapter(mAdapter);
		listView.setSelector(new BitmapDrawable());
		listView.setOnItemClickListener(this);
		listView.setCacheColorHint(getResources().getColor(R.color.transparent));
		listView.setFadingEdgeLength(0);
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2() {
			@Override
			public void onPullDownToRefresh() {
				mPage = 1;
				getKnowledgeModels(PULL_DOWN);
			}

			@Override
			public void onPullUpToRefresh() {
				getKnowledgeModels(PULL_UP);
			}
		});
		mPullToRefreshListView.setHeaderVisible(true);
		mPullToRefreshListView.setMode(Mode.BOTH);
		mPullToRefreshListView.setIsShowHeaderFresh(true);
	}

	private void getKnowledgeModels(final int pullStatus) {

		if (mIsGettingData) {
			return;
		}
		mIsGettingData = true;
		new ActionProcessor().startAction(MyConsultActivity.this, new IActionListener() {

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
				return DoctorReq.getConsultList(mPage);
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		ConsultListModel model = (ConsultListModel) parent.getAdapter().getItem(position);
//		if (null != model) {
//			// Intent intent = new Intent(MyConsultActivity.this,
//			// MyKnowledgeDetailActivity.class);
//			// intent.putExtra(ConstantSet.EXTRA_KNOWLEDGEMODEL, model);
//			// startActivityForResult(intent, REQUEST_CODE);
//		}
//	    ConsultListModel model = (ConsultListModel) parent.getAdapter().getItem(position);
//        if (null != model) {
//            Intent intent = new Intent(this, DoctorDetailActivity.class);
//            intent.putExtra(ConstantSet.EXTRA_DOCTORMODEL, model.);
//            startActivity(intent);
//            finish();
//        }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
			mPullToRefreshListView.setHeaderVisible(true);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(final View v) {
		doActionAgain(TAG, new ActionListener() {

			@Override
			public void doAction() {
				switch (v.getId()) {
					case R.id.title_with_back_title_btn_left:
						finish();
						break;
					default:
						break;
				}
			}
		});
	}
}