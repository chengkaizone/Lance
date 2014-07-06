package opengl.lance.demo_11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import static opengl.lance.demo_11.Constant_3.*;

public class Land {
	private FloatBuffer vertexBuffer;
	private FloatBuffer texBuffer;
	private int vCount;
	private int texId;

	/**
	 * 初始化草地
	 * 
	 * @param texId
	 * @param mapArr
	 *            ---从灰度图中加载陆地上每个顶点的高度
	 * @param rows
	 * @param cols
	 */
	public Land(int texId, float[][] colorArray) {
		int rows = colorArray.length - 1;
		int cols = colorArray[0].length - 1;
		this.texId = texId;
		vCount = rows * cols * 3 * 2;
		float[] vertices = new float[vCount * 3];
		int c = 0;
		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < cols; i++) {
				// 计算当前格子左上侧点坐标
				float zsx = -UNIT_SIZE * cols / 2 + i * UNIT_SIZE;
				float zsz = -UNIT_SIZE * rows / 2 + j * UNIT_SIZE;

				vertices[c++] = zsx;// 矩形第一个点XYZ坐标
				vertices[c++] = yArray[j][i];
				vertices[c++] = zsz;

				vertices[c++] = zsx;// 矩形第二个点XYZ坐标
				vertices[c++] = yArray[j + 1][i];
				vertices[c++] = zsz + UNIT_SIZE;

				vertices[c++] = zsx + UNIT_SIZE;// 矩形第四个点XYZ坐标
				vertices[c++] = yArray[j][i + 1];
				vertices[c++] = zsz;

				vertices[c++] = zsx + UNIT_SIZE;// 矩形第四个点XYZ坐标
				vertices[c++] = yArray[j][i + 1];
				vertices[c++] = zsz;

				vertices[c++] = zsx;// 矩形第二个点XYZ坐标
				vertices[c++] = yArray[j + 1][i];
				vertices[c++] = zsz + UNIT_SIZE;

				vertices[c++] = zsx + UNIT_SIZE;// 矩形第三个点XYZ坐标
				vertices[c++] = yArray[j + 1][i + 1];
				vertices[c++] = zsz + UNIT_SIZE;
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

	// 自动切分纹理产生纹理数组的方法
	public float[] generateTexCoor(int bw, int bh) {
		float[] result = new float[bw * bh * 6 * 2];
		float sizew = 1.0f / bw;// 列数
		float sizeh = 1.0f / bh;// 行数
		// 纹理重复粘贴ST坐标范围
		int c = 0;
		for (int i = 0; i < bh; i++) {
			for (int j = 0; j < bw; j++) {
				// 每行列一个矩形，由两个三角形构成，共六个点，12个纹理坐标
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
