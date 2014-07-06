package org.lance.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 处理日期时间的工具类
 * 
 * @author lance
 * 
 */
public class DateService {
	// 默认日期格式
	public static final String DATEPATTERN = "yyyy-MM-dd";
	// 默认时间格式
	public static final String TIMEPATTERN = "HH:mm";

	/**
	 * 处理时间为long型
	 * 
	 * @param format
	 * @return
	 */
	public static long getDateToLong(String format) {
		long result = -1;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date date = sdf.parse(format);
			result = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Return 缺省的日期格式 (yyyy-MM-dd)
	 * 
	 * @return 在页面中显示的日期格式
	 */
	public static String getDATEPATTERN() {
		return DATEPATTERN;
	}

	/**
	 * 按照日期格式，将字符串解析为日期对象
	 * 
	 * @param aMask
	 *            输入字符串的格式
	 * @param strDate
	 *            一个按aMask格式排列的日期的字符串描述
	 * @return Date 对象
	 * @see java.text.SimpleDateFormat
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String pattern, String strDate)
			throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(pattern);
		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return (date);
	}

	/**
	 * 返回指定格式的日期用字符串表示
	 * 
	 * @param pattern
	 * @param aDate
	 * @return
	 */
	public static final String getDateTime(String pattern, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";
		if (aDate == null) {
		} else {
			df = new SimpleDateFormat(pattern);
			returnValue = df.format(aDate);
		}
		return (returnValue);
	}

	/**
	 * 根据日期格式，返回日期按DATEPATTERN格式转换后的字符串
	 * 
	 * @param aDate
	 * @return
	 */
	public static final String convertDateToString(Date aDate) {
		return getDateTime(DATEPATTERN, aDate);
	}

	/**
	 * 按照日期格式，将字符串解析为日期对象
	 * 
	 * @param strDate
	 *            (格式 yyyy-MM-dd)
	 * @return
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String strDate)
			throws ParseException {
		Date aDate = null;
		try {
			aDate = convertStringToDate(DATEPATTERN, strDate);
		} catch (ParseException pe) {
			pe.printStackTrace();
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return aDate;
	}

	/**
	 * 时间相加
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date dateAdd(Date date, int day) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, day);
		return calendar.getTime();
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long dateDiffer(Date date1, Date date2) {
		return (date1.getTime() - date2.getTime()) / (1000 * 3600 * 24);
	}

}