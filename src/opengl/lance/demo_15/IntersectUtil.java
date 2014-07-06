package opengl.lance.demo_15;

import java.util.ArrayList;
import java.util.Collections;

public class IntersectUtil {
	/**
	 * 判断两点是否相交
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param length
	 * @param width
	 * @param height
	 * @param startX
	 * @param startY
	 * @param startZ
	 * @param endX
	 * @param endY
	 * @param endZ
	 * @return 返回与说有点相交的距离~不相距离为无穷大
	 */
	public static float isIntersectant(float x, float y, float z, float length,
			float width, float height, float startX, float startY,
			float startZ, float endX, float endY, float endZ) {
		int count = 0;
		ArrayList<Float> al = new ArrayList<Float>();
		// 可视区域
		float left = x - length; // 左面
		float right = x + length; // 右面
		float top = y + height; // 上面
		float bottom = y - height; // 下面
		float face = z + width; // 前面
		float back = z - width; // 后面
		// 计算左面相交点
		float[] pointOnLeft = isLineCrossPlaneX(left, startX, startY, startZ,
				endX, endY, endZ);
		// 计算右面相交点
		float[] pointOnRight = isLineCrossPlaneX(right, startX, startY, startZ,
				endX, endY, endZ);
		// 计算上面相交点
		float[] pointOnTop = isLineCrossPlaneY(top, startX, startY, startZ,
				endX, endY, endZ);
		// 计算下面相交点
		float[] pointOnBottom = isLineCrossPlaneY(bottom, startX, startY,
				startZ, endX, endY, endZ);
		// 计算前面相交点
		float[] pointOnFace = isLineCrossPlaneZ(face, startX, startY, startZ,
				endX, endY, endZ);
		// 计算后面相交点
		float[] pointOnBack = isLineCrossPlaneZ(back, startX, startY, startZ,
				endX, endY, endZ);

		// 判断直线是否穿过立方体
		// 首先判断点是否在射线上，在判断相交点是否在长方体坐标范围内
		if (isOnOneLine(startX, startY, startZ, endX, endY, endZ,
				pointOnLeft[0], pointOnLeft[1], pointOnLeft[2])
				&& pointOnLeft[1] >= bottom
				&& pointOnLeft[1] <= top
				&& pointOnLeft[2] >= back && pointOnLeft[2] <= face) {
			float distance = 0;
			distance = getDistance(startX, startY, startZ, pointOnLeft[0],
					pointOnLeft[1], pointOnLeft[2]);
			al.add(distance);
			count++;
		}

		if (isOnOneLine(startX, startY, startZ, endX, endY, endZ,
				pointOnRight[0], pointOnRight[1], pointOnRight[2])
				&& pointOnRight[1] >= bottom
				&& pointOnRight[1] <= top
				&& pointOnRight[2] >= back && pointOnRight[2] <= face) {
			float distance = 0;
			distance = getDistance(startX, startY, startZ, pointOnRight[0],
					pointOnRight[1], pointOnRight[2]);
			al.add(distance);
			count++;
		}

		if (isOnOneLine(startX, startY, startZ, endX, endY, endZ,
				pointOnTop[0], pointOnTop[1], pointOnTop[2])
				&& pointOnTop[0] >= left
				&& pointOnTop[0] <= right
				&& pointOnTop[2] >= back && pointOnTop[2] <= face) {
			float distance = 0;
			distance = getDistance(startX, startY, startZ, pointOnTop[0],
					pointOnTop[1], pointOnTop[2]);
			al.add(distance);
			count++;
		}

		if (isOnOneLine(startX, startY, startZ, endX, endY, endZ,
				pointOnBottom[0], pointOnBottom[1], pointOnBottom[2])
				&& pointOnBottom[0] >= left
				&& pointOnBottom[0] <= right
				&& pointOnBottom[2] >= back && pointOnBottom[2] <= face) {
			float distance = 0;
			distance = getDistance(startX, startY, startZ, pointOnBottom[0],
					pointOnBottom[1], pointOnBottom[2]);
			al.add(distance);
			count++;
		}

		if (isOnOneLine(startX, startY, startZ, endX, endY, endZ,
				pointOnFace[0], pointOnFace[1], pointOnFace[2])
				&& pointOnFace[0] >= left
				&& pointOnFace[0] <= right
				&& pointOnFace[1] >= bottom && pointOnFace[1] <= top) {
			float distance = 0;
			distance = getDistance(startX, startY, startZ, pointOnFace[0],
					pointOnFace[1], pointOnFace[2]);
			al.add(distance);
			count++;
		}

		if (isOnOneLine(startX, startY, startZ, endX, endY, endZ,
				pointOnBack[0], pointOnBack[1], pointOnBack[2])
				&& pointOnBack[0] >= left
				&& pointOnBack[0] <= right
				&& pointOnBack[1] >= bottom && pointOnBack[1] <= top) {
			float distance = 0;
			distance = getDistance(startX, startY, startZ, pointOnBack[0],
					pointOnBack[1], pointOnBack[2]);
			al.add(distance);
			count++;
		}

		if (count >= 2) {
			Collections.sort(al);
			return al.get(0);
		} else {
			return Float.POSITIVE_INFINITY;
		}
	}

	// 判断三个坐标点是否共线并且顺次分布~与物体的交点必须在可视区域内
	public static boolean isOnOneLine(float startX, float startY, float startZ,
			float endX, float endY, float endZ, float intersectX,
			float intersectY, float intersectZ) {
		if (bigOrSmall(startX, endX, intersectX)
				&& bigOrSmall(startY, endY, intersectY)
				&& bigOrSmall(startZ, endZ, intersectZ)) {
			return true;
		}
		return false;
	}

	// 判断三点是否顺次分布在射线上 another必须在start~end之间
	public static boolean bigOrSmall(float start, float end, float another) {
		if (start > end) {
			if (another >= end && another <= start) {
				return true;
			}
		} else {
			if (another >= start && another <= end) {
				return true;
			}
		}
		return false;
	}

	// 求线与面交点的坐标（面平行于X轴）
	public static float[] isLineCrossPlaneX(float planeX, float startX,
			float startY, float startZ, float endX, float endY, float endZ) {
		float[] forCompared = new float[3];
		forCompared[0] = planeX;
		forCompared[1] = startY + (planeX - startX) * (endY - startY)
				/ (endX - startX);
		forCompared[2] = startZ + (planeX - startX) * (endZ - startZ)
				/ (endX - startX);
		return forCompared;
	}

	// 求线与面交点的坐标（面平行于Y轴）
	public static float[] isLineCrossPlaneY(float planeY, float startX,
			float startY, float startZ, float endX, float endY, float endZ) {
		float[] forCompared = new float[3];
		forCompared[0] = startX + (planeY - startY) * (endX - startX)
				/ (endY - startY);
		forCompared[1] = planeY;
		forCompared[2] = startZ + (planeY - startY) * (endZ - startZ)
				/ (endY - startY);
		return forCompared;
	}

	// 求线与面交点的坐标（面平行于Z轴）
	public static float[] isLineCrossPlaneZ(float planeZ, float startX,
			float startY, float startZ, float endX, float endY, float endZ) {
		float[] forCompared = new float[3];
		forCompared[0] = startX + (planeZ - startZ) * (endX - startX)
				/ (endZ - startZ);
		forCompared[1] = startY + (planeZ - startZ) * (endY - startY)
				/ (endZ - startZ);
		forCompared[2] = planeZ;
		return forCompared;
	}

	/**
	 * 获取交点与near平面的绝对距离
	 * 
	 * @param startX
	 * @param startY
	 * @param startZ
	 * @param intersectX
	 * @param intersectY
	 * @param intersectZ
	 * @return
	 */
	public static float getDistance(float startX, float startY, float startZ,
			float intersectX, float intersectY, float intersectZ) {
		float distance = 0;
		double numberForDistance = (double) ((intersectX - startX)
				* (intersectX - startX) + (intersectY - startY)
				* (intersectY - startY) + (intersectZ - startZ)
				* (intersectZ - startZ));
		distance = (float) Math.sqrt(numberForDistance);
		return distance;
	}
}
