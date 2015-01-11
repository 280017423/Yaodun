package com.pregnancy.app.model;

import com.pregnancy.app.util.DateUtil;
import com.qianjiang.framework.orm.BaseModel;

public class MyQuestionModel extends BaseModel {

	private static final long serialVersionUID = -4257923920475674423L;
	private String userQuestionReplyId;
	private String reply;
	private String replyTime;
	private int userType; // 1表示是药师,0:表示是用户

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

	public String getDisplayTime() {
		return DateUtil.getTimeStr(com.qianjiang.framework.util.DateUtil.getSysDate(), replyTime);
	}
}
