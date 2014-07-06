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
 * ��������ĸ�����
 * 
 * @author lance
 * 
 */
public class ThemeSettingHelper {
	// Ĭ������
	public static final String THEME_DEFAULT = "default_theme";
	// ҹɫ����
	public static final String THEME_NIGHT = "night_theme";
	// ����������������ӳ��
	private static WeakReference<ThemeSettingHelper> sThemeSettingsHelper;
	// ���������õļ���---��Ҫ��java���������ջ����й�---��̨��Activity������ĳʱ������---��Դ����ʱ
	private final List<WeakReference<ThemeCallback>> mCallbacks = new ArrayList<WeakReference<ThemeCallback>>();
	// ����������
	private Context mThemeContext;
	// �������
	private String mThemePackageName;

	private ThemeSettingHelper(Context context) {
		initTheme(context,
				PreferenceManager.getDefaultSharedPreferences(context)
						.getString("theme", THEME_DEFAULT));
	}

	// ��ʼ��������Ҫ��Ϊ�˻�ȡ����ĵ�ǰ����
	private void initTheme(Context context, String theme) {
		this.mThemeContext = getThemeContext(context, theme);
	}

	/**
	 * ��̬����---��ȡʵ����Ψһ����
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
				// ���ش�����������
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
	 * ΪImageView����Դλͼ
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
	 * ΪImageView����Դλͼ
	 * 
	 * @param context
	 * @param image
	 * @param resId
	 */
	public void setImageViewSrc(Context context, ImageView image, int resId) {
		image.setImageDrawable(getThemeDrawable(context, resId));
	}

	/**
	 * ΪListView���÷ָ���
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
	 * ���÷ָ���
	 * 
	 * @param context
	 * @param listView
	 * @param resId
	 */
	public void setListViewDivider(Context context, ListView listView, int resId) {
		listView.setDivider(getThemeDrawable(context, resId));
	}

	/**
	 * ΪListView���ñ���ѡ����
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
	 * ���ñ���ѡ����
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
	 * ����������ɫ
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
	 * ����������ɫ
	 * 
	 * @param context
	 * @param textView
	 * @param resId
	 */
	public void setTextViewColor(Context context, TextView textView, int resId) {
		textView.setTextColor(getThemeColor(context, resId));
	}

	/**
	 * ���ñ���--ʹ��drawable����
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
	 * ���ñ���
	 * 
	 * @param context
	 * @param view
	 * @param resId
	 */
	public void setViewBackgroud(Context context, View view, int resId) {
		view.setBackgroundDrawable(getThemeDrawable(context, resId));
	}

	/**
	 * Ϊ�ؼ����ñ���ɫ
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
	 * Ϊ�ؼ����ñ���ɫ
	 * 
	 * @param context
	 * @param view
	 * @param resId
	 */
	public void setViewBackgroudColor2(Context context, View view, int resId) {
		view.setBackgroundColor(getThemeColor(context, resId).getDefaultColor());
	}

	/**
	 * Ϊ�������ñ���---����ֻ֧��ͼƬ--��֧����ɫ
	 * 
	 * @param act
	 * @param resId
	 */
	public void setWindowBackgroud(Activity act, int resId) {
		act.getWindow().setBackgroundDrawable(getThemeDrawable(act, resId));
	}

	/**
	 * �ڲ˵��иı�������ɫ������
	 * 
	 * @param context
	 * @param theme
	 */
	public void changeTheme(Context context, String theme) {
		synchronized (this.mCallbacks) {
			String str = this.mThemePackageName;
			// ��һ����Ҫ��Ϊ�˻�ȡ��ǰ����������չ⡢ҹ��--�������ú�mThemePackageName�����¸�ֵ
			initTheme(context, theme);
			// �����ͬ�����±�������
			if (!this.mThemePackageName.equals(str)) {
				PreferenceManager.getDefaultSharedPreferences(context).edit()
						.putString("theme", this.mThemePackageName).commit();
				for (int i = 0; i < mCallbacks.size(); i++) {
					WeakReference<ThemeCallback> refer = this.mCallbacks.get(i);
					if (refer != null) {
						ThemeCallback callback = refer.get();
						if (callback != null) {
							// ����Ӧ������
							callback.applyTheme();
						}
					}
				}
			}
		}
	}

	/**
	 * ע������ص�---��ʾ�ý������ʹ������
	 * 
	 * @param callback
	 */
	public void registerThemeCallback(ThemeCallback callback) {
		mCallbacks.add(new WeakReference<ThemeCallback>(callback));
	}

	/**
	 * ȡ��ע������ص�---activity����ʱ����ȡ������Ӧ��---����ʧȥ�������ڻ���
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
	 * ������Դid������Ӧ����
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	private Object getThemeResource(Context context, int resId) {
		Resources res = context.getResources();
		String type = res.getResourceTypeName(resId);
		String ident = "";
		// �����ҹ��ģʽ
		if (THEME_NIGHT.equals(this.mThemePackageName)) {
			// ������Ŀ���Ʒ��ظ�����Դ��ʶ����--װ��ҹɫ����
			ident = "night_" + res.getResourceEntryName(resId);
			return getResourceValueByName(this.mThemeContext, ident, type);
		} else if (THEME_DEFAULT.equals(this.mThemePackageName)) {
			ident = res.getResourceEntryName(resId);
			return getResourceValueByName(this.mThemeContext, ident, type);
		}
		return getDefaultResourceValue(context, resId);
	}

	/**
	 * �÷�������type���ͷ��ز�ͬ�Ķ���type==>drawableʱ����Drawable/type==>
	 * colorʱ����ColorStateList
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
	 * ���ݱ�ʶ��id����Ĭ�ϵĶ���
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	private Object getDefaultResourceValue(Context context, int resId) {
		Resources resources = context.getResources();
		// ������Դ������Դ����drawable/color...
		String type = resources.getResourceTypeName(resId);
		return getResourceValue(resources, resId, type);
	}

	/**
	 * ���ݲ����������ͷ�����Ӧ�Ķ���һ�����ڷ���Drawable/ColorStateList����
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
	 * ��������Ҫʹ�����ⱳ����Ӧ��ʵ�ָ÷���---Ϊ����ؼ����ñ����Լ�������ɫ
	 * 
	 * @author chengkai
	 * 
	 */
	public static interface ThemeCallback {
		public void applyTheme();

		public Context getContext();
	}
}
