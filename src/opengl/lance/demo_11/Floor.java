package opengl.lance.demo_11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Floor {
	private FloatBuffer vertexBuffer;
	private FloatBuffer texBuffer;
	private int vCount;
	private int texId;

	public Floor(float width, float height, int texId) {
		this.texId = texId;
		final float UNIT_SIZE = 1f;
		vCount = 6;
		float[] vertices = { -UNIT_SIZE * width, 0, -UNIT_SIZE * height,
				-UNIT_SIZE * width, 0, UNIT_SIZE * height, UNIT_SIZE * width,
				0, -UNIT_SIZE * height, -UNIT_SIZE * width, 0,
				UNIT_SIZE * height, UNIT_SIZE * width, 0, UNIT_SIZE * height,
				UNIT_SIZE * width, 0, -UNIT_SIZE * height,

		};
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		float[] texs = { 0, 0, 0, 0.642f, 1, 0, 0, 0.642f, 1, 0.642f, 1, 0, };
		ByteBuffer tbb = ByteBuffer.allocateDirect(texs.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texs);
		texBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
