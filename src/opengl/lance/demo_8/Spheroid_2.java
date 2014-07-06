package opengl.lance.demo_8;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class Spheroid_2 {
	private IntBuffer vertexBuffer;
	private IntBuffer colorBuffer;
	private ByteBuffer indexBuffer;

	private int vCount;
	private int iCount;

	float moveX = 3.01f;

	public Spheroid_2(int scale) {
		final int UNIT_SIZE = 10000;
		int angleSpan = 18;
		// float a=2f;
		// float b=1.5f;
		// float c=1.5f;
		List<Integer> lint = new ArrayList<Integer>();
		// 以xoz为平面的笛卡尔坐标系---以经纬度切分
		for (int vangle = -90; vangle <= 90; vangle += angleSpan) {
			for (int hangle = 0; hangle < 360; hangle += angleSpan) {
				// 计算球面的顶点数组
				int x = (int) (UNIT_SIZE * scale
						* Math.cos(Math.toRadians(vangle)) * Math.cos(Math
						.toRadians(hangle)));
				int z = (int) (UNIT_SIZE * scale * Math.sin(Math
						.toRadians(vangle)));
				int y = (int) (UNIT_SIZE * scale
						* Math.cos(Math.toRadians(vangle)) * Math.sin(Math
						.toRadians(hangle)));
				lint.add(x);
				lint.add(y);
				lint.add(z);
			}
		}
		vCount = lint.size() / 3;
		int[] vertices = new int[lint.size()];
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = lint.get(i);
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asIntBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		final int ONE = 65535;
		int[] colors = new int[vCount * 4];
		for (int i = 0; i < vCount; i++) {
			colors[i * 4] = (int) (ONE * Math.random());
			colors[i * 4 + 1] = (int) (ONE * Math.random());
			colors[i * 4 + 2] = (int) (ONE * Math.random());
			colors[i * 4 + 3] = 0;
		}
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asIntBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);

		List<Integer> lind = new ArrayList<Integer>();
		int row = 180 / angleSpan;// 球面根据维度切分的行数
		int col = 360 / angleSpan;// 球面根据经度切分的列数
		for (int i = 0; i <= row; i++) {
			if (i > 0 && i < row) {
				for (int j = -1; j < col; j++) {
					int k = i * col + j;
					lind.add(k + col);
					lind.add(k + 1);
					lind.add(k);
				}
				for (int j = 0; j < col + 1; j++) {
					int k = i * col + j;
					lind.add(k - col);
					lind.add(k - 1);
					lind.add(k);
				}
			}
		}
		iCount = lind.size();
		byte[] indices = new byte[iCount];
		for (int i = 0; i < indices.length; i++) {
			indices[i] = lind.get(i).byteValue();
		}
		System.out.println(vCount + "<<<iCount>>>" + iCount);
		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glTranslatef(moveX, 0, 0);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, vertexBuffer);

		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, colorBuffer);

		gl.glDrawElements(GL10.GL_TRIANGLES, iCount, GL10.GL_UNSIGNED_BYTE,
				indexBuffer);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
