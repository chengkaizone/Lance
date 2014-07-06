package opengl.lance.demo_8;

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

public class MySurfaceView_4 extends GLSurfaceView {
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;// �Ƕ����ű���
	private SceneRenderer mRenderer;// ������Ⱦ��
	float mPreviousY;
	float mPreviousX;

	public MySurfaceView_4(Context context) {
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
			float dx = x - mPreviousX;// ���㴥�ر�Yλ��
			mRenderer.angleY += dx * TOUCH_SCALE_FACTOR;// ������Y����ת�Ƕ�
		}
		mPreviousY = y;// ��¼���ر�λ��
		mPreviousX = x;// ��¼���ر�λ��
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		// ��Y����ת�ĽǶ�
		float angleY = 0;
		// ��ת������
		float vx = 0.70711f;
		float vz = 0.70711f;
		float vy = 0f;
		// ����ת����ת�Ƕ�
		float angleAxis = 0;
		// ����Id
		int bigTexId;
		int smallTexId;
		// ������
		MyCube c;

		public SceneRenderer() {
			new Thread() {
				public void run() {
					while (true) {
						angleAxis = (angleAxis + 2f) % 360.0f;
						try {
							Thread.sleep(40);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}

		public void onDrawFrame(GL10 gl) {
			// ����Ϊ�򿪱������
			gl.glEnable(GL10.GL_CULL_FACE);
			// ������ɫģ��Ϊƽ����ɫ
			gl.glShadeModel(GL10.GL_SMOOTH);
			// �����ɫ��������Ȼ���
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			// ���õ�ǰ����Ϊģʽ����
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			// ���õ�ǰ����Ϊ��λ����
			gl.glLoadIdentity();
			// ��������Զ
			gl.glTranslatef(0, 0f, -8f);
			// ������ת
			gl.glRotatef(angleY, 0, 1, 0);
			// ��ָ������ת
			gl.glRotatef(angleAxis, vx, vy, vz);
			// ���Ʒ���
			c.drawSelf(gl);
		}

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
			gl.glFrustumf(-ratio, ratio, -1, 1, 1, 30);
		}

		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// �رտ�����
			gl.glDisable(GL10.GL_DITHER);
			// �����ض�Hint��Ŀ��ģʽ������Ϊ����Ϊʹ�ÿ���ģʽ
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			// ������Ļ����ɫ��ɫRGBA
			gl.glClearColor(0, 0, 0, 0);
			// ������Ȳ���
			gl.glEnable(GL10.GL_DEPTH_TEST);

			// ��������
			bigTexId = initTexture(gl, R.drawable.cubebig);
			smallTexId = initTexture(gl, R.drawable.cubesmall);
			// ����������
			c = new MyCube(1, smallTexId, bigTexId);
			// ����һ���߳̽���������ָ������ת
			new Thread() {
				public void run() {
					while (true) {
						angleAxis = (angleAxis + 2f) % 360.0f;
						try {
							Thread.sleep(40);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
	}

	// ��ʼ������
	public int initTexture(GL10 gl, int drawableId)// textureId
	{
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
