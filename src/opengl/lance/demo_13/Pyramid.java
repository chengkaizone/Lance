package opengl.lance.demo_13;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

//��ʾ����ͷ�����׶����
public class Pyramid {
	final float UNIT_SIZE = 0.5f;
	private FloatBuffer mVertexBuffer;// �����������ݻ���
	private FloatBuffer mTextureBuffer;// �����������ݻ���
	int vCount = 0;// ��������
	int texId;// ����ID

	public Pyramid(float scale, int texId) {
		this.texId = texId;

		// �����������ݵĳ�ʼ��================begin============================
		vCount = 18;// ÿ��������4���������棬12������ �ټӵ׾��ε�����������

		float vertices[] = new float[] { 0, 2.5f * scale * UNIT_SIZE, 0,
				UNIT_SIZE * scale, 0, UNIT_SIZE * scale, UNIT_SIZE * scale, 0,
				-UNIT_SIZE * scale,

				0, 2.5f * scale * UNIT_SIZE, 0, UNIT_SIZE * scale, 0,
				-UNIT_SIZE * scale, -UNIT_SIZE * scale, 0, -UNIT_SIZE * scale,

				0, 2.5f * scale * UNIT_SIZE, 0, -UNIT_SIZE * scale, 0,
				-UNIT_SIZE * scale, -UNIT_SIZE * scale, 0, UNIT_SIZE * scale,

				0, 2.5f * scale * UNIT_SIZE, 0, -UNIT_SIZE * scale, 0,
				UNIT_SIZE * scale, UNIT_SIZE * scale, 0, UNIT_SIZE * scale,

				UNIT_SIZE * scale, 0, -UNIT_SIZE * scale, -UNIT_SIZE * scale,
				0, UNIT_SIZE * scale, -UNIT_SIZE * scale, 0,
				-UNIT_SIZE * scale,

				UNIT_SIZE * scale, 0, UNIT_SIZE * scale, -UNIT_SIZE * scale, 0,
				UNIT_SIZE * scale, UNIT_SIZE * scale, 0, -UNIT_SIZE * scale, };

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
		float[] texST = { 0.5f, 0.0f, 0, 1, 1, 1, 0.5f, 0.0f, 0, 1, 1, 1, 0.5f,
				0.0f, 0, 1, 1, 1, 0.5f, 0.0f, 0, 1, 1, 1,

				1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0

		};
		ByteBuffer tbb = ByteBuffer.allocateDirect(texST.length * 4);
		tbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mTextureBuffer = tbb.asFloatBuffer();// ת��Ϊint�ͻ���
		mTextureBuffer.put(texST);// �򻺳����з��붥����ɫ����
		mTextureBuffer.position(0);// ���û�������ʼλ��
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
				0, // ��ʼ����
				vCount // ���������
		);
	}
}
