package opengl.lance.demo_11;

import javax.microedition.khronos.opengles.GL10;

import static opengl.lance.demo_11.MySurfaceView_3.*;

public class Cactus implements Comparable<Cactus> {
	float x;
	float z;
	// �����Ƶĳ���
	float yAngle;
	MyCactusGroup cg;

	public Cactus(float x, float z, float yAngle, MyCactusGroup cg) {
		this.x = x;
		this.z = z;
		this.yAngle = yAngle;
		this.cg = cg;
	}

	// ����������
	public void drawSelf(GL10 gl) {
		gl.glPushMatrix();
		gl.glTranslatef(x, 0, z);
		gl.glRotatef(yAngle, 0, 1, 0);
		cg.cfd.drawSelf(gl);
		gl.glPopMatrix();
	}

	// ���������λ�ü����������泯��
	public void calculateBillboardDirection() {
		float xspan = x - cx;
		float zspan = z - cz;
		// �����������ӽ���Ļ--->xspan/zspan--->����ֵ
		if (zspan <= 0) {
			yAngle = (float) Math.toDegrees(Math.atan(xspan / zspan));
		}
		// ��������Ƹ��ӽ���Ļ
		else {
			// ������---��������ֵ������Ƕ�
			yAngle = 180 + (float) Math.toDegrees(Math.atan(xspan / zspan));
		}
	}

	@Override
	public int compareTo(Cactus another) {
		// ���㵱ǰ�����ƺ��������x/z�������
		float xc = x - cx;
		float zc = z - cz;
		// ����ͬ��ǰ�����ƱȽϵ���һ����������ͼ��������ľ���
		float xo = another.x - cx;
		float zo = another.z - cz;

		float disA = (float) (Math.sqrt(xc * xc + zc * zc));
		float disB = (float) (Math.sqrt(xo * xo + zo * zo));

		return 0;
	}
}
