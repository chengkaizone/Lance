package org.lance.fireworks;

import android.content.Context;
import android.graphics.Color;

/**
 * 风格烟花
 * 
 * @author lance
 * 
 */
public class FireworksFive extends Fireworks {

	public FireworksFive(Context context, int color, int endX, int entY) {
		super(context, color, endX, entY);
		pace = 20;
	}

	public double qiu(int data1, int data2) {
		int xx = Math.abs(data1 - endPoint.x);
		int yy = Math.abs(data2 - endPoint.y);
		return Math.sqrt(xx * xx + yy * yy);
	}

	@Override
	public LittleFireworks[] blast() {
		for (int i = 0; i < fires.length; i++) {
			int lineLong = (int) qiu(fires[i].x, fires[i].y) / 3;

			int level;

			if (fires[i].x <= endPoint.x) {
				if (fires[i].y <= endPoint.y) {
					level = 1;
				} else {
					level = 2;
				}
			} else {
				if (fires[i].y <= endPoint.y) {
					level = 3;
				} else {
					level = 4;
				}
			}

			switch (level) {
			case 1:
				fires[i].x += lineLong / 4;
				fires[i].y -= lineLong / 2;
				break;

			case 2:
				fires[i].x -= lineLong / 2;
				fires[i].y -= lineLong / 4;
				break;

			case 3:
				fires[i].x += lineLong / 2;
				fires[i].y += lineLong / 4;
				break;
			case 4:
				fires[i].x -= lineLong / 4;
				fires[i].y += lineLong / 2;
			default:
				break;
			}

		}
		if (circle >= maxCircle) {
			for (int i = 0; i < fires.length; i++) {
				fires[i].color = Color.WHITE;
			}
		}
		return fires;
	}

	@Override
	public LittleFireworks[] initBlast() {
		// 初始化爆炸的情况,初始化爆炸得粒子

		int[] lineOne = new int[] { endPoint.x - 50, endPoint.y };
		int[] lineTwo = new int[] { endPoint.x + 50, endPoint.y };
		int[] lineThree = new int[] { endPoint.x, endPoint.y - 50 };
		int[] lineFour = new int[] { endPoint.x, endPoint.y + 50 };
		// 35是50/2得开根求得得
		int[] lineFive = new int[] { endPoint.x - 35, endPoint.y - 35 };
		int[] lineSix = new int[] { endPoint.x + 35, endPoint.y + 35 };
		int[] lineSeven = new int[] { endPoint.x - 35, endPoint.y + 35 };
		int[] lineEight = new int[] { endPoint.x + 35, endPoint.y - 35 };

		// 每条线是200/8＝25
		int red = (int) (Math.random() * 255);
		int green = (int) (Math.random() * 255);
		int blue = (int) (Math.random() * 255);
		int[] cols = new int[8];
		for (int i = 0; i < cols.length; i++) {
			red = (int) (Math.random() * 255);
			green = (int) (Math.random() * 255);
			blue = (int) (Math.random() * 255);
			cols[i] = 0xff000000 | red << 16 | green << 8 | blue;
		}

		for (int i = 0; i < fires.length; i++) {
			if (i < 25) {
				fires[i] = new LittleFireworks(lineOne[0] + 2 * i, lineOne[1],
						cols[0]);

			} else if (i < 50) {
				fires[i] = new LittleFireworks(lineTwo[0] - 2 * (i - 25),
						lineTwo[1], cols[1]);

			} else if (i < 75) {
				fires[i] = new LittleFireworks(lineThree[0], lineThree[1] + 2
						* (i - 50), cols[2]);

			} else if (i < 100) {
				fires[i] = new LittleFireworks(lineFour[0], lineFour[1] - 2
						* (i - 75), cols[3]);

			} else if (i < 125) {
				fires[i] = new LittleFireworks(lineFive[0]
						+ (int) (1.4 * (i - 100)), lineFive[1]
						+ (int) (1.4 * (i - 100)), cols[4]);

			} else if (i < 150) {
				fires[i] = new LittleFireworks(lineSix[0]
						- (int) (1.4 * (i - 125)), lineSix[1]
						- (int) (1.4 * (i - 125)), cols[5]);

			} else if (i < 175) {
				fires[i] = new LittleFireworks(lineSeven[0]
						+ (int) (1.4 * (i - 150)), lineSeven[1]
						- (int) (1.4 * (i - 150)), cols[6]);

			} else if (i < 200) {
				fires[i] = new LittleFireworks(lineEight[0]
						- (int) (1.4 * (i - 175)), lineEight[1]
						+ (int) (1.4 * (i - 175)), cols[7]);
			}
		}
		return fires;
	}
}
