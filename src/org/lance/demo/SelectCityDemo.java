package org.lance.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;

import org.lance.adapter.GroupAdapter;
import org.lance.adapter.SingleAdapter;
import org.lance.db.CityDatabaseHelper;
import org.lance.entity.City;
import org.lance.listener.SectionListener;
import org.lance.main.R;
import org.lance.widget.SectionBar;

public class SelectCityDemo extends BaseActivity implements SectionListener {
	private GroupAdapter adapter;
	private SingleAdapter sAdapter;
	private ListView listView;
	private ListView searchList;
	private EditText edit;
	private SectionBar bar;
	private CityDatabaseHelper dbhelper;
	private List<String> data = new ArrayList<String>();
	private List<String> group = new ArrayList<String>();
	private List<String> filterData = new ArrayList<String>();
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x666:
				filterData = (List<String>) msg.obj;
				sAdapter = new SingleAdapter(SelectCityDemo.this,
						R.layout.more_list_item, R.id.column_title, filterData);
				searchList.setAdapter(sAdapter);
			}
		}
	};
	private Filter filter = new Filter() {
		// 执行过滤时将参数传递到该方法作为参数---调用filte方法时回调
		protected FilterResults performFiltering(CharSequence constraint) {
			List<String> list = filteResult((String) constraint);
			// System.out.println(list.size());
			FilterResults result = new FilterResults();
			result.values = list;
			return result;
		}

		// 过滤完后发布结果
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			Message msg = handler.obtainMessage(0x666);
			msg.obj = results.values;
			handler.sendMessage(msg);
		}
	};

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.select_city);
		dbhelper = new CityDatabaseHelper(this);
		edit = (EditText) findViewById(R.id.search_edit);
		listView = (ListView) findViewById(R.id.select_list);
		searchList = (ListView) findViewById(R.id.search_list);
		bar = (SectionBar) findViewById(R.id.sideBar);
		bar.initHintContainer(R.id.section_lay, R.id.section_text);
		bar.setSectionListener(this);
		edit.addTextChangedListener(editListener);
		sAdapter = new SingleAdapter(this, R.layout.more_list_item,
				R.id.column_title, filterData);
		searchList.setAdapter(sAdapter);
		initData();
		adapter = new GroupAdapter(this, R.layout.list_group_item, 0,
				R.layout.more_list_item, R.id.column_title, data, group);
		listView.setAdapter(adapter);
	}

	private void initData() {
		if (dbhelper.hasRecode()) {
			initList();
			return;
		}
		try {
			List<City> temp = new ArrayList<City>();
			InputStream is = this.getAssets().open("city_list.txt");
			InputStreamReader isr = new InputStreamReader(is, "gbk");
			BufferedReader reader = new BufferedReader(isr);
			String str = "";
			City city = null;
			while ((str = reader.readLine()) != null) {
				String[] srr = str.split("/");
				city = new City();
				city.setName(srr[1]);
				city.setProvince(srr[0]);
				city.setSpell(srr[2]);
				temp.add(city);
			}
			reader.close();
			dbhelper.initTable(temp);
			initList();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initList() {
		List<City> citys = dbhelper.getCitysForSpell();
		for (City c : citys) {
			String gstr = c.getSpell().substring(0, 1);
			if (!group.contains(gstr)) {
				group.add(gstr);
				data.add(gstr);
				data.add(c.getName());
			} else {
				data.add(c.getName());
			}
		}
	}

	private List<String> filteResult(String str) {
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).startsWith(str) && (!group.contains(str))) {
				values.add(data.get(i));
			}
		}
		return values;
	}

	private TextWatcher editListener = new TextWatcher() {
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void afterTextChanged(Editable s) {
			final String str = s.toString();
			// handler.removeMessages(0x666);
			if (!TextUtils.isEmpty(str)) {
				listView.setVisibility(View.GONE);
				bar.setVisibility(View.GONE);
				searchList.setVisibility(View.VISIBLE);
				new Thread() {
					public void run() {
						// 该方法执行之后取消之前未过滤的操作--添加一个新的过滤请求
						filter.filter(str);
					}
				}.start();
			} else {
				listView.setVisibility(View.VISIBLE);
				bar.setVisibility(View.VISIBLE);
				searchList.setVisibility(View.GONE);
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (listView.getVisibility() != View.VISIBLE) {
				listView.setVisibility(View.VISIBLE);
				searchList.setVisibility(View.GONE);
				edit.setText("");
			} else {
				finish();
			}
		}
		return true;
	}

	@Override
	public void doPosition(String str) {
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).equalsIgnoreCase(str)) {
				listView.setSelection(i);
				return;
			}
		}
	}
}
