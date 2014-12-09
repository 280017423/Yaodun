package com.yaodun.app.util;

import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.util.AppUtil;

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

	public static final String LOGIN_INTERFACE = "/User/loginInterface";
	public static final String ADD_INTERFACE = "/User/addInterface";
	public static final String CHANGE_PASSWORD = "/User/changePassword";
	public static final String KNOWLEDGE_LIST = "/User/knowledgeList";
	public static final String DOCTOR_LIST = "/User/doctorList";
	public static final String KNOWLEDGE_DETAIL = "/User/knowledgeDetail";
	public static final String ATTENTION_KNOWLEDGE = "/User/attentionKnowledge";
	public static final String KNOWLEDGE_REPLY = "/User/knowledgeReply";
	public static final String FEEDBACK = "/User/feedBack";
	public static final String SEARCH_OTHER_INFO = "/DrugBasicCheck/searchOtherInfo";

	public static final String MEDICINE_SEARCH_API = "/DrugBasicCheck/searchDrug";
	public static final String MEDICINE_CHECK_API = "DrugBasicCheck/drugCheckinterface";
	public static final String MEDICINE_CHECK_RULE_API = "DrugBasicCheck/searchOtherInfo";
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
	public static final String KEY_USER_ID = "userId";
	public static final String KEY_USERNAME = "userName";
	public static final String KEY_PASSWORD = "passWord";
	public static final String KEY_OLD_PASSWORD = "oldPassword";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_TELEPHONE = "telephone";
	public static final String KEY_SEX = "sex";
	public static final String KEY_BIRTHDAY = "birthday";

	public static final String KEY_KNOWLEDGETYPE = "knowledgeType";
	public static final String KEY_PAGENUM = "pageNum";
	public static final String KEY_KNOWLEDGEID = "knowledgeId";
	public static final String KEY_DISCUSS = "discuss";
	public static final String KEY_OPERATION = "operation";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_OTHERINFO = "otherInfo";

	public static final String KEY_APP = "app";
	public static final String KEY_FROME_ID = "from_id";
	public static final String KEY_ID = "id";
	public static final String KEY_KEYWORD = "keyword";
	public static final String KEY_DRUGNAME = "drugName";
	public static final String KEY_BASEINFO = "baseInfo";
	public static final String KEY_DRUGINFO = "drugInfo";
	public static final String KEY_OTHER_INFO = "otherInfo";
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

	/**
	 * 
	 * @Description 获取api接口地址，带切换的功能
	 * @return String api接口地址
	 * @Author Administrator
	 * @Date 2014年7月31日 上午10:07:27
	 * 
	 */
	public static String getApiRootUrl() {
		return AppUtil.getMetaDataByKey(QJApplicationBase.CONTEXT, API_ROOT_URL);
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
}
