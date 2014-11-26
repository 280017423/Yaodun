package com.yaodun.app.model;

import com.qianjiang.framework.orm.BaseModel;

public class SystemModel extends BaseModel {

	private static final long serialVersionUID = -4177757735676059776L;
	public static final String COLUMN_ITEM_KEY = "ITEM_KEY";
	public static final String COLUMN_ITEM_VALUE = "ITEM_VALUE";

	/**
	 * 键
	 */
	private String ItemKey;

	/**
	 * 值
	 */
	private String ItemValue;

	public String getItemKey() {
		return ItemKey;
	}

	public void setItemKey(String itemKey) {
		ItemKey = itemKey;
	}

	public String getItemValue() {
		return ItemValue;
	}

	public void setItemValue(String itemValue) {
		ItemValue = itemValue;
	}

}
