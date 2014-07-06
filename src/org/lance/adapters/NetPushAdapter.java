package org.lance.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import org.lance.entity.Project;
import org.lance.main.R;
import org.lance.widget.PushableListView;
import org.lance.widget.PushableListView.OnPushListener;
import static org.lance.widget.PushableListView.*;

public class NetPushAdapter extends BaseAdapter implements SectionIndexer,
		OnScrollListener, OnPushListener {

	private Context context;
	private List<Project> pros;
	private String[] sections;

	public NetPushAdapter(Context context, String[] groups, List<Project> pros) {
		this.context = context;
		this.sections = groups;
		this.pros = pros;
		System.out.println("竞技项目数：" + pros.size());
	}

	@Override
	public int getCount() {
		return pros.size();
	}

	@Override
	public Object getItem(int position) {
		return pros.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.adapter_netease, null);
			holder.title = (TextView) convertView
					.findViewById(R.id.netease_title);
			holder.time = (TextView) convertView
					.findViewById(R.id.netease_time);
			holder.desc = (TextView) convertView
					.findViewById(R.id.netease_desc);
			holder.china = (ImageView) convertView
					.findViewById(R.id.netease_china);
			holder.dir = (ImageView) convertView.findViewById(R.id.neteast_dir);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		Project pro = pros.get(position);
		holder.time.setText("16:40");
		holder.desc.setText(pro.getDesc());
		if (position % 3 == 0) {
			holder.china.setVisibility(View.VISIBLE);
		} else {
			holder.china.setVisibility(View.GONE);
		}

		String curGroup = pro.getGroupName();
		String pre = position >= 1 ? pros.get(position - 1).getGroupName() : "";
		if (!(pre.equals(curGroup))) {
			holder.title.setVisibility(View.VISIBLE);
			holder.title.setText(curGroup);
		} else {
			holder.title.setVisibility(View.GONE);
		}
		return convertView;
	}

	private class Holder {
		TextView title;
		TextView time;
		TextView desc;
		ImageView china;
		ImageView dir;
	}

	public int getTitleState(int position) {
		if (position < 0 || getCount() == 0) {
			return STATE_GONE;
		}
		int index = getSectionForPosition(position);
		if (index == -1 || index > sections.length) {
			return STATE_GONE;
		}
		int nextSectionPosition = getPositionForSection(index + 1);
		if (nextSectionPosition != -1 && position == nextSectionPosition - 1) {
			return STATE_PUSHED_UP;
		}
		return STATE_VISIBLE;
	}

	@Override
	// 根据组块索引计算出标题头在列表中第一次出现的位置
	public int getPositionForSection(int section) {
		String sec = sections[section];
		for (int i = 0; i < pros.size(); i++) {
			if (pros.get(i).getGroupName().equals(sec)) {
				return i;
			}
		}
		return 0;
	}

	@Override
	// 任何时候都会调用该方法
	public int getSectionForPosition(int position) {
		// 如果描述和标题头相同 返回位置
		String group = pros.get(position).getGroupName();
		for (int i = 0; i < sections.length; i++) {
			if (sections[i].equals(group)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object[] getSections() {
		return sections;
	}

	// 注意此处：如果标题头不是TextView 则需要获取相应的视图
	public void setTitleText(View mHeader, int firstVisiblePosition) {
		String title = pros.get(firstVisiblePosition).getGroupName();
		TextView sectionHeader = (TextView) mHeader;
		sectionHeader.setText(title);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (view instanceof PushableListView) {
			((PushableListView) view).titleLayout(firstVisibleItem);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void handleTitle(View title, int position) {
		setTitleText(title, position);
	}

	@Override
	public int getStateFromAdapter(int position) {
		return getTitleState(position);
	}

}