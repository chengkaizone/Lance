package opengl.lance.demo_4;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 * @author Administrator
 * 
 */
public class TrianglePair2 {
	private IntBuffer vertexBuffer;
	private IntBuffer colorBuffer;
	int vCount;
	float yAngle;
	float zAngle;

	public TrianglePair2() {
		final int UNIT_SIZE = 10000;
		int[] vertices = new int[] { -8 * UNIT_SIZE, 10 * UNIT_SIZE, 0,
				-2 * UNIT_SIZE, 2 * UNIT_SIZE, 0, -8 * UNIT_SIZE,
				2 * UNIT_SIZE, 0, 8 * UNIT_SIZE, 2 * UNIT_SIZE, 0,
				8 * UNIT_SIZE, 10 * UNIT_SIZE, 0, 2 * UNIT_SIZE,
				10 * UNIT_SIZE, 0 };
		vCount = vertices.length / 3;
		ByteBuffer vbb = ByteBuffer.allocateDirect(vCount * 3 * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asIntBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		final int one = 65535;
		int[] colors = new int[] { one, one, one, 0, 0, 0, one, 0, 0, 0, one,
				0, one, one, one, 0, one, 0, 0, 0, one, 0, 0, 0 };

		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asIntBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glRotatef(yAngle, 0, 1, 0);
		gl.glRotatef(zAngle, 0, 0, 1);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, colorBuffer);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);
	}

}
