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
	 * �������ľ�����
	 * 
	 * @param index
	 *            ���� item������ֵ ���о�����Ϣ��ʾ
	 */
	public void showContent(int index) {
		fManager = this.getSupportFragmentManager();
		fTransaction = fManager.beginTransaction();
		ContentFragment content = (ContentFragment) fManager
				.findFragmentById(R.id.frag_content);
		// �˴����list��Ƭ����˺�ǰһ����ͬ��һ�����Ƭ״̬��Ϊnull������Ϊnull��
		if ((content != null && content.getIndex() != index) || content == null) {
			// rebuild
			content = new ContentFragment(index);
			// not back
			fTransaction.add(R.id.frag_content, content);
			fTransaction.commit();
			// ���ø÷�����Ҫ������֧������ع���back�����ص���һ��ѡ��
			// fTransaction.addToBackStack("content");
			// fTransaction.replace(R.id.frag_content,content);
			// fTransaction.commit();
		}
	}

}
