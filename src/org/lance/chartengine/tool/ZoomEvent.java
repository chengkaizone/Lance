package org.lance.chartengine.tool;

/**
 * 缩放事件
 * 
 * @author lance
 * 
 */
public class ZoomEvent {
	/** A flag to be used to know if this is a zoom in or out. */
	private boolean mZoomIn;
	/** The zoom rate. */
	private float mZoomRate;

	/**
	 * Builds the zoom event. 使用放大,放小,缩放比率创建缩放事件
	 * 
	 * @param in
	 *            zoom in or out
	 * @param rate
	 *            the zoom rate
	 */
	public ZoomEvent(boolean in, float rate) {
		mZoomIn = in;
		mZoomRate = rate;
	}

	/**
	 * Returns the zoom type. 返回缩放类型
	 * 
	 * @return true if zoom in, false otherwise
	 */
	public boolean isZoomIn() {
		return mZoomIn;
	}

	/**
	 * Returns the zoom rate. 返回缩放比率
	 * 
	 * @return the zoom rate
	 */
	public float getZoomRate() {
		return mZoomRate;
	}
}
