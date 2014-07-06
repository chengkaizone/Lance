package org.lance.demo;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.lance.adapters.SimpleSortAdapter;
import org.lance.main.R;
import org.lance.widget.SortableListView;

public class SimpleDragDemo extends Activity implements OnClickListener{
	private SortableListView dragList;
	private SimpleSortAdapter mSelectAdapter;
	private Button switchButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drag_custom_list);
		dragList = (SortableListView) findViewById(R.id.drag_list);
		switchButton=(Button)findViewById(R.id.drag_title_button);
		mSelectAdapter = new SimpleSortAdapter(this,initData());
		mSelectAdapter.setFine(true);
		dragList.setAdapter(mSelectAdapter);
		dragList.setOnDragListener(mSelectAdapter);
		switchButton.setOnClickListener(this);
	}

	private ArrayList<String> initData() {
		ArrayList<String> data=new ArrayList<String>();
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

	@Override
	public void onClick(View v) {
		boolean  bool=mSelectAdapter.isFine();
		if(bool){
			switchButton.setText("Ч��ģʽ");
		}else{
			switchButton.setText("��ͨģʽ");
		}
		mSelectAdapter.setFine(!bool);
		
	}
}
