package com.pregnancy.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.qianjiang.framework.util.StringUtil;

/**
 * 日期工具类
 * 
 * @author zou.sq
 */
public class DateUtil {

	/**
	 * 获取处理后的时间
	 * 
	 * @param currentTime
	 *            当前时间
	 * @param compareTime
	 *            需要比较的时间
	 * @return String 处理后的时间
	 */
	public static String getTimeStr(String currentTime, String compareTime) {
		String result = "";
		Calendar currentCalendar = Calendar.getInstance();
		Calendar compareCalendar = Calendar.getInstance();
		if (!StringUtil.isNullOrEmpty(currentTime) && !StringUtil.isNullOrEmpty(compareTime)) {
			SimpleDateFormat dayDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			try {
				Date currentDate = dayDateFormat.parse(currentTime);
				Date compareDate = dayDateFormat.parse(compareTime);
				currentCalendar.setTime(currentDate);
				compareCalendar.setTime(compareDate);
				currentCalendar.add(Calendar.DATE, -1);
				result = (compareCalendar.get(Calendar.MONTH) + 1) + "-" + compareCalendar.get(Calendar.DATE);
				// 是同一天
				if (currentDate.compareTo(compareDate) == 0) {
					result = "今天";
				} else {

					if (currentCalendar.compareTo(compareCalendar) == 0) {
						// 如果和昨天日期一样，说明是昨天
						result = "昨天";
					} else if (currentCalendar.compareTo(compareCalendar) > 0) {
						// 比昨天还小，说明是之前的日子，先
						result = (compareCalendar.get(Calendar.MONTH) + 1) + "-" + compareCalendar.get(Calendar.DATE);
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}
