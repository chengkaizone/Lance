package org.lance.demo;

import java.util.ArrayList;
import java.util.List;

import org.lance.adapter.CustomGroupAdapter;
import org.lance.main.R;
import org.lance.util.MoveBg;
import org.lance.widget.GroupDragableListView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class TabNewsActivity extends Activity {
	RelativeLayout layout;
	TextView tv_front;//��Ҫ�ƶ���View

	TextView tv_bar_news;
	TextView tv_bar_sport;
	TextView tv_bar_play;
	TextView tv_bar_finance;
	TextView tv_bar_science;
	TextView tv_bar_more;

	int avg_width = 0;// ���ڼ�¼ƽ��ÿ����ǩ�Ŀ�ȣ��ƶ���ʱ����Ҫ
	private GroupDragableListView listView;
	private CustomGroupAdapter adapter;
	private ArrayList<String> group;
	private ArrayList<String> data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_news);

		initViews();
	}

	private void initViews() {
		layout = (RelativeLayout) findViewById(R.id.layout_title_bar);
		listView = (GroupDragableListView) findViewById(R.id.custom_drag);
		initData();
		adapter = new CustomGroupAdapter(this, R.layout.group_list_item_tag,
				R.id.group_list_item_text, R.layout.custom_column_new_item,
				R.id.group_list_item_text,R.id.custom_column_image, data, group);
		listView.setAdapter(adapter);
		listView.setGroupTitle(group);
		listView.setDragListViewListener(adapter);
		tv_bar_news = (TextView) findViewById(R.id.tv_title_bar_news);
		tv_bar_sport = (TextView) findViewById(R.id.tv_title_bar_sport);
		tv_bar_play = (TextView) findViewById(R.id.tv_title_bar_play);
		tv_bar_finance = (TextView) findViewById(R.id.tv_title_bar_finance);
		tv_bar_science = (TextView) findViewById(R.id.tv_title_bar_science);
		tv_bar_more = (TextView) findViewById(R.id.tv_title_bar_more);

		tv_bar_news.setOnClickListener(onClickListener);
		tv_bar_sport.setOnClickListener(onClickListener);
		tv_bar_play.setOnClickListener(onClickListener);
		tv_bar_finance.setOnClickListener(onClickListener);
		tv_bar_science.setOnClickListener(onClickListener);
		tv_bar_more.setOnClickListener(onClickListener);

		tv_front = new TextView(this);
		tv_front.setBackgroundResource(R.drawable.slidebar);
		tv_front.setTextColor(Color.WHITE);
		tv_front.setText("ͷ��");
		tv_front.setGravity(Gravity.CENTER);
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		layout.addView(tv_front, param);

	}

	private OnClickListener onClickListener = new OnClickListener() {
		int startX;//�ƶ�����ʼλ��

		@Override
		public void onClick(View v) {
			avg_width = findViewById(R.id.layout).getWidth();
			switch (v.getId()) {
			case R.id.tv_title_bar_news:
				MoveBg.moveFrontBg(tv_front, startX, 0, 0, 0);
				startX = 0;
				tv_front.setText(R.string.title_news_category_tops);
				break;
			case R.id.tv_title_bar_sport:
				MoveBg.moveFrontBg(tv_front, startX, avg_width, 0, 0);
				startX = avg_width;
				tv_front.setText(R.string.title_news_category_sport);
				break;
			case R.id.tv_title_bar_play:
				MoveBg.moveFrontBg(tv_front, startX, avg_width * 2, 0, 0);
				startX = avg_width * 2;
				tv_front.setText(R.string.title_news_category_play);
				break;
			case R.id.tv_title_bar_finance:
				MoveBg.moveFrontBg(tv_front, startX, avg_width * 3, 0, 0);
				startX = avg_width * 3;
				tv_front.setText(R.string.title_news_category_finance);
				break;
			case R.id.tv_title_bar_science:
				MoveBg.moveFrontBg(tv_front, startX, avg_width * 4, 0, 0);
				startX = avg_width * 4;
				tv_front.setText(R.string.title_news_category_science);
				break;
			case R.id.tv_title_bar_more:
				MoveBg.moveFrontBg(tv_front, startX, avg_width * 5, 0, 0);
				startX = avg_width * 5;
				tv_front.setText(R.string.title_news_category_more);
				break;

			default:
				break;
			}

		}
	};
	
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
