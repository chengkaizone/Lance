package opengl.lance.demo_15;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class Taper {
	private FloatBuffer myVertexBuffer;// �������껺��
	private FloatBuffer myTexture;// ������

	int textureId;
	int vCount;// ��������

	float height;// Բ������
	float circle_radius;// Բ׶����뾶
	float degreespan; // Բ�ػ�ÿһ�ݵĶ�����С
	int col;// Բ׶ƽ���зֵĿ���

	public float mAngleX;
	public float mAngleY;
	public float mAngleZ;

	public Taper(float height, float circle_radius, float degreespan, int col,
			int textureId) {
		this.height = height;
		this.circle_radius = circle_radius;
		this.degreespan = degreespan; // ��ʾ���ٶ��и�һ��
		this.col = col; // ��ʾheight�ּ����и�
		this.textureId = textureId;

		float spanHeight = (float) height / col;// Բ׶ÿ����ռ�ĸ߶�
		int spannum = (int) (360.0f / degreespan);
		float spanR = circle_radius / col;// �뾶��λ����

		ArrayList<Float> val = new ArrayList<Float>();// �������б�

		for (float circle_degree = 360.0f; circle_degree > 0.0f; circle_degree -= degreespan)// ѭ����
		{
			for (int j = 0; j < col; j++)// ѭ����
			{
				float currentR = j * spanR;// ��ǰ�����Բ�뾶
				float currentHeight = height - j * spanHeight;// ��ǰ����ĸ߶�

				float x1 = (float) (currentR * Math.cos(Math
						.toRadians(circle_degree)));
				float y1 = currentHeight;
				float z1 = (float) (currentR * Math.sin(Math
						.toRadians(circle_degree)));

				float x2 = (float) ((currentR + spanR) * Math.cos(Math
						.toRadians(circle_degree)));
				float y2 = currentHeight - spanHeight;
				float z2 = (float) ((currentR + spanR) * Math.sin(Math
						.toRadians(circle_degree)));

				float x3 = (float) ((currentR + spanR) * Math.cos(Math
						.toRadians(circle_degree - degreespan)));
				float y3 = currentHeight - spanHeight;
				float z3 = (float) ((currentR + spanR) * Math.sin(Math
						.toRadians(circle_degree - degreespan)));

				float x4 = (float) ((currentR) * Math.cos(Math
						.toRadians(circle_degree - degreespan)));
				float y4 = currentHeight;
				float z4 = (float) ((currentR) * Math.sin(Math
						.toRadians(circle_degree - degreespan)));

				val.add(x1);
				val.add(y1);
				val.add(z1);// ���������Σ���6�����������
				val.add(x2);
				val.add(y2);
				val.add(z2);
				val.add(x4);
				val.add(y4);
				val.add(z4);

				val.add(x2);
				val.add(y2);
				val.add(z2);
				val.add(x3);
				val.add(y3);
				val.add(z3);
				val.add(x4);
				val.add(y4);
				val.add(z4);

			}
		}

		vCount = val.size() / 3;// ȷ����������

		// ����
		float[] vertexs = new float[vCount * 3];
		for (int i = 0; i < vCount * 3; i++) {
			vertexs[i] = val.get(i);
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertexs.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		myVertexBuffer = vbb.asFloatBuffer();
		myVertexBuffer.put(vertexs);
		myVertexBuffer.position(0);

		// ����
		float[] textures = generateTexCoor(spannum, col);
		ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		myTexture = tbb.asFloatBuffer();
		myTexture.put(textures);
		myTexture.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(mAngleX, 1, 0, 0);// ��ת
		gl.glRotatef(mAngleY, 0, 1, 0);
		gl.glRotatef(mAngleZ, 0, 0, 1);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);// �򿪶��㻺��
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, myVertexBuffer);// ָ�����㻺��

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, myTexture);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);// ����ͼ��

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);// �رջ���
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

	}

	// �Զ��з����������������ķ���
	public float[] generateTexCoor(int bw, int bh) {
		float[] result = new float[bw * bh * 6 * 2];
		float sizew = 1.0f / bw;// �е�λ����
		float sizeh = 1.0f / bh;// �е�λ����
		int c = 0;
		for (int j = 0; j < bw; j++) {
			for (int i = 0; i < bh; i++) {
				// ÿ����һ�����Σ������������ι��ɣ��������㣬12����������
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
