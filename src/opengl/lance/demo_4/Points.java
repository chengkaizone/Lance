package opengl.lance.demo_4;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Points {
	private IntBuffer vertexBuffer;
	private IntBuffer colorBuffer;
	private ByteBuffer indexBuffer;
	private int vCount;
	private int iCount;

	public Points() {
		final int UNIT_SIZE = 10000;
		int[] vertices = new int[] { -20 * UNIT_SIZE, 13 * UNIT_SIZE, 0,
				-1 * UNIT_SIZE, 1 * UNIT_SIZE, 0, -13 * UNIT_SIZE,
				-2 * UNIT_SIZE, 0, -2 * UNIT_SIZE, -34 * UNIT_SIZE, 0 };
		vCount = vertices.length / 3;
		ByteBuffer vbb = ByteBuffer.allocateDirect(vCount * 3 * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asIntBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		final int one = 65535;
		int[] colors = new int[] { one, one, one, 0, one, one, one, 0, one,
				one, one, 0, one, one, one, 0 };
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asIntBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);

		byte[] indexs = new byte[] { 0, 3, 2, 1 };
		iCount = indexs.length;
		indexBuffer = ByteBuffer.allocateDirect(iCount);
		indexBuffer.put(indexs);
		indexBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glVertexPointer(3, GL10.GL_FIXED, 0, vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, colorBuffer);

		gl.glDrawElements(GL10.GL_POINTS, iCount, GL10.GL_UNSIGNED_BYTE,
				indexBuffer);
	}
}
