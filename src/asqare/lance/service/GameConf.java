package asqare.lance.service;

import android.content.Context;

/**
 * 游戏配置信息
 * 
 * @author ganchengkai
 * 
 */
public class GameConf {
	// 设置连连看的每个方块的图片的宽、高
	public static final int PIECE_WIDTH = 40;
	public static final int PIECE_HEIGHT = 40;
	// 记录游戏的总事件（默认100秒）.
	public static int DEFAULT_TIME = 100;
	// Piece[][]数组第一维的长度
	private int xSize;
	// Piece[][]数组第二维的长度
	private int ySize;
	// Board中第一张图片出现的x座标
	private int beginImageX;
	// Board中第一张图片出现的y座标
	private int beginImageY;
	// 记录游戏的总时间, 单位是秒
	private long gameTime;
	// 游戏上下文对象
	private Context context;

	/**
	 * 创建游戏配置
	 * 
	 * @param xSize
	 *            Piece[][]数组第一维长度
	 * @param ySize
	 *            Piece[][]数组第二维长度
	 * @param beginImageX
	 *            Board中第一张图片出现的x座标
	 * @param beginImageY
	 *            Board中第一张图片出现的y座标
	 * @param gameTime
	 *            设置每局的时间, 单位是秒
	 * @param context
	 *            应用上下文
	 */
	public GameConf(int xSize, int ySize, int beginImageX, int beginImageY,
			long gameTime, Context context) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.beginImageX = beginImageX;
		this.beginImageY = beginImageY;
		this.gameTime = gameTime;
		this.context = context;
	}

	public long getGameTime() {
		return gameTime;
	}

	public int getXSize() {
		return xSize;
	}

	public int getYSize() {
		return ySize;
	}

	public int getBeginImageX() {
		return beginImageX;
	}

	public int getBeginImageY() {
		return beginImageY;
	}

	public Context getContext() {
		return context;
	}
}
