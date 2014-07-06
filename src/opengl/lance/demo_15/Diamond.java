package opengl.lance.demo_15;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Diamond {
	private FloatBuffer mVertexBuffer;// �����������ݻ���
	private FloatBuffer mColorBuffer;// ��ɫ���ݻ���
	public float yAngle = 0;
	public float xOffset;
	public float yOffset;
	public float zOffset;
	public int vCount = 0;

	public Diamond() {
		float UNIT_SIZE = 0.5f;
		vCount = 6;
		// ��������
		float vertices[] = new float[] { 0, 4 * UNIT_SIZE, 0, -4 * UNIT_SIZE,
				0, 0, 0, -4 * UNIT_SIZE, 0,

				0, -4 * UNIT_SIZE, 0, 4 * UNIT_SIZE, 0, 0, 0, 4 * UNIT_SIZE, 0

		};
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);

		float one = 65535;
		// ��ɫ����
		float colors[] = new float[] { one, one, one, 0, 0, one, one, 0, 0, 0,
				one, 0, one, one, one, 0, 0, 0, one, 0, one, 0, 0, 0 };
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put(colors);
		mColorBuffer.position(0);

	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);// ���ö�����������
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);// ���ö�����ɫ����

		gl.glRotatef(yAngle, 0, 1, 0);// ��y����ת
		gl.glTranslatef(0, yOffset, 0);
		gl.glTranslatef(xOffset, 0, 0);

		// Ϊ����ָ��������������
		gl.glVertexPointer(3, // ÿ���������������Ϊ3 xyz
				GL10.GL_FLOAT, // ��������ֵ������Ϊ GL_FIXED
				0, // ����������������֮��ļ��
				mVertexBuffer // ������������
		);

		// Ϊ����ָ��������ɫ����
		gl.glColorPointer(4, // ������ɫ����ɳɷ֣�����Ϊ4��RGBA
				GL10.GL_FLOAT, // ������ɫֵ������Ϊ GL_FIXED
				0, // ����������ɫ����֮��ļ��
				mColorBuffer // ������ɫ����
		);

		// ����ͼ��
		gl.glDrawArrays(GL10.GL_TRIANGLES, // �������η�ʽ���
				0, // ��ʼ����
				vCount // ���������
		);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}
}
