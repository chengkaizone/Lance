package org.lance.fireworks;

import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 此类为爆炸后会画线的式样
 * 
 * @author lance
 * 
 */
public class FireworksOne extends Fireworks {

	public FireworksOne(Context context, int color, int endX, int entY) {
		super(context, color, endX, entY);
		pace = 20;
	}

	@Override
	public LittleFireworks[] initBlast() {
		// 初始化爆炸的情况
		final int ONE = 20; // 这是爆炸的第一圈的半径
		int red = (int) (Math.random() * 255);
		int green = (int) (Math.random() * 255);
		int blue = (int) (Math.random() * 255);
		int col = 0xff000000 | red << 16 | green << 8 | blue;
		Random rand = new Random();
		int blastX = endPoint.x - ONE * circle;
		int blastY = endPoint.y - ONE * circle;
		if (Math.random() < 0.5) {
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
			}
		}

		color = 0xff000000 | 225 << 16 | 203 << 8 | 114;
		size = 10;
		state = 3;
		return fires;
	}

	@Override
	public LittleFireworks[] blast() {
		// 处理爆炸的情况
		if (circle <= 4) {
			for (int i = 0; i < fires.length; i++) {
				fires[i].setPlace();
			}
		}
		return fires;
	}

	public void draw(Canvas canvas, Vector<Fireworks> lList) {
		super.draw(canvas, lList);
		Paint mPaint = new Paint();
		int col = 0xff000000 | 220 << 16 | 220 << 8 | 220;
		if (this.state == 3) {
			if (Math.random() < 0.5) {
				mPaint.setColor(col);
			} else {
				mPaint.setColor(color);
			}
			for (int i = 0; i < fires.length; i++) {
				canvas.drawLine(x, y, fires[i].x, fires[i].y, mPaint);
			}
		}
	}
}
