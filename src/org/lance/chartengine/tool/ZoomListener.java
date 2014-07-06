package org.lance.chartengine.tool;

/**
 * ���ż�����
 * 
 * @author lance
 * 
 */
public interface ZoomListener {

	/**
	 * Called when a zoom change is triggered. ִ������
	 * 
	 * @param e
	 *            the zoom event
	 */
	void zoomApplied(ZoomEvent e);

	/**
	 * Called when a zoom reset is done. ��������
	 */
	void zoomReset();
}
