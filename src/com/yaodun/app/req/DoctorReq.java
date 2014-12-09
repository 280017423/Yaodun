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
import com.yaodun.app.model.DoctorModel;
import com.yaodun.app.model.QuestionDetailModel;
import com.yaodun.app.model.UserInfoModel;
import com.yaodun.app.util.ServerAPIConstant;

/**
 * 用药知识请求类
 * 
 * @author zou.sq
 */
public class DoctorReq {

	/**
	 * 获取药师列表
	 * 
	 * @param page
	 *            页数
	 * @return ActionResult 请求结构数据
	 */
	public static ActionResult getDoctorList(int page) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.DOCTOR_LIST);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_PAGENUM, "" + page));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					List<DoctorModel> models = jsonResult.getData(new TypeToken<List<DoctorModel>>() {
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

	/**
	 * 获取提问详细
	 * 
	 * @param id
	 *            医生id
	 * @return ActionResult 请求结构数据
	 */
	public static ActionResult getDoctorDetail(String id) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.QUESTION_DETAIL);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserInfoModel model = UserMgr.getUserInfoModel();
		String userId = "";
		if (null != model) {
			userId = model.getUserId();
		}
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_USER_ID, userId));
		// TODO
		// postParams.add(new
		// BasicNameValuePair(ServerAPIConstant.KEY_KNOWLEDGEID, id));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					result.ResultObject = jsonResult.getData(new TypeToken<List<QuestionDetailModel>>() {
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
