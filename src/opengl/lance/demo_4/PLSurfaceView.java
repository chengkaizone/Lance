package opengl.lance.demo_4;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.opengl.GLSurfaceView;

public class PLSurfaceView extends GLSurfaceView {
	SceneRenderer renderer;

	public PLSurfaceView(Context context) {
		super(context);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// …Ë÷√øπæ‚≥›
		PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0,
				Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
		canvas.setDrawFilter(pfd);
		super.onDraw(canvas);
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		Points p = new Points();
		Lines l = new Lines();

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_QUADRATIC_ATTENUATION);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
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
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			gl.glTranslatef(0.5f, 0, -2.0f);
			p.drawSelf(gl);
			gl.glTranslatef(-1.0f, 0, 0);
			l.drawSelf(gl);
		}

	}
}
