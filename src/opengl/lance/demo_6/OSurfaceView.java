package opengl.lance.demo_6;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.MotionEvent;

public class OSurfaceView extends GLSurfaceView {
	private SceneRenderer renderer;
	private float previousX;

	public OSurfaceView(Context context) {
		super(context);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dx = x - previousX;
			renderer.angle += dx * OContant.TOUCH_SCALE_FACTOR;
			this.requestRender();
			break;
		}
		previousX = x;
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		OCube cube = new OCube();
		float angle = 45;

		@Override
		public void onDrawFrame(GL10 gl) {
			// gl.glEnable(GL10.GL_CULL_FACE);
			// gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			GLU.gluLookAt(gl, 0f, // 人眼位置的X
					10f, // 人眼位置的Y
					15.0f, // 人眼位置的Z
					0, // 人眼球看的点X
					0f, // 人眼球看的点Y
					0, // 人眼球看的点Z
					0, 1, 0);
			gl.glRotatef(angle, 0, 1, 0);

			gl.glPushMatrix();
			gl.glTranslatef(-2, 0, 0);
			cube.drawSelf(gl);
			gl.glPopMatrix();

			gl.glPushMatrix();
			gl.glTranslatef(2, 0, 0);
			cube.drawSelf(gl);
			gl.glPopMatrix();
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			float ratio = (float) height / width;
			gl.glFrustumf(-1, 1, -ratio, ratio, 8f, 100);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);
		}
	}
}
