package com.pregnancy.app.model;

import com.pregnancy.app.util.DateUtil;
import com.qianjiang.framework.orm.BaseModel;

public class QuestionModel extends BaseModel {

	private static final long serialVersionUID = -4257923920475674423L;
	private String questionId;
	private String description;
	private String sendTime;

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
		return sendTime;
	}

	public void setTime(String time) {
		this.sendTime = time;
	}

	public String getDisplayTime() {
		return DateUtil.getTimeStr(com.qianjiang.framework.util.DateUtil.getSysDate(), sendTime);
	}

}
