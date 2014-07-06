package org.lance.chartengine.model;

import java.util.Date;

/**
 * ����/ʱ��ϵ��
 * 
 * @author lance
 * 
 */
public class TimeSeries extends XYSeries {

	/**
	 * Builds a new date / time series. ����һ������/ʱ��ϵ��
	 * 
	 * @param title
	 *            the series title
	 */
	public TimeSeries(String title) {
		super(title);
	}

	/**
	 * Adds a new value to the series. ���һ��ֵ��ϵ��
	 * 
	 * @param x
	 *            the date / time value for the X axis
	 * @param y
	 *            the value for the Y axis
	 */
	public synchronized void add(Date x, double y) {
		super.add(x.getTime(), y);
	}
}
