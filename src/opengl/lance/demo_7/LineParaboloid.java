package opengl.lance.demo_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class LineParaboloid {
	private FloatBuffer vertexBuffer;

	private int vCount;

	float xAngle;
	float yAngle;
	float zAngle;

	/**
	 * @param height
	 *            ---������
	 * @param a
	 *            ---�����������
	 * @param b
	 *            ---�����������----����a/b���������������
	 * @param col
	 *            ---�������зֵķ���
	 * @param angleSpan
	 *            ---����Բ��ת�ǿ��
	 */
	public LineParaboloid(float height, float a, float b, int col,
			float angleSpan) {
		// ��ֱ�������зֵķ���
		float heightSpan = height / col;
		// ����������Բ�зֵķ���
		int parts = (int) (360f / angleSpan);

		List<Float> lver = new ArrayList<Float>();
		List<Float> lnor = new ArrayList<Float>();
		for (float h = height; h > 0; h -= heightSpan) {
			for (float degree = 360f; degree > 0; degree -= angleSpan) {
				float x1 = (float) (a * Math.sqrt(2 * h) * Math.cos(Math
						.toRadians(degree)));
				float y1 = h;
				float z1 = (float) (b * Math.sqrt(2 * h) * Math.sin(Math
						.toRadians(degree)));

				float x2 = (float) (a * Math.sqrt(2 * (h - heightSpan)) * Math
						.cos(Math.toRadians(degree)));
				float y2 = h - heightSpan;
				float z2 = (float) (b * Math.sqrt(2 * (h - heightSpan)) * Math
						.sin(Math.toRadians(degree)));

				float x3 = (float) (a * Math.sqrt(2 * (h - heightSpan)) * Math
						.cos(Math.toRadians(degree - angleSpan)));
				float y3 = h - heightSpan;
				float z3 = (float) (b * Math.sqrt(2 * (h - heightSpan)) * Math
						.sin(Math.toRadians(degree - angleSpan)));

				float x4 = (float) (a * Math.sqrt(2 * h) * Math.cos(Math
						.toRadians(degree - angleSpan)));
				float y4 = h;
				float z4 = (float) (b * Math.sqrt(2 * h) * Math.sin(Math
						.toRadians(degree - angleSpan)));

				lver.add(x1);
				lver.add(y1);
				lver.add(z1);// ���������Σ���6�����������
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
		gl.glColor4f(200, 0, 0, 0);
		gl.glDrawArrays(GL10.GL_LINES, 0, vCount);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
