package opengl.lance.demo_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class Cylinder_1 {
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer texBuffer;

	private int vCount;
	private int texId;

	float xAngle;
	float yAngle;
	float zAngle;

	public Cylinder_1(float length, float radius, float degreeSpan, int texId) {
		this.texId = texId;
		int spanNum = (int) (360 / degreeSpan);
		List<Float> lver = new ArrayList<Float>();
		List<Float> lnor = new ArrayList<Float>();
		for (float degree = 360f; degree > 0f; degree -= degreeSpan) {
			float x1 = (-length / 2);
			float y1 = (float) (radius * Math.sin(Math.toRadians(degree)));
			float z1 = (float) (radius * Math.cos(Math.toRadians(degree)));

			float a1 = 0;
			float b1 = y1;
			float c1 = z1;
			float len1 = vectorLength(a1, b1, c1);
			a1 = a1 / len1;
			b1 = b1 / len1;
			c1 = c1 / len1;

			float x2 = (-length / 2);
			float y2 = (float) (radius * Math.sin(Math.toRadians(degree
					- degreeSpan)));
			float z2 = (float) (radius * Math.cos(Math.toRadians(degree
					- degreeSpan)));

			float a2 = 0;
			float b2 = y2;
			float c2 = z2;
			float len2 = vectorLength(a2, b2, c2);
			a2 = a2 / len2;
			b2 = b2 / len2;
			c2 = c2 / len2;

			float x3 = (length / 2);
			float y3 = (float) (radius * Math.sin(Math.toRadians(degree
					- degreeSpan)));
			float z3 = (float) (radius * Math.cos(Math.toRadians(degree
					- degreeSpan)));

			float a3 = 0;
			float b3 = y3;
			float c3 = z3;
			float len3 = vectorLength(a3, b3, c3);
			a3 = a3 / len3;
			b3 = b3 / len3;
			c3 = c3 / len3;

			float x4 = (length / 2);
			float y4 = (float) (radius * Math.sin(Math.toRadians(degree)));
			float z4 = (float) (radius * Math.cos(Math.toRadians(degree)));

			float a4 = 0;
			float b4 = y4;
			float c4 = z4;
			float len4 = vectorLength(a4, b4, c4);
			a4 = a4 / len4;
			b4 = b4 / len4;
			c4 = c4 / len4;
			lver.add(x1);
			lver.add(y1);
			lver.add(z1);
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
		ByteBuffer nbb = ByteBuffer.allocateDirect(vertices.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);

		float[] texCoors = generateTexCoor(spanNum);
		ByteBuffer tbb = ByteBuffer.allocateDirect(texCoors.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texCoors);
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

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}

	private float vectorLength(float x, float y, float z) {
		float pf = x * x + y * y + z * z;
		return (float) Math.sqrt(pf);
	}

	// 自动切分纹理产生数组的方法---bh为转角切分的份数
	private float[] generateTexCoor(int bh) {
		float[] result = new float[bh * 2 * 6];
		float REPEAT = 2;
		float sizeh = 1.0f / bh;// 计算切分的行数
		int c = 0;
		for (int i = 0; i < bh; i++) {
			// 每列一个矩形---由两个三角形组成--6个顶点-共12个纹理坐标
			float t = i * sizeh;
			result[c++] = 0;
			result[c++] = t;

			result[c++] = 0;
			result[c++] = t + sizeh;

			result[c++] = REPEAT;
			result[c++] = t;

			result[c++] = 0;
			result[c++] = t + sizeh;

			result[c++] = REPEAT;
			result[c++] = t + sizeh;

			result[c++] = REPEAT;
			result[c++] = t;
		}
		return result;
	}
}
