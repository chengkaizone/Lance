package org.lance.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

/**
 * µ•¡–  ≈‰
 * 
 * @author lance
 * 
 */
public class SingleAdapter extends BaseAdapter implements Filterable {
	private List<String> data;
	private LayoutInflater inflater;
	private Filter mFilter;
	private int layout;
	private int resId;

	public SingleAdapter(Context context, int layout, int resId,
			List<String> data) {
		this.layout = layout;
		this.resId = resId;
		inflater = LayoutInflater.from(context);
		this.data = data;
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
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(layout, null);
			if (convertView instanceof TextView) {
				holder.text = (TextView) convertView;
			} else {
				holder.text = (TextView) convertView.findViewById(resId);
			}
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.text.setText(data.get(position));
		return convertView;
	}

	public void update(List<String> data) {
		this.data = data;
		this.notifyDataSetChanged();
	}

	private class Holder {
		TextView text;
	}

	public void setFilter(Filter filter) {

	}

	@Override
	public Filter getFilter() {
		return mFilter;
	}
}
