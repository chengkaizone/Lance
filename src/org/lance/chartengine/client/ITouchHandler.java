package org.lance.chartengine.client;

import org.lance.chartengine.tool.PanListener;
import org.lance.chartengine.tool.ZoomListener;

import android.view.MotionEvent;

/**
 * �����������ӿ�
 * 
 * @author lance
 * 
 */
public interface ITouchHandler {
	/**
	 * Handles the touch event. �������¼�
	 * 
	 * @param event
	 *            the touch event
	 * @return true if the event was handled
	 */
	boolean handleTouch(MotionEvent event);

	/**
	 * Adds a new zoom listener. ������ż���
	 * 
	 * @param listener
	 *            zoom listener
	 */
	void addZoomListener(ZoomListener listener);

	/**
	 * Removes a zoom listener. �Ƴ����ż���
	 * 
	 * @param listener
	 *            zoom listener
	 */
	void removeZoomListener(ZoomListener listener);

	/**
	 * Adds a new pan listener. ����̼���
	 * 
	 * @param listener
	 *            pan listener
	 */
	void addPanListener(PanListener listener);

	/**
	 * Removes a pan listener. �Ƴ��̼���
	 * 
	 * @param listener
	 *            pan listener
	 */
	void removePanListener(PanListener listener);

}