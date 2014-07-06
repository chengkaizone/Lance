package org.lance.chartengine.tool;

/**
 * 缩放监听器
 * 
 * @author lance
 * 
 */
public interface ZoomListener {

	/**
	 * Called when a zoom change is triggered. 执行缩放
	 * 
	 * @param e
	 *            the zoom event
	 */
	void zoomApplied(ZoomEvent e);

	/**
	 * Called when a zoom reset is done. 缩放重置
	 */
	void zoomReset();
}
