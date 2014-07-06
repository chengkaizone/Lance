package opengl.lance.demo_15;

import static opengl.lance.demo_15.Constant_2.HEIGHT;
import static opengl.lance.demo_15.Constant_2.SCALE;
import static opengl.lance.demo_15.Constant_2.STARTY;
import static opengl.lance.demo_15.Constant_2.direction;

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
import android.view.KeyEvent;

public class BallSurfaceView extends GLSurfaceView {
	private SceneRenderer renderer;
	static float cx = 0;
	static float cy = 10;
	static float cz = 20;
	static float tx = 0;
	static float ty = 2;
	static float tz = 0;
	float xAngle;
	float ballX;
	float ballZ;
	int keyState = 0;
	public boolean flag = true;

	public BallSurfaceView(Context context) {
		super(context);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		new HandleKeyEvent(this).start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case 19:
			keyState = keyState | 0x1;// 1~up
			break;
		case 20:
			keyState = keyState | 0x2;// 2~down
			break;
		case 21:
			keyState = keyState | 0x4;// 4~left
			break;
		case 22:
			keyState = keyState | 0x8;// 8~right
		case KeyEvent.KEYCODE_BACK:
			flag = false;
			((Sample15_2) getContext()).finish();
			break;
		}
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case 19:
		case 20:
		case 21:
		case 22:
			// 松开按键时恢复转角和状态
			direction = 0;
			keyState = 0;
			break;
		}
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		BallForDraw ball;
		TextureRect_2 floor;

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			GLU.gluLookAt(gl, cx, cy, cz, tx, ty, tz, 0, 1, 0);
			// 移动坐标系
			gl.glPushMatrix();
			gl.glTranslatef(0, 2 * STARTY + HEIGHT + SCALE, 0);
			gl.glPopMatrix();
			// 绘制地板
			gl.glPushMatrix();
			gl.glTranslatef(0, 0, 0);
			floor.drawSelf(gl);
			gl.glPopMatrix();
			// 绘制球体
			gl.glPushMatrix();
			gl.glTranslatef(ballX, 0.5f + SCALE, ballZ);
			gl.glRotatef(xAngle, 1, 0, 0);
			ball.drawSelf(gl);
			gl.glPopMatrix();
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			float ratio = (float) width / height;
			gl.glFrustumf(-ratio, ratio, -1, 1, 1, 100);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);

			int floorId = initTexture(gl, R.drawable.floor);
			int ballId = initTexture(gl, R.drawable.basketball);
			// 创建球体
			ball = new BallForDraw(ballX, ballZ, 11.25f, SCALE, ballId);
			// 创建地板
			floor = new TextureRect_2(20, 0, 30, floorId, new float[] { 0, 0,
					0, 1, 1, 1, 1, 1, 1, 0, 0, 0, });
			new Thread() {
				public void run() {
					while (true) {
						xAngle += 3f;
						try {
							Thread.sleep(30);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}

		public int initTexture(GL10 gl, int drawableId) {
			// 生成纹理ID
			int[] textures = new int[1];
			gl.glGenTextures(1, textures, 0);// 提供未使用的纹理对象名称
			int textureId = textures[0];
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);// 创建和使用纹理对象
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
					GL10.GL_NEAREST);// 指定放大缩小过滤方法
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
					GL10.GL_LINEAR);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
					GL10.GL_REPEAT);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
					GL10.GL_REPEAT);

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
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTmp, 0);// 定义二维纹理
			bitmapTmp.recycle();

			return textureId;
		}

	}

}
