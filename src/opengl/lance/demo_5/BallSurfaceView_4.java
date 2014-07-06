package opengl.lance.demo_5;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class BallSurfaceView_4 extends GLSurfaceView {
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private SceneRenderer renderer;
	private float previousX;
	private float previousY;
	private int greenAngle = 0;
	private int redAngle = 90;

	public BallSurfaceView_4(Context context) {
		super(context);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
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
		OtherBall_4 ball = new OtherBall_4(3);

		public SceneRenderer() {
			new Thread() {
				public void run() {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					while (true) {
						greenAngle += 5;
						redAngle += 5;
						requestRender();
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);
			gl.glEnable(GL10.GL_LIGHTING);
			initMaterialWhite(gl);
			initLight0(gl);
			initLight1(gl);
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
			// gl.glShadeModel(GL10.GL_SMOOTH);
			float xGreen = (float) (7 * Math.cos(Math.toRadians(greenAngle)));
			float zGreen = (float) (7 * Math.sin(Math.toRadians(greenAngle)));
			float[] positionParam0 = { xGreen, 1, zGreen, 1 };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, positionParam0, 0);
			float xRed = (float) (7 * Math.cos(Math.toRadians(redAngle)));
			float zRed = (float) (7 * Math.sin(Math.toRadians(redAngle)));
			float[] positionParam1 = { xRed, 1, zRed, 1 };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, positionParam1, 0);
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			gl.glTranslatef(0, 0, -2.0f);// z坐标大于0不可见
			ball.drawSelf(gl);// 调用绘制方法
			// 添加下面这句没有触摸效果
			// gl.glLoadIdentity();

		}

		private void initLight0(GL10 gl) {
			gl.glEnable(GL10.GL_LIGHT0);
			float[] ambientParams = { 0.1f, 0.1f, 0.1f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams, 0);
			float[] diffuseParams = { 0.0f, 1f, 1f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams, 0);
			float[] specularParams = { 0.0f, 1.0f, 0f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams, 0);
		}

		private void initLight1(GL10 gl) {
			gl.glEnable(GL10.GL_LIGHT1);
			float[] ambientParams = { 0.1f, 0.1f, 0.1f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, ambientParams, 0);
			float[] diffuseParams = { 1f, 0f, 0f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, diffuseParams, 0);
			float[] specularParams = { 1f, 0f, 0f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPECULAR, specularParams, 0);
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

			float[] shininessMaterial = { 1.5f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS,
					shininessMaterial, 0);
			// caizhinansezifaguang
			float[] emission = { 0, 0, 0.3f, 1.0f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_EMISSION, emission,
					0);
		}
	}
}
