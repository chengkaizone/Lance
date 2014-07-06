package opengl.lance.demo_13;

import static opengl.lance.demo_13.MySurfaceView.*;

import javax.microedition.khronos.opengles.GL10;

//��������ϵͳ�е�ĳ������
public class SingleParticle implements Comparable<SingleParticle> {
	int particleForDrawIndex;// ��Ӧ�������ӵı��
	float vx;// x���ٶȷ���
	float vy;// y���ٶȷ���
	float vz;// z���ٶȷ���
	float timeSpan = 0;// �ۼ�ʱ��
	float yAngle;// �����泯��Ƕȷ�λ��

	public SingleParticle(int particleForDrawIndex, float vx, float vy, float vz) {
		this.particleForDrawIndex = particleForDrawIndex;
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
	}

	public void drawSelf(GL10 gl) {
		// ÿ�λ���ǰ���������λ�ü�����������泯��
		calculateBillboardDirection();

		gl.glPushMatrix();
		// ���ݵ�ǰʱ������������λ��
		float x = vx * timeSpan;
		float z = vz * timeSpan;
		float y = vy * timeSpan - 0.5f * timeSpan * timeSpan * 1.0f;
		gl.glTranslatef(x, y, z);
		gl.glRotatef(yAngle, 0, 1, 0);
		// ��������
		FireWorks.pfdArray[particleForDrawIndex].drawSelf(gl);
		gl.glPopMatrix();
	}

	// @Override
	public int compareTo(SingleParticle another) {// ��д�ıȽ��������������������ķ���
		float x = vx * timeSpan - MySurfaceView.cx;
		float z = vz * timeSpan - MySurfaceView.cz;
		float y = vy * timeSpan - 0.5f * timeSpan * timeSpan * 1.0f
				- MySurfaceView.cy;

		float xo = another.vx * another.timeSpan - MySurfaceView.cx;
		float zo = another.vz * another.timeSpan - MySurfaceView.cz;
		float yo = another.vy * another.timeSpan - 0.5f * another.timeSpan
				* another.timeSpan * 1.0f - MySurfaceView.cy;

		float disA = (float) Math.sqrt(x * x + y * y + z * z);
		float disB = (float) Math.sqrt(xo * xo + yo * yo + zo * zo);

		return ((disA - disB) == 0) ? 0 : ((disA - disB) > 0) ? -1 : 1;
	}

	// �˴�ʹ���˱�־�弼��
	public void calculateBillboardDirection() {// ���������λ�ü�����������泯��
		float x = vx * timeSpan;
		float z = vz * timeSpan;

		float xspan = x - cx;
		float zspan = z - cz;

		if (zspan <= 0) {
			yAngle = (float) Math.toDegrees(Math.atan(xspan / zspan));
		} else {
			yAngle = 180 + (float) Math.toDegrees(Math.atan(xspan / zspan));
		}
	}
}
