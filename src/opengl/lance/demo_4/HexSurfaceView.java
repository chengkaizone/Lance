package opengl.lance.demo_4;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class HexSurfaceView extends GLSurfaceView {
	private float TOUCH_SCALE_FACTOR = 180.0F / 320;
	private boolean isPerspective;
	private SceneRenderer renderer;
	private float previousY;
	private float xAngle;

	public HexSurfaceView(Context context) {
		super(context);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dy = y - previousY;
			xAngle += dy * TOUCH_SCALE_FACTOR;
			requestRender();
			break;
		}
		previousY = y;
		return true;
	}

	public boolean isPerspective() {
		return isPerspective;
	}

	public void setPerspective(boolean isPerspective) {
		this.isPerspective = isPerspective;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		private int width, height;
		Hexagon[] hs = new Hexagon[] { new Hexagon(0), new Hexagon(-2),
				new Hexagon(-4), new Hexagon(-6), new Hexagon(-8),
				new Hexagon(-10), new Hexagon(-12) };

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glEnable(GL10.GL_DEPTH_TEST);
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);
			this.width = width;
			this.height = height;
			// gl.glMatrixMode(GL10.GL_PROJECTION);
			// gl.glLoadIdentity();
			//
			// float ratio=(float)width/height;
			// gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			float ratio = (float) width / height;
			if (isPerspective) {
				gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
			} else {
				gl.glOrthof(-ratio, ratio, -1, 1, 1, 10);
			}
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			// gl.glEnable(GL10.GL_CULL_FACE);
			gl.glTranslatef(0, 0, -1.4f);
			gl.glRotatef(xAngle, 1, 0, 0);
			for (Hexagon h : hs) {
				h.drawSelf(gl);
			}
		}

	}
}
