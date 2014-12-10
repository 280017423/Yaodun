package com.yaodun.app.model;

import com.qianjiang.framework.orm.BaseModel;

public class QuestionModel extends BaseModel {

	private static final long serialVersionUID = -4257923920475674423L;
	private String questionId;
	private String description;
	private String time;

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getDescription() {
		return null == description ? "" : description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDisplayTime() {
		return "11-12 14:20";
	}

}
