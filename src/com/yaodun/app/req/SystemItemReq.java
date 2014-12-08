package com.yaodun.app.req;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pdw.gson.reflect.TypeToken;
import com.qianjiang.framework.app.JsonResult;
import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.util.HttpClientUtil;
import com.yaodun.app.R;
import com.yaodun.app.authentication.ActionResult;
import com.yaodun.app.manager.UserMgr;
import com.yaodun.app.model.CommonModel;
import com.yaodun.app.model.UserInfoModel;
import com.yaodun.app.util.ServerAPIConstant;

/**
 * 用药知识请求类
 * 
 * @author zou.sq
 */
public class SystemItemReq {

	/**
	 * 反馈意见
	 * 
	 * @param content
	 *            反馈内容
	 * @return ActionResult 请求结构数据
	 */
	public static ActionResult sendFeedback(String content) {
		ActionResult result = new ActionResult();
		UserInfoModel model = UserMgr.getUserInfoModel();
		String userId = "";
		if (null != model) {
			userId = model.getUserId();
		}
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.FEEDBACK);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_DESCRIPTION, content));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_USER_ID, userId));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				result.ResultObject = jsonResult.Msg;
				result.ResultCode = jsonResult.Code;
			} else {
				result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
				result.ResultObject = QJApplicationBase.CONTEXT.getString(R.string.network_is_not_available);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
		}
		return result;
	}

	/**
	 * 反馈意见
	 * 
	 * @param otherInfo
	 *            参数类型
	 * @return ActionResult 请求结构数据
	 */
	public static ActionResult getDetail(String otherInfo) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.SEARCH_OTHER_INFO);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_OTHERINFO, otherInfo));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					List<CommonModel> models = jsonResult.getData(new TypeToken<List<CommonModel>>() {
					}.getType());
					result.ResultObject = models;
				} else {
					result.ResultObject = jsonResult.Msg;
				}
				result.ResultCode = jsonResult.Code;
			} else {
				result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
				result.ResultObject = QJApplicationBase.CONTEXT.getString(R.string.network_is_not_available);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
		}
		return result;
	}

}
