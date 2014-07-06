package org.lance.chartengine.renderer;

import java.io.Serializable;

import android.graphics.Color;
import android.graphics.Paint.Align;

/**
 * ��ϵ����Ⱦ��
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
	 * Returns the series color. ����ϵ����ɫ
	 * 
	 * @return the series color
	 */
	public int getColor() {
		return mColor;
	}

	/**
	 * Sets the series color. ����ϵ����ɫ
	 * 
	 * @param color
	 *            the series color
	 */
	public void setColor(int color) {
		mColor = color;
	}

	/**
	 * Returns if the chart point values should be displayed as text.
	 * ����ͼ�������Ƿ���Ϊ�ı���ʾ
	 * 
	 * @return if the chart point values should be displayed as text
	 */
	public boolean isDisplayChartValues() {
		return mDisplayChartValues;
	}

	/**
	 * Sets if the chart point values should be displayed as text. ����ͼ����Ƿ�ʹ���ı���ʾ
	 * 
	 * @param display
	 *            if the chart point values should be displayed as text
	 */
	public void setDisplayChartValues(boolean display) {
		mDisplayChartValues = display;
	}

	/**
	 * Returns the chart values minimum distance. ����ͼ�����С����
	 * 
	 * @return the chart values minimum distance
	 */
	public int getDisplayChartValuesDistance() {
		return mDisplayChartValuesDistance;
	}

	/**
	 * Sets chart values minimum distance. ����ͼ�����С����
	 * 
	 * @param distance
	 *            the chart values minimum distance
	 */
	public void setDisplayChartValuesDistance(int distance) {
		mDisplayChartValuesDistance = distance;
	}

	/**
	 * Returns the chart values text size. ����ͼ���ı������С
	 * 
	 * @return the chart values text size
	 */
	public float getChartValuesTextSize() {
		return mChartValuesTextSize;
	}

	/**
	 * Sets the chart values text size. ����ͼ���ı������С
	 * 
	 * @param textSize
	 *            the chart values text size
	 */
	public void setChartValuesTextSize(float textSize) {
		mChartValuesTextSize = textSize;
	}

	/**
	 * Returns the chart values text align. ����ͼ���ı���������
	 * 
	 * @return the chart values text align
	 */
	public Align getChartValuesTextAlign() {
		return mChartValuesTextAlign;
	}

	/**
	 * Sets the chart values text align. ����ͼ���ı���������
	 * 
	 * @param align
	 *            the chart values text align
	 */
	public void setChartValuesTextAlign(Align align) {
		mChartValuesTextAlign = align;
	}

	/**
	 * Returns the chart values spacing from the data point. ����ֵ��ͼ�����ݵ�ļ��
	 * 
	 * @return the chart values spacing
	 */
	public float getChartValuesSpacing() {
		return mChartValuesSpacing;
	}

	/**
	 * Sets the chart values spacing from the data point. ����ֵ��ͼ�����ݵ�ļ��
	 * 
	 * @param spacing
	 *            the chart values spacing (in pixels) from the chart data point
	 */
	public void setChartValuesSpacing(float spacing) {
		mChartValuesSpacing = spacing;
	}

	/**
	 * Returns the stroke style. ���رʻ����
	 * 
	 * @return the stroke style
	 */
	public BasicStroke getStroke() {
		return mStroke;
	}

	/**
	 * Sets the stroke style. ���ñʻ����
	 * 
	 * @param stroke
	 *            the stroke style
	 */
	public void setStroke(BasicStroke stroke) {
		mStroke = stroke;
	}

	/**
	 * Returns the gradient is enabled value. ���ؽ����Ƿ����
	 * 
	 * @return the gradient enabled
	 */
	public boolean isGradientEnabled() {
		return mGradientEnabled;
	}

	/**
	 * Sets the gradient enabled value. ���ý���Ľ���ֵ
	 * 
	 * @param enabled
	 *            the gradient enabled
	 */
	public void setGradientEnabled(boolean enabled) {
		mGradientEnabled = enabled;
	}

	/**
	 * Returns the gradient start value. ������ʼ�����ֵ
	 * 
	 * @return the gradient start value
	 */
	public double getGradientStartValue() {
		return mGradientStartValue;
	}

	/**
	 * Returns the gradient start color. ���ؿ�ʼ�������ɫ
	 * 
	 * @return the gradient start color
	 */
	public int getGradientStartColor() {
		return mGradientStartColor;
	}

	/**
	 * Sets the gradient start value and color. ���ý��俪֧����ɫ��ֵ
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
	 * Returns the gradient stop value. ����ֹͣ�����ֵ
	 * 
	 * @return the gradient stop value
	 */
	public double getGradientStopValue() {
		return mGradientStopValue;
	}

	/**
	 * Returns the gradient stop color. ����ֹͣ�������ɫ
	 * 
	 * @return the gradient stop color
	 */
	public int getGradientStopColor() {
		return mGradientStopColor;
	}

	/**
	 * Sets the gradient stop value and color. ����ֹͣ�����ֵ����ɫ
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
