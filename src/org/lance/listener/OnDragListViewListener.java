package org.lance.listener;

import android.view.View;
import android.view.ViewGroup;

/**
 * ��������ListView�ļ�����
 * 
 * @author lance
 * 
 */
public interface OnDragListViewListener {
	public void drag(int from, int to);

	public void drop(int from, int to);

	public void remove(int which);
	/**
	 * ͨ���ռ�id���ؿռ�����
	 * @param viewGroup
	 * @return
	 */
	public View getControlViewFromId(ViewGroup viewGroup);
}
