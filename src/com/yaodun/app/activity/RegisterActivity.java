package com.yaodun.app.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.qianjiang.framework.util.ImeUtil;
import com.qianjiang.framework.util.NetUtil;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;
import com.yaodun.app.R;
import com.yaodun.app.authentication.ActionResult;
import com.yaodun.app.model.UserInfoModel;
import com.yaodun.app.req.UserReq;
import com.yaodun.app.util.ConstantSet;

/**
 * 注册界面
 * 
 * @author zou.sq
 */
public class RegisterActivity extends YaodunActivityBase implements OnClickListener {

	private EditText mEdtPhone;
	private EditText mEdtPwd;
	private EditText mEdtPwdAgain;
	private EditText mEdtNickname;
	private int mSexValue;
	private boolean mIsRegister;
	private LoadingUpView mLoadingUpView;
	private RadioGroup mRgSex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initVariable();
		initView();
		setListener();
	}

	private void setListener() {
		mRgSex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (R.id.radio_male == checkedId) {
					mSexValue = 0;
				} else {
					mSexValue = 1;
				}
			}
		});
	}

	private void initVariable() {
		mLoadingUpView = new LoadingUpView(this);
	}

	private void initView() {
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.title_text_register);

		mEdtPhone = (EditText) findViewById(R.id.edt_child_phone);
		mEdtPwd = (EditText) findViewById(R.id.edt_child_pwd);
		mEdtPwdAgain = (EditText) findViewById(R.id.edt_child_pwd_again);
		mEdtNickname = (EditText) findViewById(R.id.edt_child_nickname);
		mRgSex = (RadioGroup) findViewById(R.id.rg_sex);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_register:
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
		String phone = mEdtPhone.getText().toString().trim();
		String pwd = mEdtPwd.getText().toString().trim();
		String pwdAgain = mEdtPwdAgain.getText().toString().trim();
		String nickname = mEdtNickname.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(phone)) {
			toast("请输入手机号");
			return;
		}
		if (StringUtil.isNullOrEmpty(pwd)) {
			toast("请输入密码");
			return;
		}
		if (StringUtil.isNullOrEmpty(pwdAgain)) {
			toast("请再次确认密码");
			return;
		}
		if (!pwd.equals(pwdAgain)) {
			toast("请确保密码一致");
			return;
		}
		if (StringUtil.isNullOrEmpty(nickname)) {
			toast("请输入昵称");
			return;
		}
		if (!NetUtil.isNetworkAvailable()) {
			toast(getString(R.string.network_is_not_available));
			return;
		}
		mIsRegister = true;
		showLoadingUpView(mLoadingUpView);
		new AsyncLogin().execute(nickname, pwd, phone);
	}

	class AsyncLogin extends AsyncTask<String, Void, ActionResult> {

		@Override
		protected ActionResult doInBackground(String... params) {
			String nickname = "";
			String pwd = "";
			String phone = "";
			if (params != null) {
				if (params.length > 0) {
					nickname = params[0];
				}
				if (params.length > 1) {
					pwd = params[1];
				}
				if (params.length > 2) {
					phone = params[2];
				}
			}
			UserInfoModel model = new UserInfoModel();
			model.setUserName(nickname);
			model.setPassword(pwd);
			model.setEmail("");
			model.setGender(mSexValue + "");
			model.setTelephone(phone);
			return UserReq.register(model);
		}

		@Override
		protected void onPostExecute(ActionResult result) {
			if (result != null && ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {
				toast("恭喜，注册成功");
				Intent intent = new Intent(RegisterActivity.this, LoadingActivity.class);
				intent.putExtra(ConstantSet.KEY_USERINFOMODEL, (UserInfoModel) result.ResultObject);
				setResult(RESULT_OK, intent);
				finish();
			} else {
				showErrorMsg(result);
			}
			dismissLoadingUpView(mLoadingUpView);
			mIsRegister = false;
		}
	}

}
