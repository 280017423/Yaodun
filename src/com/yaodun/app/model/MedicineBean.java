package com.yaodun.app.model;

import com.qianjiang.framework.orm.BaseModel;

public class MedicineBean extends BaseModel {
	private static final long serialVersionUID = -7762357462973968709L;
	private String basicId;
	private String drugname;
	private String productName;
	private String unit;
	private String usage;
	private String dosage;// 用量+用量单位
	private String frequery;// 频次

	public String getBasicId() {
		return null == basicId ? "" : basicId;
	}

	public void setBasicId(String basicId) {
		this.basicId = basicId;
	}

	public String getDrugname() {
		return null == drugname ? "" : drugname;
	}

	public void setDrugname(String drugname) {
		this.drugname = drugname;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUsage() {
		return null == usage ? "" : usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getDosage() {
		return null == dosage ? "" : dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getFrequery() {
		return null == frequery ? "" : frequery;
	}

	public void setFrequery(String frequery) {
		this.frequery = frequery;
	}

}
