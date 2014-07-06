package org.lance.chartengine.client;

import org.lance.chartengine.chart.AbstractChart;
import org.lance.chartengine.chart.RoundChart;
import org.lance.chartengine.chart.XYChart;
import org.lance.chartengine.renderer.DefaultRenderer;
import org.lance.chartengine.tool.Pan;
import org.lance.chartengine.tool.PanListener;
import org.lance.chartengine.tool.ZoomListener;

import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * 一个为老平台设计的处理器
 * 
 * @author lance
 * 
 */
public class TouchHandlerOld implements ITouchHandler {
	/** The chart renderer. */
	private DefaultRenderer mRenderer;
	/** The old x coordinate. */
	private float oldX;
	/** The old y coordinate. */
	private float oldY;
	/** The zoom buttons rectangle. */
	private RectF zoomR = new RectF();
	/** The pan tool. */
	private Pan mPan;
	/** The graphical view. */
	private GraphicalView graphicalView;

	/**
	 * Creates an implementation of the old version of the touch handler.
	 * 创建一个实现了老版本的触摸处理器
	 * 
	 * @param view
	 *            the graphical view
	 * @param chart
	 *            the chart to be drawn
	 */
	public TouchHandlerOld(GraphicalView view, AbstractChart chart) {
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
	}

	/**
	 * 处理触摸事件
	 */
	public boolean handleTouch(MotionEvent event) {
		int action = event.getAction();
		if (mRenderer != null && action == MotionEvent.ACTION_MOVE) {
			if (oldX >= 0 || oldY >= 0) {
				float newX = event.getX();
				float newY = event.getY();
				if (mRenderer.isPanEnabled()) {
					mPan.apply(oldX, oldY, newX, newY);
				}
				oldX = newX;
				oldY = newY;
				graphicalView.repaint();
				return true;
			}
		} else if (action == MotionEvent.ACTION_DOWN) {
			oldX = event.getX();
			oldY = event.getY();
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
		} else if (action == MotionEvent.ACTION_UP) {
			oldX = 0;
			oldY = 0;
		}
		return !mRenderer.isClickEnabled();
	}

	/**
	 * Adds a new zoom listener. 添加缩放监听
	 * 
	 * @param listener
	 *            zoom listener
	 */
	public void addZoomListener(ZoomListener listener) {
	}

	/**
	 * Removes a zoom listener. 移除缩放监听
	 * 
	 * @param listener
	 *            zoom listener
	 */
	public void removeZoomListener(ZoomListener listener) {
	}

	/**
	 * Adds a new pan listener. 添加监听
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
	 * Removes a pan listener. 移除监听
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