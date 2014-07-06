package org.lance.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 通用分组
 * 
 * @author lance
 * 
 */
public class GroupAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<String> data;
	private List<String> group;
	private int glayId;
	private int gtId;
	private int clayId;
	private int ctId;

	public GroupAdapter(Context context, int glayId, int gtId, int clayId,
			int ctId, List<String> data, List<String> group) {
		this.glayId = glayId;
		this.gtId = gtId;
		this.clayId = clayId;
		this.ctId = ctId;
		this.data = data;
		this.group = group;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public String getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
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

	private boolean isGroup(int position) {
		String temp = data.get(position);
		if (group.contains(temp)) {
			return true;
		}
		return false;
	}

	public boolean isEnabled(int position) {
		if (!isGroup(position)) {
			return true;
		}
		return false;
	}

	public int getEnablePosition() {
		for (int i = 0; i < data.size(); i++) {
			if (!group.contains(getItem(i))) {
				return i;
			}
		}
		return 0;
	}

	public void remove(int position) {
		this.data.remove(position);
	}

	public void insert(String item, int to) {
		this.data.add(to, item);
	}

	public int getChildrenCount(int position) {
		// 采用某种算法计算
		return data.size() - group.size();
	}

	public ItemPosition getRealPosition(int position) {
		return null;
	}

	public static class ItemPosition {
		public int group;
		public int pos = -1;
	}

	private class GroupHolder {
		TextView group;
	}

	private class ChildHolder {
		TextView child;
	}
}
