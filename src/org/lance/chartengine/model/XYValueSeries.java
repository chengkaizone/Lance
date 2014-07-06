package org.lance.chartengine.model;

import java.util.ArrayList;
import java.util.List;

import org.lance.chartengine.util.MathHelper;

/**
 * һ����չ��XYϵ�������˵���ά�ȡ���������XYͼ������ĭ
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
	 * Builds a new XY value series. ����һ������ֵ��ϵ�ж���
	 * 
	 * @param title
	 *            the series title.
	 */
	public XYValueSeries(String title) {
		super(title);
	}

	/**
	 * Adds a new value to the series. ���һ��ֵ��ϵ��
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
	 * Initializes the values range. ��ʼ������ֵ�ķ�Χ
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
	 * Updates the values range. ��������ֵ�÷�Χ
	 * 
	 * @param value
	 *            the new value
	 */
	private void updateRange(double value) {
		mMinValue = Math.min(mMinValue, value);
		mMaxValue = Math.max(mMaxValue, value);
	}

	/**
	 * Adds a new value to the series. ���һ��ֵ��ϵ����
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
	 * Removes an existing value from the series. �Ƴ��Ѵ���ָ��������ֵ
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
	 * Removes all the values from the series. �Ƴ�ϵ���е�����ֵ
	 */
	public synchronized void clear() {
		super.clear();
		mValue.clear();
		initRange();
	}

	/**
	 * Returns the value at the specified index. ����ָ��������ֵ
	 * 
	 * @param index
	 *            the index
	 * @return the value
	 */
	public synchronized double getValue(int index) {
		return mValue.get(index);
	}

	/**
	 * Returns the minimum value. ������Сֵ
	 * 
	 * @return the minimum value
	 */
	public double getMinValue() {
		return mMinValue;
	}

	/**
	 * Returns the maximum value. �������ֵ
	 * 
	 * @return the maximum value
	 */
	public double getMaxValue() {
		return mMaxValue;
	}

}
