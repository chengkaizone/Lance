package org.lance.chartengine.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 多分类的系列图表---如圆环图
 * 
 * @author lance
 * 
 */
public class MultipleCategorySeries implements Serializable {
	/** The series title. */
	private String mTitle;
	/** The series local keys. */
	private List<String> mCategories = new ArrayList<String>();
	/** The series name. */
	private List<String[]> mTitles = new ArrayList<String[]>();
	/** The series values. */
	private List<double[]> mValues = new ArrayList<double[]>();

	/**
	 * Builds a new category series. 创建分类系列
	 * 
	 * @param title
	 *            the series title
	 */
	public MultipleCategorySeries(String title) {
		mTitle = title;
	}

	/**
	 * Adds a new value to the series 添加一个值到系列
	 * 
	 * @param titles
	 *            the titles to be used as labels
	 * @param values
	 *            the new value
	 */
	public void add(String[] titles, double[] values) {
		add(mCategories.size() + "", titles, values);
	}

	/**
	 * Adds a new value to the series. 添加一个值到系列
	 * 
	 * @param category
	 *            the category name
	 * @param titles
	 *            the titles to be used as labels
	 * @param values
	 *            the new value
	 */
	public void add(String category, String[] titles, double[] values) {
		mCategories.add(category);
		mTitles.add(titles);
		mValues.add(values);
	}

	/**
	 * Removes an existing value from the series. 从系列中移除一个已经存在的值
	 * 
	 * @param index
	 *            the index in the series of the value to remove
	 */
	public void remove(int index) {
		mCategories.remove(index);
		mTitles.remove(index);
		mValues.remove(index);
	}

	/**
	 * Removes all the existing values from the series. 移除系列中的所有值
	 */
	public void clear() {
		mCategories.clear();
		mTitles.clear();
		mValues.clear();
	}

	/**
	 * Returns the values at the specified index. 返回系列中指定索引的值
	 * 
	 * @param index
	 *            the index
	 * @return the value at the index
	 */
	public double[] getValues(int index) {
		return mValues.get(index);
	}

	/**
	 * Returns the category name at the specified index. 返回系列中指定索引的分类名
	 * 
	 * @param index
	 *            the index
	 * @return the category name at the index
	 */
	public String getCategory(int index) {
		return mCategories.get(index);
	}

	/**
	 * Returns the categories count. 返回分类数量
	 * 
	 * @return the categories count
	 */
	public int getCategoriesCount() {
		return mCategories.size();
	}

	/**
	 * Returns the series item count. 返回系列中的item数量
	 * 
	 * @param index
	 *            the index
	 * @return the series item count
	 */
	public int getItemCount(int index) {
		return mValues.get(index).length;
	}

	/**
	 * Returns the series titles. 返回系列的所有标题
	 * 
	 * @param index
	 *            the index
	 * @return the series titles
	 */
	public String[] getTitles(int index) {
		return mTitles.get(index);
	}

	/**
	 * Transforms the category series to an XY series. 转换分类系列微坐标系列
	 * 
	 * @return the XY series
	 */
	public XYSeries toXYSeries() {
		XYSeries xySeries = new XYSeries(mTitle);
		return xySeries;
	}
}
