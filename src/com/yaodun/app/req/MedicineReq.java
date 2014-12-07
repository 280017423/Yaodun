package com.yaodun.app.req;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.pdw.gson.reflect.TypeToken;
import com.qianjiang.framework.app.JsonResult;
import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.util.HttpClientUtil;
import com.qianjiang.framework.util.StringUtil;
import com.yaodun.app.R;
import com.yaodun.app.authentication.ActionResult;
import com.yaodun.app.manager.UserMgr;
import com.yaodun.app.model.MedicineBean;
import com.yaodun.app.model.UserHealthModel;
import com.yaodun.app.model.UserInfoModel;
import com.yaodun.app.util.ServerAPIConstant;

/**
 * 用药查询请求类
 * 
 * @author zou.sq
 */
public class MedicineReq {

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
	
	public static ActionResult medicineCheck(UserHealthModel health, List<MedicineBean> medicines) {
        ActionResult result = new ActionResult();
        String url = ServerAPIConstant.getUrl(ServerAPIConstant.MEDICINE_CHECK_API);
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_BASEINFO, JSON.toJSONString(health)));
        postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_DRUGINFO, JSON.toJSONString(medicines)));
        postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_APP, ServerAPIConstant.getAppSign()));
        try {
            JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
            if (jsonResult != null) {
                if (jsonResult.isOK()) {
                    result.ResultObject = jsonResult.getData(new TypeToken<List<MedicineBean>>() {
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
	
	/**
	 * 检测规则查询
	 * @param keyword
	 * @return
	 */
	public static ActionResult getMedicineCheckRules(int queryType) {
        ActionResult result = new ActionResult();
        String url = ServerAPIConstant.getUrl(ServerAPIConstant.MEDICINE_CHECK_RULE_API);
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_OTHER_INFO, ""+queryType));
        postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_APP, ServerAPIConstant.getAppSign()));
        try {
            JsonResult jsonResult = HttpClientUtil.post(url, null, postParams);
            if (jsonResult != null) {
                if (jsonResult.isOK()) {
                    result.ResultObject = jsonResult.getData(new TypeToken<List<MedicineBean>>() {
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

