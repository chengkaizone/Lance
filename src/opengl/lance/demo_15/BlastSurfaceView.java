package opengl.lance.demo_15;

import static opengl.lance.demo_15.Constant_6.BULLET_OFFSET_X;
import static opengl.lance.demo_15.Constant_6.BULLET_OFFSET_Y;
import static opengl.lance.demo_15.Constant_6.BULLET_OFFSET_Z;
import static opengl.lance.demo_15.Constant_6.CUBE_OFFSET_X;
import static opengl.lance.demo_15.Constant_6.CUBE_OFFSET_Y;
import static opengl.lance.demo_15.Constant_6.CUBE_OFFSET_Z;
import static opengl.lance.demo_15.Constant_6.LONG_R;
import static opengl.lance.demo_15.Constant_6.VX;

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

public class BlastSurfaceView extends GLSurfaceView {
	SceneRenderer renderer;
	float cx;
	float cy = 3;
	float cz = 10;
	float tx;
	float ty;
	float tz;

	ArrayList<BulletForControl> alBFC;
	Sample15_6 activity;
	Cube_6 cube;

	public BlastSurfaceView(Context context) {
		super(context);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		alBFC = new ArrayList<BulletForControl>();
		activity = (Sample15_6) this.getContext();
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		Bullet bullet;;
		int[] explos = new int[6];
		TextureRect[] blasts = new TextureRect[6];
		// 子弹智能体
		BulletForControl bfc;

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			GLU.gluLookAt(gl, cx, cy, cz, tx, ty, tz, 0, 1, 0);

			gl.glPushMatrix();
			for (int i = 0; i < alBFC.size(); i++) {
				// 绘制子弹及爆炸动画
				alBFC.get(i).drawSelf(gl);
			}
			gl.glPopMatrix();

			gl.glPushMatrix();
			gl.glTranslatef(CUBE_OFFSET_X, CUBE_OFFSET_Y, CUBE_OFFSET_Z);
			cube.drawSelf(gl);
			gl.glPopMatrix();
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();

			float ratio = (float) width / height;
			// 透视投影距离设置印象测试
			gl.glFrustumf(-ratio * 0.5f, ratio * 0.5f, -0.5f, 0.5f, 1, 100);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0.8f, 0.8f, 0.8f, 0);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);

			int cubeId = initTexture(gl, R.drawable.stone);
			int taperId = initTexture(gl, R.drawable.bullet);
			int cylinderId = initTexture(gl, R.drawable.bullet);
			// 创建爆炸纹理Id多帧
			explos[0] = initTexture(gl, R.drawable.explode1);
			explos[1] = initTexture(gl, R.drawable.explode2);
			explos[2] = initTexture(gl, R.drawable.explode3);
			explos[3] = initTexture(gl, R.drawable.explode4);
			explos[4] = initTexture(gl, R.drawable.explode5);
			explos[5] = initTexture(gl, R.drawable.explode6);
			for (int i = 0; i < explos.length; i++) {
				// 创建爆炸帧动画
				blasts[i] = new TextureRect(explos[i], LONG_R * 3f, LONG_R * 3f);
			}
			// 根据锥体、圆柱体创建子弹对象
			bullet = new Bullet(taperId, cylinderId);
			// 创建墙面
			cube = new Cube_6(cubeId, 2, 0.5f, 1f);
			// 创建子弹智能体
			bfc = new BulletForControl(BlastSurfaceView.this, bullet, blasts,
					-BULLET_OFFSET_X, BULLET_OFFSET_Y, BULLET_OFFSET_Z, VX,
					0.0f, 0.0f);
			alBFC.add(bfc);
			new BulletGoThread(alBFC).start();
		}

		// 初始化纹理
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
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTmp, 0);// 定义二维纹理
			bitmapTmp.recycle();

			return textureId;
		}

	}

}
