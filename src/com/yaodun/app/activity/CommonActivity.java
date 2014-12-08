package com.yaodun.app.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qianjiang.framework.util.NetUtil;
import com.qianjiang.framework.widget.LoadingUpView;
import com.yaodun.app.R;
import com.yaodun.app.authentication.ActionProcessor;
import com.yaodun.app.authentication.ActionResult;
import com.yaodun.app.listener.IActionListener;
import com.yaodun.app.model.CommonModel;
import com.yaodun.app.req.SystemItemReq;
import com.yaodun.app.util.ConstantSet;

/**
 * 公共显示界面
 * 
 * @author zou.sq
 */
public class CommonActivity extends YaodunActivityBase implements OnClickListener {
	private static final int STATUS_GET_CLASS_NEWS_SUCCESS = 2;
	private static final int STATUS_GET_CLASS_NEWS_FAIL = 3;
	private LinearLayout mLlBack;
	private TextView mTvContent;
	private int mTitleResId;
	private LoadingUpView mLoadingUpView;
	private int mBaseInfo;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			Object objec = msg.obj;
			dismissLoadingUpView(mLoadingUpView);
			switch (what) {
				case STATUS_GET_CLASS_NEWS_SUCCESS:
					if (null != objec) {
						ActionResult result = (ActionResult) msg.obj;
						List<CommonModel> models = (List<CommonModel>) result.ResultObject;
						if (null != models && !models.isEmpty()) {
							mTvContent.setText(models.get(0).getData());
						}
					}

					break;
				case STATUS_GET_CLASS_NEWS_FAIL:
					if (null != objec) {
						ActionResult result = (ActionResult) msg.obj;
						showErrorMsg(result);
					}
					break;
				default:
					break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);
		initVariable();
		initView();
		setListener();
		getDetail();
	}

	private void initVariable() {
		mLoadingUpView = new LoadingUpView(this, false);
		Intent intent = getIntent();
		mBaseInfo = intent.getIntExtra(ConstantSet.EXTRA_JUMP_FLAG, 0);
		if (0 == mBaseInfo) {
			finish();
		}
		switch (mBaseInfo) {
			case ConstantSet.EXTRA_ABOUT:
				mTitleResId = R.string.text_about;
				break;
			case ConstantSet.EXTRA_INTRODUCE:
				mTitleResId = R.string.text_introduce;
				break;
			case ConstantSet.EXTRA_DECLARE:
				mTitleResId = R.string.text_declare;
				break;

			default:
				break;
		}
	}

	private void setListener() {
		mLlBack.setOnClickListener(this);
	}

	private void initView() {
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(mTitleResId);
		mLlBack = (LinearLayout) findViewById(R.id.title_with_back_title_btn_left);
		TextView mTvBack = (TextView) findViewById(R.id.tv_title_with_back_left);
		mTvBack.setBackgroundResource(R.drawable.btn_back_bg);
		mTvContent = (TextView) findViewById(R.id.tv_content);
	}

	private void getDetail() {
		if (!NetUtil.isNetworkAvailable()) {
			toast(getString(R.string.network_is_not_available));
			return;
		}
		showLoadingUpView(mLoadingUpView);
		new ActionProcessor().startAction(this, new IActionListener() {

			@Override
			public void onSuccess(ActionResult result) {
				sendMessageToHandler(STATUS_GET_CLASS_NEWS_SUCCESS, result);
			}

			@Override
			public void onError(ActionResult result) {
				sendMessageToHandler(STATUS_GET_CLASS_NEWS_FAIL, result);
			}

			@Override
			public ActionResult onAsyncRun() {
				return SystemItemReq.getDetail(mBaseInfo + "");
			}
		});
	}

	private void sendMessageToHandler(int what, ActionResult result) {
		Message msg = mHandler.obtainMessage(what);
		msg.obj = result;
		mHandler.sendMessage(msg);
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
}
