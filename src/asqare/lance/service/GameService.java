package asqare.lance.service;

import asqare.lance.model.Piece;

/**
 * 游戏逻辑处理---游戏可扩展性
 * 
 * @author ganchengkai
 * 
 */
public interface GameService {
	/**
	 * 控制游戏开始的方法
	 */
	void start();

	/**
	 * 定义一个接口方法, 用于返回一个二维数组
	 * 
	 * @return 存放方块对象的二维数组
	 */
	Piece[][] getPieces();

	/**
	 * 判断模块中是否还有方块存在
	 * 
	 * @return 如果还剩Piece对象返回true, 没有返回false
	 */
	boolean hasPieces();

	/**
	 * 根据鼠标的x座标和y坐标, 查找出一个Piece对象
	 * 
	 * @param touchX
	 *            鼠标点击的x座标
	 * @param touchY
	 *            鼠标点击的y座标
	 * @return 返回对应的Piece对象, 没有返回null
	 */
	Piece findPiece(float touchX, float touchY);

	/**
	 * 判断两个Piece是否可以相连, 可以连接, 返回LinkInfo对象
	 * 
	 * @param p1
	 *            第一个Piece对象
	 * @param p2
	 *            第二个Piece对象
	 * @return 如果可以相连，返回LinkInfo对象, 如果两个Piece不可以连接, 返回null
	 */
	LinkInfo link(Piece p1, Piece p2);
}
