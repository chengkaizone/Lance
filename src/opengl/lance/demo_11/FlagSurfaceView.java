package opengl.lance.demo_11;

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

public class FlagSurfaceView extends GLSurfaceView {

	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;// �Ƕ����ű���
	private float mPreviousY;// �ϴεĴ���λ��Y����
	private float mPreviousX;// �ϴεĴ���λ��X����
	private SceneRenderer mRenderer;// ������Ⱦ��
	private float yAngle = 0;
	private float xAngle = 0;

	public FlagSurfaceView(Context context) {
		super(context);
		mRenderer = new SceneRenderer(); // ����������Ⱦ��
		setRenderer(mRenderer); // ������Ⱦ��
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// ������ȾģʽΪ������Ⱦ
	}

	// �����¼��ص�����
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float y = e.getY();
		float x = e.getX();
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dy = y - mPreviousY;// ���㴥�ر�Yλ��
			float dx = x - mPreviousX;// ���㴥�ر�Xλ��
			yAngle += dx * TOUCH_SCALE_FACTOR;// ������y����ת�Ƕ�
			xAngle += dy * TOUCH_SCALE_FACTOR;// ������x����ת�Ƕ�
			requestRender();// �ػ滭��
		}
		mPreviousY = y;// ��¼���ر�λ��
		mPreviousX = x;// ��¼���ر�λ��
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		FlagRect[] flags = new FlagRect[8];
		int index = 0;

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			gl.glPushMatrix();
			gl.glTranslatef(0, 0, -3f);
			gl.glRotatef(xAngle, 1, 0, 0);
			gl.glRotatef(yAngle, 0, 1, 0);
			flags[index].drawSelf(gl);
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
			gl.glFrustumf(-ratio * 0.4f, ratio * 0.4f, -1 * 0.4f, 1 * 0.4f, 1,
					100);

		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);
			int texId = initTexture(gl, R.drawable.cn_flag);
			new Thread() {
				public void run() {
					while (true) {
						index = (index + 1) % flags.length;
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			// Ϊ8֡ͼƬ�����������---����8֡����ͼƬ
			double dangle = Math.PI * 2 / 8;
			for (int i = 0; i < flags.length; i++) {
				flags[i] = new FlagRect(texId, dangle * i);
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

			InputStream is = getResources().openRawResource(drawableId);
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
}
