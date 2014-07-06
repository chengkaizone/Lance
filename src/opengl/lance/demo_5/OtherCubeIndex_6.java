package opengl.lance.demo_5;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class OtherCubeIndex_6 {
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private ByteBuffer indexBuffer;
	float xOffset;
	float yOffset;
	private int vCount;
	private int iCount;

	public OtherCubeIndex_6(float scale, float length, float width) {
		final float UNIT_SIZE = 0.5f;
		final float UNIT_HIGHT = 0.5f;
		float[] vertices = { -UNIT_SIZE * length, UNIT_HIGHT * scale,
				-UNIT_SIZE * width,// 0号点
				UNIT_SIZE * length, UNIT_HIGHT * scale, -UNIT_SIZE * width,// 1号点
				-UNIT_SIZE * length, UNIT_HIGHT * scale, UNIT_SIZE * width,// 2号点
				UNIT_SIZE * length, UNIT_HIGHT * scale, UNIT_SIZE * width,// 3号点

				-UNIT_SIZE * length, -UNIT_HIGHT * scale, -UNIT_SIZE * width,// 4号点
				UNIT_SIZE * length, -UNIT_HIGHT * scale, -UNIT_SIZE * width,// 5号点
				-UNIT_SIZE * length, -UNIT_HIGHT * scale, UNIT_SIZE * width,// 6号点
				UNIT_SIZE * length, -UNIT_HIGHT * scale, UNIT_SIZE * width// 7号点
		};
		vCount = vertices.length / 3;
		ByteBuffer vbb = ByteBuffer.allocateDirect(vCount * 3 * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		float[] normals = { -1, 1, -1,// -1/3,1/3,-1/3,
				1, 1, -1,// 1/3,1/3,-1/3,
				-1, 1, 1,// -1/3,1/3,1/3,
				1, 1, 1,// 1/3,1/3,1/3,

				-1, -1, -1,// -1/3,-1/3,-1/3,
				1, -1 - 1,// 1/3,-1/3,-1/3,
				-1, -1, 1,// -1/3,-1/3,1/3,
				1, -1, 1 // 1/3,-1/3,1/3
		};
		ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);

		byte[] indices = {
				// 后面
				4, 0, 5, 5, 0, 1,
				// 前面
				2, 6, 3, 3, 6, 7,
				// 顶面
				0, 2, 1, 1, 2, 3,
				// 底面
				6, 4, 7, 7, 4, 5,
				// 左面
				4, 6, 2, 2, 0, 4,
				// 右面
				1, 3, 7, 7, 5, 1 };
		iCount = indices.length;
		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(xOffset, 1, 0, 0);
		gl.glRotatef(yOffset, 0, 1, 0);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);

		gl.glDrawElements(GL10.GL_TRIANGLES, iCount, GL10.GL_UNSIGNED_BYTE,
				indexBuffer);
	}
}
