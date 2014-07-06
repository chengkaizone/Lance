package org.lance.db;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import org.lance.entity.ImageInfo;

/**
 * 将二进制图片存入数据库---注意二进制文件只能使用blob；不能存入字符串
 * 不能通过二进制数组转化为字符串存储--可能是因为转化的二进制数组在数据库中的存储机制不同
 * 
 * @author lance
 * 
 */
public class ImageDatabaseHelper extends BaseDatabaseHelper {
	public static final String TABLE_IMAGE = "images";
	public static final String IMAGE_NAME = "name";
	public static final String IMAGE_DATA = "data";

	public ImageDatabaseHelper(Context context) {
		super(context);
	}

	static String IMAGE_SQL = "create table " + TABLE_IMAGE
			+ "(_id integer primary key autoincrement," + IMAGE_NAME
			+ " text not null," + IMAGE_DATA + " blob)";

	public boolean insertIcon(ImageInfo image) {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = getWritableDatabase();
			cursor = db.query(TABLE_IMAGE, null, "name=?",
					new String[] { image.getName() }, null, null, null);
			if (cursor.getCount() == 0) {
				ContentValues cv = new ContentValues();
				cv.put(IMAGE_NAME, image.getName());
				cv.put(IMAGE_DATA, image.getData());
				db.insert(TABLE_IMAGE, null, cv);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return false;
	}

	public ImageInfo getImageInfo(String name) {
		ImageInfo info = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = getWritableDatabase();
			cursor = db.query(TABLE_IMAGE, null, "name=?", new String[] { name },
					null, null, null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				info = new ImageInfo();
				byte[] brr = cursor.getBlob(cursor.getColumnIndex(IMAGE_DATA));
				info.setName(name);
				info.setData(brr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			db.close();
		}
		return info;
	}

	/**
	 * 是否已存入数据库---作为快速判断方法
	 * 
	 * @return
	 */
	public boolean hasRecode() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_IMAGE, null, null, null, null, null, null);
		try {
			if (cursor.getCount() > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return false;
	}

	// 将位图转化为二进制数组---转化为字符串存入数据库
	public byte[] bitmapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	// 将流转化为二进制数组
	public byte[] getInput(InputStream input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		while ((len = input.read(b, 0, 1024)) != -1) {
			baos.write(b, 0, len);
			baos.flush();
		}
		return baos.toByteArray();
	}
}
