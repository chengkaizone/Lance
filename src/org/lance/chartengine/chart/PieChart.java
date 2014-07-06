package org.lance.chartengine.chart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.lance.chartengine.model.CategorySeries;
import org.lance.chartengine.model.ChartPoint;
import org.lance.chartengine.model.SeriesSelection;
import org.lance.chartengine.renderer.DefaultRenderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

/**
 * The pie chart rendering class. 饼形图表 绘制比例图
 * 
 * @author lance
 * 
 */
public class PieChart extends RoundChart {
	/** Handles returning values when tapping on PieChart. */
	private PieMapper mPieMapper;

	/**
	 * Builds a new pie chart instance. 创建实例
	 * 
	 * @param dataset
	 *            the series dataset
	 * @param renderer
	 *            the series renderer
	 */
	public PieChart(CategorySeries dataset, DefaultRenderer renderer) {
		super(dataset, renderer);
		mPieMapper = new PieMapper();
	}

	/**
	 * The graphical representation of the pie chart. 绘制饼图表
	 * 
	 * @param canvas
	 *            the canvas to paint to
	 * @param x
	 *            the top left x value of the view to draw to
	 * @param y
	 *            the top left y value of the view to draw to
	 * @param width
	 *            the width of the view to draw to
	 * @param height
	 *            the height of the view to draw to
	 * @param paint
	 *            the paint
	 */
	@Override
	public void draw(Canvas canvas, int x, int y, int width, int height,
			Paint paint) {
		paint.setAntiAlias(mRenderer.isAntialiasing());
		paint.setStyle(Style.FILL);
		paint.setTextSize(mRenderer.getLabelsTextSize());
		int legendSize = getLegendSize(mRenderer, height / 5, 0);
		int left = x;
		int top = y;
		int right = x + width;
		int sLength = mDataset.getItemCount();
		double total = 0;
		String[] titles = new String[sLength];
		for (int i = 0; i < sLength; i++) {
			total += mDataset.getValue(i);
			titles[i] = mDataset.getCategory(i);
		}
		if (mRenderer.isFitLegend()) {
			legendSize = drawLegend(canvas, mRenderer, titles, left, right, y,
					width, height, legendSize, paint, true);
		}
		int bottom = y + height - legendSize;
		drawBackground(mRenderer, canvas, x, y, width, height, paint, false,
				DefaultRenderer.NO_COLOR);

		float currentAngle = mRenderer.getStartAngle();
		int mRadius = Math.min(Math.abs(right - left), Math.abs(bottom - top));
		int radius = (int) (mRadius * 0.35 * mRenderer.getScale());

		if (mCenterX == NO_VALUE) {
			mCenterX = (left + right) / 2;
		}
		if (mCenterY == NO_VALUE) {
			mCenterY = (bottom + top) / 2;
		}

		// Hook in clip detection after center has been calculated
		mPieMapper.setDimensions(radius, mCenterX, mCenterY);
		boolean loadPieCfg = !mPieMapper.areAllSegmentPresent(sLength);
		if (loadPieCfg) {
			mPieMapper.clearPieSegments();
		}

		float shortRadius = radius * 0.9f;
		float longRadius = radius * 1.1f;

		RectF oval = new RectF(mCenterX - radius, mCenterY - radius, mCenterX
				+ radius, mCenterY + radius);
		List<RectF> prevLabelsBounds = new ArrayList<RectF>();

		for (int i = 0; i < sLength; i++) {
			paint.setColor(mRenderer.getSeriesRendererAt(i).getColor());
			float value = (float) mDataset.getValue(i);
			float angle = (float) (value / total * 360);
			canvas.drawArc(oval, currentAngle, angle, true, paint);
			drawLabel(canvas, mDataset.getCategory(i), mRenderer,
					prevLabelsBounds, mCenterX, mCenterY, shortRadius,
					longRadius, currentAngle, angle, left, right,
					mRenderer.getLabelsColor(), paint, true);
			if (mRenderer.isDisplayValues()) {
				drawLabel(canvas, getLabel(mDataset.getValue(i)), mRenderer,
						prevLabelsBounds, mCenterX, mCenterY, shortRadius / 2,
						longRadius / 2, currentAngle, angle, left, right,
						mRenderer.getLabelsColor(), paint, false);
			}

			// Save details for getSeries functionality
			if (loadPieCfg) {
				mPieMapper.addPieSegment(i, value, currentAngle, angle);
			}
			currentAngle += angle;
		}
		prevLabelsBounds.clear();
		drawLegend(canvas, mRenderer, titles, left, right, y, width, height,
				legendSize, paint, false);
		drawTitle(canvas, x, y, width, paint);
	}

	public SeriesSelection getSeriesAndPointForScreenCoordinate(
			ChartPoint screenPoint) {
		return mPieMapper.getSeriesAndPointForScreenCoordinate(screenPoint);
	}

}

/**
 * PieChart Segment Selection Management. 饼形图表分段管理类
 * 
 * @author lance
 * 
 */
class PieMapper implements Serializable {

	private List<PieSegment> mPieSegmentList = new ArrayList<PieSegment>();

	private int mPieChartRadius;

	private int mCenterX, mCenterY;

	/**
	 * Set PieChart location on screen. 在屏幕上设置饼形图位置
	 * 
	 * @param pieRadius
	 * @param centerX
	 * @param centerY
	 */
	public void setDimensions(int pieRadius, int centerX, int centerY) {
		mPieChartRadius = pieRadius;
		mCenterX = centerX;
		mCenterY = centerY;
	}

	/**
	 * If we have all PieChart Config then there is no point in reloading it
	 * 是否父类所有段
	 * 
	 * @param datasetSize
	 * @return true if cfg for each segment is present
	 */
	public boolean areAllSegmentPresent(int datasetSize) {
		return mPieSegmentList.size() == datasetSize;
	}

	/**
	 * Add configuration for a PieChart Segment 为饼形段添加一个配置
	 * 
	 * @param dataIndex
	 * @param value
	 * @param startAngle
	 * @param angle
	 */
	public void addPieSegment(int dataIndex, float value, float startAngle,
			float angle) {
		mPieSegmentList
				.add(new PieSegment(dataIndex, value, startAngle, angle));
	}

	/**
	 * Fetches angle relative to pie chart center point where 3 O'Clock is 0 and
	 * 12 O'Clock is 270degrees Clears the pie segments list. 清除饼形段
	 */
	public void clearPieSegments() {
		mPieSegmentList.clear();
	}

	/**
	 * Fetches angle relative to pie chart center point where 3 O'Clock is 0 and
	 * 12 O'Clock is 270degrees 根据屏幕点获取闹钟上的角度
	 * 
	 * @param screenPoint
	 * @return angle in degress from 0-360.
	 */
	public double getAngle(ChartPoint screenPoint) {
		double dx = screenPoint.getX() - mCenterX;
		// Minus to correct for coord re-mapping
		double dy = -(screenPoint.getY() - mCenterY);

		double inRads = Math.atan2(dy, dx);

		// We need to map to coord system when 0 degree is at 3 O'clock, 270 at
		// 12 O'clock
		if (inRads < 0)
			inRads = Math.abs(inRads);
		else
			inRads = 2 * Math.PI - inRads;

		return Math.toDegrees(inRads);
	}

	/**
	 * Checks if Point falls within PieChart 检测点是否在饼形图内
	 * 
	 * @param screenPoint
	 * @return true if in PieChart
	 */
	public boolean isOnPieChart(ChartPoint screenPoint) {
		// Using a bit of Pythagoras
		// inside circle if (x-center_x)**2 + (y-center_y)**2 <= radius**2:

		double sqValue = (Math.pow(mCenterX - screenPoint.getX(), 2) + Math
				.pow(mCenterY - screenPoint.getY(), 2));

		double radiusSquared = mPieChartRadius * mPieChartRadius;
		boolean isOnPieChart = sqValue <= radiusSquared;
		return isOnPieChart;
	}

	/**
	 * Fetches the SeriesSelection for the PieSegment selected. 获取屏幕坐标序列块
	 * 
	 * @param screenPoint
	 *            - the user tap location
	 * @return null if screen point is not in PieChart or its config if it is
	 */
	public SeriesSelection getSeriesAndPointForScreenCoordinate(
			ChartPoint screenPoint) {
		if (isOnPieChart(screenPoint)) {
			double angleFromPieCenter = getAngle(screenPoint);

			for (PieSegment pieSeg : mPieSegmentList) {
				if (pieSeg.isInSegment(angleFromPieCenter)) {
					return new SeriesSelection(0, pieSeg.getDataIndex(),
							pieSeg.getValue(), pieSeg.getValue());
				}
			}
		}
		return null;
	}

}

/**
 * Holds An PieChart Segment
 * 
 * @author lance
 * 
 */
class PieSegment implements Serializable {
	private float mStartAngle;

	private float mEndAngle;

	private int mDataIndex;

	private float mValue;

	public PieSegment(int dataIndex, float value, float startAngle, float angle) {
		mStartAngle = startAngle;
		mEndAngle = angle + startAngle;
		mDataIndex = dataIndex;
		mValue = value;
	}

	/**
	 * Checks if angle falls in segment. 检查角度是否在段内
	 * 
	 * @param angle
	 * @return true if in segment, false otherwise.
	 */
	public boolean isInSegment(double angle) {
		return angle >= mStartAngle && angle <= mEndAngle;
	}

	protected float getStartAngle() {
		return mStartAngle;
	}

	protected float getEndAngle() {
		return mEndAngle;
	}

	protected int getDataIndex() {
		return mDataIndex;
	}

	protected float getValue() {
		return mValue;
	}

	public String toString() {
		return "mDataIndex=" + mDataIndex + ",mValue=" + mValue
				+ ",mStartAngle=" + mStartAngle + ",mEndAngle=" + mEndAngle;
	}

}
