package com.yaodun.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qianjiang.framework.imageloader.core.DisplayImageOptions;
import com.qianjiang.framework.imageloader.core.display.SimpleBitmapDisplayer;
import com.qianjiang.framework.util.ImeUtil;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;
import com.yaodun.app.R;
import com.yaodun.app.adapter.MyQuestionAdapter;
import com.yaodun.app.authentication.ActionProcessor;
import com.yaodun.app.authentication.ActionResult;
import com.yaodun.app.listener.IActionListener;
import com.yaodun.app.model.ConsultListModel;
import com.yaodun.app.model.MyQuestionDetailModel;
import com.yaodun.app.model.MyQuestionModel;
import com.yaodun.app.req.ConsultReq;
import com.yaodun.app.req.DoctorReq;
import com.yaodun.app.util.ConstantSet;

/**
 * 我的提问详细界面
 * 
 * @author zou.sq
 */
public class QuestionDetailActivity extends YaodunActivityBase implements OnClickListener {

	private static final int GET_DATA_SUCCESSED = 0;
	private static final int GET_DATA_FAIL = 1;
	private static final int SEND_DATA_SUCCESSED = 2;
	private static final int SEND_DATA_FAIL = 3;
	private static final int ATTENTION_SUCCESSED = 4;
	private static final int ATTENTION_FAIL = 5;
	private ConsultListModel mConsultListModel;
	private EditText mEdtCommit;
	private boolean mIsSend;
	private boolean mIsAttention;
	private ImageView mIvImg;
	private TextView mTvName;
	private TextView mTvProfessional;
	private TextView mTvDescription;
	private LoadingUpView mLoadingUpView;
	private DisplayImageOptions mOptions;
	private Button mBtnAttention;
	private ListView mListView;
	private List<MyQuestionModel> mQuestionList;
	private MyQuestionAdapter mAdapter;
	private boolean mIsGettingData;
	private MyQuestionDetailModel mDetailModel;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			dismissLoadingUpView(mLoadingUpView);
			ActionResult result = (ActionResult) msg.obj;
			if (result != null) {
				switch (msg.what) {
					case GET_DATA_SUCCESSED:
						mDetailModel = (MyQuestionDetailModel) result.ResultObject;
						if (mDetailModel != null) {
							String imgUrl = mDetailModel.getDoctorImg();
							if (!StringUtil.isNullOrEmpty(imgUrl)) {
								mImageLoader.displayImage(imgUrl, mIvImg, mOptions);
							}
							mTvName.setText(mDetailModel.getDoctorName());
							mTvProfessional.setText("");
							mTvDescription.setText(mDetailModel.getDoctorDescription());
							mQuestionList.clear();
							mQuestionList.addAll(mDetailModel.getQuestionList());
							mAdapter.notifyDataSetChanged();
							if (1 == mDetailModel.getAttionStatus()) {
								mBtnAttention.setBackgroundResource(R.drawable.btn_attention_disable);
							} else {
								mBtnAttention.setBackgroundResource(R.drawable.btn_attention_bg);
							}
						}
						break;
					case GET_DATA_FAIL:
						showErrorMsg((ActionResult) result);
						break;
					case SEND_DATA_SUCCESSED:
						mEdtCommit.setText("");
						MyQuestionModel model = (MyQuestionModel) result.ResultObject;
						if (null != model) {
							model.setUserType(0);
							mQuestionList.add(model);
							mAdapter.notifyDataSetChanged();
						}
						toast("评论成功");
						break;
					case SEND_DATA_FAIL:
						showErrorMsg((ActionResult) result);
						break;
					case ATTENTION_SUCCESSED:
						if (1 == mDetailModel.getAttionStatus()) {
							mDetailModel.setAttionStatus(0);
							mBtnAttention.setBackgroundResource(R.drawable.btn_attention_bg);
							toast("已取消关注");
						} else {
							mDetailModel.setAttionStatus(1);
							mBtnAttention.setBackgroundResource(R.drawable.btn_attention_disable);
							toast("关注成功");
						}
						break;
					case ATTENTION_FAIL:
						showErrorMsg((ActionResult) result);
						break;
					default:
						break;
				}
			}
			mIsSend = false;
			mIsAttention = false;
			mIsGettingData = false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_detail);
		initVariable();
		initView();
		setListener();
		getDetail();
	}

	private void getDetail() {
		if (mIsGettingData) {
			return;
		}
		showLoadingUpView(mLoadingUpView);
		mIsGettingData = true;
		new ActionProcessor().startAction(QuestionDetailActivity.this, new IActionListener() {

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
				return ConsultReq.getQuestionDetail(mConsultListModel.getQuestionId());
			}
		});
	}

	private void initVariable() {
		Intent intent = getIntent();
		mConsultListModel = (ConsultListModel) intent.getSerializableExtra(ConstantSet.EXTRA_CONSULTLISTMODEL);
		if (null == mConsultListModel) {
			finish();
		}
		mQuestionList = new ArrayList<MyQuestionModel>();
		mAdapter = new MyQuestionAdapter(this, mQuestionList, mImageLoader);
		mLoadingUpView = new LoadingUpView(this);
		mOptions = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc()
				.displayer(new SimpleBitmapDisplayer()).build();
	}

	private void setListener() {
	}

	private void initView() {
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.question_detail);
		View left = findViewById(R.id.title_with_back_title_btn_left);
		left.setOnClickListener(this);
		TextView tvLeft = (TextView) findViewById(R.id.tv_title_with_back_left);
		tvLeft.setBackgroundResource(R.drawable.btn_back_bg);

		mListView = (ListView) findViewById(R.id.lv_question_detail);
		mListView.setAdapter(mAdapter);
		mEdtCommit = (EditText) findViewById(R.id.et_commit_content);
		mIvImg = (ImageView) findViewById(R.id.iv_doctor_img);
		mTvName = (TextView) findViewById(R.id.tv_doctor_name);
		mTvProfessional = (TextView) findViewById(R.id.tv_doctor_professional);
		mTvDescription = (TextView) findViewById(R.id.tv_doctor_description);
		mBtnAttention = (Button) findViewById(R.id.btn_attention);
		mBtnAttention.setBackgroundResource(R.drawable.btn_attention_disable);
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
					case R.id.btn_attention:
						attentionKnowledge();
						break;
					case R.id.btn_send:
						ImeUtil.hideSoftInput(QuestionDetailActivity.this);
						sendReplay();
						break;

					default:
						break;
				}
			}
		});

	}

	private void sendReplay() {
		if (mIsSend) {
			return;
		}
		final String commit = mEdtCommit.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(commit)) {
			toast("提问不能为空");
			return;
		}
		showLoadingUpView(mLoadingUpView);
		mIsSend = true;
		new ActionProcessor().startAction(QuestionDetailActivity.this, new IActionListener() {

			@Override
			public void onSuccess(ActionResult result) {
				sendHandler(SEND_DATA_SUCCESSED, result);
			}

			@Override
			public void onError(ActionResult result) {
				sendHandler(SEND_DATA_FAIL, result);
			}

			@Override
			public ActionResult onAsyncRun() {
				return ConsultReq.replyQuestion(mConsultListModel.getQuestionId(), commit);
			}
		});
	}

	private void attentionKnowledge() {
		if (mIsAttention || null == mConsultListModel) {
			return;
		}
		showLoadingUpView(mLoadingUpView);
		mIsAttention = true;
		new ActionProcessor().startAction(QuestionDetailActivity.this, new IActionListener() {

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
				String operation = "1";
				if (1 == mDetailModel.getAttionStatus()) {
					operation = "0";
				}
				return DoctorReq.attentionDoctor(mDetailModel.getDoctorId(), operation);
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
