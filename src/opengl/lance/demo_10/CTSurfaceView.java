package opengl.lance.demo_10;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.lance.main.R;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

public class CTSurfaceView extends GLSurfaceView {
	private SceneRenderer mRenderer;// 场景渲染器

	public CTSurfaceView(Context context) {
		super(context);
		mRenderer = new SceneRenderer(); // 创建场景渲染器
		setRenderer(mRenderer); // 设置渲染器
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// 设置渲染模式为主动渲染
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		final int ONE = 65535;
		ColorRect red;
		ColorRect green;
		TextureRect tian;
		TextureRect qiang;

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);

			int tid = initTexture(gl, R.drawable.top);
			int qid = initTexture(gl, R.drawable.base);

			tian = new TextureRect(tid);
			qiang = new TextureRect(qid);
			red = new ColorRect(ONE, 0, 0, ONE * 3 / 5);
			green = new ColorRect(0, ONE, 0, ONE * 2 / 3);
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
			// 这里注意图片的前后与孩子顺序无关；只与距离的远近有关
			gl.glPushMatrix();
			gl.glTranslatef(0, 0, -1.8f);
			// 开启混合
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_COLOR, GL10.GL_ONE_MINUS_SRC_COLOR);
			qiang.drawSelf(gl);
			gl.glDisable(GL10.GL_BLEND);
			gl.glPopMatrix();

			gl.glPushMatrix();
			gl.glTranslatef(-0.9f, -0.9f, -2f);
			tian.drawSelf(gl);
			gl.glPopMatrix();

			gl.glPushMatrix();
			gl.glTranslatef(-0.5f, 0.6f, -1.9f);
			green.drawSelf(gl);
			gl.glPopMatrix();

			gl.glPushMatrix();
			gl.glTranslatef(0.5f, -0.5f, -1.9f);
			red.drawSelf(gl);
			gl.glPopMatrix();
		}

		private int initTexture(GL10 gl, int drawId) {
			int[] texs = new int[1];
			gl.glGenTextures(1, texs, 0);
			int curId = texs[0];
			gl.glBindTexture(GL10.GL_TEXTURE_2D, curId);

			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
					GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
					GL10.GL_LINEAR);

			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
					GL10.GL_REPEAT);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
					GL10.GL_REPEAT);
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

			return curId;
		}

	}
}
