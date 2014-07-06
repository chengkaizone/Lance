package org.lance.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.lance.main.R;

/**
 * 可搜索列表视图---未使用
 * 
 * @author lance
 * 
 */
public class SearchListView extends FrameLayout {
	private TextView mEmptyOrErrorText;
	private ListView mSearchHistoryView;
	private ListView mSearchResultView;

	public SearchListView(Context context) {
		super(context);
	}

	public SearchListView(Context context, AttributeSet attr) {
		super(context, attr);
	}

	public SearchListView(Context context, AttributeSet attr, int style) {
		super(context, attr, style);
	}

	private void initStatus(ListView listView) {
		listView.setVisibility(8);
		listView.setFadingEdgeLength(0);
		listView.setCacheColorHint(0);
		listView.setSelector(getResources().getDrawable(
				R.drawable.ck_divider_line));
		listView.setDividerHeight(0);
	}

	public ListView getSearchHistoryListView() {
		return this.mSearchHistoryView;
	}

	public ListView getSearchResultListView() {
		return this.mSearchResultView;
	}

	public boolean isSearchAdapterNULL() {
		if ((this.mSearchResultView.getAdapter() == null)
				|| (this.mSearchResultView.getAdapter().isEmpty())) {
			return true;
		}
		return false;
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		Context context = getContext();
		this.mSearchHistoryView = new ListView(context);
		this.mSearchResultView = new ListView(context);
		this.mEmptyOrErrorText = new TextView(context);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -2);
		addView(this.mSearchHistoryView, params);
		addView(this.mSearchResultView, params);
		params = new FrameLayout.LayoutParams(-2, -2);
		params.gravity = 1;
		params.setMargins(10, 10, 10, 10);
		addView(this.mEmptyOrErrorText, params);
		initStatus(this.mSearchHistoryView);
		initStatus(this.mSearchResultView);
		this.mEmptyOrErrorText.setTextSize(16.0F);
		this.mEmptyOrErrorText.setVisibility(8);
	}

	public void setHistoryOrSuggestListViewAdapter(BaseAdapter adapter) {
		this.mEmptyOrErrorText.setVisibility(8);
		this.mSearchResultView.setVisibility(8);
		this.mSearchHistoryView.setVisibility(0);
		this.mSearchHistoryView.setAdapter(adapter);
	}

	public void setNetworkInvalid() {
		this.mSearchHistoryView.setVisibility(8);
		this.mSearchResultView.setVisibility(8);
		this.mEmptyOrErrorText.setVisibility(0);
		this.mEmptyOrErrorText.setText("网络错误");
	}

	public void setNoResultSearch(String str) {
		this.mSearchHistoryView.setVisibility(8);
		this.mSearchResultView.setVisibility(8);
		this.mEmptyOrErrorText.setVisibility(0);
		if (str.length() > 10) {
			str = str.substring(0, 10);
		}
		TextView text = this.mEmptyOrErrorText;
		Resources res = getResources();
		Object[] arrayOfObject = new Object[1];
		arrayOfObject[0] = str;
		text.setText(res.getString(2131493075, arrayOfObject));
	}

	public void setSearchResultListViewAdapter(BaseAdapter paramBaseAdapter) {
		this.mEmptyOrErrorText.setVisibility(8);
		this.mSearchHistoryView.setVisibility(8);
		this.mSearchResultView.setVisibility(0);
		this.mSearchResultView.setAdapter(paramBaseAdapter);
	}

	public void showSearchResultView() {
		this.mSearchHistoryView.setVisibility(8);
		this.mSearchResultView.setVisibility(0);
		this.mEmptyOrErrorText.setVisibility(8);
	}
}