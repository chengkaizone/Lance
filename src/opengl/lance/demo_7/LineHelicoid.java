package opengl.lance.demo_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class LineHelicoid {
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer texBuffer;

	private int vCount;
	private int texId;

	float xAngle;
	float yAngle;
	float zAngle;
	// 螺旋体总共所绕的圈数
	final static float MAX_ANGLE = 360f * 3;
	// 螺旋体角度变化跨度---数值越小越接近圆---越大越接近多边形
	final static float HEDICOID_ANGLE_SPAN = 10f;
	// 截面半径变化跨度0.01f---如果为正值；那么螺旋体的半径会越变越大--如果为0；那么螺旋体成均匀的柱状 如果大于0就会成蚊香状
	final static float HEDICOID_R_SPAN = 0f;
	// 螺旋高度变化跨度---数值越小螺旋体越密
	final static float LENGTH_SPAN = 0.3f;
	/*
	 * 截面圆角度变化跨度---如果是180度--只能绘制两次(正反重合)；得到的将会是扁平的螺旋体
	 * 数值越小截面越接近圆---越大越像个多边形--越小内存消耗越大
	 */
	final static float CIRCLE_ANGLE_SPAN = 10f;
	// 螺旋面半径变化跨度0.2f---如果为正值；那么螺旋体会越变越宽
	final static float CIRCLE_R_SPAN = 0f;
	// 截面圆开始绘制角度
	final static float CIECLE_ANGLE_BEGIN = 0f;
	// 截面圆结束绘制角度---如果起始角度没有360度；截面将会是一个圆弧
	final static float CIECLE_ANGLE_OVER = 360f;

	/**
	 * @param circleRadius
	 *            ---截面圆半径
	 * @param hradius
	 *            ---螺旋体半径
	 */
	public LineHelicoid(float circleRadius, float hradius) {
		List<Float> lver = new ArrayList<Float>();
		List<Float> lnor = new ArrayList<Float>();
		/*
		 * hangle螺旋体变化角度、cr截面圆半径、hr螺旋体半径 螺旋体角度变化小于360乘以绘制的圈数来控制循环、length螺旋体当前高度
		 */
		for (float hangle = 0, cr = circleRadius, hr = hradius, length = 0; hangle < MAX_ANGLE; hangle += HEDICOID_ANGLE_SPAN, cr += CIRCLE_R_SPAN, hr += HEDICOID_R_SPAN, length += LENGTH_SPAN) {
			/*
			 * cangle截面圆角度变化角度
			 */
			for (float cangle = CIECLE_ANGLE_BEGIN; cangle < CIECLE_ANGLE_OVER; cangle += CIRCLE_ANGLE_SPAN) {
				// 计算第一个顶点坐标
				float x1 = (float) ((hr + cr * Math.cos(Math.toRadians(cangle))) * Math
						.cos(Math.toRadians(hangle)));
				float y1 = (float) (cr * Math.sin(Math.toRadians(cangle)) + length);
				float z1 = (float) ((hr + cr * Math.cos(Math.toRadians(cangle))) * Math
						.sin(Math.toRadians(hangle)));

				float x2 = (float) ((hr + cr
						* Math.cos(Math.toRadians(cangle + CIRCLE_ANGLE_SPAN))) * Math
						.cos(Math.toRadians(hangle)));
				float y2 = (float) (cr
						* Math.sin(Math.toRadians(cangle + CIRCLE_ANGLE_SPAN)) + length);
				float z2 = (float) ((hr + cr
						* Math.cos(Math.toRadians(cangle + CIRCLE_ANGLE_SPAN))) * Math
						.sin(Math.toRadians(hangle)));

				float x3 = (float) (((hr + HEDICOID_R_SPAN) + (cr + CIRCLE_R_SPAN)
						* Math.cos(Math.toRadians(cangle + CIRCLE_ANGLE_SPAN))) * Math
						.cos(Math.toRadians(hangle + HEDICOID_ANGLE_SPAN)));
				float y3 = (float) ((cr + CIRCLE_R_SPAN)
						* Math.sin(Math.toRadians(cangle + CIRCLE_ANGLE_SPAN)) + (length + LENGTH_SPAN));
				float z3 = (float) (((hr + HEDICOID_R_SPAN) + (cr + CIRCLE_R_SPAN)
						* Math.cos(Math.toRadians(cangle + CIRCLE_ANGLE_SPAN))) * Math
						.sin(Math.toRadians(hangle + HEDICOID_ANGLE_SPAN)));

				float x4 = (float) (((hr + HEDICOID_R_SPAN) + (cr + CIRCLE_R_SPAN)
						* Math.cos(Math.toRadians(cangle))) * Math.cos(Math
						.toRadians(hangle + HEDICOID_ANGLE_SPAN)));
				float y4 = (float) ((cr + CIRCLE_R_SPAN)
						* Math.sin(Math.toRadians(cangle)) + (length + LENGTH_SPAN));
				float z4 = (float) (((hr + HEDICOID_R_SPAN) + (cr + CIRCLE_R_SPAN)
						* Math.cos(Math.toRadians(cangle))) * Math.sin(Math
						.toRadians(hangle + HEDICOID_ANGLE_SPAN)));

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
		vCount = lver.size() / 3;
		float[] vertexs = new float[vCount * 3];
		for (int i = 0; i < vCount * 3; i++) {
			vertexs[i] = lver.get(i);
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertexs.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertexs);
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
