package org.lance.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import org.lance.adapter.SingleAdapter;
import org.lance.demo.FragmentDemo;
import org.lance.main.R;
import org.lance.widget.SignableListView;

/**
 * 由主界面的碎片管理器管理
 * 
 * @author lance
 * 
 */
public class ListTitleFragment extends Fragment implements OnItemClickListener {

	private FragmentDemo mContext;
	private SingleAdapter sAdapter;
	private SignableListView list;
	private int currentPositon = 0;

	// step 1
	public ListTitleFragment() {
	}

	@Override
	// step 2
	public void onAttach(Activity activity) {
		mContext = (FragmentDemo) activity;
		super.onAttach(activity);
	}

	/**
	 * 创建时候 取出保存的状态值 作为当前值 ，一边显示相关页面
	 */
	@Override
	// step3
	public void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			currentPositon = savedInstanceState.getInt("currentPosition", 0);
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	// step4
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_list, container, false);
		list = (SignableListView) root.findViewById(R.id.frag_list);
		sAdapter = new SingleAdapter(mContext, R.layout.adapter_frag_item, 0,
				convert(Shakespeare.TITLES));
		list.setAdapter(sAdapter);
		list.setOnItemClickListener(this);
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setSelection(currentPositon);
		list.setCheckBackground(R.drawable.tab_front_bg);
		mContext.showContent(currentPositon);
		return root;
	}

	@Override
	// step 5
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 如果没有布局文件则调用该方法来获取组件
	}

	/**
	 * 当发生异常操作时 比如：竖屏变横屏时 保存当前状态 ，以便下一次启动时还能保持当前状态
	 */
	@Override
	// step 6
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt("currentPosition", currentPositon);
		super.onSaveInstanceState(outState);
	}

	private List<String> convert(String[] srr) {
		List<String> data = new ArrayList<String>();
		for (String str : srr) {
			data.add(str);
		}
		return data;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		currentPositon = position;// 把选中List的item的位置作为当前选中项
		mContext.showContent(currentPositon);
	}

	/**
	 * 为Fragment生命周期 最后一个 相当于 Activity中onDestory();
	 */
	@Override
	// last step
	public void onDetach() {
		super.onDetach();
		mContext = null;
	}
}
