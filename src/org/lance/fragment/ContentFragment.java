package org.lance.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lance.main.R;

/**
 * 在碎片管理界面调用
 * 
 * @author lance
 * 
 */
public class ContentFragment extends Fragment {
	private int mIndex;

	// 获得索引值 为以后作为 单击事件 是否单击了 同一个 item 做判断使用
	public int getIndex() {
		return mIndex;
	}

	public ContentFragment(int index) {
		// 传入显示索引值 以便创建的时候取出
		Bundle bundle = new Bundle();
		bundle.putInt("index", index);
		this.setArguments(bundle);
	}

	// 如果碎片在布局中；则必须有一个无参构造器
	public ContentFragment() {
	}

	@Override
	// step 2 在调用构造器之后调用
	public void onCreate(Bundle savedInstanceState) {
		// 取出索引值
		if (getArguments() != null) {
			mIndex = getArguments().getInt("index", 0);
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	// step 3
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.details_fragment, container,
				false);
		TextView text = (TextView) root.findViewById(R.id.tvContent);
		text.setText(Shakespeare.DIALOGUE[mIndex]);
		return root;
	}
}
