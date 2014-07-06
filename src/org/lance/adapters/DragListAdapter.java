package org.lance.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.lance.main.R;

public class DragListAdapter extends ArrayAdapter<String> {
	private Context context;
	private List<String> data;
	private LayoutInflater inflater;

	public DragListAdapter(Context context, int textViewResourceId,
			List<String> data) {
		super(context, textViewResourceId, data);
		this.context = context;
		this.data = data;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
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
			convertView = inflater.inflate(R.layout.custom_column_item, null);
			holder.title = (TextView) convertView
					.findViewById(R.id.custom_column_text);
			holder.image = (ImageView) convertView
					.findViewById(R.id.custom_column_image);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.title.setText(data.get(position));
		return convertView;
	}

	private class Holder {
		TextView title;
		ImageView image;
	}
}
