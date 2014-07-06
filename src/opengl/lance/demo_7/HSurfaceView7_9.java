package opengl.lance.demo_7;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.lance.main.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class HSurfaceView7_9 extends GLSurfaceView {
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;// �Ƕ����ű���
	SceneRenderer renderer;// ������Ⱦ��
	private float previousY;// �ϴεĴ���λ��Y����
	private float previousX;// �ϴεĴ���λ��Y����
	private int lightAngle = 90;// �Ƶĵ�ǰ�Ƕ�

	public HSurfaceView7_9(Context context) {
		super(context);
		renderer = new SceneRenderer(); // ����������Ⱦ��
		setRenderer(renderer); // ������Ⱦ��
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// ������ȾģʽΪ������Ⱦ
	}

	// �����¼��ص�����
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float y = e.getY();
		float x = e.getX();
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dy = y - previousY;// ���㴥�ر�Yλ��
			float dx = x - previousX;// ���㴥�ر�Yλ��
			renderer.drum.xAngle += dy * TOUCH_SCALE_FACTOR;// ������x����ת�Ƕ�
			renderer.drum.zAngle += dx * TOUCH_SCALE_FACTOR;// ������z����ת�Ƕ�
			requestRender();// �ػ滭��
			break;
		}
		previousY = y;// ��¼���ر�λ��
		previousX = x;// ��¼���ر�λ��
		return true;
	}

	public class SceneRenderer implements GLSurfaceView.Renderer {
		public int hyperId;
		public int circleId;
		Drum drum;

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

		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// �رտ�����
			gl.glDisable(GL10.GL_DITHER);
			// �����ض�Hint��Ŀ��ģʽ������Ϊ����Ϊʹ�ÿ���ģʽ
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			// ������Ļ����ɫ��ɫRGBA
			gl.glClearColor(0, 0, 0, 0);
			// ������ɫģ��Ϊƽ����ɫ
			gl.glShadeModel(GL10.GL_SMOOTH);
			// ������Ȳ���
			gl.glEnable(GL10.GL_DEPTH_TEST);
			// ��Ƥ����Id
			hyperId = initTexture(gl, R.drawable.hong);// ����ID
			// ����������Id
			circleId = initTexture(gl, R.drawable.huang);
			drum = new Drum(HSurfaceView7_9.this);
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			gl.glPushMatrix();
			float x = 0f;
			float y = (float) (7 * Math.cos(Math.toRadians(lightAngle)));
			float z = (float) (7 * Math.sin(Math.toRadians(lightAngle)));
			float[] pos = { x, y, z, 0 };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, pos, 0);

			initLight(gl);
			gl.glTranslatef(0, 0, -8f);
			drum.drawSelf(gl);
			closeLight(gl);
			gl.glPopMatrix();

		}

		// ��ʼ����ɫ��
		private void initLight(GL10 gl) {
			gl.glEnable(GL10.GL_LIGHTING);// �������
			gl.glEnable(GL10.GL_LIGHT0);// ��1�ŵ�

			// ����������
			float[] ambientParams = { 0.2f, 0.2f, 0.2f, 1.0f };// ����� RGBA
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams, 0);

			// ɢ�������
			float[] diffuseParams = { 1f, 1f, 1f, 1.0f };// ����� RGBA
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams, 0);

			// ���������
			float[] specularParams = { 1f, 1f, 1f, 1.0f };// ����� RGBA
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams, 0);
		}

		// �رյ�
		private void closeLight(GL10 gl) {
			gl.glDisable(GL10.GL_LIGHT0);
			gl.glDisable(GL10.GL_LIGHTING);
		}

		private int initTexture(GL10 gl, int drawId) {
			int[] texs = new int[1];
			gl.glGenTextures(1, texs, 0);
			int curId = texs[0];
			gl.glBindTexture(GL10.GL_TEXTURE_2D, curId);
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
			Bitmap bitmap;
			try {
				bitmap = BitmapFactory.decodeStream(is);
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
