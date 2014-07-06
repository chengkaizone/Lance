package opengl.lance.demo_5;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class BallSurfaceView_2 extends GLSurfaceView {
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private SceneRenderer renderer;
	private float previousX;
	private float previousY;
	private int lightNum;

	public BallSurfaceView_2(Context context) {
		super(context);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.requestRender();
	}

	public int getLightNum() {
		return lightNum;
	}

	public void setLightNum(int lightNum) {
		this.lightNum = lightNum;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dx = x - previousX;
			float dy = y = previousY;
			renderer.ball.xAngle += dx * TOUCH_SCALE_FACTOR;
			renderer.ball.zAngle += dy * TOUCH_SCALE_FACTOR;
			this.requestRender();
			break;
		}
		previousX = x;
		previousY = y;
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		OtherBall_1 ball = new OtherBall_1(4);

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {

			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			float ratio = (float) width / height;
			gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glEnable(GL10.GL_LIGHTING);
			initMaterialWhite(gl);
			float[] positionParam0 = { 2, 1, 0, 1 };
			float[] positionParam1 = { -2, 1, 0, 1 };
			float[] positionParam2 = { 1, 2, 4, 1 };
			float[] positionParam3 = { 2, -1, 3, 1 };
			float[] positionParam4 = { -1, 2, 4, 1 };
			gl.glDisable(GL10.GL_LIGHT0);
			gl.glDisable(GL10.GL_LIGHT1);
			gl.glDisable(GL10.GL_LIGHT2);
			gl.glDisable(GL10.GL_LIGHT3);
			gl.glDisable(GL10.GL_LIGHT4);
			switch (lightNum) {
			case 1:
				initLight0(gl, positionParam0);
				break;
			case 2:
				initLight0(gl, positionParam0);
				initLight1(gl, positionParam1);
				break;
			case 3:
				initLight0(gl, positionParam0);
				initLight1(gl, positionParam1);
				initLight2(gl, positionParam2);
				break;
			case 4:
				initLight0(gl, positionParam0);
				initLight1(gl, positionParam1);
				initLight2(gl, positionParam2);
				initLight3(gl, positionParam3);
				break;
			case 5:
				initLight0(gl, positionParam0);
				initLight1(gl, positionParam1);
				initLight2(gl, positionParam2);
				initLight3(gl, positionParam3);
				initLight4(gl, positionParam4);
				break;
			}
			// if(openFlag){
			// gl.glEnable(GL10.GL_LIGHTING);
			// initLight0(gl);//��ʼ��0�ŵ�
			// initMaterialWhite(gl);//��ʼ������
			// float[] positionParamGreen={2,1,0,1};//λ������---����1��ʾ��λ��
			// gl.glLightfv(GL10.GL_LIGHT0,
			// GL10.GL_POSITION,positionParamGreen,0);
			// }else{
			// gl.glDisable(GL10.GL_LIGHTING);//�رչ�Դ
			// }
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			gl.glTranslatef(0, 0, -1.0f);// z�������0���ɼ�
			ball.drawSelf(gl);// ���û��Ʒ���
			gl.glLoadIdentity();// �ָ�ԭʼ״̬

		}

		private void initLight0(GL10 gl, float[] params) {
			gl.glEnable(GL10.GL_LIGHT0);
			float[] ambientParams = { 0.1f, 0.1f, 0.1f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams, 0);
			float[] diffuseParams = { 0.5f, 0.5f, 0.5f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams, 0);
			float[] specularParams = { 1.0f, 1.0f, 1.0f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams, 0);
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, params, 0);
		}

		private void initLight1(GL10 gl, float[] params) {
			gl.glEnable(GL10.GL_LIGHT1);// ��ɫ��
			float[] ambientParams = { 0.2f, 0.03f, 0.03f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, ambientParams, 0);
			float[] diffuseParams = { 0.5f, 0.1f, 0.1f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, diffuseParams, 0);
			float[] specularParams = { 1.0f, 0.1f, 0.1f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPECULAR, specularParams, 0);
			gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, params, 0);
		}

		private void initLight2(GL10 gl, float[] params) {
			gl.glEnable(GL10.GL_LIGHT2);// ��ɫ��
			float[] ambientParams = { 0.03f, 0.03f, 0.2f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT2, GL10.GL_AMBIENT, ambientParams, 0);
			float[] diffuseParams = { 0.1f, 0.1f, 0.5f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT2, GL10.GL_DIFFUSE, diffuseParams, 0);
			float[] specularParams = { 0.1f, 0.1f, 1.0f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT2, GL10.GL_SPECULAR, specularParams, 0);
			gl.glLightfv(GL10.GL_LIGHT2, GL10.GL_POSITION, params, 0);
		}

		private void initLight3(GL10 gl, float[] params) {
			gl.glEnable(GL10.GL_LIGHT3);// ��ɫ��
			float[] ambientParams = { 0.03f, 0.2f, 0.03f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT3, GL10.GL_AMBIENT, ambientParams, 0);
			float[] diffuseParams = { 0.1f, 0.5f, 0.1f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT3, GL10.GL_DIFFUSE, diffuseParams, 0);
			float[] specularParams = { 0.1f, 1.0f, 0.1f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT3, GL10.GL_SPECULAR, specularParams, 0);
			gl.glLightfv(GL10.GL_LIGHT3, GL10.GL_POSITION, params, 0);
		}

		private void initLight4(GL10 gl, float[] params) {
			gl.glEnable(GL10.GL_LIGHT4);// ��ɫ��
			float[] ambientParams = { 0.2f, 0.2f, 0.03f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT4, GL10.GL_AMBIENT, ambientParams, 0);
			float[] diffuseParams = { 0.5f, 0.5f, 0.1f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT4, GL10.GL_DIFFUSE, diffuseParams, 0);
			float[] specularParams = { 1.0f, 1.0f, 0.1f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT4, GL10.GL_SPECULAR, specularParams, 0);
			gl.glLightfv(GL10.GL_LIGHT4, GL10.GL_POSITION, params, 0);
		}

		private void initMaterialWhite(GL10 gl) {
			float[] ambientMaterial = { 0.4f, 0.4f, 0.4f, 1.0f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT,
					ambientMaterial, 0);
			float[] diffuseMaterial = { 0.8f, 0.8f, 0.8f, 1.0f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE,
					diffuseMaterial, 0);
			float[] specularMaterial = { 1.0f, 1.0f, 1.0f, 1.0f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR,
					specularMaterial, 0);

			float[] shininessMaterial = { 2.0f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS,
					shininessMaterial, 0);
		}
	}
}
