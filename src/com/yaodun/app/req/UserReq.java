package com.yaodun.app.req;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.qianjiang.framework.app.JsonResult;
import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.util.HttpClientUtil;
import com.qianjiang.framework.util.StringUtil;
import com.yaodun.app.R;
import com.yaodun.app.authentication.ActionResult;
import com.yaodun.app.manager.UserMgr;
import com.yaodun.app.model.UserInfoModel;
import com.yaodun.app.util.ServerAPIConstant;

/**
 * 用户请求类
 * 
 * @author zou.sq
 */
public class UserReq {

	/**
	 * 用户登录
	 * 
	 * @param name
	 *            用户名字
	 * @param tel
	 *            用户电话
	 * @return ActionResult 请求结构数据
	 */
	public static ActionResult login(String name, String tel) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.LOGIN_API);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_CHILREN_NAME, name));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_MOBILE, tel));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_APP, ServerAPIConstant.getAppSign()));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					UserInfoModel userInfoModel = jsonResult.getData(ServerAPIConstant.KEY_CHILDREN_INFO,
							UserInfoModel.class);
					// 保存用户信息
					UserMgr.saveUserInfo(userInfoModel);
				}
				result.ResultObject = jsonResult.Msg; // 登录成功有积分提示语
				result.ResultCode = jsonResult.Code;
			} else {
				result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
				result.ResultObject = QJApplicationBase.CONTEXT.getString(R.string.network_is_not_available);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
		}
		return result;
	}

	/**
	 * 自动登录
	 * 
	 * @return true: 成功 false:失败
	 */
	public static ActionResult autoLogin() {
		ActionResult result = new ActionResult();
		UserInfoModel user = UserMgr.getUserInfoModel();
		if (null != user && !StringUtil.isNullOrEmpty(user.getName()) && !StringUtil.isNullOrEmpty(user.getMobile())) {
			result = login(user.getName(), user.getMobile());
		}
		return result;
	}
}
