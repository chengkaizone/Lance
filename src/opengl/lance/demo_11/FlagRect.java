package opengl.lance.demo_11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 立体图形的顶点数量需要计算---矩形的顶点可以是固定的
 * 
 * @author Administrator
 * 
 */
public class FlagRect {
	private FloatBuffer vertexBuffer;
	private FloatBuffer texBuffer;
	private int vCount;
	private int texId;

	/**
	 * 纹理ID
	 * 
	 * @param texId
	 * @param startAngle起始角度
	 */
	public FlagRect(int texId, double startAngle) {
		this.texId = texId;
		final int cols = 20;
		final int rows = cols * 3 / 4;
		// 每格的单位长度
		final float UNIT_SIZE = 1.5f / cols;
		// 计算每一格的单位弧度---一定要注意此处使用的是弧度--不需要转化
		final double angleSpan = Math.PI * 4 / cols;
		// 计算顶点总数---矩形=2个三角形--6个顶点
		vCount = cols * rows * 2 * 3;
		int c = 0;
		float[] vertices = new float[vCount * 3];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				float x = -UNIT_SIZE * cols / 2 + j * UNIT_SIZE;
				float y = UNIT_SIZE * rows / 2 - i * UNIT_SIZE;
				float z1 = (float) Math.sin(startAngle + j * angleSpan) * 0.1f;
				float z2 = (float) Math.sin(startAngle + (j + 1) * angleSpan) * 0.1f;
				vertices[c++] = x;
				vertices[c++] = y;
				vertices[c++] = z1;

				vertices[c++] = x;
				vertices[c++] = y - UNIT_SIZE;
				vertices[c++] = z1;

				vertices[c++] = x + UNIT_SIZE;
				vertices[c++] = y;
				vertices[c++] = z2;

				vertices[c++] = x;
				vertices[c++] = y - UNIT_SIZE;
				vertices[c++] = z1;

				vertices[c++] = x + UNIT_SIZE;
				vertices[c++] = y - UNIT_SIZE;
				vertices[c++] = z2;

				vertices[c++] = x + UNIT_SIZE;
				vertices[c++] = y;
				vertices[c++] = z2;
			}
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		float textures[] = generateTexCoor(cols, rows);
		// 创建顶点纹理数据缓冲
		ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(textures);
		texBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);// 启用顶点坐标数组
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

	// 自动切分纹理产生纹理数组的方法bw水平方向上的分数，bh---垂直方向上的数量---注意切分次序
	public float[] generateTexCoor(int bw, int bh) {
		float[] result = new float[bw * bh * 6 * 2];
		float sizew = 1.0f / bw;// 列数
		float sizeh = 0.75f / bh;// 垂直放i型那个上的跨度
		int c = 0;
		// 切分纹理是注意循环次序
		for (int i = 0; i < bh; i++) {
			for (int j = 0; j < bw; j++) {
				float s = j * sizew;
				float t = i * sizeh;
				result[c++] = s;
				result[c++] = t;
				result[c++] = s;
				result[c++] = t + sizeh;
				result[c++] = s + sizew;
				result[c++] = t;

				result[c++] = s;
				result[c++] = t + sizeh;
				result[c++] = s + sizew;
				result[c++] = t + sizeh;
				result[c++] = s + sizew;
				result[c++] = t;
			}
		}
		return result;
	}
}
