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
import com.yaodun.app.model.MyQuestionDetailModel;
import com.yaodun.app.model.MyQuestionModel;
import com.yaodun.app.model.UserInfoModel;
import com.yaodun.app.util.ServerAPIConstant;

/**
 * 药师咨询请求类
 * 
 * @author zou.sq
 * @version 1.1.0 2013-04-02 <br>
 */
public class ConsultReq {

	/**
	 * 获取我的咨询详细
	 * 
	 * @param id
	 *            问题id
	 * @return ActionResult 请求结构数据
	 */
	public static ActionResult getQuestionDetail(String id) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.QUESTION_DETAIL);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserInfoModel model = UserMgr.getUserInfoModel();
		String userId = "";
		if (null != model) {
			userId = model.getUserId();
		}
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_USER_ID, userId));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_QUESITONID, id));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					result.ResultObject = jsonResult.getData(new TypeToken<MyQuestionDetailModel>() {
					}.getType());
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

	/**
	 * 回复保存
	 * 
	 * @param id
	 *            问题id
	 * @param content
	 *            评论内容
	 * @return ActionResult 请求结构数据
	 */
	public static ActionResult replyQuestion(String id, String content) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.QUESTION_REPLY);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserInfoModel model = UserMgr.getUserInfoModel();
		String userId = "";
		if (null != model) {
			userId = model.getUserId();
		}
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_QUESTIONID, id));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_REPLY, content));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_USER_ID, userId));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					result.ResultObject = jsonResult.getData(new TypeToken<MyQuestionModel>() {
					}.getType());
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
