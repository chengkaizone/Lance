package opengl.lance.demo_11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import static opengl.lance.demo_11.Constant_3.*;

public class Land {
	private FloatBuffer vertexBuffer;
	private FloatBuffer texBuffer;
	private int vCount;
	private int texId;

	/**
	 * ��ʼ���ݵ�
	 * 
	 * @param texId
	 * @param mapArr
	 *            ---�ӻҶ�ͼ�м���½����ÿ������ĸ߶�
	 * @param rows
	 * @param cols
	 */
	public Land(int texId, float[][] colorArray) {
		int rows = colorArray.length - 1;
		int cols = colorArray[0].length - 1;
		this.texId = texId;
		vCount = rows * cols * 3 * 2;
		float[] vertices = new float[vCount * 3];
		int c = 0;
		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < cols; i++) {
				// ���㵱ǰ�������ϲ������
				float zsx = -UNIT_SIZE * cols / 2 + i * UNIT_SIZE;
				float zsz = -UNIT_SIZE * rows / 2 + j * UNIT_SIZE;

				vertices[c++] = zsx;// ���ε�һ����XYZ����
				vertices[c++] = yArray[j][i];
				vertices[c++] = zsz;

				vertices[c++] = zsx;// ���εڶ�����XYZ����
				vertices[c++] = yArray[j + 1][i];
				vertices[c++] = zsz + UNIT_SIZE;

				vertices[c++] = zsx + UNIT_SIZE;// ���ε��ĸ���XYZ����
				vertices[c++] = yArray[j][i + 1];
				vertices[c++] = zsz;

				vertices[c++] = zsx + UNIT_SIZE;// ���ε��ĸ���XYZ����
				vertices[c++] = yArray[j][i + 1];
				vertices[c++] = zsz;

				vertices[c++] = zsx;// ���εڶ�����XYZ����
				vertices[c++] = yArray[j + 1][i];
				vertices[c++] = zsz + UNIT_SIZE;

				vertices[c++] = zsx + UNIT_SIZE;// ���ε�������XYZ����
				vertices[c++] = yArray[j + 1][i + 1];
				vertices[c++] = zsz + UNIT_SIZE;
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

	// �Զ��з����������������ķ���
	public float[] generateTexCoor(int bw, int bh) {
		float[] result = new float[bw * bh * 6 * 2];
		float sizew = 1.0f / bw;// ����
		float sizeh = 1.0f / bh;// ����
		// �����ظ�ճ��ST���귶Χ
		int c = 0;
		for (int i = 0; i < bh; i++) {
			for (int j = 0; j < bw; j++) {
				// ÿ����һ�����Σ������������ι��ɣ��������㣬12����������
				float s = j * sizew;
				float t = i * sizeh;

				result[c++] = s;
				result[c++] = t;

				result[c++] = s;
				result[c++] = t + sizeh;

				result[c++] = s + sizew;
				result[c++] = t;

				result[c++] = s + sizew;
				result[c++] = t;

				result[c++] = s;
				result[c++] = t + sizeh;

				result[c++] = s + sizew;
				result[c++] = t + sizeh;
			}
		}
		return result;
	}

}
