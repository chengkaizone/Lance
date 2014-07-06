package opengl.lance.demo_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class Cylinder_2 {
	private FloatBuffer vertexBuffer;

	private int vCount;

	float xAngle;
	float yAngle;
	float zAngle;

	public Cylinder_2(float height, float radius, float degreeSpan, int col) {
		List<Float> lver = new ArrayList<Float>();
		float colHeight = height / col;// 每个圆柱体所占的长度
		// float half=height/2;
		for (float degree = 360f; degree > 0f; degree -= degreeSpan) {
			for (int j = 0; j < col; j++) {
				float x1 = (float) (radius * Math.cos(Math.toRadians(degree)));
				float y1 = j * colHeight;
				float z1 = (float) (radius * Math.sin(Math.toRadians(degree)));

				float x2 = (float) (radius * Math.cos(Math.toRadians(degree
						- degreeSpan)));
				float y2 = j * colHeight;
				float z2 = (float) (radius * Math.sin(Math.toRadians(degree
						- degreeSpan)));

				float x3 = (float) (radius * Math.cos(Math.toRadians(degree
						- degreeSpan)));
				float y3 = (j + 1) * colHeight;
				float z3 = (float) (radius * Math.sin(Math.toRadians(degree
						- degreeSpan)));

				float x4 = (float) (radius * Math.cos(Math.toRadians(degree)));
				float y4 = (j + 1) * colHeight;
				float z4 = (float) (radius * Math.sin(Math.toRadians(degree)));

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
		for (int i = 0; i < vertices.length; i++) {
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
		gl.glColor4f(100, 0, 0, 0);// 线性绘制法绘制圆柱体线的颜色
		gl.glDrawArrays(GL10.GL_LINES, 0, vCount);

	}
}
