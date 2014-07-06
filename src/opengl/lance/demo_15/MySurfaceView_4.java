package opengl.lance.demo_15;

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

public class MySurfaceView_4 extends GLSurfaceView {

	private SceneRenderer mRenderer;// ������Ⱦ��
	float cx = 0;// �����xλ��
	float cy = 10;// �����yλ��
	float cz = 10;// �����zλ��

	float tx = 0;// //Ŀ���xλ��
	float ty = 0;// Ŀ���yλ��
	float tz = 0;// Ŀ���zλ��
	final float UNIT_Z = 2.0f;// �ƶ�����ϵ��λ��

	final float UNIT_OFFSET_X = 4.0f;// ׵��X��λ��
	final float UNIT_OFFSET_Y = 5.0f;// ׵��Y��λ��
	final float UNIT_OFFSET_Z = 0.5f;// ׵��Z��λ��
	ArrayList<LogicControl> al;// ׵������б�
	GoThread gt;// �˶��߳�

	public MySurfaceView_4(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mRenderer = new SceneRenderer(); // ����������Ⱦ��
		setRenderer(mRenderer); // ������Ⱦ��
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// ������ȾģʽΪ������Ⱦ
		al = new ArrayList<LogicControl>();// ��������
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {

		int ZTId;// ׵������ID
		int ZTId_T;// 2׵������ID

		ZhuiTi zhuiTi;// ����׵��
		ZhuiTi zhuiTi_T;// ������һ��׵��

		LogicControl ct;// ����׵���˶�

		@Override
		public void onDrawFrame(GL10 gl) {
			// TODO Auto-generated method stub
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
			GLU.gluLookAt(gl, cx, // �������Xλ��
					cy, // �������Yλ��
					cz, // �������Zλ��
					tx, // Ŀ���X
					ty, // Ŀ���Y
					tz, // Ŀ���Z
					0, // UPλ��
					1, 0);

			gl.glPushMatrix();
			gl.glTranslatef(0, 0, -UNIT_Z);
			for (int i = 0; i < al.size(); i++) {
				al.get(i).drawSelf(gl);// ����׵��
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
			gl.glClearColor(0.8f, 0.8f, 0.8f, 0.8f);
			// ������ɫģ��Ϊƽ����ɫ
			gl.glShadeModel(GL10.GL_SMOOTH);
			// ������Ȳ���
			gl.glEnable(GL10.GL_DEPTH_TEST);
			// ����Ϊ�򿪱������
			gl.glEnable(GL10.GL_CULL_FACE);

			ZTId = initTexture(gl, R.drawable.ground);// ��ʼ������ID
			ZTId_T = initTexture(gl, R.drawable.bottom);

			zhuiTi = new ZhuiTi(ZTId, 1, 2, 1);// ����׵��
			zhuiTi_T = new ZhuiTi(ZTId_T, 1, 1, 1);// ����׵��

			// ct=new
			// LogicControl(zhuiTi,0,0,-7*UNIT_OFFSET_Z,0.0f,0.0f,0.0f);//Z���ϵ��ϰ���
			// al.add(ct);
			ct = new LogicControl(zhuiTi_T, UNIT_OFFSET_X, 0, -UNIT_OFFSET_Z,
					0.0f, 0.0f, 0.0f);// X���ϵ��ϰ���
			al.add(ct);
			// ct=new
			// LogicControl(zhuiTi,0,UNIT_OFFSET_Y,-UNIT_OFFSET_Z,0.0f,0.0f,0.0f);//Y���ϵ��ϰ���
			// al.add(ct);
			ct = new LogicControl(zhuiTi_T, 0, 0, -UNIT_OFFSET_Z, 1.0f, 0.0f,
					0.0f);// ����׵��
			al.add(ct);// �����б���
			ct = new LogicControl(zhuiTi_T, -UNIT_OFFSET_X, 0, -UNIT_OFFSET_Z,
					0.0f, 0.0f, 0.0f);// X���ϵ��ϰ���
			al.add(ct);
			// ct=new
			// LogicControl(zhuiTi,0,-UNIT_OFFSET_Y,-UNIT_OFFSET_Z,0.0f,0.0f,0.0f);//Y���ϵ��ϰ���
			// al.add(ct);
			// ct=new
			// LogicControl(zhuiTi,0,0,4*UNIT_OFFSET_Z,0.0f,0.0f,0.0f);//Z���ϵ��ϰ���
			// al.add(ct);//�����б���
			gt = new GoThread(al);// �����˶��̶߳���
			gt.start();// �����߳�

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
