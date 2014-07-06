package org.lance.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;

import org.lance.adapter.GroupableAdapter;
import org.lance.main.R;
import org.lance.widget.GroupableListView;

public class QQMainDemo extends BaseActivity {
	private List<Map<String, String>> groups;
	private List<List<Map<String, String>>> childs;
	private GroupableListView listView;
	private GroupableAdapter gAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qq_main);
		listView = (GroupableListView) findViewById(R.id.home_expandableListView);
		// 初始化分组及子数据
		initData();
		listView.setHeaderView(R.layout.qq_group_header, R.id.groupIcon,
				R.id.groupto);
		// 初始化适配器
		gAdapter = new GroupableAdapter(this, listView, groups,
				R.layout.qq_group, new String[] { "g" },
				new int[] { R.id.groupto }, childs, R.layout.qq_child,
				new String[] { "c" }, new int[] { R.id.childto },
				R.layout.qq_group, R.id.groupIcon,
				R.drawable.group_unfold_arrow, R.drawable.group_fold_arrow);
		listView.setAdapter(gAdapter);
	}

	private void initData() {
		groups = new ArrayList<Map<String, String>>();
		childs = new ArrayList<List<Map<String, String>>>();

		for (int i = 1; i <= 10; i++) {
			Map<String, String> group = new HashMap<String, String>();
			group.put("g", "分组" + i);
			groups.add(group);
			List<Map<String, String>> child = new ArrayList<Map<String, String>>();
			for (int j = 0; j < 10; j++) {
				Map<String, String> childdata = new HashMap<String, String>();
				childdata.put("c", "用户" + i + "-" + j);
				child.add(childdata);
			}
			childs.add(child);
		}
	}
}