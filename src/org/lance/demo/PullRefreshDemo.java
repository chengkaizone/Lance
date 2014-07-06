package org.lance.demo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import org.lance.adapters.HeadGalleryAdapter;
import org.lance.adapters.NewsAdapter;
import org.lance.entity.NewsInfo;
import org.lance.listener.OnRefreshListener;
import org.lance.main.R;
import org.lance.temp.HeadGallery;
import org.lance.widget.Indicator;
import org.lance.widget.PullRefreshListView;

public class PullRefreshDemo extends BaseActivity {
	private PullRefreshListView listView;
	private List<NewsInfo> news = new ArrayList<NewsInfo>();
	private LinkedList<String> descs = new LinkedList<String>();
	private LinkedList<String> details = new LinkedList<String>();
	private NewsAdapter newAdapter;
	private HeadGalleryAdapter gAdapter;
	private Indicator indicator;
	private View headView;
	private HeadGallery container;
	private int[] resDraw = { R.drawable.bg2, R.drawable.bg2, R.drawable.bg2 };
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				newAdapter = new NewsAdapter(PullRefreshDemo.this, descs,
						details);
				listView.setAdapter(newAdapter);
			}
		}
	};

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.refresh_layout);
		init();
	}

	private void init() {
		listView = (PullRefreshListView) this
				.findViewById(R.id.refresh_listview);
		listView.initHeader(R.layout.refreshing_view, R.id.indicator_arraw,
				R.id.indicator_text, R.id.refreshing_pb, R.id.refresh_time);
		listView.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new GetDataTask().execute();
			}
		});
		news = new ArrayList<NewsInfo>();
		for (int i = 0; i < resDraw.length; i++) {
			Bitmap b = BitmapFactory.decodeResource(getResources(), resDraw[i]);
			NewsInfo info = new NewsInfo();
			info.setBitmap(b);
			if (i == 1) {
				info.setTag("");
			} else {
				info.setTag("图集" + i);
			}
			info.setTitle("两会正在举行-->" + i);
			news.add(info);
		}
		for (int i = 1; i <= 1; i++) {
			descs.add("头条两会正在举行" + i);
			details.add("风刀霜结发上课防守打法能收到甲方" + i);
		}
		newAdapter = new NewsAdapter(PullRefreshDemo.this, descs, details);
		headView = LayoutInflater.from(PullRefreshDemo.this).inflate(
				R.layout.head_advertisment, null);
		container = (HeadGallery) headView
				.findViewById(R.id.head_advert_grally);
		indicator = container.getIndicator();
		indicator.setPoints(resDraw.length);

		indicator.init(R.drawable.ck_gray_icon, R.drawable.ck_red_icon);
		gAdapter = new HeadGalleryAdapter(this, news);
		container.setAdapter(gAdapter);

		listView.addHeaderView(headView);
		listView.setAdapter(newAdapter);
	}

	private class GetDataTask extends AsyncTask<Void, Void, LinkedList<String>> {
		@Override
		protected LinkedList<String> doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			return descs;
		}

		@Override
		protected void onPostExecute(LinkedList<String> result) {
			descs.addFirst("执行更新--->");
			details.addFirst("详情更新--->都是分开的计算房价的快速反击的快速反击的快");
			listView.onRefreshComplete();
			handler.sendEmptyMessage(0x123);
			super.onPostExecute(result);
		}
	}
}
