package opengl.lance.demo_6;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.lance.main.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class CSurfaceView extends GLSurfaceView {
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private float previousX;
	private float previousY;
	private int earthTextureId;
	private int moonTextureId;
	private SceneRenderer renderer;

	public CSurfaceView(Context context) {
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
			renderer.earth.xAngle += dy * TOUCH_SCALE_FACTOR;
			renderer.earth.zAngle += dx * TOUCH_SCALE_FACTOR;
			this.requestRender();
			break;
		}
		previousX = x;
		previousY = y;
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		CBall earth;
		CBall moon;
		MyCelestial celSmall;
		MyCelestial celBig;

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);
			gl.glEnable(GL10.GL_CULL_FACE);
			gl.glEnable(GL10.GL_LIGHTING);// ¿ªÆô¹âÕÕ
			initSunLight(gl);
			initMaterial(gl);
			earthTextureId = initTextures(gl, R.drawable.earth);
			moonTextureId = initTextures(gl, R.drawable.moon);
			earth = new CBall(6, earthTextureId);
			moon = new CBall(2, moonTextureId);
			celSmall = new MyCelestial(0, 0, 0, 1, 750);
			celBig = new MyCelestial(0, 0, 0, 2, 200);
			new Thread() {
				public void run() {
					while (true) {
						try {
							Thread.sleep(50);
							earth.yAngle += 2 * TOUCH_SCALE_FACTOR;
							moon.yAngle += 2 * TOUCH_SCALE_FACTOR;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			new Thread() {
				public void run() {
					while (true) {
						try {
							Thread.sleep(100);
							celSmall.yAngle += 0.05f;
							if (celSmall.yAngle >= 360) {
								celSmall.yAngle = 0;
							}
							celBig.yAngle += 0.5f;
							if (celBig.yAngle >= 360) {
								celBig.yAngle = 0;
							}
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
			gl.glFrustumf(-ratio * 0.5f, ratio * 0.5f, -0.5f, 0.5f, 1, 50);
		}

		@Override
		public void onDrawFrame(GL10 gl) {

			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			gl.glTranslatef(0, 0, -3.5f);
			gl.glPushMatrix();
			gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 2.5f);
			earth.drawSelf(gl);

			gl.glTranslatef(0, 0, 1.5f);
			gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 1f);
			moon.drawSelf(gl);

			gl.glPopMatrix();

			gl.glPushMatrix();
			gl.glTranslatef(0, -8f, 0);
			celSmall.drawSelf(gl);
			celBig.drawSelf(gl);
			gl.glPopMatrix();

		}

		private void initSunLight(GL10 gl) {
			gl.glEnable(GL10.GL_LIGHT0);
			float[] ambientParams = { 0.05f, 0.05f, 0.02f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams, 0);
			float[] diffuseParams = { 1f, 1f, 0.5f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams, 0);
			float[] specularParams = { 1.0f, 1f, 0.5f, 1f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams, 0);
			float[] pos = { -14.14f, 8.6f, 6f, 0 };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, pos, 0);
		}

		private void initMaterial(GL10 gl) {
			float[] ambientMaterial = { 1f, 1f, 1f, 1f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT,
					ambientMaterial, 0);
			float[] diffuseMaterial = { 1f, 1f, 1f, 1f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE,
					diffuseMaterial, 0);
			float[] specularMaterial = { 1f, 1f, 1f, 1f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR,
					specularMaterial, 0);

			gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 100f);
		}

		private int initTextures(GL10 gl, int textureId) {
			int[] textures = new int[1];
			gl.glGenTextures(1, textures, 0);
			int curTextureId = textures[0];
			gl.glBindTexture(GL10.GL_TEXTURE_2D, curTextureId);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
					GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
					GL10.GL_LINEAR);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
					GL10.GL_REPEAT);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
					GL10.GL_REPEAT);

			InputStream is = getResources().openRawResource(textureId);
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream(is);
			} catch (Exception e) {
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
			return curTextureId;
		}
	}
}
