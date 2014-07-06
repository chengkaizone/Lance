package org.lance.db;

import static org.lance.db.CityDatabaseHelper.*;
import static org.lance.db.ImageDatabaseHelper.*;
import org.lance.util.Prefs;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * 数据库操作---提供基本操作
 * 
 * @author lance
 * 
 */
public class BaseDatabaseHelper extends SQLiteOpenHelper {
	public Context context;
	/**
	 * 数据库名
	 */
	static final String DB = "wall.db3";
	

	public BaseDatabaseHelper(Context context) {
		super(context, DB, null, Prefs.getDBVersion());
		this.context = context;
	}

	// 创建所有的表
	public void onCreate(SQLiteDatabase db) {
		try {
			/*
			 * System.out.println(CONDITION_HEAD + TABLE_WALL + TABLE_SQL);
			 * System.out.println(CONDITION_HEAD + TABLE_CART + TABLE_SQL);
			 * System.out.println(ORDER_SQL); System.out.println(SCENE_SQL);
			 * System.out.println(USER_SQL); System.out.println(LOG_SQL);
			 */
			db.execSQL(CITY_SQL);
			db.execSQL(IMAGE_SQL);
			// System.out.println("--->"+LOG_SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void onUpgrade(SQLiteDatabase db, int paramInt1, int paramInt2) {
		upgradeTables(db);
	}

	// 升级数据表---主要是重建墙纸和场景数据表---购物车表结构也会随着墙纸数据表的结构进行更新
	private void upgradeTables(SQLiteDatabase db) {
		try {
			String drop_wall = "drop table if exists " + TABLE_CITY;
			String drop_scene = "drop table if exists " + TABLE_IMAGE;
			db.execSQL(drop_wall);
			db.execSQL(drop_scene);
			db.execSQL(CITY_SQL);
			db.execSQL(IMAGE_SQL);
			/*
			 * 用法向表中增加一个 VARCHAR 列： ALTER TABLE 表名 ADD COLUMN 要添加的列名
			 * VARCHAR(30); 对现存列改名： ALTER TABLE 表名 RENAME COLUMN 要重命名的列名 TO 新列名;
			 * 对现存表改名： ALTER TABLE 表名 RENAME TO 要重命名的表名;
			 */
			/*
			 * System.out.println(drop_wall + "\n" + drop_cart + "\n" +
			 * drop_order + "\n" + drop_effect + "\n" + drop_scene+"\n");
			 */
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
	

