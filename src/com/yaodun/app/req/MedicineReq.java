package com.yaodun.app.req;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pdw.gson.reflect.TypeToken;
import com.qianjiang.framework.app.JsonResult;
import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.HttpClientUtil;
import com.yaodun.app.R;
import com.yaodun.app.authentication.ActionResult;
import com.yaodun.app.model.MedicineBean;
import com.yaodun.app.model.MedicineCheckResultBean;
import com.yaodun.app.model.MedicineCheckRuleBean;
import com.yaodun.app.model.UserInfoModel;
import com.yaodun.app.util.HttpClientConnector;
import com.yaodun.app.util.ServerAPIConstant;

/**
 * 用药查询请求类
 * 
 * @author zou.sq
 */
public class MedicineReq {
	static final String TAG = "MedicineReq";
	static final String CHAR_SET = "utf-8";

	/**
	 * 根据关键字搜药名
	 * 
	 * @param keyword
	 * @return
	 */
	public static ActionResult searchMedicineName(String keyword) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.MEDICINE_SEARCH_API);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_DRUGNAME, keyword));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_APP, ServerAPIConstant.getAppSign()));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					result.ResultObject = jsonResult.getData(new TypeToken<List<MedicineBean>>() {
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

	public static ActionResult medicineCheck(UserInfoModel user, List<MedicineBean> medicines) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.MEDICINE_CHECK_API);
		JSONObject baseinfo = new JSONObject();
		JSONArray druginfo = new JSONArray();
		try {
			baseinfo.put("male", URLEncoder.encode(user.getGender(), CHAR_SET));
			baseinfo.put("gender", URLEncoder.encode(user.getGender(), CHAR_SET));
			baseinfo.put("age", "");
			baseinfo.put("height", user.height);
			baseinfo.put("weight", user.weight);
			baseinfo.put("isLiver", user.isLiver);
			baseinfo.put("kidney", user.kindney);
			baseinfo.put("checkType", user.checkType);
			baseinfo.put("isGestate", user.isGestate);
			baseinfo.put("gestateTime", user.gestateTime);
			baseinfo.put("isSuckling", user.isSuckling);

			for (int i = 0; i < medicines.size(); ++i) {
				MedicineBean medicin = medicines.get(i);
				JSONObject drug = new JSONObject();
				drug.put("basicId", medicin.getBasicId());
				drug.put("drugname", URLEncoder.encode(medicin.getDrugname(), CHAR_SET));
				drug.put("usage", URLEncoder.encode(medicin.getUsage(), CHAR_SET));
				drug.put("dosage", URLEncoder.encode(medicin.getDosage(), CHAR_SET));
				drug.put("frequery", URLEncoder.encode(medicin.getFrequery(), CHAR_SET));
				druginfo.put(drug);
			}
		} catch (Exception e) {
			EvtLog.e(TAG, e);
		}
		// List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		// postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_BASEINFO,
		// baseinfo.toString()));
		// postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_DRUGINFO,
		// JSON.toJSONString(medicines)));
		// postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_APP,
		// ServerAPIConstant.getAppSign()));
		try {
			// JsonResult jsonResult = HttpClientUtil.post(url, null,
			// postParams);
			JSONObject param = new JSONObject();
			param.put(ServerAPIConstant.KEY_BASEINFO, baseinfo);
			param.put(ServerAPIConstant.KEY_DRUGINFO, druginfo);
			param.put(ServerAPIConstant.KEY_APP, ServerAPIConstant.getAppSign());
			String rs = HttpClientConnector.httpPost(url, param.toString());
			JsonResult jsonResult = new JsonResult(rs);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					result.ResultObject = jsonResult.getData(new TypeToken<List<MedicineCheckResultBean>>() {
					}.getType());
				} else {
					result.ResultObject = jsonResult.Data;
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
	 * 检测规则查询
	 * 
	 * @param keyword
	 * @return
	 */
	public static ActionResult getMedicineCheckRules(int queryType) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.MEDICINE_CHECK_RULE_API);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_OTHER_INFO, "" + queryType));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_APP, ServerAPIConstant.getAppSign()));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					result.ResultObject = jsonResult.getData(new TypeToken<List<MedicineCheckRuleBean>>() {
					}.getType());
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
