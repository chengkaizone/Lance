package org.lance.fireworks;

import java.util.Random;

import android.content.Context;

/**
 * 风格烟花
 * 
 * @author lance
 * 
 */
public class FireworksFour extends Fireworks {

	public FireworksFour(Context context, int color, int endX, int entY) {
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
			}
		}
		color = 0xff000000 | 225 << 16 | 203 << 8 | 114;
		size = 10;
		state = 3;
		return fires;
	}

	@Override
	public LittleFireworks[] blast() {
		if (circle >= maxCircle - 1) {
			for (int i = 0; i < fires.length; i++) {
				// 前三圈按照下面的代码来计算爆炸点的碎花的位置
				fires[i].setPlace();
			}
		} else {
			for (int i = 0; i < fires.length; i++) {
				// 三圈后就碎花就开始下坠
				fires[i].setPace(fires[i].horizontal, GRAVITY);
				fires[i].setPlace();
			}
		}
		if (circle >= maxCircle) {
			for (int i = 0; i < fires.length; i++) {
				int red = (int) (Math.random() * 255);
				int green = (int) (Math.random() * 255);
				int blue = (int) (Math.random() * 255);
				int col = 0xff000000 | red << 16 | green << 8 | blue;
				fires[i].color = col;
			}
		}
		return fires;
	}
}
