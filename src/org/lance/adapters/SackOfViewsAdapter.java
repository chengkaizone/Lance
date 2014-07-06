package org.lance.adapters;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SackOfViewsAdapter extends BaseAdapter {
	private List<View> views;

	public SackOfViewsAdapter(int count) {
		views = new ArrayList<View>();
	}

	public SackOfViewsAdapter(List<View> views) {
		this.views = views;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public Object getItem(int position) {
		return views.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View localView = this.views.get(position);
		if (localView == null) {
			localView = newView(position, parent);
			this.views.set(position, localView);
		}
		return localView;
	}

	protected View newView(int position, ViewGroup parent) {
		throw new RuntimeException("You must override newView()!");
	}

	public int getViewTypeCount() {
		return getCount();
	}

	public boolean isEnabled(int paramInt) {
		return false;
	}

	public int getItemViewType(int paramInt) {
		return paramInt;
	}
}
