package org.lance.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import org.lance.widget.GroupableListView;
import org.lance.widget.GroupableListView.GroupHeaderAdapter;

/**
 * 模拟QQ特效的分组适配器
 * 
 * @author lance
 */
public class GroupableAdapter extends SimpleExpandableListAdapter implements
		GroupHeaderAdapter {
	private GroupableListView listView;
	private Context context;
	private int groupLayout;
	private int groupIcon;
	private int resNormal;
	private int resPressed;
	private boolean hasState = false;
	private HashMap<Integer, Integer> groupStatusMap = new HashMap<Integer, Integer>();

	public GroupableAdapter(Context context, GroupableListView listView,
			List<? extends Map<String, ?>> groupData, int expandedGroupLayout,
			String[] groupFrom, int[] groupTo,
			List<? extends List<? extends Map<String, ?>>> childData,
			int childLayout, String[] childFrom, int[] childTo) {
		super(context, groupData, expandedGroupLayout, groupFrom, groupTo,
				childData, childLayout, childFrom, childTo);
		this.context = context;
		this.listView = listView;
	}

	public GroupableAdapter(Context context, GroupableListView listView,
			List<? extends Map<String, ?>> groupData, int expandedGroupLayout,
			String[] groupFrom, int[] groupTo,
			List<? extends List<? extends Map<String, ?>>> childData,
			int childLayout, String[] childFrom, int[] childTo,
			int groupLayout, int groupIcon, int normal, int pressed) {
		super(context, groupData, expandedGroupLayout, groupFrom, groupTo,
				childData, childLayout, childFrom, childTo);
		this.context = context;
		this.listView = listView;
		this.groupLayout = groupLayout;
		this.groupIcon = groupIcon;
		this.resNormal = normal;
		this.resPressed = pressed;
		hasState = true;
	}

	public void setGroupLayout(int groupLayout, int groupIcon, int normal,
			int pressed) {
		this.groupLayout = groupLayout;
		this.groupIcon = groupIcon;
		this.resNormal = normal;
		this.resPressed = pressed;
		hasState = true;
	}

	@Override
	public int getGroupHeaderState(int groupPosition, int childPosition) {
		final int childCount = getChildrenCount(groupPosition);
		if (childPosition == childCount - 1) {
			return PINNED_HEADER_PUSHED_UP;
		} else if (childPosition == -1
				&& !listView.isGroupExpanded(groupPosition)) {
			return PINNED_HEADER_GONE;
		} else {
			return PINNED_HEADER_VISIBLE;
		}
	}

	@Override
	public void configureGroupHeader(View header, TextView text,
			int groupPosition, int childPosition, int alpha) {
		Map<String, String> groupData = (Map<String, String>) this
				.getGroup(groupPosition);
		if (text != null) {
			text.setText(groupData.get("g"));
		}
	}

	@Override
	public void setGroupClickStatus(int groupPosition, int status) {
		groupStatusMap.put(groupPosition, status);
	}

	@Override
	public int getGroupClickStatus(int groupPosition) {
		if (groupStatusMap.containsKey(groupPosition)) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 0;
		}
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(context).inflate(groupLayout,
					null);
			holder.image = (ImageView) convertView.findViewById(groupIcon);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if (hasState) {
			if (isExpanded) {
				holder.image.setImageResource(resNormal);
			} else {
				holder.image.setImageResource(resPressed);
			}
		}
		return super.getGroupView(groupPosition, isExpanded, convertView,
				parent);
	}

	private class Holder {
		ImageView image;
	}
}