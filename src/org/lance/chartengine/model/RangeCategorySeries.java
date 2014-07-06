package org.lance.chartengine.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个系列的范围类别图表如范围栏
 * 
 * @author lance
 * 
 */
public class RangeCategorySeries extends CategorySeries {
	/** The series values. */
	private List<Double> mMaxValues = new ArrayList<Double>();

	/**
	 * Builds a new category series. 创建一个分类系列
	 * 
	 * @param title
	 *            the series title
	 */
	public RangeCategorySeries(String title) {
		super(title);
	}

	/**
	 * Adds new values to the series 添加一个值到系列
	 * 
	 * @param minValue
	 *            the new minimum value
	 * @param maxValue
	 *            the new maximum value
	 */
	public synchronized void add(double minValue, double maxValue) {
		super.add(minValue);
		mMaxValues.add(maxValue);
	}

	/**
	 * Adds new values to the series. 添加一个值到系列
	 * 
	 * @param category
	 *            the category
	 * @param minValue
	 *            the new minimum value
	 * @param maxValue
	 *            the new maximum value
	 */
	public synchronized void add(String category, double minValue,
			double maxValue) {
		super.add(category, minValue);
		mMaxValues.add(maxValue);
	}

	/**
	 * Removes existing values from the series. 从系列中移除一个已经存在的值
	 * 
	 * @param index
	 *            the index in the series of the values to remove
	 */
	public synchronized void remove(int index) {
		super.remove(index);
		mMaxValues.remove(index);
	}

	/**
	 * Removes all the existing values from the series. 移除系列中所有的值
	 */
	public synchronized void clear() {
		super.clear();
		mMaxValues.clear();
	}

	/**
	 * Returns the minimum value at the specified index. 返回指定索引的最小值
	 * 
	 * @param index
	 *            the index
	 * @return the minimum value at the index
	 */
	public double getMinimumValue(int index) {
		return getValue(index);
	}

	/**
	 * Returns the maximum value at the specified index. 返回指定索引的最大值
	 * 
	 * @param index
	 *            the index
	 * @return the maximum value at the index
	 */
	public double getMaximumValue(int index) {
		return mMaxValues.get(index);
	}

	/**
	 * Transforms the range category series to an XY series. 转换分类系列为坐标系列
	 * 
	 * @return the XY series
	 */
	public XYSeries toXYSeries() {
		XYSeries xySeries = new XYSeries(getTitle());
		int length = getItemCount();
		for (int k = 0; k < length; k++) {
			xySeries.add(k + 1, getMinimumValue(k));
			// the new fast XYSeries implementation doesn't allow 2 values at
			// the same X,
			// so I had to do a hack until I find a better solution
			xySeries.add(k + 1.000001, getMaximumValue(k));
		}
		return xySeries;
	}
}
