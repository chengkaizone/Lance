package opengl.lance.demo_6;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import static opengl.lance.demo_6.OContant.*;

public class OColorRect {
	private FloatBuffer vertexBuffer;
	private IntBuffer colorBuffer;
	private int vCount;

	public OColorRect(float width, float height) {
		vCount = 6;
		float[] vertices = { 0, 0, 0, width * UNIT_SIZE, height * UNIT_SIZE, 0,
				-width * UNIT_SIZE, height * UNIT_SIZE, 0, -width * UNIT_SIZE,
				-height * UNIT_SIZE, 0, width * UNIT_SIZE, -height * UNIT_SIZE,
				0, width * UNIT_SIZE, height * UNIT_SIZE, 0, };
		ByteBuffer vbb = ByteBuffer.allocateDirect(vCount * 3 * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		int[] colors = { ONE, ONE, ONE, 0, 0, 0, ONE, 0, 0, 0, ONE, 0, 0, 0,
				ONE, 0, 0, 0, ONE, 0, 0, 0, ONE, 0, };
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asIntBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, colorBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, vCount);
	}
}
