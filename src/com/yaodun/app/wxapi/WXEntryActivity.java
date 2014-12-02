package com.yaodun.app.wxapi;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.qianjiang.framework.util.EvtLog;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yaodun.app.util.ConstantSet;

/**
 * 这个页面专门接收微信分享的结果
 * 
 * @author tom
 * 
 */

public class WXEntryActivity extends Activity {
	final String TAG = getClass().getSimpleName();
	
	private IWXAPI api;
	String token;
	
	IWXAPIEventHandler wxHandler = new IWXAPIEventHandler() {
		
		// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
		@Override
		public void onResp(BaseResp resp) {
			EvtLog.d(TAG, "onResp");
			
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
			    SendAuth.Resp sendResp = (SendAuth.Resp) resp;
			    getToken(sendResp.code);
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
		
		WXAPIFactory.createWXAPI(this, ConstantSet.APP_ID_WX, true);
		api.registerApp(ConstantSet.APP_ID_WX);
		api.handleIntent(getIntent(), wxHandler);
	}
	
	void toldShareWeixinOk(boolean isOk){
		Intent intent = new Intent(ConstantSet.ACTION_WEIXIN_LOGIN);
		if(!TextUtils.isEmpty(token)){
		    intent.putExtra(ConstantSet.EXTRA_TOKEN, token);
		}
    	sendBroadcast(intent);
    	finish();
	}
	void getToken(final String code){
	    final String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
	            +ConstantSet.APP_ID_WX+"&secret=SECRET&code="
	            +code+"&grant_type=authorization_code";
	    new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... arg0) {
                try{
                    HttpGet get = new HttpGet(url);
                    DefaultHttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(get);
                    String strJSON = EntityUtils.toString(response.getEntity());
                    JSONObject jobj = new JSONObject(strJSON);
                    token = jobj.optString("access_token");
                }catch(Exception e){
                    EvtLog.e(TAG, e);
                }
                return null;
            }
	        
	    }.execute();
	}
}
