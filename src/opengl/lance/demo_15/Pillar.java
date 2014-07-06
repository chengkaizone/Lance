package opengl.lance.demo_15;

import static opengl.lance.demo_15.Constant_7.BIGER_FACTER;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Pillar extends Shape {
	private FloatBuffer mVertexBuffer;// �����������ݻ���
	private FloatBuffer mTextureBuffer;// �����������ݻ���
	public float mAngleY;// ��Y����ת
	float scale;// ������߶�
	int vCount;// ��������
	int textureId;// ����ID
	float[] tempVerteices;
	float minX;// x����Сλ��
	float maxX;// x�����λ��
	float minY;// y����Сλ��
	float maxY;// y�����λ��
	float minZ;// z����Сλ��
	float maxZ;// z�����λ��
	float midX;// ���ĵ�����
	float midY;
	float midZ;
	public float xOffset;// �������ƶ���λ��
	public float yOffset;
	public float zOffset;

	public Pillar(int textureId, float scale, float length, float width,
			float xOffset, float yOffset, float zOffset) {
		this.scale = scale;
		this.textureId = textureId;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
		vCount = 36;
		float UNIT_SIZE = 0.5f;// ��λ����
		float UNIT_HIGHT = 0.5f;
		float[] verteices = {

				// ����
				-UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				-UNIT_SIZE * width,
				-UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				UNIT_SIZE * width,
				UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				-UNIT_SIZE * width,

				UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				-UNIT_SIZE * width,
				-UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				UNIT_SIZE * width,
				UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				UNIT_SIZE * width,

				// ����
				-UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				-UNIT_SIZE * width,
				-UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				-UNIT_SIZE * width,
				UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				-UNIT_SIZE * width,

				UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				-UNIT_SIZE * width,
				-UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				-UNIT_SIZE * width,
				UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				-UNIT_SIZE * width,

				// ǰ��
				-UNIT_SIZE * length, UNIT_HIGHT * scale, UNIT_SIZE * width,
				-UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				UNIT_SIZE * width,
				UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				UNIT_SIZE * width,

				UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				UNIT_SIZE * width,
				-UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				UNIT_SIZE * width,
				UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				UNIT_SIZE * width,

				// ����
				-UNIT_SIZE * length, -UNIT_HIGHT * scale, UNIT_SIZE * width,
				-UNIT_SIZE * length, -UNIT_HIGHT * scale, -UNIT_SIZE * width,
				UNIT_SIZE * length, -UNIT_HIGHT * scale, UNIT_SIZE * width,

				UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				UNIT_SIZE * width,
				-UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				-UNIT_SIZE * width,
				UNIT_SIZE * length,
				-UNIT_HIGHT * scale,
				-UNIT_SIZE * width,

				// ����
				-UNIT_SIZE * length, -UNIT_HIGHT * scale, -UNIT_SIZE * width,
				-UNIT_SIZE * length, -UNIT_HIGHT * scale, UNIT_SIZE * width,
				-UNIT_SIZE * length, UNIT_HIGHT * scale, -UNIT_SIZE * width,

				-UNIT_SIZE * length, UNIT_HIGHT * scale, -UNIT_SIZE * width,
				-UNIT_SIZE * length, -UNIT_HIGHT * scale,
				UNIT_SIZE * width,
				-UNIT_SIZE * length,
				UNIT_HIGHT * scale,
				UNIT_SIZE * width,

				// ����
				UNIT_SIZE * length, UNIT_HIGHT * scale, -UNIT_SIZE * width,
				UNIT_SIZE * length, UNIT_HIGHT * scale, UNIT_SIZE * width,
				UNIT_SIZE * length, -UNIT_HIGHT * scale, -UNIT_SIZE * width,

				UNIT_SIZE * length, -UNIT_HIGHT * scale, -UNIT_SIZE * width,
				UNIT_SIZE * length, UNIT_HIGHT * scale, UNIT_SIZE * width,
				UNIT_SIZE * length, -UNIT_HIGHT * scale, UNIT_SIZE * width

		};
		tempVerteices = verteices;
		ByteBuffer vbb = ByteBuffer.allocateDirect(verteices.length * 4); // ���������������ݻ���
		vbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mVertexBuffer = vbb.asFloatBuffer();// ת��Ϊfloat�ͻ���
		mVertexBuffer.put(verteices);// �򻺳����з��붥����������
		mVertexBuffer.position(0);// ���û�������ʼλ��
		float[] textureCoors = new float[vCount * 2];
		for (int i = 0; i < vCount / 6; i++)// ��������������
		{
			textureCoors[i * 12] = 0;
			textureCoors[(i * 12) + 1] = 0;

			textureCoors[(i * 12) + 2] = 0;
			textureCoors[(i * 12) + 3] = 1;

			textureCoors[(i * 12) + 4] = 1;
			textureCoors[(i * 12) + 5] = 0;

			textureCoors[(i * 12) + 6] = 1;
			textureCoors[(i * 12) + 7] = 0;

			textureCoors[(i * 12) + 8] = 0;
			textureCoors[(i * 12) + 9] = 1;

			textureCoors[(i * 12) + 10] = 1;
			textureCoors[(i * 12) + 11] = 1;

		}

		ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoors.length * 4);// ���������������ݻ���
		tbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mTextureBuffer = tbb.asFloatBuffer();// ת��Ϊfloat�ͻ���
		mTextureBuffer.put(textureCoors);// �򻺳����з��붥����������
		mTextureBuffer.position(0);// ���û�������ʼλ��
	}

	@Override
	public void drawSelf(GL10 gl) {
		gl.glRotatef(mAngleY, 0, 1, 0);
		gl.glTranslatef(xOffset, yOffset, zOffset);
		if (hiFlag) {
			// �Ŵ�����---x/y/z��̷Ŵ�
			gl.glScalef(BIGER_FACTER, BIGER_FACTER, BIGER_FACTER);
		}
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glEnable(GL10.GL_TEXTURE_2D);// ��������
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);// ����ʹ����������
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);// ָ����������
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);// ������
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);// ����
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);// �ر���������
		gl.glDisable(GL10.GL_TEXTURE_2D);// �ر�����
	}

	@Override
	public float[] findMid() {
		midX = (minX + maxX) / 2 + xOffset;
		midY = (minY + maxY) / 2 + yOffset;
		midZ = (minZ + maxZ) / 2 + zOffset;
		float[] mid = { midX, midY, midZ };
		return mid;
	}

	@Override
	public float[] findMinMax() {
		for (int i = 0; i < tempVerteices.length / 3; i++) {
			// �ж�X�����С�����λ��
			if (tempVerteices[i * 3] < minX) {
				minX = tempVerteices[i * 3];
			}
			if (tempVerteices[i * 3] > maxX) {
				maxX = tempVerteices[i * 3];
			}
			// �ж�Y�����С�����λ��
			if (tempVerteices[i * 3 + 1] < minY) {
				minY = tempVerteices[i * 3 + 1];
			}
			if (tempVerteices[i * 3 + 1] > maxY) {
				maxY = tempVerteices[i * 3 + 1];
			}
			// �ж�Z�����С�����λ��
			if (tempVerteices[i * 3 + 2] < minZ) {
				minZ = tempVerteices[i * 3 + 2];
			}
			if (tempVerteices[i * 3 + 2] > maxZ) {
				maxZ = tempVerteices[i * 3 + 2];
			}
		}
		float[] length = { maxX - minX, maxZ - minZ, maxY - minY };
		return length;
	}

	@Override
	public void setHilight(boolean flag) {
		this.hiFlag = flag;
	}
}
