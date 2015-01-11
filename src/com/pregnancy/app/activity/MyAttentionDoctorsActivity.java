package com.pregnancy.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.pregnancy.app.R;
import com.pregnancy.app.adapter.MyDoctorAdapter;
import com.pregnancy.app.authentication.ActionProcessor;
import com.pregnancy.app.authentication.ActionResult;
import com.pregnancy.app.listener.IActionListener;
import com.pregnancy.app.model.DoctorModel;
import com.pregnancy.app.req.DoctorReq;
import com.pregnancy.app.util.ConstantSet;
import com.qianjiang.framework.widget.LoadingUpView;
import com.qianjiang.framework.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjiang.framework.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qianjiang.framework.widget.pulltorefresh.PullToRefreshListView;

/**
 * 药师咨询界面
 * 
 * @author zou.sq
 */
public class MyAttentionDoctorsActivity extends YaodunActivityBase implements OnClickListener {

	private static final int GET_DATA_SUCCESSED = 0;
	private static final int GET_DATA_FAIL = 1;
	private static final int PULL_DOWN = 0;
	private static final int PULL_UP = 1;
	private static final int ATTENTION_SUCCESSED = 4;
	private static final int ATTENTION_FAIL = 5;
	private View mEmptyView;
	private PullToRefreshListView mPullToRefreshListView;
	private int mPage;
	private boolean mIsGettingData;
	private List<DoctorModel> mDoctorModels;
	private MyDoctorAdapter mDoctorAdapter;
	private boolean mIsAttention;
	private LoadingUpView mLoadingUpView;
	private Handler mHandler = new Handler() {
		@Override
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
					case ATTENTION_SUCCESSED:
						toast("取消关注成功");
						mPullToRefreshListView.setHeaderVisible(true);
						break;
					case ATTENTION_FAIL:
						showErrorMsg((ActionResult) result);
						break;
					default:
						break;
				}
			}
			dismissLoadingUpView(mLoadingUpView);
			mIsGettingData = false;
			mIsAttention = false;
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
		mLoadingUpView = new LoadingUpView(this);
		mDoctorModels = new ArrayList<DoctorModel>();
		mDoctorAdapter = new MyDoctorAdapter(this, mDoctorModels, mImageLoader, this);
	}

	private void initView() {
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.text_my_attention);
		View left = findViewById(R.id.title_with_back_title_btn_left);
		left.setOnClickListener(this);
		TextView tvLeft = (TextView) findViewById(R.id.tv_title_with_back_left);
		tvLeft.setBackgroundResource(R.drawable.btn_back_bg);

		mEmptyView = View.inflate(this, R.layout.view_empty_layout, null);
		TextView tvEmptyToast = (TextView) mEmptyView.findViewById(R.id.tv_empty_content);
		tvEmptyToast.setText(R.string.tips_doctor_consule_empty_view);

		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_doctor_consult);
		ListView listView = mPullToRefreshListView.getRefreshableView();
		listView.setCacheColorHint(getResources().getColor(R.color.transparent));
		listView.setFadingEdgeLength(0);
		listView.setAdapter(mDoctorAdapter);
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
			case R.id.title_with_back_title_btn_left:
				finish();
				break;
			case R.id.btn_attention:
				DoctorModel model = (DoctorModel) v.getTag();
				cancelAttention(model);
				break;
			default:
				break;
		}
	}

	private void cancelAttention(final DoctorModel mDoctorModel) {
		if (mIsAttention || null == mDoctorModel) {
			return;
		}
		showLoadingUpView(mLoadingUpView);
		mIsAttention = true;
		new ActionProcessor().startAction(MyAttentionDoctorsActivity.this, new IActionListener() {

			@Override
			public void onSuccess(ActionResult result) {
				sendHandler(ATTENTION_SUCCESSED, result);
			}

			@Override
			public void onError(ActionResult result) {
				sendHandler(ATTENTION_FAIL, result);
			}

			@Override
			public ActionResult onAsyncRun() {
				// operation为1代表关注,为0代表取消关注
				return DoctorReq.attentionDoctor(mDoctorModel.getDoctorId(), "0");
			}
		});
	}

	private void sendHandler(int what, ActionResult result) {
		Message msg = mHandler.obtainMessage();
		msg.what = what;
		msg.obj = result;
		mHandler.sendMessage(msg);
	}

}
