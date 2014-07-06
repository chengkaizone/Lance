package org.lance.util;

import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 获取指定类型墙纸对应的描述信息
 * 
 * @author chengkai
 * 
 */
public class Prefs {
	// 共享参数文件名
	private static final String PREFER_NAME = "lance";
	private static final String DB_VERSION = "db_version";

	private static Properties env = new Properties();
	static {
		try {
			// 相对路径获得输入流
			InputStream is = Prefs.class
					.getResourceAsStream("/lance.properties");
			env.load(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 返回数据版本
	 * 
	 * @return
	 */
	public static int getDBVersion() {
		try {
			String code = env.getProperty(DB_VERSION);
			return Integer.parseInt(code);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return 1;
	}
}
