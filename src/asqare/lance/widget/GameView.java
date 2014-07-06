package asqare.lance.widget;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import asqare.lance.model.Piece;
import asqare.lance.service.GameService;
import asqare.lance.service.ImageService;
import asqare.lance.service.LinkInfo;

/**
 * 游戏渲染类
 * 
 * @author ganchengkai
 * 
 */
public class GameView extends View {
	private GameService gameService;
	// 当前已经被选中的方块
	private Piece selectedPiece;
	// 两个方块的连接信息
	private LinkInfo linkInfo;
	private Paint paint;
	// 选中标识的图片对象
	private Bitmap selectImage;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.paint = new Paint();
		this.paint.setColor(Color.RED);
		this.paint.setStrokeWidth(3);
		this.selectImage = ImageService.getSelectImage(context);
	}

	public void setLinkInfo(LinkInfo linkInfo) {
		this.linkInfo = linkInfo;
	}

	public void setGameService(GameService gameService) {
		this.gameService = gameService;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (this.gameService == null)
			return;
		Piece[][] pieces = gameService.getPieces();
		if (pieces != null) {
			// 遍历pieces二维数组
			for (int i = 0; i < pieces.length; i++) {
				for (int j = 0; j < pieces[i].length; j++) {
					// 如果二维数组中该元素不为空（即有方块），将这个方块的图片画出来
					if (pieces[i][j] != null) {
						// 得到这个Piece对象
						Piece piece = pieces[i][j];
						// 根据方块左上角X、Y座标绘制方块
						canvas.drawBitmap(piece.getImage().getImage(),
								piece.getBeginX(), piece.getBeginY(), null);
					}
				}
			}
		}
		// 如果当前对象中有linkInfo对象, 即连接信息
		if (this.linkInfo != null) {
			// 绘制连接线
			drawLine(this.linkInfo, canvas);
			// 处理完后清空linkInfo对象
			this.linkInfo = null;
		}
		// 画选中标识的图片
		if (this.selectedPiece != null) {
			canvas.drawBitmap(this.selectImage, this.selectedPiece.getBeginX(),
					this.selectedPiece.getBeginY(), null);
		}
	}

	// 根据LinkInfo绘制连接线的方法。---根据屏幕上的点绘制连线
	private void drawLine(LinkInfo linkInfo, Canvas canvas) {
		List<Point> points = linkInfo.getLinkPoints();
		for (int i = 0; i < points.size() - 1; i++) {
			Point currentPoint = points.get(i);
			Point nextPoint = points.get(i + 1);
			canvas.drawLine(currentPoint.x, currentPoint.y, nextPoint.x,
					nextPoint.y, this.paint);
		}
	}

	/**
	 * 设置当前选中的方块
	 * 
	 * @param piece
	 */
	public void setSelectedPiece(Piece piece) {
		this.selectedPiece = piece;
	}

	/**
	 * 开始游戏
	 */
	public void startGame() {
		this.gameService.start();
		// 通知重绘
		this.postInvalidate();
	}
}
