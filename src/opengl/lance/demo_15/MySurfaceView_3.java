package opengl.lance.demo_15;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.lance.main.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class MySurfaceView_3 extends GLSurfaceView {

	private SceneRenderer mRender;

	public MySurfaceView_3(Context context) {
		super(context);
		mRender = new SceneRenderer();// ����������Ⱦ��
		setRenderer(mRender);// ������Ⱦ��
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// ����Ϊ�Զ���Ⱦ
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (x > 12 && x < 48 && y > 371 && y < 404) {
				mRender.diamond.yOffset += 2f;
			}
			if (x > 268 && x < 305 && y > 371 && y < 404) {
				mRender.diamond.yOffset -= 2f;
			}
			break;
		}
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		Diamond diamond;
		TextureRect_3 upButton;
		TextureRect_3 downButton;
		int upTextureId;
		int downTextureId;

		public SceneRenderer() {
		}

		@Override
		public void onDrawFrame(GL10 gl) {

			// ������Ȳ���
			gl.glEnable(GL10.GL_DEPTH_TEST);
			// ������ɫģ��Ϊƽ����ɫ
			gl.glShadeModel(GL10.GL_SMOOTH);
			// �����ɫ��������Ȼ���
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			// ���õ�ǰ����Ϊģʽ����
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			// ���õ�ǰ����Ϊ��λ����
			gl.glLoadIdentity();

			GLU.gluLookAt(gl, 0, 2, 0, 0, 0, -5, 0, 1, 0);// �����λ��

			gl.glPushMatrix();
			gl.glTranslatef(0, 0, -9);
			diamond.drawSelf(gl);
			gl.glPopMatrix();

			gl.glPushMatrix();
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			// ���õ�ǰ����Ϊ��λ����
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glTranslatef(-3, -4, -5);
			upButton.drawSelf(gl);
			gl.glPopMatrix();
			gl.glPushMatrix();
			gl.glTranslatef(3, -4, -5);
			downButton.drawSelf(gl);
			gl.glPopMatrix();
			gl.glPopMatrix();

		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {

			// �����Ӵ���С��λ��
			gl.glViewport(0, 0, width, height);
			// ���õ�ǰ����ΪͶӰ����
			gl.glMatrixMode(GL10.GL_PROJECTION);
			// ���õ�ǰ����Ϊ��λ����
			gl.glLoadIdentity();
			// ����͸��ͶӰ�ı���
			float ratio = (float) width / height;
			// ���ô˷����������͸��ͶӰ����
			gl.glFrustumf(-ratio, ratio, -1, 1, 1, 100);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {

			// �رտ�����
			gl.glDisable(GL10.GL_DITHER);
			// �����ض�Hint��Ŀ��ģʽ������Ϊ����Ϊʹ�ÿ���ģʽ
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			// ������Ļ����ɫ��ɫRGBA
			gl.glClearColor(0, 0, 0, 0);
			// ������Ȳ���
			gl.glEnable(GL10.GL_DEPTH_TEST);
			gl.glShadeModel(GL10.GL_SMOOTH);

			upTextureId = initTexture(gl, R.drawable.up);
			downTextureId = initTexture(gl, R.drawable.down);
			upButton = new TextureRect_3(upTextureId);
			downButton = new TextureRect_3(downTextureId);
			diamond = new Diamond();

			new Thread() {
				public void run() {
					while (true) {
						diamond.yAngle += 5f;
						try {
							sleep(20);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			}.start();
		}
	}

	// ��ʼ������
	public int initTexture(GL10 gl, int drawableId) {
		// ��������ID
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		int currTextureId = textures[0];
		gl.glBindTexture(GL10.GL_TEXTURE_2D, currTextureId);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_CLAMP_TO_EDGE);

		InputStream is = this.getResources().openRawResource(drawableId);
		Bitmap bitmapTmp;
		try {
			bitmapTmp = BitmapFactory.decodeStream(is);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTmp, 0);
		bitmapTmp.recycle();

		return currTextureId;
	}
}
