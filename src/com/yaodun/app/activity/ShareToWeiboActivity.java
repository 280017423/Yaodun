package com.yaodun.app.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.yaodun.app.openapi.SinaweiboHelper;
import com.yaodun.app.openapi.WeixinHelper;

/**
 * 微博分享页面
 * 
 * @author zou.sq
 */
public class ShareToWeiboActivity extends YaodunActivityBase implements OnClickListener {

	private EditText mShareContent;
	private View mPopupWindowView;

	private PopupWindow mPopupWindow;

	WeixinHelper weixinHelper;
	SinaweiboHelper weiboHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_share);
		initUI();
		weixinHelper = new WeixinHelper(this);
		weiboHelper = new SinaweiboHelper(this);
		weiboHelper.onCreate(savedInstanceState);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		weiboHelper.onNewIntent(intent);
	}

	// 分享操作
	private void initUI() {
		mShareContent = (EditText) findViewById(R.id.et_share_content);
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
				weiboHelper.setTitle(message);
				weiboHelper.shareToWeibo();
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
				// IWXAPI api = weixinHelper.getWeixinApi();
				// // 初始化一个WXTextObject对象
				// WXTextObject textObj = new WXTextObject();
				// textObj.text = "test";
				//
				// // 用WXTextObject对象初始化一个WXMediaMessage对象
				// WXMediaMessage msg = new WXMediaMessage();
				// msg.mediaObject = textObj;
				// // 发送文本类型的消息时，title字段不起作用
				// // msg.title = "Will be ignored";
				// msg.description = "test";
				//
				// // 构造一个Req
				// SendMessageToWX.Req req = new SendMessageToWX.Req();
				// req.transaction = weixinHelper.buildTransaction("text"); //
				// transaction字段用于唯一标识一个请求
				// req.message = msg;
				// req.scene = SendMessageToWX.Req.WXSceneSession;
				//
				// // 调用api接口发送数据到微信
				// api.sendReq(req);
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
