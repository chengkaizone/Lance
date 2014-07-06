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

	@Override
	public void onClick(View v) {
		boolean  bool=mSelectAdapter.isFine();
		if(bool){
			switchButton.setText("效果模式");
		}else{
			switchButton.setText("普通模式");
		}
		mSelectAdapter.setFine(!bool);
		
	}
}
