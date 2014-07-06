package opengl.lance.demo_13;

import javax.microedition.khronos.opengles.GL10;

/**
 * 代表某个粒子系统中的一个粒子；该类包括单个粒子的各个属性
 * 
 * @author Administrator
 * 
 */
public class HandleParticle {
	// 离子编号
	int particleIndex;
	// 各个方向上的速度
	float xSpeed;
	float ySpeed;
	float zSpeed;
	// 时间间隔
	float timeSpan = 0;

	public HandleParticle(int index, float xSpeed, float ySpeed, float zSpeed) {
		this.particleIndex = index;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.zSpeed = zSpeed;
	}

	public void drawSelf(GL10 gl) {
		gl.glPushMatrix();
		// 根据当前时间戳计算出粒子位置
		float x = xSpeed * timeSpan;
		float z = zSpeed * timeSpan;
		float y = ySpeed * timeSpan - 0.5f * timeSpan * timeSpan * 1.0f;
		gl.glTranslatef(x, y, z);
		// 绘制粒子
		FireSport.pArray[particleIndex].drawSelf(gl);
		gl.glPopMatrix();
	}
}
