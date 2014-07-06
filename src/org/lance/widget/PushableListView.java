package org.lance.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 可推动标题的分组ListView
 * 
 * @author lance
 * 
 */
public class PushableListView extends ListView {
	// 隐藏状态
	public static final int STATE_GONE = 0;
	// 在List顶部显示
	public static final int STATE_VISIBLE = 1;
	// If the header extends beyond the bottom of the first shown element, push
	// it up and clip
	public static final int STATE_PUSHED_UP = 2;
	private View titleView;
	private boolean titleVisible;
	private int width;
	private int height;
	private OnPushListener onPushListener;

	public PushableListView(Context context) {
		super(context);
	}

	public PushableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PushableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (titleVisible) {
			drawChild(canvas, titleView, getDrawingTime());
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (titleView != null) {
			measureChild(titleView, widthMeasureSpec, heightMeasureSpec);
			width = titleView.getMeasuredWidth();
			height = titleView.getMeasuredHeight();
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (titleView != null) {
			titleView.layout(0, 0, width, height);
			titleLayout(getFirstVisiblePosition());
		}
	}

	public void setTitleView(View view) {
		titleView = view;
		if (titleView != null) {
			setFadingEdgeLength(0);
		}
		requestLayout();
	}

	public OnPushListener getOnPushListener() {
		return onPushListener;
	}

	public void setOnPushListener(OnPushListener onPushListener) {
		this.onPushListener = onPushListener;
	}

	// 滑动时处理头部---适配器中需要实现OnSrcollListener监听器
	public void titleLayout(int firsttitleVisiablePosition) {
		if (titleView == null) {
			return;
		}
		int state = STATE_GONE;
		if (onPushListener != null) {
			state = onPushListener
					.getStateFromAdapter(firsttitleVisiablePosition);
		} else {
			Log.i(this.getClass().getSimpleName(), "你应当实现OnPushListener监听器"
					+ "适配器在handleTitle中调用setTitleText(title, position);"
					+ "\n在getStateFromAdapter中调用getTitleState(position)");
			return;
		}
		switch (state) {
		case STATE_GONE:
			titleVisible = false;
			break;
		case STATE_VISIBLE:
			if (titleView.getTop() != 0) {
				titleView.layout(0, 0, width, height);
			}
			onPushListener.handleTitle(titleView, firsttitleVisiablePosition);
			titleVisible = true;
			break;
		case STATE_PUSHED_UP:
			View firstView = getChildAt(0);
			if (firstView != null) {
				int bottom = firstView.getBottom();
				int headerHeight = titleView.getHeight();
				int top;
				if (bottom < headerHeight) {
					top = (bottom - headerHeight);
				} else {
					top = 0;
				}
				onPushListener.handleTitle(titleView,
						firsttitleVisiablePosition);
				if (titleView.getTop() != top) {
					titleView.layout(0, top, width, height + top);
				}
				titleVisible = true;
			}
			break;
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
	}

	public interface OnPushListener {
		/**
		 * 处理标题视图---适配器需要做相应的处理
		 * 
		 * @param title
		 * @param position
		 */
		public void handleTitle(View title, int position);

		/**
		 * 根据适配器获取状态---适配器需要做相应的处理
		 * 
		 * @param position
		 * @return
		 */
		public int getStateFromAdapter(int position);
	}
}