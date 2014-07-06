package opengl.lance.demo_6;

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

public class RSurfaceView extends GLSurfaceView {
	private SceneRenderer renderer;

	public RSurfaceView(Context context) {
		super(context);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		private RTexture oneoneTex;
		private RTexture fourfourTex;
		private RTexture fourtwoTex;

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glEnable(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);
			int texId = initTexture(gl, R.drawable.xwl);
			oneoneTex = new RTexture(1.5f, 1.5f, texId, 1, 1);
			fourfourTex = new RTexture(1.5f, 1.5f, texId, 4, 4);
			fourtwoTex = new RTexture(1.5f, 1.5f, texId, 4, 2);
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

			gl.glTranslatef(0, 0, -5f);

			gl.glPushMatrix();
			gl.glTranslatef(-3.5f, 0, 0);
			oneoneTex.drawSelf(gl);
			gl.glPopMatrix();

			gl.glPushMatrix();
			gl.glTranslatef(0, 0, 0);
			fourfourTex.drawSelf(gl);
			gl.glPopMatrix();

			gl.glPushMatrix();
			gl.glTranslatef(3.5f, 0, 0);
			fourtwoTex.drawSelf(gl);
			gl.glPopMatrix();

		}

		private int initTexture(GL10 gl, int drawId) {
			int[] trr = new int[1];
			gl.glGenTextures(1, trr, 0);
			int curTexId = trr[0];
			gl.glBindTexture(GL10.GL_TEXTURE_2D, curTexId);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
					GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
					GL10.GL_LINEAR);

			// gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
			// GL10.GL_CLAMP_TO_EDGE);
			// gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
			// GL10.GL_CLAMP_TO_EDGE);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
					GL10.GL_REPEAT);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
					GL10.GL_REPEAT);
			Bitmap bitmap = null;
			InputStream is = getResources().openRawResource(drawId);
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
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			bitmap.recycle();
			return curTexId;
		}
	}

}
