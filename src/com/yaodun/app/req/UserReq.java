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
	 * 用户注册
	 * 
	 * @param model
	 *            用户对象
	 * @return ActionResult 请求结构数据
	 */
	public static ActionResult register(UserInfoModel model) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.ADD_INTERFACE);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_USERNAME, model.getUserName()));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_PASSWORD, model.getPassword()));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_EMAIL, model.getEmail()));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_TELEPHONE, model.getTelephone()));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_SEX, model.getGender()));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_BIRTHDAY, "2012-12-01"));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					UserInfoModel userInfoModel = jsonResult.getData(UserInfoModel.class);
					if (null != userInfoModel) {
						model.setUserId(userInfoModel.getUserId());
					}
					result.ResultObject = model; // 登录成功有积分提示语
				} else {
					result.ResultObject = jsonResult.Msg; // 登录成功有积分提示语
				}
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
	 * 用户登录
	 * 
	 * @param name
	 *            用户名字
	 * @param pwd
	 *            密码
	 * @return ActionResult 请求结构数据
	 */
	public static ActionResult login(String name, String pwd) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.LOGIN_INTERFACE);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_USERNAME, name));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_PASSWORD, pwd));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					UserInfoModel userInfoModel = jsonResult.getData(UserInfoModel.class);
					if (null != userInfoModel) {
						userInfoModel.setPassword(pwd);
					}
					// 保存用户信息
					UserMgr.saveUserInfo(userInfoModel);
				}
				result.ResultObject = jsonResult.Msg; // 登录成功有积分提示语
				result.ResultCode = jsonResult.Code;
			} else {
				result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
		}
		return result;
	}

	/**
	 * 修改密码
	 * 
	 * @param originPwd
	 *            原始密码
	 * @param newPwd
	 *            新密码
	 * @return ActionResult 请求结构数据
	 */
	public static ActionResult changePwd(String originPwd, String newPwd) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.CHANGE_PASSWORD);
		UserInfoModel model = UserMgr.getUserInfoModel();
		String userId = "";
		if (null != model) {
			userId = model.getUserId();
		}
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_USER_ID, userId));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_PASSWORD, newPwd));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_OLD_PASSWORD, originPwd));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					UserInfoModel userInfoModel = UserMgr.getUserInfoModel();
					if (null != userInfoModel) {
						userInfoModel.setPassword(newPwd);
						UserMgr.saveUserInfo(userInfoModel);
					}
				}
				result.ResultObject = jsonResult.Msg;
				result.ResultCode = jsonResult.Code;
			} else {
				result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
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
		if (null != user && !StringUtil.isNullOrEmpty(user.getUserName())
				&& !StringUtil.isNullOrEmpty(user.getPassword())) {
			result = login(user.getUserName(), user.getPassword());
		}
		return result;
	}
}
