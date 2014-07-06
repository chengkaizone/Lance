package org.lance.chartengine.client;

import org.lance.chartengine.chart.AbstractChart;
import org.lance.chartengine.chart.RoundChart;
import org.lance.chartengine.chart.XYChart;
import org.lance.chartengine.model.ChartPoint;
import org.lance.chartengine.model.SeriesSelection;
import org.lance.chartengine.renderer.DefaultRenderer;
import org.lance.chartengine.renderer.XYMultipleSeriesRenderer;
import org.lance.chartengine.tool.FitZoom;
import org.lance.chartengine.tool.PanListener;
import org.lance.chartengine.tool.Zoom;
import org.lance.chartengine.tool.ZoomListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 一个封装了图形化图表的视图
 * 
 * @author lance
 * 
 */
public class GraphicalView extends View {
	/** The chart to be drawn. */
	private AbstractChart mChart;
	/** The chart renderer. */
	private DefaultRenderer mRenderer;
	/** The view bounds. */
	private Rect mRect = new Rect();
	/** The user interface thread handler. */
	private Handler mHandler;
	/** The zoom buttons rectangle. */
	private RectF mZoomR = new RectF();
	/** The zoom in icon. */
	private Bitmap zoomInImage;
	/** The zoom out icon. */
	private Bitmap zoomOutImage;
	/** The fit zoom icon. */
	private Bitmap fitZoomImage;
	/** The zoom area size. */
	private int zoomSize = 50;
	/** The zoom buttons background color. */
	private static final int ZOOM_BUTTONS_COLOR = Color
			.argb(175, 150, 150, 150);
	/** The zoom in tool. */
	private Zoom mZoomIn;
	/** The zoom out tool. */
	private Zoom mZoomOut;
	/** The fit zoom tool. */
	private FitZoom mFitZoom;
	/** The paint to be used when drawing the chart. */
	private Paint mPaint = new Paint();
	/** The touch handler. */
	private ITouchHandler mTouchHandler;
	/** is allow touch event */
	private boolean allowTouch;
	/** The old x coordinate. */
	private float oldX;
	/** The old y coordinate. */
	private float oldY;

	public GraphicalView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mHandler = new Handler();
	}

	public GraphicalView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHandler = new Handler();
	}

	public GraphicalView(Context context) {
		super(context);
		mHandler = new Handler();
	}

	/**
	 * Creates a new graphical view. 创建一个图形视图
	 * 
	 * @param context
	 *            the context
	 * @param chart
	 *            the chart to be drawn
	 */
	public GraphicalView(Context context, AbstractChart chart) {
		super(context);
		mHandler = new Handler();
		init(chart);
	}

	private void init(AbstractChart chart) {
		this.mChart = chart;
		if (mChart instanceof XYChart) {
			mRenderer = ((XYChart) mChart).getRenderer();
		} else {
			mRenderer = ((RoundChart) mChart).getRenderer();
		}
		if (mRenderer.isZoomButtonsVisible()) {
			zoomInImage = BitmapFactory.decodeStream(GraphicalView.class
					.getResourceAsStream("image/zoom_in.png"));
			zoomOutImage = BitmapFactory.decodeStream(GraphicalView.class
					.getResourceAsStream("image/zoom_out.png"));
			fitZoomImage = BitmapFactory.decodeStream(GraphicalView.class
					.getResourceAsStream("image/zoom_1.png"));
		}

		if (mRenderer.isZoomEnabled() && mRenderer.isZoomButtonsVisible()
				|| mRenderer.isExternalZoomEnabled()) {
			mZoomIn = new Zoom(mChart, true, mRenderer.getZoomRate());
			mZoomOut = new Zoom(mChart, false, mRenderer.getZoomRate());
			mFitZoom = new FitZoom(mChart);
		}
		int version = 7;
		try {
			version = Integer.valueOf(Build.VERSION.SDK);
		} catch (Exception e) {
			// do nothing
		}
		if (version < 7) {
			mTouchHandler = new TouchHandlerOld(this, mChart);
		} else {
			mTouchHandler = new TouchHandler(this, mChart);
		}
	}

	public void setChart(AbstractChart chart) {
		init(chart);
		repaint();
	}

	/**
	 * Returns the current series selection object. 返回当前系列选择对象
	 * 
	 * @return the series selection
	 */
	public SeriesSelection getCurrentSeriesAndPoint() {
		if (mChart != null) {
			mChart.getSeriesAndPointForScreenCoordinate(new ChartPoint(oldX,
					oldY));
		}
		return null;
	}

	/**
	 * Transforms the currently selected screen point to a real point.
	 * 
	 * @param scale
	 *            the scale
	 * @return the currently selected real point
	 */
	public double[] toRealPoint(int scale) {
		if ((mChart != null) && mChart instanceof XYChart) {
			XYChart chart = (XYChart) mChart;
			return chart.toRealPoint(oldX, oldY, scale);
		}
		return null;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.getClipBounds(mRect);// 这里获取画布的大小
		int top = mRect.top;
		int left = mRect.left;
		int width = mRect.width();
		int height = mRect.height();
		if (mChart == null) {
			return;
		}
		if (mRenderer.isInScroll()) {
			top = 0;
			left = 0;
			width = getMeasuredWidth();
			height = getMeasuredHeight();
		}
		mChart.draw(canvas, left, top, width, height, mPaint);
		if (mRenderer != null && mRenderer.isZoomEnabled()
				&& mRenderer.isZoomButtonsVisible()) {
			mPaint.setColor(ZOOM_BUTTONS_COLOR);// 绘制缩放按钮
			zoomSize = Math.max(zoomSize, Math.min(width, height) / 7);
			mZoomR.set(left + width - zoomSize * 3, top + height - zoomSize
					* 0.775f, left + width, top + height);
			canvas.drawRoundRect(mZoomR, zoomSize / 3, zoomSize / 3, mPaint);
			float buttonY = top + height - zoomSize * 0.625f;
			if (zoomInImage != null) {
				canvas.drawBitmap(zoomInImage, left + width - zoomSize * 2.75f,
						buttonY, null);
			}
			if (zoomOutImage != null) {
				canvas.drawBitmap(zoomOutImage,
						left + width - zoomSize * 1.75f, buttonY, null);
			}
			if (fitZoomImage != null) {
				canvas.drawBitmap(fitZoomImage,
						left + width - zoomSize * 0.75f, buttonY, null);
			}
		}
	}

	/**
	 * Sets the zoom rate. 设置缩放比率
	 * 
	 * @param rate
	 *            the zoom rate
	 */
	public void setZoomRate(float rate) {
		if (mZoomIn != null && mZoomOut != null) {
			mZoomIn.setZoomRate(rate);
			mZoomOut.setZoomRate(rate);
		}
	}

	public boolean isAllowTouch() {
		return allowTouch;
	}

	public void setAllowTouch(boolean allowTouch) {
		this.allowTouch = allowTouch;
	}

	/**
	 * Do a chart zoom in. 缩小图表
	 */
	public void zoomIn() {
		if (mZoomIn != null) {
			mZoomIn.apply(Zoom.ZOOM_AXIS_XY);
			repaint();
		}
	}

	/**
	 * Do a chart zoom out. 放大图表
	 */
	public void zoomOut() {
		if (mZoomOut != null) {
			mZoomOut.apply(Zoom.ZOOM_AXIS_XY);
			repaint();
		}
	}

	/**
	 * Do a chart zoom reset / fit zoom. 复位/适应缩放图表
	 */
	public void zoomReset() {
		if (mFitZoom != null) {
			mFitZoom.apply();
			mZoomIn.notifyZoomResetListeners();
			repaint();
		}
	}

	/**
	 * Adds a new zoom listener. 增加一个缩放监听
	 * 
	 * @param listener
	 *            zoom listener
	 */
	public void addZoomListener(ZoomListener listener, boolean onButtons,
			boolean onPinch) {
		if (onButtons) {
			if (mZoomIn != null) {
				mZoomIn.addZoomListener(listener);
				mZoomOut.addZoomListener(listener);
			}
			if (onPinch) {
				mTouchHandler.addZoomListener(listener);
			}
		}
	}

	/**
	 * Removes a zoom listener. 移除缩放监听
	 * 
	 * @param listener
	 *            zoom listener
	 */
	public synchronized void removeZoomListener(ZoomListener listener) {
		if (mZoomIn != null) {
			mZoomIn.removeZoomListener(listener);
			mZoomOut.removeZoomListener(listener);
		}
		mTouchHandler.removeZoomListener(listener);
	}

	/**
	 * Adds a new pan listener. 增加触摸监听
	 * 
	 * @param listener
	 *            pan listener
	 */
	public void addPanListener(PanListener listener) {
		mTouchHandler.addPanListener(listener);
	}

	/**
	 * Removes a pan listener. 移除触摸监听
	 * 
	 * @param listener
	 *            pan listener
	 */
	public void removePanListener(PanListener listener) {
		mTouchHandler.removePanListener(listener);
	}

	protected RectF getZoomRectangle() {
		return mZoomR;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// save the x and y so they can be used in the click and long press
			// listeners
			oldX = event.getX();
			oldY = event.getY();
		}
		if (mRenderer != null && isAllowTouch()
				&& (mRenderer.isPanEnabled() || mRenderer.isZoomEnabled())) {
			if (mTouchHandler.handleTouch(event)) {
				return true;
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * Schedule a view content repaint. 重绘视图
	 */
	public void repaint() {
		mHandler.post(new Runnable() {
			public void run() {
				invalidate();
			}
		});
	}

	/**
	 * Schedule a view content repaint, in the specified rectangle area.
	 * 安排一个视图内容重新绘制,在指定的矩形区域
	 * 
	 * @param left
	 *            the left position of the area to be repainted
	 * @param top
	 *            the top position of the area to be repainted
	 * @param right
	 *            the right position of the area to be repainted
	 * @param bottom
	 *            the bottom position of the area to be repainted
	 */
	public void repaint(final int left, final int top, final int right,
			final int bottom) {
		mHandler.post(new Runnable() {
			public void run() {
				invalidate(left, top, right, bottom);
			}
		});
	}

	/**
	 * Saves the content of the graphical view to a bitmap. 将图形内容转成位图
	 * 
	 * @return the bitmap
	 */
	public Bitmap toBitmap() {
		setDrawingCacheEnabled(false);
		if (!isDrawingCacheEnabled()) {
			setDrawingCacheEnabled(true);
		}
		if (mRenderer.isApplyBackgroundColor()) {
			setDrawingCacheBackgroundColor(mRenderer.getBackgroundColor());
		}
		setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
		return getDrawingCache(true);
	}

}