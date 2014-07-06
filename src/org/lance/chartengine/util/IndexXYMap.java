package org.lance.chartengine.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * 需要为X的值排序
 * 
 * @author lance
 * 
 * @param <K>
 * @param <V>
 */
public class IndexXYMap<K, V> extends TreeMap<K, V> {
	private final List<K> indexList = new ArrayList<K>();

	private double maxXDifference = 0;

	public IndexXYMap() {
		super();
	}

	public V put(K key, V value) {
		indexList.add(key);
		updateMaxXDifference();
		return super.put(key, value);
	}

	private void updateMaxXDifference() {
		if (indexList.size() < 2) {
			maxXDifference = 0;
			return;
		}

		if (Math.abs((Double) indexList.get(indexList.size() - 1)
				- (Double) indexList.get(indexList.size() - 2)) > maxXDifference)
			maxXDifference = Math
					.abs((Double) indexList.get(indexList.size() - 1)
							- (Double) indexList.get(indexList.size() - 2));
	}

	public double getMaxXDifference() {
		return maxXDifference;
	}

	public void clear() {
		updateMaxXDifference();
		super.clear();
		indexList.clear();
	}

	/**
	 * Returns X-value according to the given index 返回给定坐标的X值
	 * 
	 * @param index
	 * @return the X value
	 */
	public K getXByIndex(int index) {
		return indexList.get(index);
	}

	/**
	 * Returns Y-value according to the given index 返回指定坐标的Y值
	 * 
	 * @param index
	 * @return the Y value
	 */
	public V getYByIndex(int index) {
		K key = indexList.get(index);
		return this.get(key);
	}

	/**
	 * Returns XY-entry according to the given index 返回指定位置的X,Y实体对象
	 * 
	 * @param index
	 * @return the X and Y values
	 */
	public XYEntry<K, V> getByIndex(int index) {
		K key = indexList.get(index);
		return new XYEntry<K, V>(key, this.get(key));
	}

	/**
	 * Removes entry from map by index 移除指定位置的X,Y实体对象
	 * 
	 * @param index
	 */
	public XYEntry<K, V> removeByIndex(int index) {
		K key = indexList.remove(index);
		return new XYEntry<K, V>(key, this.remove(key));
	}

	public int getIndexForKey(K key) {
		return Collections.binarySearch(indexList, key, null);
	}
}
