package opengl.lance.demo_15;

public class CalUtil {
	/**
	 * 
	 * @param angle弧度
	 * @param gVector重力向量
	 *            [x,y,z,1]
	 * @return
	 */
	public static double[] xRotate(double angle, double[] gVector) {
		// 绕x轴旋转变换矩阵
		double[][] matrix = { { 1, 0, 0, 0 },
				{ 0, Math.cos(angle), Math.sin(angle), 0 },
				{ 0, -Math.sin(angle), Math.cos(angle), 0 }, { 0, 0, 0, 1 }, };
		double[] tempDot = { gVector[0], gVector[1], gVector[2], gVector[3] };
		for (int j = 0; j < tempDot.length; j++) {
			gVector[j] = (tempDot[0] * matrix[0][j] + tempDot[1] * matrix[1][j]
					+ tempDot[2] * matrix[2][j] + tempDot[3] * matrix[3][j]);
		}
		return gVector;
	}

	/**
	 * 绕y轴旋转
	 * 
	 * @param angle转角弧度
	 * @param gVector重力向量
	 * @return
	 */
	public static double[] yRotate(double angle, double[] gVector) {
		// 绕y轴旋转变换矩阵
		double[][] matrix = { { Math.cos(angle), 0, -Math.sin(angle), 0 },
				{ 0, 1, 0, 0 }, { Math.sin(angle), 0, Math.cos(angle), 0 },
				{ 0, 0, 0, 1 } };
		// 各个方向上的向量拷贝
		double[] tempDot = { gVector[0], gVector[1], gVector[2], gVector[3] };
		for (int j = 0; j < tempDot.length; j++) {
			gVector[j] = (tempDot[0] * matrix[0][j] + tempDot[1] * matrix[1][j]
					+ tempDot[2] * matrix[2][j] + tempDot[3] * matrix[3][j]);
		}
		return gVector;
	}

	public static double[] zRotate(double angle, double[] gVector) {
		double[][] matrix = { { Math.cos(angle), Math.sin(angle), 0, 0 },
				{ -Math.sin(angle), Math.cos(angle), 0, 0 }, { 0, 0, 1, 0 },
				{ 0, 0, 0, 1 } };
		double[] tempDot = { gVector[0], gVector[1], gVector[2], gVector[3], };
		for (int j = 0; j < tempDot.length; j++) {
			gVector[j] = (tempDot[0] * matrix[0][j] + tempDot[1] * matrix[1][j]
					+ tempDot[2] * matrix[2][j] + tempDot[3] * matrix[3][j]);
		}
		return gVector;
	}

	public static float[] calculateABPosition(
	// 屏幕x坐标
			float tx, float ty,
			// 屏幕宽高
			float w, float h,
			// 可视区域
			float left, float top, float near, float far,
			// 转角
			float alpha,
			// 摄像机坐标
			float cx, float cy, float cz) {
		// 计算出触摸点距离屏幕中心的距离~此时是以手机屏幕中心点为平面坐标系~计算出触摸点在坐标系中的坐标
		float k = tx - w / 2;
		float p = h / 2 - ty;
		// 计算射线在near平面的坐标~相似三角形
		float kNear = 2 * k * left / w;
		float pNear = 2 * p * top / h;
		// 投影比率
		float ratio = far / near;
		// 计算far平面B点坐标x
		float kFar = ratio * kNear;
		float pFar = ratio * pNear;
		// 射线在near平面交点A点的坐标
		float beforeNX = 0 + kNear;
		float beforeNY = 0 + pNear;
		float beforeNZ = -near;
		// 射线在far平面交点B点的坐标
		float beforeFX = 0 + kFar;
		float beforeFY = 0 + pFar;
		float beforeFZ = -far;
		// A点near平面坐标
		double[] beforeN = new double[] { beforeNX, beforeNY, beforeNZ, 1 };
		// 计算旋转摄像机后的A点坐标
		double[] afterN = yRotate(Math.toRadians(alpha), beforeN);
		// B点far平面坐标
		double[] beforeF = new double[] { beforeFX, beforeFY, beforeFZ, 1 };
		// 计算旋转摄像机后的B点坐标
		double[] afterF = yRotate(Math.toRadians(alpha), beforeF);
		// 返回最终的AB两点坐标
		return new float[] { (float) afterN[2] + cx, (float) afterN[1] + cy,
				(float) afterN[2] + cz, (float) afterF[0] + cx,
				(float) afterF[1] + cy, (float) afterF[2] + cz };
	}
}
