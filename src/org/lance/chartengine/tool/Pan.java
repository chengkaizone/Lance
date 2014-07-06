package org.lance.chartengine.tool;

import java.util.ArrayList;
import java.util.List;

import org.lance.chartengine.chart.AbstractChart;
import org.lance.chartengine.chart.RoundChart;
import org.lance.chartengine.chart.XYChart;

/**
 * 盘工具
 * 
 * @author lance
 * 
 */
public class Pan extends AbstractTool {
	/** The pan listeners. */
	private List<PanListener> mPanListeners = new ArrayList<PanListener>();
	/** Pan limits reached on the X axis. */
	private boolean limitsReachedX = false;
	/** Pan limits reached on the X axis. */
	private boolean limitsReachedY = false;

	/**
	 * Builds and instance of the pan tool. 创建盘工具实例
	 * 
	 * @param chart
	 *            the XY chart
	 */
	public Pan(AbstractChart chart) {
		super(chart);
	}

	/**
	 * Apply the tool. 执行工具
	 * 
	 * @param oldX
	 *            the previous location on X axis
	 * @param oldY
	 *            the previous location on Y axis
	 * @param newX
	 *            the current location on X axis
	 * @param newY
	 *            the current location on the Y axis
	 */
	public void apply(float oldX, float oldY, float newX, float newY) {
		boolean notLimitedUp = true;
		boolean notLimitedBottom = true;
		boolean notLimitedLeft = true;
		boolean notLimitedRight = true;
		if (mChart instanceof XYChart) {
			int scales = mRenderer.getScalesCount();
			double[] limits = mRenderer.getPanLimits();
			boolean limited = limits != null && limits.length == 4;
			XYChart chart = (XYChart) mChart;
			for (int i = 0; i < scales; i++) {
				double[] range = getRange(i);
				double[] calcRange = chart.getCalcRange(i);
				if (limitsReachedX
						&& limitsReachedY
						&& (range[0] == range[1]
								&& calcRange[0] == calcRange[1] || range[2] == range[3]
								&& calcRange[2] == calcRange[3])) {
					return;
				}
				checkRange(range, i);

				double[] realPoint = chart.toRealPoint(oldX, oldY, i);
				double[] realPoint2 = chart.toRealPoint(newX, newY, i);
				double deltaX = realPoint[0] - realPoint2[0];
				double deltaY = realPoint[1] - realPoint2[1];
				double ratio = getAxisRatio(range);
				if (chart.isVertical(mRenderer)) {
					double newDeltaX = -deltaY * ratio;
					double newDeltaY = deltaX / ratio;
					deltaX = newDeltaX;
					deltaY = newDeltaY;
				}
				if (mRenderer.isPanXEnabled()) {
					if (limits != null) {
						if (notLimitedLeft) {
							notLimitedLeft = limits[0] <= range[0] + deltaX;
						}
						if (notLimitedRight) {
							notLimitedRight = limits[1] >= range[1] + deltaX;
						}
					}
					if (!limited || (notLimitedLeft && notLimitedRight)) {
						setXRange(range[0] + deltaX, range[1] + deltaX, i);
						limitsReachedX = false;
					} else {
						limitsReachedX = true;
					}
				}
				if (mRenderer.isPanYEnabled()) {
					if (limits != null) {
						if (notLimitedBottom) {
							notLimitedBottom = limits[2] <= range[2] + deltaY;
						}
						if (notLimitedUp) {
							notLimitedUp = limits[3] >= range[3] + deltaY;
						}
					}
					if (!limited || (notLimitedBottom && notLimitedUp)) {
						setYRange(range[2] + deltaY, range[3] + deltaY, i);
						limitsReachedY = false;
					} else {
						limitsReachedY = true;
					}
				}
			}
		} else {
			RoundChart chart = (RoundChart) mChart;
			chart.setCenterX(chart.getCenterX() + (int) (newX - oldX));
			chart.setCenterY(chart.getCenterY() + (int) (newY - oldY));
		}
		notifyPanListeners();
	}

	/**
	 * Return the X / Y axis range ratio. 返回X/Y轴的比例范围
	 * 
	 * @param range
	 *            the axis range
	 * @return the ratio
	 */
	private double getAxisRatio(double[] range) {
		return Math.abs(range[1] - range[0]) / Math.abs(range[3] - range[2]);
	}

	/**
	 * Notify the pan listeners about a pan. 通知盘监听器执行
	 */
	private synchronized void notifyPanListeners() {
		for (PanListener listener : mPanListeners) {
			listener.panApplied();
		}
	}

	/**
	 * Adds a new pan listener. 添加一个盘监听
	 * 
	 * @param listener
	 *            pan listener
	 */
	public synchronized void addPanListener(PanListener listener) {
		mPanListeners.add(listener);
	}

	/**
	 * Removes a pan listener. 移除盘监听
	 * 
	 * @param listener
	 *            pan listener
	 */
	public synchronized void removePanListener(PanListener listener) {
		mPanListeners.remove(listener);
	}

}
