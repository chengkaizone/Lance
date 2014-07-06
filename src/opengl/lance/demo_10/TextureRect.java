package opengl.lance.demo_10;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

public class TextureRect {
	// ע����ڻ���ƽ���ͼ��ʹ����������---���ڹ̶��ĵ�---ʹ�ø�������Ч��
	private IntBuffer vertexBuffer;
	private FloatBuffer texBuffer;

	private int vCount;
	private int texId;

	public TextureRect(int texId) {
		this.texId = texId;
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

		float[] texs = { 0, 0, 0, 1, 1, 1,

		1, 1, 1, 0, 0, 0, };
		ByteBuffer tbb = ByteBuffer.allocateDirect(texs.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texs);
		texBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, vertexBuffer);
		// ����������ͼ
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
		// ��ͼ
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);

		// gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
}
