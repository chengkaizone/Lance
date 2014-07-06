package org.lance.chartengine.client;

import org.lance.chartengine.chart.AbstractChart;
import org.lance.chartengine.chart.RoundChart;
import org.lance.chartengine.chart.XYChart;
import org.lance.chartengine.renderer.DefaultRenderer;
import org.lance.chartengine.tool.Pan;
import org.lance.chartengine.tool.PanListener;
import org.lance.chartengine.tool.Zoom;
import org.lance.chartengine.tool.ZoomListener;

import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * 触摸事件的主要处理器
 * 
 * @author lance
 * 
 */
public class TouchHandler implements ITouchHandler {
	/** The chart renderer. */
	private DefaultRenderer mRenderer;
	/** The old x coordinate. */
	private float oldX;
	/** The old y coordinate. */
	private float oldY;
	/** The old x2 coordinate. */
	private float oldX2;
	/** The old y2 coordinate. */
	private float oldY2;
	/** The zoom buttons rectangle. */
	private RectF zoomR = new RectF();
	/** The pan tool. */
	private Pan mPan;
	/** The zoom for the pinch gesture. */
	private Zoom mPinchZoom;
	/** The graphical view. */
	private GraphicalView graphicalView;

	/**
	 * Creates an implementation of the new version of the touch handler.
	 * 创建一个对应新版本的触摸处理器
	 * 
	 * @param view
	 *            the graphical view
	 * @param chart
	 *            the chart to be drawn
	 */
	public TouchHandler(GraphicalView view, AbstractChart chart) {
		graphicalView = view;
		zoomR = graphicalView.getZoomRectangle();
		if (chart instanceof XYChart) {
			mRenderer = ((XYChart) chart).getRenderer();
		} else {
			mRenderer = ((RoundChart) chart).getRenderer();
		}
		if (mRenderer.isPanEnabled()) {
			mPan = new Pan(chart);
		}
		if (mRenderer.isZoomEnabled()) {
			mPinchZoom = new Zoom(chart, true, 1);
		}
	}

	/**
	 * Handles the touch event. 处理缩放事件
	 * 
	 * @param event
	 *            the touch event
	 */
	public boolean handleTouch(MotionEvent event) {
		int action = event.getAction();
		if (mRenderer != null && action == MotionEvent.ACTION_MOVE) {
			if (oldX >= 0 || oldY >= 0) {
				float newX = event.getX(0);
				float newY = event.getY(0);
				if (event.getPointerCount() > 1 && (oldX2 >= 0 || oldY2 >= 0)
						&& mRenderer.isZoomEnabled()) {
					float newX2 = event.getX(1);
					float newY2 = event.getY(1);
					float newDeltaX = Math.abs(newX - newX2);
					float newDeltaY = Math.abs(newY - newY2);
					float oldDeltaX = Math.abs(oldX - oldX2);
					float oldDeltaY = Math.abs(oldY - oldY2);
					float zoomRate = 1;

					float tan1 = Math.abs(newY - oldY) / Math.abs(newX - oldX);
					float tan2 = Math.abs(newY2 - oldY2)
							/ Math.abs(newX2 - oldX2);
					if (tan1 <= 0.25 && tan2 <= 0.25) {
						// horizontal pinch zoom, |deltaY| / |deltaX| is [0 ~
						// 0.25], 0.25 is
						// the approximate value of tan(PI / 12)
						zoomRate = newDeltaX / oldDeltaX;
						applyZoom(zoomRate, Zoom.ZOOM_AXIS_X);
					} else if (tan1 >= 3.73 && tan2 >= 3.73) {
						// pinch zoom vertically, |deltaY| / |deltaX| is [3.73 ~
						// infinity],
						// 3.732 is the approximate value of tan(PI / 2 - PI /
						// 12)
						zoomRate = newDeltaY / oldDeltaY;
						applyZoom(zoomRate, Zoom.ZOOM_AXIS_Y);
					} else {
						// pinch zoom diagonally
						if (Math.abs(newX - oldX) >= Math.abs(newY - oldY)) {
							zoomRate = newDeltaX / oldDeltaX;
						} else {
							zoomRate = newDeltaY / oldDeltaY;
						}
						applyZoom(zoomRate, Zoom.ZOOM_AXIS_XY);
					}
					oldX2 = newX2;
					oldY2 = newY2;
				} else if (mRenderer.isPanEnabled()) {
					mPan.apply(oldX, oldY, newX, newY);
					oldX2 = 0;
					oldY2 = 0;
				}
				oldX = newX;
				oldY = newY;
				graphicalView.repaint();
				return true;
			}
		} else if (action == MotionEvent.ACTION_DOWN) {
			oldX = event.getX(0);
			oldY = event.getY(0);
			if (mRenderer != null && mRenderer.isZoomEnabled()
					&& zoomR.contains(oldX, oldY)) {
				if (oldX < zoomR.left + zoomR.width() / 3) {
					graphicalView.zoomIn();
				} else if (oldX < zoomR.left + zoomR.width() * 2 / 3) {
					graphicalView.zoomOut();
				} else {
					graphicalView.zoomReset();
				}
				return true;
			}
		} else if (action == MotionEvent.ACTION_UP
				|| action == MotionEvent.ACTION_POINTER_UP) {
			oldX = 0;
			oldY = 0;
			oldX2 = 0;
			oldY2 = 0;
			if (action == MotionEvent.ACTION_POINTER_UP) {
				oldX = -1;
				oldY = -1;
			}
		}
		return !mRenderer.isClickEnabled();
	}

	/**
	 * 执行缩放
	 * 
	 * @param zoomRate
	 * @param axis
	 */
	private void applyZoom(float zoomRate, int axis) {
		if (zoomRate > 0.9 && zoomRate < 1.1) {
			mPinchZoom.setZoomRate(zoomRate);
			mPinchZoom.apply(axis);
		}
	}

	/**
	 * Adds a new zoom listener. 添加缩放监听
	 * 
	 * @param listener
	 *            zoom listener
	 */
	public void addZoomListener(ZoomListener listener) {
		if (mPinchZoom != null) {
			mPinchZoom.addZoomListener(listener);
		}
	}

	/**
	 * Removes a zoom listener. 移除缩放监听
	 * 
	 * @param listener
	 *            zoom listener
	 */
	public void removeZoomListener(ZoomListener listener) {
		if (mPinchZoom != null) {
			mPinchZoom.removeZoomListener(listener);
		}
	}

	/**
	 * Adds a new pan listener. 添加盘监听
	 * 
	 * @param listener
	 *            pan listener
	 */
	public void addPanListener(PanListener listener) {
		if (mPan != null) {
			mPan.addPanListener(listener);
		}
	}

	/**
	 * Removes a pan listener. 移除盘监听
	 * 
	 * @param listener
	 *            pan listener
	 */
	public void removePanListener(PanListener listener) {
		if (mPan != null) {
			mPan.removePanListener(listener);
		}
	}
}