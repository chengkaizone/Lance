package org.lance.chartengine.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 算术工具操作类
 * 
 * @author lance
 * 
 */
public class MathHelper {
	/** A value that is used a null value. */
	public static final double NULL_VALUE = Double.MAX_VALUE;
	/**
	 * A number formatter to be used to make sure we have a maximum number of
	 * fraction digits in the labels.
	 */
	private static final NumberFormat FORMAT = NumberFormat.getNumberInstance();

	private MathHelper() {
		// empty constructor
	}

	/**
	 * Calculate the minimum and maximum values out of a list of doubles.
	 * 计算数组集合的最小值和最大值
	 * 
	 * @param values
	 *            the input values
	 * @return an array with the minimum and maximum values
	 */
	public static double[] minmax(List<Double> values) {
		if (values.size() == 0) {
			return new double[2];
		}
		double min = values.get(0);
		double max = min;
		int length = values.size();
		for (int i = 1; i < length; i++) {
			double value = values.get(i);
			min = Math.min(min, value);
			max = Math.max(max, value);
		}
		return new double[] { min, max };
	}

	/**
	 * 计算一个数组中的最大值和最小值
	 * 
	 * @param values
	 * @return
	 */
	public static double[] minmax(double[] values) {
		if (values.length == 0) {
			return new double[2];
		}
		double min = values[0];
		double max = min;
		for (int i = 1; i < values.length; i++) {
			double value = values[i];
			min = Math.min(min, value);
			max = Math.max(max, value);
		}
		return new double[] { min, max };
	}

	/**
	 * Computes a reasonable set of labels for a data interval and number of
	 * labels. 使用数据间隔和标签数量计算一个合理的标签集合
	 * 
	 * @param start
	 *            start value
	 * @param end
	 *            final value
	 * @param approxNumLabels
	 *            desired number of labels
	 * @return collection containing {start value, end value, increment}
	 */
	public static List<Double> getLabels(final double start, final double end,
			final int approxNumLabels) {
		FORMAT.setMaximumFractionDigits(5);
		List<Double> labels = new ArrayList<Double>();
		double[] labelParams = computeLabels(start, end, approxNumLabels);
		// when the start > end the inc will be negative so it will still work
		int numLabels = 1 + (int) ((labelParams[1] - labelParams[0]) / labelParams[2]);
		// we want the range to be inclusive but we don't want to blow up when
		// looping for the case where the min and max are the same. So we loop
		// on
		// numLabels not on the values.
		for (int i = 0; i < numLabels; i++) {
			double z = labelParams[0] + i * labelParams[2];
			try {
				// this way, we avoid a label value like 0.4000000000000000001
				// instead
				// of 0.4
				z = FORMAT.parse(FORMAT.format(z)).doubleValue();
			} catch (ParseException e) {
				// do nothing here
			}
			labels.add(z);
		}
		return labels;
	}

	/**
	 * Computes a reasonable number of labels for a data range.
	 * 通过一个数据范围计算一个合理数量的标签
	 * 
	 * @param start
	 *            start value
	 * @param end
	 *            final value
	 * @param approxNumLabels
	 *            desired number of labels
	 * @return double[] array containing {start value, end value, increment}
	 */
	private static double[] computeLabels(final double start, final double end,
			final int approxNumLabels) {
		if (Math.abs(start - end) < 0.0000001f) {
			return new double[] { start, start, 0 };
		}
		double s = start;
		double e = end;
		boolean switched = false;
		if (s > e) {
			switched = true;
			double tmp = s;
			s = e;
			e = tmp;
		}
		double xStep = roundUp(Math.abs(s - e) / approxNumLabels);
		// Compute x starting point so it is a multiple of xStep.
		double xStart = xStep * Math.ceil(s / xStep);
		double xEnd = xStep * Math.floor(e / xStep);
		if (switched) {
			return new double[] { xEnd, xStart, -1.0 * xStep };
		}
		return new double[] { xStart, xEnd, xStep };
	}

	/**
	 * Given a number, round up to the nearest power of ten times 1, 2, or 5.
	 * The argument must be strictly positive. 四舍五入到一个最接近10的倍数,参数必须严格
	 * 
	 * @param val
	 * @return
	 */
	private static double roundUp(final double val) {
		int exponent = (int) Math.floor(Math.log10(val));
		double rval = val * Math.pow(10, -exponent);
		if (rval > 5.0) {
			rval = 10.0;
		} else if (rval > 2.0) {
			rval = 5.0;
		} else if (rval > 1.0) {
			rval = 2.0;
		}
		rval *= Math.pow(10, exponent);
		return rval;
	}

	/**
	 * Transforms a list of Float values into an array of float. 转换集合为一个数组
	 * 
	 * @param values
	 *            the list of Float
	 * @return the array of floats
	 */
	public static float[] getFloats(List<Float> values) {
		int length = values.size();
		float[] result = new float[length];
		for (int i = 0; i < length; i++) {
			result[i] = values.get(i).floatValue();
		}
		return result;
	}

	/**
	 * Transforms a list of Double values into an array of double. 转换集合为一个数组
	 * 
	 * @param values
	 *            the list of Double
	 * @return the array of doubles
	 */
	public static double[] getDoubles(List<Double> values) {
		int length = values.size();
		double[] result = new double[length];
		for (int i = 0; i < length; i++) {
			result[i] = values.get(i).doubleValue();
		}
		return result;
	}

}
