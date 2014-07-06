package opengl.lance.demo_11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static opengl.lance.demo_11.Constant_1.*;

import javax.microedition.khronos.opengles.GL10;

public class Desert {

	private FloatBuffer mVertexBuffer;// �����������ݻ���
	private FloatBuffer mTextureBuffer;// �����������ݻ���
	int vCount = 0;// ��������
	float yAngle;// y����ת�Ƕ�
	int xOffset;// xƫ����
	int zOffset;// zƽ����
	int texId;// ����ID
	int width;// �ذ����width����λ
	int height;// �ذ�����height����λ

	public Desert(int xOffset, int zOffset, float scale, float yAngle,
			int texId, int width, int height) {
		this.xOffset = xOffset;// ��ȡ����
		this.zOffset = zOffset;// ��ȡ����
		this.yAngle = yAngle;// ��ȡ����
		this.texId = texId;// ��ȡ����
		this.width = width;// ��ȡ����
		this.height = height;// ��ȡ����

		// �����������ݵĳ�ʼ��================begin============================
		vCount = width * height * 6;// ÿ���ذ��6������

		float vertices[] = new float[vCount * 3];
		int k = 0;
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {// ÿ���ذ��������������6�����㹹��
				vertices[k++] = i * UNIT_SIZE * scale;// ��һ�������εĵ�һ��������
				vertices[k++] = 0;
				vertices[k++] = j * UNIT_SIZE * scale;
				vertices[k++] = i * UNIT_SIZE * scale;// ��һ�������εĵڶ���������
				vertices[k++] = 0;
				vertices[k++] = (j + 1) * UNIT_SIZE * scale;
				vertices[k++] = (i + 1) * UNIT_SIZE * scale;// ��һ�������εĵ�����������
				vertices[k++] = 0;
				vertices[k++] = (j + 1) * UNIT_SIZE * scale;

				vertices[k++] = (i + 1) * UNIT_SIZE * scale;// �ڶ��������εĵ�һ��������
				vertices[k++] = 0;
				vertices[k++] = (j + 1) * UNIT_SIZE * scale;
				vertices[k++] = (i + 1) * UNIT_SIZE * scale;// �ڶ��������εĵڶ���������
				vertices[k++] = 0;
				vertices[k++] = j * UNIT_SIZE * scale;
				vertices[k++] = i * UNIT_SIZE * scale;// �ڶ��������εĵ�����������
				vertices[k++] = 0;
				vertices[k++] = j * UNIT_SIZE * scale;
			}
		;

		// ���������������ݻ���
		// vertices.length*4����Ϊһ��Float�ĸ��ֽ�
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mVertexBuffer = vbb.asFloatBuffer();// ת��Ϊint�ͻ���
		mVertexBuffer.put(vertices);// �򻺳����з��붥����������
		mVertexBuffer.position(0);// ���û�������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
		// ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// �����������ݵĳ�ʼ��================end============================

		// ���� �������ݳ�ʼ��
		float[] texST = new float[vCount * 2];
		for (int i = 0; i < vCount * 2 / 12; i++) {
			texST[i * 12] = 0;// ��һ�������εĵ�һ�����st����
			texST[i * 12 + 1] = 0;

			texST[i * 12 + 2] = 0;// ��һ�������εĵڶ������st����
			texST[i * 12 + 3] = 1;

			texST[i * 12 + 4] = 1;// ��һ�������εĵ��������st����
			texST[i * 12 + 5] = 1;

			texST[i * 12 + 6] = 1;// �ڶ��������εĵ�һ�����st����
			texST[i * 12 + 7] = 1;

			texST[i * 12 + 8] = 1;// �ڶ��������εĵڶ������st����
			texST[i * 12 + 9] = 0;

			texST[i * 12 + 10] = 0;// �ڶ��������εĵ��������st����
			texST[i * 12 + 11] = 0;
		}
		;
		ByteBuffer tbb = ByteBuffer.allocateDirect(texST.length * 4);
		tbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mTextureBuffer = tbb.asFloatBuffer();// ת��Ϊint�ͻ���
		mTextureBuffer.put(texST);// �򻺳����з��붥����ɫ����
		mTextureBuffer.position(0);// ���û�������ʼλ��
	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);// ���ö�����������

		gl.glPushMatrix();// �����ֳ�
		gl.glTranslatef(xOffset * UNIT_SIZE, 0, 0);
		gl.glTranslatef(0, 0, zOffset * UNIT_SIZE);
		gl.glRotatef(yAngle, 0, 1, 0);

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
				0, // ��ʼ����
				vCount // ���������
		);

		gl.glPopMatrix();// �ָ��ֳ�
	}
}
