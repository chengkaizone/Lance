package opengl.lance.demo_11;

import static opengl.lance.demo_11.Constant_4.BALL_SCALE;

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

public class MySurfaceView_4 extends GLSurfaceView {

	private SceneRenderer mRenderer;// ������Ⱦ��

	public MySurfaceView_4(Context context) {
		super(context);
		mRenderer = new SceneRenderer(); // ����������Ⱦ��
		setRenderer(mRenderer); // ������Ⱦ��
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// ������ȾģʽΪ������Ⱦ
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		int lqcTexId;// ��������
		int lqcTMTexId;// ����͸������ ������ ���ƱȽ���ʵ��Ч��
		int basketballTexId;// ��������

		Floor rfloor;// ��͸�������棬���� ���ƱȽ���ʵ��Ч��
		Floor floor;// ��ͨ������
		Ball ball;// ���ڻ��Ƶ���
		BallController ctrl;// ���ڿ��Ƶ���

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
			GLU.gluLookAt(gl, 0.0f, // ����λ�õ�X
					7.0f, // ����λ�õ�Y
					7.0f, // ����λ�õ�Z
					0, // �����򿴵ĵ�X
					0f, // �����򿴵ĵ�Y
					0, // �����򿴵ĵ�Z
					0, 1, 0);
			gl.glTranslatef(0, -2, 0);
			floor.drawSelf(gl);// ���Ƶذ�
			ctrl.drawSelfMirror(gl);// ���ƾ�����

			rfloor.drawSelf(gl);// ��͸���ذ�
			ctrl.drawSelf(gl);// ����ʵ������
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
			gl.glFrustumf(-ratio, ratio, -1, 1, 3, 100);
		}

		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// �رտ�����
			gl.glDisable(GL10.GL_DITHER);
			// �����ض�Hint��Ŀ��ģʽ������Ϊ����Ϊʹ�ÿ���ģʽ
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			// ������Ļ����ɫ��ɫRGBA
			gl.glClearColor(0, 0, 0, 0);
			// ������Ȳ���
			// gl.glEnable(GL10.GL_DEPTH_TEST);
			// �������
			gl.glEnable(GL10.GL_BLEND);
			// ����Դ���������Ŀ��������
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

			basketballTexId = initTexture(gl, R.drawable.basketball);
			lqcTMTexId = initTexture(gl, R.drawable.mdbtm);// ���� ���ƱȽ���ʵ��Ч��
			// ������͸������ ��
			rfloor = new Floor(4, 2.568f, lqcTMTexId);// ���� ���ƱȽ���ʵ��Ч��
			// ��ʼ������
			lqcTexId = initTexture(gl, R.drawable.mdb);
			// ������ͨ��͸�������
			floor = new Floor(4, 2.568f, lqcTexId);
			// �������ڻ��Ƶ���
			ball = new Ball(BALL_SCALE, basketballTexId);
			// �������ڿ��Ƶ���
			ctrl = new BallController(ball, 3f);
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