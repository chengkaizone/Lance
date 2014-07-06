package org.lance.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
/**
 * ���϶������ListView 
 * @author lance
 *
 */
public class SortableListView extends ListView {
	// ����ק�����ʵ����һ��ImageView
	private ImageView dragImageView;
	// ��ָ���µ�ʱ���б�����listview�е�λ��
	private int downPosition;
	// ���ڽ����б�����
	private int fromPosition;
	// ��ָ�϶���ʱ�򣬵�ǰ�϶������б��е�λ��
	private int destPosition;
	// �ڵ�ǰ�������е�λ��
	private int dragPoint;
	// ��ǰ��ͼ����Ļ�ľ���(����ֻʹ����y������)
	private int dragOffset;
	// windows���ڿ�����
	private WindowManager windowManager;
	// ���ڿ�����ק�����ʾ�Ĳ���
	private WindowManager.LayoutParams windowParams;
	//������ͼ��Ĭ�ϱ�����ɫ
	private int dragColor = Color.argb(55, 0xcc, 0xcc, 0xcc);
	private ViewGroup itemView;
	private int mEventY = 0;
	
	private OnDragListener onDragListener=new OnDragListener() {
		public void drop(int prePosition,int destPosition) {
		}
		public void down(int downPosition) {
		}
		public View getControlViewFromId(ViewGroup viewGroup) {
			return null;
		}
		public void drag(int dest, int from, boolean isUp) {
		}
	};

	public SortableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// ����touch�¼�����ʵ���Ǽ�һ�����
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			// �жϵ�ǰxyֵ �Ƿ���item�� ����� ���ظ�item��position ���� ���� INVALID_POSITION��-1��
			downPosition = pointToPosition(x, y);
			if (downPosition == AdapterView.INVALID_POSITION) {
				return super.onInterceptTouchEvent(ev);
			}
			fromPosition = downPosition;
			destPosition = downPosition;
			// ��ȡ��ǰ�����view
			itemView = (ViewGroup) getChildAt(downPosition
					- getFirstVisiblePosition());
			// �������-view���ϱ߽�
			dragPoint = y - itemView.getTop();
			// ������Ļ�е�y����-listview�е�y����,��ƫ����
			dragOffset = (int) (ev.getRawY() - y);
			View dragger=onDragListener.getControlViewFromId(itemView);
			if (dragger != null && x > dragger.getLeft()
					&& x < dragger.getRight()) {
				itemView.setDrawingCacheEnabled(true);
				Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());
				onDragListener.down(downPosition);
				startDrag(bm, y);
				return true;
			}
		}
		return false;
	}

	/**
	 * �����¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		if (dragImageView != null && downPosition != INVALID_POSITION) {
			switch (action) {
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				onDragListener.drop(downPosition,destPosition);
				stopDrag();
				mEventY = 0;
				itemView.destroyDrawingCache();
				break;
			case MotionEvent.ACTION_MOVE:
				int moveY = (int) ev.getY();
				mEventY = moveY;
				onDrag(moveY, (int) ev.getRawY());
				break;
			case MotionEvent.ACTION_DOWN:
				break;
			}
			return true;
		} else {
			return super.onTouchEvent(ev);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mEventY != 0) {
			if (mEventY != 0 && mEventY <= 0) {
				setSelectionFromTop(getFirstVisiblePosition(), getChildAt(0)
						.getTop() + 3);
			} else if (mEventY >= getHeight()) {
				setSelectionFromTop(getFirstVisiblePosition(), getChildAt(0)
						.getTop() - 3);
			}
		}
	}

	/**
	 * ׼���϶�����ʼ���϶����ͼ��
	 * @param bm
	 * @param y
	 */
	public void startDrag(Bitmap bm, int y) {
		stopDrag();
		windowParams = new WindowManager.LayoutParams();
		windowParams.gravity = Gravity.TOP;
		windowParams.x = 0;
		windowParams.y = y - dragPoint + dragOffset;
		windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		windowParams.format = PixelFormat.TRANSLUCENT;
		windowParams.windowAnimations = 0;

		ImageView imageView = new ImageView(getContext());
		imageView.setImageBitmap(bm);
		imageView.setBackgroundColor(dragColor);
		windowManager = (WindowManager) getContext()
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(imageView, windowParams);
		dragImageView = imageView;
	}

	/**
	 * ֹͣ�϶���ȥ���϶����ͷ��
	 */
	public void stopDrag() {
		if (dragImageView != null) {
			windowManager.removeView(dragImageView);
			dragImageView = null;
			itemView.setBackgroundColor(Color.TRANSPARENT);
		}
	}

	/**
	 * �϶�ִ�У���Move������ִ��
	 */
	public void onDrag(int y, int rawY) {
		// �����ϱ߽磬��Ϊ��Сֵλ��0
		if (y < 0) { 
			y = 0;
		} else if (y > getHeight()) {
			y = getHeight();
		}
		postInvalidate();
		if (dragImageView != null) {
			windowParams.alpha = 0.8f;
			windowParams.y = rawY - dragPoint;
			windowManager.updateViewLayout(dragImageView, windowParams);
		}
		// Ϊ�˱��⻬�����ָ��ߵ�ʱ�򣬷���-1������
		int itemPosition = pointToPosition(0, y);
		if (itemPosition != INVALID_POSITION) {
			destPosition = itemPosition;
			onDrop(y);
		}
	}

	// �����϶���λ�����б��з���
	public void onDrop(int y) {
		// ��������ƶ�
		if (destPosition > fromPosition) {
			onDragListener.drag(fromPosition, destPosition,false);
		} else if (destPosition < fromPosition) {// ��������ƶ�
			onDragListener.drag(fromPosition, destPosition,true);
		}
		fromPosition = destPosition;
	}

	public void setOnDragListener(OnDragListener onDragListener) {
		this.onDragListener = onDragListener;
	}
	
	public void setDragColor(int dragColor) {
		this.dragColor = dragColor;
	}
	
	/**
	 * �϶�ʱ��״̬����
	 * @author lance
	 */
	public interface OnDragListener {
		public void drag(int dest, int from,boolean isUp);
		public void drop(int prePosition,int destPosition);
		public void down(int downPosition);
		public View getControlViewFromId(ViewGroup viewGroup);
	}
}
