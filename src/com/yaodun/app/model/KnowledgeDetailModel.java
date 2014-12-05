package com.yaodun.app.model;

import com.qianjiang.framework.orm.BaseModel;

public class KnowledgeDetailModel extends BaseModel {

	private static final long serialVersionUID = -7189942557184630918L;
	private String description;
	private String countAttention;
	private String createtime;

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCountAttention() {
		return countAttention;
	}

	public void setCountAttention(String countAttention) {
		this.countAttention = countAttention;
	}

}
