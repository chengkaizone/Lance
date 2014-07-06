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
 * 可拖动排序的ListView 
 * @author lance
 *
 */
public class SortableListView extends ListView {
	// 被拖拽的项，其实就是一个ImageView
	private ImageView dragImageView;
	// 手指按下的时候列表项在listview中的位置
	private int downPosition;
	// 用于交换列表项用
	private int fromPosition;
	// 手指拖动的时候，当前拖动项在列表中的位置
	private int destPosition;
	// 在当前数据项中的位置
	private int dragPoint;
	// 当前视图和屏幕的距离(这里只使用了y方向上)
	private int dragOffset;
	// windows窗口控制类
	private WindowManager windowManager;
	// 用于控制拖拽项的显示的参数
	private WindowManager.LayoutParams windowParams;
	//浮动视图的默认背景颜色
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

	// 拦截touch事件，其实就是加一层控制
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			// 判断当前xy值 是否在item上 如果在 返回改item的position 否则 返回 INVALID_POSITION（-1）
			downPosition = pointToPosition(x, y);
			if (downPosition == AdapterView.INVALID_POSITION) {
				return super.onInterceptTouchEvent(ev);
			}
			fromPosition = downPosition;
			destPosition = downPosition;
			// 获取当前点击的view
			itemView = (ViewGroup) getChildAt(downPosition
					- getFirstVisiblePosition());
			// 点击坐标-view的上边界
			dragPoint = y - itemView.getTop();
			// 整个屏幕中的y坐标-listview中的y坐标,即偏移量
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
	 * 触摸事件
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
	 * 准备拖动，初始化拖动项的图像
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
	 * 停止拖动，去除拖动项的头像
	 */
	public void stopDrag() {
		if (dragImageView != null) {
			windowManager.removeView(dragImageView);
			dragImageView = null;
			itemView.setBackgroundColor(Color.TRANSPARENT);
		}
	}

	/**
	 * 拖动执行，在Move方法中执行
	 */
	public void onDrag(int y, int rawY) {
		// 超出上边界，设为最小值位置0
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
		// 为了避免滑动到分割线的时候，返回-1的问题
		int itemPosition = pointToPosition(0, y);
		if (itemPosition != INVALID_POSITION) {
			destPosition = itemPosition;
			onDrop(y);
		}
	}

	// 根据拖动的位置在列表中放下
	public void onDrop(int y) {
		// 鼠标向下移动
		if (destPosition > fromPosition) {
			onDragListener.drag(fromPosition, destPosition,false);
		} else if (destPosition < fromPosition) {// 鼠标向上移动
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
	 * 拖动时的状态监听
	 * @author lance
	 */
	public interface OnDragListener {
		public void drag(int dest, int from,boolean isUp);
		public void drop(int prePosition,int destPosition);
		public void down(int downPosition);
		public View getControlViewFromId(ViewGroup viewGroup);
	}
}
