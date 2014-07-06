package opengl.lance.demo_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class Cirque {
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer texBuffer;
	private int vCount;
	private int texId;
	float xAngle;
	float yAngle;
	float zAngle;

	/**
	 * ringSpan表示环每一份多少度；circleSpan表示圆截环每一份多少度;ringRadius表示环半径；
	 * circleRadius圆截面半径。texId表示纹理映射Id
	 * 
	 * @param ringSpan
	 * @param circleSpan
	 * @param ringRadius
	 * @param circleRadius
	 * @param texId
	 */
	public Cirque(float ringSpan, float circleSpan, float ringRadius,
			float circleRadius, int texId) {
		this.texId = texId;
		List<Float> lver = new ArrayList<Float>();
		List<Float> lnor = new ArrayList<Float>();
		// cdegree截环转角跨度
		for (float cdegree = 0f; cdegree < 360f; cdegree += circleSpan) {
			// 圆环半径
			for (float rdegree = -180f; rdegree < 180f; rdegree += ringSpan) {
				float x1 = (float) ((ringRadius + circleRadius
						* Math.cos(Math.toRadians(cdegree))) * Math.cos(Math
						.toRadians(rdegree)));

				float y1 = (float) (circleRadius * Math.sin(Math
						.toRadians(cdegree)));
				float z1 = (float) ((ringRadius + circleRadius
						* Math.cos(Math.toRadians(cdegree))) * Math.sin(Math
						.toRadians(rdegree)));

				float x2 = (float) ((ringRadius + circleRadius
						* Math.cos(Math.toRadians(cdegree))) * Math.cos(Math
						.toRadians(rdegree + ringSpan)));
				float y2 = (float) (circleRadius * Math.sin(Math
						.toRadians(cdegree)));
				float z2 = (float) ((ringRadius + circleRadius
						* Math.cos(Math.toRadians(cdegree))) * Math.sin(Math
						.toRadians(rdegree + ringSpan)));

				float x3 = (float) ((ringRadius + circleRadius
						* Math.cos(Math.toRadians(cdegree + circleSpan))) * Math
						.cos(Math.toRadians(rdegree + ringSpan)));
				float y3 = (float) (circleRadius * Math.sin(Math
						.toRadians(cdegree + circleSpan)));
				float z3 = (float) ((ringRadius + circleRadius
						* Math.cos(Math.toRadians(cdegree + circleSpan))) * Math
						.sin(Math.toRadians(rdegree + ringSpan)));

				float x4 = (float) ((ringRadius + circleRadius
						* Math.cos(Math.toRadians(cdegree + circleSpan))) * Math
						.cos(Math.toRadians(rdegree)));
				float y4 = (float) (circleRadius * Math.sin(Math
						.toRadians(cdegree + circleSpan)));
				float z4 = (float) ((ringRadius + circleRadius
						* Math.cos(Math.toRadians(cdegree + circleSpan))) * Math
						.sin(Math.toRadians(rdegree)));

				lver.add(x4);
				lver.add(y4);
				lver.add(z4);
				lver.add(x1);
				lver.add(y1);
				lver.add(z1);
				lver.add(x3);
				lver.add(y3);
				lver.add(z3);

				lver.add(x1);
				lver.add(y1);
				lver.add(z1);
				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
				lver.add(x3);
				lver.add(y3);
				lver.add(z3);

				// 圆环顶点的法向量应该以截面圆的圆心作为原点
				float a1 = (float) (x1 - (ringRadius * Math.cos(Math
						.toRadians(rdegree))));
				float b1 = y1 - 0;
				float c1 = (float) (z1 - (ringRadius * Math.sin(Math
						.toRadians(rdegree))));
				float len1 = generalNormal(a1, b1, c1);
				a1 = a1 / len1;
				b1 = b1 / len1;
				c1 = c1 / len1;

				float a2 = (float) (x2 - (ringRadius * Math.cos(Math
						.toRadians(rdegree + ringSpan))));
				float b2 = y1 - 0;
				float c2 = (float) (z2 - (ringRadius * Math.sin(Math
						.toRadians(rdegree + ringSpan))));
				float len2 = generalNormal(a2, b2, c2);
				a2 = a2 / len2;
				b2 = b2 / len2;
				c2 = c2 / len2;

				float a3 = (float) (x3 - (ringRadius * Math.cos(Math
						.toRadians(rdegree + ringSpan))));
				float b3 = y1 - 0;
				float c3 = (float) (z3 - (ringRadius * Math.sin(Math
						.toRadians(rdegree + ringSpan))));
				float len3 = generalNormal(a3, b3, c3);
				a3 = a3 / len3;
				b3 = b3 / len3;
				c3 = c3 / len3;

				float a4 = (float) (x4 - (ringRadius * Math.cos(Math
						.toRadians(rdegree))));
				float b4 = y1 - 0;
				float c4 = (float) (z4 - (ringRadius * Math.sin(Math
						.toRadians(rdegree))));
				float len4 = generalNormal(a4, b4, c4);
				a4 = a4 / len4;
				b4 = b4 / len4;
				c4 = c4 / len4;

				lnor.add(a1);
				lnor.add(b1);
				lnor.add(c1);
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
		float[] vertices = new float[lver.size()];
		for (int i = 0; i < lver.size(); i++) {
			vertices[i] = lver.get(i);
		}
		vCount = vertices.length / 3;
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

		int row = (int) (360 / circleSpan);
		int col = (int) (360 / ringSpan);
		float[] texs = calCoors(row, col);
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

	private float generalNormal(double x, double y, double z) {
		double pf = x * x + y * y + z * z;
		return (float) Math.sqrt(pf);
	}

	private float[] calCoors(int bw, int bh) {
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
