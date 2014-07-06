package opengl.lance.demo_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * 圆锥
 * 
 * @author Administrator
 * 
 */
public class Taper {
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer texBuffer;
	private int texId;
	private int vCount;
	float xAngle;
	float yAngle;
	float zAngle;

	// 圆锥高--半径--切分角度--所分段数--纹理id
	public Taper(float height, float radius, float degreeSpan, int col,
			int texId) {
		this.texId = texId;
		// 根据目标圆锥高度及所分的段数--计算出每段的高度
		float heightSpan = height / col;
		// 根据转角跨度计算需要分几份
		int numSpan = (int) (360 / degreeSpan);
		// 根据所分段数计算出半径跨度
		float radSpan = radius / col;// 半径跨度

		// 法向量点所在圆截面的高度?
		float heightNormal = (height * radius * radius)
				/ (height * height + radius * radius);
		// 法向量点所在圆截面的半径?
		float radNormal = (height * height * radius)
				/ (height * height + radius * radius);

		List<Float> lver = new ArrayList<Float>();
		List<Float> lnor = new ArrayList<Float>();
		// 计算顶点坐标和法向量坐标
		for (float degree = 360f; degree > 0f; degree -= degreeSpan) {
			for (int j = 0; j < col; j++) {
				// 计算当前分段的半径、当前坐标高度
				float curRad = j * radSpan;
				float curHeight = height - j * heightSpan;

				float x1 = (float) (curRad * Math.cos(Math.toRadians(degree)));
				float y1 = curHeight;
				float z1 = (float) (curRad * Math.sin(Math.toRadians(degree)));

				float a1 = (float) (radNormal * Math
						.cos(Math.toRadians(degree)));
				float b1 = heightNormal;
				float c1 = (float) (radNormal * Math
						.sin(Math.toRadians(degree)));
				// 计算法向量的模长
				float len1 = vectorlength(a1, b1, c1);
				// 计算出当前点的平均法向量
				a1 = a1 / len1;
				b1 = b1 / len1;
				c1 = c1 / len1;

				float x2 = (float) ((curRad + radSpan) * Math.cos(Math
						.toRadians(degree)));
				float y2 = curHeight - heightSpan;
				float z2 = (float) ((curRad + radSpan) * Math.sin(Math
						.toRadians(degree)));

				float a2 = (float) (radNormal * Math
						.cos(Math.toRadians(degree)));
				float b2 = heightNormal;
				float c2 = (float) (radNormal * Math
						.sin(Math.toRadians(degree)));
				float len2 = vectorlength(a2, b2, c2);
				a2 = a2 / len2;
				b2 = b2 / len2;
				c2 = c2 / len2;

				float x3 = (float) ((curRad + radSpan) * Math.cos(Math
						.toRadians(degree - degreeSpan)));
				float y3 = curHeight - heightSpan;
				float z3 = (float) ((curRad + radSpan) * Math.sin(Math
						.toRadians(degree - degreeSpan)));

				float a3 = (float) (radNormal * Math.cos(Math.toRadians(degree
						- degreeSpan)));
				float b3 = heightNormal;
				float c3 = (float) (radNormal * Math.sin(Math.toRadians(degree
						- degreeSpan)));
				float len3 = vectorlength(a3, b3, c3);
				a3 = a3 / len3;
				b3 = b3 / len3;
				c3 = c3 / len3;

				float x4 = (float) ((curRad) * Math.cos(Math.toRadians(degree
						- degreeSpan)));
				float y4 = curHeight;
				float z4 = (float) ((curRad) * Math.sin(Math.toRadians(degree
						- degreeSpan)));

				float a4 = (float) (radNormal * Math.cos(Math.toRadians(degree
						- degreeSpan)));
				float b4 = heightNormal;
				float c4 = (float) (radNormal * Math.sin(Math.toRadians(degree
						- degreeSpan)));
				float len4 = vectorlength(a4, b4, c4);
				a4 = a4 / len4;
				b4 = b4 / len4;
				c4 = c4 / len4;

				// 添加第一个三角形顶点坐标
				lver.add(x1);
				lver.add(y1);
				lver.add(z1);
				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
				lver.add(x4);
				lver.add(y4);
				lver.add(z4);
				// 添加第二个三角形顶点坐标
				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
				lver.add(x3);
				lver.add(y3);
				lver.add(z3);
				lver.add(x4);
				lver.add(y4);
				lver.add(z4);
				// 添加第一个三角形平均法向量坐标
				lnor.add(a1);
				lnor.add(b1);
				lnor.add(c1);
				lnor.add(a2);
				lnor.add(b2);
				lnor.add(c2);
				lnor.add(a4);
				lnor.add(b4);
				lnor.add(c4);
				// 添加第二个三角形法向量坐标
				lnor.add(a2);
				lnor.add(b2);
				lnor.add(c2);
				lnor.add(a3);
				lnor.add(b3);
				lnor.add(c3);
				lnor.add(a4);
				lnor.add(b4);
				lnor.add(c4);
			}
		}
		vCount = lver.size() / 3;
		// 顶点坐标
		float[] vertices = new float[lver.size()];
		for (int i = 0; i < lver.size(); i++) {
			vertices[i] = lver.get(i);
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		// 法向量坐标
		float[] normals = new float[lnor.size()];
		for (int i = 0; i < lnor.size(); i++) {
			normals[i] = lnor.get(i);
		}
		ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
		nbb.order(ByteOrder.nativeOrder());
		normalBuffer = nbb.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);
		// 创建纹理坐标
		float[] texs = genanalTexCoors(numSpan, col);
		ByteBuffer tbb = ByteBuffer.allocateDirect(texs.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texs);
		texBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(xAngle, 1, 0, 0);
		gl.glRotatef(yAngle, 0, 1, 0);
		gl.glRotatef(zAngle, 0, 0, 1);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);

		gl.glDisable(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}

	// 计算某法向量的平均法向量
	private float vectorlength(float x, float y, float z) {
		float temp = x * x + y * y + z * z;
		return (float) Math.sqrt(temp);
	}

	/**
	 * bw水平方向上所分的份数---bh垂直方向上所分的份数 一个4边形两个三角形有些顶点有两个纹理坐标
	 * 
	 * @param bw
	 * @param bh
	 * @return
	 */
	private float[] genanalTexCoors(int bw, int bh) {
		float[] result = new float[bw * bh * 6 * 2];
		// 纹理坐标0-1之间
		float sizew = 1f / bw;// 水平方向上纹理坐标跨度
		float sizeh = 1f / bh;// 垂直方向上纹理坐标跨度
		int c = 0;
		for (int i = 0; i < bw; i++) {
			for (int j = 0; j < bh; j++) {
				float s = i * sizew;
				float t = j * sizeh;
				// 第一个顶点的纹理坐标
				result[c++] = s;
				result[c++] = t;
				// 2
				result[c++] = s;
				result[c++] = t + sizeh;
				// 3
				result[c++] = s + sizew;
				result[c++] = t;
				// 4
				result[c++] = s;
				result[c++] = t + sizeh;
				// 5
				result[c++] = s + sizew;
				result[c++] = t + sizeh;
				// 6
				result[c++] = s + sizew;
				result[c++] = t;
			}
		}
		return result;
	}
}
