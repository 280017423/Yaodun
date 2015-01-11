package com.pregnancy.app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pregnancy.app.R;
import com.pregnancy.app.authentication.ActionProcessor;
import com.pregnancy.app.authentication.ActionResult;
import com.pregnancy.app.listener.IActionListener;
import com.pregnancy.app.req.SystemItemReq;
import com.qianjiang.framework.app.QJActivityBase;
import com.qianjiang.framework.util.NetUtil;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;

public class FeedBackActivity extends QJActivityBase implements OnClickListener {
	private static final String TAG = "FeedBackActivity";
	private boolean mIsGettingData;
	private LoadingUpView mLoadingUpView;
	private EditText mEdtContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initView();
	}

	private void initView() {
		TextView tvTitle = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		tvTitle.setText(getString(R.string.feedback_title));
		mLoadingUpView = new LoadingUpView(this, true);
		mLoadingUpView.setCancelable(true);
		mEdtContent = (EditText) findViewById(R.id.edt_feedback_content);
		LinearLayout llEnsureSubmit = (LinearLayout) findViewById(R.id.title_with_back_title_btn_right);
		TextView tvRight = (TextView) findViewById(R.id.tv_title_with_right);
		tvRight.setText(getString(R.string.submit));
		TextView tvBack = (TextView) findViewById(R.id.tv_title_with_back_left);
		LinearLayout llBack = (LinearLayout) findViewById(R.id.title_with_back_title_btn_left);
		tvBack.setBackgroundResource(R.drawable.btn_back_bg);
		tvRight.setBackgroundResource(R.drawable.btn_logout_selector);
		llBack.setOnClickListener(this);
		llEnsureSubmit.setOnClickListener(this);
	}

	private void submitContent(final String content) {
		if (mIsGettingData) {
			return;
		}
		if (!NetUtil.isNetworkAvailable()) {
			toast(getString(R.string.network_is_not_available));
			return;
		}
		if (StringUtil.isNullOrEmpty(content)) {
			toast(getString(R.string.feedback_empty));
			return;
		}
		mIsGettingData = true;
		showLoadingUpWindow(mLoadingUpView);
		new ActionProcessor().startAction(this, true, false, new IActionListener() {

			@Override
			public void onSuccess(ActionResult result) {
				toastResult(result);
				mIsGettingData = false;
				dismissLoadingUpWindow(mLoadingUpView);
				finish();
			}

			@Override
			public void onError(ActionResult result) {
				toastResult(result);
				mIsGettingData = false;
				dismissLoadingUpWindow(mLoadingUpView);
			}

			@Override
			public ActionResult onAsyncRun() {
				return SystemItemReq.sendFeedback(content);
			}
		});
	}

	private void toastResult(ActionResult actionResult) {
		if (!FeedBackActivity.this.isFinishing()) {
			toast((String) actionResult.ResultObject);
		}
	}

	@Override
	public void onClick(final View v) {
		doActionAgain(TAG, new ActionListener() {
			@Override
			public void doAction() {
				switch (v.getId()) {
					case R.id.title_with_back_title_btn_right:
						submitContent(mEdtContent.getText().toString());
						break;
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
