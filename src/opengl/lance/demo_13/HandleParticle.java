package opengl.lance.demo_13;

import javax.microedition.khronos.opengles.GL10;

/**
 * ����ĳ������ϵͳ�е�һ�����ӣ���������������ӵĸ�������
 * 
 * @author Administrator
 * 
 */
public class HandleParticle {
	// ���ӱ��
	int particleIndex;
	// ���������ϵ��ٶ�
	float xSpeed;
	float ySpeed;
	float zSpeed;
	// ʱ����
	float timeSpan = 0;

	public HandleParticle(int index, float xSpeed, float ySpeed, float zSpeed) {
		this.particleIndex = index;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.zSpeed = zSpeed;
	}

	public void drawSelf(GL10 gl) {
		gl.glPushMatrix();
		// ���ݵ�ǰʱ������������λ��
		float x = xSpeed * timeSpan;
		float z = zSpeed * timeSpan;
		float y = ySpeed * timeSpan - 0.5f * timeSpan * timeSpan * 1.0f;
		gl.glTranslatef(x, y, z);
		// ��������
		FireSport.pArray[particleIndex].drawSelf(gl);
		gl.glPopMatrix();
	}
}
