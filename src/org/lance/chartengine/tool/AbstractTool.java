package org.lance.chartengine.tool;

import org.lance.chartengine.chart.AbstractChart;
import org.lance.chartengine.chart.XYChart;
import org.lance.chartengine.renderer.XYMultipleSeriesRenderer;

/**
 * 用于被图形视图继承的抽象类
 * 
 * @author lance
 * 
 */
public abstract class AbstractTool {
	/** The chart. */
	protected AbstractChart mChart;
	/** The renderer. */
	protected XYMultipleSeriesRenderer mRenderer;

	/**
	 * Abstract tool constructor. 构造器
	 * 
	 * @param chart
	 *            the chart
	 */
	public AbstractTool(AbstractChart chart) {
		mChart = chart;
		if (chart instanceof XYChart) {
			mRenderer = ((XYChart) chart).getRenderer();
		}
	}

	/**
	 * Returns the current chart range. 返回当前图表的范围
	 * 
	 * @param scale
	 *            the scale
	 * @return the chart range
	 */
	public double[] getRange(int scale) {
		double minX = mRenderer.getXAxisMin(scale);
		double maxX = mRenderer.getXAxisMax(scale);
		double minY = mRenderer.getYAxisMin(scale);
		double maxY = mRenderer.getYAxisMax(scale);
		return new double[] { minX, maxX, minY, maxY };
	}

	/**
	 * Sets the range to the calculated one, if not already set. 如果没有设置
	 * 那么设置X,Y轴的取值范围
	 * 
	 * @param range
	 *            the range
	 * @param scale
	 *            the scale
	 */
	public void checkRange(double[] range, int scale) {
		if (mChart instanceof XYChart) {
			double[] calcRange = ((XYChart) mChart).getCalcRange(scale);
			if (calcRange != null) {
				if (!mRenderer.isMinXSet(scale)) {
					range[0] = calcRange[0];
					mRenderer.setXAxisMin(range[0], scale);
				}
				if (!mRenderer.isMaxXSet(scale)) {
					range[1] = calcRange[1];
					mRenderer.setXAxisMax(range[1], scale);
				}
				if (!mRenderer.isMinYSet(scale)) {
					range[2] = calcRange[2];
					mRenderer.setYAxisMin(range[2], scale);
				}
				if (!mRenderer.isMaxYSet(scale)) {
					range[3] = calcRange[3];
					mRenderer.setYAxisMax(range[3], scale);
				}
			}
		}
	}

	/**
	 * Sets a new range on the X axis. 在X轴上设置一个新的范围
	 * 
	 * @param min
	 *            the minimum value
	 * @param max
	 *            the maximum value
	 * @param scale
	 *            the scale
	 */
	protected void setXRange(double min, double max, int scale) {
		mRenderer.setXAxisMin(min, scale);
		mRenderer.setXAxisMax(max, scale);
	}

	/**
	 * Sets a new range on the Y axis. 在Y轴上设置一个新的范围
	 * 
	 * @param min
	 *            the minimum value
	 * @param max
	 *            the maximum value
	 * @param scale
	 *            the scale
	 */
	protected void setYRange(double min, double max, int scale) {
		mRenderer.setYAxisMin(min, scale);
		mRenderer.setYAxisMax(max, scale);
	}

}
