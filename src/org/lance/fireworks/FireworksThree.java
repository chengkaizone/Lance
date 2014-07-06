package org.lance.fireworks;

import java.util.Random;

import android.content.Context;

/**
 * ����̻�
 * 
 * @author lance
 * 
 */
public class FireworksThree extends Fireworks {

	public FireworksThree(Context context, int color, int endX, int entY) {
		super(context, color, endX, entY);
		pace = 20; // �����ٶȣ�ÿ���̻����ָ����ͬ�������ٶ�
	}

	@Override
	public LittleFireworks[] initBlast() {
		// ��ʼ����ը�����
		final int ONE = 20; // ���Ǳ�ը�ĵ�һȦ�İ뾶
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
		final int EVERY = 40;// ��ը��ÿȦ�İ뾶
		Random rand = new Random();
		if (circle >= maxCircle) {
			for (int i = 0; i < fires.length; i++) {
				int red = (int) (Math.random() * 255);
				int green = (int) (Math.random() * 255);
				int blue = (int) (Math.random() * 255);
				int col = 0xff000000 | red << 16 | green << 8 | blue;
				fires[i].color = col;
			}
		}
		for (int i = 0; i < fires.length; i++) {
			fires[i].x += rand.nextInt(EVERY) - EVERY / 2;
			fires[i].y += rand.nextInt(EVERY) - EVERY / 2;

		}
		return fires;
	}

}
