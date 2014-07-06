package opengl.lance.demo_15;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class ZhuiTi {

	private FloatBuffer mVertexBuffer; // �����������ݻ���
	private FloatBuffer mTextureBuffer;// �����������ݻ���
	int textureId;// ����ID
	int vCount;// ��������
	float mOffsetX;
	float mOffsetY;
	float[] tempVerteices;// ������������

	public ZhuiTi(int textureId, float length, float hight, float width) {
		this.textureId = textureId;
		float UNIT_SIZE = 1.0f;
		float[] verteices = {
				// ����
				0, UNIT_SIZE * hight, 0,
				UNIT_SIZE * length,
				0,
				-UNIT_SIZE * width,
				-UNIT_SIZE * length,
				0,
				-UNIT_SIZE * width,
				// ����
				0, UNIT_SIZE * hight, 0, -UNIT_SIZE * length,
				0,
				-UNIT_SIZE * width,
				-UNIT_SIZE * length,
				0,
				UNIT_SIZE * width,
				// ǰ��
				0, UNIT_SIZE * hight, 0, -UNIT_SIZE * length, 0,
				UNIT_SIZE * width,
				UNIT_SIZE * length,
				0,
				UNIT_SIZE * width,
				// ����
				0, UNIT_SIZE * hight, 0, UNIT_SIZE * length, 0,
				UNIT_SIZE * width, UNIT_SIZE * length,
				0,
				-UNIT_SIZE * width,
				// ����
				-UNIT_SIZE * length, 0, -UNIT_SIZE * width, UNIT_SIZE * length,
				0, -UNIT_SIZE * width, -UNIT_SIZE * length, 0,
				UNIT_SIZE * width,

				-UNIT_SIZE * length, 0, UNIT_SIZE * width, UNIT_SIZE * length,
				0, -UNIT_SIZE * width, UNIT_SIZE * length, 0, UNIT_SIZE * width

		};
		vCount = verteices.length / 3;// ��ȡ��������
		ByteBuffer vbb = ByteBuffer.allocateDirect(verteices.length * 4); // ���������������ݻ���
		vbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mVertexBuffer = vbb.asFloatBuffer();// ת��Ϊfloat�ͻ���
		mVertexBuffer.put(verteices);// �򻺳����з��붥����������
		mVertexBuffer.position(0);// ���û�������ʼλ��

		tempVerteices = verteices;

		float[] textureCoors = new float[vCount * 2];
		for (int i = 0; i < vCount / 3; i++)// ������������
		{
			textureCoors[i * 6] = 0;
			textureCoors[i * 6 + 1] = 0;

			textureCoors[i * 6 + 2] = 0;
			textureCoors[i * 6 + 3] = 1;

			textureCoors[i * 6 + 4] = 1;
			textureCoors[i * 6 + 5] = 0;
		}
		ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoors.length * 4);// ���������������ݻ���
		tbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mTextureBuffer = tbb.asFloatBuffer();// ת��Ϊfloat�ͻ���
		mTextureBuffer.put(textureCoors);// �򻺳����з��붥������
		mTextureBuffer.position(0);// ���û�������ʼλ��

	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(mOffsetX, 1, 0, 0);
		gl.glRotatef(mOffsetY, 0, 1, 0);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);

		gl.glEnable(GL10.GL_TEXTURE_2D);// ��������
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);// ����ʹ����������
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);// ָ����������
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);// ������

		gl.glPushMatrix();
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);// ����
		gl.glPopMatrix();

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);// �ر���������
		gl.glDisable(GL10.GL_TEXTURE_2D);// �ر�����

	}
}
