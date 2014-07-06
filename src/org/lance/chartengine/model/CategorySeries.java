package org.lance.chartengine.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 一个系列类别的图表--像饼图
 * 
 * @author lance
 * 
 */
public class CategorySeries implements Serializable {
	/** The series title. */
	private String mTitle;
	/** The series categories. */
	private List<String> mCategories = new ArrayList<String>();
	/** The series values. */
	private List<Double> mValues = new ArrayList<Double>();

	/**
	 * Builds a new category series. 创建分类系列实例
	 * 
	 * @param title
	 *            the series title
	 */
	public CategorySeries(String title) {
		mTitle = title;
	}

	/**
	 * Returns the series title. 返回系列标题
	 * 
	 * @return the series title
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Adds a new value to the series 添加一个值到系列
	 * 
	 * @param value
	 *            the new value
	 */
	public synchronized void add(double value) {
		add(mCategories.size() + "", value);
	}

	/**
	 * Adds a new value to the series. 添加一个值到系列
	 * 
	 * @param category
	 *            the category
	 * @param value
	 *            the new value
	 */
	public synchronized void add(String category, double value) {
		mCategories.add(category);
		mValues.add(value);
	}

	/**
	 * Replaces the value at the specific index in the series. 替代在系列指定的索引值
	 * 
	 * @param index
	 *            the index in the series
	 * @param category
	 *            the category
	 * @param value
	 *            the new value
	 */
	public synchronized void set(int index, String category, double value) {
		mCategories.set(index, category);
		mValues.set(index, value);
	}

	/**
	 * Removes an existing value from the series. 从系列中移除一个已经存在的值
	 * 
	 * @param index
	 *            the index in the series of the value to remove
	 */
	public synchronized void remove(int index) {
		mCategories.remove(index);
		mValues.remove(index);
	}

	/**
	 * Removes all the existing values from the series. 移除系列中的所有值
	 */
	public synchronized void clear() {
		mCategories.clear();
		mValues.clear();
	}

	/**
	 * Returns the value at the specified index. 返回指定索引的值
	 * 
	 * @param index
	 *            the index
	 * @return the value at the index
	 */
	public synchronized double getValue(int index) {
		return mValues.get(index);
	}

	/**
	 * Returns the category name at the specified index. 返回指定索引的分类名
	 * 
	 * @param index
	 *            the index
	 * @return the category name at the index
	 */
	public synchronized String getCategory(int index) {
		return mCategories.get(index);
	}

	/**
	 * Returns the series item count. 返回系列的item数量
	 * 
	 * @return the series item count
	 */
	public synchronized int getItemCount() {
		return mCategories.size();
	}

	/**
	 * Transforms the category series to an XY series. 转换分类系列微坐标系列
	 * 
	 * @return the XY series
	 */
	public XYSeries toXYSeries() {
		XYSeries xySeries = new XYSeries(mTitle);
		int k = 0;
		for (double value : mValues) {
			xySeries.add(++k, value);
		}
		return xySeries;
	}
}
