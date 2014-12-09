package com.yaodun.app.model;

import com.qianjiang.framework.orm.BaseModel;
import com.yaodun.app.util.DateUtil;

public class KnowledgeModel extends BaseModel {

	private static final long serialVersionUID = -7189942557184630918L;
	private String knowledgeId;
	private String title;
	private int type;
	private String img;
	private String createtime;
	private int countDiscuss;

	public String getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(String knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	public String getTitle() {
		return null == title ? "" : title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public int getCountDiscuss() {
		return countDiscuss;
	}

	public void setCountDiscuss(int countDiscuss) {
		this.countDiscuss = countDiscuss;
	}

	public String getDisplayTime() {
		return DateUtil.getTimeStr(com.qianjiang.framework.util.DateUtil.getSysDate(), createtime);
	}

}
