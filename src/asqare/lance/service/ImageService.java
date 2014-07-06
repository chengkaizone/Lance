package asqare.lance.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.lance.main.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import asqare.lance.model.PieceImage;

/**
 * 加载游戏中图片的工具类
 * 
 * @author ganchengkai
 * 
 */
public class ImageService {
	// 保存所有连连看图片资源值drawable文件夹下的资源Id
	private static List<Integer> imageValues = getImageValues();

	/**
	 * 通过反射获取以candy_开头的资源图片
	 * 
	 * @return
	 */
	public static List<Integer> getImageValues() {
		try {
			Field[] drawableFields = R.drawable.class.getFields();
			List<Integer> resourceValues = new ArrayList<Integer>();
			for (Field field : drawableFields) {
				// 如果该Field的名称以candy_开头
				if (field.getName().indexOf("candy_") == 0) {
					resourceValues.add(field.getInt(R.drawable.class));
				}
			}
			return resourceValues;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 随机从sourceValues的集合中获取size个图片Id
	 * 
	 * @param sourceValues
	 *            从中获取的集合
	 * @param size
	 *            需要获取的个数
	 * @return size个图片Id的集合
	 */
	public static List<Integer> getRandomValues(List<Integer> sourceValues,
			int size) {
		Random random = new Random();
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			try {
				// size为0时会抛出非法参数异常
				int index = random.nextInt(sourceValues.size());
				Integer image = sourceValues.get(index);
				result.add(image);
			} catch (IndexOutOfBoundsException e) {
				return result;
			}
		}
		return result;
	}

	/**
	 * 从drawable目录中中获取size个图片资源Id(以candy_为前缀的资源名称)
	 * 
	 * @param size
	 *            需要获取的图片Id的数量
	 * @return size个图片ID的集合
	 */
	public static List<Integer> getPlayValues(int size) {
		// 处理成偶数
		if (size % 2 != 0) {
			size += 1;
		}
		List<Integer> playImageValues = getRandomValues(imageValues, size / 2);
		playImageValues.addAll(playImageValues);
		// 混排
		Collections.shuffle(playImageValues);
		return playImageValues;
	}

	/**
	 * 将id和位图封装成PieceImage对象
	 * 
	 * @param context
	 * @param resourceValues
	 * @return size个PieceImage对象的集合
	 */
	public static List<PieceImage> getPlayImages(Context context, int size) {
		List<Integer> resourceValues = getPlayValues(size);
		List<PieceImage> result = new ArrayList<PieceImage>();
		for (Integer value : resourceValues) {
			Bitmap src = BitmapFactory.decodeResource(context.getResources(),
					value);
			// 创建制定大小的方块图片
			Bitmap des = Bitmap.createBitmap(src, 0, 0, GameConf.PIECE_WIDTH,
					GameConf.PIECE_HEIGHT);
			// 封装图片ID与图片本身
			PieceImage pieceImage = new PieceImage(des, value);
			result.add(pieceImage);
		}
		return result;
	}

	/**
	 * 获取选中标识的图片
	 * 
	 * @param context
	 * @return 返回被选的位图标志
	 */
	public static Bitmap getSelectImage(Context context) {
		Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.selected);
		return bm;
	}
}
