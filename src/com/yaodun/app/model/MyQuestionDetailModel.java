package com.yaodun.app.model;

import java.util.ArrayList;
import java.util.List;

import com.qianjiang.framework.orm.BaseModel;

public class MyQuestionDetailModel extends BaseModel {

	private static final long serialVersionUID = -4257923920475674423L;
	private String question;
	private String createtime;
	private String doctorId;
	private String doctorName;
	private String doctorImg;
	private String professional;
	private String doctorDescription;
	private int attionStatus;
	private List<MyQuestionModel> questionreply;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDoctorImg() {
		return doctorImg;
	}

	public void setDoctorImg(String doctorImg) {
		this.doctorImg = doctorImg;
	}

	public String getDoctorDescription() {
		return doctorDescription;
	}

	public void setDoctorDescription(String doctorDescription) {
		this.doctorDescription = doctorDescription;
	}

	public int getAttionStatus() {
		return attionStatus;
	}

	public void setAttionStatus(int attionStatus) {
		this.attionStatus = attionStatus;
	}

	public List<MyQuestionModel> getQuestionList() {
		return null == questionreply ? new ArrayList<MyQuestionModel>() : questionreply;
	}

	public void setQuestionList(List<MyQuestionModel> questionList) {
		this.questionreply = questionList;
	}

	public String getProfessional() {
		return null == professional ? "" : professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

}
