package opengl.lance.demo_11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import static opengl.lance.demo_11.Constant_3.*;

public class Water {
	private FloatBuffer vertexBuffer;
	private FloatBuffer texBuffer;

	private int vCount;
	private int texId;

	/**
	 * @param texId纹理贴图id
	 * @param startAngle起始角度
	 * @param rows水平切分的份数
	 * @param cols垂直切分的份数
	 * @param multiply倍数
	 */
	public Water(int texId, double startAngle, int rows, int cols, int multiply) {
		this.texId = texId;
		// 计算顶点数
		vCount = rows * cols * 2 * 3;
		// 每个顶点三个数据
		float[] vertices = new float[vCount * 3];
		// 转角跨度
		double angleSpan = Math.PI * 4 / cols;
		int c = 0;
		final float LOCAL_UNIT_SIZE = UNIT_SIZE * multiply;
		// 循环行
		for (int i = 0; i < rows; i++) {
			// 循环列---列数
			for (int j = 0; j < cols; j++) {
				// 计算当前循环的顶点坐标
				float x = -LOCAL_UNIT_SIZE * cols / 2 + j * LOCAL_UNIT_SIZE;
				float z = -LOCAL_UNIT_SIZE * cols / 2 + i * LOCAL_UNIT_SIZE;

				float y1 = (float) Math.sin(startAngle + j * angleSpan) * 0.4f
						- WATER_HIGH_ADJUST;
				float y2 = (float) Math.sin(startAngle + (j + 1) * angleSpan)
						* 0.4f - WATER_HIGH_ADJUST;

				vertices[c++] = x;
				vertices[c++] = y1;
				vertices[c++] = z;

				vertices[c++] = x;
				vertices[c++] = y1;
				vertices[c++] = z + LOCAL_UNIT_SIZE;

				vertices[c++] = x + LOCAL_UNIT_SIZE;
				vertices[c++] = y2;
				vertices[c++] = z;

				vertices[c++] = x;
				vertices[c++] = y1;
				vertices[c++] = z + LOCAL_UNIT_SIZE;

				vertices[c++] = x + LOCAL_UNIT_SIZE;
				vertices[c++] = y2;
				vertices[c++] = z + LOCAL_UNIT_SIZE;

				vertices[c++] = x + LOCAL_UNIT_SIZE;
				vertices[c++] = y2;
				vertices[c++] = z;
			}
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		float[] texs = texCoors(rows, cols);
		ByteBuffer tbb = ByteBuffer.allocateDirect(texs.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texs);
		texBuffer.position(0);

	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// 为画笔指定顶点坐标数据
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		// 开启纹理
		gl.glEnable(GL10.GL_TEXTURE_2D);
		// 允许使用纹理ST坐标缓冲
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// 为画笔指定纹理ST坐标缓冲
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		// 绑定当前纹理
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
		// 绘制图形
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);

		// 关闭纹理
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	private float[] texCoors(int bw, int bh) {
		float[] results = new float[bw * bh * 6 * 2];
		float sizew = 1f / bw;
		float sizeh = 0.75f / bh;
		int c = 0;
		for (int i = 0; i < bh; i++) {
			// 在切分纹理图片时先水平切割；所以水平切割
			for (int j = 0; j < bw; j++) {
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
}
