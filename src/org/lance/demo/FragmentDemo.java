package org.lance.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.lance.fragment.ContentFragment;
import org.lance.main.R;

public class FragmentDemo extends FragmentActivity {
	private FragmentManager fManager;
	private FragmentTransaction fTransaction;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
	}

	/**
	 * 横竖屏的决断者
	 * 
	 * @param index
	 *            传入 item的索引值 进行具体信息显示
	 */
	public void showContent(int index) {
		fManager = this.getSupportFragmentManager();
		fTransaction = fManager.beginTransaction();
		ContentFragment content = (ContentFragment) fManager
				.findFragmentById(R.id.frag_content);
		// 此处如果list碎片点击了和前一次相同的一项；则碎片状态不为null；否则为null；
		if ((content != null && content.getIndex() != index) || content == null) {
			// rebuild
			content = new ContentFragment(index);
			// not back
			fTransaction.add(R.id.frag_content, content);
			fTransaction.commit();
			// 调用该方法主要适用于支持事物回滚；back键返回到上一个选项
			// fTransaction.addToBackStack("content");
			// fTransaction.replace(R.id.frag_content,content);
			// fTransaction.commit();
		}
	}

}
