package org.lance.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * ��������ʱ��Ĺ�����
 * 
 * @author lance
 * 
 */
public class DateService {
	// Ĭ�����ڸ�ʽ
	public static final String DATEPATTERN = "yyyy-MM-dd";
	// Ĭ��ʱ���ʽ
	public static final String TIMEPATTERN = "HH:mm";

	/**
	 * ����ʱ��Ϊlong��
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
	 * Return ȱʡ�����ڸ�ʽ (yyyy-MM-dd)
	 * 
	 * @return ��ҳ������ʾ�����ڸ�ʽ
	 */
	public static String getDATEPATTERN() {
		return DATEPATTERN;
	}

	/**
	 * �������ڸ�ʽ�����ַ�������Ϊ���ڶ���
	 * 
	 * @param aMask
	 *            �����ַ����ĸ�ʽ
	 * @param strDate
	 *            һ����aMask��ʽ���е����ڵ��ַ�������
	 * @return Date ����
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
	 * ����ָ����ʽ���������ַ�����ʾ
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
	 * �������ڸ�ʽ���������ڰ�DATEPATTERN��ʽת������ַ���
	 * 
	 * @param aDate
	 * @return
	 */
	public static final String convertDateToString(Date aDate) {
		return getDateTime(DATEPATTERN, aDate);
	}

	/**
	 * �������ڸ�ʽ�����ַ�������Ϊ���ڶ���
	 * 
	 * @param strDate
	 *            (��ʽ yyyy-MM-dd)
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
	 * ʱ�����
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
	 * ��ȡ��������֮�������
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long dateDiffer(Date date1, Date date2) {
		return (date1.getTime() - date2.getTime()) / (1000 * 3600 * 24);
	}

}