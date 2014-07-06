package org.lance.chartengine.renderer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.lance.chartengine.chart.PointStyle;
import org.lance.chartengine.util.MathHelper;

import android.graphics.Color;
import android.graphics.Paint.Align;

/**
 * 多个X,Y系列渲染器
 * 
 * @author lance
 * 
 */
public class XYMultipleSeriesRenderer extends DefaultRenderer {
	/** The X axis title. */
	private String mXTitle = "";
	/** The Y axis title. */
	private String[] mYTitle;
	/** The axis title text size. */
	private float mAxisTitleTextSize = 12;
	/** The start value in the X axis range. */
	private double[] mMinX;
	/** The end value in the X axis range. */
	private double[] mMaxX;
	/** The start value in the Y axis range. */
	private double[] mMinY;
	/** The end value in the Y axis range. */
	private double[] mMaxY;
	/** The approximative number of labels on the x axis. */
	private int mXLabels = 5;
	/** The approximative number of labels on the y axis. */
	private int mYLabels = 5;
	/** The current orientation of the chart. */
	private Orientation mOrientation = Orientation.HORIZONTAL;
	/** The X axis text labels. */
	private Map<Double, String> mXTextLabels = new HashMap<Double, String>();
	/** The Y axis text labels. */
	private Map<Integer, Map<Double, String>> mYTextLabels = new LinkedHashMap<Integer, Map<Double, String>>();
	/** A flag for enabling or not the pan on the X axis. */
	private boolean mPanXEnabled = true;
	/** A flag for enabling or not the pan on the Y axis. */
	private boolean mPanYEnabled = true;
	/** A flag for enabling or not the zoom on the X axis. */
	private boolean mZoomXEnabled = true;
	/** A flag for enabling or not the zoom on the Y axis . */
	private boolean mZoomYEnabled = true;
	/** The spacing between bars, in bar charts. */
	private double mBarSpacing = 0;
	/** The margins colors. */
	private int mMarginsColor = NO_COLOR;
	/** The pan limits. */
	private double[] mPanLimits;
	/** The zoom limits. */
	private double[] mZoomLimits;
	/** The X axis labels rotation angle. */
	private float mXLabelsAngle;
	/** The Y axis labels rotation angle. */
	private float mYLabelsAngle;
	/** The initial axis range. */
	private Map<Integer, double[]> initialRange = new LinkedHashMap<Integer, double[]>();
	/** The point size for charts displaying points. */
	private float mPointSize = 3;
	/** The grid color. */
	private int mGridColor = Color.argb(75, 200, 200, 200);
	/** The number of scales. */
	private int scalesCount;
	/** The X axis labels alignment. */
	private Align xLabelsAlign = Align.CENTER;
	/** The Y axis labels alignment. */
	private Align[] yLabelsAlign;
	/** The Y axis alignment. */
	private Align[] yAxisAlign;
	/** The X axis labels color. */
	private int mXLabelsColor = TEXT_COLOR;
	/** The Y axis labels color. */
	private int[] mYLabelsColor = new int[] { TEXT_COLOR };
	/**
	 * If X axis value selection algorithm to be used. Only used by the time
	 * charts.
	 */
	private boolean mXRoundedLabels = true;

	/**
	 * An enum for the XY chart orientation of the X axis. 图表X轴上方位的枚举,水平,垂直
	 */
	public enum Orientation {
		HORIZONTAL(0), VERTICAL(90);
		/** The rotate angle. */
		private int mAngle = 0;

		private Orientation(int angle) {
			mAngle = angle;
		}

		/**
		 * Return the orientation rotate angle.
		 * 
		 * @return the orientaion rotate angle
		 */
		public int getAngle() {
			return mAngle;
		}
	}

	public XYMultipleSeriesRenderer(int count) {
		for (int i = 0; i < count; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			addSeriesRenderer(r);
		}
		scalesCount = 1;
		initAxesRange(1);
	}

	/**
	 * 设置渲染器数量
	 */
	public void initRendererCount(int count) {
		removeAllRenderers();
		for (int i = 0; i < count; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			addSeriesRenderer(r);
		}
	}

	/*
	 * public XYMultipleSeriesRenderer(int scaleNumber) { scalesCount =
	 * scaleNumber; initAxesRange(scaleNumber); }
	 */
	public void initAxesRange(int scales) {
		mYTitle = new String[scales];
		yLabelsAlign = new Align[scales];
		yAxisAlign = new Align[scales];
		mYLabelsColor = new int[scales];
		mMinX = new double[scales];
		mMaxX = new double[scales];
		mMinY = new double[scales];
		mMaxY = new double[scales];
		for (int i = 0; i < scales; i++) {
			mYLabelsColor[i] = TEXT_COLOR;
			initAxesRangeForScale(i);
		}
	}

	public void initAxesRangeForScale(int i) {
		mMinX[i] = MathHelper.NULL_VALUE;
		mMaxX[i] = -MathHelper.NULL_VALUE;
		mMinY[i] = MathHelper.NULL_VALUE;
		mMaxY[i] = -MathHelper.NULL_VALUE;
		double[] range = new double[] { mMinX[i], mMaxX[i], mMinY[i], mMaxY[i] };
		initialRange.put(i, range);
		mYTitle[i] = "";
		mYTextLabels.put(i, new HashMap<Double, String>());
		yLabelsAlign[i] = Align.CENTER;
		yAxisAlign[i] = Align.LEFT;
	}

	/**
	 * Returns the current orientation of the chart X axis. 返回图表X轴的方位
	 * 
	 * @return the chart orientation
	 */
	public Orientation getOrientation() {
		return mOrientation;
	}

	/**
	 * Sets the current orientation of the chart X axis. 设置当前图表X轴的方位:水平,垂直
	 * 
	 * @param orientation
	 *            the chart orientation
	 */
	public void setOrientation(Orientation orientation) {
		mOrientation = orientation;
	}

	/**
	 * Returns the title for the X axis. 返回X轴标题文本
	 * 
	 * @return the X axis title
	 */
	public String getXTitle() {
		return mXTitle;
	}

	/**
	 * Sets the title for the X axis. 设置X轴标题文本
	 * 
	 * @param title
	 *            the X axis title
	 */
	public void setXTitle(String title) {
		mXTitle = title;
	}

	/**
	 * Returns the title for the Y axis. 返回Y轴标题的文本
	 * 
	 * @return the Y axis title
	 */
	public String getYTitle() {
		return getYTitle(0);
	}

	/**
	 * Returns the title for the Y axis. 返回Y轴标题大小---带缩放
	 * 
	 * @param scale
	 *            the renderer scale
	 * @return the Y axis title
	 */
	public String getYTitle(int scale) {
		return mYTitle[scale];
	}

	/**
	 * Sets the title for the Y axis. 设置Y轴上标题的字体大小
	 * 
	 * @param title
	 *            the Y axis title
	 */
	public void setYTitle(String title) {
		setYTitle(title, 0);
	}

	/**
	 * Sets the title for the Y axis. 设置Y轴上标题的字体大小---带缩放
	 * 
	 * @param title
	 *            the Y axis title
	 * @param scale
	 *            the renderer scale
	 */
	public void setYTitle(String title, int scale) {
		mYTitle[scale] = title;
	}

	/**
	 * Returns the axis title text size. 返回轴上标题的字体大小
	 * 
	 * @return the axis title text size
	 */
	public float getAxisTitleTextSize() {
		return mAxisTitleTextSize;
	}

	/**
	 * Sets the axis title text size. 设置轴上的文本标签的字体大小
	 * 
	 * @param textSize
	 *            the chart axis text size
	 */
	public void setAxisTitleTextSize(float textSize) {
		mAxisTitleTextSize = textSize;
	}

	/**
	 * Returns the start value of the X axis range. 返回X轴返回内的最小值
	 * 
	 * @return the X axis range start value
	 */
	public double getXAxisMin() {
		return getXAxisMin(0);
	}

	/**
	 * Sets the start value of the X axis range. 设置X轴最小值
	 * 
	 * @param min
	 *            the X axis range start value
	 */
	public void setXAxisMin(double min) {
		setXAxisMin(min, 0);
	}

	/**
	 * Returns if the minimum X value was set. 是否设置X轴最小值
	 * 
	 * @return the minX was set or not
	 */
	public boolean isMinXSet() {
		return isMinXSet(0);
	}

	/**
	 * Returns the end value of the X axis range. 返回X轴范围内的最大值
	 * 
	 * @return the X axis range end value
	 */
	public double getXAxisMax() {
		return getXAxisMax(0);
	}

	/**
	 * Sets the end value of the X axis range. 设置X轴范围内的最大值
	 * 
	 * @param max
	 *            the X axis range end value
	 */
	public void setXAxisMax(double max) {
		setXAxisMax(max, 0);
	}

	/**
	 * Returns if the maximum X value was set. X轴是否设置了最大值
	 * 
	 * @return the maxX was set or not
	 */
	public boolean isMaxXSet() {
		return isMaxXSet(0);
	}

	/**
	 * Returns the start value of the Y axis range. 返回Y轴范围内的最小值
	 * 
	 * @return the Y axis range end value
	 */
	public double getYAxisMin() {
		return getYAxisMin(0);
	}

	/**
	 * Sets the start value of the Y axis range. 设置最小的Y值---Y轴范围内
	 * 
	 * @param min
	 *            the Y axis range start value
	 */
	public void setYAxisMin(double min) {
		setYAxisMin(min, 0);
	}

	/**
	 * Returns if the minimum Y value was set. 返回最小的Y值
	 * 
	 * @return the minY was set or not
	 */
	public boolean isMinYSet() {
		return isMinYSet(0);
	}

	/**
	 * Returns the end value of the Y axis range. 返回Y轴范围内的最大值
	 * 
	 * @return the Y axis range end value
	 */
	public double getYAxisMax() {
		return getYAxisMax(0);
	}

	/**
	 * Sets the end value of the Y axis range. 设置Y轴范围内的最大值
	 * 
	 * @param max
	 *            the Y axis range end value
	 */
	public void setYAxisMax(double max) {
		setYAxisMax(max, 0);
	}

	/**
	 * Returns if the maximum Y value was set. 返回被设置的最大Y值
	 * 
	 * @return the maxY was set or not
	 */
	public boolean isMaxYSet() {
		return isMaxYSet(0);
	}

	/**
	 * Returns the start value of the X axis range. 返回X轴范围内的开始值
	 * 
	 * @param scale
	 *            the renderer scale
	 * @return the X axis range start value
	 */
	public double getXAxisMin(int scale) {
		return mMinX[scale];
	}

	/**
	 * Sets the start value of the X axis range. 设置X轴的起始值
	 * 
	 * @param min
	 *            the X axis range start value
	 * @param scale
	 *            the renderer scale
	 */
	public void setXAxisMin(double min, int scale) {
		if (!isMinXSet(scale)) {
			initialRange.get(scale)[0] = min;
		}
		mMinX[scale] = min;
	}

	/**
	 * Returns if the minimum X value was set. 最小的X值是否被设置
	 * 
	 * @param scale
	 *            the renderer scale
	 * @return the minX was set or not
	 */
	public boolean isMinXSet(int scale) {
		return mMinX[scale] != MathHelper.NULL_VALUE;
	}

	/**
	 * Returns the end value of the X axis range. 返回X轴范围内的结束值
	 * 
	 * @param scale
	 *            the renderer scale
	 * @return the X axis range end value
	 */
	public double getXAxisMax(int scale) {
		return mMaxX[scale];
	}

	/**
	 * Sets the end value of the X axis range. 设置X轴范围内的结束值
	 * 
	 * @param max
	 *            the X axis range end value
	 * @param scale
	 *            the renderer scale
	 */
	public void setXAxisMax(double max, int scale) {
		if (!isMaxXSet(scale)) {
			initialRange.get(scale)[1] = max;
		}
		mMaxX[scale] = max;
	}

	/**
	 * Returns if the maximum X value was set. 如果最大的X值被设置返回true
	 * 
	 * @param scale
	 *            the renderer scale
	 * @return the maxX was set or not
	 */
	public boolean isMaxXSet(int scale) {
		return mMaxX[scale] != -MathHelper.NULL_VALUE;
	}

	/**
	 * Returns the start value of the Y axis range. 返回Y轴类的起始值
	 * 
	 * @param scale
	 *            the renderer scale
	 * @return the Y axis range end value
	 */
	public double getYAxisMin(int scale) {
		return mMinY[scale];
	}

	/**
	 * Sets the start value of the Y axis range. 设置Y轴范围内的起始值
	 * 
	 * @param min
	 *            the Y axis range start value
	 * @param scale
	 *            the renderer scale
	 */
	public void setYAxisMin(double min, int scale) {
		if (!isMinYSet(scale)) {
			initialRange.get(scale)[2] = min;
		}
		mMinY[scale] = min;
	}

	/**
	 * Returns if the minimum Y value was set. 最低的Y值是否被设置
	 * 
	 * @param scale
	 *            the renderer scale
	 * @return the minY was set or not
	 */
	public boolean isMinYSet(int scale) {
		return mMinY[scale] != MathHelper.NULL_VALUE;
	}

	/**
	 * Returns the end value of the Y axis range. 返回Y轴范围内的结束值
	 * 
	 * @param scale
	 *            the renderer scale
	 * @return the Y axis range end value
	 */
	public double getYAxisMax(int scale) {
		return mMaxY[scale];
	}

	/**
	 * Sets the end value of the Y axis range. 在Y轴的范围内设定结束值
	 * 
	 * @param max
	 *            the Y axis range end value
	 * @param scale
	 *            the renderer scale
	 */
	public void setYAxisMax(double max, int scale) {
		if (!isMaxYSet(scale)) {
			initialRange.get(scale)[3] = max;
		}
		mMaxY[scale] = max;
	}

	/**
	 * Returns if the maximum Y value was set. 返回Y轴最大的值是否被设置
	 * 
	 * @param scale
	 *            the renderer scale
	 * @return the maxY was set or not
	 */
	public boolean isMaxYSet(int scale) {
		return mMaxY[scale] != -MathHelper.NULL_VALUE;
	}

	/**
	 * Returns the approximate number of labels for the X axis. 返回X轴刻纹标签接近的数量
	 * 
	 * @return the approximate number of labels for the X axis
	 */
	public int getXLabels() {
		return mXLabels;
	}

	/**
	 * Sets the approximate number of labels for the X axis. 设置X轴的刻纹数量
	 * 
	 * @param xLabels
	 *            the approximate number of labels for the X axis
	 */
	public void setXLabels(int xLabels) {
		mXLabels = xLabels;
	}

	/**
	 * Adds a new text label for the specified X axis value. 为指定X轴值添加一个文本标签
	 * 
	 * @param x
	 *            the X axis value
	 * @param text
	 *            the text label
	 */
	public synchronized void addXTextLabel(double x, String text) {
		mXTextLabels.put(x, text);
	}

	/**
	 * Removes text label for the specified X axis value. 移除指定X轴值上的文本标签
	 * 
	 * @param x
	 *            the X axis value
	 */
	public synchronized void removeXTextLabel(double x) {
		mXTextLabels.remove(x);
	}

	/**
	 * Returns the X axis text label at the specified X axis value.
	 * 返回指定X轴值上的文本标签
	 * 
	 * @param x
	 *            the X axis value
	 * @return the X axis text label
	 */
	public synchronized String getXTextLabel(Double x) {
		return mXTextLabels.get(x);
	}

	/**
	 * Returns the X text label locations. 获取X轴文本标签的为孩子
	 * 
	 * @return the X text label locations
	 */
	public synchronized Double[] getXTextLabelLocations() {
		return mXTextLabels.keySet().toArray(new Double[0]);
	}

	/**
	 * Clears the existing text labels on the X axis. 清除X轴上的文本标签
	 */
	public synchronized void clearXTextLabels() {
		mXTextLabels.clear();
	}

	/**
	 * If X axis labels should be rounded. X轴的标签应该是环形的
	 * 
	 * @return if rounded time values to be used
	 */
	public boolean isXRoundedLabels() {
		return mXRoundedLabels;
	}

	/**
	 * Sets if X axis rounded time values to be used. 设置X轴被使用的近似值
	 * 
	 * @param rounded
	 *            rounded values to be used
	 */
	public void setXRoundedLabels(boolean rounded) {
		mXRoundedLabels = rounded;
	}

	/**
	 * Adds a new text label for the specified Y axis value. 添加一个文本标签在Y轴指定的值位置
	 * 
	 * @param y
	 *            the Y axis value
	 * @param text
	 *            the text label
	 */
	public void addYTextLabel(double y, String text) {
		addYTextLabel(y, text, 0);
	}

	/**
	 * Removes text label for the specified Y axis value. 移除指定Y轴值的标签
	 * 
	 * @param y
	 *            the Y axis value
	 */
	public void removeYTextLabel(double y) {
		removeYTextLabel(y, 0);
	}

	/**
	 * Adds a new text label for the specified Y axis value. 在Y轴指定值得地方添加文本标签
	 * 
	 * @param y
	 *            the Y axis value
	 * @param text
	 *            the text label
	 * @param scale
	 *            the renderer scale
	 */
	public synchronized void addYTextLabel(double y, String text, int scale) {
		mYTextLabels.get(scale).put(y, text);
	}

	/**
	 * Removes text label for the specified Y axis value. 移除指定Y值得文本标签
	 * 
	 * @param y
	 *            the Y axis value
	 * @param scale
	 *            the renderer scale
	 */
	public synchronized void removeYTextLabel(double y, int scale) {
		mYTextLabels.get(scale).remove(y);
	}

	/**
	 * Returns the Y axis text label at the specified Y axis value.
	 * 返回在Y轴指定值的Y轴文本标签
	 * 
	 * @param y
	 *            the Y axis value
	 * @return the Y axis text label
	 */
	public String getYTextLabel(Double y) {
		return getYTextLabel(y, 0);
	}

	/**
	 * Returns the Y axis text label at the specified Y axis value.
	 * 返回Y轴指定Y轴值文本标签
	 * 
	 * @param y
	 *            the Y axis value
	 * @param scale
	 *            the renderer scale
	 * @return the Y axis text label
	 */
	public synchronized String getYTextLabel(Double y, int scale) {
		return mYTextLabels.get(scale).get(y);
	}

	/**
	 * Returns the Y text label locations. 返回Y轴文本的位置
	 * 
	 * @return the Y text label locations
	 */
	public Double[] getYTextLabelLocations() {
		return getYTextLabelLocations(0);
	}

	/**
	 * Returns the Y text label locations. 返回Y轴文本的位置---带缩放
	 * 
	 * @param scale
	 *            the renderer scale
	 * @return the Y text label locations
	 */
	public synchronized Double[] getYTextLabelLocations(int scale) {
		return mYTextLabels.get(scale).keySet().toArray(new Double[0]);
	}

	/**
	 * Clears the existing text labels on the Y axis. 清除Y轴存在的文本标签
	 */
	public void clearYTextLabels() {
		clearYTextLabels(0);
	}

	/**
	 * Clears the existing text labels on the Y axis. 清除Y轴存在的文本标签---带缩放
	 * 
	 * @param scale
	 *            the renderer scale
	 */
	public synchronized void clearYTextLabels(int scale) {
		mYTextLabels.get(scale).clear();
	}

	/**
	 * Returns the approximate number of labels for the Y axis. 返回Y轴的标签数量
	 * 
	 * @return the approximate number of labels for the Y axis
	 */
	public int getYLabels() {
		return mYLabels;
	}

	/**
	 * Sets the approximate number of labels for the Y axis. 为Y轴设置近似数量的标签。
	 * 
	 * @param yLabels
	 *            the approximate number of labels for the Y axis
	 */
	public void setYLabels(int yLabels) {
		mYLabels = yLabels;
	}

	/**
	 * Sets if the chart point values should be displayed as text.
	 * 设置图点值应该是否显示为文本。
	 * 
	 * @param display
	 *            if the chart point values should be displayed as text
	 */
	public void setDisplayChartValues(boolean display) {
		SimpleSeriesRenderer[] renderers = getSeriesRenderers();
		for (SimpleSeriesRenderer renderer : renderers) {
			renderer.setDisplayChartValues(display);
		}
	}

	/**
	 * Sets the chart values text size. 设置图表中文本字体大小
	 * 
	 * @param textSize
	 *            the chart values text size
	 */
	public void setChartValuesTextSize(float textSize) {
		SimpleSeriesRenderer[] renderers = getSeriesRenderers();
		for (SimpleSeriesRenderer renderer : renderers) {
			renderer.setChartValuesTextSize(textSize);
		}
	}

	/**
	 * Returns the enabled state of the pan on at least one axis. 返回盘的可用状态
	 * 
	 * @return if pan is enabled
	 */
	public boolean isPanEnabled() {
		return isPanXEnabled() || isPanYEnabled();
	}

	/**
	 * Returns the enabled state of the pan on X axis. 返回盘X轴的可用状态
	 * 
	 * @return if pan is enabled on X axis
	 */
	public boolean isPanXEnabled() {
		return mPanXEnabled;
	}

	/**
	 * Returns the enabled state of the pan on Y axis. 返回盘Y轴的可用状态
	 * 
	 * @return if pan is enabled on Y axis
	 */
	public boolean isPanYEnabled() {
		return mPanYEnabled;
	}

	/**
	 * Sets the enabled state of the pan. 设置X,Y轴的盘监听可用状态
	 * 
	 * @param enabledX
	 *            pan enabled on X axis
	 * @param enabledY
	 *            pan enabled on Y axis
	 */
	public void setPanEnabled(boolean enabledX, boolean enabledY) {
		mPanXEnabled = enabledX;
		mPanYEnabled = enabledY;
	}

	/**
	 * Override {@link DefaultRenderer#setPanEnabled(boolean)} so it can be
	 * delegated to {@link #setPanEnabled(boolean, boolean)}.设置盘监听可用状态
	 */
	@Override
	public void setPanEnabled(final boolean enabled) {
		setPanEnabled(enabled, enabled);
	}

	/**
	 * Returns the enabled state of the zoom on at least one axis.
	 * 返回启用状态的变焦在至少一个轴。
	 * 
	 * @return if zoom is enabled
	 */
	public boolean isZoomEnabled() {
		return isZoomXEnabled() || isZoomYEnabled();
	}

	/**
	 * Returns the enabled state of the zoom on X axis. 返回X轴缩放的可用状态
	 * 
	 * @return if zoom is enabled on X axis
	 */
	public boolean isZoomXEnabled() {
		return mZoomXEnabled;
	}

	/**
	 * Returns the enabled state of the zoom on Y axis. 返回Y轴缩放的可用状态
	 * 
	 * @return if zoom is enabled on Y axis
	 */
	public boolean isZoomYEnabled() {
		return mZoomYEnabled;
	}

	/**
	 * Sets the enabled state of the zoom. 设置缩放可用状态
	 * 
	 * @param enabledX
	 *            zoom enabled on X axis
	 * @param enabledY
	 *            zoom enabled on Y axis
	 */
	public void setZoomEnabled(boolean enabledX, boolean enabledY) {
		mZoomXEnabled = enabledX;
		mZoomYEnabled = enabledY;
	}

	/**
	 * Returns the spacing between bars, in bar charts. 返回柱状条间的间隔
	 * 
	 * @return the spacing between bars
	 */
	public double getBarSpacing() {
		return mBarSpacing;
	}

	/**
	 * Sets the spacing between bars, in bar charts. Only available for bar
	 * charts. This is a coefficient of the bar width. For instance, if you want
	 * the spacing to be a half of the bar width, set this value to 0.5.
	 * 设置柱状条中间的间隔
	 * 
	 * @param spacing
	 *            the spacing between bars coefficient
	 */
	public void setBarSpacing(double spacing) {
		mBarSpacing = spacing;
	}

	/**
	 * Returns the margins color. 返回边框颜色
	 * 
	 * @return the margins color
	 */
	public int getMarginsColor() {
		return mMarginsColor;
	}

	/**
	 * Sets the color of the margins. 设置边框颜色
	 * 
	 * @param color
	 *            the margins color
	 */
	public void setMarginsColor(int color) {
		mMarginsColor = color;
	}

	/**
	 * Returns the grid color. 返回网格颜色
	 * 
	 * @return the grid color
	 */
	public int getGridColor() {
		return mGridColor;
	}

	/**
	 * Sets the color of the grid. 设置网格颜色
	 * 
	 * @param color
	 *            the grid color
	 */
	public void setGridColor(int color) {
		mGridColor = color;
	}

	/**
	 * Returns the pan limits. 返回盘限制
	 * 
	 * @return the pan limits
	 */
	public double[] getPanLimits() {
		return mPanLimits;
	}

	/**
	 * Sets the pan limits as an array of 4 values. Setting it to null or a
	 * different size array will disable the panning limitation. Values:
	 * [panMinimumX, panMaximumX, panMinimumY, panMaximumY]
	 * 设置平移限制,设置null或不同大小的数组将禁用平移限制
	 * 
	 * @param panLimits
	 *            the pan limits 4个double值的数组
	 */
	public void setPanLimits(double[] panLimits) {
		mPanLimits = panLimits;
	}

	/**
	 * Returns the zoom limits. 返回缩放限制
	 * 
	 * @return the zoom limits
	 */
	public double[] getZoomLimits() {
		return mZoomLimits;
	}

	/**
	 * Sets the zoom limits as an array of 4 values. Setting it to null or a
	 * different size array will disable the zooming limitation. Values:
	 * [zoomMinimumX, zoomMaximumX, zoomMinimumY, zoomMaximumY]
	 * 设置缩放限制,X的最大值和最小值,Y的最大值和最小值
	 * 
	 * @param zoomLimits
	 *            the zoom limits
	 */
	public void setZoomLimits(double[] zoomLimits) {
		mZoomLimits = zoomLimits;
	}

	/**
	 * Returns the rotation angle of labels for the X axis. 返回X轴标签的转角
	 * 
	 * @return the rotation angle of labels for the X axis
	 */
	public float getXLabelsAngle() {
		return mXLabelsAngle;
	}

	/**
	 * Sets the rotation angle (in degrees) of labels for the X axis. 设置X轴标签的转角
	 * 
	 * @param angle
	 *            the rotation angle of labels for the X axis
	 */
	public void setXLabelsAngle(float angle) {
		mXLabelsAngle = angle;
	}

	/**
	 * Returns the rotation angle of labels for the Y axis. 返回Y轴标签的角度
	 * 
	 * @return the approximate number of labels for the Y axis
	 */
	public float getYLabelsAngle() {
		return mYLabelsAngle;
	}

	/**
	 * Sets the rotation angle (in degrees) of labels for the Y axis. 设置Y轴标签的角度
	 * 
	 * @param angle
	 *            the rotation angle of labels for the Y axis
	 */
	public void setYLabelsAngle(float angle) {
		mYLabelsAngle = angle;
	}

	/**
	 * Returns the size of the points, for charts displaying points. 返回图表显示点的大小
	 * 
	 * @return the point size
	 */
	public float getPointSize() {
		return mPointSize;
	}

	/**
	 * Sets the size of the points, for charts displaying points. 设置点的大小
	 * 
	 * @param size
	 *            the point size
	 */
	public void setPointSize(float size) {
		mPointSize = size;
	}

	/**
	 * 设置轴的范围值
	 * 
	 * @param range
	 */
	public void setRange(double[] range) {
		setRange(range, 0);
	}

	/**
	 * Sets the axes range values. 设置轴的范围值
	 * 
	 * @param range
	 *            an array having the values in this order: minX, maxX, minY,
	 *            maxY
	 * @param scale
	 *            the renderer scale
	 */
	public void setRange(double[] range, int scale) {
		setXAxisMin(range[0], scale);
		setXAxisMax(range[1], scale);
		setYAxisMin(range[2], scale);
		setYAxisMax(range[3], scale);
	}

	/**
	 * 是否已经设置初始化范围
	 * 
	 * @return
	 */
	public boolean isInitialRangeSet() {
		return isInitialRangeSet(0);
	}

	/**
	 * Returns if the initial range is set. 是否已经设置初始化范围
	 * 
	 * @param scale
	 *            the renderer scale
	 * @return the initial range was set or not
	 */
	public boolean isInitialRangeSet(int scale) {
		return initialRange.get(scale) != null;
	}

	/**
	 * Returns the initial range. 返回初始化范围
	 * 
	 * @return the initial range
	 */
	public double[] getInitialRange() {
		return getInitialRange(0);
	}

	/**
	 * Returns the initial range. 返回轴初始化范围
	 * 
	 * @param scale
	 *            缩放参数 the renderer scale
	 * @return the initial range
	 */
	public double[] getInitialRange(int scale) {
		return initialRange.get(scale);
	}

	/**
	 * Sets the axes initial range values. This will be used in the zoom fit
	 * tool. 设置轴初始范围值。这将用于变焦配合工具
	 * 
	 * @param range
	 *            an array having the values in this order: minX, maxX, minY,
	 *            maxY
	 */
	public void setInitialRange(double[] range) {
		setInitialRange(range, 0);
	}

	/**
	 * Sets the axes initial range values. This will be used in the zoom fit
	 * tool. 设置轴初始范围值。这将用于变焦配合工具
	 * 
	 * @param range
	 *            an array having the values in this order: minX, maxX, minY,
	 *            maxY
	 * @param scale缩放参数
	 *            the renderer scale
	 */
	public void setInitialRange(double[] range, int scale) {
		initialRange.put(scale, range);
	}

	/**
	 * Returns the X axis labels color. 返回X轴标签颜色
	 * 
	 * @return the X axis labels color
	 */
	public int getXLabelsColor() {
		return mXLabelsColor;
	}

	/**
	 * Returns the Y axis labels color. 返回Y轴标签颜色
	 * 
	 * @return the Y axis labels color
	 */
	public int getYLabelsColor(int scale) {
		return mYLabelsColor[scale];
	}

	/**
	 * Sets the X axis labels color. 设置X轴标签颜色
	 * 
	 * @param color
	 *            the X axis labels color
	 */
	public void setXLabelsColor(int color) {
		mXLabelsColor = color;
	}

	/**
	 * Sets the Y axis labels color. 设置Y轴标签颜色
	 * 
	 * @param scale
	 *            the renderer scale
	 * @param color
	 *            the Y axis labels color
	 */
	public void setYLabelsColor(int scale, int color) {
		mYLabelsColor[scale] = color;
	}

	/**
	 * Returns the X axis labels alignment. 返回X轴标签对齐属性
	 * 
	 * @return X labels alignment
	 */
	public Align getXLabelsAlign() {
		return xLabelsAlign;
	}

	/**
	 * Sets the X axis labels alignment. 设置X轴刻纹相对标签的对齐方式
	 * 
	 * @param align
	 *            the X labels alignment
	 */
	public void setXLabelsAlign(Align align) {
		xLabelsAlign = align;
	}

	/**
	 * Returns the Y axis labels alignment. 返回Y轴标签对齐方式 缩放
	 * 
	 * @param scale
	 *            缩放比率 the renderer scale
	 * @return Y labels alignment
	 */
	public Align getYLabelsAlign(int scale) {
		return yLabelsAlign[scale];
	}

	public void setYLabelsAlign(Align align) {
		setYLabelsAlign(align, 0);
	}

	public Align getYAxisAlign(int scale) {
		return yAxisAlign[scale];
	}

	public void setYAxisAlign(Align align, int scale) {
		yAxisAlign[scale] = align;
	}

	/**
	 * Sets the Y axis labels alignment. 设置Y轴标签对齐方式---缩放比率
	 * 
	 * @param align
	 *            the Y labels alignment
	 */
	public void setYLabelsAlign(Align align, int scale) {
		yLabelsAlign[scale] = align;
	}

	public int getScalesCount() {
		return scalesCount;
	}

	public void setScaleCount(int scale) {
		scalesCount = scale;
		initAxesRange(scale);
	}

	/**
	 * 设置标题
	 * 
	 * @param chartTitle
	 * @param xTitle
	 * @param yTitle
	 */
	public void setChartTitles(String chartTitle, String xTitle, String yTitle) {
		setChartTitle(chartTitle);
		setXTitle(xTitle);
		setYTitle(yTitle);
	}

	/**
	 * 设置限制
	 * 
	 * @param panLimit
	 * @param zoomLimit
	 */
	public void setChartLimit(double[] panLimit, double[] zoomLimit) {
		setPanLimits(panLimit);
		setZoomLimits(zoomLimit);
	}

	/**
	 * 设置坐标轴属性
	 * 
	 * @param xAxisMin
	 * @param xAxisMax
	 * @param yAxisMin
	 * @param yAxisMax
	 * @param axesColor
	 * @param labelColor
	 */
	public void setRendererAxes(double xAxisMin, double xAxisMax,
			double yAxisMin, double yAxisMax, int axesColor, int labelColor) {
		setXAxisMin(xAxisMin);
		setXAxisMax(xAxisMax);
		setYAxisMin(yAxisMin);
		setYAxisMax(yAxisMax);
		setAxesColor(axesColor);
		setLabelsColor(labelColor);
	}

	/**
	 * 设置标签相关的属性
	 * 
	 * @param renderer
	 * @param xLabelCount
	 * @param yLabelCount
	 * @param xAlign
	 * @param yAlign
	 * @param isShowGrid
	 * @param isZoomButtonVisible
	 */
	public void setRendererLabels(int xLabelCount, int yLabelCount,
			Align xAlign, Align yAlign, boolean isShowGrid,
			boolean isZoomButtonVisible) {
		setXLabels(xLabelCount);
		setYLabels(yLabelCount);
		setXLabelsAlign(xAlign);
		setYLabelsAlign(yAlign);
		setShowGrid(isShowGrid);
		setZoomButtonsVisible(isZoomButtonVisible);
	}

	/**
	 * 设置尺寸相关
	 * 
	 * @param axisTitleTextSize
	 * @param chartTitleTextSize
	 * @param labelTextSize
	 * @param legendTextSize
	 * @param pointSize
	 */
	public void setRendererSizes(int axisTitleTextSize, int chartTitleTextSize,
			int labelTextSize, int legendTextSize, float pointSize) {
		setAxisTitleTextSize(axisTitleTextSize);
		setChartTitleTextSize(chartTitleTextSize);
		setLabelsTextSize(labelTextSize);
		setLegendTextSize(legendTextSize);
		setPointSize(pointSize);
	}

	/**
	 * 设置颜色和点的风格
	 * 
	 * @param colors
	 * @param pointStyles
	 */
	public void setRendererColorAndPointStyle(int[] colors,
			PointStyle[] pointStyles) {
		for (int i = 0; i < getSeriesRendererCount(); i++) {
			XYSeriesRenderer r = (XYSeriesRenderer) getSeriesRendererAt(i);
			if (colors != null) {
				r.setColor(colors[i]);
			}
			if (pointStyles != null) {
				r.setPointStyle(pointStyles[i]);
			}
		}
	}

	/**
	 * 是否填充点
	 * 
	 * @param isFill
	 */
	public void setFillPoints(boolean isFill) {
		for (int i = 0; i < getSeriesRendererCount(); i++) {
			((XYSeriesRenderer) getSeriesRendererAt(i)).setFillPoints(isFill);
		}
	}

	/**
	 * 设置线的宽度
	 * 
	 * @param lineWidth
	 */
	public void setLineWidth(int lineWidth) {
		for (int i = 0; i < getSeriesRendererCount(); i++) {
			((XYSeriesRenderer) getSeriesRendererAt(i)).setLineWidth(lineWidth);
		}
	}
}
