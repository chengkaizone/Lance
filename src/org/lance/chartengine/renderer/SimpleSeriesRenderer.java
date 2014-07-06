package org.lance.chartengine.renderer;

import java.io.Serializable;

import android.graphics.Color;
import android.graphics.Paint.Align;

/**
 * 简单系列渲染器
 * 
 * @author lance
 * 
 */
public class SimpleSeriesRenderer implements Serializable {
	/** The series color. */
	private int mColor = Color.BLUE;
	/** If the values should be displayed above the chart points. */
	private boolean mDisplayChartValues;
	/** The minimum distance between displaying chart values. */
	private int mDisplayChartValuesDistance = 100;
	/** The chart values text size. */
	private float mChartValuesTextSize = 10;
	/** The chart values text alignment. */
	private Align mChartValuesTextAlign = Align.CENTER;
	/** The chart values spacing from the data point. */
	private float mChartValuesSpacing = 5f;
	/** The stroke style. */
	private BasicStroke mStroke;
	/** If gradient is enabled. */
	private boolean mGradientEnabled = false;
	/** The gradient start value. */
	private double mGradientStartValue;
	/** The gradient start color. */
	private int mGradientStartColor;
	/** The gradient stop value. */
	private double mGradientStopValue;
	/** The gradient stop color. */
	private int mGradientStopColor;

	/**
	 * Returns the series color. 返回系列颜色
	 * 
	 * @return the series color
	 */
	public int getColor() {
		return mColor;
	}

	/**
	 * Sets the series color. 设置系列颜色
	 * 
	 * @param color
	 *            the series color
	 */
	public void setColor(int color) {
		mColor = color;
	}

	/**
	 * Returns if the chart point values should be displayed as text.
	 * 返回图表文字是否作为文本显示
	 * 
	 * @return if the chart point values should be displayed as text
	 */
	public boolean isDisplayChartValues() {
		return mDisplayChartValues;
	}

	/**
	 * Sets if the chart point values should be displayed as text. 设置图表点是否使用文本显示
	 * 
	 * @param display
	 *            if the chart point values should be displayed as text
	 */
	public void setDisplayChartValues(boolean display) {
		mDisplayChartValues = display;
	}

	/**
	 * Returns the chart values minimum distance. 返回图表的最小距离
	 * 
	 * @return the chart values minimum distance
	 */
	public int getDisplayChartValuesDistance() {
		return mDisplayChartValuesDistance;
	}

	/**
	 * Sets chart values minimum distance. 设置图表的最小距离
	 * 
	 * @param distance
	 *            the chart values minimum distance
	 */
	public void setDisplayChartValuesDistance(int distance) {
		mDisplayChartValuesDistance = distance;
	}

	/**
	 * Returns the chart values text size. 返回图表文本字体大小
	 * 
	 * @return the chart values text size
	 */
	public float getChartValuesTextSize() {
		return mChartValuesTextSize;
	}

	/**
	 * Sets the chart values text size. 设置图表文本字体大小
	 * 
	 * @param textSize
	 *            the chart values text size
	 */
	public void setChartValuesTextSize(float textSize) {
		mChartValuesTextSize = textSize;
	}

	/**
	 * Returns the chart values text align. 返回图表文本对齐属性
	 * 
	 * @return the chart values text align
	 */
	public Align getChartValuesTextAlign() {
		return mChartValuesTextAlign;
	}

	/**
	 * Sets the chart values text align. 设置图表文本对齐属性
	 * 
	 * @param align
	 *            the chart values text align
	 */
	public void setChartValuesTextAlign(Align align) {
		mChartValuesTextAlign = align;
	}

	/**
	 * Returns the chart values spacing from the data point. 返回值的图表数据点的间距
	 * 
	 * @return the chart values spacing
	 */
	public float getChartValuesSpacing() {
		return mChartValuesSpacing;
	}

	/**
	 * Sets the chart values spacing from the data point. 设置值的图表数据点的间距
	 * 
	 * @param spacing
	 *            the chart values spacing (in pixels) from the chart data point
	 */
	public void setChartValuesSpacing(float spacing) {
		mChartValuesSpacing = spacing;
	}

	/**
	 * Returns the stroke style. 返回笔画风格
	 * 
	 * @return the stroke style
	 */
	public BasicStroke getStroke() {
		return mStroke;
	}

	/**
	 * Sets the stroke style. 设置笔画风格
	 * 
	 * @param stroke
	 *            the stroke style
	 */
	public void setStroke(BasicStroke stroke) {
		mStroke = stroke;
	}

	/**
	 * Returns the gradient is enabled value. 返回渐变是否结束
	 * 
	 * @return the gradient enabled
	 */
	public boolean isGradientEnabled() {
		return mGradientEnabled;
	}

	/**
	 * Sets the gradient enabled value. 设置渐变的结束值
	 * 
	 * @param enabled
	 *            the gradient enabled
	 */
	public void setGradientEnabled(boolean enabled) {
		mGradientEnabled = enabled;
	}

	/**
	 * Returns the gradient start value. 返回起始渐变的值
	 * 
	 * @return the gradient start value
	 */
	public double getGradientStartValue() {
		return mGradientStartValue;
	}

	/**
	 * Returns the gradient start color. 返回开始渐变的颜色
	 * 
	 * @return the gradient start color
	 */
	public int getGradientStartColor() {
		return mGradientStartColor;
	}

	/**
	 * Sets the gradient start value and color. 设置渐变开支的颜色和值
	 * 
	 * @param start
	 *            the gradient start value
	 * @param color
	 *            the gradient start color
	 */
	public void setGradientStart(double start, int color) {
		mGradientStartValue = start;
		mGradientStartColor = color;
	}

	/**
	 * Returns the gradient stop value. 返回停止渐变的值
	 * 
	 * @return the gradient stop value
	 */
	public double getGradientStopValue() {
		return mGradientStopValue;
	}

	/**
	 * Returns the gradient stop color. 返回停止渐变的颜色
	 * 
	 * @return the gradient stop color
	 */
	public int getGradientStopColor() {
		return mGradientStopColor;
	}

	/**
	 * Sets the gradient stop value and color. 设置停止渐变的值和颜色
	 * 
	 * @param start
	 *            the gradient stop value
	 * @param color
	 *            the gradient stop color
	 */
	public void setGradientStop(double start, int color) {
		mGradientStopValue = start;
		mGradientStopColor = color;
	}

}
