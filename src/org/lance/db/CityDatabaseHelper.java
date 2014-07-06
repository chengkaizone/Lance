package org.lance.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.lance.entity.City;

/**
 * 城市表数据操作
 * 
 * @author lance
 * 
 */
public class CityDatabaseHelper extends BaseDatabaseHelper {

	public static final String TABLE_CITY = "citys";
	public static final String CITY_NAME = "city_name";
	public static final String CITY_PRO = "city_provice";
	public static final String CITY_SPELL = "city_spell";

	public CityDatabaseHelper(Context context) {
		super(context);
	}

	static final String CITY_SQL = "create table " + TABLE_CITY
			+ "(_id integer primary key autoincrement," + CITY_NAME
			+ " text not null," + CITY_PRO + " text not null," + CITY_SPELL
			+ " text not null)";

	public void insertCity(City city) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(CITY_NAME, city.getName());
		cv.put(CITY_PRO, city.getProvince());
		cv.put(CITY_SPELL, city.getSpell());
		db.insert(TABLE_CITY, null, cv);
		db.close();
	}

	public void initTable(List<City> data) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = null;
		City city = null;
		for (int i = 0; i < data.size(); i++) {
			city = data.get(i);
			cv = new ContentValues();
			cv.put(CITY_NAME, city.getName());
			cv.put(CITY_PRO, city.getProvince());
			cv.put(CITY_SPELL, city.getSpell());
			db.insert(TABLE_CITY, null, cv);
		}
		db.close();
	}

	public List<City> getCitys() {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.query(TABLE_CITY, null, null, null, null, null, null);
		List<City> citys = new ArrayList<City>();
		City city = null;
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			city = new City();
			city.setName(cursor.getString(cursor.getColumnIndex(CITY_NAME)));
			city.setProvince((cursor.getString(cursor.getColumnIndex(CITY_PRO))));
			city.setSpell((cursor.getString(cursor.getColumnIndex(CITY_SPELL))));
			citys.add(city);
		}
		cursor.close();
		db.close();
		return citys;
	}

	public List<City> getCitysForSpell() {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.query(TABLE_CITY, null, null, null, null, null,
				CITY_SPELL);
		List<City> citys = new ArrayList<City>();
		City city = null;
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			city = new City();
			city.setName(cursor.getString(cursor.getColumnIndex(CITY_NAME)));
			city.setProvince((cursor.getString(cursor.getColumnIndex(CITY_PRO))));
			city.setSpell((cursor.getString(cursor.getColumnIndex(CITY_SPELL))));
			citys.add(city);
		}
		cursor.close();
		db.close();
		return citys;
	}

	/**
	 * 是否已存入数据库---作为快速判断方法
	 * 
	 * @return
	 */
	public boolean hasRecode() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_CITY, null, null, null, null, null, null);
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
}
