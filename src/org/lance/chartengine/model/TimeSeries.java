package org.lance.chartengine.model;

import java.util.Date;

/**
 * 日期/时间系列
 * 
 * @author lance
 * 
 */
public class TimeSeries extends XYSeries {

	/**
	 * Builds a new date / time series. 创建一个日期/时间系列
	 * 
	 * @param title
	 *            the series title
	 */
	public TimeSeries(String title) {
		super(title);
	}

	/**
	 * Adds a new value to the series. 添加一个值到系列
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
