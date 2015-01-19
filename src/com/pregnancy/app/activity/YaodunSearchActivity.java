package com.pregnancy.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pregnancy.app.R;
import com.pregnancy.app.adapter.DetectRuleAdapter;
import com.pregnancy.app.adapter.MonthAdapter;
import com.pregnancy.app.adapter.SearchedMedicineNameAdapter;
import com.pregnancy.app.authentication.ActionResult;
import com.pregnancy.app.manager.UserMgr;
import com.pregnancy.app.model.MedicineBean;
import com.pregnancy.app.model.MedicineCheckResultBean;
import com.pregnancy.app.model.MedicineCheckRuleBean;
import com.pregnancy.app.model.QueryType;
import com.pregnancy.app.model.UserInfoModel;
import com.pregnancy.app.req.MedicineReq;
import com.pregnancy.app.util.ConstantSet;
import com.pregnancy.app.util.PopWindowUtil;
import com.pregnancy.app.util.SharedPreferenceUtil;
import com.qianjiang.framework.util.ImeUtil;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.util.UIUtil;
import com.qianjiang.framework.widget.LoadingUpView;

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
	private EditText mEtName;
	private TextView mTvRenshenTime;
	private View mViewMonth;
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
		queryType = QueryType.medicine_yunfu;
	}

	private void initView() {
		mTvTitle = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		mTvTitle.setText(QueryType.getQueryTypeString(queryType));

		mScrollView = (ScrollView) findViewById(R.id.sv_layout);
		layoutYunfu = findViewById(R.id.layout_yunfu);
		rbRenshen = (RadioButton) findViewById(R.id.rb_renshen);
		rbBuru = (RadioButton) findViewById(R.id.rb_buru);
		rbRenshen.setOnCheckedChangeListener(this);
		rbBuru.setOnCheckedChangeListener(this);
		mViewMonth = findViewById(R.id.ll_month);
		mTvRenshenTime = (TextView) findViewById(R.id.tv_renshen_time);
		mTvRenshenTime.setCompoundDrawables(null, null, getArrowDrawable(R.drawable.expand_down), null);
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
		if (null == addList || addList.isEmpty()) {
			toast("请输入药品再查询");
			return;
		}
		if (queryType == QueryType.medicine_yunfu) {
			if (rbRenshen.isChecked()) {
				String month = mTvRenshenTime.getText().toString().trim();
				if (StringUtil.isNullOrEmpty(month)) {
					toast("请输入月份");
					return;
				} else {
					int monthValue = Integer.parseInt(month);
					if (monthValue < 1 || monthValue > 12) {
						toast("请输入正确月份");
						return;
					}
				}
			}
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
					user.gestateTime = rbRenshen.isChecked() ? mTvRenshenTime.getText().toString().trim() : "";
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
						StringBuilder builder = new StringBuilder();
						for (int i = 0; i < tmpList.size(); i++) {
							builder.append(tmpList.get(i).getResult() + "\r\n");
						}
						showPop(builder.toString());
					}
				} else {
					showErrorMsg(result);
				}
			}
		}.execute();
	}

	protected void showPop(final String checkResult) {
		View contentView = View.inflate(this, R.layout.view_search_result, null);
		final PopWindowUtil popUtil = new PopWindowUtil(contentView, null);
		TextView tvCheckResult = (TextView) contentView.findViewById(R.id.tv_query_result_content);
		ImageView ivClose = (ImageView) contentView.findViewById(R.id.iv_close);
		View viewAskDoctor = contentView.findViewById(R.id.ll_ask_doctor);
		tvCheckResult.setText(checkResult);
		viewAskDoctor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String info = "";
				if (null != addList && !addList.isEmpty()) {
					for (int i = 0; i < addList.size(); i++) {
						info += addList.get(i).getDrugname();
					}
				}
				SharedPreferenceUtil.saveValue(YaodunSearchActivity.this, ConstantSet.FILE_JYT_CONFIG,
						ConstantSet.KEY_MEDICINE_INFO, info + checkResult);
				popUtil.dismiss();
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
			case R.id.iv_qrcode:
				startActivity(QrCodeActivity.getStartActIntent(mContext));
				break;
			case R.id.btn_search:
				doMedicineCheck();
				break;
			case R.id.tv_renshen_time:
				showMonthPop();
				break;
			default:
				break;
		}
	}

	private void showMonthPop() {
		View contentView = View.inflate(this, R.layout.view_month_layout, null);
		final PopupWindow popupWindow = new PopupWindow(
				contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				mTvRenshenTime.setCompoundDrawables(null, null, getArrowDrawable(R.drawable.expand_down), null);
			}
		});
		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00);
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.setOutsideTouchable(true);
		ListView lvMonth = (ListView) contentView.findViewById(R.id.lv_month);
		lvMonth.setAdapter(new MonthAdapter(YaodunSearchActivity.this));
		lvMonth.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mTvRenshenTime.setText((position + 1) + "");
				popupWindow.dismiss();
			}
		});
		popupWindow.showAsDropDown(mTvRenshenTime);
		mTvRenshenTime.setCompoundDrawables(null, null, getArrowDrawable(R.drawable.expand_up), null);
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
		boolean isRenshen = rbRenshen.isChecked();
		if (isRenshen) {
			mViewMonth.setVisibility(View.VISIBLE);
		} else {
			mViewMonth.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		if (null != receiver) {
			unregisterReceiver(receiver);
		}
		super.onDestroy();
	}

	private Drawable getArrowDrawable(int id) {
		Drawable drawable = getResources().getDrawable(id);
		// / 这一步必须要做,否则不会显示.
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		return drawable;
	}
}
