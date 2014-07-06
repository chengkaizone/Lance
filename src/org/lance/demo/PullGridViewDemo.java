package org.lance.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SimpleAdapter;

import org.lance.listener.OnRefreshListener;
import org.lance.main.R;
import org.lance.widget.PullRefreshGridView;

public class PullGridViewDemo extends BaseActivity {
	private SimpleAdapter citySimpleAdapter;
	private PullRefreshGridView gridView;
	public LinearLayout head;
	private int i = 0;
	private List<HashMap<String, String>> cityList = new ArrayList<HashMap<String, String>>();
	// ���13������
	String[] areaName = new String[] { "�Ͼ�", "����", "����", "��ͨ", "����", "����",
			"����", "̩��", "�γ�", "��", "����", "���Ƹ�", "��Ǩ" };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pull_gridview_main);
		// ����ģ�� ���г�ʼ��
		for (int i = 0; i < areaName.length * 1; i++) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("id", String.valueOf(i));
			hashMap.put("name", areaName[i % 13].toString());
			cityList.add(hashMap);
		}
		gridView = (PullRefreshGridView) findViewById(R.id.order_form_grid);
		head = (LinearLayout) findViewById(R.id.head);
		LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		// ��ȡPullToRefreshGridView�����head����
		gridView.initHeader(R.layout.refreshing_view, R.id.indicator_arraw,
				R.id.indicator_text, R.id.refreshing_pb, R.id.refresh_time);
		head.addView(gridView.getHeadView(), p);
		citySimpleAdapter = new SimpleAdapter(this, cityList,
				R.layout.grid_itemlayout, new String[] { "name" },
				new int[] { R.id.txtRepDlgCity });
		gridView.setAdapter(citySimpleAdapter);
		gridView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				task();
			}
		});
	}

	private void task() {
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("id", String.valueOf(cityList.size()));
				hashMap.put("name", "����-->" + i++);
				cityList.add(0, hashMap);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				citySimpleAdapter.notifyDataSetChanged();
				gridView.onRefreshComplete();
			}
		}.execute();
	}
}