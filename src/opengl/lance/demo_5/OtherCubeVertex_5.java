package opengl.lance.demo_5;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class OtherCubeVertex_5 {
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	float xOffset;
	float yOffset;
	private int vCount;

	public OtherCubeVertex_5(float scale, float length, float width) {

		final float UNIT_SIZE = 0.5f;
		final float UNIT_HIGHT = 0.5f;
		float[] vertices = {
				// 顶面
				-UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				-UNIT_SIZE * width,
				-UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				UNIT_SIZE * width,
				UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				-UNIT_SIZE * width,

				UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				-UNIT_SIZE * width,
				-UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				UNIT_SIZE * width,
				UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				UNIT_SIZE * width,

				// 后面
				-UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				-UNIT_SIZE * width,
				-UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				-UNIT_SIZE * width,
				UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				-UNIT_SIZE * width,

				UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				-UNIT_SIZE * width,
				-UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				-UNIT_SIZE * width,
				UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				-UNIT_SIZE * width,

				// 前面
				-UNIT_SIZE * length, UNIT_HIGHT * scale, UNIT_SIZE * width,
				-UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				UNIT_SIZE * width,
				UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				UNIT_SIZE * width,

				UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				UNIT_SIZE * width,
				-UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				UNIT_SIZE * width,
				UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				UNIT_SIZE * width,

				// 下面
				-UNIT_SIZE * length, -UNIT_HIGHT * scale, UNIT_SIZE * width,
				-UNIT_SIZE * length, -UNIT_HIGHT * scale, -UNIT_SIZE * width,
				UNIT_SIZE * length, -UNIT_HIGHT * scale, UNIT_SIZE * width,

				UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				UNIT_SIZE * width,
				-UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				-UNIT_SIZE * width,
				UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				-UNIT_SIZE * width,

				// 左面
				-UNIT_SIZE * length, -UNIT_HIGHT * scale, -UNIT_SIZE * width,
				-UNIT_SIZE * length, -UNIT_HIGHT * scale, UNIT_SIZE * width,
				-UNIT_SIZE * length, UNIT_HIGHT * scale, -UNIT_SIZE * width,

				-UNIT_SIZE * length, UNIT_HIGHT * scale, -UNIT_SIZE * width,
				-UNIT_SIZE * length, -UNIT_HIGHT * scale,
				UNIT_SIZE * width,
				-UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				UNIT_SIZE * width,

				// 右面
				UNIT_SIZE * length, UNIT_HIGHT * scale, -UNIT_SIZE * width,
				UNIT_SIZE * length, UNIT_HIGHT * scale, UNIT_SIZE * width,
				UNIT_SIZE * length, -UNIT_HIGHT * scale, -UNIT_SIZE * width,

				UNIT_SIZE * length, -UNIT_HIGHT * scale, -UNIT_SIZE * width,
				UNIT_SIZE * length, UNIT_HIGHT * scale, UNIT_SIZE * width,
				UNIT_SIZE * length, -UNIT_HIGHT * scale, UNIT_SIZE * width

		};
		vCount = vertices.length / 3;
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		float[] normals = {
				// 顶面顶点法向量
				0, 1, 0, 0, 1, 0, 0, 1, 0,

				0, 1, 0, 0, 1, 0, 0, 1, 0,
				// 后面顶点法向量
				0, 0, -1, 0, 0, -1, 0, 0, -1,

				0, 0, -1, 0, 0, -1, 0, 0, -1,
				// 前面顶点法向量
				0, 0, 1, 0, 0, 1, 0, 0, 1,

				0, 0, 1, 0, 0, 1, 0, 0, 1,
				// 下面顶点法向量
				0, -1, 0, 0, -1, 0, 0, -1, 0,

				0, -1, 0, 0, -1, 0, 0, -1, 0,
				// 左面顶点法向量
				-1, 0, 0, -1, 0, 0, -1, 0, 0,

				-1, 0, 0, -1, 0, 0, -1, 0, 0,
				// 右面顶点法向量
				1, 0, 0, 1, 0, 0, 1, 0, 0,

				1, 0, 0, 1, 0, 0, 1, 0, 0 };
		ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(xOffset, 1, 0, 0);
		gl.glRotatef(yOffset, 0, 1, 0);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);
	}
}
