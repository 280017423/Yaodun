package com.yaodun.app.model;

import com.qianjiang.framework.orm.BaseModel;

public class UserInfoModel extends BaseModel {
	private static final long serialVersionUID = -4763716679331905978L;
	private String userId;
	private String userName;
	private String password;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
