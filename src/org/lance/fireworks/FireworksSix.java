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
public class FireworksSix extends Fireworks {

	public FireworksSix(Context context, int color, int endX, int entY) {
		super(context, color, endX, entY);
		pace = 20;
	}

	@Override
	public LittleFireworks[] blast() {
		int j = -1;
		for (int i = 0; i < fires.length; i++) {
			j = -j;
			fires[i].x += (fires[i].x - endPoint.x) / 4 + j;
			fires[i].y += (fires[i].y - endPoint.y) / 4 + 1;
		}
		return fires;
	}

	@Override
	public LittleFireworks[] initBlast() {
		// 初始化粒子 只要分成20
		Random rand = new Random();
		for (int i = 0; i < fires.length; i++) {
			int x = rand.nextInt(20);
			int y = rand.nextInt(20);
			if (Math.random() < 0.5) {
				x = -x;
			}
			if (Math.random() < 0.5) {
				y = -y;
			}
			if (x * x + y * y > 400) {
				x -= x / Math.abs(x) * 10;
				y -= y / Math.abs(y) * 10;
			}
			fires[i] = new LittleFireworks(x + endPoint.x, y + endPoint.y,
					color);
		}
		return fires;
	}

	public void draw(Canvas canvas, Vector<Fireworks> lList) {
		Paint mPaint = new Paint();
		RectF oval = new RectF(x, y, x + size, y + size);
		if (state == 1) {
			mPaint.setColor(color);
			if (mFireAnim != null) {
				mFireAnim.drawAnimation(canvas, null, x, y);
			}
		} else if (state == 2) {
			mPaint.setColor(color);
			canvas.drawOval(oval, mPaint);
			LittleFireworks[] fires2 = initBlast();
			fires = new LittleFireworks[fires2.length];
			fires = fires2;
			for (int i = 0; i < fires.length; i++) {
				mPaint.setColor(fires[i].color);
				canvas.drawLine(fires[i].x, fires[i].y, fires[i].x
						+ (fires[i].x - endPoint.x) / 2, fires[i].y
						+ (fires[i].y - endPoint.y) / 2, mPaint);
			}
			state = 3;
		} else if (state == 3) {
			if (circle < maxCircle) {
				circle++;
				this.fires = blast();
				for (int i = 0; i < fires.length; i++) {
					mPaint.setColor(fires[i].color);
					canvas.drawLine(fires[i].x, fires[i].y, fires[i].x
							+ (fires[i].x - endPoint.x) / circle, fires[i].y
							+ (fires[i].y - endPoint.y) / circle, mPaint);
				}
			} else {
				for (int i = 0; i < fires.length; i++) {
					mPaint.setColor(fires[i].color);
					mPaint.setAlpha(20 + (int) (Math.random() * 0xff));
					canvas.drawLine(fires[i].x, fires[i].y, fires[i].x
							+ (fires[i].x - endPoint.x) / circle, fires[i].y
							+ (fires[i].y - endPoint.y) / circle, mPaint);
				}
			}
		} else if (state == 4) {
			synchronized (lList) {
				lList.remove(this);
			}
		}
	}
}
