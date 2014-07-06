package org.lance.fireworks;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 处理烟花动画
 * 
 * @author lance
 * 
 */
public class FireworksAnimation {

	/** 上一帧播放时间 **/
	private long mLastPlayTime = 0;
	/** 播放当前帧的ID **/
	private int mPlayID = 0;
	/** 动画frame数量 **/
	private int mFrameCount = 0;
	/** 用于储存动画资源图片 **/
	private Bitmap[] mFrameBitmap = null;
	/** 是否循环播放 **/
	private boolean isLoop = false;
	/** 播放结束 **/
	private boolean isEnd = false;
	/** 动画播放间隙时间 **/
	private static final int ANIM_STEP = 100;

	/**
	 * 构造函数
	 * 
	 * @param context
	 * @param frameBitmapID
	 * @param isloop
	 */
	public FireworksAnimation(Context context, int[] frameBitmapID,
			boolean isloop) {
		mFrameCount = frameBitmapID.length;
		mFrameBitmap = new Bitmap[mFrameCount];
		for (int i = 0; i < mFrameCount; i++) {
			mFrameBitmap[i] = loadBitmap(context, frameBitmapID[i]);
		}
		this.isLoop = isloop;
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 * @param frameBitmap
	 * @param isloop
	 */
	public FireworksAnimation(Context context, Bitmap[] frameBitmap,
			boolean isloop) {
		mFrameCount = frameBitmap.length;
		mFrameBitmap = frameBitmap;
		this.isLoop = isloop;
	}

	/**
	 * 绘制动画中的其中一帧
	 * 
	 * @param Canvas
	 * @param paint
	 * @param x
	 * @param y
	 * @param frameID
	 */
	public void drawFrame(Canvas canvas, Paint paint, int x, int y, int frameID) {
		canvas.drawBitmap(mFrameBitmap[frameID], x, y, paint);
	}

	/**
	 * 绘制动画
	 * 
	 * @param Canvas
	 * @param paint
	 * @param x
	 * @param y
	 */
	public void drawAnimation(Canvas canvas, Paint paint, int x, int y) {
		// 如果没有播放结束则继续播放
		if (!isEnd) {
			canvas.drawBitmap(mFrameBitmap[mPlayID],
					x - mFrameBitmap[mPlayID].getWidth() / 2, y
							- mFrameBitmap[mPlayID].getHeight() / 2, paint);
			long time = System.currentTimeMillis();
			if (time - mLastPlayTime > ANIM_STEP) {
				mPlayID++;
				mLastPlayTime = time;
				if (mPlayID >= mFrameCount) {
					// 标志动画播放结束
					isEnd = true;
					if (isLoop) {
						// 设置循环播放
						isEnd = false;
						mPlayID = 0;
					}
				}
			}
		}
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	/**
	 * 读取图片资源
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public Bitmap loadBitmap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
}
