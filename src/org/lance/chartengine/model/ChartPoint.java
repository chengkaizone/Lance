package org.lance.chartengine.model;

import java.io.Serializable;

/**
 * 用一个类来封装一个指定的点图表专用的点对象
 * 
 * @author lance
 * 
 */
public final class ChartPoint implements Serializable {
	/** The X axis coordinate value. */
	private float mX;
	/** The Y axis coordinate value. */
	private float mY;

	public ChartPoint() {
	}

	public ChartPoint(float x, float y) {
		mX = x;
		mY = y;
	}

	public float getX() {
		return mX;
	}

	public float getY() {
		return mY;
	}

	public void setX(float x) {
		mX = x;
	}

	public void setY(float y) {
		mY = y;
	}
}
