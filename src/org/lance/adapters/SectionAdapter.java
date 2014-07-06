package org.lance.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import org.lance.main.R;
import org.lance.util.SortService;

public class SectionAdapter extends BaseAdapter implements SectionIndexer {
	private List<String> data;
	private Context context;
	private LayoutInflater inflater;
	private final int NUM = 35;

	public SectionAdapter(Context context, List<String> data) {
		this.context = context;
		this.data = SortService.sortByChar(data);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
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
			convertView = inflater.inflate(R.layout.listview_row, null);
			holder.header = (LinearLayout) convertView
					.findViewById(R.id.section);
			holder.text = (TextView) convertView.findViewById(R.id.textView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		patchView(holder.header, holder.text, position);
		return convertView;
	}

	private void patchView(LinearLayout header, TextView text, int position) {
		String label = data.get(position);
		char firstChar = label.toUpperCase().charAt(0);
		if (position == 0) {
			setSection(header, label);
		} else {
			String preLabel = data.get(position - 1);
			char preFirstChar = preLabel.toUpperCase().charAt(0);
			// 如果前一项和后一项的起始字母不同则添加头部
			if (firstChar != preFirstChar) {
				setSection(header, label);
			} else {
				// 如果不同则隐藏头部
				header.setVisibility(View.GONE);
			}
		}
		text.setText(label);
	}

	// 为字符串分配样式
	private void setSection(LinearLayout header, String label) {
		TextView text = new TextView(context);
		header.setBackgroundColor(0xffaabbcc);
		text.setTextColor(Color.WHITE);
		text.setText(label.substring(0, 1).toUpperCase());
		text.setTextSize(20);
		text.setPadding(5, 0, 0, 0);
		text.setGravity(Gravity.CENTER_VERTICAL);
		text.setEnabled(false);
		header.addView(text);
	}

	@Override
	public int getPositionForSection(int section) {
		// System.out.println("section--->" + section);
		if (section == NUM) {
			return 0;
		}
		for (int i = 0; i < data.size(); i++) {
			String l = data.get(i);
			char firstChar = l.toUpperCase().charAt(0);
			if (firstChar == section) {
				// System.out.println("firstChar--->" + firstChar);
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	private class Holder {
		LinearLayout header;
		TextView text;
	}
}
