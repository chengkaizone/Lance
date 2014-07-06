package opengl.lance.demo_13;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

//���ڻ��Ƶ������ӵ���---���Ƶ���һ������
public class ParticleForDraw {
	private FloatBuffer mVertexBuffer;// �����������ݻ���
	private FloatBuffer mTextureBuffer;// �����������ݻ���
	float scale;// ���ӳߴ�
	int texId;// ����Id

	public ParticleForDraw(int texId, float scale) {
		this.texId = texId;
		this.scale = scale;

		// �����������ݵĳ�ʼ��================begin=======================================
		float vertices[] = new float[]// ��������ֵ����
		{ 0, 0, 0, scale, scale, 0, -scale, scale, 0, -scale, -scale, 0, scale,
				-scale, 0, scale, scale, 0, };

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

		// �����������ݳ�ʼ��
		float[] texST = { 0.5f, 0.5f, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0 };
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

		// ����
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, // �������η�ʽ���
				0, // ��ʼ����
				6 // ���������
		);
	}
}
