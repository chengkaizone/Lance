package org.lance.chartengine.chart;

/**
 * 图形点风格类型---枚举
 * 
 * @author lance
 * 
 */
public enum PointStyle {
	// 点的风格---X点,圆点(空心点),三角形,矩形,钻石,点
	X("x"), CIRCLE("circle"), TRIANGLE("triangle"), SQUARE("square"), DIAMOND(
			"diamond"), POINT("point");

	/** The point shape name. */
	private String mName;

	/**
	 * The point style enum constructor. 根据枚举构造器创建实例
	 * 
	 * @param name
	 *            the name
	 */
	private PointStyle(String name) {
		mName = name;
	}

	/**
	 * Returns the point shape name. 返回点名
	 * 
	 * @return the point shape name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Returns the point shape name. 点的名字
	 * 
	 * @return the point shape name
	 */
	public String toString() {
		return getName();
	}

	/**
	 * Return the point shape that has the provided symbol. 根据提供的标签名返回点风格
	 * 
	 * @param name
	 *            the point style name
	 * @return the point shape
	 */
	public static PointStyle getPointStyleForName(String name) {
		PointStyle pointStyle = null;
		PointStyle[] styles = values();
		int length = styles.length;
		for (int i = 0; i < length && pointStyle == null; i++) {
			if (styles[i].mName.equals(name)) {
				pointStyle = styles[i];
			}
		}
		return pointStyle;
	}

	/**
	 * Returns the point shape index based on the given name. 根据名字返回点的索引
	 * 
	 * @return the point shape index
	 */
	public static int getIndexForName(String name) {
		int index = -1;
		PointStyle[] styles = values();
		int length = styles.length;
		for (int i = 0; i < length && index < 0; i++) {
			if (styles[i].mName.equals(name)) {
				index = i;
			}
		}
		return Math.max(0, index);
	}

}
