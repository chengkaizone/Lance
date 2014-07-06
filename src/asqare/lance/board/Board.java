package asqare.lance.board;

import java.util.List;

import asqare.lance.model.Piece;
import asqare.lance.model.PieceImage;
import asqare.lance.service.GameConf;
import asqare.lance.service.ImageService;

/**
 * 控制游戏摆放的基类---游戏难度由子类扩展
 * 
 * @author ganchengkai
 * 
 */
public abstract class Board {
	/**
	 * 创建游戏方块对象---由子类实现---扩展难度
	 * 
	 * @param config
	 * @param pieces
	 * @return 返回所有的
	 */
	protected abstract List<Piece> createPieces(GameConf config,
			Piece[][] pieces);

	/**
	 * 创建游戏中的方块
	 * 
	 * @param config
	 * @return
	 */
	public Piece[][] create(GameConf config) {
		Piece[][] pieces = new Piece[config.getXSize()][config.getYSize()];
		List<Piece> notNullPieces = createPieces(config, pieces);
		List<PieceImage> playImages = ImageService.getPlayImages(
				config.getContext(), notNullPieces.size());
		int imageWidth = playImages.get(0).getImage().getWidth();
		int imageHeight = playImages.get(0).getImage().getHeight();
		for (int i = 0; i < notNullPieces.size(); i++) {
			Piece piece = notNullPieces.get(i);
			piece.setImage(playImages.get(i));
			piece.setBeginX(piece.getIndexX() * imageWidth
					+ config.getBeginImageX());
			piece.setBeginY(piece.getIndexY() * imageHeight
					+ config.getBeginImageY());
			pieces[piece.getIndexX()][piece.getIndexY()] = piece;
		}
		return pieces;
	}
}
