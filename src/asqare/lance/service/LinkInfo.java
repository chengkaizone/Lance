package asqare.lance.service;

import java.util.List;
import java.util.ArrayList;

import android.graphics.Point;

/**
 * 两个方块可以连接的情况---3种情况
 * 
 * @author ganchengkai
 * 
 */
public class LinkInfo {
	// 创建一个集合用于保存连接点
	private List<Point> points = new ArrayList<Point>();

	/**
	 * 直接相连的情况
	 * 
	 * @param p1
	 * @param p2
	 */
	public LinkInfo(Point p1, Point p2) {
		// 加到集合中去
		points.add(p1);
		points.add(p2);
	}

	/**
	 * 一个转折点的情况
	 * 
	 * @param p1
	 * @param p2
	 * @param p3
	 */
	public LinkInfo(Point p1, Point p2, Point p3) {
		points.add(p1);
		points.add(p2);
		points.add(p3);
	}

	/**
	 * 有两个转折点的情况
	 * 
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param p4
	 */
	public LinkInfo(Point p1, Point p2, Point p3, Point p4) {
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
	}

	/**
	 * 返回连接集合
	 * 
	 * @return
	 */
	public List<Point> getLinkPoints() {
		return points;
	}
}
