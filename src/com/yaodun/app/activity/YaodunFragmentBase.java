package com.yaodun.app.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.qianjiang.framework.app.QJActivityBase;
import com.qianjiang.framework.imageloader.core.ImageLoader;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.QJActivityManager;
import com.qianjiang.framework.util.StringUtil;
import com.qianjiang.framework.widget.LoadingUpView;
import com.yaodun.app.R;
import com.yaodun.app.authentication.ActionResult;
import com.yaodun.app.listener.IDialogProtocol;
import com.yaodun.app.util.DialogManager;
import com.yaodun.app.widget.CustomDialog.Builder;

/**
 * 界面基类
 * 
 * @author zou.sq
 */
public class YaodunFragmentBase extends Fragment {
	protected final String TAG = getClass().getSimpleName();
	protected ImageLoader mImageLoader = ImageLoader.getInstance();

	/**
	 * toast显示错误消息
	 * 
	 * @param result
	 */
	protected void showErrorMsg(ActionResult result) {
		showErrorToast(result, getResources().getString(R.string.network_is_not_available), false);
	}

	/**
	 * toast显示错误消息
	 * 
	 * @param result
	 * @param noMsgReturnToast
	 *            若result.obj中没有信息，是否显示默认信息
	 */
	protected void showErrorMsg(ActionResult result, boolean noMsgReturnToast) {
		showErrorToast(result, getResources().getString(R.string.network_is_not_available), noMsgReturnToast);
	}

	/**
	 * toast显示错误消息
	 * 
	 * @param result
	 * @param netErrorMsg
	 *            网络异常时显示消息
	 */
	protected void showErrorMsg(ActionResult result, String netErrorMsg) {
		showErrorToast(result, netErrorMsg, false);
	}

	/**
	 * 显示错误消息
	 * 
	 * @param result
	 * @param netErrorMsg
	 *            网络异常时显示消息
	 * @param 是否需要无返回值的提示
	 */
	private void showErrorToast(ActionResult result, String netErrorMsg, boolean noMsgReturnToast) {
		if (result == null) {
			return;
		}
		if (ActionResult.RESULT_CODE_NET_ERROR.equals(result.ResultCode)) {
			toast(netErrorMsg);
		} else if (result.ResultObject != null) {
			// 增加RESULT_CODE_ERROR值时也弹出网络异常
			if (ActionResult.RESULT_CODE_NET_ERROR.equals(result.ResultObject.toString())) {
				toast(netErrorMsg);
			} else {
				toast(result.ResultObject.toString());
			}
		} else if (noMsgReturnToast) {
			toast(getResources().getString(R.string.no_return_data));
		}

	}

	protected boolean showLoadingUpView(LoadingUpView loadingUpView) {
		if (loadingUpView != null && !loadingUpView.isShowing()) {
			loadingUpView.showPopup();
			return true;
		}
		return false;
	}

	protected boolean dismissLoadingUpView(LoadingUpView loadingUpView) {
		if (loadingUpView != null && loadingUpView.isShowing()) {
			loadingUpView.dismiss();
			return true;
		}
		return false;
	}

	/**
	 * 默认的toast方法，该方法封装下面的两点特性：<br>
	 * 1、只有当前activity所属应用处于顶层时，才会弹出toast；<br>
	 * 2、默认弹出时间为 Toast.LENGTH_SHORT;
	 * 
	 * @param msg
	 *            弹出的信息内容
	 */
	public void toast(final String msg) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!StringUtil.isNullOrEmpty(msg)) {
					Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
					TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
					// 用来防止某些系统自定义了消息框
					if (tv != null) {
						tv.setGravity(Gravity.CENTER);
					}
					toast.show();
				}
			}
		});
	}
}
