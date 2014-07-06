package opengl.lance.demo_13;

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

class MySurfaceView extends GLSurfaceView {

	private final float TOUCH_SCALE_FACTOR = (float) Math.PI / 320;// �Ƕ����ű���
	private SceneRenderer mRenderer;// ������Ⱦ��

	private float mPreviousY;// �ϴεĴ���λ��Y����
	private float mPreviousX;// �ϴεĴ���λ��X����

	static float yAngle = 0;// �������y����ת�ĽǶ�
	static float xAngle = 0;// �������x����ת�ĽǶ�

	static float cz = 2.35f;// �����z����
	static float cy = 0.8f;// �����y����
	static float cx = 0.0f;// �����x����

	static float tz = 0.0f;// �۲�Ŀ���z����
	static float ty = 0.6f;// �۲�Ŀ���y����
	static float tx = 0.0f;// �۲�Ŀ���x����

	static final float distance = 2.358f;// �������۲�Ŀ���ľ���

	public MySurfaceView(Context context) {
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
			xAngle += dy * TOUCH_SCALE_FACTOR;// ��X��ת���Ƕ�
			yAngle -= dx * TOUCH_SCALE_FACTOR;// ��Y��ת���Ƕ�

			// ����������ת���ĽǶȼ����������λ��
			cy = (float) (distance * Math.sin(xAngle)) + ty;
			cx = (float) (distance * Math.cos(xAngle) * Math.sin(yAngle)) + tx;
			cz = (float) (distance * Math.cos(xAngle) * Math.cos(yAngle)) + tz;
		}
		mPreviousY = y;// ��¼���ر�λ��
		mPreviousX = x;// ��¼���ر�λ��
		return true;
	}

	@Override
	public void onPause() {
		super.onPause();
		mRenderer.fw.fwt.flag = false;// ֹͣ��ʱ�˶�����������ӵ��߳�
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		int fuTexId;// �̻���׶����ID
		int[] ptexIdArray = new int[6];// �����������ID
		Pyramid pyramid;// �̻���׶
		FireWorks fw;// �������ϵͳ

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

			// ����cameraλ��
			GLU.gluLookAt(gl, cx, // ����λ�õ�X
					cy, // ����λ�õ�Y
					cz, // ����λ�õ�Z
					tx, // �����򿴵ĵ�X
					ty, // �����򿴵ĵ�Y
					tz, // �����򿴵ĵ�Z
					0, 1, 0);

			fw.drawSelf(gl);// �����̻�����
			// �����׶��Y��������һ��
			gl.glTranslatef(0, -0.7f, 0f);
			pyramid.drawSelf(gl);// ���������׶
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
			gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);

			// ��������
			fuTexId = initTexture(gl, R.drawable.fu);
			ptexIdArray[0] = initTexture(gl, R.drawable.partical_red);
			ptexIdArray[1] = initTexture(gl, R.drawable.partical_green);
			ptexIdArray[2] = initTexture(gl, R.drawable.partical_blue);
			ptexIdArray[3] = initTexture(gl, R.drawable.partical_purple);
			ptexIdArray[4] = initTexture(gl, R.drawable.partical_yellow);
			ptexIdArray[5] = initTexture(gl, R.drawable.partical_cyan);

			// ���������׶
			pyramid = new Pyramid(0.6f, fuTexId);
			// �����������ϵͳ
			fw = new FireWorks(ptexIdArray);
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
