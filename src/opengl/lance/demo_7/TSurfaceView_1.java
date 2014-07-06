package opengl.lance.demo_7;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.lance.main.R;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class TSurfaceView_1 extends GLSurfaceView {
	private final float TOUCH_SCALE_FACTOR = 180F / 320;
	private SceneRenderer renderer;

	private float previousX;
	private float previousY;
	private float lightAngle = 90;

	public TSurfaceView_1(Context context) {
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
			float dy = y - previousY;
			renderer.tap.xAngle += dy * TOUCH_SCALE_FACTOR;
			renderer.tap.yAngle += dx * TOUCH_SCALE_FACTOR;
			this.requestRender();
			break;
		}
		previousX = x;
		previousY = y;
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		Taper tap;

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);

			int texId = initTexture(gl, R.drawable.jiaotong);
			tap = new Taper(5f, 1.5f, 12f, 10, texId);
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			float ratio = (float) width / height;
			gl.glFrustumf(-ratio, ratio, -1f, 1f, 1f, 100);
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			gl.glPushMatrix();
			float lx = 0;
			float ly = (float) (7 * Math.cos(Math.toRadians(lightAngle)));
			float lz = (float) (7 * Math.sin(Math.toRadians(lightAngle)));
			float[] pos = { lx, ly, lz, 0 };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, pos, 0);

			initMaterial(gl);

			gl.glTranslatef(0, -3f, -5f);
			initLight(gl);
			tap.drawSelf(gl);
			closeLight(gl);

			gl.glPopMatrix();
		}

		private void initLight(GL10 gl) {
			gl.glEnable(GL10.GL_LIGHTING);
			gl.glEnable(GL10.GL_LIGHT0);

			float[] ambient = { 0.2f, 0.2f, 0.2f, 1f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambient, 0);
			float[] diffuse = { 1f, 1f, 1f, 1f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuse, 0);
			float[] specular = { 1f, 1f, 1f, 1f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specular, 0);
		}

		private void closeLight(GL10 gl) {
			gl.glDisable(GL10.GL_LIGHT0);
			gl.glDisable(GL10.GL_LIGHTING);
		}

		private void initMaterial(GL10 gl) {
			float[] ambient = { 248f / 255, 242f / 255, 142f / 255, 1f };
			gl.glMaterialfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambient, 0);
			float[] diffuse = { 248f / 255, 242f / 255, 142f / 255, 1f };
			gl.glMaterialfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuse, 0);
			float[] specular = { 248f / 255, 242f / 255, 142f / 255, 1f };
			gl.glMaterialfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specular, 0);
			gl.glMaterialf(GL10.GL_LIGHT0, GL10.GL_SHININESS, 100f);
		}

		private int initTexture(GL10 gl, int drawId) {
			int[] texs = new int[1];
			gl.glGenTextures(1, texs, 0);
			int curTexId = texs[0];

			gl.glBindTexture(GL10.GL_TEXTURE_2D, curTexId);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
					GL10.GL_LINEAR_MIPMAP_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
					GL10.GL_LINEAR_MIPMAP_LINEAR);
			((GL11) gl).glTexParameterf(GL10.GL_TEXTURE_2D,
					GL11.GL_GENERATE_MIPMAP, GL10.GL_TRUE);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
					GL10.GL_TEXTURE_WRAP_T);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
					GL10.GL_TEXTURE_WRAP_T);
			InputStream is = null;
			Bitmap bitmap = null;
			try {
				is = getResources().openRawResource(drawId);
				bitmap = BitmapFactory.decodeStream(is);
			} catch (NotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			bitmap.recycle();
			return curTexId;
		}
	}

}
