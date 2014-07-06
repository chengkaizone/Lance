package opengl.lance.demo_9;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

public class MyCelestial {
	// 天球半径
	private final float UNIT_SIZE = 6.0f;
	private FloatBuffer vertexBuffer;
	private IntBuffer colorBuffer;

	private int vCount;
	float yAngle;
	int xOffset;
	int zOffset;
	float scale;

	public MyCelestial(int xOffset, int zOffset, float scale, float yAngle,
			int vCount) {
		this.xOffset = xOffset;
		this.zOffset = zOffset;
		this.scale = scale;
		this.vCount = vCount;
		this.yAngle = yAngle;
		float[] vertices = new float[vCount * 3];
		for (int i = 0; i < vCount; i++) {
			// 经度上0~360随机产生一个角度
			double jd = Math.PI * 2 * Math.random();
			// 维度0~90随机产生一个角度
			double wd = Math.PI / 2 * Math.random();
			vertices[i * 3] = (float) (UNIT_SIZE * Math.cos(wd) * Math.cos(jd));
			vertices[i * 3 + 1] = (float) (UNIT_SIZE * Math.sin(wd));
			vertices[i * 3 + 2] = (float) (UNIT_SIZE * Math.cos(wd) * Math
					.sin(jd));
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		final int ONE = 65535;
		int[] colors = new int[vCount * 4];
		for (int i = 0; i < vCount; i++) {
			colors[i] = ONE;
			colors[i + 1] = ONE;
			colors[i + 2] = ONE;
			colors[i + 3] = 0;
		}
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asIntBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		// 关闭光照
		gl.glDisable(GL10.GL_LIGHTING);
		// 设置星星尺寸
		gl.glPointSize(scale);
		// 获取坐标系
		gl.glPushMatrix();
		// 平移坐标系
		gl.glTranslatef(xOffset * UNIT_SIZE, 0, 0);
		gl.glTranslatef(0, 0, zOffset * UNIT_SIZE);
		gl.glRotatef(yAngle, 0, 1, 0);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, colorBuffer);

		gl.glDrawArrays(GL10.GL_POINTS, 0, vCount);

		gl.glPopMatrix();
		gl.glPointSize(1);
		gl.glEnable(GL10.GL_LIGHTING);
	}
}
