package org.lance.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.lance.listener.OnDragListViewListener;

/**
 * 数组适配
 * 
 * @author lance
 * 
 */
public class CustomGroupAdapter extends ArrayAdapter<String> implements
	OnDragListViewListener {
	private LayoutInflater inflater;
	private List<String> group = null;
	private List<String> data = null;
	private int glayId;
	private int gtId;
	
	private int clayId;
	private int ctId;
	private int controlId;

	public CustomGroupAdapter(Context context, int glayId, int gtId,
			int clayId, int ctId,int controlId, List<String> data, List<String> group) {
		super(context, 0, data);
		this.glayId = glayId;
		this.gtId = gtId;
		this.clayId = clayId;
		this.ctId = ctId;
		this.controlId=controlId;
		this.data = data;
		this.group = group;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public boolean isEnabled(int position) {
		if (group.contains(getItem(position))) {
			return false;
		}
		return true;
	}

	public int getEnablePosition() {
		for (int i = 0; i < data.size(); i++) {
			if (!group.contains(getItem(i))) {
				return i;
			}
		}
		return 0;
	}

	private boolean isGroup(int position) {
		String temp = data.get(position);
		if (group.contains(temp)) {
			return true;
		}
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (isGroup(position)) {
			// 复用主数据
			return getGroupView(position, convertView);
		}
		return getChildView(position, convertView);
	}

	public View getChildView(int pos, View childView) {
		ChildHolder holder = null;
		if ((childView != null) && (childView.getTag() instanceof ChildHolder)) {
			holder = (ChildHolder) childView.getTag();
		} else {
			holder = new ChildHolder();
			childView = inflater.inflate(clayId, null);
			if (childView instanceof TextView) {
				holder.child = (TextView) childView;
			} else {
				holder.child = (TextView) childView.findViewById(ctId);
			}
			childView.setTag(holder);
		}
		holder.child.setText(data.get(pos));
		return childView;
	}

	public View getGroupView(int position, View groupView) {
		GroupHolder holder = null;
		if ((groupView != null) && (groupView.getTag() instanceof GroupHolder)) {
			holder = (GroupHolder) groupView.getTag();
		} else {
			holder = new GroupHolder();
			groupView = inflater.inflate(glayId, null);
			if (groupView instanceof TextView) {
				holder.group = (TextView) groupView;
			} else {
				holder.group = (TextView) groupView.findViewById(gtId);
			}
			groupView.setTag(holder);
		}
		holder.group.setText(data.get(position));
		return groupView;
	}
	@Override
	public void remove(int which) {
		remove(getItem(which));
	}

	@Override
	public void drop(int from, int to) {
		String item =getItem(from);
		remove(item);
		insert(item, to);
	}

	@Override
	public void drag(int from, int to) {
		// 不用实现
	}

	private class GroupHolder {
		TextView group;
	}

	private class ChildHolder {
		TextView child;
	}

	@Override
	public View getControlViewFromId(ViewGroup viewGroup) {
		return viewGroup.findViewById(controlId);
	}

}
