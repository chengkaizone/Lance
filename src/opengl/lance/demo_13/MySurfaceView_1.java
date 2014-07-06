package opengl.lance.demo_13;

import static opengl.lance.demo_13.Constant_1.HEIGHT;
import static opengl.lance.demo_13.Constant_1.LENGTH;
import static opengl.lance.demo_13.Constant_1.SCALE;
import static opengl.lance.demo_13.Constant_1.STARTY;
import static opengl.lance.demo_13.Constant_1.WIDTH;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.lance.main.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;

public class MySurfaceView_1 extends GLSurfaceView {
	float cx = 0;// �����xλ��
	float cy = 10;// �����yλ��
	float cz = 30;// �����zλ��

	float tx = 0;// //Ŀ���xλ��
	float ty = 2;// Ŀ���yλ��
	float tz = 0;// Ŀ���zλ��
	private SceneRenderer mRenderer;

	public MySurfaceView_1(Context context) {
		super(context);
		mRenderer = new SceneRenderer(); // ����������Ⱦ��
		setRenderer(mRenderer); // ������Ⱦ��
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// ������ȾģʽΪ������Ⱦ
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		int cubeTextureId;// ����������ID
		int ballTextureId;// ������ID
		int floorTextureId;// ��������
		Ball_1 ball;// ��
		Floor_1 floor;// ����
		Cube_1 cubeTexture;// ����������
		BallMoveThread_1 bgt;// ���˶��߳�
		List<BallController_1> albfc = new ArrayList<BallController_1>();

		// @Override
		public void onDrawFrame(GL10 gl) {
			// ����ƽ����ɫ
			gl.glShadeModel(GL10.GL_SMOOTH);
			// ����Ϊ�򿪱������
			gl.glEnable(GL10.GL_CULL_FACE);
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
			gl.glPushMatrix();// ��ȡ������
			gl.glTranslatef(0, 2 * STARTY + HEIGHT + SCALE, 0);// �ƶ�������
			// gl.glRotatex(90, 1, 0, 0);
			cubeTexture.drawSelf(gl);// ������������
			gl.glPopMatrix();// �ͷ�������

			gl.glPushMatrix();
			gl.glTranslatef(0, 0, 0);
			floor.drawSelf(gl);
			gl.glPopMatrix();

			gl.glPushMatrix();
			for (BallController_1 bfcc : albfc) {
				bfcc.drawSelf(gl);
			}
			gl.glPopMatrix();
		}

		// @Override
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
			gl.glFrustumf(-ratio * 0.5f, ratio * 0.5f, -0.5f, 0.5f, 1, 100);
		}

		// @Override
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
			// ����Ϊ�򿪱������
			gl.glEnable(GL10.GL_CULL_FACE);

			cubeTextureId = initTexture(gl, R.drawable.base);// ��ʼ������
			cubeTexture = new Cube_1(cubeTextureId, HEIGHT, LENGTH, WIDTH);// ������������
			ballTextureId = initTexture(gl, R.drawable.basketball_blue);// ��ʼ��������
			ball = new Ball_1(11.25f, SCALE, ballTextureId);// ������
			floorTextureId = initTexture(gl, R.drawable.floor);

			floor = new Floor_1(20, 30, floorTextureId);
			albfc.add(new BallController_1(ball, 0, 0, 0.05f, 0));
			albfc.add(new BallController_1(ball, -1, 0, 0, 0.05f));
			albfc.add(new BallController_1(ball, 1, 1, 0.05f, 0.05f));
			bgt = new BallMoveThread_1(albfc);
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
