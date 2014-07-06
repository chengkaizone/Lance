package opengl.lance.demo_15;

import static opengl.lance.demo_15.Constant_6.*;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * �ӵ�������
 * 
 * @author Administrator
 * 
 */
public class BulletForControl {
	BlastSurfaceView mSurfaceView;// ��������
	Bullet bullet;// �����ӵ�
	float xOffset;// �ӵ���x���ϵľ���
	float yOffset;// �ӵ���x���ϵľ���
	float zOffset;// �ӵ���x���ϵľ���
	float vx;// x���ϵ��ٶ�
	float vy;// y���ϵ��ٶ�
	float vz;// z���ϵ��ٶ�
	float minX;// x����Сλ��
	float maxX;// x�����λ��
	float minY;// y����Сλ��
	float maxY;// y�����λ��
	float minZ;// z����Сλ��
	float maxZ;// z�����λ��
	float TIME_SPAN = 0.3f;// ��λʱ��
	float[] bulletVerteices;// �ӵ��������Сλ������
	float[] cubeVerteices;// ������������Сλ������
	TextureRect[] trExplo;// ���ű�ը������֡
	// ��ը�����Ƿ�ʼ��־
	boolean anmiStart = false;
	// ��ը����������εı�־��Ƕ�
	float anmiYAngle;
	int anmiIndex = 0;// ��ը����֡����

	public BulletForControl(BlastSurfaceView mSurfaceView, Bullet bullet,
			TextureRect[] trExplo, float xOffset, float yOffset, float zOffset,
			float vx, float vy, float vz) {
		this.mSurfaceView = mSurfaceView;// ��ȡ����
		this.bullet = bullet;// ��ȡ����
		this.trExplo = trExplo;// ��ȡ����
		this.xOffset = xOffset;// ��ȡxλ��
		this.yOffset = yOffset;// ��ȡyλ��
		this.zOffset = zOffset;// ��ȡzλ��
		this.vx = vx;// ��ȡx�ٶ�
		this.vy = vy;// ��ȡy�ٶ�
		this.vz = vz;// ��ȡz�ٶ�
		init();// ��ʼ��
		bulletVerteices = findMinMax(bullet.cylinder.tempVerteices);// �ҵ��ӵ������Сֵ
		init();// ��ʼ��
		cubeVerteices = findMinMax(mSurfaceView.cube.tempVerteices);// �ҵ������������Сֵ
	}

	public void drawSelf(GL10 gl) {
		gl.glPushMatrix();
		if (!anmiStart) {
			gl.glTranslatef(xOffset, yOffset, zOffset);
			bullet.drawSelf(gl);// �����ӵ�
		} else {
			// �������
			gl.glEnable(GL10.GL_BLEND);
			// ����Դ���������Ŀ��������
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			gl.glTranslatef(-0.4f, 0, 0.4f);
			gl.glRotatef(anmiYAngle, 0, 1, 0);// ��־����ת
			trExplo[anmiIndex].drawSelf(gl);// ���Ʊ�ը������ǰ֡
			gl.glDisable(GL10.GL_BLEND);
		}

		gl.glPopMatrix();
	}

	public void go(ArrayList<BulletForControl> al) {
		if (!anmiStart)// �����������һ�����ٽ�����ײ���
		{
			xOffset = xOffset + vx * TIME_SPAN;// ��¼��һ��λ��
			yOffset = yOffset + vy * TIME_SPAN;
			zOffset = zOffset + vz * TIME_SPAN;
			for (int i = 0; i < al.size(); i++) {
				BulletForControl bfc = al.get(i);// ȡһ���ӵ�
				float temp = check(bfc);// ��־λ
				if (temp > OVER)// ������ײ
				{
					mSurfaceView.activity.playSound(1, 1);
					anmiYAngle = calculateBillboardDirection();// ���㱬ը����������εı�־��Ƕ�
					// ���ö�����ʼ��־
					anmiStart = true;
				}
			}
		} else {
			// ������ʼ��֡
			if (anmiIndex < trExplo.length - 1) {// ����û�в����궯����֡
				anmiIndex = anmiIndex + 1;
			} else {
				anmiStart = false;
				// �Ƴ��ӵ�
				mSurfaceView.alBFC.remove(this);
			}
		}

	}

	public void init() {// ��ʼ����С���ֵ
		minX = minY = minZ = Float.POSITIVE_INFINITY;
		maxX = maxY = maxZ = Float.NEGATIVE_INFINITY;
	}

	public float[] findMinMax(float[] temp)// �����������С�������
	{
		for (int i = 0; i < temp.length / 3; i++) {
			// �ж�X�����С�����λ��
			if (temp[i * 3] < minX) {
				minX = temp[i * 3];
			}
			if (temp[i * 3] > maxX) {
				maxX = temp[i * 3];
			}
			// �ж�Y�����С�����λ��
			if (temp[i * 3 + 1] < minY) {
				minY = temp[i * 3 + 1];
			}
			if (temp[i * 3 + 1] > maxY) {
				maxY = temp[i * 3 + 1];
			}
			// �ж�Z�����С�����λ��
			if (temp[i * 3 + 2] < minZ) {
				minZ = temp[i * 3 + 2];
			}
			if (temp[i * 3 + 2] > maxZ) {
				maxZ = temp[i * 3 + 2];
			}
		}
		float[] verteices = { minX, maxX, minY, maxY, minZ, maxZ };
		Log.d("minX=" + minX, "maxX=" + maxX);
		Log.d("minY=" + minY, "maxY=" + maxY);
		Log.d("minZ=" + minZ, "maxZ=" + maxZ);
		return verteices;
	}

	public float check(BulletForControl bfc) {
		float xOver = calOver(bulletVerteices[1] + bfc.xOffset,
				bulletVerteices[0] + bfc.xOffset,// ����Xλ���ص�����
				cubeVerteices[1] + CUBE_OFFSET_X, cubeVerteices[0]
						+ CUBE_OFFSET_X);
		float yOver = calOver(bulletVerteices[3] + bfc.yOffset,
				bulletVerteices[2] + bfc.yOffset,// ����yλ���ص�����
				cubeVerteices[3] + CUBE_OFFSET_Y, cubeVerteices[2]
						+ CUBE_OFFSET_Y);
		float zOver = calOver(bulletVerteices[5] + bfc.zOffset,
				bulletVerteices[4] + bfc.zOffset,// ����zλ���ص�����
				cubeVerteices[5] + CUBE_OFFSET_Z, cubeVerteices[4]
						+ CUBE_OFFSET_Z);
		Log.d("xOver*yOver*zOver", xOver * yOver * zOver + "");
		return xOver * yOver * zOver;
	}

	// �����ص����ַ���
	public static float calOver(float amax, float amin, float bmax, float bmin) {
		float leftMax = 0;
		float leftMin = 0;
		float rightMax = 0;
		float rightMin = 0;

		if (amax < bmax)// ��a��b�����
		{
			leftMax = amax;// �ֱ�ֵ
			leftMin = amin;
			rightMax = bmax;
			rightMin = bmin;
		} else// ��a��b���ұ�
		{
			leftMax = bmax;// �ֱ�ֵ
			leftMin = bmin;
			rightMax = amax;
			rightMin = amin;
		}

		if (leftMax > rightMin)// �����ص�ֵ�Ĵ�С
		{
			return leftMax - rightMin;
		} else// �����ص�������0��
		{
			return 0;
		}
	}

	public float calculateBillboardDirection() {// ���������λ�ü��㱬ը�����泯��
		float yAngle = 0;

		float xspan = xOffset - mSurfaceView.cx;// ���㵱ǰλ���������λ�õľ���
		float zspan = xOffset - mSurfaceView.cz;

		if (zspan <= 0) {
			yAngle = (float) Math.toDegrees(Math.atan(xspan / zspan)); // ����Ƕ�
		} else {
			yAngle = 180 + (float) Math.toDegrees(Math.atan(xspan / zspan));// ����Ƕ�
		}

		return yAngle;
	}
}
