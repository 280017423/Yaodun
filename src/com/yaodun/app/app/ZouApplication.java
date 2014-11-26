package com.yaodun.app.app;

import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.imageloader.cache.disc.naming.Md5FileNameGenerator;
import com.qianjiang.framework.imageloader.core.ImageLoader;
import com.qianjiang.framework.imageloader.core.ImageLoaderConfiguration;
import com.yaodun.app.util.ConstantSet;
import com.yaodun.app.util.DBUtil;

/**
 * 全局应用程序
 * 
 * @author cui.yp
 * 
 */
public class ZouApplication extends QJApplicationBase {
	// 测试包Key
	// public static final String strKey = "MLoOwYzufBBHwQdqEO2ZnMpl";
	// 正式包Key
	public static final String strKey = "wbZ6TstPukeCZ3GlOGX0doUD";
	public static final String TAG = "QianJiangApplication";
	public static String LOCATE_CITYNAME = "";
	public static final int APP_INIT_STATE_SUCCESS = 1;
	public static final int THREAD_POOL_SIZE = 3;
	public static final int MEMORY_CACHE_SIZE = 1500000;
	public static int APP_INIT_STATE;

	@Override
	// 建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
		super.onTerminate();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		CONTEXT = this;
		initImageLoader();
		new Thread(new Runnable() {
			@Override
			public void run() {
				DBUtil.getDataManager().firstOpen();

			}
		}).start();
	}

	@Override
	protected void setAppSign() {
		APP_SIGN = ConstantSet.APP_SIGN;
	}

	@Override
	protected void setClientType() {
	}

	/**
	 * This configuration tuning is custom. You can tune every option, you may
	 * tune some of them, or you can create default configuration by
	 * ImageLoaderConfiguration.createDefault(this); method.
	 * 
	 * @Name initImageLoader
	 * @Description 初始化图片加载器
	 * @return void
	 * @Author Administrator
	 * @Date 2014-3-21 上午11:42:06
	 * 
	 */
	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.threadPoolSize(THREAD_POOL_SIZE).threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(MEMORY_CACHE_SIZE)
				// 1.5 Mb
				.denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
				// .enableLogging() // Not necessary in common
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

}
