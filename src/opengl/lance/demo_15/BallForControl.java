package opengl.lance.demo_15;

import static opengl.lance.demo_15.Constant_5.*;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class BallForControl {
	MySurfaceView_5 mSurfaceView;
	Ball ball;// ���������
	float xOffset;// ����X��������λ��
	float yOffset;
	float zOffset;// ����Z�������ϵ�λ��
	float TIME_SPAN = 0.2f;// ��λʱ��
	float vx;// X���˶��ٶ�
	float vy;
	float vz;
	float minX;// x����Сλ��
	float maxX;// x�����λ��
	float minY;// y����Сλ��
	float maxY;// y�����λ��
	float minZ;// z����Сλ��
	float maxZ;// z�����λ��
	float tempXOffset;// ��ʱ�����������ж�x�᷽�������һ���Ƿ���������ײ
	float tempYOffset;// ��ʱ�����������ж�y�᷽�������һ���Ƿ���������ײ
	float tempZOffset;// ��ʱ�����������ж�z�᷽�������һ���Ƿ���������ײ

	public BallForControl(MySurfaceView_5 mSurfaceView, Ball ball,
			float xOffset, float yOffset, float zOffset, float vx, float vy,
			float vz) {
		this.mSurfaceView = mSurfaceView;
		this.ball = ball;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
		init();// ��ʼ�������С����
		findMinMax();// �ҵ����������С�������
	}

	public void drawSelf(GL10 gl) {
		gl.glPushMatrix();
		gl.glTranslatef(xOffset, yOffset, zOffset);
		ball.drawSelf(gl);// ������
		gl.glPopMatrix();

	}

	public void go(ArrayList<BallForControl> alBall) {
		tempXOffset = xOffset + vx * TIME_SPAN;
		tempYOffset = yOffset + vy * TIME_SPAN;
		tempZOffset = zOffset + vz * TIME_SPAN;
		for (int i = 0; i < alBall.size(); i++) {
			BallForControl bfc = alBall.get(i);
			boolean temp = check(bfc);
			if (temp) {
				bfc.vx = -bfc.vx;
				// bfc.vy=-bfc.vy;
				bfc.vz = -bfc.vz;
				temp = false;
			} else {
				xOffset = tempXOffset;
				yOffset = tempYOffset;
				zOffset = tempZOffset;
			}

		}

	}

	// ��ʼ�������Сֵ
	public void init() {
		minX = minY = minZ = Float.POSITIVE_INFINITY;
		maxX = maxY = maxZ = Float.NEGATIVE_INFINITY;
	}

	public void findMinMax()// �������������С�������
	{
		for (int i = 0; i < mSurfaceView.cube.tempVerteices.length / 3; i++) {
			// �ж�X�����С�����λ��
			if (mSurfaceView.cube.tempVerteices[i * 3] < minX) {
				minX = mSurfaceView.cube.tempVerteices[i * 3];
			}
			if (mSurfaceView.cube.tempVerteices[i * 3] > maxX) {
				maxX = mSurfaceView.cube.tempVerteices[i * 3];
			}
			// �ж�Y�����С�����λ��
			if (mSurfaceView.cube.tempVerteices[i * 3 + 1] < minY) {
				minY = mSurfaceView.cube.tempVerteices[i * 3 + 1];
			}
			if (mSurfaceView.cube.tempVerteices[i * 3 + 1] > maxY) {
				maxY = mSurfaceView.cube.tempVerteices[i * 3 + 1];
			}
			// �ж�Z�����С�����λ��
			if (mSurfaceView.cube.tempVerteices[i * 3 + 2] < minZ) {
				minZ = mSurfaceView.cube.tempVerteices[i * 3 + 2];
			}
			if (mSurfaceView.cube.tempVerteices[i * 3 + 2] > maxZ) {
				maxZ = mSurfaceView.cube.tempVerteices[i * 3 + 2];
			}
		}
		Log.d("minX=" + minX, "maxX=" + maxX);
		Log.d("minY=" + minY, "maxY=" + maxY);
		Log.d("minZ=" + minZ, "maxZ=" + maxZ);

	}

	public boolean check(BallForControl bfcA)// �����Ƿ�����ײ
	{
		float[] nearest = over(bfcA.tempXOffset, bfcA.tempYOffset,
				bfcA.tempZOffset,// �������������
				minX + UNIT_XOFFSET_C, maxX + UNIT_XOFFSET_C, minY
						+ UNIT_YOFFSET_C, maxY + UNIT_YOFFSET_C, minZ
						+ UNIT_ZOFFSET_C, maxZ + UNIT_ZOFFSET_C);
		float[] ballXYZ = { bfcA.tempXOffset, bfcA.tempYOffset,
				bfcA.tempZOffset };// ������������

		float ballToCube = lengthBAC(ballXYZ, nearest);

		if (ballToCube < BALL_IN)// �Ƚϴ�С����С������ײ
		{
			return true;
		}

		return false;
	}

	public float[] over(float ballX, float ballY, float ballZ, float cubeMinX,
			float cubeMaxX, float cubeMinY, float cubeMaxY, float cubeMinZ,
			float cubeMaxZ) {// ����AABB�������
		if (ballX < cubeMinX) {
			ballX = cubeMinX;
		} else if (ballX > cubeMaxX) {
			ballX = cubeMaxX;
		}
		if (ballY < cubeMinY) {
			ballY = cubeMinY;
		} else if (ballY > cubeMaxY) {
			ballY = cubeMaxY;
		}
		if (ballZ < cubeMinZ) {
			ballZ = cubeMinZ;
		} else if (ballZ > cubeMaxZ) {
			ballZ = cubeMaxZ;
		}
		float[] tempXYZ = { ballX, ballY, ballZ };
		return tempXYZ;
	}

	public float lengthBAC(float[] ballXYZ, float[] cubeXYZ)// �������ĵ�AABB�������ľ���
	{
		float aabb = (float) Math.sqrt((ballXYZ[0] - cubeXYZ[0])
				* (ballXYZ[0] - cubeXYZ[0]) + (ballXYZ[1] - cubeXYZ[1])
				* (ballXYZ[1] - cubeXYZ[1]) + (ballXYZ[2] - cubeXYZ[2])
				* (ballXYZ[2] - cubeXYZ[2]));

		return aabb;
	}
}
