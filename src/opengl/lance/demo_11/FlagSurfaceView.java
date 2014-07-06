package opengl.lance.demo_11;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.lance.main.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class FlagSurfaceView extends GLSurfaceView {

	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;// 角度缩放比例
	private float mPreviousY;// 上次的触控位置Y坐标
	private float mPreviousX;// 上次的触控位置X坐标
	private SceneRenderer mRenderer;// 场景渲染器
	private float yAngle = 0;
	private float xAngle = 0;

	public FlagSurfaceView(Context context) {
		super(context);
		mRenderer = new SceneRenderer(); // 创建场景渲染器
		setRenderer(mRenderer); // 设置渲染器
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// 设置渲染模式为主动渲染
	}

	// 触摸事件回调方法
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float y = e.getY();
		float x = e.getX();
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dy = y - mPreviousY;// 计算触控笔Y位移
			float dx = x - mPreviousX;// 计算触控笔X位移
			yAngle += dx * TOUCH_SCALE_FACTOR;// 设置沿y轴旋转角度
			xAngle += dy * TOUCH_SCALE_FACTOR;// 设置沿x轴旋转角度
			requestRender();// 重绘画面
		}
		mPreviousY = y;// 记录触控笔位置
		mPreviousX = x;// 记录触控笔位置
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		FlagRect[] flags = new FlagRect[8];
		int index = 0;

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			gl.glPushMatrix();
			gl.glTranslatef(0, 0, -3f);
			gl.glRotatef(xAngle, 1, 0, 0);
			gl.glRotatef(yAngle, 0, 1, 0);
			flags[index].drawSelf(gl);
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
			// 调用此方法计算产生透视投影矩阵
			gl.glFrustumf(-ratio * 0.4f, ratio * 0.4f, -1 * 0.4f, 1 * 0.4f, 1,
					100);

		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0, 0, 0, 0);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);
			int texId = initTexture(gl, R.drawable.cn_flag);
			new Thread() {
				public void run() {
					while (true) {
						index = (index + 1) % flags.length;
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			// 为8帧图片创建纹理矩形---创建8帧矩形图片
			double dangle = Math.PI * 2 / 8;
			for (int i = 0; i < flags.length; i++) {
				flags[i] = new FlagRect(texId, dangle * i);
			}
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
