package opengl.lance.demo_5;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class OtherBall_2 {
	private IntBuffer vertexBuffer;
	private IntBuffer normalBuffer;
	private ByteBuffer indexBuffer;
	float xAngle;
	float yAngle;
	float zAngle;
	private int vCount;
	private int iCount;

	public OtherBall_2(int scale) {
		final int UNIT_SIZE = 10000;
		List<Integer> alVertix = new ArrayList<Integer>();
		final int angleSpan = 18;
		// 垂直方向上分割
		for (int vAngle = -90; vAngle <= 90; vAngle = vAngle + angleSpan) {
			// 水平方向上分割
			for (int hAngle = 0; hAngle < 360; hAngle = hAngle + angleSpan) {
				double xozLength = scale * UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle));
				int x = (int) (xozLength * Math.cos(Math.toRadians(hAngle)));
				int z = (int) (xozLength * Math.sin(Math.toRadians(hAngle)));
				int y = (int) (scale * UNIT_SIZE * Math.sin(Math
						.toRadians(vAngle)));

				alVertix.add(x);
				alVertix.add(y);
				alVertix.add(z);
			}
		}
		vCount = alVertix.size() / 3;
		int[] vertices = new int[vCount * 3];
		for (int i = 0; i < alVertix.size(); i++) {
			vertices[i] = alVertix.get(i);
		}
		;
		// 创建顶点坐标数据
		ByteBuffer vbb = ByteBuffer.allocateDirect(vCount * 3 * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asIntBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		// 创建法向量坐标数据
		ByteBuffer nbb = ByteBuffer.allocateDirect(vCount * 3 * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asIntBuffer();
		normalBuffer.put(vertices);
		normalBuffer.position(0);

		List<Integer> alIndex = new ArrayList<Integer>();
		int row = 180 / angleSpan;// 10行
		int col = 360 / angleSpan;// 20列
		for (int i = 0; i <= row; i++) {// 对每一行进行迭代
			if (i > 0 && i <= row - 1) {// 1-9
				for (int j = -1; j < col; j++) {
					int k = i * col + j;
					alIndex.add(k + col);
					alIndex.add(k + 1);
					alIndex.add(k);
				}
				for (int j = 0; j < col + 1; j++) {
					int k = i * col + j;
					alIndex.add(k - col);
					alIndex.add(k - 1);
					alIndex.add(k);
				}
			}
		}
		iCount = alIndex.size();
		byte[] indexs = new byte[iCount];
		for (int i = 0; i < iCount; i++) {
			indexs[i] = alIndex.get(i).byteValue();
		}
		indexBuffer = ByteBuffer.allocateDirect(iCount);
		indexBuffer.put(indexs);
		indexBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(xAngle, 1, 0, 0);
		gl.glRotatef(yAngle, 0, 1, 0);
		gl.glRotatef(zAngle, 0, 0, 1);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, vertexBuffer);
		gl.glNormalPointer(GL10.GL_FIXED, 0, normalBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, iCount, GL10.GL_UNSIGNED_BYTE,
				indexBuffer);
	}
}
