package com.yaodun.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.qianjiang.framework.widget.LoadingUpView;
import com.yaodun.app.R;
import com.yaodun.app.authentication.ActionProcessor;
import com.yaodun.app.authentication.ActionResult;
import com.yaodun.app.listener.IActionListener;
import com.yaodun.app.model.KnowledgeDetailModel;
import com.yaodun.app.model.KnowledgeModel;
import com.yaodun.app.req.KnowledgeReq;
import com.yaodun.app.util.ConstantSet;

/**
 * 关于界面
 * 
 * @author zou.sq
 */
public class KnowledgeDetailActivity extends YaodunActivityBase implements OnClickListener {

	private static final int GET_DATA_SUCCESSED = 0;
	private static final int GET_DATA_FAIL = 1;
	private KnowledgeModel mKnowledgeModel;
	private TextView mTvTitle;
	private TextView mTvCollectCount;
	private TextView mTvDetail;
	private boolean mIsGettingData;
	private LoadingUpView mLoadingUpView;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			dismissLoadingUpView(mLoadingUpView);
			ActionResult result = (ActionResult) msg.obj;
			if (result != null) {
				switch (msg.what) {
					case GET_DATA_SUCCESSED:
						KnowledgeDetailModel model = (KnowledgeDetailModel) result.ResultObject;
						if (model != null) {
							mTvCollectCount.setText(getString(R.string.knowledge_detail_collect_count,
									model.getCountAttention()));
							mTvDetail.setText(model.getDescription());
						}
						break;
					case GET_DATA_FAIL:
						if (1 == msg.arg2) {
							showErrorMsg((ActionResult) result);
						}
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
		setContentView(R.layout.activity_knowledge_detail);
		initVariable();
		initView();
		setListener();
		getKnowledgeModels();
	}

	private void initVariable() {
		Intent intent = getIntent();
		mKnowledgeModel = (KnowledgeModel) intent.getSerializableExtra(ConstantSet.EXTRA_KNOWLEDGEMODEL);
		if (null == mKnowledgeModel) {
			finish();
		}
		mLoadingUpView = new LoadingUpView(this);
	}

	private void setListener() {
	}

	private void initView() {
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.knowledge_detail);
		View left = findViewById(R.id.title_with_back_title_btn_left);
		left.setOnClickListener(this);
		TextView tvLeft = (TextView) findViewById(R.id.tv_title_with_back_left);
		tvLeft.setBackgroundResource(R.drawable.btn_back_bg);
		mTvTitle = (TextView) findViewById(R.id.tv_knowledge_detail_title);
		mTvCollectCount = (TextView) findViewById(R.id.tv_knowledge_detail_collect);
		mTvDetail = (TextView) findViewById(R.id.tv_knowledge_detail);
		mTvTitle.setText(mKnowledgeModel.getTitle());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_with_back_title_btn_left:
				finish();
				break;

			default:
				break;
		}
	}

	private void getKnowledgeModels() {
		if (mIsGettingData) {
			return;
		}
		showLoadingUpView(mLoadingUpView);
		mIsGettingData = true;
		new ActionProcessor().startAction(KnowledgeDetailActivity.this, new IActionListener() {

			@Override
			public void onSuccess(ActionResult result) {
				sendHandler(GET_DATA_SUCCESSED, result);
			}

			@Override
			public void onError(ActionResult result) {
				sendHandler(GET_DATA_FAIL, result);
			}

			@Override
			public ActionResult onAsyncRun() {
				return KnowledgeReq.getKnowledgeDetail(mKnowledgeModel.getId());
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
