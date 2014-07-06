package opengl.lance.demo_13;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class MyPyramid {
	final float UNIT_SIZE = 0.5f;
	private FloatBuffer vertexBuffer;
	private FloatBuffer texBuffer;

	private int vCount;
	private int texId;
	float xAngle;
	float yAngle;

	public MyPyramid(float scale, int texId) {
		this.texId = texId;
		// ׶���ܹ�18������
		this.vCount = 18;
		float[] vertices = { 0, 2.5f * UNIT_SIZE * scale, 0,
				-UNIT_SIZE * scale, 0, UNIT_SIZE * scale, UNIT_SIZE * scale, 0,
				UNIT_SIZE * scale,

				0, 2.5f * UNIT_SIZE * scale, 0, UNIT_SIZE * scale, 0,
				UNIT_SIZE * scale, UNIT_SIZE * scale, 0, -UNIT_SIZE * scale,

				0, 2.5f * UNIT_SIZE * scale, 0, UNIT_SIZE * scale, 0,
				-UNIT_SIZE * scale, -UNIT_SIZE * scale, 0, -UNIT_SIZE * scale,

				0, 2.5f * UNIT_SIZE * scale, 0, -UNIT_SIZE * scale, 0,
				-UNIT_SIZE * scale, -UNIT_SIZE * scale, 0,
				UNIT_SIZE * scale,

				// ���Ƶ����ı���---2��������
				UNIT_SIZE * scale, 0, -UNIT_SIZE * scale, UNIT_SIZE * scale, 0,
				UNIT_SIZE * scale, -UNIT_SIZE * scale, 0, -UNIT_SIZE * scale,

				UNIT_SIZE * scale, 0, UNIT_SIZE * scale, -UNIT_SIZE * scale, 0,
				UNIT_SIZE * scale, -UNIT_SIZE * scale, 0, -UNIT_SIZE * scale, };
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		float[] texs = { 0.5f, 0, 0, 1, 1, 1,

		0.5f, 0, 0, 1, 1, 1,

		0.5f, 0, 0, 1, 1, 1,

		0.5f, 0, 0, 1, 1, 1,
				// ע��������ͼ���������������ῴ����ͼƬ
				0, 0, 0, 1, 1, 0,

				0, 1, 1, 1, 1, 0, };
		ByteBuffer tbb = ByteBuffer.allocateDirect(texs.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texs);
		texBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(xAngle, 1, 0, 0);
		gl.glRotatef(yAngle, 0, 1, 0);
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
