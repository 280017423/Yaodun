package com.pregnancy.app.activity;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.pregnancy.app.R;
import com.pregnancy.app.authentication.ActionResult;
import com.pregnancy.app.authentication.LoginProcessor;
import com.pregnancy.app.manager.UserMgr;
import com.pregnancy.app.model.UserInfoModel;
import com.pregnancy.app.req.UserReq;
import com.pregnancy.app.util.ConstantSet;
import com.qianjiang.framework.authentication.BaseLoginProcessor;
import com.qianjiang.framework.authentication.BaseLoginProcessor.LOGIN_TYPE;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.ImeUtil;
import com.qianjiang.framework.util.NetUtil;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

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
	private EditText mEdtPwd;
	private LoadingUpView mLoadingUpView;
	private Tencent mTencent;
	private IWXAPI mWxApi;
	BroadcastReceiver wxReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if (arg1.hasExtra(ConstantSet.EXTRA_OPENAPI_AUTH_RESPONSE)) {
				String authResponse = arg1.getStringExtra(ConstantSet.EXTRA_OPENAPI_AUTH_RESPONSE);
				EvtLog.d(TAG, "wxReceiver, " + authResponse);
				try {
					JSONObject jobj = new JSONObject(authResponse);
					openAPILogin(OpenAPILoginSource.weixin, jobj.getString("openid"));
				} catch (Exception e) {
					EvtLog.e(TAG, e);
				}
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initVariable();
		initView();
		setListener();
		registerReceiver(wxReceiver, new IntentFilter(ConstantSet.ACTION_WEIXIN_LOGIN));
	}

	private void setListener() {
	}

	private void initVariable() {
		mTencent = Tencent.createInstance(ConstantSet.APP_ID_QQ, getApplicationContext());
		mIdentify = getIntent().getStringExtra(BaseLoginProcessor.IDENTIFY);
		mLoginType = (LOGIN_TYPE) getIntent().getExtras().get(BaseLoginProcessor.KEY_LOGIN_TYPE);
	}

	private void initView() {
		mLoadingUpView = new LoadingUpView(this, true);
		mLoadingUpView.setCancelable(false);
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.button_title_login);
		mEdtChildName = (EditText) findViewById(R.id.edt_child_name);
		mEdtPwd = (EditText) findViewById(R.id.edt_tel);
	}

	private void checkAndLogin() {
		if (mIsLogining) {
			return;
		}
		String childName = mEdtChildName.getText().toString();
		String edtTel = mEdtPwd.getText().toString();
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
					case R.id.btn_login_qq:
						doQQLogin();
						break;
					case R.id.btn_login_weixin:
						doWeixinLogin();
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
				// 报告用户名字
				CrashReport.setUserId(UserMgr.getUserInfoModel().getUserName());
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

	enum OpenAPILoginSource {
		QQ, weixin,
	}

	void openAPILogin(final OpenAPILoginSource source, final String openId) {
		new AsyncTask<String, Void, ActionResult>() {

			@Override
			protected ActionResult doInBackground(String... params) {
				return UserReq.openAPILogin(source.name(), openId);
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
		}.execute();
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
			UserInfoModel userInfoModel = (UserInfoModel) data.getSerializableExtra(ConstantSet.KEY_USERINFOMODEL);
			mEdtChildName.setText(userInfoModel.getUserName());
			mEdtPwd.setText(userInfoModel.getPassword());
			checkAndLogin();
		}
		super.onActivityResult(requestCode, resultCode, data);
		// if (null != mTencent) {
		// mTencent.onActivityResult(requestCode, resultCode, data);
		// }
	}

	void doQQLogin() {
		if (mTencent.isSessionValid()) {
			openAPILogin(OpenAPILoginSource.QQ, mTencent.getOpenId());
		} else {
			mTencent.login(this, "all", new IUiListener() {

				@Override
				public void onError(UiError arg0) {
					EvtLog.d(TAG, "onError");
				}

				@Override
				public void onComplete(Object arg0) {
					EvtLog.d(TAG, arg0.toString());
					try {
						JSONObject jobj = new JSONObject(arg0.toString());
						openAPILogin(OpenAPILoginSource.QQ, jobj.getString("openid"));
					} catch (Exception e) {
						EvtLog.e(TAG, e);
					}
				}

				@Override
				public void onCancel() {
					EvtLog.d(TAG, "onCancel");
				}
			});
		}
	}

	void doWeixinLogin() {
		if (mWxApi == null) {
			mWxApi = WXAPIFactory.createWXAPI(this, ConstantSet.APP_ID_WX);
			boolean regOk = mWxApi.registerApp(ConstantSet.APP_ID_WX);
			EvtLog.d(TAG, "registerApp, " + regOk);
		}
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		// req.scope = "token";
		req.state = "none";
		mWxApi.sendReq(req);
	}

	@Override
	protected void onDestroy() {
		if (null != wxReceiver) {
			unregisterReceiver(wxReceiver);
		}
		super.onDestroy();
	}
}
