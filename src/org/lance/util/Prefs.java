package org.lance.util;

import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * ��ȡָ������ǽֽ��Ӧ��������Ϣ
 * 
 * @author chengkai
 * 
 */
public class Prefs {
	// ��������ļ���
	private static final String PREFER_NAME = "lance";
	private static final String DB_VERSION = "db_version";

	private static Properties env = new Properties();
	static {
		try {
			// ���·�����������
			InputStream is = Prefs.class
					.getResourceAsStream("/lance.properties");
			env.load(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * �������ݰ汾
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
