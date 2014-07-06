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
		group.add("����");
		group.add("����");
		data.add("����");
		data.add("����");
		data.add("����");
		data.add("����");
		data.add("����");
		data.add("�Ƽ�");
		data.add("����");
		data.add("����");
		data.add("��Ʊ");
		data.add("CBA");
		data.add("��Ϸ");
		data.add("���");
		data.add("�ֻ�");
		data.add("Ů��");
		data.add("̽��");
		data.add("�ƾ�");
		data.add("����");
		data.add("����");
		data.add("����");
		data.add("����");
		data.add("����");
		data.add("NBA");
		data.add("Ӣ��");
		data.add("����");
		data.add("���");
		data.add("�¼�");
		data.add("�г�");
		data.add("ʱ��");
		data.add("����");
		data.add("���");
		data.add("����");
		data.add("����");
		data.add("��Ӱ");
		data.add("����");
		data.add("����");
		return data;
	}
}
