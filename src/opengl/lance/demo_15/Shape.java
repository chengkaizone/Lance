package opengl.lance.demo_15;

import javax.microedition.khronos.opengles.GL10;

public abstract class Shape {
	boolean hiFlag;// 是否放大缩小

	/**
	 * 绘制立体图形
	 * 
	 * @param gl
	 */
	public abstract void drawSelf(GL10 gl);

	/**
	 * 根据物体在三维笛卡尔坐标系统的顶点计算出各个方向上的长度
	 * 
	 * @return
	 */
	public abstract float[] findMinMax();

	/**
	 * 计算立体图形的中心点~各个方向的1/2
	 * 
	 * @return
	 */
	public abstract float[] findMid();

	/**
	 * 设置物体是否放大~true放大~false缩小
	 * 
	 * @param flag
	 */
	public abstract void setHilight(boolean flag);

}
