package org.lance.adapters;

import java.util.ArrayList;

import org.lance.main.R;
import org.lance.widget.DragGridView.OnGridViewItemListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DateAdapter extends BaseAdapter implements OnGridViewItemListener{

	private Context context;
	private ArrayList<String> lstDate;
	private TextView txtAge;

	public DateAdapter(Context mContext, ArrayList<String> list) {
		this.context = mContext;
		lstDate = list;
	}

	@Override
	public int getCount() {
		return lstDate.size();
	}

	@Override
	public Object getItem(int position) {
		return lstDate.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.drag_item, null);
		txtAge = (TextView) convertView.findViewById(R.id.txt_userage);
		if (lstDate.get(position) == null) {
			txtAge.setText("+");
			txtAge.setBackgroundResource(R.drawable.drag_red);
		} else if (lstDate.get(position).equals("none")) {
			txtAge.setText("");
			txtAge.setBackgroundDrawable(null);
		} else {
			txtAge.setText("Item" + lstDate.get(position));
		}
		return convertView;
	}

	@Override
	public void onExchange(int from, int to) {
		Object endObject = getItem(to);
		Object startObject = getItem(from);
		lstDate.add(from, (String) endObject);
		lstDate.remove(from + 1);
		lstDate.add(to, (String) startObject);
		lstDate.remove(to + 1);
	}

	@Override
	public void onExchangeFinish() {
		this.notifyDataSetChanged();
	}

}
