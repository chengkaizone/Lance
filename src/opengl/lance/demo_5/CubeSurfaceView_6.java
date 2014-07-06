package opengl.lance.demo_5;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

public class CubeSurfaceView_6 extends GLSurfaceView {
	private SceneRenderer renderer;
	private float cx = 0;
	private float cy = 3;
	private float cz = 40;

	private float dx = 0;
	private float dy = 0;
	private float dz = 0;

	public CubeSurfaceView_6(Context context) {
		super(context);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		OtherCubeIndex_6 cube;

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);
			gl.glEnable(GL10.GL_CULL_FACE);
			gl.glEnable(GL10.GL_LIGHTING);
			initLight(gl);
			initMaterial(gl);
			cube = new OtherCubeIndex_6(2.5f, 2.5f, 2.5f);
			new Thread() {
				public void run() {
					while (true) {
						try {
							Thread.sleep(30);
							cube.yOffset += 2.0f;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			float ratio = (float) width / height;
			// ע��˴�������ʹ�ö���⣻������Ҫ���ô�һ��
			gl.glFrustumf(-ratio, ratio, -1, 1, 8, 100);
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_CULL_FACE);
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			GLU.gluLookAt(gl, cx, cy, cz, dx, dy, dz, 0, 1, 0);
			gl.glPushMatrix();
			gl.glRotatef(45, 0, 1, 0);
			gl.glRotatef(45, 1, 0, 0);
			cube.drawSelf(gl);
			gl.glPopMatrix();
		}

		private void initLight(GL10 gl) {
			gl.glEnable(GL10.GL_LIGHT0);// ��0�Ź�Դ

			// ����������
			float[] ambientParams = { 0.46f, 0.21f, 0.05f, 1.0f };// ����� RGBA
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams, 0);

			// ɢ�������
			float[] diffuseParams = { 0.46f, 0.21f, 0.05f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams, 0);

			// ���������
			float[] specularParams = { 0.46f, 0.21f, 0.05f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams, 0);

			// ָ����Դλ��
			float[] directionParams = { -1f, 1f, 1f, 0 };// �����
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, directionParams, 0);
		}

		private void initMaterial(GL10 gl) {
			// ������Ϊ��ɫ����
			float ambientMaterial[] = { 0.6f, 0.6f, 0.6f, 1.0f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT,
					ambientMaterial, 0);
			// ɢ���Ϊ��ɫ����
			float diffuseMaterial[] = { 1.0f, 1.0f, 1.0f, 1.0f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE,
					diffuseMaterial, 0);
			// �߹����Ϊ��ɫ
			float specularMaterial[] = { 1f, 1.0f, 1f, 1.0f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR,
					specularMaterial, 0);
		}
	}
}
