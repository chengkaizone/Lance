package org.lance.demo;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import org.lance.main.R;
import org.lance.util.Processor;
import org.lance.util.Processor.HalfType;
import org.lance.widget.CornerListView;

public class RoundCornerDemo extends BaseActivity implements
		OnItemClickListener {
	private ImageView left, right, top, bottom;
	private CornerListView mListView = null;
	ArrayList<HashMap<String, String>> map_list1 = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.circle_main);
		initClip();
		getDataSource1();
		SimpleAdapter adapter1 = new SimpleAdapter(this, map_list1,
				R.layout.simple_list_item_1, new String[] { "item" },
				new int[] { R.id.item_title });
		mListView = (CornerListView) findViewById(R.id.list1);
		mListView.setAdapter(adapter1);
		mListView.setOnItemClickListener(this);
	}

	public ArrayList<HashMap<String, String>> getDataSource1() {
		map_list1 = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map1 = new HashMap<String, String>();
		HashMap<String, String> map2 = new HashMap<String, String>();
		map1.put("item", "…Ë÷√1");
		map2.put("item", "…Ë÷√2");
		map_list1.add(map1);
		map_list1.add(map2);
		return map_list1;
	}

	private void initClip() {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources()
				.getDrawable(R.drawable.btn_bg);
		Bitmap bitmap = bitmapDrawable.getBitmap();
		bitmap = Processor.createRepeater(bitmap, 150);

		bitmapDrawable = (BitmapDrawable) getResources().getDrawable(
				R.drawable.btn_bg_click);
		Bitmap bitmap_click = bitmapDrawable.getBitmap();
		bitmap_click = Processor.createRepeater(bitmap_click, 150);

		left = (ImageView) findViewById(R.id.circle_left);
		right = (ImageView) findViewById(R.id.circle_right);
		top = (ImageView) findViewById(R.id.circle_top);
		bottom = (ImageView) findViewById(R.id.circle_bottom);

		left.setImageBitmap(Processor.getRoundCornerImage(bitmap_click, 10,
				HalfType.LEFT));
		right.setImageBitmap(Processor.getRoundCornerImage(bitmap_click, 10,
				HalfType.RIGHT));
		top.setImageBitmap(Processor.getRoundCornerImage(bitmap_click, 10,
				HalfType.TOP));
		bottom.setImageBitmap(Processor.getRoundCornerImage(bitmap_click, 10,
				HalfType.NONE));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg2 == 0) {
			// System.out.println("0");
		} else {
			// System.out.println("1");
		}
	}
}