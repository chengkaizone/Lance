package barcode.lance.client.common;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;
import barcode.lance.qrcode.QRCodeReader;
import barcode.lance.qrcode.QRCodeWriter;
import barcode.lance.assist.BarcodeFormat;
import barcode.lance.assist.BinaryBitmap;
import barcode.lance.assist.ChecksumException;
import barcode.lance.assist.EncodeHintType;
import barcode.lance.assist.FormatException;
import barcode.lance.assist.MultiFormatWriter;
import barcode.lance.assist.NotFoundException;
import barcode.lance.assist.Result;
import barcode.lance.assist.WriterException;
import barcode.lance.common.BitMatrix;
import barcode.lance.common.HybridBinarizer;
import static barcode.lance.client.common.StringContant.*;

/**
 * 条码服务类用于生成二维码图片或计息二维码图片
 * 
 * @author chengkai
 */
public class BarcodeService {
	/**
	 * 根据字符串创建二维码图片 默认宽高300
	 * 
	 * @param info
	 * @return
	 * @throws WriterException
	 */
	public static Bitmap getGeneratedBitmap(String info) throws WriterException {
		int width = 300, height = 300;
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BitMatrix bitMatrix = new QRCodeWriter().encode(info,
				BarcodeFormat.QR_CODE, width, height, hints);
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (bitMatrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				} else {
					pixels[y * width + x] = 0xffffffff;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	public static Bitmap encodeAsBitmap(String contents, BarcodeFormat format,
			int desiredWidth, int desiredHeight) throws WriterException {
		final int WHITE = 0xFFFFFFFF; // 可以指定其他颜色，让二维码变成彩色效果
		final int BLACK = 0xFF000000;

		Hashtable<EncodeHintType, String> hints = null;
		String encoding = guessAppropriateEncoding(contents);
		if (encoding != null) {
			hints = new Hashtable<EncodeHintType, String>(2);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result = writer.encode(contents, format, desiredWidth,
				desiredHeight, hints);
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		// All are 0, or black, by default
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 根据二维码图片直接解析出相关信息
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String getDecodeInfo(Bitmap bitmap) {
		Result result = getDecodeResult(bitmap);
		if (result != null) {
			return result.getText();
		}
		return null;
	}

	/**
	 * 通过路径读取二维码图片信息
	 * 
	 * @param path
	 * @return
	 */
	public static String getDecodeInfo(String path) {
		Result result = getDecodeResult(path);
		if (result != null) {
			return result.getText();
		}
		return null;
	}

	/**
	 * 通过路径读取二维码结果
	 * 
	 * @param path
	 * @return
	 */
	public static Result getDecodeResult(String path) {
		try {
			return getDecodeResult(getDecodeBitmap(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通过路径获取位图
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap getDecodeBitmap(String path) {
		return BitmapFactory.decodeFile(path);
	}

	/**
	 * 根据位图解析出结果
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Result getDecodeResult(Bitmap bitmap) {
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		try {
			RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
			BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
			QRCodeReader reader = new QRCodeReader();
			return reader.decode(bBitmap, hints);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 保存条码位图
	 * 
	 * @param bitmap
	 * @return
	 */
	public static boolean saveBarcode(Bitmap bitmap, String fileName) {
		String path = readSD(fileName);
		if (path != null) {
			try {
				FileOutputStream out = new FileOutputStream(path);
				if (bitmap != null) {
					bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * 检测SD卡环境
	 * 
	 * @param file
	 * @return
	 */
	private static String readSD(String fileName) {
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String path = Environment.getExternalStorageDirectory()
						.getCanonicalPath() + "/" + fileName;
				return path;
			} else {
				System.out.println(NO_SDCARD);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将位图转换为二进制数组
	 * 
	 * @param bit
	 * @return
	 */
	public static byte[] changeBitmapToByteArray(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 猜测适当的编码方式
	 * 
	 * @param contents
	 * @return
	 */
	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}
}