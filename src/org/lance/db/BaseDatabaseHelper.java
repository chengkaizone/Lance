package org.lance.db;

import static org.lance.db.CityDatabaseHelper.*;
import static org.lance.db.ImageDatabaseHelper.*;
import org.lance.util.Prefs;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * ���ݿ����---�ṩ��������
 * 
 * @author lance
 * 
 */
public class BaseDatabaseHelper extends SQLiteOpenHelper {
	public Context context;
	/**
	 * ���ݿ���
	 */
	static final String DB = "wall.db3";
	

	public BaseDatabaseHelper(Context context) {
		super(context, DB, null, Prefs.getDBVersion());
		this.context = context;
	}

	// �������еı�
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

	// �������ݱ�---��Ҫ���ؽ�ǽֽ�ͳ������ݱ�---���ﳵ��ṹҲ������ǽֽ���ݱ�Ľṹ���и���
	private void upgradeTables(SQLiteDatabase db) {
		try {
			String drop_wall = "drop table if exists " + TABLE_CITY;
			String drop_scene = "drop table if exists " + TABLE_IMAGE;
			db.execSQL(drop_wall);
			db.execSQL(drop_scene);
			db.execSQL(CITY_SQL);
			db.execSQL(IMAGE_SQL);
			/*
			 * �÷����������һ�� VARCHAR �У� ALTER TABLE ���� ADD COLUMN Ҫ��ӵ�����
			 * VARCHAR(30); ���ִ��и����� ALTER TABLE ���� RENAME COLUMN Ҫ������������ TO ������;
			 * ���ִ������� ALTER TABLE ���� RENAME TO Ҫ�������ı���;
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
	

