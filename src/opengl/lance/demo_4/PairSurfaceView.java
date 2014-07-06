package opengl.lance.demo_4;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class PairSurfaceView extends GLSurfaceView {
	private float TOUCH_SCALE_FACTOR = 180 / 320F;
	private SceneRenderer renderer;
	private boolean smoothFlag;
	private boolean cullFlag;
	private boolean backFlag;
	private float previousX;
	private float previousY;

	public PairSurfaceView(Context context) {
		super(context);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	public boolean isSmoothFlag() {
		return smoothFlag;
	}

	public void setSmoothFlag(boolean smoothFlag) {
		this.smoothFlag = smoothFlag;
	}

	public boolean isCullFlag() {
		return cullFlag;
	}

	public void setCullFlag(boolean cullFlag) {
		this.cullFlag = cullFlag;
	}

	public boolean isBackFlag() {
		return backFlag;
	}

	public void setBackFlag(boolean backFlag) {
		this.backFlag = backFlag;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dy = y - previousY;
			float dx = x - previousX;
			renderer.tp.yAngle += dx * TOUCH_SCALE_FACTOR;
			renderer.tp.zAngle += dy * TOUCH_SCALE_FACTOR;
			requestRender();
			break;
		}
		previousX = x;
		previousY = y;
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		TrianglePair2 tp = new TrianglePair2();

		@Override
		public void onDrawFrame(GL10 gl) {
			if (backFlag) {
				gl.glEnable(GL10.GL_CULL_FACE);
			} else {
				gl.glDisable(GL10.GL_CULL_FACE);
			}
			if (smoothFlag) {
				gl.glShadeModel(GL10.GL_SMOOTH);
			} else {
				gl.glShadeModel(GL10.GL_FLAT);
			}
			if (cullFlag) {
				gl.glFrontFace(GL10.GL_CW);
			} else {
				gl.glFrontFace(GL10.GL_CCW);
			}
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			gl.glTranslatef(0, 0, -2.0f);
			tp.drawSelf(gl);
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
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glEnable(GL10.GL_DEPTH_TEST);
		}
	}
}
