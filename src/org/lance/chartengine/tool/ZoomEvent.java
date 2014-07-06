package org.lance.chartengine.tool;

/**
 * �����¼�
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
	 * Builds the zoom event. ʹ�÷Ŵ�,��С,���ű��ʴ��������¼�
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
	 * Returns the zoom type. ������������
	 * 
	 * @return true if zoom in, false otherwise
	 */
	public boolean isZoomIn() {
		return mZoomIn;
	}

	/**
	 * Returns the zoom rate. �������ű���
	 * 
	 * @return the zoom rate
	 */
	public float getZoomRate() {
		return mZoomRate;
	}
}
