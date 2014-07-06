package opengl.lance.demo_7;

import javax.microedition.khronos.opengles.GL10;

/**
 * ��˫�����ƽ��Բ��ϳ�����
 * 
 * @author Administrator
 * 
 */
public class Drum {
	HSurfaceView7_9 surface;
	Hyperboloid hyperboloid;
	Circle circle;

	public float xAngle;
	public float yAngle;
	public float zAngle;

	public Drum(HSurfaceView7_9 surface) {
		this.surface = surface;

		hyperboloid = new Hyperboloid(10f, 2f, 2f, 6f, 10, 18,
				surface.renderer.hyperId);// ����Բ����
		circle = new Circle(hyperboloid.radius, 12f, surface.renderer.circleId);
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(xAngle, 1, 0, 0);// ��ת
		gl.glRotatef(yAngle, 0, 1, 0);
		gl.glRotatef(zAngle, 0, 0, 1);

		gl.glPushMatrix();
		initMaterial(gl);// ��ʼ������
		hyperboloid.drawSelf(gl);// ����
		gl.glPopMatrix();// �ָ��任�����ֳ�

		gl.glPushMatrix();
		initMaterial(gl);
		gl.glTranslatef(0, hyperboloid.height * 0.5f, 0);
		gl.glRotatef(90, 1, 0, 0);
		gl.glRotatef(180, 0, 0, 1);
		circle.drawSelf(gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
		initMaterial(gl);
		gl.glTranslatef(0, -hyperboloid.height * 0.5f, 0);
		gl.glRotatef(-90, 1, 0, 0);
		circle.drawSelf(gl);
		gl.glPopMatrix();
	}

	// ��ʼ������
	private void initMaterial(GL10 gl) {
		// ������
		float ambientMaterial[] = { 248f / 255f, 242f / 255f, 144f / 255f, 1.0f };
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT,
				ambientMaterial, 0);
		// ɢ���
		float diffuseMaterial[] = { 248f / 255f, 242f / 255f, 144f / 255f, 1.0f };
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE,
				diffuseMaterial, 0);
		// �߹����
		float specularMaterial[] = { 248f / 255f, 242f / 255f, 144f / 255f,
				1.0f };
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR,
				specularMaterial, 0);
		gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 100.0f);
	}
	// HSurfaceView7_9 surface;
	// private Hyperboloid hyper;
	// private Circle circle;
	//
	// float xAngle;
	// float yAngle;
	// float zAngle;
	//
	// public Drum(HSurfaceView7_9 surface) {
	// this.surface = surface;
	// // ����������
	// hyper = new Hyperboloid(10f, 2f, 2f, 6f, 10, 18,
	// surface.renderer.hyperId);
	// // ����Բ---��Ƥ
	// circle = new Circle(hyper.radius, 12, surface.renderer.circleId);
	// }
	//
	// public void drawSelf(GL10 gl) {
	// gl.glRotatef(xAngle, 1, 0, 0);
	// gl.glRotatef(yAngle, 0, 1, 0);
	// gl.glRotatef(zAngle, 0, 0, 1);
	// // �Ȼ��ƺ��˫����
	// gl.glPushMatrix();
	// initMaterial(gl);// ��ʼ������
	// hyper.drawSelf(gl);// ����
	// gl.glPopMatrix();// �ָ��任�����ֳ�
	// // ��ƽ��һ�ξ�����ת��һ���ӽǻ�������Բ
	// gl.glPushMatrix();
	// initMaterial(gl);
	// gl.glTranslatef(0,hyper.height * 0.5f, 0);
	// gl.glRotatef(90, 1, 0, 0);
	// gl.glRotatef(180, 0, 0, 1);
	// circle.drawSelf(gl);
	// gl.glPopMatrix();
	//
	// gl.glPushMatrix();
	// initMaterial(gl);
	// // ˮƽ����ƽ��
	// gl.glTranslatef(0,-hyper.height * 0.5f, 0);
	// gl.glRotatef(-90, 1, 0, 0);
	// circle.drawSelf(gl);
	// gl.glPopMatrix();
	// }
	//
	// // ��ʼ������
	// private void initMaterial(GL10 gl) {
	// // ������
	// float ambientMaterial[] = { 248f / 255f, 242f / 255f, 144f / 255f, 1.0f
	// };
	// gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT,
	// ambientMaterial, 0);
	// // ɢ���
	// float diffuseMaterial[] = { 248f / 255f, 242f / 255f, 144f / 255f, 1.0f
	// };
	// gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE,
	// diffuseMaterial, 0);
	// // �߹����
	// float specularMaterial[] = { 248f / 255f, 242f / 255f, 144f / 255f,
	// 1.0f };
	// gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR,
	// specularMaterial, 0);
	// gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 100.0f);
	// }
}
