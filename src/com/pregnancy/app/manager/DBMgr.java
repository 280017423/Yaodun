package com.pregnancy.app.manager;

import java.util.ArrayList;
import java.util.List;

import com.pregnancy.app.util.ConstantSet;
import com.pregnancy.app.util.DBUtil;
import com.qianjiang.framework.orm.BaseModel;
import com.qianjiang.framework.orm.DataAccessException;
import com.qianjiang.framework.orm.DataManager;
import com.qianjiang.framework.util.StringUtil;

/**
 * 数据库管理类
 * 
 * @author zou.sq
 */
public class DBMgr {
	private DBMgr() {
	}

	/**
	 * 
	 * @Name saveModelAsync
	 * @Description 保存model
	 * @param model
	 *            需保存的对象
	 * @param <T>
	 *            泛型
	 * 
	 */
	public static <T extends BaseModel> void saveModel(final T model) {
		if (null == model) {
			return;
		}
		DataManager dataManager = DBUtil.getDataManager();
		try {
			dataManager.save(model);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Name getLocalNewsModel
	 * @Description 获取BaseModel本地记录，倒序
	 * @param type
	 *            model类型
	 * @param <T>
	 *            BaseModel类型子类
	 * @return List<T> BaseModel类型集合
	 * 
	 */
	public static <T extends BaseModel> List<T> getBaseModel(Class<T> type) {
		List<T> results = null;
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		try {
			results = dataManager.getList(type, null, null, "_id desc", null);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		dataManager.close();
		return results;
	}

	/**
	 * 下拉刷新
	 * 
	 * @param type
	 *            指定的model
	 * @param <T>
	 *            BaseModel的子类
	 * @param key
	 *            主键
	 * @return List<T> 指定的model的缓存数据
	 * 
	 */
	public static <T extends BaseModel> List<T> getHistoryData(Class<T> type, String key) {
		List<T> results = new ArrayList<T>();
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		try {
			results = dataManager.getList(type, true, null, null, null, null, key + " desc",
					ConstantSet.INFO_NUM_IN_ONE_PAGE + "");
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		dataManager.close();
		return results;
	}

	/**
	 * 加载更多数据
	 * 
	 * @param type
	 *            指定的model
	 * @param <T>
	 *            JYTBaseModel的子类
	 * @param min_id
	 *            最小ID
	 * @param key
	 *            主键
	 * @return List<T> 指定的model的缓存数据
	 * 
	 */
	public static <T extends BaseModel> List<T> getHistoryData(Class<T> type, int min_id, String key) {
		List<T> results = new ArrayList<T>();
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		try {
			results = dataManager.getList(type, true, key + " < " + min_id, null, null, null, key + " desc",
					ConstantSet.INFO_NUM_IN_ONE_PAGE + "");
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		dataManager.close();
		return results;
	}

	/**
	 * 
	 * @Name deleteTeachersFromDb
	 * @Description 删除老师信息
	 * @param type
	 *            指定的model
	 * @param <T>
	 *            BaseModel的子类
	 * @return boolean true 删除成功，false 删除失败
	 * 
	 */
	public static <T extends BaseModel> boolean deleteTableFromDb(Class<T> type) {
		boolean isSucces = false;
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		isSucces = dataManager.delete(type, null, null);
		dataManager.close();
		return isSucces;
	}

	/**
	 * 删除数据库的表
	 * 
	 * @param tableName
	 *            表名字
	 * @param <T>
	 *            BaseModel的子类
	 * @return boolean 是否删除成功
	 */
	public static <T extends BaseModel> boolean deleteTableFromDb(String tableName) {
		boolean isSucces = false;
		if (StringUtil.isNullOrEmpty(tableName)) {
			return isSucces;
		}
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		isSucces = dataManager.delete(tableName, null, null);
		dataManager.close();
		return isSucces;
	}

}
