package opengl.lance.demo_9;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class MyPyramid {
	private final float UNIT_SIZE = 0.5f;
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer texBuffer;
	private int vCount;
	private int texId;

	float yAngle;
	int xOffset;
	int zOffset;

	public MyPyramid(int xOffset, int zOffset, float scale, float yAngle,
			int texId) {
		this.xOffset = xOffset;
		this.zOffset = zOffset;
		this.yAngle = yAngle;
		this.texId = texId;
		// 金字塔4个三角形12个顶点
		vCount = 12;
		// 顶点数据
		float[] vertices = { 0, 2 * UNIT_SIZE * scale, 0, UNIT_SIZE * scale, 0,
				UNIT_SIZE * scale, UNIT_SIZE * scale, 0, -UNIT_SIZE * scale,

				0, 2 * UNIT_SIZE * scale, 0, UNIT_SIZE * scale, 0,
				-UNIT_SIZE * scale, -UNIT_SIZE * scale, 0, -UNIT_SIZE * scale,

				0, 2 * UNIT_SIZE * scale, 0, -UNIT_SIZE * scale, 0,
				-UNIT_SIZE * scale, -UNIT_SIZE * scale, 0, UNIT_SIZE * scale,

				0, 2 * UNIT_SIZE * scale, 0, -UNIT_SIZE * scale, 0,
				UNIT_SIZE * scale, UNIT_SIZE * scale, 0, UNIT_SIZE * scale, };
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		final float TP = 0.89443f;
		final float DP = 0.44721f;
		final float ZERO = 0f;
		// 法向量数据
		float normals[] = new float[] { TP, DP, ZERO, TP, DP, ZERO, TP, DP,
				ZERO,

				ZERO, DP, -TP, ZERO, DP, -TP, ZERO, DP, -TP,

				-TP, DP, ZERO, -TP, DP, ZERO, -TP, DP, ZERO,

				ZERO, DP, TP, ZERO, DP, TP, ZERO, DP, TP, };
		ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);
		// 12个顶点24个纹理数据
		float[] texs = { 0.5f, 0f, 0, 1, 1, 1, 0.5f, 0f, 0, 1, 1, 1, 0.5f, 0f,
				0, 1, 1, 1, 0.5f, 0f, 0, 1, 1, 1, };
		ByteBuffer tbb = ByteBuffer.allocateDirect(texs.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texs);
		texBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

		gl.glPushMatrix();
		gl.glTranslatef(xOffset * UNIT_SIZE, 0, 0);
		gl.glTranslatef(0, 0, zOffset * UNIT_SIZE);
		gl.glRotatef(yAngle, 0, 1, 0);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);

		// gl.glDisableClientState(GL10.GL_TEXTURE_2D);
		// gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		// gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

		gl.glPopMatrix();
	}
}
