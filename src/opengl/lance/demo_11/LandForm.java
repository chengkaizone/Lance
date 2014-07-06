package opengl.lance.demo_11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

import static opengl.lance.demo_11.Constant_3.*;

public class LandForm {

	private FloatBuffer mVertexBuffer;// �����������ݻ���
	private FloatBuffer mTextureBuffer;// ������ɫ���ݻ���
	int vCount;// ��������
	int texId;// ����Id

	public LandForm(int texId, float[][] yArray) {
		this.texId = texId;
		int rows = yArray.length - 1;
		int cols = yArray[0].length - 1;
		// �����������ݵĳ�ʼ��================begin============================
		vCount = cols * rows * 2 * 3;// ÿ���������������Σ�ÿ��������3������
		float vertices[] = new float[vCount * 3];// ÿ������xyz��������
		int count = 0;// ���������
		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < cols; i++) {
				// ���㵱ǰ�������ϲ������
				float zsx = -UNIT_SIZE * cols / 2 + i * UNIT_SIZE;
				float zsz = -UNIT_SIZE * rows / 2 + j * UNIT_SIZE;

				vertices[count++] = zsx;// ���ε�һ����XYZ����
				vertices[count++] = yArray[j][i];
				vertices[count++] = zsz;

				vertices[count++] = zsx;// ���εڶ�����XYZ����
				vertices[count++] = yArray[j + 1][i];
				vertices[count++] = zsz + UNIT_SIZE;

				vertices[count++] = zsx + UNIT_SIZE;// ���ε��ĸ���XYZ����
				vertices[count++] = yArray[j][i + 1];
				vertices[count++] = zsz;

				vertices[count++] = zsx + UNIT_SIZE;// ���ε��ĸ���XYZ����
				vertices[count++] = yArray[j][i + 1];
				vertices[count++] = zsz;

				vertices[count++] = zsx;// ���εڶ�����XYZ����
				vertices[count++] = yArray[j + 1][i];
				vertices[count++] = zsz + UNIT_SIZE;

				vertices[count++] = zsx + UNIT_SIZE;// ���ε�������XYZ����
				vertices[count++] = yArray[j + 1][i + 1];
				vertices[count++] = zsz + UNIT_SIZE;
			}
		}

		// ���������������ݻ���
		// vertices.length*4����Ϊһ�������ĸ��ֽ�
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mVertexBuffer = vbb.asFloatBuffer();// ת��Ϊint�ͻ���
		mVertexBuffer.put(vertices);// �򻺳����з��붥����������
		mVertexBuffer.position(0);// ���û�������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
		// ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// �����������ݵĳ�ʼ��================end============================

		// �����������ݵĳ�ʼ��================begin============================
		// �Զ������������飬20��15��
		float textures[] = generateTexCoor(cols, rows);

		// ���������������ݻ���
		ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
		tbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mTextureBuffer = tbb.asFloatBuffer();// ת��ΪFloat�ͻ���
		mTextureBuffer.put(textures);// �򻺳����з��붥����ɫ����
		mTextureBuffer.position(0);// ���û�������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
		// ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// �����������ݵĳ�ʼ��================end============================
	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);// ���ö�����������

		// Ϊ����ָ��������������
		gl.glVertexPointer(3, // ÿ���������������Ϊ3 xyz
				GL10.GL_FLOAT, // ��������ֵ������Ϊ GL_FIXED
				0, // ����������������֮��ļ��
				mVertexBuffer // ������������
		);

		// ��������
		gl.glEnable(GL10.GL_TEXTURE_2D);
		// ����ʹ������ST���껺��
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// Ϊ����ָ������ST���껺��
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
		// �󶨵�ǰ����
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);

		// ����ͼ��
		gl.glDrawArrays(GL10.GL_TRIANGLES, // �������η�ʽ���
				0, vCount);

		// �ر�����
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}

	// �Զ��з����������������ķ���
	public float[] generateTexCoor(int bw, int bh) {
		float[] result = new float[bw * bh * 6 * 2];
		float sizew = 1.0f / bw;// ����
		float sizeh = 1.0f / bh;// ����
		// �����ظ�ճ��ST���귶Χ
		// float sizew=8.0f/bw;//����
		// float sizeh=8.0f/bh;//����
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
