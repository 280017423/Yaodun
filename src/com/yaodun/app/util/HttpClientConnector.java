/**********************************************************
 * Copyright © 2013-1014 深圳市美传网络科技有限公司版权所有
 * 创 建 人：yangbo
 * 创 建 日 期：2014-7-18 下午4:36:13
 * 版 本 号：
 * 修 改 人：
 * 描 述：
 * <p>
 *	
 * </p>
 **********************************************************/
package com.yaodun.app.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.qianjiang.framework.util.EvtLog;

/**
 * 当请求参数是嵌套的json格式时，原有的框架用不了，内层的json被转化之后多出很多引号，服务器识别不了
 * @author tom
 * @date 2014-12-14
 * @version
 * @since
 */
public class HttpClientConnector {
	private final static int RETRY_TIME = 3;
	private final static String TAG = "HttpClientConnector";

	public static String httpPost(String serverUrl, String content) throws ClientProtocolException, IOException {
		final HttpClient httpClient = getHttpClient();
		final HttpPost httpPost = new HttpPost(serverUrl);
		StringEntity entity = new StringEntity(content, HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		EvtLog.d(TAG, "start, url: "+serverUrl);
		EvtLog.d(TAG, "params: "+content);
		
		HttpResponse response = httpClient.execute(httpPost);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new IOException("Status code error:" + statusCode);
		}
		byte[] parseResponse = parseResponse(response);
		String result = new String(parseResponse, HTTP.UTF_8);
		EvtLog.d(TAG, "url: "+serverUrl+ "\nresponse: "+result);
		return result;
	}

	/**
	 * @param url
	 * @return
	 */
	private static String getHttpRequest(final String url) {
		String result = "";
		int time = 0;

		HttpClient client = null;
		do {
			try {
				client = getHttpClient();
				HttpUriRequest request = new HttpGet(url);
				HttpResponse response = client.execute(request);
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					throw new IOException("Status code error:" + statusCode);
				}

				byte[] parseResponse = parseResponse(response);
				result = new String(parseResponse, HTTP.UTF_8);
				client.getConnectionManager().shutdown();
				break;
			} catch (ClientProtocolException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						continue;
					}
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						continue;
					}
				}
				// 发生网络异常
				e.printStackTrace();
			} finally {
				client = null;
			}
		} while (time < RETRY_TIME);
		return result;
	}

	private static DefaultHttpClient getHttpClient() {
		final HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "UTF-8");

		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
		HttpConnectionParams.setSoTimeout(params, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);

		HttpProtocolParams.setUserAgent(params, "Xima Software HTTP Client 1.0");

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

		ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);

		return new DefaultHttpClient(manager, params);
	}

	static byte[] parseResponse(final HttpResponse response) {
		byte[] result = new byte[] {};
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			try {
				result = EntityUtils.toByteArray(entity);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					entity.consumeContent();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	private static String MD5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}
}
