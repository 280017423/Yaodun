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
import com.yaodun.app.model.ConsultListModel;
import com.yaodun.app.model.DoctorModel;
import com.yaodun.app.model.QuestionModel;
import com.yaodun.app.model.UserInfoModel;
import com.yaodun.app.util.ServerAPIConstant;

/**
 * 用药知识请求类
 * 
 * @author zou.sq
 */
public class DoctorReq {

	/**
	 * 获取我的咨询列表
	 * 
	 * @param page
	 *            页数
	 * @return ActionResult 请求结构数据
	 */
	public static ActionResult getConsultList(int page) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.OWN_CONSULT_LIST);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserInfoModel model = UserMgr.getUserInfoModel();
		String userId = "";
		if (null != model) {
			userId = model.getUserId();
		}
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_USER_ID, userId));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_PAGENUM, "" + page));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					List<ConsultListModel> models = jsonResult.getData(new TypeToken<List<ConsultListModel>>() {
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
		UserInfoModel model = UserMgr.getUserInfoModel();
		String userId = "";
		if (null != model) {
			userId = model.getUserId();
		}
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_USER_ID, userId));
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
	 * 发表提问
	 * 
	 * @param id
	 *            医生id
	 * @param content
	 *            评论内容
	 * @return ActionResult 请求结构数据
	 */
	public static ActionResult sendQuestion(String id, String content) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.QUESTION_SAVE);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserInfoModel model = UserMgr.getUserInfoModel();
		String userId = "";
		if (null != model) {
			userId = model.getUserId();
		}
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_DOCTORID, id));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_DESCRIPTION, content));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_USER_ID, userId));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					result.ResultObject = jsonResult.getData(new TypeToken<QuestionModel>() {
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

	public static ActionResult attentionDoctor(String doctorId, String operation) {
        ActionResult result = new ActionResult();
        String url = ServerAPIConstant.getUrl(ServerAPIConstant.ATTENTION_DOCTOR);
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        UserInfoModel model = UserMgr.getUserInfoModel();
        String userId = "";
        if (null != model) {
            userId = model.getUserId();
        }
        postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_USER_ID, userId));
        postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_DOCTORID, doctorId));
        postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_OPERATION, operation));
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

}
