package opengl.lance.demo_10;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

public class ColorRect {
	private IntBuffer vertexBuffer;
	private IntBuffer colorBuffer;

	private int vCount;

	public ColorRect(int r, int g, int b, int alpha) {
		final int UNIT_SIZE = 40000;
		int[] vertices = { -1 * UNIT_SIZE, 1 * UNIT_SIZE, 0, -1 * UNIT_SIZE,
				-1 * UNIT_SIZE, 0, 1 * UNIT_SIZE, -1 * UNIT_SIZE, 0,

				1 * UNIT_SIZE, -1 * UNIT_SIZE, 0, 1 * UNIT_SIZE, 1 * UNIT_SIZE,
				0, -1 * UNIT_SIZE, 1 * UNIT_SIZE, 0, };
		vCount = vertices.length / 3;
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asIntBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		int[] colors = { r, g, b, alpha, r, g, b, alpha, r, g, b, alpha, r, g,
				b, alpha, r, g, b, alpha, r, g, b, alpha, };
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asIntBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);

	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glVertexPointer(3, GL10.GL_FIXED, 0, vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, colorBuffer);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);
		// 此处关闭以使绘图效果不会影响后面的绘图---在多次绘图中最好开启的功能使用后都一一关闭

		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
