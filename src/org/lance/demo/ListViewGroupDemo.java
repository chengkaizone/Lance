package org.lance.demo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import org.lance.adapters.GroupListAdapter;
import org.lance.main.R;

public class ListViewGroupDemo extends BaseActivity {

	private GroupListAdapter adapter = null;
	private ListView listView = null;
	private List<String> list = new ArrayList<String>();
	private List<String> listTag = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_list_activity);
		setData();
		adapter = new GroupListAdapter(this, list, listTag);
		listView = (ListView) findViewById(R.id.group_list);
		listView.setAdapter(adapter);
	}

	public void setData() {
		list.add("A");
		listTag.add("A");
		for (int i = 0; i < 3; i++) {
			list.add("阿凡达" + i);
		}
		list.add("B");
		listTag.add("B");
		for (int i = 0; i < 3; i++) {
			list.add("比特风暴" + i);
		}
		list.add("C");
		listTag.add("C");
		for (int i = 0; i < 30; i++) {
			list.add("查理风云" + i);
		}
	}
}