package opengl.lance.demo_7;

import javax.microedition.khronos.opengles.GL10;

/**
 * 将双曲面和平面圆组合成硒鼓
 * 
 * @author Administrator
 * 
 */
public class Drum {
	HSurfaceView7_9 surface;
	Hyperboloid hyperboloid;
	Circle circle;

	public float xAngle;
	public float yAngle;
	public float zAngle;

	public Drum(HSurfaceView7_9 surface) {
		this.surface = surface;

		hyperboloid = new Hyperboloid(10f, 2f, 2f, 6f, 10, 18,
				surface.renderer.hyperId);// 创建圆柱体
		circle = new Circle(hyperboloid.radius, 12f, surface.renderer.circleId);
	}

	public void drawSelf(GL10 gl) {
		gl.glRotatef(xAngle, 1, 0, 0);// 旋转
		gl.glRotatef(yAngle, 0, 1, 0);
		gl.glRotatef(zAngle, 0, 0, 1);

		gl.glPushMatrix();
		initMaterial(gl);// 初始化纹理
		hyperboloid.drawSelf(gl);// 绘制
		gl.glPopMatrix();// 恢复变换矩阵现场

		gl.glPushMatrix();
		initMaterial(gl);
		gl.glTranslatef(0, hyperboloid.height * 0.5f, 0);
		gl.glRotatef(90, 1, 0, 0);
		gl.glRotatef(180, 0, 0, 1);
		circle.drawSelf(gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
		initMaterial(gl);
		gl.glTranslatef(0, -hyperboloid.height * 0.5f, 0);
		gl.glRotatef(-90, 1, 0, 0);
		circle.drawSelf(gl);
		gl.glPopMatrix();
	}

	// 初始化材质
	private void initMaterial(GL10 gl) {
		// 环境光
		float ambientMaterial[] = { 248f / 255f, 242f / 255f, 144f / 255f, 1.0f };
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT,
				ambientMaterial, 0);
		// 散射光
		float diffuseMaterial[] = { 248f / 255f, 242f / 255f, 144f / 255f, 1.0f };
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE,
				diffuseMaterial, 0);
		// 高光材质
		float specularMaterial[] = { 248f / 255f, 242f / 255f, 144f / 255f,
				1.0f };
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR,
				specularMaterial, 0);
		gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 100.0f);
	}
	// HSurfaceView7_9 surface;
	// private Hyperboloid hyper;
	// private Circle circle;
	//
	// float xAngle;
	// float yAngle;
	// float zAngle;
	//
	// public Drum(HSurfaceView7_9 surface) {
	// this.surface = surface;
	// // 创建曲面体
	// hyper = new Hyperboloid(10f, 2f, 2f, 6f, 10, 18,
	// surface.renderer.hyperId);
	// // 创建圆---鼓皮
	// circle = new Circle(hyper.radius, 12, surface.renderer.circleId);
	// }
	//
	// public void drawSelf(GL10 gl) {
	// gl.glRotatef(xAngle, 1, 0, 0);
	// gl.glRotatef(yAngle, 0, 1, 0);
	// gl.glRotatef(zAngle, 0, 0, 1);
	// // 先绘制红鼓双曲面
	// gl.glPushMatrix();
	// initMaterial(gl);// 初始化纹理
	// hyper.drawSelf(gl);// 绘制
	// gl.glPopMatrix();// 恢复变换矩阵现场
	// // 先平移一段距离在转换一定视角绘制两个圆
	// gl.glPushMatrix();
	// initMaterial(gl);
	// gl.glTranslatef(0,hyper.height * 0.5f, 0);
	// gl.glRotatef(90, 1, 0, 0);
	// gl.glRotatef(180, 0, 0, 1);
	// circle.drawSelf(gl);
	// gl.glPopMatrix();
	//
	// gl.glPushMatrix();
	// initMaterial(gl);
	// // 水平方向平移
	// gl.glTranslatef(0,-hyper.height * 0.5f, 0);
	// gl.glRotatef(-90, 1, 0, 0);
	// circle.drawSelf(gl);
	// gl.glPopMatrix();
	// }
	//
	// // 初始化材质
	// private void initMaterial(GL10 gl) {
	// // 环境光
	// float ambientMaterial[] = { 248f / 255f, 242f / 255f, 144f / 255f, 1.0f
	// };
	// gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT,
	// ambientMaterial, 0);
	// // 散射光
	// float diffuseMaterial[] = { 248f / 255f, 242f / 255f, 144f / 255f, 1.0f
	// };
	// gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE,
	// diffuseMaterial, 0);
	// // 高光材质
	// float specularMaterial[] = { 248f / 255f, 242f / 255f, 144f / 255f,
	// 1.0f };
	// gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR,
	// specularMaterial, 0);
	// gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 100.0f);
	// }
}
