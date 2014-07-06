package org.lance.demo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import org.lance.adapter.CustomGroupAdapter;
import org.lance.main.R;
import org.lance.widget.SortableListView;
import org.lance.widget.GroupDragableListView;

public class CustomColumnDemo extends BaseActivity {
	private GroupDragableListView listView;
	private CustomGroupAdapter adapter;
	private ArrayList<String> group;
	private ArrayList<String> data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_main);
		listView = (GroupDragableListView) findViewById(R.id.custom_drag);
		initData();
		adapter = new CustomGroupAdapter(this, R.layout.group_list_item_tag,
				R.id.group_list_item_text, R.layout.custom_column_new_item,
				R.id.group_list_item_text,R.id.custom_column_image, data, group);
		listView.setAdapter(adapter);
		listView.setGroupTitle(group);
		listView.setDragListViewListener(adapter);
	}

	private List<String> initData() {
		data = new ArrayList<String>();
		group = new ArrayList<String>();
		group.add("导航");
		group.add("更多");
		data.add("导航");
		data.add("国内");
		data.add("体育");
		data.add("国际");
		data.add("娱乐");
		data.add("科技");
		data.add("军事");
		data.add("更多");
		data.add("股票");
		data.add("CBA");
		data.add("游戏");
		data.add("社会");
		data.add("手机");
		data.add("女人");
		data.add("探索");
		data.add("财经");
		data.add("数码");
		data.add("足球");
		data.add("足球");
		data.add("足球");
		data.add("旅游");
		data.add("NBA");
		data.add("英超");
		data.add("西甲");
		data.add("意甲");
		data.add("德甲");
		data.add("中超");
		data.add("时尚");
		data.add("美容");
		data.add("情感");
		data.add("房产");
		data.add("明星");
		data.add("电影");
		data.add("电视");
		data.add("音乐");
		return data;
	}
}
