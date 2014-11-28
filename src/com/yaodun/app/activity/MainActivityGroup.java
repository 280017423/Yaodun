package com.yaodun.app.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.qianjiang.framework.model.VersionInfo;
import com.qianjiang.framework.util.AppDownloader;
import com.qianjiang.framework.util.AppDownloader.DownloadProgressListener;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.HttpClientUtil;
import com.qianjiang.framework.util.QJActivityManager;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.BottomTab;
import com.qianjiang.framework.widget.BottomTab.OnBottomCheckedListener;
import com.yaodun.app.R;
import com.yaodun.app.listener.NewVersionListener;
import com.yaodun.app.req.AppReq;
import com.yaodun.app.widget.CustomProgressDialog;
import com.yaodun.app.widget.CustomProgressDialog.DIALOG_DEFAULT_LAYOUT_TYPE;

/**
 * 界面基类
 * 
 * @author zou.sq
 */
public class MainActivityGroup extends YaodunActivityBase {
	public static MainActivityGroup INSTANCE;
	private static final String TAG = "MainActivityGroup";
	private static final int DIALOG_HAS_NEW_VERSION = 0;
	private static final int DIALOG_EXIT_APP = 1;
	private RelativeLayout mRlContainer;
	private BottomTab mBottomTab;
	private VersionInfo mCurrentVersionInfo;
	// 获取MaxID是否成功
	private Class<?>[] mClasses = {
			YaodunActivityGroup.class,
			MedicineKnowledgeActivity.class,
			DoctorConsultActivity.class,
			MoreActivity.class };
	private Handler mCheckVersionHandler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) {
			if (DIALOG_HAS_NEW_VERSION == msg.what) {
				showDialog(DIALOG_HAS_NEW_VERSION);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_group);
		INSTANCE = this;
		initView();
		// checkUpdate();
		initStartIndex();
	}

	private void initStartIndex() {
		int msgType = 0;
		// 默认显示第一个界面
		mBottomTab.setSelectedIndex(msgType);
	}

	private void checkUpdate() {
		EvtLog.d(TAG, "检查是否有新版本");
		new Thread(new Runnable() {

			@Override
			public void run() {
				AppReq.checkVersion(new NewVersionListener() {

					@Override
					public void onUpdateReturned(VersionInfo versionInfo) {
						if (null != versionInfo && VersionInfo.HAS_NEW_VERSION == versionInfo.hasNew) {
							mCurrentVersionInfo = versionInfo;
							mCheckVersionHandler.obtainMessage();
							mCheckVersionHandler.sendEmptyMessage(DIALOG_HAS_NEW_VERSION);
						}
					}
				});
			}
		}).start();
	}

	private void initView() {
		mRlContainer = (RelativeLayout) findViewById(R.id.rl_main_container);
		mBottomTab = (BottomTab) findViewById(R.id.bottom_tab);
		mBottomTab.setOnBottomCheckedListener(new OnBottomCheckedListener() {
			@Override
			public void onBottomCheckedListener(int checkedPos) {
				setCurActivity(null, checkedPos);
			}
		});
	}

	private void setCurActivity(Intent intent, int index) {
		if (index >= mClasses.length) {
			return;
		}
		mRlContainer.removeAllViews();
		Class<?> targetClass = mClasses[index];
		// 如果没有，就new，有的话，直接传递数据
		if (intent == null) {
			intent = new Intent(this, targetClass);
		} else {
			intent.setClass(this, targetClass);
		}
		View activityView = getLocalActivityManager().startActivity(intent.getComponent().getShortClassName(), intent)
				.getDecorView();
		mRlContainer.addView(activityView, new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog(DIALOG_EXIT_APP);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			YaodunActivityBase activity = (YaodunActivityBase) getLocalActivityManager().getCurrentActivity();
			activity.handleActivityResult(requestCode, resultCode, data);
			EvtLog.d(TAG, "onActivityResult");
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_HAS_NEW_VERSION:
				String updateLog = StringUtil.isNullOrEmpty(mCurrentVersionInfo.appLogs) ? "" : mCurrentVersionInfo.appLogs;
				String updateTitleStr = getString(R.string.more_check_newversion_btn_update_later);
				boolean isCancelable = true;
				if (mCurrentVersionInfo.isForceUpdate()) {
					updateTitleStr = getString(R.string.more_check_newversion_exit);
					isCancelable = false;
				}
				Dialog updateDialog = createDialogBuilder(this,
						getString(R.string.more_check_newversion_dlg_title, mCurrentVersionInfo.appVersionNo),
						updateLog, updateTitleStr, getString(R.string.more_check_newversion_btn_update)).create(id);
				// 小米手机，默认点击对话框外部也可关闭，需要多一部设置
				updateDialog.setCanceledOnTouchOutside(false);
				updateDialog.setCancelable(isCancelable);
				return updateDialog;
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
				HttpClientUtil.setCookieStore(null);
				finish();
				break;
			case DIALOG_HAS_NEW_VERSION:
				// 如果是强制更新，取消需要关闭应用
				if (mCurrentVersionInfo != null && mCurrentVersionInfo.isForceUpdate()) {
					// 强制升级
					doForceUpdate();
				} else {
					// 普通升级
					doNormalUpdate();
				}
				break;
			default:
				break;
		}
		super.onNegativeBtnClick(id, dialog, which);
	}

	private void doNormalUpdate() {
		EvtLog.d(TAG, "开始升级");
		AppDownloader loader = new AppDownloader(MainActivityGroup.this, new DownloadProgressListener() {

			@Override
			public void onError(boolean isUpgradeMust) {
				EvtLog.d(TAG, "下载失败");
			}

			@Override
			public void onDownloading(int progress, int max) {
				EvtLog.d(TAG, max + "：下载中》》》" + progress);

			}

			@Override
			public void onDownloadComplete() {
				EvtLog.d(TAG, "下载完成");
			}
		});
		loader.download(mCurrentVersionInfo.appPath, Double.valueOf(mCurrentVersionInfo.appSize), false);
	}

	private void doForceUpdate() {
		final CustomProgressDialog customProgressDialog = new CustomProgressDialog(
				this, DIALOG_DEFAULT_LAYOUT_TYPE.FORCE_UPDATE);
		customProgressDialog.show();
		AppDownloader loader = new AppDownloader(MainActivityGroup.this, new DownloadProgressListener() {

			@Override
			public void onError(boolean isUpgradeMust) {
				EvtLog.d(TAG, "下载失败");
				customProgressDialog.dismiss();
				toast("强制升级失败，退出应用");
				exitApp();
			}

			@Override
			public void onDownloading(final int progress, final int max) {
				EvtLog.d(TAG, max + "：下载中》》》" + progress);
				ProgressBar progressBar = customProgressDialog.getProgressBar();
				final TextView tvProgress = customProgressDialog.getProgressText();
				if (progressBar != null) {
					if (progressBar.getMax() == 0) {
						progressBar.setMax(max);
					}
					progressBar.setProgress(progress);
				}
				if (tvProgress != null) {
					runOnUiThread(new Runnable() {
						public void run() {
							tvProgress.setText("当前进度: " + progress + "%");
						}
					});

				}

			}

			@Override
			public void onDownloadComplete() {
				EvtLog.d(TAG, "下载完成");
				customProgressDialog.dismiss();
				exitApp();
			}
		});
		loader.download(mCurrentVersionInfo.appPath, Double.valueOf(mCurrentVersionInfo.appSize), true);
	}

	@Override
	public void onPositiveBtnClick(int id, DialogInterface dialog, int which) {
		super.onPositiveBtnClick(id, dialog, which);
		switch (id) {
			case DIALOG_HAS_NEW_VERSION:
				if (mCurrentVersionInfo != null && mCurrentVersionInfo.isForceUpdate()) {
					// 如果是强制更新，取消需要关闭应用
					exitApp();
				}
				break;

			default:
				break;
		}
	}

	private void exitApp() {
		// 退出其他的所有的存在的Activity
		QJActivityManager.getInstance().popAllExceptOne(getClass());
		// 退出时，因为有推送服务，所以进程没关闭，需要还原Cookies
		HttpClientUtil.setCookieStore(null);
		finish();
	}

}
