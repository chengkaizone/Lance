package opengl.lance.demo_6;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class RTexture {
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private int vCount;
	private int texId;

	public RTexture(float width, float height, int texId, int sRange, int tRange) {
		this.texId = texId;
		vCount = 6;
		final float UNIT_SIZE = 1f;
		float[] vertices = { width * UNIT_SIZE, height * UNIT_SIZE, 0,
				-width * UNIT_SIZE, height * UNIT_SIZE, 0, -width * UNIT_SIZE,
				-height * UNIT_SIZE, 0,

				-width * UNIT_SIZE, -height * UNIT_SIZE, 0, width * UNIT_SIZE,
				-height * UNIT_SIZE, 0, width * UNIT_SIZE, height * UNIT_SIZE,
				0, };
		ByteBuffer vbb = ByteBuffer.allocateDirect(vCount * 3 * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		float[] texST = { 
				sRange, 0, 
				0, 0,
				0, tRange, 
				0, tRange, 
				sRange,tRange,
				sRange, 0, };
		ByteBuffer tbb = ByteBuffer.allocateDirect(vCount * 2 * 4);
		tbb.order(ByteOrder.nativeOrder());
		textureBuffer = tbb.asFloatBuffer();
		textureBuffer.put(texST);
		textureBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnable(GL10.GL_TEXTURE_2D);// 开启纹理
		// 开启纹理坐标数组---比其他方式多一步
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// 传入数据
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		// 绑定纹理ID
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
		// 开始绘制
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);
	}
}
