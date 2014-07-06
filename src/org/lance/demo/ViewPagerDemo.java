package org.lance.demo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;

import org.lance.main.R;

public class ViewPagerDemo extends BaseActivity {
	private ViewPager viewPager;
	private List<View> pageViews;
	private ViewGroup main, group;
	private ImageView imageView;
	private ImageView[] imageViews;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		init();
	}

	private void init() {
		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		pageViews.add(inflater.inflate(R.layout.pager_item01, null));
		pageViews.add(inflater.inflate(R.layout.pager_item02, null));
		pageViews.add(inflater.inflate(R.layout.pager_item03, null));
		pageViews.add(inflater.inflate(R.layout.pager_item04, null));

		imageViews = new ImageView[pageViews.size()];
		main = (ViewGroup) inflater.inflate(R.layout.pager_main, null);
		group = (ViewGroup) main.findViewById(R.id.viewGroup);
		viewPager = (ViewPager) main.findViewById(R.id.guidePages);
		for (int i = 0; i < pageViews.size(); i++) {
			imageView = new ImageView(ViewPagerDemo.this);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(20, 0, 20, 0);
			imageViews[i] = imageView;
			if (i == 0) {
				// imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.page_indicator);
			}
			group.addView(imageViews[i]);
		}
		setContentView(main);
		// 能自动检测首饰
		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}

	private class GuidePageAdapter extends PagerAdapter {
		public int getCount() {
			return pageViews.size();
		}

		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		public void destroyItem(View view, int position, Object obj) {
			((ViewPager) view).removeView(pageViews.get(position));
		}

		public Object instantiateItem(View view, int position) {
			((ViewPager) view).addView(pageViews.get(position));
			return pageViews.get(position);
		}

		public void restoreState(Parcelable par, ClassLoader loader) {
		}

		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		public void finishUpdate(View arg0) {

		}
	}

	private class GuidePageChangeListener implements OnPageChangeListener {
		public void onPageScrollStateChanged(int state) {
			// System.out.println("state>>>" + state);
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// System.out.println(arg0 + " " + arg1 + " " + arg2);
		}

		public void onPageSelected(int position) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[position]
						.setBackgroundResource(R.drawable.page_indicator_focused);
				if (position != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.page_indicator);
				}
			}
		}
	}
}