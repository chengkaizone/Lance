package opengl.lance.demo_11;

import javax.microedition.khronos.opengles.GL10;

import static opengl.lance.demo_11.Constant_4.*;

public class BallController {
	final float TIME_SPAN = 0.05f;
	// �������ٶ�
	final float G = 0.8f;
	Ball ball;
	// ���߶�
	float maxHight = 0;
	// ���ʱ��
	float timeLive;
	// ��ǰy����
	float currentY;
	// ����ٶ�
	float maxSpeed;
	//
	float curSpeed;
	// �Ƿ�Ϊ�½��׶�
	boolean flag = true;

	// h=1/2gt^2//zi you luo ti yun dong de jishuan gongsi
	public BallController(Ball ball, float high) {
		this.ball = ball;
		this.maxHight = high;
		currentY = high;
		new Thread() {
			public void run() {
				while (true) {
					if (flag) {
						if (currentY > 0) {
							currentY = maxHight - 0.5f * G * timeLive
									* timeLive;
							timeLive += TIME_SPAN;
						} else {
							maxSpeed = G * timeLive;
							flag = false;
							timeLive = 0;
							curSpeed = maxSpeed;
							maxSpeed = maxSpeed * 0.8f;
						}
					} else {
						if (maxSpeed < 0.35f) {
							currentY = 0;
							break;
						}
						if (curSpeed > 0) {
							currentY = maxSpeed * timeLive - 0.5f * G
									* timeLive * timeLive;
							timeLive += TIME_SPAN;
							curSpeed = maxSpeed - G * timeLive;
						} else {
							flag = true;
							timeLive = 0;
							maxHight = currentY;
						}
					}
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	// ��������ʵ��
	public void drawSelf(GL10 gl) {
		gl.glPushMatrix();
		gl.glTranslatef(0, UNIT_SIZE * BALL_SCALE + currentY, 0);
		ball.drawSelf(gl);
		gl.glPopMatrix();
	}

	// ������������
	public void drawSelfMirror(GL10 gl) {
		gl.glPushMatrix();
		gl.glTranslatef(0, -UNIT_SIZE * BALL_SCALE - currentY, 0);
		ball.drawSelf(gl);
		gl.glPopMatrix();
	}
}
