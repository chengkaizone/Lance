package org.lance.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import org.lance.util.ThemeSettingHelper;

/**
 * ��Ҫ���ڼ̳�����ִ����ͬ�Ĳ���---�˳�������
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
			// ���ص��˷������ʾ�����Ѿ��ı�
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
		// �����Ҫ�ı�Ƥ���Ľ�������д�÷���---�������Լ�ʵ��
	}

	public ThemeSettingHelper getThemeSettingHelper() {
		return this.helper;
	}

	@Override
	public Context getContext() {
		return this;
	}

	/**
	 * ��Activity�ָ�ʱ�Ҹ�Activity��Ϊ��ϵͳ���յ��ù�onSaveInstanceState�����ǵ��ø÷���
	 * ���δ���ù�onSaveInstanceState������ô��ָ�ʱ�����ᴥ���÷���
	 */
	protected void onRestoreInstanceState(Bundle save) {
		super.onRestoreInstanceState(save);
		// ȡ������
		name = save.getString("name");
	}

	String name = "ganchengkai";

	/**
	 * ��Activity��Ϊ�ڴ�Խ�������ʱ���ø÷���---�÷�����Ҫ���ڱ�������еļ�¼����
	 */
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// ��������м�¼�����ֱ���
		outState.putString("name", name);
	}
}
