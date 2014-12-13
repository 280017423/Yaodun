package com.yaodun.app.model;

import com.qianjiang.framework.orm.BaseModel;

public class UserInfoModel extends BaseModel {
	private static final long serialVersionUID = -4763716679331905978L;
	private String userId;
	private String userName;
	private String email;
	private String password;
	private String gender;
	private String telephone;
	
	//上一次是第三方登录时，保存不到密码，就没办法用密码自动登录，所以要保存source和openid
	public String source;//哪个第三方平台
	public String openid;//授权后拿到的用户唯一标识

	public String getUserId() {
		return null == userId ? "" : userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return null == userName ? "" : userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return null == password ? "" : password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return null == email ? "" : email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return null == gender ? "" : gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getTelephone() {
		return null == telephone ? "" : telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Override
	public String toString() {
		return "UserInfoModel [userId=" + userId + ", userName=" + userName + ", email=" + email + ", password="
				+ password + ", gender=" + gender + ", telephone=" + telephone + "]";
	}

}
