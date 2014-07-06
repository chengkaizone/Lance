package opengl.lance.demo_5;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class BallSurfaceView_1 extends GLSurfaceView {
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private SceneRenderer renderer;
	private float previousX;
	private float previousY;
	private boolean openFlag;

	public BallSurfaceView_1(Context context) {
		super(context);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.requestRender();
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

	public boolean isOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(boolean openFlag) {
		this.openFlag = openFlag;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		OtherBall_1 ball = new OtherBall_1(4);

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_ADD);
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
			if (openFlag) {
				gl.glEnable(GL10.GL_LIGHTING);
				initLight0(gl);// 初始化0号灯
				initMaterialWhite(gl);// 初始化材质
				float[] positionParamGreen = { 2, 1, 0, 1 };// 位置数组---最后的1表示定位光
				gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION,
						positionParamGreen, 0);
			} else {
				gl.glDisable(GL10.GL_LIGHTING);// 关闭光源
			}
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			gl.glTranslatef(0, 0, -1.0f);// z坐标大于0不可见
			ball.drawSelf(gl);// 调用绘制方法
			gl.glLoadIdentity();// 恢复原始状态

		}

		private void initLight0(GL10 gl) {
			gl.glEnable(GL10.GL_LIGHT0);
			float[] ambientParams = { 0.1f, 0.1f, 0.1f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams, 0);
			float[] diffuseParams = { 0.5f, 0.5f, 0.5f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams, 0);
			float[] specularParams = { 1.0f, 1.0f, 1.0f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams, 0);
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
