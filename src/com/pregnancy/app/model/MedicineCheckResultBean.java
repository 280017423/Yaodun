package com.pregnancy.app.model;

/**
 * 检测规则
 * 
 * @author tom
 * 
 */
public class MedicineCheckResultBean {
	private String grade;
	private String result;

	public String getGrade() {
		return null == grade ? "" : grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getResult() {
		return null == result ? "" : result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
