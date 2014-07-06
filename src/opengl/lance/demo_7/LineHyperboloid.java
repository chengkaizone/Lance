package opengl.lance.demo_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class LineHyperboloid {
	private FloatBuffer vertexBuffer;// 顶点坐标缓冲

	private int vCount;// 顶点数量

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
	public LineHyperboloid(float height, float a, float b, float c, int col,
			float angleSpan) {
		radius = (float) (a * Math.sqrt((1 + (0.5f * height) * (0.5f * height)
				/ (c * c))));
		// 抛物体每块所占的高度
		float heightSpan = height / col;

		List<Float> lver = new ArrayList<Float>();// 顶点存放列表

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

				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
				lver.add(x4);
				lver.add(y4);
				lver.add(z4);

				lver.add(x4);
				lver.add(y4);
				lver.add(z4);
				lver.add(x1);
				lver.add(y1);
				lver.add(z1);

				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
				lver.add(x3);
				lver.add(y3);
				lver.add(z3);

				lver.add(x3);
				lver.add(y3);
				lver.add(z3);
				lver.add(x4);
				lver.add(y4);
				lver.add(z4);

				lver.add(x4);
				lver.add(y4);
				lver.add(z4);
				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
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
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(xAngle, 1, 0, 0);
		gl.glRotatef(yAngle, 0, 1, 0);
		gl.glRotatef(zAngle, 0, 0, 1);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColor4f(100, 0, 0, 0);

		gl.glDrawArrays(GL10.GL_LINES, 0, vCount);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
