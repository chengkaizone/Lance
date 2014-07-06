package org.lance.chartengine.chart;

import org.lance.chartengine.model.CategorySeries;
import org.lance.chartengine.renderer.DefaultRenderer;
import org.lance.chartengine.renderer.SimpleSeriesRenderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

/**
 * 圆形图表模板---用于扩展圆形图表
 * 
 * @author lance
 * 
 */
public abstract class RoundChart extends AbstractChart {
	/** The legend shape width. */
	protected static final int SHAPE_WIDTH = 10;
	/** The series dataset. */
	protected CategorySeries mDataset;
	/** The series renderer. */
	protected DefaultRenderer mRenderer;
	/** A no value constant. */
	protected static final int NO_VALUE = Integer.MAX_VALUE;
	/** The chart center X axis. */
	protected int mCenterX = NO_VALUE;
	/** The chart center y axis. */
	protected int mCenterY = NO_VALUE;

	/**
	 * 创建圆形图表实例
	 * 
	 * @param dataset
	 *            the series dataset
	 * @param renderer
	 *            the series renderer
	 */
	public RoundChart(CategorySeries dataset, DefaultRenderer renderer) {
		mDataset = dataset;
		mRenderer = renderer;
	}

	/**
	 * 绘制图表主题
	 * 
	 * @param canvas
	 *            the canvas to paint to
	 * @param x
	 *            the top left x value of the view to draw to
	 * @param y
	 *            the top left y value of the view to draw to
	 * @param width
	 *            the width of the view to draw to
	 * @param paint
	 *            the paint
	 */
	public void drawTitle(Canvas canvas, int x, int y, int width, Paint paint) {
		if (mRenderer.isShowLabels()) {
			paint.setColor(mRenderer.getLabelsColor());
			paint.setTextAlign(Align.CENTER);
			paint.setTextSize(mRenderer.getChartTitleTextSize());
			drawString(canvas, mRenderer.getChartTitle(), x + width / 2, y
					+ mRenderer.getChartTitleTextSize(), paint);
		}
	}

	/**
	 * 返回刻纹宽度
	 * 
	 * @param seriesIndex
	 *            the series index
	 * @return the legend shape width
	 */
	public int getLegendShapeWidth(int seriesIndex) {
		return SHAPE_WIDTH;
	}

	/**
	 * The graphical representation of the legend shape. 绘制刻纹
	 * 
	 * @param canvas
	 *            the canvas to paint to
	 * @param renderer
	 *            the series renderer
	 * @param x
	 *            the x value of the point the shape should be drawn at
	 * @param y
	 *            the y value of the point the shape should be drawn at
	 * @param seriesIndex
	 *            the series index
	 * @param paint
	 *            the paint to be used for drawing
	 */
	public void drawLegendShape(Canvas canvas, SimpleSeriesRenderer renderer,
			float x, float y, int seriesIndex, Paint paint) {
		canvas.drawRect(x, y - SHAPE_WIDTH / 2, x + SHAPE_WIDTH, y
				+ SHAPE_WIDTH / 2, paint);
	}

	/**
	 * Returns the renderer. 返回渲染器
	 * 
	 * @return the renderer
	 */
	public DefaultRenderer getRenderer() {
		return mRenderer;
	}

	/**
	 * Returns the center on X axis. 返回X轴的中心
	 * 
	 * @return the center on X axis
	 */
	public int getCenterX() {
		return mCenterX;
	}

	/**
	 * Returns the center on Y axis. 返回Y轴的中心
	 * 
	 * @return the center on Y axis
	 */
	public int getCenterY() {
		return mCenterY;
	}

	/**
	 * Sets a new center on X axis. 在X轴上设置一个新的中心
	 * 
	 * @param centerX
	 *            center on X axis
	 */
	public void setCenterX(int centerX) {
		mCenterX = centerX;
	}

	/**
	 * Sets a new center on Y axis. 在Y轴上设置一个新的中心
	 * 
	 * @param centerY
	 *            center on Y axis
	 */
	public void setCenterY(int centerY) {
		mCenterY = centerY;
	}

}
