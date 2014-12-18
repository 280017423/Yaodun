package com.yaodun.app.openapi;

import java.io.FileInputStream;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.WeixinUtil;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yaodun.app.R;
import com.yaodun.app.util.ConstantSet;

public class WeixinHelper{
	private final String TAG = getClass().getSimpleName();
	
	Activity act;
	String title;
	String description;
	String webpageUrl;
	String bmpPath;
	String webBmpUrl;
	
	public WeixinHelper(Activity act) {
		this.act = act;
		api = getWeixinApi();
	}
	public void setTitle(String title){
		this.title = title;
	}
	public void setDescription(String description){
		this.description = description; 
	}
	public void setWebpageUrl(String webpageUrl){
		this.webpageUrl = webpageUrl;
	}
	public void setBmpPath(String bmpPath){
		this.bmpPath = bmpPath;
	}
//	public void setBmpUrl(String bmpUrl){
//		this.webBmpUrl = bmpUrl;
//	}
	
	// IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
    private static final int THUMB_SIZE = 150;
    public void shareToWeixin(boolean shareToPengyouquan){
    	EvtLog.d(TAG, "shareToWeixin, shareToPengyouquan: "+shareToPengyouquan);
    	EvtLog.d(TAG, "shareToWeixin, title: "+title+", description: "+description+", webpageUrl: "+webpageUrl+", bmpPath: "+bmpPath+", webBmpUrl: "+webBmpUrl);
    	
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = getWeixinApi();
		
		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		if(TextUtils.isEmpty(title)){
			msg.title = "药盾";//空title似乎分享不出去
		}else{
			msg.title = title;// 发送文本类型的消息时，title字段不起作用
		}
		if(TextUtils.isEmpty(description)){
			msg.description = "药盾";
		}else{
			msg.description = description;
		}
		
		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		
		//如果分享有链接
		if(!TextUtils.isEmpty(webpageUrl)){
			EvtLog.d(TAG, "share to weixin, url: "+webpageUrl);
			
			WXWebpageObject webpage = new WXWebpageObject();
			webpage.webpageUrl = webpageUrl;
//			WXMediaMessage msg = new WXMediaMessage(webpage);
			msg.mediaObject = webpage;
			
			Bitmap thumbBmp = null;
			if(!TextUtils.isEmpty(bmpPath)){
				try{
					EvtLog.d(TAG, "shareToWeixin, decode bmp from bmpPath");
					Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(bmpPath));
					thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
					bmp.recycle();
				}catch (Exception e) {
					EvtLog.e(TAG, e);
				}	
			}
			if(thumbBmp == null && !TextUtils.isEmpty(webBmpUrl)){
				try{
					EvtLog.d(TAG, "shareToWeixin, decode bmp from webBmpUrl");
					Bitmap bmp = BitmapFactory.decodeStream(new URL(webBmpUrl).openStream());
					thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
					bmp.recycle();
				}catch (Exception e) {
					EvtLog.e(TAG, e);
				}				
			}
			if(thumbBmp == null){
				EvtLog.d(TAG, "shareToWeixin, decode bmp from ic_launcher");
				Bitmap bmp = BitmapFactory.decodeResource(act.getResources(), R.drawable.ic_launcher);
				thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
				bmp.recycle();
			}
			
			msg.thumbData = WeixinUtil.bmpToByteArray(thumbBmp, true);			
			req.transaction = buildTransaction("webpage");
		}
		//没有链接，有网络图片
		else if(!TextUtils.isEmpty(webBmpUrl)){
			EvtLog.d(TAG, "share to weixin, imgurl: "+webBmpUrl);
			try{
				WXImageObject imgObj = new WXImageObject();
				imgObj.imageUrl = webBmpUrl;
				
				msg.mediaObject = imgObj;

				Bitmap bmp = BitmapFactory.decodeStream(new URL(webBmpUrl).openStream());
				Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
				bmp.recycle();
				msg.thumbData = WeixinUtil.bmpToByteArray(thumbBmp, true);
				
				req.transaction = buildTransaction("img");
				req.message = msg;
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		//没有链接，有本地图片
		else if(!TextUtils.isEmpty(bmpPath)){
			EvtLog.d(TAG, "share to weixin, imgpath: "+bmpPath);
			
			try{
				WXImageObject imgObj = new WXImageObject();
				imgObj.setImagePath(bmpPath);
				msg.mediaObject = imgObj;
				
				Bitmap bmp = BitmapFactory.decodeFile(bmpPath);
				Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
				bmp.recycle();
				msg.thumbData = WeixinUtil.bmpToByteArray(thumbBmp, true);
				
//				Bitmap bmp = BitmapFactory.decodeStream(new URL(mSharePhotoUri).openStream());
//				Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//				bmp.recycle();
//				msg.thumbData = WeixinUtil.bmpToByteArray(thumbBmp, true);
				
				req.transaction = buildTransaction("img");
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		//分享文本
		else if(!TextUtils.isEmpty(title)){
			EvtLog.d(TAG, "share to weixin, text: "+title);
			
			// 初始化一个WXTextObject对象
			WXTextObject textObj = new WXTextObject();
			textObj.text = title;
			msg.mediaObject = textObj;
			
			req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
		}
		
		req.message = msg;
		boolean isSupport = isWeixinPengyouquanSupport();
//		isSupport = false;
		req.scene = (isSupport && shareToPengyouquan) ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		EvtLog.d(TAG, "share to weixin, isSupport: "+isSupport+", shareToPengyouquan: "+shareToPengyouquan);
		
		// 调用api接口发送数据到微信
		boolean isOk = api.sendReq(req);
		EvtLog.d(TAG, "share to weixin complete, "+isOk);
	}
    public IWXAPI getWeixinApi(){
    	if(api == null){
			api = WXAPIFactory.createWXAPI(act, ConstantSet.APP_ID_WX, false);
		}
    	api.registerApp(ConstantSet.APP_ID_WX);
    	return api;
    }
    private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	public boolean isWeixinPengyouquanSupport(){
		api = getWeixinApi();
		
		int wxSdkVersion = api.getWXAppSupportAPI();
		if (wxSdkVersion < TIMELINE_SUPPORTED_VERSION) {
			return false;
		}
		
		return true;
	}
}
