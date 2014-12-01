package com.yaodun.app.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.qianjiang.framework.authentication.BaseLoginProcessor;
import com.qianjiang.framework.authentication.BaseLoginProcessor.LOGIN_TYPE;
import com.qianjiang.framework.util.ImeUtil;
import com.qianjiang.framework.util.NetUtil;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;
import com.yaodun.app.R;
import com.yaodun.app.authentication.ActionResult;
import com.yaodun.app.authentication.LoginProcessor;
import com.yaodun.app.req.UserReq;
import com.yaodun.app.util.ConstantSet;

/**
 * 登录界面
 * 
 * @author zou.sq
 */
public class LoginActivity extends YaodunActivityBase implements OnClickListener {
	private static final int DIALOG_EXIT_APP = 0;
	private static final int REGISTER_ACTIVITY_REQUEST_CODE = 10;
	private static final int DELAY_TIME = 500;
	private static final String TAG = "LoginActivity";
	private boolean mIsLogining;
	private LOGIN_TYPE mLoginType;
	// 登录来源动作标记
	private String mIdentify;
	private EditText mEdtChildName;
	private EditText mEdtTel;
	private LoadingUpView mLoadingUpView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initVariable();
		initView();
		setListener();
	}

	private void setListener() {
	}

	private void initVariable() {
		mIdentify = getIntent().getStringExtra(BaseLoginProcessor.IDENTIFY);
		mLoginType = (LOGIN_TYPE) getIntent().getExtras().get(BaseLoginProcessor.KEY_LOGIN_TYPE);
	}

	private void initView() {
		mLoadingUpView = new LoadingUpView(this, true);
		mLoadingUpView.setCancelable(false);
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.button_title_login);
		mEdtChildName = (EditText) findViewById(R.id.edt_child_name);
		mEdtTel = (EditText) findViewById(R.id.edt_tel);
	}

	private void checkAndLogin() {
		if (mIsLogining) {
			return;
		}
		String childName = mEdtChildName.getText().toString();
		String edtTel = mEdtTel.getText().toString();
		if (StringUtil.isNullOrEmpty(childName) || StringUtil.isNullOrEmpty(edtTel)) {
			toast(getString(R.string.toast_login_error));
			return;
		}
		ImeUtil.hideSoftInput(LoginActivity.this);
		if (!NetUtil.isNetworkAvailable()) {
			toast(getString(R.string.network_is_not_available));
			return;
		}
		showLoadingUpView(mLoadingUpView);
		mIsLogining = true;
		new AsyncLogin().execute(childName, edtTel);
	}

	@Override
	public void onClick(final View v) {
		doActionAgain(TAG, DELAY_TIME, new ActionListener() {
			@Override
			public void doAction() {
				switch (v.getId()) {
					case R.id.btn_login:
						checkAndLogin();
						break;
					case R.id.btn_register:
						Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
						startActivityForResult(intent, REGISTER_ACTIVITY_REQUEST_CODE);
						break;

					default:
						break;
				}
			}
		});
	}

	/**
	 * 异步登录
	 * 
	 * @author zou.sq
	 * @since 2012-7-16 下午03:26:10
	 */
	class AsyncLogin extends AsyncTask<String, Void, ActionResult> {

		@Override
		protected ActionResult doInBackground(String... params) {
			String userName = "";
			String userTel = "";
			if (params != null) {
				if (params.length > 0) {
					userName = params[0];
				}
				if (params.length > 1) {
					userTel = params[1];
				}
			}
			return UserReq.login(userName, userTel);
		}

		@Override
		protected void onPostExecute(ActionResult result) {
			if (result != null && ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {
				if (LOGIN_TYPE.From_ActivityGroup_Tab.equals(mLoginType)) {
					jumpToActivityGroup(true);
				} else {
					LoginProcessor.getInstance().onLoginSuccess(LoginActivity.this, mIdentify);
				}
			} else {
				// 登录失败
				if (!LOGIN_TYPE.From_ActivityGroup_Tab.equals(mLoginType)) {
					LoginProcessor.getInstance().onLoginError(LoginActivity.this, mIdentify);
				}
				showErrorMsg(result);
			}
			dismissLoadingUpView(mLoadingUpView);
			mIsLogining = false;
		}
	}

	private void jumpToActivityGroup(boolean loginSuccess) {
		Intent intent = new Intent(this, MainActivityGroup.class);
		intent.putExtra(ConstantSet.EXTRA_LOGIN_STATUS, loginSuccess);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_EXIT_APP:
				return createDialogBuilder(this, getString(R.string.button_text_tips),
						getString(R.string.exit_dialog_title), getString(R.string.button_text_no),
						getString(R.string.button_text_yes)).create(id);
			default:
				break;
		}
		return super.onCreateDialog(id);
	}

	@Override
	public void onNegativeBtnClick(int id, DialogInterface dialog, int which) {
		switch (id) {
			case DIALOG_EXIT_APP:
				finish();
				break;
			default:
				break;
		}
		super.onNegativeBtnClick(id, dialog, which);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 是退出APP的那个LoginType时，才退出
		if (keyCode == KeyEvent.KEYCODE_BACK && mLoginType != null && mLoginType.equals(LOGIN_TYPE.Exit_To_Cancel_Apk)) {
			showDialog(DIALOG_EXIT_APP);
			return false;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (LOGIN_TYPE.From_ActivityGroup_Tab.equals(mLoginType)) {
				jumpToActivityGroup(false);
			} else {
				toBack();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void toBack() {
		doExitLoginActivity();
	}

	private void doExitLoginActivity() {
		// 底层已做关闭处理
		LoginProcessor.getInstance().onLoginCancel(this, mIdentify, MainActivityGroup.class);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (RESULT_OK == resultCode && REGISTER_ACTIVITY_REQUEST_CODE == requestCode && null != data) {
			// TODO 处理注册成功逻辑
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
