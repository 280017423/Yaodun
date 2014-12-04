package com.yaodun.app.req;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import com.qianjiang.framework.app.JsonResult;
import com.qianjiang.framework.model.VersionInfo;
import com.qianjiang.framework.util.HttpClientUtil;
import com.qianjiang.framework.util.MessageException;
import com.qianjiang.framework.util.NetworkException;
import com.qianjiang.framework.util.PackageUtil;
import com.yaodun.app.listener.NewVersionListener;
import com.yaodun.app.util.ServerAPIConstant;

/**
 * 应用信息类
 * 
 * @author cui.yp
 * @version 1.1.0 2013-04-02 <br>
 */
public class AppReq {

	/**
	 * 检测新版本
	 * 
	 * @param listener
	 *            版本回调接口
	 */
	public static void checkVersion(NewVersionListener listener) {
		if (null == listener) {
			return;
		}
		try {
			// TODO
			String apiUrl = "";
			List<NameValuePair> postParams = new ArrayList<NameValuePair>();
			postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_VERSION, PackageUtil.getVersionCode() + ""));
			postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_APP, ServerAPIConstant.getAppSign()));
			postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_PROJECT_SIGN, ServerAPIConstant.PROJECT_SIGN)); // 3表示打卡app
			// 1 表示android
			postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_CLIENT_TYPE, "1"));
			JsonResult jsonResult;

			jsonResult = HttpClientUtil.get(apiUrl, postParams);
			if (jsonResult != null && jsonResult.isOK()) {
				VersionInfo version = jsonResult.getData(VersionInfo.class);
				listener.onUpdateReturned(version);
				return;
			}
		} catch (NetworkException e) {
			e.printStackTrace();
		} catch (MessageException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

}
