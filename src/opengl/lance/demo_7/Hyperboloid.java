package opengl.lance.demo_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * 绘制双曲面
 * 
 * @author Administrator
 * 
 */
public class Hyperboloid {
	private FloatBuffer vertexBuffer;// 顶点坐标缓冲
	private FloatBuffer texBuffer;// 纹理缓冲

	private int texId;
	private int vCount;// 顶点数量

	public float height;

	public float xAngle;
	public float yAngle;
	public float zAngle;
	// 双曲面鼓面圆的半径
	float radius;

	/**
	 * @param height
	 *            ---双曲线体高度
	 * @param a
	 *            /b/c---任意参数
	 * @param col
	 *            ---双曲线体切割的份数
	 * @param angleSpan
	 *            ---截面圆转角的跨度
	 * @param texId
	 *            ---纹理ID
	 */
	public Hyperboloid(float height, float a, float b, float c, int col,
			float angleSpan, int texId) {
		this.texId = texId;
		this.height = height;
		radius = (float) (a * Math.sqrt((1 + (0.5f * height) * (0.5f * height)
				/ (c * c))));
		// 抛物体每块所占的高度
		float heightSpan = height / col;
		// 截面角度切分份数
		int spannum = (int) (360.0f / angleSpan);

		List<Float> lver = new ArrayList<Float>();// 顶点存放列表
		// List<Float> lnor = new ArrayList<Float>();// 法向量存放列表

		for (float h = 0.5f * height; h > -0.5f * height; h -= heightSpan) {
			for (float angle = 360; angle > 0; angle -= angleSpan) { // 中间量
				float mid = 1 + (h * h) / (c * c);
				float midNext = 1 + ((h - heightSpan) * (h - heightSpan))
						/ (c * c);

				float x1 = (float) (a * Math.sqrt(mid) * Math.cos(Math
						.toRadians(angle)));
				float y1 = h;
				float z1 = (float) (b * Math.sqrt(mid) * Math.sin(Math
						.toRadians(angle)));

				float x2 = (float) (a * Math.sqrt(midNext) * Math.cos(Math
						.toRadians(angle)));
				float y2 = h - heightSpan;
				float z2 = (float) (b * Math.sqrt(midNext) * Math.sin(Math
						.toRadians(angle)));

				float x3 = (float) (a * Math.sqrt(midNext) * Math.cos(Math
						.toRadians(angle - angleSpan)));
				float y3 = h - heightSpan;
				float z3 = (float) (b * Math.sqrt(midNext) * Math.sin(Math
						.toRadians(angle - angleSpan)));

				float x4 = (float) (a * Math.sqrt(mid) * Math.cos(Math
						.toRadians(angle - angleSpan)));
				float y4 = h;
				float z4 = (float) (b * Math.sqrt(mid) * Math.sin(Math
						.toRadians(angle - angleSpan)));

				lver.add(x1);
				lver.add(y1);
				lver.add(z1);// 两个三角形，共6个顶点的坐标
				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
				lver.add(x4);
				lver.add(y4);
				lver.add(z4);

				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
				lver.add(x3);
				lver.add(y3);
				lver.add(z3);
				lver.add(x4);
				lver.add(y4);
				lver.add(z4);
			}
		}

		vCount = lver.size() / 3;// 确定顶点数量
		// 顶点
		float[] vertexs = new float[vCount * 3];
		for (int i = 0; i < vCount * 3; i++) {
			vertexs[i] = lver.get(i);
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertexs.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertexs);
		vertexBuffer.position(0);
		// 纹理
		float[] textures = texCoors(col, spannum);
		ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(textures);
		texBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(xAngle, 1, 0, 0);
		gl.glRotatef(yAngle, 0, 1, 0);
		gl.glRotatef(zAngle, 0, 0, 1);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	private float[] texCoors(int bw, int bh) {
		float[] results = new float[bw * bh * 6 * 2];
		// 计算水平切分纹理图的大小单位
		float sizew = 1f / bh;
		// 计算垂直切分纹理图的大小单位
		float sizeh = 1f / bw;
		int c = 0;
		for (int i = 0; i < bw; i++) {
			for (int j = 0; j < bh; j++) {
				float s = j * sizew;
				float t = i * sizeh;
				// 水平方向纹理
				results[c++] = s;
				// 垂直方向上的纹理
				results[c++] = t;
				results[c++] = s;
				results[c++] = t + sizeh;
				results[c++] = s + sizew;
				results[c++] = t;

				results[c++] = s;
				results[c++] = t + sizeh;
				results[c++] = s + sizew;
				results[c++] = t + sizeh;
				results[c++] = s + sizew;
				results[c++] = t;
			}
		}
		return results;
	}

}
