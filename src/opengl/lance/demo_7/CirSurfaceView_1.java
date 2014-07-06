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

public class CirSurfaceView_1 extends GLSurfaceView {
	private final float TOUCH_SCALE_FACTOR = 180F / 320;
	private SceneRenderer renderer;
	private float previousX;
	private float previousY;
	private int lightAngle = 90;

	public CirSurfaceView_1(Context context) {
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
			renderer.cirque.xAngle += dy * TOUCH_SCALE_FACTOR;
			renderer.cirque.zAngle += dx * TOUCH_SCALE_FACTOR;
			this.requestRender();
			break;
		}
		previousX = x;
		previousY = y;
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		Cirque cirque;

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);

			int texId = buildTexId(gl, R.drawable.qinghua);
			cirque = new Cirque(12f, 12f, 10f, 2f, texId);
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			float ratio = (float) width / height;
			gl.glFrustumf(-ratio, ratio, -1, 1, 1, 100);
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			gl.glPushMatrix();
			float x = 0f;
			float y = (float) (7 * Math.sin(Math.toRadians(lightAngle)));
			float z = (float) (7 * Math.cos(Math.toRadians(lightAngle)));
			float[] pos = { x, y, z, 0 };// 定位光
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, pos, 0);

			gl.glTranslatef(0, 0, -25f);
			initMaterial(gl);
			initLight(gl);
			cirque.drawSelf(gl);
			closeLight(gl);

			gl.glPopMatrix();
		}

		private void initLight(GL10 gl) {
			gl.glEnable(GL10.GL_LIGHTING);
			gl.glEnable(GL10.GL_LIGHT0);

			float[] ambient = { 0.1f, 0.1f, 0.1f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambient, 0);
			float[] diffuse = { 0.5f, 0.5f, 0.5f, 1.0f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuse, 0);
			float[] specular = { 1.0f, 1f, 1f, 1f };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specular, 0);
		}

		private void closeLight(GL10 gl) {
			gl.glDisable(GL10.GL_LIGHT0);
			gl.glDisable(GL10.GL_LIGHTING);
		}

		private void initMaterial(GL10 gl) {
			float[] ambient = { 248f / 255, 242f / 255, 142f / 255, 1f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, ambient, 0);
			float[] diffuse = { 248f / 255, 242f / 255, 142f / 255, 1f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, diffuse, 0);
			float[] specular = { 248f / 255, 242f / 255, 142f / 255, 1f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, specular,
					0);
			gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 100f);
		}

		// 注意纹理图片不能使jpg格式文件
		private int buildTexId(GL10 gl, int drawId) {
			int[] texs = new int[1];
			gl.glGenTextures(1, texs, 0);
			int texId = texs[0];
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
					GL10.GL_LINEAR_MIPMAP_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
					GL10.GL_LINEAR_MIPMAP_LINEAR);
			((GL11) gl).glTexParameterf(GL10.GL_TEXTURE_2D,
					GL11.GL_GENERATE_MIPMAP, GL10.GL_TRUE);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
					GL10.GL_REPEAT);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
					GL10.GL_REPEAT);
			InputStream is = getResources().openRawResource(drawId);
			Bitmap bitmap = null;
			try {
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
			// //可以直接通过简单的工厂方法获取位图对象
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			bitmap.recycle();
			return texId;
		}
	}

}
