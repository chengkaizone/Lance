package opengl.lance.demo_11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * ����ͼ�εĶ���������Ҫ����---���εĶ�������ǹ̶���
 * 
 * @author Administrator
 * 
 */
public class FlagRect {
	private FloatBuffer vertexBuffer;
	private FloatBuffer texBuffer;
	private int vCount;
	private int texId;

	/**
	 * ����ID
	 * 
	 * @param texId
	 * @param startAngle��ʼ�Ƕ�
	 */
	public FlagRect(int texId, double startAngle) {
		this.texId = texId;
		final int cols = 20;
		final int rows = cols * 3 / 4;
		// ÿ��ĵ�λ����
		final float UNIT_SIZE = 1.5f / cols;
		// ����ÿһ��ĵ�λ����---һ��Ҫע��˴�ʹ�õ��ǻ���--����Ҫת��
		final double angleSpan = Math.PI * 4 / cols;
		// ���㶥������---����=2��������--6������
		vCount = cols * rows * 2 * 3;
		int c = 0;
		float[] vertices = new float[vCount * 3];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				float x = -UNIT_SIZE * cols / 2 + j * UNIT_SIZE;
				float y = UNIT_SIZE * rows / 2 - i * UNIT_SIZE;
				float z1 = (float) Math.sin(startAngle + j * angleSpan) * 0.1f;
				float z2 = (float) Math.sin(startAngle + (j + 1) * angleSpan) * 0.1f;
				vertices[c++] = x;
				vertices[c++] = y;
				vertices[c++] = z1;

				vertices[c++] = x;
				vertices[c++] = y - UNIT_SIZE;
				vertices[c++] = z1;

				vertices[c++] = x + UNIT_SIZE;
				vertices[c++] = y;
				vertices[c++] = z2;

				vertices[c++] = x;
				vertices[c++] = y - UNIT_SIZE;
				vertices[c++] = z1;

				vertices[c++] = x + UNIT_SIZE;
				vertices[c++] = y - UNIT_SIZE;
				vertices[c++] = z2;

				vertices[c++] = x + UNIT_SIZE;
				vertices[c++] = y;
				vertices[c++] = z2;
			}
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		float textures[] = generateTexCoor(cols, rows);
		// ���������������ݻ���
		ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(textures);
		texBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);// ���ö�����������
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

	// �Զ��з����������������ķ���bwˮƽ�����ϵķ�����bh---��ֱ�����ϵ�����---ע���зִ���
	public float[] generateTexCoor(int bw, int bh) {
		float[] result = new float[bw * bh * 6 * 2];
		float sizew = 1.0f / bw;// ����
		float sizeh = 0.75f / bh;// ��ֱ��i���Ǹ��ϵĿ��
		int c = 0;
		// �з�������ע��ѭ������
		for (int i = 0; i < bh; i++) {
			for (int j = 0; j < bw; j++) {
				float s = j * sizew;
				float t = i * sizeh;
				result[c++] = s;
				result[c++] = t;
				result[c++] = s;
				result[c++] = t + sizeh;
				result[c++] = s + sizew;
				result[c++] = t;

				result[c++] = s;
				result[c++] = t + sizeh;
				result[c++] = s + sizew;
				result[c++] = t + sizeh;
				result[c++] = s + sizew;
				result[c++] = t;
			}
		}
		return result;
	}
}
