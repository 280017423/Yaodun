package com.yaodun.app.manager;

import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.util.HttpClientUtil;
import com.qianjiang.framework.util.StringUtil;
import com.yaodun.app.authentication.LoginProcessor;
import com.yaodun.app.model.UserInfoModel;
import com.yaodun.app.util.ConstantSet;
import com.yaodun.app.util.DBUtil;
import com.yaodun.app.util.SharedPreferenceUtil;

/**
 * 用户相关逻辑类
 * 
 */
public class UserMgr {
	private static String USER_INFO_SHAREDPREFERENCE_NAME = "USER_INFO_MODEL";

	private UserMgr() {
	}

	/**
	 * 用户登出
	 */
	public static void logout() {
		// 清除本地的用户信息
		clearUserInfoModel();
		clearConfigInfo();
		// 清除所有的用户数据
		DBUtil.clearAllTables();
		// 重置本地状态
		LoginProcessor.getInstance().setLoginStatus(false);
		// 还原Cookies
		HttpClientUtil.setCookieStore(null);
	}

	/**
	 * 保存用户信息到本地
	 * 
	 * @param userInfoModel
	 *            用户信息
	 * @Date 2014-9-28 下午12:01:06
	 * 
	 */
	public static void saveUserInfo(UserInfoModel userInfoModel) {
		if (userInfoModel == null) {
			return;
		}
		SharedPreferenceUtil.saveObject(QJApplicationBase.CONTEXT, USER_INFO_SHAREDPREFERENCE_NAME, userInfoModel);
	}

	/**
	 * 清除本地用户信息
	 * 
	 * @Date 2014-9-28 下午12:01:06
	 * 
	 */
	public static void clearUserInfoModel() {
		SharedPreferenceUtil.clearObject(QJApplicationBase.CONTEXT, USER_INFO_SHAREDPREFERENCE_NAME);
	}

	/**
	 * 清除本地配置信息
	 * 
	 * @Date 2014-9-28 下午12:01:06
	 * 
	 */
	public static void clearConfigInfo() {
		SharedPreferenceUtil.clearObject(QJApplicationBase.CONTEXT, ConstantSet.FILE_JYT_CONFIG);
	}

	/**
	 * 获取本地用户信息
	 * 
	 * @Date 2014-9-28 下午12:01:06
	 * @return UserInfoModel 用户信息
	 * 
	 */
	public static UserInfoModel getUserInfoModel() {
		UserInfoModel userInfoModel = (UserInfoModel) SharedPreferenceUtil.getObject(QJApplicationBase.CONTEXT,
				USER_INFO_SHAREDPREFERENCE_NAME, UserInfoModel.class);
		return userInfoModel;
	}

	/**
	 * 数据库是否已经存了用户信息
	 * 
	 * @return 数据库是否已经存了用户信息
	 */
	public static boolean hasUserInfo() {
		UserInfoModel userInfoModel = getUserInfoModel();
		return userInfoModel != null && !StringUtil.isNullOrEmpty(userInfoModel.getChildrenId());
	}

}
