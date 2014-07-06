package opengl.lance.demo_9;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class MyDesert {
	private final float UNIT_SIZE = 0.5f;
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer texBuffer;
	private int vCount;
	private int texId;
	int xOffset;
	int zOffset;
	float yAngle;

	public MyDesert(int xOffset, int zOffset, float scale, float yAngle,
			int texId, int width, int height) {
		this.xOffset = xOffset;
		this.zOffset = zOffset;
		this.yAngle = yAngle;
		this.texId = texId;
		// 顶点数量
		vCount = width * height * 6;
		// 一个顶点三个数据
		float[] vertices = new float[vCount * 3];
		for (int c = 0, i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				vertices[c++] = i * UNIT_SIZE * scale;
				vertices[c++] = 0;
				vertices[c++] = j * UNIT_SIZE * scale;

				vertices[c++] = i * UNIT_SIZE * scale;
				vertices[c++] = 0;
				vertices[c++] = (j + 1) * UNIT_SIZE * scale;

				vertices[c++] = (i + 1) * UNIT_SIZE * scale;
				vertices[c++] = 0;
				vertices[c++] = (j + 1) * UNIT_SIZE * scale;

				vertices[c++] = (i + 1) * UNIT_SIZE * scale;
				vertices[c++] = 0;
				vertices[c++] = (j + 1) * UNIT_SIZE * scale;

				vertices[c++] = (i + 1) * UNIT_SIZE * scale;
				vertices[c++] = 0;
				vertices[c++] = j * UNIT_SIZE * scale;

				vertices[c++] = i * UNIT_SIZE * scale;
				vertices[c++] = 0;
				vertices[c++] = j * UNIT_SIZE * scale;
			}
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vCount * 3 * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		// 因为只是基于平面的原因；所以平面的法向量一般都是基于垂直方向的；垂直于平面
		float[] normals = new float[vCount * 3];
		for (int i = 0; i < vCount; i++) {
			normals[i * 3] = 0;
			normals[i * 3 + 1] = 1;
			normals[i * 3 + 2] = 0;
		}
		ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);

		float[] texs = new float[vCount * 2];
		for (int i = 0; i < (vCount * 2 / 12); i++) {
			int c = 0;
			texs[i * 12 + c++] = 0;
			texs[i * 12 + c++] = 0;

			texs[i * 12 + c++] = 0;
			texs[i * 12 + c++] = 1;

			texs[i * 12 + c++] = 1;
			texs[i * 12 + c++] = 1;

			texs[i * 12 + c++] = 1;
			texs[i * 12 + c++] = 1;

			texs[i * 12 + c++] = 1;
			texs[i * 12 + c++] = 0;

			texs[i * 12 + c++] = 0;
			texs[i * 12 + c++] = 0;
		}
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
		gl.glTranslatef(xOffset, 0, 0);
		gl.glTranslatef(0, 0, zOffset);
		gl.glRotatef(yAngle, 0, 1, 0);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);

		gl.glPopMatrix();
	}
}
