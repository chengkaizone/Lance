package org.lance.listener;

import android.view.View;
import android.view.ViewGroup;

/**
 * 用于拖拉ListView的监听器
 * 
 * @author lance
 * 
 */
public interface OnDragListViewListener {
	public void drag(int from, int to);

	public void drop(int from, int to);

	public void remove(int which);
	/**
	 * 通过空间id返回空间引用
	 * @param viewGroup
	 * @return
	 */
	public View getControlViewFromId(ViewGroup viewGroup);
}
