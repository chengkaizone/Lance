package opengl.lance.demo_11;

import static opengl.lance.demo_11.Constant_1.CAMERA_INI_X;
import static opengl.lance.demo_11.Constant_1.CAMERA_INI_Y;
import static opengl.lance.demo_11.Constant_1.CAMERA_INI_Z;
import static opengl.lance.demo_11.Constant_1.DEGREE_SPAN;
import static opengl.lance.demo_11.Constant_1.DESERT_COLS;
import static opengl.lance.demo_11.Constant_1.DESERT_ROWS;
import static opengl.lance.demo_11.Constant_1.DIRECTION_INI;
import static opengl.lance.demo_11.Constant_1.DISTANCE;
import static opengl.lance.demo_11.Constant_1.MOVE_SPAN;
import static opengl.lance.demo_11.Constant_1.UNIT_SIZE;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.lance.main.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.KeyEvent;

public class MySurfaceView extends GLSurfaceView {
	private SceneRenderer mRenderer;// ������Ⱦ��

	static float direction = DIRECTION_INI;// ���߷���
	static float cx = CAMERA_INI_X;// �����x����
	static float cy = CAMERA_INI_Y;// �����y����
	static float cz = CAMERA_INI_Z;// �����z����

	static float tx = (float) (cx - Math.sin(direction) * DISTANCE);// �۲�Ŀ���x����
	static float ty = CAMERA_INI_Y - 0.4f;// �۲�Ŀ���y����
	static float tz = (float) (cz - Math.cos(direction) * DISTANCE);// �۲�Ŀ���z����

	float ratio;// ͶӰ�ı���

	public MySurfaceView(Context context) {
		super(context);
		mRenderer = new SceneRenderer(); // ����������Ⱦ��
		setRenderer(mRenderer); // ������Ⱦ��
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// ������ȾģʽΪ������Ⱦ
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent e) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			((Sample11_1) getContext()).finish();
			return true;
		}
		if (keyCode == 19 || keyCode == 20) {// ���¼���ʾǰ�������
			float xOffset = 0;// �˲���Xλ��
			float zOffset = 0;// �˲���Zλ��

			switch (keyCode) {
			case 19:// ���ϼ�����ǰ��
				// ǰ��ʱ�˶������뵱ǰ������ͬ
				xOffset = (float) -Math.sin(direction) * MOVE_SPAN;
				zOffset = (float) -Math.cos(direction) * MOVE_SPAN;
				break;
			case 20:// ���¼��������
				// ����ʱ�˶������뵱ǰ�����෴
				xOffset = (float) Math.sin(direction) * MOVE_SPAN;
				zOffset = (float) Math.cos(direction) * MOVE_SPAN;
				break;
			}
			// �����˶����XZֵ
			cx = cx + xOffset;
			cz = cz + zOffset;
		} else {
			switch (keyCode) {
			case 21: // ���������ת��
				direction = direction + DEGREE_SPAN;
				break;
			case 22: // ���Ҵ�����ת��
				direction = direction - DEGREE_SPAN;
				break;
			}
		}
		// �����µĹ۲�Ŀ���XZ����
		tx = (float) (cx - Math.sin(direction) * DISTANCE);// �۲�Ŀ���x����
		tz = (float) (cz - Math.cos(direction) * DISTANCE);// �۲�Ŀ���z����

		// �������������Ƶĳ���
		mRenderer.cg.calDirection();

		// �������ư������ӵ�ľ�������
		Collections.sort(mRenderer.cg.cactuses);

		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		int desertTextureId;// ɳĮ����
		int cactusTextureId;// ����������

		Desert desert;// ɳĮ
		MyCactusGroup cg;// ��������
		Celestial celestialSmall;// С�����ǿհ���
		Celestial celestialBig;// �������ǿհ���

		public void onDrawFrame(GL10 gl) {
			// ����ƽ����ɫ
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
			// ����ɳĮ
			desert.drawSelf(gl);
			// �����ǿ�
			celestialSmall.drawSelf(gl);
			celestialBig.drawSelf(gl);
			// ����������
			cg.drawSelf(gl);
		}

		public void onSurfaceChanged(GL10 gl, int width, int height) {
			// �����Ӵ���С��λ��
			gl.glViewport(0, 0, width, height);
			// ����ͶӰ�ı���
			ratio = (float) width / height;
			// ���õ�ǰ����ΪͶӰ����
			gl.glMatrixMode(GL10.GL_PROJECTION);
			// ���õ�ǰ����Ϊ��λ����
			gl.glLoadIdentity();
			// ���ô˷����������͸��ͶӰ����
			gl.glFrustumf(-ratio * 0.6f, ratio * 0.6f, -1 * 0.6f, 1 * 0.6f, 1,
					100);
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
			// �������
			gl.glEnable(GL10.GL_BLEND);
			// ���û�ϲ���
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			// ����Ϊ�򿪱������
			gl.glEnable(GL10.GL_CULL_FACE);

			// ��ʼ������
			desertTextureId = initTexture(gl, R.drawable.desert);
			cactusTextureId = initTexture(gl, R.drawable.cactus);

			// ��ʼ��ɳĮ
			desert = new Desert(0, 0, 1.0f, 0, desertTextureId, DESERT_COLS,
					DESERT_ROWS);
			// ��ʼ�������Ƽ���
			cg = new MyCactusGroup(cactusTextureId);
			// �����ǿ�
			celestialSmall = new Celestial(UNIT_SIZE * DESERT_COLS / 2,
					UNIT_SIZE * DESERT_ROWS / 2, 1, 0, 350);
			celestialBig = new Celestial(UNIT_SIZE * DESERT_COLS / 2, UNIT_SIZE
					* DESERT_ROWS / 2, 2, 0, 150);

			new Thread() {// ��ʱת���ǿյ��߳�
				public void run() {
					while (true) {
						celestialSmall.yAngle += 0.5;
						if (celestialSmall.yAngle >= 360) {
							celestialSmall.yAngle = 0;
						}
						celestialBig.yAngle += 0.5;
						if (celestialBig.yAngle >= 360) {
							celestialBig.yAngle = 0;
						}
						try {
							Thread.sleep(100);
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
		gl.glGenTextures(1, textures, 0); // �ṩ δʹ�õ������������
		int currTextureId = textures[0];
		gl.glBindTexture(GL10.GL_TEXTURE_2D, currTextureId);// ������ʹ���������
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);// ָ���Ŵ���С���˷���
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
