package asqare.lance.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.graphics.Point;
import asqare.lance.board.Board;
import asqare.lance.board.impl.FullBoard;
import asqare.lance.board.impl.HorizontalBoard;
import asqare.lance.board.impl.VerticalBoard;
import asqare.lance.model.Piece;

/**
 * 游戏逻辑的具体实现
 * 
 * @author ganchengkai
 * 
 */
public class GameServiceImpl implements GameService {
	// 方块数组
	private Piece[][] pieces;
	private GameConf config;

	public GameServiceImpl(GameConf config) {
		// 根据游戏配置创建方块
		this.config = config;
	}

	@Override
	public void start() {
		Board board = null;
		Random random = new Random();
		int index = random.nextInt(3);
		switch (index) {
		case 0:
			board = new VerticalBoard();
			break;
		case 1:
			board = new HorizontalBoard();
			break;
		default:
			board = new FullBoard();
		}
		this.pieces = board.create(config);
	}

	@Override
	public Piece[][] getPieces() {
		return this.pieces;
	}

	@Override
	public boolean hasPieces() {
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[i].length; j++) {
				if (pieces[i][j] != null) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Piece findPiece(float touchX, float touchY) {
		int relativeX = (int) touchX - this.config.getBeginImageX();
		int relativeY = (int) touchY - this.config.getBeginImageY();
		if (relativeX < 0 || relativeY < 0) {
			return null;
		}
		int indexX = getIndex(relativeX, GameConf.PIECE_WIDTH);
		// 获取relativeY坐标在Piece[][]数组中的第二维的索引值
		int indexY = getIndex(relativeY, GameConf.PIECE_HEIGHT);
		// 超出边界返回null
		if (indexX < 0 || indexY < 0) {
			return null;
		}
		if (indexX >= this.config.getXSize()
				|| indexY >= this.config.getYSize()) {
			return null;
		}
		return this.pieces[indexX][indexY];
	}

	private int getIndex(int relative, int size) {
		int index = -1;
		if (relative % size == 0) {
			index = relative / size - 1;
		} else {
			index = relative / size;
		}
		return index;
	}

	@Override
	public LinkInfo link(Piece p1, Piece p2) {
		if (p1.equals(p2))
			return null;
		if (!p1.isSameImage(p2))
			return null;
		if (p2.getIndexX() < p1.getIndexX())
			return link(p2, p1);
		Point p1Point = p1.getCenter();
		Point p2Point = p2.getCenter();
		if (p1.getIndexY() == p2.getIndexY()) {
			if (!isXBlock(p1Point, p2Point, GameConf.PIECE_WIDTH)) {
				return new LinkInfo(p1Point, p2Point);
			}
		}
		// 如果两个Piece在同一列
		if (p1.getIndexX() == p2.getIndexX()) {
			if (!isYBlock(p1Point, p2Point, GameConf.PIECE_HEIGHT)) {
				// 它们之间没有真接障碍, 没有转折点
				return new LinkInfo(p1Point, p2Point);
			}
		}
		// 有一个转折点的情况
		Point cornerPoint = getCornerPoint(p1Point, p2Point,
				GameConf.PIECE_WIDTH, GameConf.PIECE_HEIGHT);
		if (cornerPoint != null) {
			return new LinkInfo(p1Point, cornerPoint, p2Point);
		}
		// 有两个转折点的情况
		Map<Point, Point> turns = getLinkPoints(p1Point, p2Point,
				GameConf.PIECE_WIDTH, GameConf.PIECE_WIDTH);
		if (turns.size() != 0) {
			return getShortcut(p1Point, p2Point, turns,
					getDistance(p1Point, p2Point));
		}
		return null;
	}

	/**
	 * 获取两个转折点的情况
	 * 
	 * @param point1
	 * @param point2
	 * @return 通道就是一些列像素点的集合
	 */
	private Map<Point, Point> getLinkPoints(Point point1, Point point2,
			int pieceWidth, int pieceHeight) {
		Map<Point, Point> result = new HashMap<Point, Point>();
		List<Point> p1UpChannel = getUpChannel(point1, point2.y, pieceHeight);
		List<Point> p1RightChannel = getRightChannel(point1, point2.x,
				pieceWidth);
		List<Point> p1DownChannel = getDownChannel(point1, point2.y,
				pieceHeight);
		List<Point> p2DownChannel = getDownChannel(point2, point1.y,
				pieceHeight);
		List<Point> p2LeftChannel = getLeftChannel(point2, point1.x, pieceWidth);
		List<Point> p2UpChannel = getUpChannel(point2, point1.y, pieceHeight);
		int heightMax = (this.config.getYSize() + 1) * pieceHeight
				+ this.config.getBeginImageY();
		int widthMax = (this.config.getXSize() + 1) * pieceWidth
				+ this.config.getBeginImageX();
		if (isLeftUp(point1, point2) || isLeftDown(point1, point2)) {
			return getLinkPoints(point2, point1, pieceWidth, pieceHeight);
		}
		if (point1.y == point2.y) {
			p1UpChannel = getUpChannel(point1, 0, pieceHeight);
			p2UpChannel = getUpChannel(point2, 0, pieceHeight);
			Map<Point, Point> upLinkPoints = getXLinkPoints(p1UpChannel,
					p2UpChannel, pieceHeight);
			p1DownChannel = getDownChannel(point1, heightMax, pieceHeight);
			p2DownChannel = getDownChannel(point2, heightMax, pieceHeight);
			Map<Point, Point> downLinkPoints = getXLinkPoints(p1DownChannel,
					p2DownChannel, pieceHeight);
			result.putAll(upLinkPoints);
			result.putAll(downLinkPoints);
		}
		if (point1.x == point2.x) {
			List<Point> p1LeftChannel = getLeftChannel(point1, 0, pieceWidth);
			p2LeftChannel = getLeftChannel(point2, 0, pieceWidth);
			Map<Point, Point> leftLinkPoints = getYLinkPoints(p1LeftChannel,
					p2LeftChannel, pieceWidth);
			p1RightChannel = getRightChannel(point1, widthMax, pieceWidth);
			List<Point> p2RightChannel = getRightChannel(point2, widthMax,
					pieceWidth);
			Map<Point, Point> rightLinkPoints = getYLinkPoints(p1RightChannel,
					p2RightChannel, pieceWidth);
			result.putAll(leftLinkPoints);
			result.putAll(rightLinkPoints);
		}
		if (isRightUp(point1, point2)) {
			Map<Point, Point> upDownLinkPoints = getXLinkPoints(p1UpChannel,
					p2DownChannel, pieceWidth);
			Map<Point, Point> rightLeftLinkPoints = getYLinkPoints(
					p1RightChannel, p2LeftChannel, pieceHeight);
			p1UpChannel = getUpChannel(point1, 0, pieceHeight);
			p2UpChannel = getUpChannel(point2, 0, pieceHeight);
			Map<Point, Point> upUpLinkPoints = getXLinkPoints(p1UpChannel,
					p2UpChannel, pieceWidth);
			p1DownChannel = getDownChannel(point1, heightMax, pieceHeight);
			p2DownChannel = getDownChannel(point2, heightMax, pieceHeight);
			Map<Point, Point> downDownLinkPoints = getXLinkPoints(
					p1DownChannel, p2DownChannel, pieceWidth);
			p1RightChannel = getRightChannel(point1, widthMax, pieceWidth);
			List<Point> p2RightChannel = getRightChannel(point2, widthMax,
					pieceWidth);
			Map<Point, Point> rightRightLinkPoints = getYLinkPoints(
					p1RightChannel, p2RightChannel, pieceHeight);
			List<Point> p1LeftChannel = getLeftChannel(point1, 0, pieceWidth);
			p2LeftChannel = getLeftChannel(point2, 0, pieceWidth);
			Map<Point, Point> leftLeftLinkPoints = getYLinkPoints(
					p1LeftChannel, p2LeftChannel, pieceHeight);
			result.putAll(upDownLinkPoints);
			result.putAll(rightLeftLinkPoints);
			result.putAll(upUpLinkPoints);
			result.putAll(downDownLinkPoints);
			result.putAll(rightRightLinkPoints);
			result.putAll(leftLeftLinkPoints);
		}
		if (isRightDown(point1, point2)) {
			Map<Point, Point> downUpLinkPoints = getXLinkPoints(p1DownChannel,
					p2UpChannel, pieceWidth);
			Map<Point, Point> rightLeftLinkPoints = getYLinkPoints(
					p1RightChannel, p2LeftChannel, pieceHeight);
			p1UpChannel = getUpChannel(point1, 0, pieceHeight);
			p2UpChannel = getUpChannel(point2, 0, pieceHeight);
			Map<Point, Point> upUpLinkPoints = getXLinkPoints(p1UpChannel,
					p2UpChannel, pieceWidth);
			p1DownChannel = getDownChannel(point1, heightMax, pieceHeight);
			p2DownChannel = getDownChannel(point2, heightMax, pieceHeight);
			Map<Point, Point> downDownLinkPoints = getXLinkPoints(
					p1DownChannel, p2DownChannel, pieceWidth);
			List<Point> p1LeftChannel = getLeftChannel(point1, 0, pieceWidth);
			p2LeftChannel = getLeftChannel(point2, 0, pieceWidth);
			Map<Point, Point> leftLeftLinkPoints = getYLinkPoints(
					p1LeftChannel, p2LeftChannel, pieceHeight);
			p1RightChannel = getRightChannel(point1, widthMax, pieceWidth);
			List<Point> p2RightChannel = getRightChannel(point2, widthMax,
					pieceWidth);
			Map<Point, Point> rightRightLinkPoints = getYLinkPoints(
					p1RightChannel, p2RightChannel, pieceHeight);
			result.putAll(downUpLinkPoints);
			result.putAll(rightLeftLinkPoints);
			result.putAll(upUpLinkPoints);
			result.putAll(downDownLinkPoints);
			result.putAll(leftLeftLinkPoints);
			result.putAll(rightRightLinkPoints);
		}
		return result;
	}

	/**
	 * 获取p1和p2之间最短的连接信息
	 * 
	 * @param p1
	 * @param p2
	 * @param turns
	 *            放转折点的map
	 * @param shortDistance
	 *            两点之间的最短距离
	 * @return p1和p2之间最短的连接信息
	 */
	private LinkInfo getShortcut(Point p1, Point p2, Map<Point, Point> turns,
			int shortDistance) {
		List<LinkInfo> infos = new ArrayList<LinkInfo>();
		for (Point point1 : turns.keySet()) {
			Point point2 = turns.get(point1);
			infos.add(new LinkInfo(p1, point1, point2, p2));
		}
		return getShortcut(infos, shortDistance);
	}

	/**
	 * 从infos中获取连接线最短的那个LinkInfo对象
	 * 
	 * @param infos
	 * @return 连接线最短的那个LinkInfo对象
	 */
	private LinkInfo getShortcut(List<LinkInfo> infos, int shortDistance) {
		int temp1 = 0;
		LinkInfo result = null;
		for (int i = 0; i < infos.size(); i++) {
			LinkInfo info = infos.get(i);
			int distance = countAll(info.getLinkPoints());
			if (i == 0) {
				temp1 = distance - shortDistance;
				result = info;
			}
			if (distance - shortDistance < temp1) {
				temp1 = distance - shortDistance;
				result = info;
			}
		}
		return result;
	}

	/**
	 * 计算List<Point>中所有点的距离总和
	 * 
	 * @param points
	 *            需要计算的连接点
	 * @return 所有点的距离的总和
	 */
	private int countAll(List<Point> points) {
		int result = 0;
		for (int i = 0; i < points.size() - 1; i++) {
			Point point1 = points.get(i);
			Point point2 = points.get(i + 1);
			result += getDistance(point1, point2);
		}
		return result;
	}

	/**
	 * 获取两个LinkPoint之间的最短距离
	 * 
	 * @param p1
	 *            第一个点
	 * @param p2
	 *            第二个点
	 * @return 两个点的距离距离总和
	 */
	private int getDistance(Point p1, Point p2) {
		int xDistance = Math.abs(p1.x - p2.x);
		int yDistance = Math.abs(p1.y - p2.y);
		return xDistance + yDistance;
	}

	/**
	 * 遍历两个集合, 先判断第一个集合的元素的x座标与另一个集合中的元素x座标相同(纵向), 如果相同, 即在同一列,
	 * 再判断是否有障碍,没有则加到结果的Map中去
	 * 
	 * @param p1Channel
	 * @param p2Channel
	 * @param pieceHeight
	 * @return
	 */
	private Map<Point, Point> getYLinkPoints(List<Point> p1Channel,
			List<Point> p2Channel, int pieceHeight) {
		Map<Point, Point> result = new HashMap<Point, Point>();
		for (int i = 0; i < p1Channel.size(); i++) {
			Point temp1 = p1Channel.get(i);
			for (int j = 0; j < p2Channel.size(); j++) {
				Point temp2 = p2Channel.get(j);
				if (temp1.x == temp2.x) {
					if (!isYBlock(temp1, temp2, pieceHeight)) {
						result.put(temp1, temp2);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 遍历两个集合, 先判断第一个集合的元素的y座标与另一个集合中的元素y座标相同(横向), 如果相同, 即在同一行, 再判断是否有障碍,
	 * 没有则加到结果的map中去
	 * 
	 * @param p1Channel
	 * @param p2Channel
	 * @param pieceWidth
	 * @return 存放可以横向直线连接的连接点的键值对
	 */
	private Map<Point, Point> getXLinkPoints(List<Point> p1Channel,
			List<Point> p2Channel, int pieceWidth) {
		Map<Point, Point> result = new HashMap<Point, Point>();
		for (int i = 0; i < p1Channel.size(); i++) {
			Point temp1 = p1Channel.get(i);
			for (int j = 0; j < p2Channel.size(); j++) {
				Point temp2 = p2Channel.get(j);
				if (temp1.y == temp2.y) {
					if (!isXBlock(temp1, temp2, pieceWidth)) {
						result.put(temp1, temp2);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 判断point2是否在point1的左上角
	 * 
	 * @param point1
	 * @param point2
	 * @return p2位于p1的左上角时返回true，否则返回false
	 */
	private boolean isLeftUp(Point point1, Point point2) {
		return (point2.x < point1.x && point2.y < point1.y);
	}

	/**
	 * 判断point2是否在point1的左下角
	 * 
	 * @param point1
	 * @param point2
	 * @return p2位于p1的左下角时返回true，否则返回false
	 */
	private boolean isLeftDown(Point point1, Point point2) {
		return (point2.x < point1.x && point2.y > point1.y);
	}

	/**
	 * 判断point2是否在point1的右上角
	 * 
	 * @param point1
	 * @param point2
	 * @return p2位于p1的右上角时返回true，否则返回false
	 */
	private boolean isRightUp(Point point1, Point point2) {
		return (point2.x > point1.x && point2.y < point1.y);
	}

	/**
	 * 判断point2是否在point1的右下角
	 * 
	 * @param point1
	 * @param point2
	 * @return p2位于p1的右下角时返回true，否则返回false
	 */
	private boolean isRightDown(Point point1, Point point2) {
		return (point2.x > point1.x && point2.y > point1.y);
	}

	/**
	 * 获取两个不在同一行或者同一列的座标点的直角连接点, 即只有一个转折点
	 * 
	 * @param point1
	 *            第一个点
	 * @param point2
	 *            第二个点
	 * @return 两个不在同一行或者同一列的座标点的直角连接点
	 */
	private Point getCornerPoint(Point point1, Point point2, int pieceWidth,
			int pieceHeight) {
		if (isLeftUp(point1, point2) || isLeftDown(point1, point2)) {
			return getCornerPoint(point2, point1, pieceWidth, pieceHeight);
		}
		List<Point> point1RightChannel = getRightChannel(point1, point2.x,
				pieceWidth);
		List<Point> point1UpChannel = getUpChannel(point1, point2.y,
				pieceHeight);
		List<Point> point1DownChannel = getDownChannel(point1, point2.y,
				pieceHeight);
		List<Point> point2DownChannel = getDownChannel(point2, point1.y,
				pieceHeight);
		List<Point> point2LeftChannel = getLeftChannel(point2, point1.x,
				pieceWidth);
		List<Point> point2UpChannel = getUpChannel(point2, point1.y,
				pieceHeight);
		if (isRightUp(point1, point2)) {
			Point linkPoint1 = getWrapPoint(point1RightChannel,
					point2DownChannel);
			Point linkPoint2 = getWrapPoint(point1UpChannel, point2LeftChannel);
			return (linkPoint1 == null) ? linkPoint2 : linkPoint1;
		}
		if (isRightDown(point1, point2)) {
			Point linkPoint1 = getWrapPoint(point1DownChannel,
					point2LeftChannel);
			Point linkPoint2 = getWrapPoint(point1RightChannel, point2UpChannel);
			return (linkPoint1 == null) ? linkPoint2 : linkPoint1;
		}
		return null;
	}

	/**
	 * 遍历两个通道, 获取它们的交点
	 * 
	 * @param p1Channel
	 *            第一个点的通道
	 * @param p2Channel
	 *            第二个点的通道
	 * @return 两个通道有交点，返回交点，否则返回null
	 */
	private Point getWrapPoint(List<Point> p1Channel, List<Point> p2Channel) {
		for (int i = 0; i < p1Channel.size(); i++) {
			Point temp1 = p1Channel.get(i);
			for (int j = 0; j < p2Channel.size(); j++) {
				Point temp2 = p2Channel.get(j);
				if (temp1.equals(temp2)) {
					return temp1;
				}
			}
		}
		return null;
	}

	/**
	 * 判断两个y座标相同的点对象之间是否有障碍, 以p1为中心向右遍历
	 * 
	 * @param p1
	 * @param p2
	 * @param pieceWidth
	 * @return 两个Piece之间有障碍返回true，否则返回false
	 */
	private boolean isXBlock(Point p1, Point p2, int pieceWidth) {
		if (p2.x < p1.x) {
			return isXBlock(p2, p1, pieceWidth);
		}
		for (int i = p1.x + pieceWidth; i < p2.x; i = i + pieceWidth) {
			if (hasPiece(i, p1.y)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断两个x座标相同的点对象之间是否有障碍, 以p1为中心向下遍历
	 * 
	 * @param p1
	 * @param p2
	 * @param pieceHeight
	 * @return 两个Piece之间有障碍返回true，否则返回false
	 */
	private boolean isYBlock(Point p1, Point p2, int pieceHeight) {
		if (p2.y < p1.y) {
			return isYBlock(p2, p1, pieceHeight);
		}
		for (int i = p1.y + pieceHeight; i < p2.y; i = i + pieceHeight) {
			if (hasPiece(p1.x, i)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断GamePanel中的x, y座标中是否有Piece对象
	 * 
	 * @param x
	 * @param y
	 * @return true 表示有该座标有piece对象 false 表示没有
	 */
	private boolean hasPiece(int x, int y) {
		if (findPiece(x, y) == null)
			return false;
		return true;
	}

	/**
	 * 给一个Point对象,返回它的左边通道
	 * 
	 * @param p
	 * @param pieceWidth
	 *            piece图片的宽
	 * @param min
	 *            向左遍历时最小的界限
	 * @return 给定Point左边的通道
	 */
	private List<Point> getLeftChannel(Point p, int min, int pieceWidth) {
		List<Point> result = new ArrayList<Point>();
		for (int i = p.x - pieceWidth; i >= min; i = i - pieceWidth) {
			if (hasPiece(i, p.y)) {
				return result;
			}
			result.add(new Point(i, p.y));
		}
		return result;
	}

	/**
	 * 给一个Point对象, 返回它的右边通道
	 * 
	 * @param p
	 * @param pieceWidth
	 * @param max
	 *            向右时的最右界限
	 * @return 给定Point右边的通道
	 */
	private List<Point> getRightChannel(Point p, int max, int pieceWidth) {
		List<Point> result = new ArrayList<Point>();
		for (int i = p.x + pieceWidth; i <= max; i = i + pieceWidth) {
			if (hasPiece(i, p.y)) {
				return result;
			}
			result.add(new Point(i, p.y));
		}
		return result;
	}

	/**
	 * 给一个Point对象, 返回它的上面通道
	 * 
	 * @param p
	 * @param min
	 *            向上遍历时最小的界限
	 * @param pieceHeight
	 * @return 给定Point上面的通道
	 */
	private List<Point> getUpChannel(Point p, int min, int pieceHeight) {
		List<Point> result = new ArrayList<Point>();
		for (int i = p.y - pieceHeight; i >= min; i = i - pieceHeight) {
			if (hasPiece(p.x, i)) {
				return result;
			}
			result.add(new Point(p.x, i));
		}
		return result;
	}

	/**
	 * 给一个Point对象, 返回它的下面通道
	 * 
	 * @param p
	 * @param max
	 *            向上遍历时的最大界限
	 * @return 给定Point下面的通道
	 */
	private List<Point> getDownChannel(Point p, int max, int pieceHeight) {
		List<Point> result = new ArrayList<Point>();
		for (int i = p.y + pieceHeight; i <= max; i = i + pieceHeight) {
			if (hasPiece(p.x, i)) {
				return result;
			}
			result.add(new Point(p.x, i));
		}
		return result;
	}
}
