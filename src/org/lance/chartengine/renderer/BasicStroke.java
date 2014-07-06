package org.lance.chartengine.renderer;

import java.io.Serializable;

import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;

/**
 * 一个轻抚风格的描述符
 * 
 * @author lance
 * 
 */
public class BasicStroke implements Serializable {
	/** The solid line style. */
	public static final BasicStroke SOLID = new BasicStroke(Cap.BUTT,
			Join.MITER, 4, null, 0);
	/** The dashed line style. */
	public static final BasicStroke DASHED = new BasicStroke(Cap.ROUND,
			Join.BEVEL, 10, new float[] { 10, 10 }, 1);
	/** The dot line style. */
	public static final BasicStroke DOTTED = new BasicStroke(Cap.ROUND,
			Join.BEVEL, 5, new float[] { 2, 10 }, 1);
	/** The stroke cap. */
	private Cap mCap;
	/** The stroke join. */
	private Join mJoin;
	/** The stroke miter. */
	private float mMiter;
	/** The path effect intervals. */
	private float[] mIntervals;
	/** The path effect phase. */
	private float mPhase;

	/**
	 * Build a new basic stroke style. 创建一个基础的笔画风格
	 * 
	 * @param cap
	 *            the stroke cap
	 * @param join
	 *            the stroke join
	 * @param miter
	 *            the stroke miter
	 * @param intervals
	 *            the path effect intervals
	 * @param phase
	 *            the path effect phase
	 */
	public BasicStroke(Cap cap, Join join, float miter, float[] intervals,
			float phase) {
		mCap = cap;
		mJoin = join;
		mMiter = miter;
		mIntervals = intervals;
	}

	/**
	 * Returns the stroke cap. 返回笔画帽
	 * 
	 * @return the stroke cap
	 */
	public Cap getCap() {
		return mCap;
	}

	/**
	 * Returns the stroke join. 返回笔画加入
	 * 
	 * @return the stroke join
	 */
	public Join getJoin() {
		return mJoin;
	}

	/**
	 * Returns the stroke miter. 返回笔画的斜度
	 * 
	 * @return the stroke miter
	 */
	public float getMiter() {
		return mMiter;
	}

	/**
	 * Returns the path effect intervals. 获取间隔
	 * 
	 * @return the path effect intervals
	 */
	public float[] getIntervals() {
		return mIntervals;
	}

	/**
	 * Returns the path effect phase. 获取阶段
	 * 
	 * @return the path effect phase
	 */
	public float getPhase() {
		return mPhase;
	}

}
