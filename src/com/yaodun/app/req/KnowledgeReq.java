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
import com.yaodun.app.model.KnowledgeDetailModel;
import com.yaodun.app.model.KnowledgeModel;
import com.yaodun.app.model.UserInfoModel;
import com.yaodun.app.util.ServerAPIConstant;

/**
 * 用药知识请求类
 * 
 * @author zou.sq
 */
public class KnowledgeReq {

	/**
	 * 获取文章列表
	 * 
	 * @param type
	 *            文章类型
	 * @param page
	 *            页数
	 * @return ActionResult 请求结构数据
	 */
	public static ActionResult getKnowledgeList(int type, int page) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.KNOWLEDGE_LIST);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_KNOWLEDGETYPE, "" + type));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_PAGENUM, "" + page));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					List<KnowledgeModel> models = jsonResult.getData(new TypeToken<List<KnowledgeModel>>() {
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
	 * 获取文章详细
	 * 
	 * @param id
	 *            文章id
	 * @return ActionResult 请求结构数据
	 */
	public static ActionResult getKnowledgeDetail(String id) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.KNOWLEDGE_DETAIL);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserInfoModel model = UserMgr.getUserInfoModel();
		String userId = "";
		if (null != model) {
			userId = model.getUserId();
		}
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_USER_ID, userId));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_KNOWLEDGEID, id));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					result.ResultObject = jsonResult.getData(new TypeToken<KnowledgeDetailModel>() {
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
	 * 发表文章评论
	 * 
	 * @param id
	 *            文章id
	 * @param content
	 *            评论内容
	 * @return ActionResult 请求结构数据
	 */
	public static ActionResult sendknowledgeReply(String id, String content) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.KNOWLEDGE_REPLY);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserInfoModel model = UserMgr.getUserInfoModel();
		String userId = "";
		if (null != model) {
			userId = model.getUserId();
		}
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_KNOWLEDGEID, id));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_DISCUSS, content));
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
	 * 收藏文章
	 * 
	 * @param id
	 *            文章id
	 * @param operation
	 *            operation为空是代表关注，为1时代表取消关注
	 * @return ActionResult 请求结构数据
	 */
	public static ActionResult attentionKnowledge(String id, String operation) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.ATTENTION_KNOWLEDGE);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserInfoModel model = UserMgr.getUserInfoModel();
		String userId = "";
		if (null != model) {
			userId = model.getUserId();
		}
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_KNOWLEDGEID, id));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_OPERATION, operation));
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
}
