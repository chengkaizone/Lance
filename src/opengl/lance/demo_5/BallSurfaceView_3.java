package opengl.lance.demo_5;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class BallSurfaceView_3 extends GLSurfaceView {
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private SceneRenderer renderer;
	private float previousX;
	private float previousY;
	private int xPosition;

	public BallSurfaceView_3(Context context) {
		super(context);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.requestRender();
	}

	public int getxPosition() {
		return xPosition;
	}

	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
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
		OtherBall_3 ball = new OtherBall_3(3);

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// gl.glDisable(GL10.GL_ADD);
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
			float[] positionParam0 = { xPosition, 1, -5, 1 };
			gl.glDisable(GL10.GL_LIGHT0);
			initLight0(gl, positionParam0);
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			gl.glTranslatef(-1, 0, -2.0f);// z坐标大于0不可见
			ball.drawSelf(gl);// 调用绘制方法
			gl.glLoadIdentity();

			gl.glTranslatef(1, 0, -2.0f);
			ball.drawSelf(gl);
			gl.glLoadIdentity();

			gl.glTranslatef(0, 0, -4.0f);
			ball.drawSelf(gl);
			gl.glLoadIdentity();// 恢复原始状态

		}

		private void initLight0(GL10 gl, float[] params) {
			gl.glEnable(GL10.GL_LIGHT0);
			float[] ambientParams = { 0.2f, 0.03f, 0.03f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams, 0);
			float[] diffuseParams = { 0.5f, 0.1f, 0.1f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams, 0);
			float[] specularParams = { 1.0f, 0.1f, 0.1f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams, 0);
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, params, 0);
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
