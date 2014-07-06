package org.lance.chartengine.chart;

import org.lance.chartengine.model.XYMultipleSeriesDataset;
import org.lance.chartengine.renderer.SimpleSeriesRenderer;
import org.lance.chartengine.renderer.XYMultipleSeriesRenderer;
import org.lance.chartengine.renderer.XYSeriesRenderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

/**
 * The line chart rendering class. 线图表
 * 
 * @author lance
 * 
 */
public class LineChart extends XYChart {
	/** The constant to identify this chart type. */
	public static final String TYPE = "Line";
	/** The legend shape width. */
	private static final int SHAPE_WIDTH = 30;
	/** The scatter chart to be used to draw the data points. */
	private ScatterChart pointsChart;

	LineChart() {
	}

	/**
	 * Builds a new line chart instance. 创建实例
	 * 
	 * @param dataset
	 *            the multiple series dataset
	 * @param renderer
	 *            the multiple series renderer
	 */
	public LineChart(XYMultipleSeriesDataset dataset,
			XYMultipleSeriesRenderer renderer) {
		super(dataset, renderer);
		pointsChart = new ScatterChart(dataset, renderer);
	}

	/**
	 * Sets the series and the renderer. 设置渲染数据
	 * 
	 * @param dataset
	 *            the series dataset
	 * @param renderer
	 *            the series renderer
	 */
	protected void setDatasetRenderer(XYMultipleSeriesDataset dataset,
			XYMultipleSeriesRenderer renderer) {
		super.setDatasetRenderer(dataset, renderer);
		pointsChart = new ScatterChart(dataset, renderer);
	}

	/**
	 * The graphical representation of a series. 绘制数据
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
		int length = points.length;
		XYSeriesRenderer renderer = (XYSeriesRenderer) seriesRenderer;
		float lineWidth = paint.getStrokeWidth();
		paint.setStrokeWidth(renderer.getLineWidth());
		if (renderer.isFillBelowLine()) {
			paint.setColor(renderer.getFillBelowLineColor());
			int pLength = points.length;
			float[] fillPoints = new float[pLength + 4];
			System.arraycopy(points, 0, fillPoints, 0, length);
			fillPoints[0] = points[0] + 1;
			fillPoints[length] = fillPoints[length - 2];
			fillPoints[length + 1] = yAxisValue;
			fillPoints[length + 2] = fillPoints[0];
			fillPoints[length + 3] = fillPoints[length + 1];
			for (int i = 0; i < length + 4; i += 2) {
				if (fillPoints[i + 1] < 0) {
					fillPoints[i + 1] = 0;
				}
			}
			paint.setStyle(Style.FILL);
			drawPath(canvas, fillPoints, paint, true);
		}
		paint.setColor(seriesRenderer.getColor());
		paint.setStyle(Style.STROKE);
		drawPath(canvas, points, paint, false);
		paint.setStrokeWidth(lineWidth);
	}

	@Override
	protected ClickableArea[] clickableAreasForPoints(float[] points,
			double[] values, float yAxisValue, int seriesIndex, int startIndex) {
		int length = points.length;
		ClickableArea[] ret = new ClickableArea[length / 2];
		for (int i = 0; i < length; i += 2) {
			int selectableBuffer = mRenderer.getSelectableBuffer();
			ret[i / 2] = new ClickableArea(new RectF(points[i]
					- selectableBuffer, points[i + 1] - selectableBuffer,
					points[i] + selectableBuffer, points[i + 1]
							+ selectableBuffer), values[i], values[i + 1]);
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
	 * The graphical representation of the legend shape. 绘制刻纹图表
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
		canvas.drawLine(x, y, x + SHAPE_WIDTH, y, paint);
		if (isRenderPoints(renderer)) {
			pointsChart.drawLegendShape(canvas, renderer, x + 5, y,
					seriesIndex, paint);
		}
	}

	/**
	 * Returns if the chart should display the points as a certain shape.
	 * 是否作为一个特定的形状渲染点
	 * 
	 * @param renderer
	 *            the series renderer
	 */
	public boolean isRenderPoints(SimpleSeriesRenderer renderer) {
		return ((XYSeriesRenderer) renderer).getPointStyle() != PointStyle.POINT;
	}

	/**
	 * Returns the scatter chart to be used for drawing the data points. 返回散射图表
	 * 
	 * @return the data points scatter chart
	 */
	public ScatterChart getPointsChart() {
		return pointsChart;
	}

	/**
	 * Returns the chart type identifier. 返回图表类型
	 * 
	 * @return the chart type
	 */
	public String getChartType() {
		return TYPE;
	}

}
