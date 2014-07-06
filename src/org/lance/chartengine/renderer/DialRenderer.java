package org.lance.chartengine.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lance.chartengine.util.MathHelper;

/**
 * 拨号图表渲染类
 * 
 * @author lance
 * 
 */
public class DialRenderer extends DefaultRenderer {
	/** The start angle in the dial range. */
	private double mAngleMin = 330;
	/** The end angle in the dial range. */
	private double mAngleMax = 30;
	/** The start value in dial range. */
	private double mMinValue = MathHelper.NULL_VALUE;
	/** The end value in dial range. */
	private double mMaxValue = -MathHelper.NULL_VALUE;
	/** The spacing for the minor ticks. */
	private double mMinorTickSpacing = MathHelper.NULL_VALUE;
	/** The spacing for the major ticks. */
	private double mMajorTickSpacing = MathHelper.NULL_VALUE;
	/** An array of the renderers types (default is NEEDLE). */
	private List<Type> mVisualTypes = new ArrayList<Type>();

	public enum Type {// 针,箭头
		NEEDLE, ARROW;
	}

	/**
	 * Returns the start angle value of the dial. 返回刻度盘的起始角度
	 * 
	 * @return the angle start value
	 */
	public double getAngleMin() {
		return mAngleMin;
	}

	/**
	 * Sets the start angle value of the dial. 设置刻度盘的起始角度
	 * 
	 * @param min
	 *            the dial angle start value
	 */
	public void setAngleMin(double min) {
		mAngleMin = min;
	}

	/**
	 * Returns the end angle value of the dial. 返回刻度盘的最大角度
	 * 
	 * @return the angle end value
	 */
	public double getAngleMax() {
		return mAngleMax;
	}

	/**
	 * Sets the end angle value of the dial. 设置刻度盘的最大角度
	 * 
	 * @param max
	 *            the dial angle end value
	 */
	public void setAngleMax(double max) {
		mAngleMax = max;
	}

	/**
	 * Returns the start value to be rendered on the dial. 返回刻度盘的最小值
	 * 
	 * @return the start value on dial
	 */
	public double getMinValue() {
		return mMinValue;
	}

	/**
	 * Sets the start value to be rendered on the dial. 设置刻度盘的最小值
	 * 
	 * @param min
	 *            the start value on the dial
	 */
	public void setMinValue(double min) {
		mMinValue = min;
	}

	/**
	 * Returns if the minimum dial value was set. 是否设置了刻度盘的最小值
	 * 
	 * @return the minimum dial value was set or not
	 */
	public boolean isMinValueSet() {
		return mMinValue != MathHelper.NULL_VALUE;
	}

	/**
	 * Returns the end value to be rendered on the dial. 返回刻度盘的最大值
	 * 
	 * @return the end value on the dial
	 */
	public double getMaxValue() {
		return mMaxValue;
	}

	/**
	 * Sets the end value to be rendered on the dial. 设置刻度盘的最大值
	 * 
	 * @param max
	 *            the end value on the dial
	 */
	public void setMaxValue(double max) {
		mMaxValue = max;
	}

	/**
	 * Returns if the maximum dial value was set. 返回已经设置的最大刻度盘值
	 * 
	 * @return the maximum dial was set or not
	 */
	public boolean isMaxValueSet() {
		return mMaxValue != -MathHelper.NULL_VALUE;
	}

	/**
	 * Returns the minor ticks spacing. 返回次要的交叉间隙
	 * 
	 * @return the minor ticks spacing
	 */
	public double getMinorTicksSpacing() {
		return mMinorTickSpacing;
	}

	/**
	 * Sets the minor ticks spacing. 设置次要的交叉间隙
	 * 
	 * @param spacing
	 *            the minor ticks spacing
	 */
	public void setMinorTicksSpacing(double spacing) {
		mMinorTickSpacing = spacing;
	}

	/**
	 * Returns the major ticks spacing. 返回主要的交叉间隙
	 * 
	 * @return the major ticks spacing
	 */
	public double getMajorTicksSpacing() {
		return mMajorTickSpacing;
	}

	/**
	 * Sets the major ticks spacing. 这是主要的交叉间隙
	 * 
	 * @param spacing
	 *            the major ticks spacing
	 */
	public void setMajorTicksSpacing(double spacing) {
		mMajorTickSpacing = spacing;
	}

	/**
	 * Returns the visual type at the specified index. 返回指定索引的视觉类型
	 * 
	 * @param index
	 *            the index
	 * @return the visual type
	 */
	public Type getVisualTypeForIndex(int index) {
		if (index < mVisualTypes.size()) {
			return mVisualTypes.get(index);
		}
		return Type.NEEDLE;
	}

	/**
	 * Sets the visual types. 设置视觉类型
	 * 
	 * @param types
	 *            the visual types
	 */
	public void setVisualTypes(Type[] types) {
		mVisualTypes.clear();
		mVisualTypes.addAll(Arrays.asList(types));
	}

}
