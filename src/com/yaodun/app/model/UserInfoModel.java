package com.yaodun.app.model;

import com.qianjiang.framework.orm.BaseModel;

public class UserInfoModel extends BaseModel {
	private static final long serialVersionUID = -4763716679331905978L;
	private String childrenId;
	private String name;
	private String mobile;
	private String sex;
	private String nClass;
	private String schoolId;
	private String schoolName;
	private int status;

	public String getChildrenId() {
		return childrenId;
	}

	public void setChildrenId(String childrenId) {
		this.childrenId = childrenId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getnClass() {
		return nClass;
	}

	public void setnClass(String nClass) {
		this.nClass = nClass;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
