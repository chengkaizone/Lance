package org.lance.chartengine.tool;

import java.util.ArrayList;
import java.util.List;

import org.lance.chartengine.chart.AbstractChart;
import org.lance.chartengine.chart.RoundChart;
import org.lance.chartengine.chart.XYChart;
import org.lance.chartengine.renderer.DefaultRenderer;

/**
 * 缩放工具
 * 
 * @author lance
 * 
 */
public class Zoom extends AbstractTool {
	/** A flag to be used to know if this is a zoom in or out. */
	private boolean mZoomIn;
	/** The zoom rate. */
	private float mZoomRate;
	/** The zoom listeners. */
	private List<ZoomListener> mZoomListeners = new ArrayList<ZoomListener>();
	/** Zoom limits reached on the X axis. */
	private boolean limitsReachedX = false;
	/** Zoom limits reached on the Y axis. */
	private boolean limitsReachedY = false;

	/** Zoom on X axis and Y axis */
	public static final int ZOOM_AXIS_XY = 0;
	/** Zoom on X axis independently */
	public static final int ZOOM_AXIS_X = 1;
	/** Zoom on Y axis independently */
	public static final int ZOOM_AXIS_Y = 2;

	/**
	 * Builds the zoom tool. 创建缩放工具
	 * 
	 * @param chart
	 *            the chart
	 * @param in
	 *            zoom in or out
	 * @param rate
	 *            the zoom rate
	 */
	public Zoom(AbstractChart chart, boolean in, float rate) {
		super(chart);
		mZoomIn = in;
		setZoomRate(rate);
	}

	/**
	 * Sets the zoom rate. 设置缩放比率
	 * 
	 * @param rate
	 */
	public void setZoomRate(float rate) {
		mZoomRate = rate;
	}

	/**
	 * Apply the zoom. 执行缩放
	 */
	public void apply(int zoom_axis) {
		if (mChart instanceof XYChart) {
			int scales = mRenderer.getScalesCount();
			for (int i = 0; i < scales; i++) {
				double[] range = getRange(i);
				checkRange(range, i);
				double[] limits = mRenderer.getZoomLimits();

				double centerX = (range[0] + range[1]) / 2;
				double centerY = (range[2] + range[3]) / 2;
				double newWidth = range[1] - range[0];
				double newHeight = range[3] - range[2];
				double newXMin = centerX - newWidth / 2;
				double newXMax = centerX + newWidth / 2;
				double newYMin = centerY - newHeight / 2;
				double newYMax = centerY + newHeight / 2;

				// if already reached last zoom, then it will always set to
				// reached
				if (i == 0) {
					limitsReachedX = limits != null
							&& (newXMin <= limits[0] || newXMax >= limits[1]);
					limitsReachedY = limits != null
							&& (newYMin <= limits[2] || newYMax >= limits[3]);
				}

				if (mZoomIn) {
					if (mRenderer.isZoomXEnabled()
							&& (zoom_axis == ZOOM_AXIS_X || zoom_axis == ZOOM_AXIS_XY)) {
						if (limitsReachedX && mZoomRate < 1) {
							// ignore pinch zoom out once reached X limit
						} else {
							newWidth /= mZoomRate;
						}
					}

					if (mRenderer.isZoomYEnabled()
							&& (zoom_axis == ZOOM_AXIS_Y || zoom_axis == ZOOM_AXIS_XY)) {
						if (limitsReachedY && mZoomRate < 1) {
						} else {
							newHeight /= mZoomRate;
						}
					}
				} else {
					if (mRenderer.isZoomXEnabled()
							&& !limitsReachedX
							&& (zoom_axis == ZOOM_AXIS_X || zoom_axis == ZOOM_AXIS_XY)) {
						newWidth *= mZoomRate;
					}

					if (mRenderer.isZoomYEnabled()
							&& !limitsReachedY
							&& (zoom_axis == ZOOM_AXIS_Y || zoom_axis == ZOOM_AXIS_XY)) {
						newHeight *= mZoomRate;
					}
				}
				if (mRenderer.isZoomXEnabled()
						&& (zoom_axis == ZOOM_AXIS_X || zoom_axis == ZOOM_AXIS_XY)) {
					newXMin = centerX - newWidth / 2;
					newXMax = centerX + newWidth / 2;
					setXRange(newXMin, newXMax, i);
				}
				if (mRenderer.isZoomYEnabled()
						&& (zoom_axis == ZOOM_AXIS_Y || zoom_axis == ZOOM_AXIS_XY)) {
					newYMin = centerY - newHeight / 2;
					newYMax = centerY + newHeight / 2;
					setYRange(newYMin, newYMax, i);
				}
			}
		} else {
			DefaultRenderer renderer = ((RoundChart) mChart).getRenderer();
			if (mZoomIn) {
				renderer.setScale(renderer.getScale() * mZoomRate);
			} else {
				renderer.setScale(renderer.getScale() / mZoomRate);
			}
		}
		notifyZoomListeners(new ZoomEvent(mZoomIn, mZoomRate));
	}

	/**
	 * Notify the zoom listeners about a zoom change. 通知缩放监听执行缩放
	 * 
	 * @param e
	 *            the zoom event
	 */
	private synchronized void notifyZoomListeners(ZoomEvent e) {
		for (ZoomListener listener : mZoomListeners) {
			listener.zoomApplied(e);
		}
	}

	/**
	 * Notify the zoom listeners about a zoom reset. 通知缩放监听重置
	 */
	public synchronized void notifyZoomResetListeners() {
		for (ZoomListener listener : mZoomListeners) {
			listener.zoomReset();
		}
	}

	/**
	 * Adds a new zoom listener. 添加缩放监听
	 * 
	 * @param listener
	 *            zoom listener
	 */
	public synchronized void addZoomListener(ZoomListener listener) {
		mZoomListeners.add(listener);
	}

	/**
	 * Removes a zoom listener. 移除缩放监听
	 * 
	 * @param listener
	 *            zoom listener
	 */
	public synchronized void removeZoomListener(ZoomListener listener) {
		mZoomListeners.remove(listener);
	}

}
