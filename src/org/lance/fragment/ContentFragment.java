package org.lance.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lance.main.R;

/**
 * ����Ƭ����������
 * 
 * @author lance
 * 
 */
public class ContentFragment extends Fragment {
	private int mIndex;

	// �������ֵ Ϊ�Ժ���Ϊ �����¼� �Ƿ񵥻��� ͬһ�� item ���ж�ʹ��
	public int getIndex() {
		return mIndex;
	}

	public ContentFragment(int index) {
		// ������ʾ����ֵ �Ա㴴����ʱ��ȡ��
		Bundle bundle = new Bundle();
		bundle.putInt("index", index);
		this.setArguments(bundle);
	}

	// �����Ƭ�ڲ����У��������һ���޲ι�����
	public ContentFragment() {
	}

	@Override
	// step 2 �ڵ��ù�����֮�����
	public void onCreate(Bundle savedInstanceState) {
		// ȡ������ֵ
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
