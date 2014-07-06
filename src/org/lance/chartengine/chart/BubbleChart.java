package org.lance.chartengine.chart;

import org.lance.chartengine.model.XYMultipleSeriesDataset;
import org.lance.chartengine.model.XYValueSeries;
import org.lance.chartengine.renderer.SimpleSeriesRenderer;
import org.lance.chartengine.renderer.XYMultipleSeriesRenderer;
import org.lance.chartengine.renderer.XYSeriesRenderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

/**
 * The bubble chart rendering class. 圆形图表
 * 
 * @author lance
 * 
 */
public class BubbleChart extends XYChart {
	/** The constant to identify this chart type. */
	public static final String TYPE = "Bubble";
	/** The legend shape width. */
	private static final int SHAPE_WIDTH = 10;
	/** The minimum bubble size. */
	private static final int MIN_BUBBLE_SIZE = 2;
	/** The maximum bubble size. */
	private static final int MAX_BUBBLE_SIZE = 20;

	BubbleChart() {
	}

	/**
	 * Builds a new bubble chart instance. 创建泡泡图表实例
	 * 
	 * @param dataset
	 *            the multiple series dataset
	 * @param renderer
	 *            the multiple series renderer
	 */
	public BubbleChart(XYMultipleSeriesDataset dataset,
			XYMultipleSeriesRenderer renderer) {
		super(dataset, renderer);
	}

	/**
	 * The graphical representation of a series. 绘制序列
	 * 
	 * @param canvas
	 *            the canvas to paint to
	 * @param paint
	 *            the paint to be used for drawing
	 * @param points
	 *            the array of points to be used for drawing the series
	 * @param seriesRenderer
	 *            the series renderer
	 * @param yAxisValue
	 *            the minimum value of the y axis
	 * @param seriesIndex
	 *            the index of the series currently being drawn
	 * @param startIndex
	 *            the start index of the rendering points
	 */
	public void drawSeries(Canvas canvas, Paint paint, float[] points,
			SimpleSeriesRenderer seriesRenderer, float yAxisValue,
			int seriesIndex, int startIndex) {
		XYSeriesRenderer renderer = (XYSeriesRenderer) seriesRenderer;
		paint.setColor(renderer.getColor());
		paint.setStyle(Style.FILL);
		int length = points.length;
		XYValueSeries series = (XYValueSeries) mDataset
				.getSeriesAt(seriesIndex);
		double max = series.getMaxValue();
		double coef = MAX_BUBBLE_SIZE / max;
		for (int i = 0; i < length; i += 2) {
			double size = series.getValue(startIndex + i / 2) * coef
					+ MIN_BUBBLE_SIZE;
			drawCircle(canvas, paint, points[i], points[i + 1], (float) size);
		}
	}

	@Override
	protected ClickableArea[] clickableAreasForPoints(float[] points,
			double[] values, float yAxisValue, int seriesIndex, int startIndex) {
		int length = points.length;
		XYValueSeries series = (XYValueSeries) mDataset
				.getSeriesAt(seriesIndex);
		double max = series.getMaxValue();
		double coef = MAX_BUBBLE_SIZE / max;
		ClickableArea[] ret = new ClickableArea[length / 2];
		for (int i = 0; i < length; i += 2) {
			double size = series.getValue(startIndex + i / 2) * coef
					+ MIN_BUBBLE_SIZE;
			ret[i / 2] = new ClickableArea(new RectF(points[i] - (float) size,
					points[i + 1] - (float) size, points[i] + (float) size,
					points[i + 1] + (float) size), values[i], values[i + 1]);
		}
		return ret;
	}

	/**
	 * Returns the legend shape width. 返回刻纹宽度
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
		paint.setStyle(Style.FILL);
		drawCircle(canvas, paint, x + SHAPE_WIDTH, y, 3);
	}

	/**
	 * The graphical representation of a circle point shape. 绘制圆
	 * 
	 * @param canvas
	 *            the canvas to paint to
	 * @param paint
	 *            the paint to be used for drawing
	 * @param x
	 *            the x value of the point the shape should be drawn at
	 * @param y
	 *            the y value of the point the shape should be drawn at
	 * @param radius
	 *            the bubble radius
	 */
	private void drawCircle(Canvas canvas, Paint paint, float x, float y,
			float radius) {
		canvas.drawCircle(x, y, radius, paint);
	}

	/**
	 * Returns the chart type identifier. 返回图表标志符
	 */
	public String getChartType() {
		return TYPE;
	}

}