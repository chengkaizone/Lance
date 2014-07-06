package opengl.lance.demo_11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import static opengl.lance.demo_11.Constant_3.*;

public class Water {
	private FloatBuffer vertexBuffer;
	private FloatBuffer texBuffer;

	private int vCount;
	private int texId;

	/**
	 * @param texId������ͼid
	 * @param startAngle��ʼ�Ƕ�
	 * @param rowsˮƽ�зֵķ���
	 * @param cols��ֱ�зֵķ���
	 * @param multiply����
	 */
	public Water(int texId, double startAngle, int rows, int cols, int multiply) {
		this.texId = texId;
		// ���㶥����
		vCount = rows * cols * 2 * 3;
		// ÿ��������������
		float[] vertices = new float[vCount * 3];
		// ת�ǿ��
		double angleSpan = Math.PI * 4 / cols;
		int c = 0;
		final float LOCAL_UNIT_SIZE = UNIT_SIZE * multiply;
		// ѭ����
		for (int i = 0; i < rows; i++) {
			// ѭ����---����
			for (int j = 0; j < cols; j++) {
				// ���㵱ǰѭ���Ķ�������
				float x = -LOCAL_UNIT_SIZE * cols / 2 + j * LOCAL_UNIT_SIZE;
				float z = -LOCAL_UNIT_SIZE * cols / 2 + i * LOCAL_UNIT_SIZE;

				float y1 = (float) Math.sin(startAngle + j * angleSpan) * 0.4f
						- WATER_HIGH_ADJUST;
				float y2 = (float) Math.sin(startAngle + (j + 1) * angleSpan)
						* 0.4f - WATER_HIGH_ADJUST;

				vertices[c++] = x;
				vertices[c++] = y1;
				vertices[c++] = z;

				vertices[c++] = x;
				vertices[c++] = y1;
				vertices[c++] = z + LOCAL_UNIT_SIZE;

				vertices[c++] = x + LOCAL_UNIT_SIZE;
				vertices[c++] = y2;
				vertices[c++] = z;

				vertices[c++] = x;
				vertices[c++] = y1;
				vertices[c++] = z + LOCAL_UNIT_SIZE;

				vertices[c++] = x + LOCAL_UNIT_SIZE;
				vertices[c++] = y2;
				vertices[c++] = z + LOCAL_UNIT_SIZE;

				vertices[c++] = x + LOCAL_UNIT_SIZE;
				vertices[c++] = y2;
				vertices[c++] = z;
			}
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		float[] texs = texCoors(rows, cols);
		ByteBuffer tbb = ByteBuffer.allocateDirect(texs.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texs);
		texBuffer.position(0);

	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// Ϊ����ָ��������������
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		// ��������
		gl.glEnable(GL10.GL_TEXTURE_2D);
		// ����ʹ������ST���껺��
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// Ϊ����ָ������ST���껺��
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		// �󶨵�ǰ����
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
		// ����ͼ��
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);

		// �ر�����
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	private float[] texCoors(int bw, int bh) {
		float[] results = new float[bw * bh * 6 * 2];
		float sizew = 1f / bw;
		float sizeh = 0.75f / bh;
		int c = 0;
		for (int i = 0; i < bh; i++) {
			// ���з�����ͼƬʱ��ˮƽ�и����ˮƽ�и�
			for (int j = 0; j < bw; j++) {
				float s = j * sizew;
				float t = i * sizeh;

				results[c++] = s;
				results[c++] = t;

				results[c++] = s;
				results[c++] = t + sizeh;

				results[c++] = s + sizew;
				results[c++] = t;

				results[c++] = s;
				results[c++] = t + sizeh;

				results[c++] = s + sizew;
				results[c++] = t + sizeh;

				results[c++] = s + sizew;
				results[c++] = t;
			}
		}
		return results;
	}
}
