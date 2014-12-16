package com.yaodun.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qianjiang.framework.util.WeixinUtil;
import com.yaodun.app.R;
import com.yaodun.app.manager.UserMgr;
import com.yaodun.app.openapi.WeixinHelper;
import com.yaodun.app.util.ConstantSet;

/**
 * 更多界面
 * 
 * @author zou.sq
 */
public class MoreActivity extends YaodunActivityBase implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		initView();
	}

	private void initView() {
		TextView titleTextView = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		titleTextView.setText(R.string.text_more);
		LinearLayout llRight = (LinearLayout) findViewById(R.id.title_with_back_title_btn_right);
		llRight.setOnClickListener(this);
		TextView btnRight = (TextView) findViewById(R.id.tv_title_with_right);
		btnRight.setText(R.string.button_logout);
		btnRight.setBackgroundResource(R.drawable.btn_logout_selector);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_more_about:
				jumpToActivity(CommonActivity.class, ConstantSet.EXTRA_ABOUT);
				break;
			case R.id.rl_more_introduce:
				jumpToActivity(CommonActivity.class, ConstantSet.EXTRA_INTRODUCE);
				break;
			case R.id.rl_more_declare:
				jumpToActivity(CommonActivity.class, ConstantSet.EXTRA_DECLARE);
				break;
			case R.id.rl_more_suggest:
				jumpToActivity(FeedBackActivity.class, 0);
				break;
			case R.id.rl_more_share:
				jumpToActivity(ShareToWeiboActivity.class, 0);
				break;
			case R.id.rl_change_pwd:
				jumpToActivity(ChangePwdActivity.class, 0);
				break;
			case R.id.rl_more_collect:
				jumpToActivity(MyCollectActivity.class, 0);
				break;
			case R.id.rl_more_my_consult:
				jumpToActivity(MyConsultActivity.class, 0);
				break;
			case R.id.rl_more_my_attention:
			case R.id.rl_more_check_version:
				Toast.makeText(MoreActivity.this, "正在开发中...", Toast.LENGTH_LONG).show();
				break;
			case R.id.title_with_back_title_btn_right:
				logout();
				break;
			default:
				break;
		}
	}

	private void jumpToActivity(Class<?> cls, int value) {
		Intent intent = new Intent(MoreActivity.this, cls);
		intent.putExtra(ConstantSet.EXTRA_JUMP_FLAG, value);
		startActivity(intent);
	}

	private void logout() {
		UserMgr.logout();
		MainActivityGroup activityGroup = (MainActivityGroup) getParent();
		activityGroup.initStartIndex();
	}

}
