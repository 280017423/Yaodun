package com.yaodun.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qianjiang.framework.util.ImeUtil;
import com.qianjiang.framework.util.UIUtil;
import com.qianjiang.framework.widget.LoadingUpView;
import com.yaodun.app.R;
import com.yaodun.app.adapter.DetectRuleAdapter;
import com.yaodun.app.adapter.SearchedMedicineNameAdapter;
import com.yaodun.app.authentication.ActionResult;
import com.yaodun.app.manager.UserMgr;
import com.yaodun.app.model.MedicineBean;
import com.yaodun.app.model.MedicineCheckResultBean;
import com.yaodun.app.model.MedicineCheckRuleBean;
import com.yaodun.app.model.QueryType;
import com.yaodun.app.model.UserInfoModel;
import com.yaodun.app.req.MedicineReq;
import com.yaodun.app.util.ConstantSet;
import com.yaodun.app.util.PopWindowUtil;

/**
 * 药盾--查询页面
 * 
 * @author zou.sq
 */
public class YaodunSearchActivity extends YaodunActivityBase implements OnClickListener, OnItemClickListener,
		OnCheckedChangeListener {
	private static final int TEACHER_VIEW_HEIGHT = 400;
	private boolean mIsSearching;
	private boolean mIsMedicineCheck;
	private TextView mTvTitle;
	private View layoutYunfu;
	private RadioButton rbRenshen, rbBuru;
	private EditText mEtRenshenTime, mEtName;
	private ListView lvSearchedNames;
	private LinearLayout layoutAddedNames;
	private SearchedMedicineNameAdapter searchAdapter;
	private List<MedicineBean> searchList = new ArrayList<MedicineBean>();
	private List<MedicineBean> addList = new ArrayList<MedicineBean>();

	private ScrollView mScrollView;
	private ListView lvDetectRules;
	private DetectRuleAdapter detectAdapter;
	private List<MedicineCheckRuleBean> detectList = new ArrayList<MedicineCheckRuleBean>();
	private int queryType;
	private LoadingUpView mLoadingUpView;
	android.os.Handler handler = new android.os.Handler();

	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
			searchMedicineName(text.toString().trim());
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

		}

		@Override
		public void afterTextChanged(Editable arg0) {

		}
	};
	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if (ConstantSet.ACTION_QRCODE_OK.equals(arg1.getAction())) {
				String qrcode = arg1.getStringExtra(ConstantSet.EXTRA_QRCODE);
				mEtName.setText(qrcode);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yaodun_search);
		initVariable();
		initView();
		getMedicineCheckRules();
		registerReceiver(receiver, new IntentFilter(ConstantSet.ACTION_QRCODE_OK));
	}

	private void initVariable() {
		mLoadingUpView = new LoadingUpView(this, true);
		Intent intent = getIntent();
		queryType = intent.getIntExtra(ConstantSet.EXTRA_JUMP_TYPE, -1);
		if (-1 == queryType) {
			finish();
		}
	}

	private void initView() {
		View left = findViewById(R.id.title_with_back_title_btn_left);
		left.setOnClickListener(this);
		TextView tvLeft = (TextView) findViewById(R.id.tv_title_with_back_left);
		tvLeft.setBackgroundResource(R.drawable.btn_back_bg);
		mTvTitle = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		mTvTitle.setText(QueryType.getQueryTypeString(queryType));

		mScrollView = (ScrollView) findViewById(R.id.sv_layout);
		layoutYunfu = findViewById(R.id.layout_yunfu);
		rbRenshen = (RadioButton) findViewById(R.id.rb_renshen);
		rbBuru = (RadioButton) findViewById(R.id.rb_buru);
		rbRenshen.setOnCheckedChangeListener(this);
		rbBuru.setOnCheckedChangeListener(this);
		mEtRenshenTime = (EditText) findViewById(R.id.et_renshen_time);
		layoutYunfu.setVisibility(queryType == QueryType.medicine_yunfu ? View.VISIBLE : View.GONE);

		mEtName = (EditText) findViewById(R.id.et_name);
		mEtName.addTextChangedListener(watcher);
		ImageView ivQrcode = (ImageView) findViewById(R.id.iv_qrcode);
		ivQrcode.setOnClickListener(this);

		layoutAddedNames = (LinearLayout) findViewById(R.id.layout_medicine_name);
		Button btnSearch = (Button) findViewById(R.id.btn_search);
		btnSearch.setOnClickListener(this);

		lvSearchedNames = (ListView) findViewById(R.id.lv_names);
		searchAdapter = new SearchedMedicineNameAdapter(mContext, searchList);
		lvSearchedNames.setAdapter(searchAdapter);
		lvSearchedNames.setOnItemClickListener(this);
		lvSearchedNames.setVisibility(searchList.size() > 0 ? View.VISIBLE : View.GONE);

		android.view.ViewGroup.LayoutParams layoutParams = lvSearchedNames.getLayoutParams();
		layoutParams.height = UIUtil.dip2px(this, TEACHER_VIEW_HEIGHT);
		lvSearchedNames.setLayoutParams(layoutParams);

		lvDetectRules = (ListView) findViewById(R.id.lv_rules);
		detectAdapter = new DetectRuleAdapter(mContext, detectList);
		lvDetectRules.setAdapter(detectAdapter);

		new CountDownTimer(200, 200) {
			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				mScrollView.scrollTo(0, 0);
			}
		}.start();
	}

	/**
	 * 根据关键字查药名
	 * 
	 * @param keyword
	 */
	private void searchMedicineName(final String keyword) {
		if (TextUtils.isEmpty(keyword)) {
			updateKeywordMatchList(null);
			mIsSearching = false;
			return;
		}

		if (mIsSearching) {
			return;
		}
		mIsSearching = true;
		new AsyncTask<String, Void, ActionResult>() {
			String passinKeyword = null;

			@Override
			protected ActionResult doInBackground(String... params) {
				passinKeyword = params[0];
				return MedicineReq.searchMedicineName(passinKeyword);
			}

			@Override
			protected void onPostExecute(ActionResult result) {
				if (passinKeyword.equals(keyword)) {// 防止这种情况：第一个请求比第二个请求后返回，结果覆盖掉第二个请求的结果
					if (result != null && ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {
						List<MedicineBean> tmpList = (List<MedicineBean>) result.ResultObject;
						updateKeywordMatchList(tmpList);

					} else {
						showErrorMsg(result);
					}
				}
				mIsSearching = false;
			}
		}.execute(keyword);

	}

	private void updateKeywordMatchList(List<MedicineBean> tmpList) {
		searchList.clear();
		if (tmpList != null) {
			searchList.addAll(tmpList);
		}
		searchAdapter.notifyDataSetChanged();

		if (searchList.size() > 0) {
			lvSearchedNames.setVisibility(View.VISIBLE);
			android.widget.RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) lvSearchedNames
					.getLayoutParams();
			if (lp != null) {
				int marginTop = 95;
				if (queryType == QueryType.medicine_yunfu) {
					marginTop += 85;
				}
				lp.topMargin = UIUtil.dip2px(getApplicationContext(), marginTop);
				lvSearchedNames.setLayoutParams(lp);
			}
		} else {
			lvSearchedNames.setVisibility(View.GONE);
		}

	}

	/**
	 * 药品检测查询
	 */
	private void doMedicineCheck() {
		if (mIsMedicineCheck) {
			return;
		}
		showLoadingUpView(mLoadingUpView);
		mIsMedicineCheck = true;
		new AsyncTask<String, Void, ActionResult>() {

			@Override
			protected ActionResult doInBackground(String... params) {
				UserInfoModel user = UserMgr.getUserInfoModel();
				if (null == user) {
					user = new UserInfoModel();
				}
				user.checkType = queryType;
				if (queryType == QueryType.medicine_yunfu) {
					user.isGestate = rbRenshen.isChecked() ? "1" : "0";
					user.gestateTime = rbRenshen.isChecked() ? mEtRenshenTime.getText().toString().trim() : "";
					user.isSuckling = rbBuru.isChecked() ? "1" : "0";
				}
				return MedicineReq.medicineCheck(user, addList);
			}

			@Override
			protected void onPostExecute(ActionResult result) {
				dismissLoadingUpView(mLoadingUpView);
				mIsMedicineCheck = false;
				if (result != null && ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {
					detectList.clear();
					detectAdapter.notifyDataSetChanged();
					mScrollView.scrollTo(0, 0);// 要加这一句才不会滚
					List<MedicineCheckResultBean> tmpList = (List<MedicineCheckResultBean>) result.ResultObject;
					if (null != tmpList && !tmpList.isEmpty()) {
						MedicineCheckResultBean checkResult = tmpList.get(0);
						showPop(checkResult);
					}
				} else {
					showErrorMsg(result);
				}
			}
		}.execute();
	}

	protected void showPop(MedicineCheckResultBean checkResult) {
		View contentView = null;
		if (null == contentView) {
			contentView = View.inflate(this, R.layout.view_search_result, null);
		}
		final PopWindowUtil popUtil = new PopWindowUtil(contentView, null);
		TextView tvCheckResult = (TextView) contentView.findViewById(R.id.tv_query_result_content);
		TextView tvCheckAdvice = (TextView) contentView.findViewById(R.id.tv_advices_content);
		ImageView ivClose = (ImageView) contentView.findViewById(R.id.iv_close);
		View viewAskDoctor = contentView.findViewById(R.id.ll_ask_doctor);
		tvCheckResult.setText(checkResult.getResult());
		tvCheckAdvice.setText(checkResult.getGrade());
		viewAskDoctor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popUtil.dismiss();
				finish();
				MainActivityGroup.INSTANCE.initStartIndex(2);
			}
		});
		ivClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popUtil.dismiss();
			}
		});
		popUtil.show();
	}

	private void getMedicineCheckRules() {
		new AsyncTask<String, Void, ActionResult>() {

			@Override
			protected ActionResult doInBackground(String... params) {
				return MedicineReq.getMedicineCheckRules(queryType);
			}

			@Override
			protected void onPostExecute(ActionResult result) {
				if (result != null && ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {
					detectList.clear();
					List<MedicineCheckRuleBean> tmpList = (List<MedicineCheckRuleBean>) result.ResultObject;
					if (tmpList != null) {
						detectList.addAll(tmpList);
					}
					detectAdapter.notifyDataSetChanged();
					mScrollView.scrollTo(0, 0);// 要加这一句才不会滚
				} else {
					showErrorMsg(result);
				}
			}
		}.execute();

	}

	/**
	 * 将选择的药名填到页面上
	 * 
	 * @param addList
	 */
	private void addMedicineNames() {
		if (addList == null || addList.size() == 0) {
			layoutAddedNames.setVisibility(View.GONE);
		} else {
			layoutAddedNames.setVisibility(View.VISIBLE);
			layoutAddedNames.removeAllViews();
			for (int i = 0; i < addList.size(); ++i) {
				final View itemView = View.inflate(mContext, R.layout.item_medicine_name, null);
				final int pos = i;
				TextView tvName = (TextView) itemView.findViewById(R.id.tv);
				tvName.setText(addList.get(i).getDrugname());
				View vMove = itemView.findViewById(R.id.iv);
				vMove.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						addList.remove(pos);
						addMedicineNames();
					}
				});
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				int dp10 = UIUtil.dip2px(mContext, 10);
				lp.setMargins(dp10, 0, dp10, 0);
				layoutAddedNames.addView(itemView, lp);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_with_back_title_btn_left:
				finish();
				break;
			case R.id.iv_qrcode:
				startActivity(QrCodeActivity.getStartActIntent(mContext));
				break;
			case R.id.btn_search:
				doMedicineCheck();
				break;
			default:
				break;
		}
	}

	private void hideIme() {
		ImeUtil.hideInputKeyboard(YaodunSearchActivity.this);// 有时候光调用这个方法收不起来，加个延迟再调用一次
		ImeUtil.hideSoftInput(YaodunSearchActivity.this, mEtName);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				ImeUtil.hideInputKeyboard(YaodunSearchActivity.this);
			}
		}, 200);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		addList.add(searchList.get(position));
		searchList.clear();
		searchAdapter.notifyDataSetChanged();
		lvSearchedNames.setVisibility(View.GONE);
		mEtName.setText("");
		addMedicineNames();
		hideIme();
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		mEtRenshenTime.setEnabled(rbRenshen.isChecked());
	}

	@Override
	protected void onDestroy() {
		if (null != receiver) {
			unregisterReceiver(receiver);
		}
		super.onDestroy();
	}
}
