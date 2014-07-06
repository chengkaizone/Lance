package opengl.lance.demo_10;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class CloudBall {

	private IntBuffer vertexBuffer;
	private IntBuffer normalBuffer;
	private FloatBuffer texBuffer;

	private int vCount;
	private int texId;

	float xAngle;
	float yAngle;
	float zAngle;

	public CloudBall(int scale, int texId) {
		this.texId = texId;
		final int UNIT_SIZE = 10;
		float angleSpan = 11.25f;

		List<Integer> lver = new ArrayList<Integer>();
		// 根据纬度切分
		for (float vangle = 90; vangle >= -90; vangle -= angleSpan) {
			// 可根据经度切分
			for (float hangle = 360; hangle > 0; hangle -= angleSpan) {

				int x1 = (int) (scale * UNIT_SIZE
						* Math.cos(Math.toRadians(vangle)) * Math.cos(Math
						.toRadians(hangle)));
				int y1 = (int) (scale * UNIT_SIZE * Math.sin(Math
						.toRadians(vangle)));
				int z1 = (int) (scale * UNIT_SIZE
						* Math.cos(Math.toRadians(vangle)) * Math.sin(Math
						.toRadians(hangle)));

				int x2 = (int) (scale * UNIT_SIZE
						* Math.cos(Math.toRadians(vangle - angleSpan)) * Math
						.cos(Math.toRadians(hangle)));
				int y2 = (int) (scale * UNIT_SIZE * Math.sin(Math
						.toRadians(vangle - angleSpan)));
				int z2 = (int) (scale * UNIT_SIZE
						* Math.cos(Math.toRadians(vangle - angleSpan)) * Math
						.sin(Math.toRadians(hangle)));

				int x3 = (int) (scale * UNIT_SIZE
						* Math.cos(Math.toRadians(vangle - angleSpan)) * Math
						.cos(Math.toRadians(hangle - angleSpan)));
				int y3 = (int) (scale * UNIT_SIZE * Math.sin(Math
						.toRadians(vangle - angleSpan)));
				int z3 = (int) (scale * UNIT_SIZE
						* Math.cos(Math.toRadians(vangle - angleSpan)) * Math
						.sin(Math.toRadians(hangle - angleSpan)));

				int x4 = (int) (scale * UNIT_SIZE
						* Math.cos(Math.toRadians(vangle)) * Math.cos(Math
						.toRadians(hangle - angleSpan)));
				int y4 = (int) (scale * UNIT_SIZE * Math.sin(Math
						.toRadians(vangle)));
				int z4 = (int) (scale * UNIT_SIZE
						* Math.cos(Math.toRadians(vangle)) * Math.sin(Math
						.toRadians(hangle - angleSpan)));

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
		int[] vertices = new int[lver.size()];
		for (int i = 0; i < lver.size(); i++) {
			vertices[i] = lver.get(i);
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asIntBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		ByteBuffer nbb = ByteBuffer.allocateDirect(vertices.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asIntBuffer();
		normalBuffer.put(vertices);
		normalBuffer.position(0);

		int bw = (int) (360 / angleSpan);
		int bh = (int) (180 / angleSpan);
		float[] texs = calTexCoors(bw, bh);
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
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FIXED, 0, normalBuffer);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	// 根据经纬度将纹理贴图切分成相应的份数---先水平循环
	private float[] calTexCoors(int bw, int bh) {
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
