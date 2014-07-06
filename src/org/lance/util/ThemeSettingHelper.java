package org.lance.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 设置主题的辅助类
 * 
 * @author lance
 * 
 */
public class ThemeSettingHelper {
	// 默认主题
	public static final String THEME_DEFAULT = "default_theme";
	// 夜色主题
	public static final String THEME_NIGHT = "night_theme";
	// 创建弱引用适用于映射
	private static WeakReference<ThemeSettingHelper> sThemeSettingsHelper;
	// 保存弱引用的集合---主要与java的垃圾回收机制有关---后台的Activity可能在某时被回收---资源紧张时
	private final List<WeakReference<ThemeCallback>> mCallbacks = new ArrayList<WeakReference<ThemeCallback>>();
	// 主题上下文
	private Context mThemeContext;
	// 主题包名
	private String mThemePackageName;

	private ThemeSettingHelper(Context context) {
		initTheme(context,
				PreferenceManager.getDefaultSharedPreferences(context)
						.getString("theme", THEME_DEFAULT));
	}

	// 初始化主题主要是为了获取保存的当前主题
	private void initTheme(Context context, String theme) {
		this.mThemeContext = getThemeContext(context, theme);
	}

	/**
	 * 静态方法---获取实例的唯一方法
	 * 
	 * @param context
	 * @return
	 */
	public static final ThemeSettingHelper getThemeSettingsHelper(
			Context context) {
		if ((sThemeSettingsHelper == null)
				|| (sThemeSettingsHelper.get() == null)) {
			sThemeSettingsHelper = new WeakReference(new ThemeSettingHelper(
					context));
		}
		return sThemeSettingsHelper.get();
	}

	private Context getThemeContext(Context context, String themePackageName) {
		this.mThemePackageName = themePackageName;
		if ((THEME_DEFAULT.equals(themePackageName))
				|| (THEME_NIGHT.equals(themePackageName))) {
			return context.getApplicationContext();
		} else {
			try {
				// 返回创建的上下文
				return context.createPackageContext(themePackageName, 3);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getCurrentThemePackage() {
		return this.mThemePackageName;
	}

	public ColorStateList getThemeColor(Context paramContext, int colorId) {
		return (ColorStateList) getThemeResource(paramContext, colorId);
	}

	public Drawable getThemeDrawable(Context paramContext, int resId) {
		return (Drawable) getThemeResource(paramContext, resId);
	}

	public boolean isDefaultTheme() {
		return THEME_DEFAULT.equals(this.mThemePackageName);
	}

	/**
	 * 为ImageView设置源位图
	 * 
	 * @param act
	 * @param layId
	 * @param resId
	 */
	public void setImageViewSrc(Activity act, int layId, int resId) {
		ImageView image = (ImageView) act.findViewById(layId);
		if (image != null) {
			setImageViewSrc(act, image, resId);
		}
	}

	/**
	 * 为ImageView设置源位图
	 * 
	 * @param context
	 * @param image
	 * @param resId
	 */
	public void setImageViewSrc(Context context, ImageView image, int resId) {
		image.setImageDrawable(getThemeDrawable(context, resId));
	}

	/**
	 * 为ListView设置分隔条
	 * 
	 * @param act
	 * @param layId
	 * @param resId
	 */
	public void setListViewDivider(Activity act, int layId, int resId) {
		ListView listView = (ListView) act.findViewById(layId);
		if (listView != null) {
			setListViewDivider(act, listView, resId);
		}
	}

	/**
	 * 设置分隔条
	 * 
	 * @param context
	 * @param listView
	 * @param resId
	 */
	public void setListViewDivider(Context context, ListView listView, int resId) {
		listView.setDivider(getThemeDrawable(context, resId));
	}

	/**
	 * 为ListView设置背景选择器
	 * 
	 * @param act
	 * @param layId
	 * @param resId
	 */
	public void setListViewSelector(Activity act, int layId, int resId) {
		ListView listView = (ListView) act.findViewById(layId);
		if (listView != null) {
			setListViewSelector(act, listView, resId);
		}
	}

	/**
	 * 设置背景选择器
	 * 
	 * @param context
	 * @param listView
	 * @param resId
	 */
	public void setListViewSelector(Context context, ListView listView,
			int resId) {
		listView.setSelector(getThemeDrawable(context, resId));
	}

	/**
	 * 设置字体颜色
	 * 
	 * @param act
	 * @param layId
	 * @param resId
	 */
	public void setTextViewColor(Activity act, int layId, int resId) {
		TextView textView = (TextView) act.findViewById(layId);
		if (textView == null) {
			setTextViewColor(act, textView, resId);
		}
	}

	/**
	 * 设置字体颜色
	 * 
	 * @param context
	 * @param textView
	 * @param resId
	 */
	public void setTextViewColor(Context context, TextView textView, int resId) {
		textView.setTextColor(getThemeColor(context, resId));
	}

	/**
	 * 设置背景--使用drawable对象
	 * 
	 * @param act
	 * @param layId
	 * @param resId
	 */
	public void setViewBackgroud(Activity act, int layId, int resId) {
		View view = act.findViewById(layId);
		if (view != null) {
			setViewBackgroud(act, view, resId);
		}
	}

	/**
	 * 设置背景
	 * 
	 * @param context
	 * @param view
	 * @param resId
	 */
	public void setViewBackgroud(Context context, View view, int resId) {
		view.setBackgroundDrawable(getThemeDrawable(context, resId));
	}

	/**
	 * 为控件设置背景色
	 * 
	 * @param act
	 * @param layId
	 * @param resId
	 */
	public void setViewIdBackgroudColor1(Activity act, int layId, int resId) {
		View view = act.findViewById(layId);
		if (view != null) {
			setViewBackgroudColor2(act, view, resId);
		}
	}

	/**
	 * 为控件设置背景色
	 * 
	 * @param context
	 * @param view
	 * @param resId
	 */
	public void setViewBackgroudColor2(Context context, View view, int resId) {
		view.setBackgroundColor(getThemeColor(context, resId).getDefaultColor());
	}

	/**
	 * 为窗口设置背景---窗口只支持图片--不支持颜色
	 * 
	 * @param act
	 * @param resId
	 */
	public void setWindowBackgroud(Activity act, int resId) {
		act.getWindow().setBackgroundDrawable(getThemeDrawable(act, resId));
	}

	/**
	 * 在菜单中改变主题颜色、背景
	 * 
	 * @param context
	 * @param theme
	 */
	public void changeTheme(Context context, String theme) {
		synchronized (this.mCallbacks) {
			String str = this.mThemePackageName;
			// 这一步主要是为了获取当前保存的主题日光、夜间--方法调用后mThemePackageName会重新赋值
			initTheme(context, theme);
			// 如果不同则重新保存主题
			if (!this.mThemePackageName.equals(str)) {
				PreferenceManager.getDefaultSharedPreferences(context).edit()
						.putString("theme", this.mThemePackageName).commit();
				for (int i = 0; i < mCallbacks.size(); i++) {
					WeakReference<ThemeCallback> refer = this.mCallbacks.get(i);
					if (refer != null) {
						ThemeCallback callback = refer.get();
						if (callback != null) {
							// 重新应用主题
							callback.applyTheme();
						}
					}
				}
			}
		}
	}

	/**
	 * 注册主题回调---表示该界面可以使用主题
	 * 
	 * @param callback
	 */
	public void registerThemeCallback(ThemeCallback callback) {
		mCallbacks.add(new WeakReference<ThemeCallback>(callback));
	}

	/**
	 * 取消注册主题回调---activity销毁时可以取消主题应用---让其失去引用利于回收
	 * 
	 * @param callback
	 */
	public void unRegisterThemeCallback(ThemeCallback callback) {
		ThemeCallback temp = null;
		for (int i = 0; i < mCallbacks.size(); i++) {
			temp = mCallbacks.get(i).get();
			if ((temp != null) && temp.equals(callback)) {
				mCallbacks.remove(i);
				return;
			}
		}
	}

	/**
	 * 根据资源id返回相应对象
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	private Object getThemeResource(Context context, int resId) {
		Resources res = context.getResources();
		String type = res.getResourceTypeName(resId);
		String ident = "";
		// 如果是夜间模式
		if (THEME_NIGHT.equals(this.mThemePackageName)) {
			// 根据条目名称返回给定资源标识符。--装配夜色主题
			ident = "night_" + res.getResourceEntryName(resId);
			return getResourceValueByName(this.mThemeContext, ident, type);
		} else if (THEME_DEFAULT.equals(this.mThemePackageName)) {
			ident = res.getResourceEntryName(resId);
			return getResourceValueByName(this.mThemeContext, ident, type);
		}
		return getDefaultResourceValue(context, resId);
	}

	/**
	 * 该方法根据type类型返回不同的对象type==>drawable时返回Drawable/type==>
	 * color时返回ColorStateList
	 * 
	 * @param resource
	 * @param resId
	 * @param type
	 * @return
	 */
	private Object getResourceValue(Resources resource, int resId, String type) {
		if ("drawable".equals(type)) {
			return resource.getDrawable(resId);
		} else if ("color".equals(type)) {
			return resource.getColorStateList(resId);
		}
		return null;
	}

	/**
	 * 根据标识符id返回默认的对象
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	private Object getDefaultResourceValue(Context context, int resId) {
		Resources resources = context.getResources();
		// 根据资源返回资源类型drawable/color...
		String type = resources.getResourceTypeName(resId);
		return getResourceValue(resources, resId, type);
	}

	/**
	 * 根据参数名、类型返回相应的对象一般用于返回Drawable/ColorStateList对象
	 * 
	 * @param context
	 * @param param
	 * @param type
	 * @return
	 */
	private Object getResourceValueByName(Context context, String param,
			String type) {
		String str = param.toLowerCase().trim();
		Resources resource = context.getResources();
		int ident = resource.getIdentifier(str, type, context.getPackageName());
		if (ident != 0) {
			return getResourceValue(resource, ident, type);
		}
		return null;
	}

	/**
	 * 主界面中要使用主题背景则应该实现该方法---为界面控件设置背景以及设置颜色
	 * 
	 * @author chengkai
	 * 
	 */
	public static interface ThemeCallback {
		public void applyTheme();

		public Context getContext();
	}
}
