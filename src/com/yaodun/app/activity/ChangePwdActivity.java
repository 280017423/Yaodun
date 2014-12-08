package com.yaodun.app.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.qianjiang.framework.util.ImeUtil;
import com.qianjiang.framework.util.NetUtil;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;
import com.yaodun.app.R;
import com.yaodun.app.authentication.ActionResult;
import com.yaodun.app.req.UserReq;

/**
 * 修改密码界面
 * 
 * @author zou.sq
 */
public class ChangePwdActivity extends YaodunActivityBase implements OnClickListener {

	private EditText mEdtOriginPwd;
	private EditText mEdtNewPwd;
	private EditText mEdtNewPwdAgain;
	private boolean mIsRegister;
	private LoadingUpView mLoadingUpView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pwd);
		initVariable();
		initView();
	}

	private void initVariable() {
		mLoadingUpView = new LoadingUpView(this);
	}

	private void initView() {
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.title_text_change_pwd);

		mEdtOriginPwd = (EditText) findViewById(R.id.edt_origin_pwd);
		mEdtNewPwd = (EditText) findViewById(R.id.edt_new_pwd);
		mEdtNewPwdAgain = (EditText) findViewById(R.id.edt_new_pwd_again);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_change_pwd:
				checkAndSubmit();
				break;
			default:
				break;
		}
	}

	private void checkAndSubmit() {
		if (mIsRegister) {
			return;
		}
		ImeUtil.hideSoftInput(this);
		String originPwd = mEdtOriginPwd.getText().toString().trim();
		String newPwd = mEdtNewPwd.getText().toString().trim();
		String newPwdAgain = mEdtNewPwdAgain.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(originPwd)) {
			toast("请输入原始密码");
			return;
		}
		if (StringUtil.isNullOrEmpty(newPwd)) {
			toast("请输入新密码");
			return;
		}
		if (StringUtil.isNullOrEmpty(newPwdAgain)) {
			toast("请再次确认新密码");
			return;
		}
		if (!newPwd.equals(newPwdAgain)) {
			toast("请确保新密码一致");
			return;
		}
		if (!NetUtil.isNetworkAvailable()) {
			toast(getString(R.string.network_is_not_available));
			return;
		}
		mIsRegister = true;
		showLoadingUpView(mLoadingUpView);
		new AsyncLogin().execute(originPwd, newPwd);
	}

	class AsyncLogin extends AsyncTask<String, Void, ActionResult> {

		@Override
		protected ActionResult doInBackground(String... params) {
			String originPwd = "";
			String newPwd = "";
			if (params != null) {
				if (params.length > 0) {
					originPwd = params[0];
				}
				if (params.length > 1) {
					newPwd = params[1];
				}
			}
			return UserReq.changePwd(originPwd, newPwd);
		}

		@Override
		protected void onPostExecute(ActionResult result) {
			dismissLoadingUpView(mLoadingUpView);
			mIsRegister = false;
			if (result != null && ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {
				toast("恭喜，密码修改成功");
				finish();
			} else {
				showErrorMsg(result);
			}
		}
	}

}
