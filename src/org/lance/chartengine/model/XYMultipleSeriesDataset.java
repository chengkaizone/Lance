package org.lance.chartengine.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 包含0-N个XYSeries
 * 
 * @author lance
 * 
 */
public class XYMultipleSeriesDataset implements Serializable {
	/** The included series. */
	private List<XYSeries> mSeries = new ArrayList<XYSeries>();

	/**
	 * Adds a new XY series to the list. 添加一个坐标系列到多系列中
	 * 
	 * @param series
	 *            the XY series to ass
	 */
	public synchronized void addSeries(XYSeries series) {
		mSeries.add(series);
	}

	/**
	 * Adds a new XY series to the list. 添加一个坐标系列到多系列中
	 * 
	 * @param index
	 *            the index in the series list
	 * @param series
	 *            the XY series to ass
	 */
	public synchronized void addSeries(int index, XYSeries series) {
		mSeries.add(index, series);
	}

	/**
	 * Removes the XY series from the list. 从多系列中移除指定的坐标系列
	 * 
	 * @param index
	 *            the index in the series list of the series to remove
	 */
	public synchronized void removeSeries(int index) {
		mSeries.remove(index);
	}

	/**
	 * Removes the XY series from the list. 从多系列中移除指定的系列
	 * 
	 * @param series
	 *            the XY series to be removed
	 */
	public synchronized void removeSeries(XYSeries series) {
		mSeries.remove(series);
	}

	/**
	 * Returns the XY series at the specified index. 返回指定索引的坐标系列
	 * 
	 * @param index
	 *            the index
	 * @return the XY series at the index
	 */
	public synchronized XYSeries getSeriesAt(int index) {
		return mSeries.get(index);
	}

	/**
	 * Returns the XY series count. 返回坐标系列的数量
	 * 
	 * @return the XY series count
	 */
	public synchronized int getSeriesCount() {
		return mSeries.size();
	}

	/**
	 * Returns an array of the XY series. 返回这个多系列的坐标系列数组
	 * 
	 * @return the XY series array
	 */
	public synchronized XYSeries[] getSeries() {
		return mSeries.toArray(new XYSeries[0]);
	}

	/**
	 * 添加数据
	 * 
	 * @param seriesTitles
	 * @param xValues
	 * @param yValues
	 */
	public synchronized void addXYDataset(String[] seriesTitles,
			List<double[]> xValues, List<double[]> yValues) {
		for (int i = 0; i < seriesTitles.length; i++) {
			XYSeries series = new XYSeries(seriesTitles[i]);
			double[] xV = xValues.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			addSeries(series);
		}
	}

	/**
	 * 添加时间类型数据
	 * 
	 * @param seriesTitles
	 * @param dates
	 * @param yValues
	 */
	public synchronized void addTimeDataset(String[] seriesTitles,
			List<Date[]> dates, List<double[]> yValues) {
		for (int i = 0; i < seriesTitles.length; i++) {
			TimeSeries series = new TimeSeries(seriesTitles[i]);
			Date[] xV = dates.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			addSeries(series);
		}
	}

	/**
	 * 添加分类数据
	 * 
	 * @param seriesTitles
	 * @param yValues
	 */
	public synchronized void addCategoryDataset(String[] seriesTitles,
			List<double[]> yValues) {
		for (int i = 0; i < seriesTitles.length; i++) {
			CategorySeries series = new CategorySeries(seriesTitles[i]);
			double[] v = yValues.get(i);
			for (int k = 0; k < v.length; k++) {
				series.add(v[k]);
			}
			addSeries(series.toXYSeries());
		}
	}
}
