package opengl.lance.demo_13;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class Ball_1 {
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer texBuffer;

	private int vCount;
	private int texId;

	float xAngle;
	float yAngle;
	float zAngle;

	public Ball_1(float angleSpan, float scale, int texId) {
		this.texId = texId;
		final float UNIT_SIZE = 1f;
		List<Float> lver = new ArrayList<Float>();
		for (float wangle = 90; wangle > -90; wangle -= angleSpan) {
			for (float jangle = 360; jangle > 0; jangle -= angleSpan) {
				float x1 = (float) (UNIT_SIZE * scale
						* Math.cos(Math.toRadians(wangle)) * Math.cos(Math
						.toRadians(jangle)));
				float z1 = (float) (UNIT_SIZE * scale
						* Math.cos(Math.toRadians(wangle)) * Math.sin(Math
						.toRadians(jangle)));
				float y1 = (float) (UNIT_SIZE * scale * Math.sin(Math
						.toRadians(wangle)));

				float x2 = (float) (UNIT_SIZE * scale
						* Math.cos(Math.toRadians(wangle - angleSpan)) * Math
						.cos(Math.toRadians(jangle)));
				float z2 = (float) (UNIT_SIZE * scale
						* Math.cos(Math.toRadians(wangle - angleSpan)) * Math
						.sin(Math.toRadians(jangle)));
				float y2 = (float) (UNIT_SIZE * scale * Math.sin(Math
						.toRadians(wangle - angleSpan)));

				float x3 = (float) (UNIT_SIZE * scale
						* Math.cos(Math.toRadians(wangle - angleSpan)) * Math
						.cos(Math.toRadians(jangle - angleSpan)));
				float z3 = (float) (UNIT_SIZE * scale
						* Math.cos(Math.toRadians(wangle - angleSpan)) * Math
						.sin(Math.toRadians(jangle - angleSpan)));
				float y3 = (float) (UNIT_SIZE * scale * Math.sin(Math
						.toRadians(wangle - angleSpan)));

				float x4 = (float) (UNIT_SIZE * scale
						* Math.cos(Math.toRadians(wangle)) * Math.cos(Math
						.toRadians(jangle - angleSpan)));
				float z4 = (float) (UNIT_SIZE * scale
						* Math.cos(Math.toRadians(wangle)) * Math.sin(Math
						.toRadians(jangle - angleSpan)));
				float y4 = (float) (UNIT_SIZE * scale * Math.sin(Math
						.toRadians(wangle)));

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

		ByteBuffer nbb = ByteBuffer.allocateDirect(vertices.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asFloatBuffer();
		normalBuffer.put(vertices);
		normalBuffer.position(0);
		// 水平方向上切分的份数
		int rows = (int) (360f / angleSpan);
		// 垂直方向上切分
		int cols = (int) (180f / angleSpan);
		float[] texs = texCoors(rows, cols);
		ByteBuffer tbb = ByteBuffer.allocateDirect(texs.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texs);
		texBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glTranslatef(xAngle, 0, 0);
		gl.glTranslatef(0, yAngle, 0);
		gl.glTranslatef(0, 0, zAngle);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
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

	private float[] texCoors(int bw, int bh) {
		float[] result = new float[bw * bh * 6 * 2];
		float sizew = 1f / bw;
		float sizeh = 1f / bh;
		int c = 0;
		for (int i = 0; i < bh; i++) {
			for (int j = 0; j < bw; j++) {
				float s = j * sizew;
				float t = i * sizeh;
				result[c++] = s;
				result[c++] = t;

				result[c++] = s;
				result[c++] = t + sizeh;
				result[c++] = s + sizew;
				result[c++] = t;

				result[c++] = s;
				result[c++] = t + sizeh;
				result[c++] = s + sizew;
				result[c++] = t + sizeh;
				result[c++] = s + sizew;
				result[c++] = t;
			}
		}
		return result;
	}
}
