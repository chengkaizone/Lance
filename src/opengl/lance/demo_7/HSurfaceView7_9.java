package opengl.lance.demo_7;

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
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class HSurfaceView7_9 extends GLSurfaceView {
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;// 角度缩放比例
	SceneRenderer renderer;// 场景渲染器
	private float previousY;// 上次的触控位置Y坐标
	private float previousX;// 上次的触控位置Y坐标
	private int lightAngle = 90;// 灯的当前角度

	public HSurfaceView7_9(Context context) {
		super(context);
		renderer = new SceneRenderer(); // 创建场景渲染器
		setRenderer(renderer); // 设置渲染器
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// 设置渲染模式为主动渲染
	}

	// 触摸事件回调方法
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float y = e.getY();
		float x = e.getX();
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dy = y - previousY;// 计算触控笔Y位移
			float dx = x - previousX;// 计算触控笔Y位移
			renderer.drum.xAngle += dy * TOUCH_SCALE_FACTOR;// 设置沿x轴旋转角度
			renderer.drum.zAngle += dx * TOUCH_SCALE_FACTOR;// 设置沿z轴旋转角度
			requestRender();// 重绘画面
			break;
		}
		previousY = y;// 记录触控笔位置
		previousX = x;// 记录触控笔位置
		return true;
	}

	public class SceneRenderer implements GLSurfaceView.Renderer {
		public int hyperId;
		public int circleId;
		Drum drum;

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
			gl.glFrustumf(-ratio, ratio, -1, 1, 1, 100);
		}

		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// 关闭抗抖动
			gl.glDisable(GL10.GL_DITHER);
			// 设置特定Hint项目的模式，这里为设置为使用快速模式
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			// 设置屏幕背景色黑色RGBA
			gl.glClearColor(0, 0, 0, 0);
			// 设置着色模型为平滑着色
			gl.glShadeModel(GL10.GL_SMOOTH);
			// 启用深度测试
			gl.glEnable(GL10.GL_DEPTH_TEST);
			// 鼓皮纹理Id
			hyperId = initTexture(gl, R.drawable.hong);// 纹理ID
			// 鼓神身纹理Id
			circleId = initTexture(gl, R.drawable.huang);
			drum = new Drum(HSurfaceView7_9.this);
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

			initLight(gl);
			gl.glTranslatef(0, 0, -8f);
			drum.drawSelf(gl);
			closeLight(gl);
			gl.glPopMatrix();

		}

		// 初始化白色灯
		private void initLight(GL10 gl) {
			gl.glEnable(GL10.GL_LIGHTING);// 允许光照
			gl.glEnable(GL10.GL_LIGHT0);// 打开1号灯

			// 环境光设置
			float[] ambientParams = { 0.2f, 0.2f, 0.2f, 1.0f };// 光参数 RGBA
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams, 0);

			// 散射光设置
			float[] diffuseParams = { 1f, 1f, 1f, 1.0f };// 光参数 RGBA
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams, 0);

			// 反射光设置
			float[] specularParams = { 1f, 1f, 1f, 1.0f };// 光参数 RGBA
			gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams, 0);
		}

		// 关闭灯
		private void closeLight(GL10 gl) {
			gl.glDisable(GL10.GL_LIGHT0);
			gl.glDisable(GL10.GL_LIGHTING);
		}

		private int initTexture(GL10 gl, int drawId) {
			int[] texs = new int[1];
			gl.glGenTextures(1, texs, 0);
			int curId = texs[0];
			gl.glBindTexture(GL10.GL_TEXTURE_2D, curId);
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
			InputStream is = getResources().openRawResource(drawId);
			Bitmap bitmap;
			try {
				bitmap = BitmapFactory.decodeStream(is);
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			bitmap.recycle();
			return curId;
		}
	}

}
