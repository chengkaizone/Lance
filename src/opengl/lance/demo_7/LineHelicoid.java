package opengl.lance.demo_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class LineHelicoid {
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
	 */
	public LineHelicoid(float circleRadius, float hradius) {
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

				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
				lver.add(x4);
				lver.add(y4);
				lver.add(z4);

				lver.add(x4);
				lver.add(y4);
				lver.add(z4);
				lver.add(x1);
				lver.add(y1);
				lver.add(z1);

				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
				lver.add(x3);
				lver.add(y3);
				lver.add(z3);

				lver.add(x3);
				lver.add(y3);
				lver.add(z3);
				lver.add(x4);
				lver.add(y4);
				lver.add(z4);

				lver.add(x4);
				lver.add(y4);
				lver.add(z4);
				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
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
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(xAngle, 1, 0, 0);
		gl.glRotatef(yAngle, 0, 1, 0);
		gl.glRotatef(zAngle, 0, 0, 1);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glColor4f(100, 0, 0, 0);

		gl.glDrawArrays(GL10.GL_LINES, 0, vCount);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
