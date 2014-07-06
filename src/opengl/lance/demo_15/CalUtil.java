package opengl.lance.demo_15;

public class CalUtil {
	/**
	 * 
	 * @param angle����
	 * @param gVector��������
	 *            [x,y,z,1]
	 * @return
	 */
	public static double[] xRotate(double angle, double[] gVector) {
		// ��x����ת�任����
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
	 * ��y����ת
	 * 
	 * @param angleת�ǻ���
	 * @param gVector��������
	 * @return
	 */
	public static double[] yRotate(double angle, double[] gVector) {
		// ��y����ת�任����
		double[][] matrix = { { Math.cos(angle), 0, -Math.sin(angle), 0 },
				{ 0, 1, 0, 0 }, { Math.sin(angle), 0, Math.cos(angle), 0 },
				{ 0, 0, 0, 1 } };
		// ���������ϵ���������
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
	// ��Ļx����
			float tx, float ty,
			// ��Ļ���
			float w, float h,
			// ��������
			float left, float top, float near, float far,
			// ת��
			float alpha,
			// ���������
			float cx, float cy, float cz) {
		// ����������������Ļ���ĵľ���~��ʱ�����ֻ���Ļ���ĵ�Ϊƽ������ϵ~�����������������ϵ�е�����
		float k = tx - w / 2;
		float p = h / 2 - ty;
		// ����������nearƽ�������~����������
		float kNear = 2 * k * left / w;
		float pNear = 2 * p * top / h;
		// ͶӰ����
		float ratio = far / near;
		// ����farƽ��B������x
		float kFar = ratio * kNear;
		float pFar = ratio * pNear;
		// ������nearƽ�潻��A�������
		float beforeNX = 0 + kNear;
		float beforeNY = 0 + pNear;
		float beforeNZ = -near;
		// ������farƽ�潻��B�������
		float beforeFX = 0 + kFar;
		float beforeFY = 0 + pFar;
		float beforeFZ = -far;
		// A��nearƽ������
		double[] beforeN = new double[] { beforeNX, beforeNY, beforeNZ, 1 };
		// ������ת��������A������
		double[] afterN = yRotate(Math.toRadians(alpha), beforeN);
		// B��farƽ������
		double[] beforeF = new double[] { beforeFX, beforeFY, beforeFZ, 1 };
		// ������ת��������B������
		double[] afterF = yRotate(Math.toRadians(alpha), beforeF);
		// �������յ�AB��������
		return new float[] { (float) afterN[2] + cx, (float) afterN[1] + cy,
				(float) afterN[2] + cz, (float) afterF[0] + cx,
				(float) afterF[1] + cy, (float) afterF[2] + cz };
	}
}
