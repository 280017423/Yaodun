package com.yaodun.app.model;

import com.qianjiang.framework.orm.BaseModel;

public class QuestionDetailModel extends BaseModel {

	private static final long serialVersionUID = 4016934675526551345L;
	private String userQuestionReplyId;
	private String reply;
	private String replyTime;
	private int userType;

	public String getUserQuestionReplyId() {
		return userQuestionReplyId;
	}

	public void setUserQuestionReplyId(String userQuestionReplyId) {
		this.userQuestionReplyId = userQuestionReplyId;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

}
