package opengl.lance.demo_13;

import static opengl.lance.demo_13.Constant_2.*;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class BallController_2 {
	Ball_2 ball;
	float timeLive;// 运动总时间
	// 球的质量
	float mass;
	// x方向上的速度
	float xSpeed;
	// x轴上的转角
	float zAngle;
	// 起始坐标
	float startX;
	float startY = 0;
	float startZ = 0;
	// 当前坐标
	float currentX;
	float currentY;
	float currentZ;

	public BallController_2(Ball_2 ball, float startX, float xSpeed, float mass) {
		this.ball = ball;
		this.mass = mass;
		this.startX = startX;
		this.xSpeed = xSpeed;
		currentX = startX;
		currentY = SCALE;
		currentZ = 0;
	}

	public void drawSelf(GL10 gl) {
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glPushMatrix();
		gl.glTranslatef(currentX, currentY, currentZ);
		gl.glRotatef(zAngle, 0, 0, 1);
		ball.drawSelf(gl);
		gl.glPopMatrix();
	}

	public void move(List<BallController_2> bcs) {
		timeLive += TIME_SPAN;
		currentX = startX + xSpeed * timeLive;
		for (int i = 0; i < bcs.size(); i++) {
			BallController_2 bc = bcs.get(i);
			if (bc != this) {
				float distance = Math.abs(this.currentX - bc.currentX);
				this.zAngle = -(float) Math.toDegrees(currentX / SCALE);
				bc.zAngle = (float) Math.toDegrees(currentX / SCALE);
				float previousSpeedA = this.xSpeed;
				float previousSpeedB = bc.xSpeed;
				// 如果两个球相碰---计算碰撞后的速度
				if (distance < SCALE * 2) {
					this.xSpeed = previousSpeedA - 2 * this.mass
							* (previousSpeedA - previousSpeedB)
							/ (this.mass + bc.mass);
					bc.xSpeed = previousSpeedB - 2 * this.mass
							* (previousSpeedB - previousSpeedA)
							/ (this.mass + bc.mass);
					this.startX = currentX;
					bc.startX = bc.currentX;
					this.timeLive = 0;
					bc.timeLive = 0;
				}
			}
		}
	}
}
