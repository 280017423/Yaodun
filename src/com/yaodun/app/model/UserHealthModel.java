package com.yaodun.app.model;

import com.qianjiang.framework.orm.BaseModel;

/**
 * 用户的健康状况，用药检测接口需要的参数
 * @author tom
 *
 */
public class UserHealthModel extends BaseModel {
	public String userId;
	public String male;//0代表男。1代表女
	public int age;//0代表不详，1代表小孩，2代表中年，3代表老年人。
	public int height;
	public int weight;
	public int isLiver;//肝功能 0，正常，1轻中度 ，2重度
	public int kindney;//0正常，1肾小球滤过率每分钟大于50，2肾小球滤过率每分钟10~50，3肾小球滤过率每分钟小于10
}
