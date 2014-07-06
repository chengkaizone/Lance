package org.lance.fireworks;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * �����̻�����
 * 
 * @author lance
 * 
 */
public class FireworksAnimation {

	/** ��һ֡����ʱ�� **/
	private long mLastPlayTime = 0;
	/** ���ŵ�ǰ֡��ID **/
	private int mPlayID = 0;
	/** ����frame���� **/
	private int mFrameCount = 0;
	/** ���ڴ��涯����ԴͼƬ **/
	private Bitmap[] mFrameBitmap = null;
	/** �Ƿ�ѭ������ **/
	private boolean isLoop = false;
	/** ���Ž��� **/
	private boolean isEnd = false;
	/** �������ż�϶ʱ�� **/
	private static final int ANIM_STEP = 100;

	/**
	 * ���캯��
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
	 * ���캯��
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
	 * ���ƶ����е�����һ֡
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
	 * ���ƶ���
	 * 
	 * @param Canvas
	 * @param paint
	 * @param x
	 * @param y
	 */
	public void drawAnimation(Canvas canvas, Paint paint, int x, int y) {
		// ���û�в��Ž������������
		if (!isEnd) {
			canvas.drawBitmap(mFrameBitmap[mPlayID],
					x - mFrameBitmap[mPlayID].getWidth() / 2, y
							- mFrameBitmap[mPlayID].getHeight() / 2, paint);
			long time = System.currentTimeMillis();
			if (time - mLastPlayTime > ANIM_STEP) {
				mPlayID++;
				mLastPlayTime = time;
				if (mPlayID >= mFrameCount) {
					// ��־�������Ž���
					isEnd = true;
					if (isLoop) {
						// ����ѭ������
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
	 * ��ȡͼƬ��Դ
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
		// ��ȡ��ԴͼƬ
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
}
