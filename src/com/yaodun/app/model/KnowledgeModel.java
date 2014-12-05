package com.yaodun.app.model;

import com.qianjiang.framework.orm.BaseModel;

public class KnowledgeModel extends BaseModel {

	private static final long serialVersionUID = -7189942557184630918L;
	private String id;
	private String title;
	private int type;
	private String img;
	private String createtime;
	private int countDiscuss;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
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

}