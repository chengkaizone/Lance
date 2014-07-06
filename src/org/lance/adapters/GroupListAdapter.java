package org.lance.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.lance.main.R;

public class GroupListAdapter extends ArrayAdapter<String> {
	private LayoutInflater inflater;
	private List<String> data = null;

	public GroupListAdapter(Context context, List<String> objects,
			List<String> tags) {
		super(context, 0, objects);
		inflater = LayoutInflater.from(context);
		this.data = tags;
	}

	@Override
	public boolean isEnabled(int position) {
		if (data.contains(getItem(position))) {
			return false;
		}
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (data.contains(getItem(position))) {
			convertView = inflater.inflate(R.layout.group_list_item_tag, null);
		} else {
			convertView = inflater.inflate(R.layout.group_list_item, null);
		}
		TextView text = (TextView) convertView
				.findViewById(R.id.group_list_item_text);
		text.setText(getItem(position));
		return convertView;
	}

}