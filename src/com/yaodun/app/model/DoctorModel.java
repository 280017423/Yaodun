package com.yaodun.app.model;

import com.qianjiang.framework.orm.BaseModel;

public class DoctorModel extends BaseModel {

	private static final long serialVersionUID = -7189942557184630918L;
	private int status; // status:0 表示未关注，1表示已关注
	private String doctorId;
	private String doctorName;
	private String professional;
	private String description;
	private String img;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return null == doctorName ? "" : doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getProfessional() {
		return null == professional ? "" : professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

	public String getDescription() {
		return null == description ? "" : description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
