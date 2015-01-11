package com.pregnancy.app.model;

import com.pregnancy.app.util.DateUtil;
import com.qianjiang.framework.orm.BaseModel;

public class ConsultListModel extends BaseModel {

	private static final long serialVersionUID = -4257923920475674423L;
	private String questionId;
	private String description;
	private int replyStatus; // eplyStatus:1表示已经回复,0:表示未回复
	private String replyTime;

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

	public int getReplyStatus() {
		return replyStatus;
	}

	public void setReplyStatus(int replyStatus) {
		this.replyStatus = replyStatus;
	}

	public String getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	
	public String getDisplayTime() {
		return DateUtil.getTimeStr(com.qianjiang.framework.util.DateUtil.getSysDate(), replyTime);
	}

}
