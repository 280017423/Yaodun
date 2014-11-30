package com.yaodun.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.qianjiang.framework.util.UIUtil;
import com.yaodun.app.R;
import com.yaodun.app.adapter.DetectRuleAdapter;
import com.yaodun.app.adapter.SearchedMedicineNameAdapter;
import com.yaodun.app.authentication.ActionResult;
import com.yaodun.app.model.DetectRuleBean;
import com.yaodun.app.model.MedicineBean;

/**
 * 药盾--查询页面
 * 
 * @author zou.sq
 */
public class YaodunSearchActivity extends YaodunActivityBase implements OnClickListener, OnItemClickListener {

	private boolean mIsSearching;
	private boolean mIsMedicineQuery;
	private TextView mTvTitle;
	private EditText mEtName;
	private ListView lvSearchedNames;
	private LinearLayout layoutAddedNames;
	private SearchedMedicineNameAdapter searchAdapter;
	private List<MedicineBean> searchList = new ArrayList<MedicineBean>();
	private List<MedicineBean> addList = new ArrayList<MedicineBean>();
	private ListView lvDetectRules;
	private DetectRuleAdapter detectAdapter;
	private List<DetectRuleBean> detectList = new ArrayList<DetectRuleBean>();

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yaodun_search);
		initView();
	}

	private void initView() {
		// title
		View left = findViewById(R.id.title_with_back_title_btn_left);
		left.setOnClickListener(this);
		TextView tvLeft = (TextView) findViewById(R.id.tv_title_with_back_left);
		tvLeft.setBackgroundResource(R.drawable.btn_back_bg);
		mTvTitle = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		mTvTitle.setText("大众用药查询");

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
				// return UserReq.searchMedicineName(keyword);
				ActionResult result = new ActionResult();
				result.ResultCode = ActionResult.RESULT_CODE_SUCCESS;
				List<MedicineBean> list = new ArrayList<MedicineBean>();
				MedicineBean m1 = new MedicineBean();
				m1.name = "阿莫";
				MedicineBean m2 = new MedicineBean();
				m2.name = "西林";
				list.add(m1);
				list.add(m2);
				result.ResultObject = list;
				return result;
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
	void doMedicineQuery() {
		if (mIsMedicineQuery) {
			return;
		}
		mIsMedicineQuery = true;
		new AsyncTask<String, Void, ActionResult>() {

			@Override
			protected ActionResult doInBackground(String... params) {
				// return UserReq.searchMedicineName(keyword);
				ActionResult result = new ActionResult();
				result.ResultCode = ActionResult.RESULT_CODE_SUCCESS;
				List<DetectRuleBean> list = new ArrayList<DetectRuleBean>();
				DetectRuleBean d1 = new DetectRuleBean();
				d1.title = "同一药物不同药名同时使用";
				d1.content = "如：属于重复用药，建议只使用其中的xxxxxxxxxxxxxx一种";
				DetectRuleBean d2 = new DetectRuleBean();
				d2.title = "同一药物不同药名同时使用";
				d2.content = "如：属于重复用药，建议只使用其中的xxxxxxxxxxxxxx一种";
				list.add(d1);
				list.add(d2);
				result.ResultObject = list;
				return result;
			}

			@Override
			protected void onPostExecute(ActionResult result) {
				if (result != null && ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {
					detectList.clear();
					List<DetectRuleBean> tmpList = (List<DetectRuleBean>) result.ResultObject;
					if (tmpList != null) {
						detectList.addAll(tmpList);
					}
					detectAdapter.notifyDataSetChanged();
				} else {
					showErrorMsg(result);
				}
				mIsMedicineQuery = false;
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
				tvName.setText(addList.get(i).name);
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
				doMedicineQuery();
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
}
