package org.lance.temp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.lance.adapters.HeadGalleryAdapter;
import org.lance.entity.NewsInfo;
import org.lance.main.R;
import org.lance.widget.Indicator;
import org.lance.widget.SingleGallery;

;

/**
 * δ���ͼƬָʾ��--�Զ���֡������
 * 
 * @author lance
 * 
 */
public class HeadGallery extends FrameLayout implements OnItemSelectedListener,
		OnItemClickListener {
	private SingleGallery mGallery;
	private TextView tag;
	private TextView title;
	private HeadGalleryAdapter mAdapter;
	private Indicator mIndicator;

	public HeadGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public HeadGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HeadGallery(Context context) {
		super(context);
	}

	// ��д��������
	public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
		this.mGallery.setOnItemClickListener(listener);
	}

	// �����Ƿ��Զ�����
	public void startAutoScroll(boolean bool) {
		if (this.mGallery != null) {
			this.mGallery.startAutoScroll(bool);
		}
	}

	@Override
	// ��������xml�ļ�ʱ����
	protected void onFinishInflate() {
		// �˷�����Ҫһ����Դ�ļ�
		mGallery = (SingleGallery) findViewById(R.id.head_mygallery);// ��Ҫһ����Դ�ļ�
		mIndicator = (Indicator) findViewById(R.id.head_indicator);
		mGallery.setAutoTime(0);
		mGallery.setOnItemSelectedListener(this);
		mGallery.setOnItemClickListener(this);
		tag = (TextView) findViewById(R.id.head_text_tag);
		title = (TextView) findViewById(R.id.head_text_title);
		super.onFinishInflate();
	}

	// ���з���
	public SingleGallery getmGallery() {
		return mGallery;
	}

	public void setmGallery(SingleGallery mGallery) {
		this.mGallery = mGallery;
	}

	// �������������ݸ��Զ��廭��
	public void setAdapter(HeadGalleryAdapter adapter) {
		if (adapter != null) {
			this.mGallery.setAdapter(adapter);
			this.mGallery.setSelection(adapter.getInitLoc(0));
			this.mAdapter = adapter;
		}
	}

	@Override
	// �ص����ര�ڿɼ��Ըı�ʱ�Ĵ���
	protected void onWindowVisibilityChanged(int loc) {
		super.onWindowVisibilityChanged(loc);
		if (((loc == 0) && (((isShown()) || (this.mGallery == null) || (this.mAdapter == null))))
				|| (this.mAdapter.getCount() <= 0)) {
			return;
		}
		this.mGallery.setSelection(this.mAdapter.getInitLoc(this.mGallery
				.getSelectedItemPosition()));
	}

	@Override
	// �ص����봰��
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if ((this.mGallery == null) || (this.mAdapter == null)
				|| (this.mAdapter.getCount() <= 0)) {
			return;
		}
		this.mGallery.setSelection(this.mAdapter.getInitLoc(this.mGallery
				.getSelectedItemPosition()));
	}

	@Override
	// ��д��ѡ�ķ���
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (mAdapter != null) {
			NewsInfo info = mAdapter.getItem(position);
			if (!info.getTag().equals("")) {
				this.tag.setText(info.getTag());
				this.tag.setBackgroundResource(R.drawable.topcomment_column);
			}
			this.title.setText(info.getTitle());
			System.out.println(info.getTag() + "---" + info.getTitle());
		}
		if (mIndicator != null) {
			mIndicator.desPoint(position);
		}
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// ������
	}

	@Override
	// ��дѡ�����ʱ�ص��˷���
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		System.out.println("ItemClickListener--->" + position);
		// this.getContext().startActivity(
		// new Intent(getContext(), DetailPage.class));
	}

	// ����ָʾ��
	public void setIndicator(Indicator indicator) {
		this.mIndicator = indicator;
	}

	public Indicator getIndicator() {
		return mIndicator;
	}
}
