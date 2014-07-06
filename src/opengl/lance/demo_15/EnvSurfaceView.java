package opengl.lance.demo_15;

import static opengl.lance.demo_15.Constant_8.BALL_R;
import static opengl.lance.demo_15.Constant_8.CAMERA_INI_X;
import static opengl.lance.demo_15.Constant_8.CAMERA_INI_Y;
import static opengl.lance.demo_15.Constant_8.CAMERA_INI_Z;
import static opengl.lance.demo_15.Constant_8.DEGREE_SPAN;
import static opengl.lance.demo_15.Constant_8.DIRECTION_INI;
import static opengl.lance.demo_15.Constant_8.DISTANCE;
import static opengl.lance.demo_15.Constant_8.FOG_D;
import static opengl.lance.demo_15.Constant_8.MOVE_SPAN;
import static opengl.lance.demo_15.Constant_8.initConstant;
import static opengl.lance.demo_15.Constant_8.yArray;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.lance.main.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.KeyEvent;

public class EnvSurfaceView extends GLSurfaceView {
	static float direction = DIRECTION_INI;
	static float cx;
	static float cy;
	static float cz;
	static float tx;
	static float ty;
	static float tz;
	private SceneRenderer renderer;

	public EnvSurfaceView(Context context) {
		super(context);
		initConstant(getResources());
		cx = CAMERA_INI_X;
		cy = CAMERA_INI_Y;
		cz = CAMERA_INI_Z;
		tx = (float) (cx - Math.sin(direction) * DISTANCE);
		ty = CAMERA_INI_Y - 0.2f;
		tz = (float) (cz - Math.cos(direction) * DISTANCE);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == 4) {
			// 0~正常终止
			System.exit(0);
			return false;
		}
		// 处理上下键
		if (keyCode == 19 || keyCode == 20) {
			float xOffset = 0;
			float zOffset = 0;
			switch (keyCode) {
			case 19:
				xOffset = -(float) Math.sin(direction) * MOVE_SPAN;
				zOffset = -(float) Math.cos(direction) * MOVE_SPAN;
				break;
			case 20:
				xOffset = (float) Math.sin(direction) * MOVE_SPAN;
				zOffset = (float) Math.cos(direction) * MOVE_SPAN;
				break;
			}
			cx += xOffset;
			cz += zOffset;
		} else if (keyCode == 21 || keyCode == 22) {// 处理左右键
			switch (keyCode) {
			case 21:
				direction += DEGREE_SPAN;
				break;
			case 22:
				direction -= DEGREE_SPAN;
				break;
			}
			tx = (float) (cx - Math.sin(direction) * DISTANCE);
			tz = (float) (cz - Math.cos(direction) * DISTANCE);
		}
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		Water[] waters = new Water[16];
		LandForm landForm;
		SkyBall sky;
		int waterIndex = 0;

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			GLU.gluLookAt(gl, cx, cy, cz, tx, ty, tz, 0, 1, 0);
			gl.glPushMatrix();
			landForm.drawSelf(gl);
			waters[waterIndex].drawSelf(gl);
			gl.glPopMatrix();

			gl.glPushMatrix();
			initFog(gl);
			gl.glTranslatef(0, cy - 25, 0);
			gl.glRotatef(-(float) (direction * 180 / Math.PI), 0, 1, 0);
			sky.drawSelf(gl);
			gl.glPopMatrix();
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			// 设置视窗大小及位置
			gl.glViewport(0, 0, width, height);
			// 设置当前矩阵为投影矩阵
			gl.glMatrixMode(GL10.GL_PROJECTION);
			// 设置当前矩阵为单位矩阵
			gl.glLoadIdentity();
			// 计算透视投影的比例
			float ratio = (float) width / height;
			gl.glFrustumf(-ratio * 0.7f, ratio * 0.7f, -0.7f * 0.7f,
					0.7f * 1.3f, 1f, 400f);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glClearColor(0, 0, 0, 0);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glMatrixMode(GL10.GL_SMOOTH);
			gl.glLoadIdentity();
			gl.glEnable(GL10.GL_DEPTH_TEST);
			// 开启混合
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			int waterId = initTexture(gl, R.drawable.water);
			int landId = initTexture(gl, R.drawable.grass);
			int skyId = initTexture(gl, R.drawable.skyball);
			landForm = new LandForm(landId, yArray, yArray.length - 1,
					yArray[0].length - 1);
			sky = new SkyBall(BALL_R, skyId);
			for (int i = 0; i < waters.length; i++) {
				waters[i] = new Water(waterId, Math.PI * 2 * i / waters.length,
						8, 8, yArray.length / 8);
			}
			// 帧动画
			new Thread() {
				public void run() {
					while (true) {
						waterIndex = (waterIndex + 1) % waters.length;
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();

		}

		public void initFog(GL10 gl) {
			gl.glEnable(GL10.GL_FOG);
			float[] fogColor = { 0.97898f, 0.96767f, 0.9866f, 0 };
			gl.glFogfv(GL10.GL_FOG_COLOR, fogColor, 0);
			gl.glFogx(GL10.GL_FOG_MODE, GL10.GL_EXP2);
			gl.glFogf(GL10.GL_FOG_DENSITY, 0.006f);
			gl.glFogf(GL10.GL_FOG_START, BALL_R - FOG_D);
			gl.glFogf(GL10.GL_FOG_END, BALL_R);
		}

		// 初始化纹理
		public int initTexture(GL10 gl, int drawableId)// textureId
		{
			// 生成纹理ID
			int[] textures = new int[1];
			gl.glGenTextures(1, textures, 0);
			int currTextureId = textures[0];
			gl.glBindTexture(GL10.GL_TEXTURE_2D, currTextureId);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
					GL10.GL_LINEAR_MIPMAP_NEAREST);// mipmap技术
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
					GL10.GL_LINEAR_MIPMAP_LINEAR);
			((GL11) gl).glTexParameterf(GL10.GL_TEXTURE_2D,
					GL11.GL_GENERATE_MIPMAP, GL10.GL_TRUE);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
					GL10.GL_REPEAT);// 纹理重复粘贴
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
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTmp, 0);
			bitmapTmp.recycle();

			return currTextureId;
		}
	}
}
