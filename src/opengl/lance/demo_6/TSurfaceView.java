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

public class TSurfaceView extends GLSurfaceView {
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private float previousX;
	private float previousY;
	private int textureId;
	private boolean smoothFlag;
	private int redLight;
	private int greenLight;
	private SceneRenderer renderer;

	public TSurfaceView(Context context) {
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
			renderer.tb.xAngle += dy * TOUCH_SCALE_FACTOR;
			renderer.tb.zAngle += dx * TOUCH_SCALE_FACTOR;
			this.requestRender();
			break;
		}
		previousX = x;
		previousY = y;
		return true;
	}

	public boolean isSmoothFlag() {
		return smoothFlag;
	}

	public void setSmoothFlag(boolean smoothFlag) {
		this.smoothFlag = smoothFlag;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		TBall tb;

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);
			gl.glEnable(GL10.GL_LIGHTING);// ¿ªÆô¹âÕÕ
			initRedLight(gl);
			initGreenLight(gl);
			initMaterial(gl);
			textureId = initTextures(gl, R.drawable.duke);
			tb = new TBall(5, textureId);
			new Thread() {
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					while (true) {
						try {
							Thread.sleep(50);
							redLight += 5;
							greenLight += 5;
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
			gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			if (smoothFlag) {
				gl.glShadeModel(GL10.GL_SMOOTH);
			} else {
				gl.glShadeModel(GL10.GL_FLAT);
			}
			float xrPos = (float) (7 * Math.cos(Math.toRadians(redLight)));
			float zrPos = (float) (7 * Math.sin(Math.toRadians(redLight)));
			float[] redPos = { xrPos, 0, zrPos, 1 };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, redPos, 0);

			float xgPos = (float) (7 * Math.cos(Math.toRadians(greenLight)));
			float zgPos = (float) (7 * Math.cos(Math.toRadians(greenLight)));
			float[] greenPos = { xgPos, 0, zgPos, 1 };
			gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, greenPos, 0);

			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			gl.glTranslatef(0, 0, -2.5f);
			gl.glPushMatrix();
			tb.drawSelf(gl);
			gl.glPopMatrix();
		}

		private void initRedLight(GL10 gl) {
			gl.glEnable(GL10.GL_LIGHT0);
			float[] ambientParams = { 0.2f, 0.2f, 0.2f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams, 0);
			float[] diffuseParams = { 0.5f, 0.2f, 0.2f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams, 0);
			float[] specularParams = { 1.0f, 1f, 1f, 1f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams, 0);
		}

		private void initGreenLight(GL10 gl) {
			gl.glEnable(GL10.GL_LIGHT1);
			float[] ambientParams = { 0.1f, 0.2f, 0.1f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, ambientParams, 0);
			float[] diffuseParams = { 0.1f, 0.5f, 0.1f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, diffuseParams, 0);
			float[] specularParams = { 1.0f, 1f, 1f, 1f };
			gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPECULAR, specularParams, 0);
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
