package org.lance.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.lance.main.R;

public class CitySearchAdapter extends BaseAdapter implements Filterable {
	private Filter mFilter;
	private List<String> mFilterList = new ArrayList<String>();
	private LayoutInflater mInflater;

	public CitySearchAdapter(Context paramContext, Filter paramFilter) {
		this.mInflater = LayoutInflater.from(paramContext);
		this.mFilter = paramFilter;
	}

	public int getCount() {
		return this.mFilterList.size();
	}

	public Filter getFilter() {
		return this.mFilter;
	}

	public List<String> getFilterList() {
		return this.mFilterList;
	}

	public Object getItem(int position) {
		return this.mFilterList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup paramViewGroup) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = this.mInflater.inflate(R.layout.more_list_item, null);
			holder.title = ((TextView) convertView
					.findViewById(R.id.column_title));
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.title.setText("");
		return convertView;
	}

	private class Holder {
		TextView title;

	}
}