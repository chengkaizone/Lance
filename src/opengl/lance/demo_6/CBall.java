package opengl.lance.demo_6;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class CBall {
	private IntBuffer vertexBuffer;
	private IntBuffer normalBuffer;
	private FloatBuffer textureBuffer;
	float xAngle;
	float yAngle;
	float zAngle;
	private int vCount;
	private int textureId;

	public CBall(int scale, int textureId) {
		this.textureId = textureId;
		final int UNIT_SIZE = 10000;
		final float angleSpan = 11.25f;

		// ����ά���зֵķ���---���ݾ����зֵķ���
		float[] texCoor = generateTexCoor((int) (360 / angleSpan),
				(int) (180 / angleSpan));
		int tc = 0;// �������������
		int ts = texCoor.length;

		List<Integer> alVertix = new ArrayList<Integer>();// ���涥������
		List<Float> alTexture = new ArrayList<Float>();
		for (float vAngle = 90; vAngle > -90; vAngle -= angleSpan) {
			for (float hAngle = 360; hAngle > 0; hAngle -= angleSpan) {
				// ����������һ���ǶȺ�����Ӧ�Ĵ˵��������ϵ��ı��ζ�������
				// ��������������ı��ε�������
				double xozLength = scale * UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle));
				int x1 = (int) (xozLength * Math.cos(Math.toRadians(hAngle)));
				int z1 = (int) (xozLength * Math.sin(Math.toRadians(hAngle)));
				int y1 = (int) (scale * UNIT_SIZE * Math.sin(Math
						.toRadians(vAngle)));

				xozLength = scale * UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle - angleSpan));
				int x2 = (int) (xozLength * Math.cos(Math.toRadians(hAngle)));
				int z2 = (int) (xozLength * Math.sin(Math.toRadians(hAngle)));
				int y2 = (int) (scale * UNIT_SIZE * Math.sin(Math
						.toRadians(vAngle - angleSpan)));

				xozLength = scale * UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle - angleSpan));
				int x3 = (int) (xozLength * Math.cos(Math.toRadians(hAngle
						- angleSpan)));
				int z3 = (int) (xozLength * Math.sin(Math.toRadians(hAngle
						- angleSpan)));
				int y3 = (int) (scale * UNIT_SIZE * Math.sin(Math
						.toRadians(vAngle - angleSpan)));

				xozLength = scale * UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle));
				int x4 = (int) (xozLength * Math.cos(Math.toRadians(hAngle
						- angleSpan)));
				int z4 = (int) (xozLength * Math.sin(Math.toRadians(hAngle
						- angleSpan)));
				int y4 = (int) (scale * UNIT_SIZE * Math.sin(Math
						.toRadians(vAngle)));

				// ������һ��������
				alVertix.add(x1);
				alVertix.add(y1);
				alVertix.add(z1);
				alVertix.add(x2);
				alVertix.add(y2);
				alVertix.add(z2);
				alVertix.add(x4);
				alVertix.add(y4);
				alVertix.add(z4);
				// �ڶ���������
				alVertix.add(x4);
				alVertix.add(y4);
				alVertix.add(z4);
				alVertix.add(x2);
				alVertix.add(y2);
				alVertix.add(z2);
				alVertix.add(x3);
				alVertix.add(y3);
				alVertix.add(z3);
				// �������������ε���������--12����������
				alTexture.add(texCoor[tc++ % ts]);
				alTexture.add(texCoor[tc++ % ts]);
				alTexture.add(texCoor[tc++ % ts]);
				alTexture.add(texCoor[tc++ % ts]);
				alTexture.add(texCoor[tc++ % ts]);
				alTexture.add(texCoor[tc++ % ts]);

				alTexture.add(texCoor[tc++ % ts]);
				alTexture.add(texCoor[tc++ % ts]);
				alTexture.add(texCoor[tc++ % ts]);
				alTexture.add(texCoor[tc++ % ts]);
				alTexture.add(texCoor[tc++ % ts]);
				alTexture.add(texCoor[tc++ % ts]);

			}
		}
		vCount = alVertix.size() / 3;
		int[] vertices = new int[vCount * 3];
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = alVertix.get(i);
		}
		// ��ʼ�����㻺��
		ByteBuffer vbb = ByteBuffer.allocateDirect(vCount * 3 * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asIntBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		// ��ʼ������������
		ByteBuffer nbb = ByteBuffer.allocateDirect(vCount * 3 * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asIntBuffer();
		normalBuffer.put(vertices);
		normalBuffer.position(0);
		// ת����������
		float[] textureCoors = new float[alTexture.size()];
		for (int i = 0; i < alTexture.size(); i++) {
			textureCoors[i] = alTexture.get(i);
		}
		alTexture.clear();
		ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoors.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		textureBuffer = tbb.asFloatBuffer();
		textureBuffer.put(textureCoors);
		textureBuffer.position(0);

	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(zAngle, 0, 0, 1);
		gl.glRotatef(xAngle, 1, 0, 0);
		gl.glRotatef(yAngle, 0, 1, 0);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FIXED, 0, normalBuffer);

		gl.glEnable(GL10.GL_TEXTURE_2D);// ��������
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);
		// gl.glDisable(GL10.GL_TEXTURE_2D);//�ر�����
	}

	// ������������
	private float[] generateTexCoor(int bw, int bh) {
		float[] result = new float[bw * bh * 6 * 2];
		float sizew = 1f / bw;
		float sizeh = 1f / bh;
		int c = 0;
		for (int i = 0; i < bh; i++) {
			for (int j = 0; j < bw; j++) {
				// ÿһ��һ�����Σ���������������ɣ�6���㣻12����������
				float s = j * sizew;
				float t = i * sizeh;

				result[c++] = s;
				result[c++] = t;

				result[c++] = s;
				result[c++] = t + sizeh;

				result[c++] = s + sizew;
				result[c++] = t;

				result[c++] = s + sizew;
				result[c++] = t;

				result[c++] = s;
				result[c++] = t + sizeh;

				result[c++] = s + sizew;
				result[c++] = t + sizeh;
			}
		}
		return result;
	}
}
