package opengl.lance.demo_9;

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

public class OtherSurfaceView extends GLSurfaceView {
	private SceneRenderer renderer;

	public OtherSurfaceView(Context context) {
		super(context);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		// 金字塔
		MyPyramid[] prr;
		// 天球
		MyCelestial celestialSmall;
		MyCelestial celestialBig;
		// 沙漠
		MyDesert desert;
		float lightAngle = 120f;

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glMatrixMode(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);
			int wallaId = initTexture(gl, R.drawable.walla);
			int wallbId = initTexture(gl, R.drawable.wallb);
			int wallcId = initTexture(gl, R.drawable.wallc);
			int desertId = initTexture(gl, R.drawable.desert);
			prr = new MyPyramid[] { new MyPyramid(-2, -2, 2f, 30, wallaId),
					new MyPyramid(3, -7, 2f, 0, wallbId),
					new MyPyramid(6, -2, 2f, 0, wallcId), };

			celestialBig = new MyCelestial(0, 0, 2, 0, 50);
			celestialSmall = new MyCelestial(0, 0, 1, 0, 250);
			desert = new MyDesert(-20, -20, 4, 0, desertId, 40, 40);
			// 开启光照
			gl.glEnable(GL10.GL_LIGHTING);
			initSunLight(gl);
			initMaterial(gl);
			// 开启雾效果
			gl.glEnable(GL10.GL_FOG);
			initFog(gl);
			new Thread() {
				public void run() {
					while (true) {
						lightAngle += 0.5f;
						if (lightAngle >= 360f) {
							lightAngle = 0f;
						}
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			new Thread() {
				public void run() {
					while (true) {
						celestialBig.yAngle += 0.5f;
						if (celestialBig.yAngle >= 360f) {
							celestialBig.yAngle = 0f;
						}
						celestialSmall.yAngle += 0.5f;
						if (celestialSmall.yAngle >= 360f) {
							celestialSmall.yAngle = 0f;
						}
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			float ratio = (float) width / height;
			// 场景中该函数十分重要
			gl.glFrustumf(-ratio, ratio, -0.5f, 1.5f, 1, 100);
			// 设置摄像机的位置---该函数在场景视角中十分重要
			GLU.gluLookAt(gl, -1f,// 人眼的位置X
					0.6f, // 人眼的位置Y
					3.0f,// 人眼的位置Z
					0, // 人眼球看的点X
					0.2f, // 人眼球看的点Y
					0, // 人眼球看的点Z
						// 最后三个参数表示Up方向向量的X、Y、Z分量
					0, 1, 0);
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			float px = (float) (1 * Math.cos(Math.toRadians(lightAngle)));
			float py = (float) (1 * Math.sin(Math.toRadians(lightAngle)));
			float[] pos = { px, py, 0.6f, 0 };
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, pos, 0);

			celestialBig.drawSelf(gl);
			celestialSmall.drawSelf(gl);
			desert.drawSelf(gl);
			for (MyPyramid p : prr) {
				p.drawSelf(gl);
			}
		}

		private void initMaterial(GL10 gl) {// 材质为白色时什么颜色的光照在上面就将体现出什么颜色
			// 环境光为白色材质
			float ambientMaterial[] = { 0.4f, 0.4f, 0.4f, 1.0f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT,
					ambientMaterial, 0);
			// 散射光为白色材质
			float diffuseMaterial[] = { 0.8f, 0.8f, 0.8f, 1.0f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE,
					diffuseMaterial, 0);
			// 高光材质为白色
			float specularMaterial[] = { 0.6f, 0.6f, 0.6f, 1.0f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR,
					specularMaterial, 0);
			// 高光反射区域,数越大高亮区域越小越暗
			float shininessMaterial[] = { 1.5f };
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS,
					shininessMaterial, 0);
		}

		private void initSunLight(GL10 gl) {
			gl.glEnable(GL10.GL_LIGHT0);// 打开0号灯

			// 环境光设置
			float[] ambientParams = { 0.2f, 0.2f, 0.0f, 1.0f };// 光参数 RGBA
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams, 0);

			// 散射光设置
			float[] diffuseParams = { 1f, 1f, 0f, 1.0f };// 光参数 RGBA
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams, 0);

			// 反射光设置
			float[] specularParams = { 1.0f, 1.0f, 0.0f, 1.0f };// 光参数 RGBA
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams, 0);
		}

		// 初始化雾
		public void initFog(GL10 gl) {
			// 设置雾的颜色数组
			float[] fogColor = { 1, 0.91765f, 0.66667f, 0 };
			// 设置雾的颜色，RGBA模式
			gl.glFogfv(GL10.GL_FOG_COLOR, fogColor, 0);
			// 设置雾的模式，选择不同的雾因子GL_EXP(默认)、GL_EXP2或GL_LINEAR
			gl.glFogx(GL10.GL_FOG_MODE, GL10.GL_EXP2);
			// 设置雾的浓度---0~1
			gl.glFogf(GL10.GL_FOG_DENSITY, 1);
			// 设置雾的开始距离---离摄像机的最近距离
			gl.glFogf(GL10.GL_FOG_START, 0.5f);
			// 设置雾的最远距离---离摄像机的最远距离
			gl.glFogf(GL10.GL_FOG_END, 100.0f);
		}

		// 初始化纹理
		public int initTexture(GL10 gl, int drawableId) {
			// 生成纹理ID
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

			InputStream is = OtherSurfaceView.this.getResources()
					.openRawResource(drawableId);
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
