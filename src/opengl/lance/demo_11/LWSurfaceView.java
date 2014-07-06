package opengl.lance.demo_11;

import static opengl.lance.demo_11.Constant_3.CAMERA_INI_X;
import static opengl.lance.demo_11.Constant_3.CAMERA_INI_Y;
import static opengl.lance.demo_11.Constant_3.CAMERA_INI_Z;
import static opengl.lance.demo_11.Constant_3.DIRECTION_INI;
import static opengl.lance.demo_11.Constant_3.DISTANCE;
import static opengl.lance.demo_11.Constant_3.yArray;

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

/**
 * ɽ������
 * 
 * @author Administrator
 * 
 */
public class LWSurfaceView extends GLSurfaceView {
	private SceneRenderer renderer;
	float direction = DIRECTION_INI;// ���߷���
	// ���������
	float cx;
	float cy;
	float cz;
	// Ŀ��۲��
	float dx;
	float dy;
	float dz;

	public LWSurfaceView(Context context) {
		super(context);
		cx = CAMERA_INI_X;
		cy = CAMERA_INI_Y;
		cz = CAMERA_INI_Z;

		dx = (float) (cx - Math.sin(direction) * DISTANCE);
		// ���ӹ۲�Ŀ����y����
		dy = CAMERA_INI_Y - 2f;
		dz = (float) (cz - Math.cos(direction) * DISTANCE);
		// ��ʼ���Ҷ�ͼ
		Constant_3.initConstant(this.getResources());
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		Land land;
		Water[] waters;
		private int index = 0;

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			// �����������λ��---���۵�λ����ʼ�����������λ��
			GLU.gluLookAt(gl, cx, cy, cz, dx, dy, dz, 0, 1, 0);
			land.drawSelf(gl);
			waters[index].drawSelf(gl);
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
			gl.glFrustumf(-ratio * 0.7f, ratio * 0.7f, -0.7f * 0.7f,
					1.3f * 0.7f, 1, 400);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// �رտ�����
			gl.glDisable(GL10.GL_DITHER);
			// �����ض�Hint��Ŀ��ģʽ������Ϊ����Ϊʹ�ÿ���ģʽ
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			// ����Ϊ�򿪱������
			gl.glEnable(GL10.GL_CULL_FACE);
			// �������
			gl.glEnable(GL10.GL_BLEND);
			// ����Դ���������Ŀ��������----ʹ�û��
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			// ������Ļ����ɫ��ɫRGBA
			gl.glClearColor(0, 0, 0, 0);
			// ������Ȳ���
			gl.glEnable(GL10.GL_DEPTH_TEST);

			int landId = initTexture(gl, R.drawable.grass);
			int waterId = initTexture(gl, R.drawable.water);
			land = new Land(landId, Constant_3.yArray);
			waters = new Water[16];
			for (int i = 0; i < waters.length; i++) {
				// ˮ�˶���Ȧ��Խ��о�Խ��---������֡������360�ȵ�������
				waters[i] = new Water(waterId, Math.PI * 2 * i / waters.length,
						8, 8, yArray.length / 8);
			}
			new Thread() {
				public void run() {
					while (true) {
						index = (index + 1) % waters.length;
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}

		// ��ʼ������
		public int initTexture(GL10 gl, int drawableId) {
			// ��������ID
			int[] textures = new int[1];
			gl.glGenTextures(1, textures, 0);
			int currTextureId = textures[0];

			gl.glBindTexture(GL10.GL_TEXTURE_2D, currTextureId);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
					GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
					GL10.GL_LINEAR);
			// gl.glTexParameterf(GL10.GL_TEXTURE_2D,
			// GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR_MIPMAP_NEAREST);//mipmap����
			// gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR_MIPMAP_LINEAR);
			// ((GL11)gl).glTexParameterf(GL10.GL_TEXTURE_2D,
			// GL11.GL_GENERATE_MIPMAP, GL10.GL_TRUE);

			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
					GL10.GL_CLAMP_TO_EDGE);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
					GL10.GL_CLAMP_TO_EDGE);
			// gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
			// GL10.GL_REPEAT);//�����ظ�ճ��
			// gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
			// GL10.GL_REPEAT);
			// Bitmap bitmapTmp=BitmapFactory.decodeResource(getResources(),
			// drawableId);
			//
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
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTmp, 0);
			bitmapTmp.recycle();

			return currTextureId;
		}
	}
}
