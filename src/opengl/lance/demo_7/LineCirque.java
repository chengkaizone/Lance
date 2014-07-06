package opengl.lance.demo_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class LineCirque {
	private FloatBuffer vertexBuffer;

	private int vCount;
	float xAngle;
	float yAngle;
	float zAngle;

	public LineCirque(float ringSpan, float circleSpan, float ringRadius,
			float circleRadius) {
		List<Float> lver = new ArrayList<Float>();
		// cdegree½Ø»·°ë¾¶
		for (float cdegree = 0f; cdegree < 360f; cdegree += circleSpan) {
			// Ô²»·°ë¾¶
			for (float rdegree = -180f; rdegree < 180f; rdegree += ringSpan) {
				float x1 = (float) ((ringRadius + circleRadius
						* Math.cos(Math.toRadians(cdegree))) * Math.cos(Math
						.toRadians(rdegree)));

				float y1 = (float) (circleRadius * Math.sin(Math
						.toRadians(cdegree)));
				float z1 = (float) ((ringRadius + circleRadius
						* Math.cos(Math.toRadians(cdegree))) * Math.sin(Math
						.toRadians(rdegree)));

				float x2 = (float) ((ringRadius + circleRadius
						* Math.cos(Math.toRadians(cdegree))) * Math.cos(Math
						.toRadians(rdegree + ringSpan)));
				float y2 = (float) (circleRadius * Math.sin(Math
						.toRadians(cdegree)));
				float z2 = (float) ((ringRadius + circleRadius
						* Math.cos(Math.toRadians(cdegree))) * Math.sin(Math
						.toRadians(rdegree + ringSpan)));

				float x3 = (float) ((ringRadius + circleRadius
						* Math.cos(Math.toRadians(cdegree + circleSpan))) * Math
						.cos(Math.toRadians(rdegree + ringSpan)));
				float y3 = (float) (circleRadius * Math.sin(Math
						.toRadians(cdegree + circleSpan)));
				float z3 = (float) ((ringRadius + circleRadius
						* Math.cos(Math.toRadians(cdegree + circleSpan))) * Math
						.sin(Math.toRadians(rdegree + ringSpan)));

				float x4 = (float) ((ringRadius + circleRadius
						* Math.cos(Math.toRadians(cdegree + circleSpan))) * Math
						.cos(Math.toRadians(rdegree)));
				float y4 = (float) (circleRadius * Math.sin(Math
						.toRadians(cdegree + circleSpan)));
				float z4 = (float) ((ringRadius + circleRadius
						* Math.cos(Math.toRadians(cdegree + circleSpan))) * Math
						.sin(Math.toRadians(rdegree)));

				lver.add(x1);
				lver.add(y1);
				lver.add(z1);
				lver.add(x2);
				lver.add(y2);
				lver.add(z2);

				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
				lver.add(x4);
				lver.add(y4);
				lver.add(z4);
				lver.add(x4);

				lver.add(y4);
				lver.add(z4);
				lver.add(x1);
				lver.add(y1);
				lver.add(z1);

				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
				lver.add(x3);
				lver.add(y3);
				lver.add(z3);

				lver.add(x3);
				lver.add(y3);
				lver.add(z3);
				lver.add(x4);
				lver.add(y4);
				lver.add(z4);

				lver.add(x4);
				lver.add(y4);
				lver.add(z4);
				lver.add(x2);
				lver.add(y2);
				lver.add(z2);
			}
		}
		float[] vertices = new float[lver.size()];
		for (int i = 0; i < lver.size(); i++) {
			vertices[i] = lver.get(i);
		}
		vCount = vertices.length / 3;
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(xAngle, 1, 0, 0);
		gl.glRotatef(yAngle, 0, 1, 0);
		gl.glRotatef(zAngle, 0, 0, 1);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColor4f(100, 0, 0, 0);
		gl.glDrawArrays(GL10.GL_LINES, 0, vCount);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
