package opengl.lance.demo_8;

import javax.microedition.khronos.opengles.GL10;

import static opengl.lance.demo_8.Constant.*;

public class MyCube {
	private Rect big;
	private Rect small;
	private float scale;

	private float xOffset;
	private float zOffset;

	public MyCube(float scale, int bigTexId, int smallTexId) {
		this.scale = scale;
		big = new Rect(scale, 2 * scale, bigTexId);
		small = new Rect(scale, scale, smallTexId);
	}

	public void drawSelf(GL10 gl) {
		gl.glPushMatrix();
		gl.glTranslatef(xOffset, 0, zOffset);
		// 绘制前小面
		gl.glPushMatrix();
		gl.glTranslatef(0, 0, 2 * UNIT_SIZE * scale);
		small.drawSelf(gl);
		gl.glPopMatrix();
		// 绘制后小面
		gl.glPushMatrix();
		gl.glTranslatef(0, 0, -2 * UNIT_SIZE * scale);
		// 切换视角绕y轴旋转180度绘制
		gl.glRotatef(180, 0, 1, 0);
		small.drawSelf(gl);
		gl.glPopMatrix();
		// 绘制上大面
		gl.glPushMatrix();
		// 朝上平移一个单位
		gl.glTranslatef(0, UNIT_SIZE * scale, 0);
		gl.glRotatef(-90, 1, 0, 0);
		big.drawSelf(gl);
		gl.glPopMatrix();
		// 绘制下大面
		gl.glPushMatrix();
		gl.glTranslatef(0, -UNIT_SIZE * scale, 0);
		gl.glRotatef(90, 1, 0, 0);
		big.drawSelf(gl);
		gl.glPopMatrix();
		// 绘制左大面
		gl.glPushMatrix();
		gl.glTranslatef(UNIT_SIZE * scale, 0, 0);
		gl.glRotatef(-90, 1, 0, 0);
		gl.glRotatef(90, 0, 1, 0);
		big.drawSelf(gl);
		gl.glPopMatrix();
		// 绘制右大面
		gl.glPushMatrix();
		gl.glTranslatef(-UNIT_SIZE * scale, 0, 0);
		gl.glRotatef(90, 1, 0, 0);
		gl.glRotatef(-90, 0, 1, 0);
		big.drawSelf(gl);
		gl.glPopMatrix();

		gl.glPopMatrix();
	}
}
