package org.lance.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.lance.main.R;
import org.lance.main.RootClassDemo;

public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private static final int[] ids = { R.drawable.test1, R.drawable.test2,
			R.drawable.test3 };

	public ImageAdapter(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	// 如果只朝着一个方向切换--必须返回无穷大
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.image_item, null);
			holder.img = ((ImageView) convertView.findViewById(R.id.imgView));
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.img.setImageResource(ids[position % ids.length]);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, RootClassDemo.class);
				Bundle bundle = new Bundle();
				bundle.putInt("image_id", ids[position % ids.length]);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	private class Holder {
		ImageView img;
	}
}
