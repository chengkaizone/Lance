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
 * 山地生成
 * 
 * @author Administrator
 * 
 */
public class LWSurfaceView extends GLSurfaceView {
	private SceneRenderer renderer;
	float direction = DIRECTION_INI;// 视线方向
	// 摄像机坐标
	float cx;
	float cy;
	float cz;
	// 目标观察点
	float dx;
	float dy;
	float dz;

	public LWSurfaceView(Context context) {
		super(context);
		cx = CAMERA_INI_X;
		cy = CAMERA_INI_Y;
		cz = CAMERA_INI_Z;

		dx = (float) (cx - Math.sin(direction) * DISTANCE);
		// 俯视观察目标点的y坐标
		dy = CAMERA_INI_Y - 2f;
		dz = (float) (cz - Math.cos(direction) * DISTANCE);
		// 初始化灰度图
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
			// 设置摄像机的位置---人眼的位置起始就是摄像机的位置
			GLU.gluLookAt(gl, cx, cy, cz, dx, dy, dz, 0, 1, 0);
			land.drawSelf(gl);
			waters[index].drawSelf(gl);
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
			// 调用此方法计算产生透视投影矩阵
			gl.glFrustumf(-ratio * 0.7f, ratio * 0.7f, -0.7f * 0.7f,
					1.3f * 0.7f, 1, 400);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// 关闭抗抖动
			gl.glDisable(GL10.GL_DITHER);
			// 设置特定Hint项目的模式，这里为设置为使用快速模式
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			// 设置为打开背面剪裁
			gl.glEnable(GL10.GL_CULL_FACE);
			// 开启混合
			gl.glEnable(GL10.GL_BLEND);
			// 设置源混合因子与目标混合因子----使用混合
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			// 设置屏幕背景色黑色RGBA
			gl.glClearColor(0, 0, 0, 0);
			// 启用深度测试
			gl.glEnable(GL10.GL_DEPTH_TEST);

			int landId = initTexture(gl, R.drawable.grass);
			int waterId = initTexture(gl, R.drawable.water);
			land = new Land(landId, Constant_3.yArray);
			waters = new Water[16];
			for (int i = 0; i < waters.length; i++) {
				// 水运动的圈数越多感觉越快---整个多帧动画是360度的整数倍
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
			// gl.glTexParameterf(GL10.GL_TEXTURE_2D,
			// GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR_MIPMAP_NEAREST);//mipmap技术
			// gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR_MIPMAP_LINEAR);
			// ((GL11)gl).glTexParameterf(GL10.GL_TEXTURE_2D,
			// GL11.GL_GENERATE_MIPMAP, GL10.GL_TRUE);

			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
					GL10.GL_CLAMP_TO_EDGE);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
					GL10.GL_CLAMP_TO_EDGE);
			// gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
			// GL10.GL_REPEAT);//纹理重复粘贴
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
