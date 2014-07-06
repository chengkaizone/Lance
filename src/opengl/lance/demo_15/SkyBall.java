package opengl.lance.demo_15;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class SkyBall {
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer texBuffer;

	private int vCount;
	private int texId;

	float zAngle;

	// 天空穹改变了纹理贴图的转角方向----同时也要改变三维坐标系方向
	public SkyBall(float scale, int texId) {
		final float ANGLE_SPAN = 11.25f;
		this.texId = texId;
		List<Float> lver = new ArrayList<Float>();
		for (float wangle = 90; wangle > -90; wangle -= ANGLE_SPAN) {
			for (float jangle = 180; jangle > 0; jangle -= ANGLE_SPAN) {
				float x1 = (float) (scale * Math.cos(Math.toRadians(wangle)) * Math
						.cos(Math.toRadians(jangle)));
				float y1 = (float) (scale * Math.cos(Math.toRadians(wangle)) * Math
						.sin(Math.toRadians(jangle)));
				float z1 = (float) (scale * Math.sin(Math.toRadians(wangle)));

				float x2 = (float) (scale
						* Math.cos(Math.toRadians(wangle - ANGLE_SPAN)) * Math
						.cos(Math.toRadians(jangle)));
				float y2 = (float) (scale
						* Math.cos(Math.toRadians(wangle - ANGLE_SPAN)) * Math
						.sin(Math.toRadians(jangle)));
				float z2 = (float) (scale * Math.sin(Math.toRadians(wangle
						- ANGLE_SPAN)));

				float x3 = (float) (scale
						* Math.cos(Math.toRadians(wangle - ANGLE_SPAN)) * Math
						.cos(Math.toRadians(jangle - ANGLE_SPAN)));
				float y3 = (float) (scale
						* Math.cos(Math.toRadians(wangle - ANGLE_SPAN)) * Math
						.sin(Math.toRadians(jangle - ANGLE_SPAN)));
				float z3 = (float) (scale * Math.sin(Math.toRadians(wangle
						- ANGLE_SPAN)));

				float x4 = (float) (scale * Math.cos(Math.toRadians(wangle)) * Math
						.cos(Math.toRadians(jangle - ANGLE_SPAN)));
				float y4 = (float) (scale * Math.cos(Math.toRadians(wangle)) * Math
						.sin(Math.toRadians(jangle - ANGLE_SPAN)));
				float z4 = (float) (scale * Math.sin(Math.toRadians(wangle)));

				lver.add(x1);
				lver.add(y1);
				lver.add(z1);
				lver.add(x4);
				lver.add(y4);
				lver.add(z4);
				lver.add(x2);
				lver.add(y2);
				lver.add(z2);

				lver.add(x4);
				lver.add(y4);
				lver.add(z4);
				lver.add(x3);
				lver.add(y3);
				lver.add(z3);
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

		ByteBuffer nbb = ByteBuffer.allocateDirect(vertices.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asFloatBuffer();
		normalBuffer.put(vertices);
		normalBuffer.position(0);
		// 水平方向上切分的份数
		int rows = (int) (180f / ANGLE_SPAN);
		// 垂直方向上切分
		int cols = (int) (180f / ANGLE_SPAN);
		float[] texs = texCoors(rows, cols);
		ByteBuffer tbb = ByteBuffer.allocateDirect(texs.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texs);
		texBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
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
				// 1
				result[c++] = s;
				result[c++] = t;
				// 4
				result[c++] = s + sizew;
				result[c++] = t;
				// 2
				result[c++] = s;
				result[c++] = t + sizeh;
				// 4
				result[c++] = s + sizew;
				result[c++] = t;
				// 3
				result[c++] = s + sizew;
				result[c++] = t + sizeh;
				// 2
				result[c++] = s;
				result[c++] = t + sizeh;
			}
		}
		return result;
	}
}
