package com.yaodun.app.model;

import com.qianjiang.framework.orm.BaseModel;

public class CommonModel extends BaseModel {

	private static final long serialVersionUID = 2057130185343686474L;
	private String id;
	private String title;
	private String data;
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return null == title ? "" : title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getData() {
		return null == data ? "" : data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
