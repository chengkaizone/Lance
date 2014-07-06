package opengl.lance.demo_15;

import javax.microedition.khronos.opengles.GL10;

public abstract class Shape {
	boolean hiFlag;// �Ƿ�Ŵ���С

	/**
	 * ��������ͼ��
	 * 
	 * @param gl
	 */
	public abstract void drawSelf(GL10 gl);

	/**
	 * ������������ά�ѿ�������ϵͳ�Ķ����������������ϵĳ���
	 * 
	 * @return
	 */
	public abstract float[] findMinMax();

	/**
	 * ��������ͼ�ε����ĵ�~���������1/2
	 * 
	 * @return
	 */
	public abstract float[] findMid();

	/**
	 * ���������Ƿ�Ŵ�~true�Ŵ�~false��С
	 * 
	 * @param flag
	 */
	public abstract void setHilight(boolean flag);

}
