package opengl.lance.demo_13;

import static opengl.lance.demo_13.Constant_1.*;

import javax.microedition.khronos.opengles.GL10;

/**
 * 计算球的运动逻辑
 * 
 * @author Administrator
 * 
 */
public class BallController_1 {
	Ball_1 ball;// 绘制球
	float timeDown = 0;
	// 球运动的总时间---在运动中逐渐累加
	float timeLive = 0;
	// x方向上的速度
	float xSpeed;
	// y方向上的速度
	float ySpeed;
	// z方向上的速度
	float zSpeed;
	// 最大Y坐标
	float maxHeight;
	// 当前y坐标
	// float currentHeight;
	// 球的起始位置
	float startX;
	float startY;
	float startZ;

	// 球的当前位置
	float currentX;
	float currentY;
	float currentZ;
	// 上一时间位置
	float previousX;
	float previousY;
	float previousZ;

	float xAngle;
	float zAngle;
	// 球的当前状态：0~在板上运动、1~首次下落、2~正在来回
	int state = 0;

	public BallController_1(Ball_1 ball, float startX, float startZ,
			float xSpeed, float zSpeed) {
		this.ball = ball;
		this.xSpeed = xSpeed;
		this.zSpeed = zSpeed;
		this.startX = startX;
		this.startZ = startZ;
		// 计算起始的y坐标
		this.startY = 2 * STARTY + HEIGHT + 2 * SCALE;

		currentX = startX;
		currentY = startY;
		currentZ = startZ;
	}

	public void drawSelf(GL10 gl) {
		// 启用混合
		gl.glEnable(GL10.GL_BLEND);
		// 设置源因子和目标因子
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glPushMatrix();
		gl.glTranslatef(0, 0, 0);
		gl.glTranslatef(currentX, currentY, currentZ);
		gl.glRotatef(xAngle, 1, 0, 0);
		gl.glRotatef(zAngle, 0, 0, 1);
		ball.drawSelf(gl);
		gl.glPopMatrix();
	}

	public void move() {
		// 每次移动都要累加时间
		timeLive += TIME_SPAN;
		// x、z方向上是匀速直线运动
		currentX = startX + xSpeed * timeLive;
		currentZ = startZ + zSpeed * timeLive;
		// 计算x/z轴上的转角---该方法计算的是x方向上的转角--存在很大误差sin三角函数的夹角
		xAngle = (float) Math.toDegrees(currentX / SCALE);
		zAngle = (float) Math.toDegrees(currentZ / SCALE);
		// 在木板上做平移运动
		if (state == 0) {
			// 如果超出木板边界---开始下落
			if (currentX > LENGTH / 2 || currentZ > WIDTH / 2) {
				state = 1;// 下落状态
				timeDown = 0;
			}
		} else if (state == 1) {// 处于首次下落状态
			// 时间的最小单位---根据自由落体运动计算
			timeDown += TIME_SPAN;
			float tempY = startY - 0.5f * G * timeDown * timeDown;
			// 如果落地将发生碰撞
			if (tempY <= SCALE + HEIGHT) {
				state = 2;
				// 计算y方向上的速度---要计算能量损失
				this.ySpeed = G * timeDown * ANERGY_LOST;
				// 重新计时
				timeDown = 0;
			} else {
				currentY = tempY;
			}
		} else if (state == 2) {// 反弹阶段
			timeDown += TIME_SPAN;
			float tempY = SCALE + HEIGHT + ySpeed * timeDown - 0.5f * G
					* timeDown * timeDown;
			// 再次和地板发生碰撞
			if (tempY <= SCALE + HEIGHT) {
				this.ySpeed = (G * timeDown - ySpeed) * ANERGY_LOST;
				timeDown = 0;
			} else {
				currentY = tempY;
			}
		}
	}
}
