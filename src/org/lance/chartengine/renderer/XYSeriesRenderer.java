package org.lance.chartengine.renderer;

import org.lance.chartengine.chart.PointStyle;

import android.graphics.Color;

/**
 * X,Yϵ����Ⱦ��
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
	 * Returns if the chart should be filled below the line. ����ͼ���������Ƿ����
	 * 
	 * @return the fill below line status
	 */
	public boolean isFillBelowLine() {
		return mFillBelowLine;
	}

	/**
	 * Sets if the line chart should be filled below its line. Filling below the
	 * line transforms a line chart into an area chart. �������ͼӦ����������߷���true
	 * ����������ת����һ�����ͼ����ͼ
	 * 
	 * @param fill
	 *            the fill below line flag value
	 */
	public void setFillBelowLine(boolean fill) {
		mFillBelowLine = fill;
	}

	/**
	 * Returns if the chart points should be filled. ����ͼ����Ƿ�Ӧ�ñ����
	 * 
	 * @return the points fill status
	 */
	public boolean isFillPoints() {
		return mFillPoints;
	}

	/**
	 * Sets if the chart points should be filled. ����ͼ����Ƿ�Ӧ�ñ����
	 * 
	 * @param fill
	 *            the points fill flag value
	 */
	public void setFillPoints(boolean fill) {
		mFillPoints = fill;
	}

	/**
	 * Returns the fill below line color. �������������ɫ
	 * 
	 * @return the fill below line color
	 */
	public int getFillBelowLineColor() {
		return mFillColor;
	}

	/**
	 * Sets the fill below the line color. ���������������ɫ
	 * 
	 * @param color
	 *            the fill below line color
	 */
	public void setFillBelowLineColor(int color) {
		mFillColor = color;
	}

	/**
	 * Returns the point style. ���ص�ķ��
	 * 
	 * @return the point style
	 */
	public PointStyle getPointStyle() {
		return mPointStyle;
	}

	/**
	 * Sets the point style. ���õ�ķ��
	 * 
	 * @param style
	 *            the point style
	 */
	public void setPointStyle(PointStyle style) {
		mPointStyle = style;
	}

	/**
	 * Returns the chart line width. ����ͼ���ߵĿ��
	 * 
	 * @return the line width
	 */
	public float getLineWidth() {
		return mLineWidth;
	}

	/**
	 * Sets the chart line width. ����ͼ���ߵĿ��
	 * 
	 * @param lineWidth
	 *            the line width
	 */
	public void setLineWidth(float lineWidth) {
		mLineWidth = lineWidth;
	}

}
