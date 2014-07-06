package opengl.lance.demo_13;

import static opengl.lance.demo_13.Constant_1.*;

import javax.microedition.khronos.opengles.GL10;

/**
 * ��������˶��߼�
 * 
 * @author Administrator
 * 
 */
public class BallController_1 {
	Ball_1 ball;// ������
	float timeDown = 0;
	// ���˶�����ʱ��---���˶������ۼ�
	float timeLive = 0;
	// x�����ϵ��ٶ�
	float xSpeed;
	// y�����ϵ��ٶ�
	float ySpeed;
	// z�����ϵ��ٶ�
	float zSpeed;
	// ���Y����
	float maxHeight;
	// ��ǰy����
	// float currentHeight;
	// �����ʼλ��
	float startX;
	float startY;
	float startZ;

	// ��ĵ�ǰλ��
	float currentX;
	float currentY;
	float currentZ;
	// ��һʱ��λ��
	float previousX;
	float previousY;
	float previousZ;

	float xAngle;
	float zAngle;
	// ��ĵ�ǰ״̬��0~�ڰ����˶���1~�״����䡢2~��������
	int state = 0;

	public BallController_1(Ball_1 ball, float startX, float startZ,
			float xSpeed, float zSpeed) {
		this.ball = ball;
		this.xSpeed = xSpeed;
		this.zSpeed = zSpeed;
		this.startX = startX;
		this.startZ = startZ;
		// ������ʼ��y����
		this.startY = 2 * STARTY + HEIGHT + 2 * SCALE;

		currentX = startX;
		currentY = startY;
		currentZ = startZ;
	}

	public void drawSelf(GL10 gl) {
		// ���û��
		gl.glEnable(GL10.GL_BLEND);
		// ����Դ���Ӻ�Ŀ������
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glPushMatrix();
		gl.glTranslatef(0, 0, 0);
		gl.glTranslatef(currentX, currentY, currentZ);
		gl.glRotatef(xAngle, 1, 0, 0);
		gl.glRotatef(zAngle, 0, 0, 1);
		ball.drawSelf(gl);
		gl.glPopMatrix();
	}

	public void move() {
		// ÿ���ƶ���Ҫ�ۼ�ʱ��
		timeLive += TIME_SPAN;
		// x��z������������ֱ���˶�
		currentX = startX + xSpeed * timeLive;
		currentZ = startZ + zSpeed * timeLive;
		// ����x/z���ϵ�ת��---�÷����������x�����ϵ�ת��--���ںܴ����sin���Ǻ����ļн�
		xAngle = (float) Math.toDegrees(currentX / SCALE);
		zAngle = (float) Math.toDegrees(currentZ / SCALE);
		// ��ľ������ƽ���˶�
		if (state == 0) {
			// �������ľ��߽�---��ʼ����
			if (currentX > LENGTH / 2 || currentZ > WIDTH / 2) {
				state = 1;// ����״̬
				timeDown = 0;
			}
		} else if (state == 1) {// �����״�����״̬
			// ʱ�����С��λ---�������������˶�����
			timeDown += TIME_SPAN;
			float tempY = startY - 0.5f * G * timeDown * timeDown;
			// �����ؽ�������ײ
			if (tempY <= SCALE + HEIGHT) {
				state = 2;
				// ����y�����ϵ��ٶ�---Ҫ����������ʧ
				this.ySpeed = G * timeDown * ANERGY_LOST;
				// ���¼�ʱ
				timeDown = 0;
			} else {
				currentY = tempY;
			}
		} else if (state == 2) {// �����׶�
			timeDown += TIME_SPAN;
			float tempY = SCALE + HEIGHT + ySpeed * timeDown - 0.5f * G
					* timeDown * timeDown;
			// �ٴκ͵ذ巢����ײ
			if (tempY <= SCALE + HEIGHT) {
				this.ySpeed = (G * timeDown - ySpeed) * ANERGY_LOST;
				timeDown = 0;
			} else {
				currentY = tempY;
			}
		}
	}
}
