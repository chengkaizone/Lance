package opengl.lance.demo_15;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class LogicControl {
	ZhuiTi zt;
	float minX;
	float maxX;
	float minY;
	float maxY;
	float minZ;
	float maxZ;
	// true~向右走
	boolean flag = true;
	float xOffset;
	float yOffset;
	float zOffset;
	float xSpeed;
	float ySpeed;
	float zSpeed;
	// 模拟时间间隔~因为现实生活中时间是连续的；程序中不是
	final float SPAN_TIME = 0.2F;
	final float V_UNIT = 0.5f;
	// 判断x方向上是否相碰撞
	float tempxOffset;
	float tempyOffset;
	float tempzOffset;

	public LogicControl(ZhuiTi zt, float xOffset, float yOffset, float zOffset,
			float xSpeed, float ySpeed, float zSpeed) {
		this.zt = zt;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.zSpeed = zSpeed;
		init();
		findMinMax();
	}

	private void init() {
		minX = Float.POSITIVE_INFINITY;
		minY = Float.POSITIVE_INFINITY;
		minZ = Float.POSITIVE_INFINITY;
		maxX = Float.NEGATIVE_INFINITY;
		maxY = Float.NEGATIVE_INFINITY;
		maxZ = Float.NEGATIVE_INFINITY;
	}

	public void findMinMax() {
		for (int i = 0; i < zt.tempVerteices.length / 3; i++) {
			// 初始化X的最小最大位置
			if (zt.tempVerteices[i * 3] < minX) {
				minX = zt.tempVerteices[i * 3];
			}
			if (zt.tempVerteices[i * 3] > maxX) {
				maxX = zt.tempVerteices[i * 3];
			}
			if (zt.tempVerteices[i * 3 + 1] < minY) {
				minY = zt.tempVerteices[i * 3 + 1];
			}
			if (zt.tempVerteices[i * 3 + 1] > maxY) {
				maxY = zt.tempVerteices[i * 3 + 1];
			}
			if (zt.tempVerteices[i * 3 + 2] < minZ) {
				minZ = zt.tempVerteices[i * 3 + 2];
			}
			if (zt.tempVerteices[i * 3 + 2] > maxZ) {
				maxZ = zt.tempVerteices[i * 3 + 2];
			}
		}
	}

	public static float calOver(float amax, float amin, float bmax, float bmin) {
		float leftMax = 0;
		float leftMin = 0;
		float rightMax = 0;
		float rightMin = 0;
		if (amax < bmax) {
			leftMax = amax;
			leftMin = amin;
			rightMax = bmax;
			rightMin = bmin;
		} else {
			leftMax = bmax;
			leftMin = bmin;
			rightMax = amax;
			rightMin = amin;
		}
		if (leftMax > rightMin) {
			return leftMax - rightMin;
		} else {
			return 0;
		}
	}

	// 计算两个智能体之间的距离
	public float check(LogicControl ac, LogicControl bc) {
		float xOver = calOver(ac.maxX + ac.tempxOffset, ac.minX
				+ ac.tempxOffset, bc.maxX + bc.tempxOffset, bc.minX
				+ bc.tempxOffset);
		float yOver = calOver(ac.maxY + ac.tempyOffset, ac.minX
				+ ac.tempyOffset, bc.maxY + bc.tempyOffset, bc.minY
				+ bc.tempyOffset);
		float zOver = calOver(ac.maxZ + ac.tempzOffset, ac.minZ
				+ ac.tempzOffset, bc.maxZ + bc.tempzOffset, bc.minZ
				+ bc.tempzOffset);
		return xOver * yOver * zOver;
	}

	public void go(ArrayList<LogicControl> lcs) {
		tempxOffset = xOffset + xSpeed * SPAN_TIME;
		tempyOffset = yOffset + ySpeed * SPAN_TIME;
		tempzOffset = zOffset + zSpeed * SPAN_TIME;
		for (int i = 0; i < lcs.size(); i++) {
			LogicControl lc = lcs.get(i);
			if (lc != this) {
				float tempV = check(this, lc);
				// 如果碰撞---将速度置反
				if (tempV > V_UNIT) {
					this.xSpeed = -this.xSpeed;
				} else {
					this.xOffset = tempxOffset;
					this.yOffset = tempyOffset;
					this.zOffset = tempzOffset;
				}
			}
		}
	}

	public void drawSelf(GL10 gl) {
		gl.glPushMatrix();
		gl.glTranslatef(xOffset, yOffset, zOffset);
		zt.drawSelf(gl);
		gl.glPopMatrix();
	}
}
