package opengl.lance.demo_11;

import javax.microedition.khronos.opengles.GL10;

import static opengl.lance.demo_11.MySurfaceView_3.*;

public class Cactus implements Comparable<Cactus> {
	float x;
	float z;
	// 仙人掌的朝向
	float yAngle;
	MyCactusGroup cg;

	public Cactus(float x, float z, float yAngle, MyCactusGroup cg) {
		this.x = x;
		this.z = z;
		this.yAngle = yAngle;
		this.cg = cg;
	}

	// 绘制仙人掌
	public void drawSelf(GL10 gl) {
		gl.glPushMatrix();
		gl.glTranslatef(x, 0, z);
		gl.glRotatef(yAngle, 0, 1, 0);
		cg.cfd.drawSelf(gl);
		gl.glPopMatrix();
	}

	// 根据摄像机位置计算仙人掌面朝向
	public void calculateBillboardDirection() {
		float xspan = x - cx;
		float zspan = z - cz;
		// 如果摄像机更接近屏幕--->xspan/zspan--->正切值
		if (zspan <= 0) {
			yAngle = (float) Math.toDegrees(Math.atan(xspan / zspan));
		}
		// 如果仙人掌更接近屏幕
		else {
			// 反正切---根据正切值来计算角度
			yAngle = 180 + (float) Math.toDegrees(Math.atan(xspan / zspan));
		}
	}

	@Override
	public int compareTo(Cactus another) {
		// 计算当前仙人掌和摄像机的x/z坐标距离
		float xc = x - cx;
		float zc = z - cz;
		// 计算同当前仙人掌比较的另一个仙人掌贴图和摄像机的距离
		float xo = another.x - cx;
		float zo = another.z - cz;

		float disA = (float) (Math.sqrt(xc * xc + zc * zc));
		float disB = (float) (Math.sqrt(xo * xo + zo * zo));

		return 0;
	}
}
