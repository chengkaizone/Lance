package org.lance.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/**
 * ≈‰÷√π§æﬂ
 * 
 * @author lance
 * 
 */
public class PrefHelper {
	protected static final String sPrefName = null;

	public static SharedPreferences getSharedPreferences(Context context) {
		if (!TextUtils.isEmpty(sPrefName)) {
			return context.getSharedPreferences(sPrefName, 0);
		}
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static boolean getBoolean(Context context, String key, boolean def) {
		return getSharedPreferences(context).getBoolean(key, def);
	}

	public static float getFloat(Context context, String key, float def) {
		return getSharedPreferences(context).getFloat(key, def);
	}

	public static int getInt(Context context, String key, int def) {
		return getSharedPreferences(context).getInt(key, def);
	}

	public static long getLong(Context context, String key, long def) {
		return getSharedPreferences(context).getLong(key, def);
	}

	public static String getString(Context context, String key, String def) {
		return getSharedPreferences(context).getString(key, def);
	}

	public static void putBoolean(Context context, String key, boolean value) {
		getSharedPreferences(context).edit().putBoolean(key, value).commit();
	}

	public static void putFloat(Context context, String key, float value) {
		getSharedPreferences(context).edit().putFloat(key, value).commit();
	}

	public static void putInt(Context context, String key, int value) {
		getSharedPreferences(context).edit().putInt(key, value).commit();
	}

	public static void putLong(Context context, String key, long value) {
		getSharedPreferences(context).edit().putLong(key, value).commit();
	}

	public static void putString(Context context, String key, String value) {
		getSharedPreferences(context).edit().putString(key, value).commit();
	}

	public static void remove(Context context, String key) {
		getSharedPreferences(context).edit().remove(key).commit();
	}
}