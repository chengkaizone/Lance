package opengl.lance.demo_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class LineTaper {
	private FloatBuffer vertexBuffer;// 顶点坐标缓冲

	int vCount;// 顶点数量

	public float xAngle;
	public float yAngle;
	public float zAngle;

	public LineTaper(float height, float radius, float degreeSpan, int col) {
		float heightSpan = height / col;
		float radSpan = radius / col;

		List<Float> lver = new ArrayList<Float>();
		for (float degree = 360f; degree > 0f; degree -= degreeSpan) {
			for (int i = 0; i < col; i++) {
				float curRad = i * radSpan;
				float curHeight = height - i * heightSpan;

				float x1 = (float) (curRad * Math.cos(Math.toRadians(degree)));
				float y1 = curHeight;
				float z1 = (float) (curRad * Math.sin(Math.toRadians(degree)));

				float x2 = (float) ((curRad + radSpan) * Math.cos(Math
						.toRadians(degree)));
				float y2 = curHeight - heightSpan;
				float z2 = (float) ((curRad + radSpan) * Math.sin(Math
						.toRadians(degree)));

				float x3 = (float) ((curRad + radSpan) * Math.cos(Math
						.toRadians(degree - degreeSpan)));
				float y3 = curHeight - heightSpan;
				float z3 = (float) ((curRad + radSpan) * Math.sin(Math
						.toRadians(degree - degreeSpan)));

				float x4 = (float) (curRad * Math.cos(Math.toRadians(degree
						- degreeSpan)));
				float y4 = curHeight;
				float z4 = (float) (curRad * Math.sin(Math.toRadians(degree
						- degreeSpan)));
				// 线性绘制就是将三角形转换为三条线---两个三角形6条线
				lver.add(x1);
				lver.add(y1);
				lver.add(z1);
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
		vCount = lver.size() / 3;
		float[] vertices = new float[lver.size()];
		for (int i = 0; i < lver.size(); i++) {
			vertices[i] = lver.get(i);
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(xAngle, 1, 0, 0);
		gl.glRotatef(yAngle, 0, 1, 0);
		gl.glRotatef(zAngle, 0, 0, 1);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColor4f(0, 0, 0, 0);
		gl.glDrawArrays(GL10.GL_LINES, 0, vCount);

		gl.glDisable(GL10.GL_VERTEX_ARRAY);
	}
}
