package opengl.lance.demo_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class Helicoid {
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer texBuffer;

	private int vCount;
	private int texId;

	float xAngle;
	float yAngle;
	float zAngle;
	// �������ܹ����Ƶ�Ȧ��
	final static float MAX_ANGLE = 360f * 3;
	// ������Ƕȱ仯���---��ֵԽСԽ�ӽ�Բ---Խ��Խ�ӽ������
	final static float HEDICOID_ANGLE_SPAN = 10f;
	// ����뾶�仯���0.01f---���Ϊ��ֵ����ô������İ뾶��Խ��Խ��--���Ϊ0����ô������ɾ��ȵ���״ �������0�ͻ������״
	final static float HEDICOID_R_SPAN = 0f;
	// �����߶ȱ仯���---��ֵԽС������Խ��
	final static float LENGTH_SPAN = 0.3f;
	/*
	 * ����Բ�Ƕȱ仯���---�����180��--ֻ�ܻ�������(�����غ�)���õ��Ľ����Ǳ�ƽ��������
	 * ��ֵԽС����Խ�ӽ�Բ---Խ��Խ��������--ԽС�ڴ�����Խ��
	 */
	final static float CIRCLE_ANGLE_SPAN = 10f;
	// ������뾶�仯���0.2f---���Ϊ��ֵ����ô�������Խ��Խ��
	final static float CIRCLE_R_SPAN = 0f;
	// ����Բ��ʼ���ƽǶ�
	final static float CIECLE_ANGLE_BEGIN = 0f;
	// ����Բ�������ƽǶ�---�����ʼ�Ƕ�û��360�ȣ����潫����һ��Բ��
	final static float CIECLE_ANGLE_OVER = 360f;

	/**
	 * @param circleRadius
	 *            ---����Բ�뾶
	 * @param hradius
	 *            ---������뾶
	 * @param texId
	 *            ---����ID
	 */
	public Helicoid(float circleRadius, float hradius, int texId) {
		this.texId = texId;
		List<Float> lver = new ArrayList<Float>();
		List<Float> lnor = new ArrayList<Float>();
		/*
		 * hangle������仯�Ƕȡ�cr����Բ�뾶��hr������뾶 ������Ƕȱ仯С��360���Ի��Ƶ�Ȧ��������ѭ����length�����嵱ǰ�߶�
		 */
		for (float hangle = 0, cr = circleRadius, hr = hradius, length = 0; hangle < MAX_ANGLE; hangle += HEDICOID_ANGLE_SPAN, cr += CIRCLE_R_SPAN, hr += HEDICOID_R_SPAN, length += LENGTH_SPAN) {
			/*
			 * cangle����Բ�Ƕȱ仯�Ƕ�
			 */
			for (float cangle = CIECLE_ANGLE_BEGIN; cangle < CIECLE_ANGLE_OVER; cangle += CIRCLE_ANGLE_SPAN) {
				// �����һ����������
				float x1 = (float) ((hr + cr * Math.cos(Math.toRadians(cangle))) * Math
						.cos(Math.toRadians(hangle)));
				float y1 = (float) (cr * Math.sin(Math.toRadians(cangle)) + length);
				float z1 = (float) ((hr + cr * Math.cos(Math.toRadians(cangle))) * Math
						.sin(Math.toRadians(hangle)));

				float x2 = (float) ((hr + cr
						* Math.cos(Math.toRadians(cangle + CIRCLE_ANGLE_SPAN))) * Math
						.cos(Math.toRadians(hangle)));
				float y2 = (float) (cr
						* Math.sin(Math.toRadians(cangle + CIRCLE_ANGLE_SPAN)) + length);
				float z2 = (float) ((hr + cr
						* Math.cos(Math.toRadians(cangle + CIRCLE_ANGLE_SPAN))) * Math
						.sin(Math.toRadians(hangle)));

				float x3 = (float) (((hr + HEDICOID_R_SPAN) + (cr + CIRCLE_R_SPAN)
						* Math.cos(Math.toRadians(cangle + CIRCLE_ANGLE_SPAN))) * Math
						.cos(Math.toRadians(hangle + HEDICOID_ANGLE_SPAN)));
				float y3 = (float) ((cr + CIRCLE_R_SPAN)
						* Math.sin(Math.toRadians(cangle + CIRCLE_ANGLE_SPAN)) + (length + LENGTH_SPAN));
				float z3 = (float) (((hr + HEDICOID_R_SPAN) + (cr + CIRCLE_R_SPAN)
						* Math.cos(Math.toRadians(cangle + CIRCLE_ANGLE_SPAN))) * Math
						.sin(Math.toRadians(hangle + HEDICOID_ANGLE_SPAN)));

				float x4 = (float) (((hr + HEDICOID_R_SPAN) + (cr + CIRCLE_R_SPAN)
						* Math.cos(Math.toRadians(cangle))) * Math.cos(Math
						.toRadians(hangle + HEDICOID_ANGLE_SPAN)));
				float y4 = (float) ((cr + CIRCLE_R_SPAN)
						* Math.sin(Math.toRadians(cangle)) + (length + LENGTH_SPAN));
				float z4 = (float) (((hr + HEDICOID_R_SPAN) + (cr + CIRCLE_R_SPAN)
						* Math.cos(Math.toRadians(cangle))) * Math.sin(Math
						.toRadians(hangle + HEDICOID_ANGLE_SPAN)));

				lver.add(x1);
				lver.add(y1);
				lver.add(z1);
				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
				lver.add(x4);
				lver.add(y4);
				lver.add(z4);

				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
				lver.add(x3);
				lver.add(y3);
				lver.add(z3);
				lver.add(x4);
				lver.add(y4);
				lver.add(z4);

				// ����ƽ������������---�ȼ������ǰ�����ԭ������
				float a1 = (float) (x1 - (hr * Math.cos(Math.toRadians(hangle))));
				float b1 = y1 - length;
				float c1 = (float) (z1 - (hr * Math.sin(Math.toRadians(hangle))));
				float l1 = averNormal(a1, b1, c1);// ģ��
				a1 = a1 / l1;// ���������
				b1 = b1 / l1;
				c1 = c1 / l1;
				// �ȼ�����������Բ��Բ��---��������ȥԲ�ľ�������Բ�ķ�����
				float a2 = (float) (x2 - (hr * Math.cos(Math.toRadians(hangle))));
				float b2 = y1 - length;
				float c2 = (float) (z2 - (hr * Math.sin(Math.toRadians(hangle))));
				float l2 = averNormal(a2, b2, c2);// ģ��
				a2 = a2 / l2;// ���������
				b2 = b2 / l2;
				c2 = c2 / l2;

				float a3 = (float) (x3 - (hr * Math.cos(Math.toRadians(hangle
						+ HEDICOID_ANGLE_SPAN))));
				float b3 = y1 - (length + LENGTH_SPAN);
				float c3 = (float) (z3 - (hr * Math.sin(Math.toRadians(hangle
						+ HEDICOID_ANGLE_SPAN))));
				float l3 = averNormal(a3, b3, c3);// ģ��
				a3 = a3 / l3;// ���������
				b3 = b3 / l3;
				c3 = c3 / l3;

				float a4 = (float) (x4 - (hr * Math.cos(Math.toRadians(hangle
						+ HEDICOID_ANGLE_SPAN))));
				float b4 = y1 - (length + LENGTH_SPAN);
				float c4 = (float) (z4 - (hr * Math.sin(Math.toRadians(hangle
						+ HEDICOID_ANGLE_SPAN))));
				float l4 = averNormal(a4, b4, c4);// ģ��
				a4 = a4 / l4;// ���������
				b4 = b4 / l4;
				c4 = c4 / l4;

				lnor.add(a1);
				lnor.add(b1);
				lnor.add(c1);// �����Ӧ�ķ�����
				lnor.add(a2);
				lnor.add(b2);
				lnor.add(c2);
				lnor.add(a4);
				lnor.add(b4);
				lnor.add(c4);

				lnor.add(a2);
				lnor.add(b2);
				lnor.add(c2);
				lnor.add(a3);
				lnor.add(b3);
				lnor.add(c3);
				lnor.add(a4);
				lnor.add(b4);
				lnor.add(c4);
			}
		}
		vCount = lver.size() / 3;
		float[] vertexs = new float[vCount * 3];
		for (int i = 0; i < vCount * 3; i++) {
			vertexs[i] = lver.get(i);
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertexs.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertexs);
		vertexBuffer.position(0);

		// ������
		float[] normals = new float[vCount * 3];
		for (int i = 0; i < vCount * 3; i++) {
			normals[i] = lnor.get(i);
		}
		ByteBuffer ibb = ByteBuffer.allocateDirect(normals.length * 4);
		ibb.order(ByteOrder.nativeOrder());
		normalBuffer = ibb.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);

		// ����
		int col = (int) (MAX_ANGLE / HEDICOID_ANGLE_SPAN);
		int row = (int) ((CIECLE_ANGLE_OVER - CIECLE_ANGLE_BEGIN) / CIRCLE_ANGLE_SPAN);
		float[] textures = texCoor(row, col);

		ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(textures);
		texBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(xAngle, 1, 0, 0);
		gl.glRotatef(yAngle, 0, 1, 0);
		gl.glRotatef(zAngle, 0, 0, 1);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, texBuffer);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}

	private float[] texCoor(int bw, int bh) {
		float[] results = new float[bw * bh * 6 * 2];
		// ��������زĵ��и��
		float sizew = 1f / bw;
		float sizeh = 1f / bh;
		int c = 0;
		for (int i = 0; i < bw; i++) {
			for (int j = 0; j < bh; j++) {
				float s = j * sizew;
				float t = i * sizeh;
				results[c++] = s;
				results[c++] = t;

				results[c++] = s;
				results[c++] = t + sizeh;

				results[c++] = s + sizew;
				results[c++] = t;

				results[c++] = s;
				results[c++] = t + sizeh;

				results[c++] = s + sizew;
				results[c++] = t + sizeh;

				results[c++] = s + sizew;
				results[c++] = t;
			}
		}
		return results;
	}

	// ��������񻯣���ģ����
	private float averNormal(float x, float y, float z) {
		float pingfang = x * x + y * y + z * z;
		float length = (float) Math.sqrt(pingfang);
		return length;
	}
}
