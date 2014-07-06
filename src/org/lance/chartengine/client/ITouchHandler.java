package org.lance.chartengine.client;

import org.lance.chartengine.tool.PanListener;
import org.lance.chartengine.tool.ZoomListener;

import android.view.MotionEvent;

/**
 * ´¥Ãş´¦ÀíÆ÷½Ó¿Ú
 * 
 * @author lance
 * 
 */
public interface ITouchHandler {
	/**
	 * Handles the touch event. ´¦Àí´¥ÃşÊÂ¼ş
	 * 
	 * @param event
	 *            the touch event
	 * @return true if the event was handled
	 */
	boolean handleTouch(MotionEvent event);

	/**
	 * Adds a new zoom listener. Ìí¼ÓËõ·Å¼àÌı
	 * 
	 * @param listener
	 *            zoom listener
	 */
	void addZoomListener(ZoomListener listener);

	/**
	 * Removes a zoom listener. ÒÆ³ıËõ·Å¼àÌı
	 * 
	 * @param listener
	 *            zoom listener
	 */
	void removeZoomListener(ZoomListener listener);

	/**
	 * Adds a new pan listener. Ìí¼ÓÅÌ¼àÌı
	 * 
	 * @param listener
	 *            pan listener
	 */
	void addPanListener(PanListener listener);

	/**
	 * Removes a pan listener. ÒÆ³ıÅÌ¼àÌı
	 * 
	 * @param listener
	 *            pan listener
	 */
	void removePanListener(PanListener listener);

}