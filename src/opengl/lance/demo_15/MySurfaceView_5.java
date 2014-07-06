package opengl.lance.demo_15;

import static opengl.lance.demo_15.Constant_5.BALL_SCALE;
import static opengl.lance.demo_15.Constant_5.BALL_X;
import static opengl.lance.demo_15.Constant_5.BALL_Y;
import static opengl.lance.demo_15.Constant_5.BALL_Z;
import static opengl.lance.demo_15.Constant_5.UNIT_XOFFSET_B;
import static opengl.lance.demo_15.Constant_5.UNIT_XOFFSET_C;
import static opengl.lance.demo_15.Constant_5.UNIT_YOFFSET_B;
import static opengl.lance.demo_15.Constant_5.UNIT_YOFFSET_C;
import static opengl.lance.demo_15.Constant_5.UNIT_ZOFFSET_B;
import static opengl.lance.demo_15.Constant_5.UNIT_ZOFFSET_C;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.lance.main.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;

public class MySurfaceView_5 extends GLSurfaceView {
	SceneRenderer mRenderer;// ������Ⱦ��
	float cx = 0;// �����xλ��
	float cy = 3;// �����yλ��
	float cz = 10;// �����zλ��

	float tx = 0;// //Ŀ���xλ��
	float ty = 0;// Ŀ���yλ��
	float tz = 0;// Ŀ���zλ��
	ArrayList<BallForControl> alBall;// ���б�
	BallGoThread bgt;// ���˶��߳�
	Cube_5 cube;// ����������

	public MySurfaceView_5(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mRenderer = new SceneRenderer(); // ����������Ⱦ��
		setRenderer(mRenderer); // ������Ⱦ��
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// ������ȾģʽΪ������Ⱦ
		alBall = new ArrayList<BallForControl>();// �������б�
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		int ballId;// ������ID

		Ball ball;// ������
		BallForControl bfc;// ��������ƶ���

		@Override
		public void onDrawFrame(GL10 gl) {
			// TODO Auto-generated method stub
			// �����ɫ����
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			// ���õ�ǰ����Ϊģʽ����
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			// ���õ�ǰ����Ϊ��λ����
			gl.glLoadIdentity();
			// ����cameraλ��
			GLU.gluLookAt(gl, cx, // �������Xλ��
					cy, // �������Yλ��
					cz, // �������Zλ��
					tx, // Ŀ���X
					ty, // Ŀ���Y
					tz, // Ŀ���Z
					0, // UPλ��
					1, 0);

			gl.glPushMatrix();
			gl.glTranslatef(UNIT_XOFFSET_C, UNIT_YOFFSET_C, UNIT_ZOFFSET_C);
			cube.drawSelf(gl);// ����������
			gl.glPopMatrix();

			gl.glPushMatrix();
			for (int i = 0; i < alBall.size(); i++) {
				alBall.get(i).drawSelf(gl);
			}
			gl.glPopMatrix();

		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			// TODO Auto-generated method stub
			// �����Ӵ���С��λ��
			gl.glViewport(0, 0, width, height);
			// ���õ�ǰ����ΪͶӰ����
			gl.glMatrixMode(GL10.GL_PROJECTION);
			// ���õ�ǰ����Ϊ��λ����
			gl.glLoadIdentity();
			// ����͸��ͶӰ�ı���
			float ratio = (float) width / height;
			// ���ô˷����������͸��ͶӰ����
			gl.glFrustumf(-ratio * 0.5f, ratio * 0.5f, -0.5f, 0.5f, 1, 100);

		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// TODO Auto-generated method stub
			// �رտ�����
			gl.glDisable(GL10.GL_DITHER);
			// �����ض�Hint��Ŀ��ģʽ������Ϊ����Ϊʹ�ÿ���ģʽ
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			// ������Ļ����ɫ��ɫRGBA
			gl.glClearColor(0.8f, 0.8f, 0.8f, 0);
			// ������ɫģ��Ϊƽ����ɫ
			gl.glShadeModel(GL10.GL_SMOOTH);
			// ������Ȳ���
			gl.glEnable(GL10.GL_DEPTH_TEST);
			// ����Ϊ�򿪱������
			gl.glEnable(GL10.GL_CULL_FACE);

			ballId = initTexture(gl, R.drawable.bottom);

			cube = new Cube_5(ballId, 1, 2, 1);// �������������
			ball = new Ball(BALL_SCALE, ballId);// ���������
			bfc = new BallForControl(MySurfaceView_5.this, ball,
					-UNIT_XOFFSET_B, UNIT_YOFFSET_B, UNIT_ZOFFSET_B, BALL_X,
					BALL_Y, BALL_Z);
			alBall.add(bfc);
			bgt = new BallGoThread(alBall);
			bgt.start();

		}

	}

	// ��ʼ������
	public int initTexture(GL10 gl, int drawableId) {
		// ��������ID
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);// �ṩδʹ�õ������������
		int textureId = textures[0];
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);// ������ʹ���������
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
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTmp, 0);// �����ά����
		bitmapTmp.recycle();

		return textureId;
	}
}
