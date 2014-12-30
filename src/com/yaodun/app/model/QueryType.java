package com.yaodun.app.model;

/**
 * 查询类型
 * 
 * @author tom
 * 
 */
public class QueryType {
	public static final int medicine_ertong = 10;// 儿童用药查询
	public static final int medicine_yunfu = 11;// 孕妇用药查询
	public static final int medicine_laoren = 12;// 老人用药查询
	public static final int medicine_chongfu = 5;// 重复用药查询
	public static final int medicine_jinji = 0;// 药品禁忌用药查询
	public static final int medicine_kangshengsu = 13;// 抗生素用药查询
	public static final int medicine_dazhong = 14;// 大众用药
	public static final int gongsi_jianjie = 100;// 公司简介
	public static final int guanyu = 101;// 关于我们
	public static final int shengming = 102;// 声明

	public static String getQueryTypeString(int queryType) {
		switch (queryType) {
			case medicine_ertong:
				return "儿童用药查询";
			case medicine_yunfu:
				return "孕妇用药查询";
			case medicine_laoren:
				return "老人用药查询";
			case medicine_chongfu:
				return "重复用药查询";
			case medicine_jinji:
				return "药品禁忌用药查询";
			case medicine_kangshengsu:
				return "抗生素用药查询";
			case medicine_dazhong:
				return "大众用药";
			default:
				return "";
		}
	}
}
