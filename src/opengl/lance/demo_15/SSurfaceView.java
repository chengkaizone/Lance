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
	// 摄像机坐标
	float cx = 0;
	float cy = 1;
	float cz = 0;
	// 目标点坐标
	float tx = 0;
	float ty = 0;
	float tz = 0;
	// 观察的可视区域
	float left;
	float right;
	float top;
	float bottom;
	float near;
	float far;
	// 摄像机角度
	float cameraAlpha = 70;
	{
		// 初始化目标物的坐标
		tx = (float) (cx - CAMERA_DISTANCE
				* Math.sin(Math.toRadians(cameraAlpha)));
		ty = cy;
		tz = (float) (cz - CAMERA_DISTANCE
				* Math.cos(Math.toRadians(cameraAlpha)));
	}
	// 保存所有立体形状
	ArrayList<Shape> shapes;
	// 距离摄像机的距离
	ArrayList<Float> alDistance;
	// 场景渲染器
	private SceneRenderer renderer;

	public SSurfaceView(Context context) {
		super(context);
		renderer = new SceneRenderer();
		this.setRenderer(renderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		// 保存所有的图形类
		shapes = new ArrayList<Shape>();
		// 保存射线穿过物体的最短距离
		alDistance = new ArrayList<Float>();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			// 清空上次保存的射线穿过物体的距离
			alDistance.clear();
			// 无穷大~无效的距离值
			float bodyDistance = Float.POSITIVE_INFINITY;
			int bodyFlag = 0;
			// 循环遍历场景中的~将射线交点加入到集合中~每个物体都会保存一个值~没有交点的物体保存的值为无穷大
			for (int i = 0; i < shapes.size(); i++) {
				shapes.get(i).setHilight(false);
				// 获取立体图形各个方向上的最大长度
				float[] minMax = shapes.get(i).findMinMax();
				// 获取立体图形在坐标系中的中心点
				float[] mid = shapes.get(i).findMid();
				// 计算出a/b两点的距离
				float[] bPosition = CalUtil.calculateABPosition(x,// 触摸点x坐标
						y,// 触摸点y坐标
						Sample15_7.screenWidth,// 屏幕宽
						Sample15_7.screenHeight,// 屏幕高
						left, // 视角左边
						top, // 视角上边
						near,// 视角near值
						far,// far值
						cameraAlpha,// 摄像机角度
						// 摄像机坐标
						cx, cy, cz);
				// 判断射线与物体是否相交；如果相交计算near距离~如不相交设置为无穷大
				float tempDistance = IntersectUtil.isIntersectant(
				// 物体中心坐标
						mid[0], mid[1], mid[2],
						// 长宽高
						minMax[0] / 2, minMax[1] / 2, minMax[2] / 2,
						// A点坐标
						bPosition[0], bPosition[1], bPosition[2],
						// B点坐标
						bPosition[3], bPosition[4], bPosition[5]);
				// 将相交的物体的near值加入集合
				alDistance.add(tempDistance);
			}
			// 循环遍历射线穿过的图形类
			for (int i = 0; i < alDistance.size(); i++) {
				// 获取near距离
				float tempA = alDistance.get(i);
				// 循环判断各个立体图形是否存在有效距离
				if (tempA < bodyDistance) {
					// 记录最小值
					bodyDistance = tempA;
					// 记录最小索引值
					bodyFlag = i;
				}
			}
			System.out.println("distance:" + bodyDistance);
			// 如果交点距离不是无穷大~放大物体
			if (!Float.isInfinite(bodyDistance)) {
				// 调整图形大小
				shapes.get(bodyFlag).setHilight(true);
				System.out.println("绘制的图形类：" + shapes.get(bodyFlag).getClass());
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
		// 处理左键
		case 21:
			cameraAlpha += 5f;
			tx = (float) (cx - CAMERA_DISTANCE
					* Math.sin(Math.toRadians(cameraAlpha)));
			tz = (float) (cz - CAMERA_DISTANCE
					* Math.cos(Math.toRadians(cameraAlpha)));
			break;
		// 处理右键
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
			// 前4个参数确定可视窗口的大小~后两个参数决定绘制图形的深度
			gl.glFrustumf(-left, right, -bottom, top, near, far);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			gl.glClearColor(0.8f, 0.8f, 0.8f, 0);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);
			// 开启背面剪切
			gl.glEnable(GL10.GL_CULL_FACE);
			int cubeId = initTexture(gl, R.drawable.stone);
			shape = new Pillar(cubeId, CUBE_X, CUBE_Y, CUBE_Z, -10f, 0, -4f);
			shapes.add(shape);
			shape = new Pillar(cubeId, CUBE_X, CUBE_Y, CUBE_Z, -10f, 4, -4f);
			shapes.add(shape);
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
