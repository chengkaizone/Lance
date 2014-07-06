package org.lance.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.lance.main.R;
import org.lance.widget.SortableListView.OnDragListener;

public class SimpleSortAdapter extends BaseAdapter implements OnDragListener {
	private String empty = "";
	private String destString;
	private Context mContext;
	private LayoutInflater mInflater;
	private boolean isFine;
	private ArrayList<String> data = new ArrayList<String>();

	public SimpleSortAdapter(Context context, ArrayList<String> data) {
		this.mContext = context;
		this.data = data;
		mInflater = LayoutInflater.from(mContext);
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

	public boolean isFine() {
		return isFine;
	}

	public void setFine(boolean isFine) {
		this.isFine = isFine;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.simple_sort_item, null);
			holder.title = (TextView) convertView
					.findViewById(R.id.list_item_title);
			holder.drag = (ImageView) convertView
					.findViewById(R.id.list_item_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String title = data.get(position);
		if (isFine) {
			if (empty.equals(title)) {
				holder.drag.setImageDrawable(new ColorDrawable(
						Color.TRANSPARENT));
			} else {
				holder.drag.setImageResource(R.drawable.custom_selector);
			}
		}
		holder.title.setText(title);
		return convertView;
	}

	public class ViewHolder {
		TextView title;
		ImageView drag;
	}

	@Override
	public void drag(int from, int dest, boolean isUp) {
		if (isFine) {
			String itemStr = getItem(dest);
			data.remove(getItem(from));
			data.remove(itemStr);
			if (isUp) {
				data.add(dest, empty);
				data.add(from, itemStr);
			} else {
				data.add(from, itemStr);
				data.add(dest, empty);
			}
			this.notifyDataSetChanged();
		}
	}

	@Override
	public void drop(int prePosition, int destPosition) {
		if (isFine) {
			String dragStr = getItem(destPosition);
			data.remove(dragStr);
			data.add(destPosition, destString);
		} else {
			String item = getItem(prePosition);
			data.remove(item);
			data.add(destPosition, item);
		}
		this.notifyDataSetChanged();
	}

	@Override
	// 此处需要通过技巧将item组视图设置为透明
	public void down(int downPosition) {
		destString = getItem(downPosition);
	}

	@Override
	public View getControlViewFromId(ViewGroup viewGroup) {
		return viewGroup.findViewById(R.id.list_item_image);
	}

}
