package opengl.lance.demo_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * 绘制平面圆
 * 
 * @author Administrator
 * 
 */
public class Circle {
	private FloatBuffer vertexBuffer;
	private FloatBuffer texBuffer;

	private int vCount;
	private int texId;

	float xAngle;
	float yAngle;
	float zAngle;

	float radius;

	public Circle(float radius, float angleSpan, int texId) {
		this.texId = texId;
		this.radius = radius;
		List<Float> lver = new ArrayList<Float>();
		List<Float> ltex = new ArrayList<Float>();
		for (float angle = 0f; angle < 360f; angle += angleSpan) {
			// 第一个点始终记为原点
			float x1 = 0f;
			float y1 = 0f;
			float z1 = 0f;
			float x2 = (float) (radius * Math.cos(Math.toRadians(angle)));
			float y2 = (float) (radius * Math.sin(Math.toRadians(angle)));
			float z2 = 0f;
			float x3 = (float) (radius * Math.cos(Math.toRadians(angle
					+ angleSpan)));
			float y3 = (float) (radius * Math.sin(Math.toRadians(angle
					+ angleSpan)));
			float z3 = 0f;

			lver.add(x1);
			lver.add(y1);
			lver.add(z1);
			lver.add(x2);
			lver.add(y2);
			lver.add(z2);
			lver.add(x3);
			lver.add(y3);
			lver.add(z3);

			// 根据纹理图的中心开始纹理图片根据角度切分纹理图素材---每一张纹理图都是有像素点的不能随便
			float s1 = (float) (0.5f * Math.cos(Math.toRadians(angle)));
			float t1 = (float) (0.5f * Math.sin(Math.toRadians(angle)));
			float s2 = (float) (0.5f * Math.cos(Math.toRadians(angle
					+ angleSpan)));
			float t2 = (float) (0.5f * Math.sin(Math.toRadians(angle
					+ angleSpan)));

			ltex.add(0.5f);
			ltex.add(0.5f);
			ltex.add(s1 + 0.5f);
			ltex.add(t1 + 0.5f);
			ltex.add(s2 + 0.5f);
			ltex.add(t2 + 0.5f);
		}
		float[] vertices = new float[lver.size()];
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = lver.get(i);
		}
		vCount = lver.size() / 3;
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		float[] texs = new float[ltex.size()];
		for (int i = 0; i < texs.length; i++) {
			texs[i] = ltex.get(i);
		}
		ByteBuffer tbb = ByteBuffer.allocateDirect(vertices.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texs);
		texBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(180f, 1, 0, 0);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);

		gl.glDisable(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
