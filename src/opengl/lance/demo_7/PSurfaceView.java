package opengl.lance.demo_7;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.lance.main.R;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class PSurfaceView extends GLSurfaceView {
	private final float TOUCH_SCALE_FACTOR = 180F / 320;
	private SceneRenderer renderer;
	private float lightAngle = 90f;
	private float previousX;
	private float previousY;

	public PSurfaceView(Context context) {
		super(context);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
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
			renderer.para.xAngle += dy * TOUCH_SCALE_FACTOR;// ������x����ת�Ƕ�
			renderer.para.zAngle += dx * TOUCH_SCALE_FACTOR;// ������z����ת�Ƕ�
			requestRender();// �ػ滭��
			break;
		}
		previousY = y;// ��¼���ر�λ��
		previousX = x;// ��¼���ر�λ��
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		Paraboloid para;

		@Override
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

			int texId = initTexture(gl, R.drawable.qinghua);// ����ID
			para = new Paraboloid(5f, 2f, 2f, 10, 20, texId);// ����������

			// ����һ���߳��Զ���ת��Դ
			new Thread() {
				public void run() {
					while (true) {
						lightAngle += 5;// ת����
						renderer.para.yAngle += 2 * TOUCH_SCALE_FACTOR;// ����Y��ת��
						requestRender();// �ػ滭��
						try {
							Thread.sleep(50);// ��Ϣ10ms���ػ�
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
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

			gl.glTranslatef(0, 0, -20f);
			initMaterial(gl);
			initLight(gl);
			para.drawSelf(gl);
			closeLight(gl);
			gl.glPopMatrix();
		}

		private void initLight(GL10 gl) {
			gl.glEnable(GL10.GL_LIGHTING);// �������
			gl.glEnable(GL10.GL_LIGHT1);// ��1�ŵ�

			// ����������
			float[] ambientParams = { 0.2f, 0.2f, 0.2f, 1.0f };// ����� RGBA
			gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, ambientParams, 0);

			// ɢ�������
			float[] diffuseParams = { 1f, 1f, 1f, 1.0f };// ����� RGBA
			gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, diffuseParams, 0);

			// ���������
			float[] specularParams = { 1f, 1f, 1f, 1.0f };// ����� RGBA
			gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPECULAR, specularParams, 0);
		}

		private void closeLight(GL10 gl) {
			gl.glDisable(GL10.GL_LIGHT0);
			gl.glDisable(GL10.GL_LIGHTING);
		}

		private void initMaterial(GL10 gl) {
			// ������
			float ambientMaterial[] = { 248f / 255f, 242f / 255f, 144f / 255f,
					1.0f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT,
					ambientMaterial, 0);
			// ɢ���
			float diffuseMaterial[] = { 248f / 255f, 242f / 255f, 144f / 255f,
					1.0f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE,
					diffuseMaterial, 0);
			// �߹����
			float specularMaterial[] = { 248f / 255f, 242f / 255f, 144f / 255f,
					1.0f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR,
					specularMaterial, 0);
			gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 100.0f);
		}

		private int initTexture(GL10 gl, int drawId) {
			int[] textures = new int[1];
			gl.glGenTextures(1, textures, 0);
			int texId = textures[0];
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
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

			InputStream is = null;
			Bitmap bitmap = null;
			try {
				is = getResources().openRawResource(drawId);
				bitmap = BitmapFactory.decodeStream(is);
			} catch (NotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			bitmap.recycle();
			return texId;
		}
	}

}
