package opengl.lance.demo_15;

import static opengl.lance.demo_15.Constant_7.CAMERA_DISTANCE;
import static opengl.lance.demo_15.Constant_7.CUBE_X;
import static opengl.lance.demo_15.Constant_7.CUBE_Y;
import static opengl.lance.demo_15.Constant_7.CUBE_Z;

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
import android.view.KeyEvent;
import android.view.MotionEvent;

public class SSurfaceView extends GLSurfaceView {
	// ���������
	float cx = 0;
	float cy = 1;
	float cz = 0;
	// Ŀ�������
	float tx = 0;
	float ty = 0;
	float tz = 0;
	// �۲�Ŀ�������
	float left;
	float right;
	float top;
	float bottom;
	float near;
	float far;
	// ������Ƕ�
	float cameraAlpha = 70;
	{
		// ��ʼ��Ŀ���������
		tx = (float) (cx - CAMERA_DISTANCE
				* Math.sin(Math.toRadians(cameraAlpha)));
		ty = cy;
		tz = (float) (cz - CAMERA_DISTANCE
				* Math.cos(Math.toRadians(cameraAlpha)));
	}
	// ��������������״
	ArrayList<Shape> shapes;
	// ����������ľ���
	ArrayList<Float> alDistance;
	// ������Ⱦ��
	private SceneRenderer renderer;

	public SSurfaceView(Context context) {
		super(context);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		// �������е�ͼ����
		shapes = new ArrayList<Shape>();
		// �������ߴ����������̾���
		alDistance = new ArrayList<Float>();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			// ����ϴα�������ߴ�������ľ���
			alDistance.clear();
			// �����~��Ч�ľ���ֵ
			float bodyDistance = Float.POSITIVE_INFINITY;
			int bodyFlag = 0;
			// ѭ�����������е�~�����߽�����뵽������~ÿ�����嶼�ᱣ��һ��ֵ~û�н�������屣���ֵΪ�����
			for (int i = 0; i < shapes.size(); i++) {
				shapes.get(i).setHilight(false);
				// ��ȡ����ͼ�θ��������ϵ���󳤶�
				float[] minMax = shapes.get(i).findMinMax();
				// ��ȡ����ͼ��������ϵ�е����ĵ�
				float[] mid = shapes.get(i).findMid();
				// �����a/b����ľ���
				float[] bPosition = CalUtil.calculateABPosition(x,// ������x����
						y,// ������y����
						Sample15_7.screenWidth,// ��Ļ��
						Sample15_7.screenHeight,// ��Ļ��
						left, // �ӽ����
						top, // �ӽ��ϱ�
						near,// �ӽ�nearֵ
						far,// farֵ
						cameraAlpha,// ������Ƕ�
						// ���������
						cx, cy, cz);
				// �ж������������Ƿ��ཻ������ཻ����near����~�粻�ཻ����Ϊ�����
				float tempDistance = IntersectUtil.isIntersectant(
				// ������������
						mid[0], mid[1], mid[2],
						// �����
						minMax[0] / 2, minMax[1] / 2, minMax[2] / 2,
						// A������
						bPosition[0], bPosition[1], bPosition[2],
						// B������
						bPosition[3], bPosition[4], bPosition[5]);
				// ���ཻ�������nearֵ���뼯��
				alDistance.add(tempDistance);
			}
			// ѭ���������ߴ�����ͼ����
			for (int i = 0; i < alDistance.size(); i++) {
				// ��ȡnear����
				float tempA = alDistance.get(i);
				// ѭ���жϸ�������ͼ���Ƿ������Ч����
				if (tempA < bodyDistance) {
					// ��¼��Сֵ
					bodyDistance = tempA;
					// ��¼��С����ֵ
					bodyFlag = i;
				}
			}
			System.out.println("distance:" + bodyDistance);
			// ���������벻�������~�Ŵ�����
			if (!Float.isInfinite(bodyDistance)) {
				// ����ͼ�δ�С
				shapes.get(bodyFlag).setHilight(true);
				System.out.println("���Ƶ�ͼ���ࣺ" + shapes.get(bodyFlag).getClass());
			}
			break;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == 4) {
			return false;
		}
		switch (keyCode) {
		// �������
		case 21:
			cameraAlpha += 5f;
			tx = (float) (cx - CAMERA_DISTANCE
					* Math.sin(Math.toRadians(cameraAlpha)));
			tz = (float) (cz - CAMERA_DISTANCE
					* Math.cos(Math.toRadians(cameraAlpha)));
			break;
		// �����Ҽ�
		case 22:
			cameraAlpha -= 5f;
			tx = (float) (cx - CAMERA_DISTANCE
					* Math.sin(Math.toRadians(cameraAlpha)));
			tz = (float) (cx - CAMERA_DISTANCE
					* Math.cos(Math.toRadians(cameraAlpha)));
			break;
		}
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		Shape shape;

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			GLU.gluLookAt(gl, cx, cy, cz, tx, ty, tz, 0, 1, 0);

			gl.glPushMatrix();
			for (int i = 0; i < shapes.size(); i++) {
				gl.glPushMatrix();
				shapes.get(i).drawSelf(gl);
				gl.glPopMatrix();
			}
			gl.glPopMatrix();
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			float ratio = (float) width / height;
			left = right = ratio * 0.5f;
			top = bottom = 0.5f;
			near = 1;
			far = 100;
			// ǰ4������ȷ�����Ӵ��ڵĴ�С~������������������ͼ�ε����
			gl.glFrustumf(-left, right, -bottom, top, near, far);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0.8f, 0.8f, 0.8f, 0);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);
			// �����������
			gl.glEnable(GL10.GL_CULL_FACE);
			int cubeId = initTexture(gl, R.drawable.stone);
			shape = new Pillar(cubeId, CUBE_X, CUBE_Y, CUBE_Z, -10f, 0, -4f);
			shapes.add(shape);
			shape = new Pillar(cubeId, CUBE_X, CUBE_Y, CUBE_Z, -10f, 4, -4f);
			shapes.add(shape);
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
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTmp, 0);// �����ά����
			bitmapTmp.recycle();

			return textureId;
		}
	}
}
