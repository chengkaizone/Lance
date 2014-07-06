package org.lance.fireworks;

import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import org.lance.main.R;

/**
 * �����̻�ʽ���ĸ��࣬�����Ժ�����̻�ʽ��
 * 
 * @author lance
 * 
 */
public abstract class Fireworks {
	int x = 30; // �õ�ĺ�����
	int y = 30; // �õ��������

	int color; // �õ����ɫ

	int pace = 30; // �õ��������ٶ�
	int size = 6; // ��Ĵ�С

	final Point endPoint = new Point(); // ��¼�õ�����λ��

	// ��¼�õ��״̬�������Ƿ��Ǳ���״̬,1������״̬��2�Ǳ�ը״
	// ̬�ĳ�ʼ����3�Ǳ�ը״̬��4����ʧ״̬
	public int state = 1;
	public int circle = 1;
	// �̻�����
	LittleFireworks[] fires = new LittleFireworks[200];

	int WALLOP = 20; // �豬ը��ÿ�������ܵ��ĳ������10N
	int GRAVITY = 20;

	FireworksAnimation mFireAnim = null;
	Context mContext;

	int maxCircle;

	public Fireworks(Context context, int color, int endX, int endY) {
		circle = 1;
		state = 1;
		pace = 25;

		this.x = endX;
		this.y = 800;
		this.color = color;
		endPoint.x = endX;
		endPoint.y = endY;
		mContext = context;
		new loadFire().start();// ���߳�ȥ����ͼƬ���ݣ��������������������Ļһ��һ����

		maxCircle = new Random().nextInt(6) + 5;
	}

	class loadFire extends Thread {
		public void run() {
			mFireAnim = new FireworksAnimation(mContext, new int[] {
					R.drawable.trail1, R.drawable.trail2, R.drawable.trail3,
					R.drawable.trail4, R.drawable.trail5, R.drawable.trail6 },
					true);
		}
	}

	/**
	 * ����
	 */
	public void rise() {
		// ���������ĵ����
		if (mFireAnim != null) {
			y -= pace;
			// ȷ��x�᲻��
			x = x * 1;
			if (y <= endPoint.y) {
				y = endPoint.y;

			}
			if (x <= endPoint.x) {
				x = endPoint.x;
			}
		}
	}

	/**
	 * �ж��Ƿ�ը
	 * 
	 * @return
	 */
	public boolean isBlast() {
		// �ж��Ƿ�ը
		if (y <= endPoint.y && x <= endPoint.x) {
			return true;
		}
		return false;
	}

	/**
	 * ��ʼ����ը�����
	 * 
	 * @return
	 */
	public abstract LittleFireworks[] initBlast();

	/**
	 * ����ը�����
	 * 
	 * @return
	 */
	public abstract LittleFireworks[] blast();

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
			if (circle <= maxCircle) {
				circle++;
				this.fires = blast();
				for (int i = 0; i < fires.length; i++) {
					mPaint.setColor(fires[i].color);
					oval = new RectF(fires[i].x, fires[i].y, fires[i].x + 2,
							fires[i].y + 2);
					canvas.drawOval(oval, mPaint);
				}
			} else {
				// �����ڿ��У�������˸
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

	public String toString() {
		return "�õ����ڵ�λ����x��" + x + ",y=" + y + "," + "��ը����x=" + endPoint.x
				+ ",y=" + endPoint.y + "��ɫ��" + color;
	}
}
