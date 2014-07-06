package opengl.lance.demo_15;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

public class BallForDraw {
	private FloatBuffer myVertex;
	private FloatBuffer myTexture;

	int vcount;// ��������
	int textureID;
	float xOffset;
	float zOffset;

	public BallForDraw(float xOffset, float zOffset, float angleSpan,
			float scale, int textureID) {
		this.textureID = textureID;
		this.xOffset = xOffset;
		this.zOffset = zOffset;
		// ����
		ArrayList<Float> val = new ArrayList<Float>();
		for (float i = 90; i > -90; i -= angleSpan) {
			for (float j = 360; j > 0; j -= angleSpan) {
				float x1 = (float) (scale * Math.cos(Math.toRadians(i)) * Math
						.cos(Math.toRadians(j)));
				float z1 = (float) (scale * Math.sin(Math.toRadians(i)));
				float y1 = (float) (scale * Math.cos(Math.toRadians(i)) * Math
						.sin(Math.toRadians(j)));

				float x2 = (float) (scale
						* Math.cos(Math.toRadians(i - angleSpan)) * Math
						.cos(Math.toRadians(j)));
				float z2 = (float) (scale * Math.sin(Math.toRadians(i
						- angleSpan)));
				float y2 = (float) (scale
						* Math.cos(Math.toRadians(i - angleSpan)) * Math
						.sin(Math.toRadians(j)));

				float x3 = (float) (scale
						* Math.cos(Math.toRadians(i - angleSpan)) * Math
						.cos(Math.toRadians(j - angleSpan)));
				float z3 = (float) (scale * Math.sin(Math.toRadians(i
						- angleSpan)));
				float y3 = (float) (scale
						* Math.cos(Math.toRadians(i - angleSpan)) * Math
						.sin(Math.toRadians(j - angleSpan)));

				float x4 = (float) (scale * Math.cos(Math.toRadians(i)) * Math
						.cos(Math.toRadians(j - angleSpan)));
				float z4 = (float) (scale * Math.sin(Math.toRadians(i)));
				float y4 = (float) (scale * Math.cos(Math.toRadians(i)) * Math
						.sin(Math.toRadians(j - angleSpan)));

				val.add(x1);
				val.add(y1);
				val.add(z1);
				val.add(x2);
				val.add(y2);
				val.add(z2);
				val.add(x4);
				val.add(y4);
				val.add(z4);

				val.add(x4);
				val.add(y4);
				val.add(z4);
				val.add(x2);
				val.add(y2);
				val.add(z2);
				val.add(x3);
				val.add(y3);
				val.add(z3);
			}
		}
		vcount = val.size() / 3;
		float[] ver = new float[vcount * 3];
		for (int i = 0; i < vcount * 3; i++) {
			ver[i] = val.get(i);
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(ver.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		myVertex = vbb.asFloatBuffer();
		myVertex.put(ver);
		myVertex.position(0);

		// ����
		ArrayList<Float> tal = new ArrayList<Float>();
		int trow = (int) (180 / angleSpan);// �������
		int tcol = (int) (360 / angleSpan);// �������
		float rowUnitSize = (float) 1.0f / trow;// ����鳤��
		float colUnitSize = (float) 1.0f / tcol;// ����鳤��
		for (int i = 0; i < trow; i++) {
			for (int j = 0; j < tcol; j++) {
				// ��һ��������
				tal.add(j * colUnitSize);
				tal.add(i * rowUnitSize);

				tal.add(j * colUnitSize);
				tal.add(i * rowUnitSize + rowUnitSize);

				tal.add(j * colUnitSize + colUnitSize);
				tal.add(i * rowUnitSize);

				// �ڶ���������
				tal.add(j * colUnitSize + colUnitSize);
				tal.add(i * rowUnitSize);

				tal.add(j * colUnitSize);
				tal.add(i * rowUnitSize + rowUnitSize);

				tal.add(j * colUnitSize + colUnitSize);
				tal.add(i * rowUnitSize + rowUnitSize);
			}
		}
		float[] textures = new float[trow * tcol * 6 * 2];
		for (int i = 0; i < textures.length; i++) {
			textures[i] = tal.get(i);
		}

		ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
		tbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		myTexture = tbb.asFloatBuffer();// ת��Ϊint�ͻ���
		myTexture.put(textures);// �򻺳����з��붥����ɫ����
		myTexture.position(0);// ���û�������ʼλ��
	}

	public void drawSelf(GL10 gl) {

		// ����
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, myVertex);

		// ����
		// ��������
		gl.glEnable(GL10.GL_TEXTURE_2D);
		// ����ʹ������ST���껺��
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// Ϊ����ָ������ST���껺��
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, myTexture);
		// �󶨵�ǰ����
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);

		// ��ͼ
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vcount);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
}
