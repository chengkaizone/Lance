package org.lance.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import org.lance.util.ThemeSettingHelper;

/**
 * 主要用于继承子类执行相同的操作---退出、换肤
 * 
 * @author chengkai
 * 
 */
public class BaseActivity extends Activity implements
		ThemeSettingHelper.ThemeCallback {
	private boolean mPaused = true;
	private boolean mThemeChanged = false;
	private ThemeSettingHelper helper;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		this.helper = ThemeSettingHelper.getThemeSettingsHelper(this);
		this.helper.registerThemeCallback(this);
	}

	@Override
	public void applyTheme() {
		if (!this.mPaused) {
			onApplyTheme();
		} else {
			// 被回调此方法后表示主题已经改变
			this.mThemeChanged = true;
		}
	}

	protected void onResume() {
		super.onResume();
		this.mPaused = false;
		if (this.mThemeChanged) {
			this.mThemeChanged = false;
			onApplyTheme();
		}
	}

	protected void onStop() {
		super.onStop();
		this.mPaused = true;
	}

	protected void onDestroy() {
		this.helper.unRegisterThemeCallback(this);
		super.onDestroy();
	}

	public void onApplyTheme() {
		// 如果有要改变皮肤的界面则重写该方法---由子类自己实现
	}

	public ThemeSettingHelper getThemeSettingHelper() {
		return this.helper;
	}

	@Override
	public Context getContext() {
		return this;
	}

	/**
	 * 当Activity恢复时且该Activity因为被系统回收调用过onSaveInstanceState方法是调用该方法
	 * 如果未调用过onSaveInstanceState方法那么活动恢复时将不会触发该方法
	 */
	protected void onRestoreInstanceState(Bundle save) {
		super.onRestoreInstanceState(save);
		// 取出数据
		name = save.getString("name");
	}

	String name = "ganchengkai";

	/**
	 * 当Activity因为内存吃紧被回收时调用该方法---该方法主要用于保存操作中的记录数据
	 */
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// 保存操作中记录的名字变量
		outState.putString("name", name);
	}
}
