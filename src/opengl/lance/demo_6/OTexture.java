package opengl.lance.demo_6;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Œ∆¿Ì”≥…‰»˝Ω«–Œ
 * 
 * @author Administrator
 * 
 */
public class OTexture {
	private IntBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	float yAngle;
	float zAngle;
	private int vCount;
	private int textureId;

	public OTexture(int textureId) {
		this.textureId = textureId;
		final int UNIT_SIZE = 30000;
		int[] vertices = { 2 * UNIT_SIZE, 0, 0, -2 * UNIT_SIZE, 0, 0, 0,
				4 * UNIT_SIZE, 0, };
		vCount = vertices.length / 3;
		ByteBuffer vbb = ByteBuffer.allocateDirect(vCount * 3 * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asIntBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		float[] textureCoors = { 0, 1, 1, 1, 0.5f, 0, };
		ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoors.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		textureBuffer = tbb.asFloatBuffer();
		textureBuffer.put(textureCoors);
		textureBuffer.position(0);

	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(zAngle, 0, 0, 1);
		gl.glRotatef(yAngle, 0, 1, 0);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, vertexBuffer);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
}
