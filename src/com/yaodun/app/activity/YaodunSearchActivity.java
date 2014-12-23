package com.yaodun.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;

import com.qianjiang.framework.util.UIUtil;
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

/**
 * 药盾--查询页面
 * 
 * @author zou.sq
 */
public class YaodunSearchActivity extends YaodunActivityBase implements OnClickListener, OnItemClickListener,
		OnCheckedChangeListener {

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

	TextView tvCheckResult, tvCheckAdvice;

	private ListView lvDetectRules;
	private DetectRuleAdapter detectAdapter;
	private List<MedicineCheckRuleBean> detectList = new ArrayList<MedicineCheckRuleBean>();
	private int queryType = 0;
	public static YaodunSearchActivity INSTANCE;

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
		INSTANCE = this;
		initView();
		registerReceiver(receiver, new IntentFilter(ConstantSet.ACTION_QRCODE_OK));
	}

	private void initView() {
		// title
		View left = findViewById(R.id.title_with_back_title_btn_left);
		left.setOnClickListener(this);
		TextView tvLeft = (TextView) findViewById(R.id.tv_title_with_back_left);
		tvLeft.setBackgroundResource(R.drawable.btn_back_bg);
		mTvTitle = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		mTvTitle.setText("大众用药查询");

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

		tvCheckResult = (TextView) findViewById(R.id.tv_query_result_content);
		tvCheckAdvice = (TextView) findViewById(R.id.tv_advices_content);
		tvCheckResult.setText("");
		tvCheckAdvice.setText("");

		lvDetectRules = (ListView) findViewById(R.id.lv_rules);
		detectAdapter = new DetectRuleAdapter(mContext, detectList);
		lvDetectRules.setAdapter(detectAdapter);
	}

	/**
	 * 根据关键字查药名
	 * 
	 * @param keyword
	 */
	void searchMedicineName(final String keyword) {
		if (mIsSearching) {
			return;
		}
		mIsSearching = true;
		new AsyncTask<String, Void, ActionResult>() {

			@Override
			protected ActionResult doInBackground(String... params) {
				return MedicineReq.searchMedicineName(keyword);
			}

			@Override
			protected void onPostExecute(ActionResult result) {
				if (result != null && ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {
					searchList.clear();
					List<MedicineBean> tmpList = (List<MedicineBean>) result.ResultObject;
					if (tmpList != null) {
						searchList.addAll(tmpList);
					}
					searchAdapter.notifyDataSetChanged();
					lvSearchedNames.setVisibility(searchList.size() > 0 ? View.VISIBLE : View.GONE);
				} else {
					showErrorMsg(result);
				}
				mIsSearching = false;
			}
		}.execute();

	}

	/**
	 * 药品检测查询
	 */
	void doMedicineCheck() {
		if (mIsMedicineCheck) {
			return;
		}
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
				if (result != null && ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {
					detectList.clear();
					List<MedicineCheckResultBean> tmpList = (List<MedicineCheckResultBean>) result.ResultObject;
					if (tmpList != null && tmpList.size() > 0) {
						MedicineCheckResultBean checkResult = tmpList.get(0);
						tvCheckResult.setText(TextUtils.isEmpty(checkResult.result) ? "" : checkResult.result);
						tvCheckAdvice.setText(TextUtils.isEmpty(checkResult.grade) ? "" : checkResult.grade);
					}
				} else {
					showErrorMsg(result);
				}
				mIsMedicineCheck = false;
			}
		}.execute();

	}

	public void changeQueryType(int queryType) {
		this.queryType = queryType;
		mTvTitle.setText(QueryType.getQueryTypeString(queryType));
		layoutYunfu.setVisibility(queryType == QueryType.medicine_yunfu ? View.VISIBLE : View.GONE);
		getMedicineCheckRules();
	}

	void getMedicineCheckRules() {
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
	void addMedicineNames() {
		if (addList == null || addList.size() == 0) {
			layoutAddedNames.setVisibility(View.GONE);
		} else {
			layoutAddedNames.setVisibility(View.VISIBLE);
			layoutAddedNames.removeAllViews();
			for (int i = 0; i < addList.size(); ++i) {
				final View itemView = View.inflate(mContext, R.layout.item_medicine_name, null);
				final int pos = i;
				TextView tvName = (TextView) itemView.findViewById(R.id.tv);
				tvName.setText(addList.get(i).drugname);
				View vMove = itemView.findViewById(R.id.iv);
				vMove.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						layoutAddedNames.removeViewAt(pos);
						addList.remove(pos);
					}
				});
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				int dp10 = UIUtil.dip2px(mContext, 10);
				lp.setMargins(dp10, dp10, dp10, 0);
				layoutAddedNames.addView(itemView, lp);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_with_back_title_btn_left:
				goClassify();
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		lvSearchedNames.setVisibility(View.GONE);
		addList.add(searchList.get(position));
		addMedicineNames();
	}

	void goClassify() {
		YaodunActivityGroup parent = (YaodunActivityGroup) getParent();
		parent.goClassify();
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		mEtRenshenTime.setEnabled(rbRenshen.isChecked());
	}
}
