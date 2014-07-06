package org.lance.fireworks;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

/**
 * 烟火加工厂
 * 
 * @author lance
 * 
 */
public class FireworksFactory {

	private List<int[]> ids = new ArrayList<int[]>();

	public void addAnimID(int[] animID) {
		ids.add(animID);
	}

	/**
	 * 创建烟花
	 * 
	 * @param context
	 * @param kind
	 * @param x
	 * @param y
	 * @return
	 */
	public Fireworks createFire(Context context, int kind, int x, int y) {
		int red = (int) (Math.random() * 255);
		int green = (int) (Math.random() * 255);
		int blue = (int) (Math.random() * 255);
		int col = 0xff000000 | red << 16 | green << 8 | blue;
		Fireworks fire = null;
		if (Math.random() < 0.5) {
			switch (kind % 6 + 1) {
			case 1:
				fire = new FireworksOne(context, col, x, y);
				break;
			case 2:
				fire = new FireworksTwo(context, col, x, y);
				break;
			case 3:
				fire = new FireworksThree(context, col, x, y);
				break;
			case 4:
				fire = new FireworksFour(context, col, x, y);
				break;
			case 5:
				fire = new FireworksFive(context, col, x, y);
				break;
			case 6:
				fire = new FireworksSix(context, col, x, y);
				break;
			}
		} else {
			// 帧动画烟火
			fire = new FireworksAnimFW(context, ids, col, x, y);
		}
		return fire;
	}
}
