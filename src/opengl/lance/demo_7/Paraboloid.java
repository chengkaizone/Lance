package opengl.lance.demo_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class Paraboloid {
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer texBuffer;

	private int vCount;
	private int texId;

	float xAngle;
	float yAngle;
	float zAngle;

	/**
	 * @param height
	 *            ---抛物面
	 * @param a
	 *            ---任意的正常数
	 * @param b
	 *            ---任意的正常数----根据a/b决定抛物面的曲度
	 * @param col
	 *            ---抛物面切分的分数
	 * @param angleSpan
	 *            ---切面圆的转角跨度
	 * @param texId
	 *            ---纹理Id
	 */
	public Paraboloid(float height, float a, float b, int col, float angleSpan,
			int texId) {
		this.texId = texId;
		// 垂直方向上切分的份数
		float heightSpan = height / col;
		// 抛物面切面圆切分的份数
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

				float a1 = 0;
				float b1 = y1;
				float c1 = z1;
				float l1 = averNormal(a1, b1, c1);// 模长
				a1 = a1 / l1;// 法向量规格化
				b1 = b1 / l1;
				c1 = c1 / l1;

				float a2 = 0;
				float b2 = y2;
				float c2 = z2;
				float l2 = averNormal(a2, b2, c2);// 模长
				a2 = a2 / l2;// 法向量规格化
				b2 = b2 / l2;
				c2 = c2 / l2;

				float a3 = 0;
				float b3 = y3;
				float c3 = z3;
				float l3 = averNormal(a3, b3, c3);// 模长
				a3 = a3 / l3;// 法向量规格化
				b3 = b3 / l3;
				c3 = c3 / l3;

				float a4 = 0;
				float b4 = y4;
				float c4 = z4;
				float l4 = averNormal(a4, b4, c4);// 模长
				a4 = a4 / l4;// 法向量规格化
				b4 = b4 / l4;
				c4 = c4 / l4;

				lnor.add(a1);
				lnor.add(b1);
				lnor.add(c1);// 顶点对应的法向量
				lnor.add(a2);
				lnor.add(b2);
				lnor.add(c2);
				lnor.add(a4);
				lnor.add(b4);
				lnor.add(c4);

				lnor.add(a2);
				lnor.add(b2);
				lnor.add(c2);
				lnor.add(a3);
				lnor.add(b3);
				lnor.add(c3);
				lnor.add(a4);
				lnor.add(b4);
				lnor.add(c4);
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

		float[] normals = new float[lnor.size()];
		for (int i = 0; i < normals.length; i++) {
			normals[i] = lnor.get(i);
		}
		ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);

		float[] texs = texCoors(parts, col);
		ByteBuffer tbb = ByteBuffer.allocateDirect(texs.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texs);
		texBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(xAngle, 1, 0, 0);
		gl.glRotatef(yAngle, 0, 1, 0);
		gl.glRotatef(zAngle, 0, 0, 1);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}

	private float averNormal(float x, float y, float z) {
		double aver = x * x + y * y + z * z;
		return (float) Math.sqrt(aver);
	}

	private float[] texCoors(int bw, int bh) {
		float[] results = new float[bw * bh * 6 * 2];
		float sizew = 1f / bw;
		float sizeh = 1f / bh;
		int c = 0;
		for (int i = 0; i < bh; i++) {
			for (int j = 0; j < bw; j++) {
				float s = j * sizew;
				float t = i * sizeh;
				results[c++] = s;
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
