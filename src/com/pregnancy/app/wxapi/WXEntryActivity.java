package com.pregnancy.app.wxapi;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.pregnancy.app.openapi.WeixinHelper;
import com.pregnancy.app.util.ConstantSet;
import com.qianjiang.framework.util.EvtLog;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * 这个页面专门接收微信分享的结果
 * 
 * @author tom
 * 
 */

public class WXEntryActivity extends Activity {
	final String TAG = getClass().getSimpleName();
	
	private WeixinHelper weixinHelper;
	String authResponse;//内容格式见https://open.weixin.qq.com/cgi-bin/frame?t=resource/res_main_tmpl&verify=1&lang=zh_CN
	
	IWXAPIEventHandler wxHandler = new IWXAPIEventHandler() {
		
		// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
		@Override
		public void onResp(BaseResp resp) {
			EvtLog.d(TAG, "onResp");
			try{
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
			    if(resp instanceof SendAuth.Resp){
			        SendAuth.Resp sendResp = (SendAuth.Resp) resp;
			        getToken(sendResp.code);
			    }else{
			        finish();
			    }
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				toldShareWeixinOk(false);
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				toldShareWeixinOk(false);
				break;
			default:
				toldShareWeixinOk(false);
				break;
			}
			}catch(Exception e){
			    EvtLog.e(TAG, e);
			}
		}
		
		// 微信发送请求到第三方应用时，会回调到该方法
		@Override
		public void onReq(BaseReq req) {
			EvtLog.d(TAG, "onReq");
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		weixinHelper = new WeixinHelper(WXEntryActivity.this);
		weixinHelper.getWeixinApi().handleIntent(getIntent(), wxHandler);
	}
	
	@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        
        setIntent(intent);
        weixinHelper.getWeixinApi().handleIntent(intent, wxHandler);
    }
	
	void toldShareWeixinOk(boolean isOk){
		Intent intent = new Intent(ConstantSet.ACTION_WEIXIN_LOGIN);
		if(!TextUtils.isEmpty(authResponse)){
		    intent.putExtra(ConstantSet.EXTRA_OPENAPI_AUTH_RESPONSE, authResponse);
		}
    	sendBroadcast(intent);
    	finish();
	}
	void getToken(final String code){
	    final String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
	            +ConstantSet.APP_ID_WX+"&secret="+ConstantSet.APP_SECRET_WX+"&code="
	            +code+"&grant_type=authorization_code";
	    new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... arg0) {
                try{
                    HttpGet get = new HttpGet(url);
                    DefaultHttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(get);
                    String strJSON = EntityUtils.toString(response.getEntity());
//                    JSONObject jobj = new JSONObject(strJSON);
//                    token = jobj.optString("access_token");
                    authResponse = strJSON;
                }catch(Exception e){
                    EvtLog.e(TAG, e);
                }
                return null;
            }
	        
	    }.execute();
	}
}
