package org.lance.fireworks;

import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * 风格烟花
 * 
 * @author lance
 * 
 */
public class FireworksTwo extends Fireworks {

	public FireworksTwo(Context context, int color, int endX, int entY) {
		super(context, color, endX, entY);
		pace = 20;
	}

	@Override
	public LittleFireworks[] initBlast() {
		final int ONE = 20; // 这是爆炸的第一圈的半径

		int red = (int) (Math.random() * 255);
		int green = (int) (Math.random() * 255);
		int blue = (int) (Math.random() * 255);

		int col = 0xff000000 | red << 16 | green << 8 | blue;

		Random rand = new Random();
		int blastX = endPoint.x - ONE * circle;
		int blastY = endPoint.y - ONE * circle;

		if (Math.random() < 0.3) {
			for (int i = 0; i < fires.length; i++) {
				fires[i] = new LittleFireworks(rand.nextInt(ONE * circle * 2)
						+ blastX, rand.nextInt(ONE * circle * 2) + blastY, col);
				fires[i].setPace(WALLOP, endPoint.x, endPoint.y);
			}
		} else {
			for (int i = 0; i < fires.length; i++) {
				fires[i] = new LittleFireworks(rand.nextInt(ONE * circle * 2)
						+ blastX, rand.nextInt(ONE * circle * 2) + blastY,
						color);
				fires[i].setPace(WALLOP, endPoint.x, endPoint.y);
			}// for
		}

		color = 0xff000000 | 225 << 16 | 203 << 8 | 114;
		size = 10;
		state = 3;
		return fires;
	}

	@Override
	public LittleFireworks[] blast() {
		final int EVERY = 25;// 爆炸的每圈的半径
		Random rand = new Random();
		for (int i = 0; i < fires.length; i++) {
			// 前面最大圈数的一半按照下面的代码来计算爆炸点的碎花的位置
			if (circle <= maxCircle / 2) {
				if (fires[i].x < endPoint.x) {
					fires[i].x -= rand.nextInt(EVERY);
				} else {
					fires[i].x += rand.nextInt(EVERY);
				}
				if (fires[i].y < endPoint.y) {
					fires[i].y -= rand.nextInt(EVERY);
				} else {
					fires[i].y += rand.nextInt(EVERY);
				}
			}
			// 后面的就碎花就开始下坠
			else {
				if (fires[i].x < endPoint.x) {
					fires[i].x -= rand.nextInt(EVERY);
				} else {
					fires[i].x += rand.nextInt(EVERY);
				}
				fires[i].y += rand.nextInt(EVERY);
			}
		}
		return fires;
	}

	public void draw(Canvas canvas, Vector<Fireworks> fList) {
		Paint mPaint = new Paint();
		mPaint.setColor(color);
		RectF oval = new RectF(x, y, x + size, y + size);
		if (state == 1) {
			if (mFireAnim != null) {
				mFireAnim.drawAnimation(canvas, mPaint, x, y);
			}
		}
		if (state == 2) {
			canvas.drawOval(oval, mPaint);
			LittleFireworks[] fires2 = initBlast();
			fires = new LittleFireworks[fires2.length];
			fires = fires2;
			for (int i = 0; i < fires.length / 4; i++) {
				mPaint.setColor(fires[i].color);
				oval = new RectF(fires[i].x, fires[i].y, fires[i].x + 2,
						fires[i].y + 2);
				canvas.drawOval(oval, mPaint);
			}
			state = 3;
		} else if (state == 3) {
			if (circle < 5) {
				circle++;
				this.fires = blast();
				for (int i = 0; i < fires.length; i++) {
					mPaint.setColor(fires[i].color);
					mPaint.setAlpha(1000);
					oval = new RectF(fires[i].x, fires[i].y, fires[i].x + 2,
							fires[i].y + 2);
					canvas.drawOval(oval, mPaint);
				}
			} else {
				// 滞留在空中，并且闪烁
				for (int i = 0; i < fires.length; i++) {
					mPaint.setColor(fires[i].color);
					mPaint.setAlpha(20 + (int) (Math.random() * 0xff));
					oval = new RectF(fires[i].x, fires[i].y, fires[i].x + 2,
							fires[i].y + 2);
					canvas.drawOval(oval, mPaint);
				}
			}
		} else if (state == 4) {
			synchronized (fList) {
				fList.remove(this);
			}
		}
	}
}
