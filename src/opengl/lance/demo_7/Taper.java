package opengl.lance.demo_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Բ׶
 * 
 * @author Administrator
 * 
 */
public class Taper {
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer texBuffer;
	private int texId;
	private int vCount;
	float xAngle;
	float yAngle;
	float zAngle;

	// Բ׶��--�뾶--�зֽǶ�--���ֶ���--����id
	public Taper(float height, float radius, float degreeSpan, int col,
			int texId) {
		this.texId = texId;
		// ����Ŀ��Բ׶�߶ȼ����ֵĶ���--�����ÿ�εĸ߶�
		float heightSpan = height / col;
		// ����ת�ǿ�ȼ�����Ҫ�ּ���
		int numSpan = (int) (360 / degreeSpan);
		// �������ֶ���������뾶���
		float radSpan = radius / col;// �뾶���

		// ������������Բ����ĸ߶�?
		float heightNormal = (height * radius * radius)
				/ (height * height + radius * radius);
		// ������������Բ����İ뾶?
		float radNormal = (height * height * radius)
				/ (height * height + radius * radius);

		List<Float> lver = new ArrayList<Float>();
		List<Float> lnor = new ArrayList<Float>();
		// ���㶥������ͷ���������
		for (float degree = 360f; degree > 0f; degree -= degreeSpan) {
			for (int j = 0; j < col; j++) {
				// ���㵱ǰ�ֶεİ뾶����ǰ����߶�
				float curRad = j * radSpan;
				float curHeight = height - j * heightSpan;

				float x1 = (float) (curRad * Math.cos(Math.toRadians(degree)));
				float y1 = curHeight;
				float z1 = (float) (curRad * Math.sin(Math.toRadians(degree)));

				float a1 = (float) (radNormal * Math
						.cos(Math.toRadians(degree)));
				float b1 = heightNormal;
				float c1 = (float) (radNormal * Math
						.sin(Math.toRadians(degree)));
				// ���㷨������ģ��
				float len1 = vectorlength(a1, b1, c1);
				// �������ǰ���ƽ��������
				a1 = a1 / len1;
				b1 = b1 / len1;
				c1 = c1 / len1;

				float x2 = (float) ((curRad + radSpan) * Math.cos(Math
						.toRadians(degree)));
				float y2 = curHeight - heightSpan;
				float z2 = (float) ((curRad + radSpan) * Math.sin(Math
						.toRadians(degree)));

				float a2 = (float) (radNormal * Math
						.cos(Math.toRadians(degree)));
				float b2 = heightNormal;
				float c2 = (float) (radNormal * Math
						.sin(Math.toRadians(degree)));
				float len2 = vectorlength(a2, b2, c2);
				a2 = a2 / len2;
				b2 = b2 / len2;
				c2 = c2 / len2;

				float x3 = (float) ((curRad + radSpan) * Math.cos(Math
						.toRadians(degree - degreeSpan)));
				float y3 = curHeight - heightSpan;
				float z3 = (float) ((curRad + radSpan) * Math.sin(Math
						.toRadians(degree - degreeSpan)));

				float a3 = (float) (radNormal * Math.cos(Math.toRadians(degree
						- degreeSpan)));
				float b3 = heightNormal;
				float c3 = (float) (radNormal * Math.sin(Math.toRadians(degree
						- degreeSpan)));
				float len3 = vectorlength(a3, b3, c3);
				a3 = a3 / len3;
				b3 = b3 / len3;
				c3 = c3 / len3;

				float x4 = (float) ((curRad) * Math.cos(Math.toRadians(degree
						- degreeSpan)));
				float y4 = curHeight;
				float z4 = (float) ((curRad) * Math.sin(Math.toRadians(degree
						- degreeSpan)));

				float a4 = (float) (radNormal * Math.cos(Math.toRadians(degree
						- degreeSpan)));
				float b4 = heightNormal;
				float c4 = (float) (radNormal * Math.sin(Math.toRadians(degree
						- degreeSpan)));
				float len4 = vectorlength(a4, b4, c4);
				a4 = a4 / len4;
				b4 = b4 / len4;
				c4 = c4 / len4;

				// ��ӵ�һ�������ζ�������
				lver.add(x1);
				lver.add(y1);
				lver.add(z1);
				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
				lver.add(x4);
				lver.add(y4);
				lver.add(z4);
				// ��ӵڶ��������ζ�������
				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
				lver.add(x3);
				lver.add(y3);
				lver.add(z3);
				lver.add(x4);
				lver.add(y4);
				lver.add(z4);
				// ��ӵ�һ��������ƽ������������
				lnor.add(a1);
				lnor.add(b1);
				lnor.add(c1);
				lnor.add(a2);
				lnor.add(b2);
				lnor.add(c2);
				lnor.add(a4);
				lnor.add(b4);
				lnor.add(c4);
				// ��ӵڶ��������η���������
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
		// ��������
		float[] vertices = new float[lver.size()];
		for (int i = 0; i < lver.size(); i++) {
			vertices[i] = lver.get(i);
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		// ����������
		float[] normals = new float[lnor.size()];
		for (int i = 0; i < lnor.size(); i++) {
			normals[i] = lnor.get(i);
		}
		ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);
		// ������������
		float[] texs = genanalTexCoors(numSpan, col);
		ByteBuffer tbb = ByteBuffer.allocateDirect(texs.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texs);
		texBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(xAngle, 1, 0, 0);
		gl.glRotatef(yAngle, 0, 1, 0);
		gl.glRotatef(zAngle, 0, 0, 1);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);

		gl.glDisable(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}

	// ����ĳ��������ƽ��������
	private float vectorlength(float x, float y, float z) {
		float temp = x * x + y * y + z * z;
		return (float) Math.sqrt(temp);
	}

	/**
	 * bwˮƽ���������ֵķ���---bh��ֱ���������ֵķ��� һ��4����������������Щ������������������
	 * 
	 * @param bw
	 * @param bh
	 * @return
	 */
	private float[] genanalTexCoors(int bw, int bh) {
		float[] result = new float[bw * bh * 6 * 2];
		// ��������0-1֮��
		float sizew = 1f / bw;// ˮƽ����������������
		float sizeh = 1f / bh;// ��ֱ����������������
		int c = 0;
		for (int i = 0; i < bw; i++) {
			for (int j = 0; j < bh; j++) {
				float s = i * sizew;
				float t = j * sizeh;
				// ��һ���������������
				result[c++] = s;
				result[c++] = t;
				// 2
				result[c++] = s;
				result[c++] = t + sizeh;
				// 3
				result[c++] = s + sizew;
				result[c++] = t;
				// 4
				result[c++] = s;
				result[c++] = t + sizeh;
				// 5
				result[c++] = s + sizew;
				result[c++] = t + sizeh;
				// 6
				result[c++] = s + sizew;
				result[c++] = t;
			}
		}
		return result;
	}
}
