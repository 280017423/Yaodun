package com.yaodun.app.util;

import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.util.AppUtil;
import com.qianjiang.framework.util.StringUtil;

/**
 * 定义与服务器端的接口交互需要用到的常量
 * 
 * @author xu.xb
 * 
 */
public class ServerAPIConstant {
	// API地址
	public static final String API_ROOT_URL = "api_root_url";
	public static final String KEY_APP_SIGN = "appSign";
	public static final String LOGIN_API = "/child/login";
	public static final String SEARCH_MEDICINE_API = "/DrugBasicCheck/searchDrug";
	public static final String GET_TEACHERS_API = "/sms/gettearchers";
	public static final String GET_CLASS_NEWS_API = "/article/list4cls";
	public static final String GET_SCHOOL_NEWS_API = "/article/list4sch";
	public static final String GET_SCHOOL_INFO_API = "/article/list4info";
	public static final String GET_MAX_ID = "/status/getMax";
	public static final String GET_DETAIL_API = "/article/detail";
	public static final String GET_SHIPU = "/shipu/getnow";
	public static final String SIGN_URL = "/kaoqin/list";
	public static final String TO_TEACHER_API = "/sms/toteacher";
	public static final String GET_TEACHER_MESSAGE_API = "/sms/fromschool";
	public static final String CHECK_VERSION = "/status/checkVersion";
	public static final String FEED_BACK = "/status/feedBack";
	public static final String CHECK_NETWORK = "/status/checkNetwork";
	public static final String GET_ADVER = "/status/getAdver";
	public static final String GET_RECOM_VIDEO_LIST = "/video/getRecomVideoList";
	public static final String GET_VIDEO_LIST = "/video/getVideoList";
	public static final String ADD_VIDEO_PLAY_COUNT = "/video/addVideoPlayCount";
	public static final String GET_EXCHANGE_LIST = "/points/exchangelist";
	public static final String EXCHANGE = "/points/exchange ";
	// API KEY
	public static final String KEY_USER_NAME = "username";
	public static final String KEY_USER_PWD = "userpwd";
	public static final String KEY_CLASS_NAME = "className";
	public static final String KEY_CHILREN_NAME = "chilren_name";
	public static final String KEY_MOBILE = "mobile";
	public static final String KEY_APP = "app";
	public static final String KEY_FROME_ID = "from_id";
	public static final String KEY_ID = "id";
	public static final String KEY_KEYWORD = "keyword";
	public static final String KEY_PAGE = "page";
	public static final String KEY_VIDEOCATEGORY = "videoCategory";
	public static final String KEY_MAX_ID = "max_id";
	public static final String KEY_MIN_ID = "min_id";
	public static final String KEY_TEACHER_ID = "teacher_id";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_CHILDREN_INFO = "childrenInfo";
	public static final String KEY_CLIENT_MAX = "clientMax";
	public static final String KEY_TEACHER_LIST = "teacherList";
	public static final String KEY_PUSH_ALIAS = "pushAlias";
	public static final String KEY_PUSH_TAG = "pushTag";
	public static final String KEY_IMG_SERVER = "imgServer";
	public static final String KEY_SERVER_TIME = "ServerTime";
	public static final String KEY_TIME = "time";
	public static final String KEY_MESSAGE_ID = "KEY_MESSAGE_ID";
	public static final String KEY_VERSION = "version";
	public static final String KEY_CLIENT_TYPE = "clientType";
	public static final String KEY_PROJECT_SIGN = "projectSign";
	public static final String PROJECT_SIGN = "1";
	public static final String KEY_VIDEO_ID = "videoId";
	public static final String KEY_VIDEOLIST = "videoList";
	public static final String KEY_RECOMVIDEOLIST = "recomVideoList";
	public static final String KEY_CATEGORYLIST = "categoryList";

	public static final String KEY_POINT = "point";
	public static final String KEY_EXCHANGE_LIST = "exchangeList";
	public static final String KEY_EXCHANGEID = "exchangeId";
	public static final String KEY_POINTRULE = "pointRule";
	public static final String KEY_ACTIVERULE = "activeRule";

	public static final String[] API_ROOT_URL_ARRAY = new String[] {
			AppUtil.getMetaDataByKey(QJApplicationBase.CONTEXT, API_ROOT_URL),
			"http://ikisappv3.api.weare-team.com/api",
			"http://54.251.110.27:8091/api" };

	public static final String[] VERSION_URL_ARRAY = new String[] {
			"http://version.api.ikisos.com/api/version/checkVersion",
			"http://version.api.weare-team.com/api/version/checkVersion",
			"http://54.251.110.27:8093/api/version/checkVersion" };

	/**
	 * 
	 * @Description 获取api接口地址，带切换的功能
	 * @return String api接口地址
	 * @Author Administrator
	 * @Date 2014年7月31日 上午10:07:27
	 * 
	 */
	public static String getApiRootUrl() {
		int index = SharedPreferenceUtil.getIntegerValueByKey(QJApplicationBase.CONTEXT, ConstantSet.KEY_API_URL_FILE,
				ConstantSet.KEY_API_URL_INDEX);
		if (SharedPreferenceUtil.INVALID_CODE == index) {
			index = 0;
			String localUrl = SharedPreferenceUtil.getStringValueByKey(QJApplicationBase.CONTEXT,
					ConstantSet.FILE_JYT_CONFIG, ConstantSet.SYSTEM_CONFIG_API_ROOT_URL);
			if (!StringUtil.isNullOrEmpty(localUrl)) {
				return localUrl;
			}
		}
		return API_ROOT_URL_ARRAY[index % API_ROOT_URL_ARRAY.length];
	}

	/**
	 * 获取appSign
	 * 
	 * @return
	 * @return String appSign
	 */
	public static String getAppSign() {
		return AppUtil.getMetaDataByKey(QJApplicationBase.CONTEXT, KEY_APP_SIGN);
	}

	/**
	 * 获取接口地址
	 * 
	 * @return interfaceName 接口名字
	 */
	public static String getUrl(String interfaceName) {
		return getApiRootUrl() + interfaceName;
	}

	/**
	 * 
	 * @Description 获取版本更新接口地址
	 * @return 接口地址
	 * @Author Administrator
	 * @Date 2014年6月27日 上午11:57:55
	 * 
	 */
	public static String getVersionCheckUrl() {
		int index = SharedPreferenceUtil.getIntegerValueByKey(QJApplicationBase.CONTEXT, ConstantSet.KEY_API_URL_FILE,
				ConstantSet.KEY_API_URL_INDEX);
		if (SharedPreferenceUtil.INVALID_CODE == index) {
			index = 0;
		}
		return VERSION_URL_ARRAY[index % VERSION_URL_ARRAY.length];
	}
}
