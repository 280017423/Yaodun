package com.yaodun.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Selection;
import android.text.Spannable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.qianjiang.framework.util.ImeUtil;
import com.qianjiang.framework.util.NetUtil;
import com.qianjiang.framework.util.StringUtil;
import com.yaodun.app.R;
import com.yaodun.app.openapi.WeixinHelper;

/**
 * 微博分享页面
 * 
 * @author zeng.ww
 * @since 2012-8-1 上午10:02:26
 * @version 2012-8-1 上午10:02:26 zeng.ww 创建文件<br>
 *          2013-03-25 xu.xb 为点击分享到不同微博选项加入相同限制，即选择一项后，要400ms才能选择另外一项<br>
 *          2013-03-28 xu.xb 修复BUG： 重复分享人人网的提示信息建议修改 #47029<br>
 */
public class ShareToWeiboActivity extends YaodunActivityBase implements OnClickListener {
	private static final String TAG = "ShareToWeiboActivity";

	private boolean mAnimationIsStart;

	private EditText mShareContent;
	private View mPopupWindowView;

	private PopupWindow mPopupWindow;
	private Handler mHandler = new Handler();

	WeixinHelper weixinHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_share);
		initUI();
		weixinHelper = new WeixinHelper(this);
	}

	// 分享操作
	private void initUI() {
		mShareContent = (EditText) findViewById(R.id.et_share_content);
		mShareContent.setText("这是分享内容");
		TextView title = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
		title.setText("告诉好友");

		LinearLayout llRight = (LinearLayout) findViewById(R.id.title_with_back_title_btn_right);
		llRight.setOnClickListener(this);
		TextView btnRight = (TextView) findViewById(R.id.tv_title_with_right);
		btnRight.setText("分享");
		btnRight.setBackgroundResource(R.drawable.btn_logout_selector);

		View left = findViewById(R.id.title_with_back_title_btn_left);
		left.setOnClickListener(this);
		TextView tvLeft = (TextView) findViewById(R.id.tv_title_with_back_left);
		tvLeft.setBackgroundResource(R.drawable.btn_back_bg);

		// 把光标定位到内容的末尾
		CharSequence text = mShareContent.getText();
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}

	}

	/**
	 * 取消分享操作
	 */
	private void doBackAction(View view) {
		Button mCancleBtn = (Button) view.findViewById(R.id.btn_weibo_share_cancle);
		mCancleBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
	}

	/**
	 * 分享到新浪微博
	 */
	private void sinaShare(View view) {
		Button mSinaShareBtn = (Button) view.findViewById(R.id.btn_weibo_share_sina);
		mSinaShareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String message = mShareContent.getText().toString();
				if (StringUtil.isNullOrEmpty(message)) {
					toast(getResources().getString(R.string.weibo_share_tip_message_null));
					return;
				}
				if (!NetUtil.isNetworkAvailable()) {
					toast(getString(R.string.network_is_not_available));
					return;
				}

			}
		});
		
	      Button mWeixinShareBtn = (Button) view.findViewById(R.id.btn_weibo_share_qq);
	      mWeixinShareBtn.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                String message = mShareContent.getText().toString();
	                if (StringUtil.isNullOrEmpty(message)) {
	                    toast(getResources().getString(R.string.weibo_share_tip_message_null));
	                    return;
	                }
	                if (!NetUtil.isNetworkAvailable()) {
	                    toast(getString(R.string.network_is_not_available));
	                    return;
	                }
	                weixinHelper.setTitle("药盾");
	                weixinHelper.setDescription(message);
	                weixinHelper.shareToWeixin(false);
	            }
	        });

	}

	// 弹出对话框
	private void showWindow(View parent) {
		if (mPopupWindow == null) {
			mPopupWindowView = View.inflate(this, R.layout.weibo_item_list, null);
			mPopupWindow = new PopupWindow(mPopupWindowView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			sinaShare(mPopupWindowView);
			doBackAction(mPopupWindowView);
		}

		mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.alpha_black));
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景.
		mPopupWindow.setAnimationStyle(R.style.share_Popwindow_anim_style);
		mPopupWindowView.startAnimation(AnimationUtils.loadAnimation(ShareToWeiboActivity.this, R.anim.menu_show));
		mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		mPopupWindow.update();

		mPopupWindowView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				return true;
			}
		});
		mPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				mAnimationIsStart = false;
			}
		});
	}

	/**
	 * @param keyCode
	 *            按键
	 * @param event
	 *            事件 键盘按下事件
	 * @return boolean
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	@Override
	protected void onDestroy() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_with_back_title_btn_left:
				finish();
				break;
			case R.id.title_with_back_title_btn_right:
				String message = mShareContent.getText().toString().trim();
				if (StringUtil.isNullOrEmpty(message)) {
					toast("分享内容不能为空");
					return;
				}
				ImeUtil.hideSoftInput(ShareToWeiboActivity.this);
				showWindow(v);
				break;

			default:
				break;
		}

	}
}
