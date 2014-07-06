package opengl.lance.demo_6;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class TBall {
	private IntBuffer vertexBuffer;
	private IntBuffer normalBuffer;
	private FloatBuffer textureBuffer;
	float xAngle;
	float yAngle;
	float zAngle;
	private int vCount;
	private int textureId;

	public TBall(int scale, int textureId) {
		this.textureId = textureId;
		final int UNIT_SIZE = 10000;
		List<Integer> vers = new ArrayList<Integer>();// 保存顶点数据
		final int angleSpan = 18;
		for (int vAngle = -90; vAngle <= 90; vAngle += angleSpan) {
			for (int hAngle = 0; hAngle < 360; hAngle += angleSpan) {
				double xozLength = scale * UNIT_SIZE
						* Math.cos(Math.toRadians(vAngle));
				int x = (int) (xozLength * Math.cos(Math.toRadians(hAngle)));
				int z = (int) (xozLength * Math.sin(Math.toRadians(hAngle)));
				int y = (int) (scale * UNIT_SIZE * Math.sin(Math
						.toRadians(vAngle)));
				vers.add(x);
				vers.add(y);
				vers.add(z);
			}
		}
		vCount = vers.size() / 3;
		int[] vertices = new int[vCount * 3];
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = vers.get(i);
		}
		vers.clear();

		List<Float> texs = new ArrayList<Float>();// 保存纹理数据
		int row = 180 / angleSpan;
		int col = 360 / angleSpan;
		for (int i = 0; i <= row; i++) {
			if (i > 0 && i < row) {
				for (int j = -1; j < col; j++) {
					int k = i * col + j;
					vers.add(vertices[(k + col) * 3]);
					vers.add(vertices[(k + col) * 3 + 1]);
					vers.add(vertices[(k + col) * 3 + 2]);
					texs.add(0.0f);// S
					texs.add(0.0f);// T
					vers.add(vertices[(k + 1) * 3]);
					vers.add(vertices[(k + 1) * 3 + 1]);
					vers.add(vertices[(k + 1) * 3 + 2]);
					texs.add(1.0f);
					texs.add(1.0f);
					vers.add(vertices[k * 3]);
					vers.add(vertices[k * 3 + 1]);
					vers.add(vertices[k * 3 + 2]);
					texs.add(1.0f);
					texs.add(0.0f);
				}
				for (int j = 0; j <= col; j++) {
					int k = i * col + j;
					vers.add(vertices[(k - col) * 3]);
					vers.add(vertices[(k - col) * 3 + 1]);
					vers.add(vertices[(k - col) * 3 + 2]);
					texs.add(1f);
					texs.add(1f);
					vers.add(vertices[(k - 1) * 3]);
					vers.add(vertices[(k - 1) * 3 + 1]);
					vers.add(vertices[(k - 1) * 3 + 2]);
					texs.add(0f);
					texs.add(0f);
					vers.add(vertices[k * 3]);
					vers.add(vertices[k * 3 + 1]);
					vers.add(vertices[k * 3 + 2]);
					texs.add(0f);
					texs.add(1f);
				}
			}
		}
		vCount = vers.size() / 3;
		vertices = new int[vCount * 3];
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = vers.get(i);
		}
		// 初始化顶点缓存
		ByteBuffer vbb = ByteBuffer.allocateDirect(vCount * 3 * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asIntBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		// 初始化法向量缓存
		ByteBuffer nbb = ByteBuffer.allocateDirect(vCount * 3 * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asIntBuffer();
		normalBuffer.put(vertices);
		normalBuffer.position(0);
		// 转化纹理数据
		float[] textureCoors = new float[texs.size()];
		for (int i = 0; i < texs.size(); i++) {
			textureCoors[i] = texs.get(i);
		}
		texs.clear();
		ByteBuffer cbb = ByteBuffer.allocateDirect(textureCoors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		textureBuffer = cbb.asFloatBuffer();
		textureBuffer.put(textureCoors);
		textureBuffer.position(0);

	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(xAngle, 1, 0, 0);
		gl.glRotatef(yAngle, 0, 1, 0);
		gl.glRotatef(zAngle, 0, 0, 1);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FIXED, 0, normalBuffer);
		gl.glEnable(GL10.GL_TEXTURE_2D);// 开启纹理
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		gl.glBindTexture(GL10.GL_ADD, textureId);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);
		gl.glDisable(GL10.GL_TEXTURE_2D);// 关闭纹理
	}
}
