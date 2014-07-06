package opengl.lance.demo_13;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 绘制粒子类---和粒子系统中的单个粒子概念不一样；该类只是起渲染作用
 * 
 * @author Administrator
 * 
 */
public class Particle {
	private FloatBuffer vertexBuffer;
	private IntBuffer colorBuffer;

	private float scale;

	public Particle(float scale, float r, float g, float b, float a) {
		this.scale = scale;
		float[] vertices = { 0f, 0f, 0f, };
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		final int ONE = 65535;
		int[] colors = { (int) r * ONE, (int) g * ONE, (int) b * ONE,
				(int) a * ONE, };
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asIntBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glPointSize(scale);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, colorBuffer);

		gl.glDrawArrays(GL10.GL_POINTS, 0, 1);
		gl.glPointSizex(1);// 恢复离子大小
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}
}
