package com.pregnancy.app.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.KeyEvent;

import com.pregnancy.app.R;
import com.pregnancy.app.util.ConstantSet;
import com.pregnancy.app.util.SharedPreferenceUtil;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.PackageUtil;
import com.qianjiang.framework.util.StringUtil;

/**
 * 启动界面
 * 
 * @author zou.sq
 */
public class LoadingActivity extends YaodunActivityBase {
	private static final int DISPLAY_TIME = 3000;
	private static final String TAG = "LoadingActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		testJump();
	}

	private void testJump() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (isJumpNewerGuiding()) {
					startActivity(new Intent(LoadingActivity.this, NewerGuidingActivity.class));
				} else {
					startActivity(new Intent(LoadingActivity.this, MainActivityGroup.class));
				}
				finish();
			}
		}, DISPLAY_TIME);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean isJumpNewerGuiding() {

		String versionString = SharedPreferenceUtil.getStringValueByKey(LoadingActivity.this,
				ConstantSet.KEY_NEWER_GUIDING_FILE, ConstantSet.KEY_NEWER_GUIDING_FINISH);

		int code = -1;
		try {
			code = PackageUtil.getVersionCode();
		} catch (NameNotFoundException e) {
			EvtLog.w(TAG, e);
		}
		if (!StringUtil.isNullOrEmpty(versionString) && versionString.equals(code + "")) {
			return false;
		}
		return true;
	}

}
