package org.lance.chartengine.model;

import java.util.ArrayList;
import java.util.List;

import org.lance.chartengine.util.MathHelper;

/**
 * 一个扩展的XY系列增添了第三维度。它是用于XY图表像泡沫
 * 
 * @author lance
 * 
 */
public class XYValueSeries extends XYSeries {
	/** A list to contain the series values. */
	private List<Double> mValue = new ArrayList<Double>();
	/** The minimum value. */
	private double mMinValue = MathHelper.NULL_VALUE;
	/** The maximum value. */
	private double mMaxValue = -MathHelper.NULL_VALUE;

	/**
	 * Builds a new XY value series. 创建一个坐标值的系列对象
	 * 
	 * @param title
	 *            the series title.
	 */
	public XYValueSeries(String title) {
		super(title);
	}

	/**
	 * Adds a new value to the series. 添加一个值到系列
	 * 
	 * @param x
	 *            the value for the X axis
	 * @param y
	 *            the value for the Y axis
	 * @param value
	 *            the value
	 */
	public synchronized void add(double x, double y, double value) {
		super.add(x, y);
		mValue.add(value);
		updateRange(value);
	}

	/**
	 * Initializes the values range. 初始化坐标值的范围
	 */
	private void initRange() {
		mMinValue = MathHelper.NULL_VALUE;
		mMaxValue = MathHelper.NULL_VALUE;
		int length = getItemCount();
		for (int k = 0; k < length; k++) {
			updateRange(getValue(k));
		}
	}

	/**
	 * Updates the values range. 更新坐标值得范围
	 * 
	 * @param value
	 *            the new value
	 */
	private void updateRange(double value) {
		mMinValue = Math.min(mMinValue, value);
		mMaxValue = Math.max(mMaxValue, value);
	}

	/**
	 * Adds a new value to the series. 添加一个值到系列中
	 * 
	 * @param x
	 *            the value for the X axis
	 * @param y
	 *            the value for the Y axis
	 */
	public synchronized void add(double x, double y) {
		add(x, y, 0d);
	}

	/**
	 * Removes an existing value from the series. 移除已存在指定索引的值
	 * 
	 * @param index
	 *            the index in the series of the value to remove
	 */
	public synchronized void remove(int index) {
		super.remove(index);
		double removedValue = mValue.remove(index);
		if (removedValue == mMinValue || removedValue == mMaxValue) {
			initRange();
		}
	}

	/**
	 * Removes all the values from the series. 移除系列中的所有值
	 */
	public synchronized void clear() {
		super.clear();
		mValue.clear();
		initRange();
	}

	/**
	 * Returns the value at the specified index. 返回指定索引的值
	 * 
	 * @param index
	 *            the index
	 * @return the value
	 */
	public synchronized double getValue(int index) {
		return mValue.get(index);
	}

	/**
	 * Returns the minimum value. 返回最小值
	 * 
	 * @return the minimum value
	 */
	public double getMinValue() {
		return mMinValue;
	}

	/**
	 * Returns the maximum value. 返回最大值
	 * 
	 * @return the maximum value
	 */
	public double getMaxValue() {
		return mMaxValue;
	}

}
