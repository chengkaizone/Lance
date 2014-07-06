package org.lance.fireworks;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 烟花动画
 * 
 * @author lance
 * 
 */
public class FireworksAnimFW extends Fireworks {

	FireworksAnimation mFWAnim = null;
	private List<int[]> ids;

	public FireworksAnimFW(Context context, List<int[]> ids, int color,
			int endX, int endY) {
		super(context, color, endX, endY);
		this.ids = ids;
		new loadAnim().start();
	}

	class loadAnim extends Thread {
		public void run() {
			Random random = new Random();
			int length = ids.size();
			// 获取长度的随机数
			int num = random.nextInt(length);
			for (int i = 0; i < length; i++) {
				if (i == num) {
					mFWAnim = new FireworksAnimation(mContext, ids.get(i),
							false);
				}
			}
			//
			// switch (random.nextInt(5)) {
			// case 0:
			// mFWAnim = new FireworksAnimation(mContext, new int[] {
			// R.drawable.fw_01,
			// R.drawable.fw_02, R.drawable.fw_03, R.drawable.fw_04,
			// R.drawable.fw_05, R.drawable.fw_06, R.drawable.fw_07,
			// R.drawable.fw_08, R.drawable.fw_09, R.drawable.fw_10,
			// R.drawable.fw_11, R.drawable.fw_12, R.drawable.fw_13 },
			// false);
			// break;
			// case 1:
			// mFWAnim = new FireworksAnimation(mContext,
			// new int[] { R.drawable.fw2_01, R.drawable.fw2_02,
			// R.drawable.fw2_03, R.drawable.fw2_04,
			// R.drawable.fw2_07, R.drawable.fw2_08,
			// R.drawable.fw2_05, R.drawable.fw2_06,
			// R.drawable.fw2_09, R.drawable.fw2_10,
			// R.drawable.fw2_11 }, false);
			// break;
			// case 2:
			// mFWAnim = new FireworksAnimation(mContext, new int[] {
			// R.drawable.fw3_01, R.drawable.fw3_02,
			// R.drawable.fw3_03, R.drawable.fw3_04,
			// R.drawable.fw3_05, R.drawable.fw3_06,
			// R.drawable.fw3_07, R.drawable.fw3_08,
			// R.drawable.fw3_09, R.drawable.fw3_10,
			// R.drawable.fw3_11, R.drawable.fw3_12,
			// R.drawable.fw3_13, R.drawable.fw3_14 }, false);
			// break;
			// case 3:
			// mFWAnim = new FireworksAnimation(mContext,
			// new int[] { R.drawable.fw4_01, R.drawable.fw4_02,
			// R.drawable.fw4_03, R.drawable.fw4_04,
			// R.drawable.fw4_05, R.drawable.fw4_06,
			// R.drawable.fw4_07, R.drawable.fw4_08,
			// R.drawable.fw4_09, R.drawable.fw4_10,
			// R.drawable.fw4_11, R.drawable.fw4_12,
			// R.drawable.fw4_13 }, false);
			// break;
			// case 4:
			// mFWAnim = new FireworksAnimation(mContext,
			// new int[] { R.drawable.fw5_01, R.drawable.fw5_02,
			// R.drawable.fw5_03, R.drawable.fw5_04,
			// R.drawable.fw5_05, R.drawable.fw5_06,
			// R.drawable.fw5_07, R.drawable.fw5_08,
			// R.drawable.fw5_09, R.drawable.fw5_10,
			// R.drawable.fw5_11, R.drawable.fw5_12,
			// R.drawable.fw5_13, R.drawable.fw5_14,
			// R.drawable.fw5_15 }, false);
			// default:
			// mFWAnim = new FireworksAnimation(mContext,
			// new int[] { R.drawable.fw5_01, R.drawable.fw5_02,
			// R.drawable.fw5_03, R.drawable.fw5_04,
			// R.drawable.fw5_05, R.drawable.fw5_06,
			// R.drawable.fw5_07, R.drawable.fw5_08,
			// R.drawable.fw5_09, R.drawable.fw5_10,
			// R.drawable.fw5_11, R.drawable.fw5_12,
			// R.drawable.fw5_13, R.drawable.fw5_14,
			// R.drawable.fw5_15 }, false);
			// break;
			// }
		}
	}

	@Override
	public LittleFireworks[] initBlast() {
		return null;
	}

	@Override
	public LittleFireworks[] blast() {
		return null;
	}

	public void draw(Canvas canvas, Vector<Fireworks> fList) {
		Paint mPaint = new Paint();
		mPaint.setColor(color);
		if (state == 1) {
			if (mFireAnim != null) {
				mFireAnim.drawAnimation(canvas, null, x, y);
			}
		}
		if (state == 2) {
			state = 3;
		} else if (state == 3) {
			if (mFWAnim != null) {
				if (mFWAnim.isEnd() == false) {
					mFWAnim.drawAnimation(canvas, null, x, y);
				} else {
					circle = 100;
				}
			}
		} else if (state == 4) {
			synchronized (fList) {
				fList.remove(this);
			}
		}
	}
}
