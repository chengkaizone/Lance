package opengl.lance.demo_6;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * �����ǿ�
 * 
 * @author Administrator
 * 
 */
public class MyCelestial {
	private float UNIT_SIZE = 20f;
	private FloatBuffer vertexBuffer;
	private IntBuffer colorBuffer;
	private float xOffset;
	private float zOffset;
	float yAngle;
	private int scale;// ���ǵĳߴ�
	private int vCount;

	public MyCelestial(float x, float y, float z, int scale, int num) {
		this.xOffset = x;
		this.zOffset = z;
		this.yAngle = y;
		this.scale = scale;
		this.vCount = num;
		float[] vertices = new float[vCount * 3];
		for (int i = 0; i < vCount; i++) {
			double jd = Math.PI * 2 * Math.random();
			double wd = Math.PI / 2 * Math.random();
			vertices[i * 3] = (float) (UNIT_SIZE * Math.cos(wd) * Math.sin(jd));
			vertices[i * 3 + 1] = (float) (UNIT_SIZE * Math.sin(wd));
			vertices[i * 3 + 2] = (float) (UNIT_SIZE * Math.cos(wd) * Math
					.cos(jd));
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		final int one = 65535;
		int[] colors = new int[vCount * 4];
		for (int i = 0; i < vCount; i++) {
			colors[i * 4] = one;
			colors[i * 4 + 1] = one;
			colors[i * 4 + 2] = one;
			colors[i * 4 + 3] = 0;
		}
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asIntBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glDisable(GL10.GL_LIGHTING);// �رչ���
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		// �������ǳߴ�
		gl.glPointSize(scale);
		// ��ȡ��λ����
		gl.glPushMatrix();
		// ����ƽ��
		gl.glTranslatef(xOffset * UNIT_SIZE, 0, 0);
		gl.glTranslatef(0, 0, zOffset * UNIT_SIZE);
		// ת��ת��
		gl.glRotatef(yAngle, 0, 1, 0);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, colorBuffer);
		// ����
		gl.glDrawArrays(GL10.GL_POINTS, 0, vCount);
		// �ָ�����
		gl.glPopMatrix();
		// ��ԭ����ֵ
		gl.glPointSize(1);
		gl.glEnable(GL10.GL_LIGHTING);// ��������
	}
}
