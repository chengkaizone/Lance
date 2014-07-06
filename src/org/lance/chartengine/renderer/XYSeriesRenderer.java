package org.lance.chartengine.renderer;

import org.lance.chartengine.chart.PointStyle;

import android.graphics.Color;

/**
 * X,Y系列渲染器
 * 
 * @author lance
 * 
 */
public class XYSeriesRenderer extends SimpleSeriesRenderer {
	/** If the chart points should be filled. */
	private boolean mFillPoints = false;
	/** If the chart should be filled below its line. */
	private boolean mFillBelowLine = false;
	/** The fill below the chart line color. */
	private int mFillColor = Color.argb(125, 0, 0, 200);
	/** The point style. */
	private PointStyle mPointStyle = PointStyle.POINT;
	/** The chart line width. */
	private float mLineWidth = 1;

	/**
	 * Returns if the chart should be filled below the line. 返回图表在线下是否被填充
	 * 
	 * @return the fill below line status
	 */
	public boolean isFillBelowLine() {
		return mFillBelowLine;
	}

	/**
	 * Sets if the line chart should be filled below its line. Filling below the
	 * line transforms a line chart into an area chart. 如果折线图应填入下面的线返回true
	 * 填充下面的线转换成一个面积图折线图
	 * 
	 * @param fill
	 *            the fill below line flag value
	 */
	public void setFillBelowLine(boolean fill) {
		mFillBelowLine = fill;
	}

	/**
	 * Returns if the chart points should be filled. 返回图标点是否应该被填充
	 * 
	 * @return the points fill status
	 */
	public boolean isFillPoints() {
		return mFillPoints;
	}

	/**
	 * Sets if the chart points should be filled. 设置图标点是否应该被填充
	 * 
	 * @param fill
	 *            the points fill flag value
	 */
	public void setFillPoints(boolean fill) {
		mFillPoints = fill;
	}

	/**
	 * Returns the fill below line color. 返回线下填充颜色
	 * 
	 * @return the fill below line color
	 */
	public int getFillBelowLineColor() {
		return mFillColor;
	}

	/**
	 * Sets the fill below the line color. 在线下设置填充颜色
	 * 
	 * @param color
	 *            the fill below line color
	 */
	public void setFillBelowLineColor(int color) {
		mFillColor = color;
	}

	/**
	 * Returns the point style. 返回点的风格
	 * 
	 * @return the point style
	 */
	public PointStyle getPointStyle() {
		return mPointStyle;
	}

	/**
	 * Sets the point style. 设置点的风格
	 * 
	 * @param style
	 *            the point style
	 */
	public void setPointStyle(PointStyle style) {
		mPointStyle = style;
	}

	/**
	 * Returns the chart line width. 返回图表线的宽度
	 * 
	 * @return the line width
	 */
	public float getLineWidth() {
		return mLineWidth;
	}

	/**
	 * Sets the chart line width. 设置图标线的宽度
	 * 
	 * @param lineWidth
	 *            the line width
	 */
	public void setLineWidth(float lineWidth) {
		mLineWidth = lineWidth;
	}

}
