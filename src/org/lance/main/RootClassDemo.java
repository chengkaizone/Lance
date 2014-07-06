package org.lance.main;

import java.util.ArrayList;
import java.util.List;

import opengl.lance.main.OpenglMainClass;

import org.lance.adapters.RootAdapter;
import org.lance.demo.AlphaImageDemo;
import org.lance.demo.AnimationsDemo;
import org.lance.demo.BarcodeDemoRoot;
import org.lance.demo.BaseActivity;
import org.lance.demo.CalendarDemo;
import org.lance.demo.CoverFlowDemo;
import org.lance.demo.CropDemo;
import org.lance.demo.CustomColumnDemo;
import org.lance.demo.D3AnimationDemo;
import org.lance.demo.FireworksDemo;
import org.lance.demo.FlipEffectDemo;
import org.lance.demo.GifViewDemo;
import org.lance.demo.ImageToSQLiteDemo;
import org.lance.demo.IndicatorDemo;
import org.lance.demo.InterpolatorsDemo;
import org.lance.demo.ListViewGroupDemo;
import org.lance.demo.MatrixImageDemo;
import org.lance.demo.MiLaucherDemo;
import org.lance.demo.NeteaseListDemo;
import org.lance.demo.OpenDoorDemo;
import org.lance.demo.PanelDemo;
import org.lance.demo.PathButtonDemo;
import org.lance.demo.PullGridViewDemo;
import org.lance.demo.PullRefreshDemo;
import org.lance.demo.QQMainDemo;
import org.lance.demo.RoundCornerDemo;
import org.lance.demo.SelectCityDemo;
import org.lance.demo.SignPageDemo;
import org.lance.demo.SimpleDragDemo;
import org.lance.demo.SwitcherDemo;
import org.lance.demo.TabDemo;
import org.lance.demo.ToastDemo;
import org.lance.demo.TurnPageDemo;
import org.lance.demo.ViewFlowDemo;
import org.lance.demo.ViewPagerDemo;
import org.lance.demo.VotePageDemo;
import org.lance.demo.WeixinChatDemo;
import org.lance.demo.ZoomImageDemo;
import org.lance.util.PrefHelper;
import org.lance.util.ThemeSettingHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import asqare.lance.main.AsqareActivity;

/**
 * ��װ����ʾ������
 * 
 * @author lance
 * 
 */
public class RootClassDemo extends BaseActivity {
	private ListView list;
	private String curTheme = "";
	private ThemeSettingHelper helper;
	private RootAdapter adapter;
	private PopupWindow popup;
	private View window;
	public View mainView;
	private int flag = 1;
	private String[] srr = { "�϶�����","�Ա��Ƽ���", "��������", "����ʵ��", "���Ž���", "���Ч��", "���ֲ�ֵ��",
			"Switcher", "�̻�����", "flip��Ч", "�����ղ�", "qq����",
			"��������","������", "��������", "��ͨ����", "�б�����ˢ��", "��������ˢ��", "��������", "3D��ת", "�����鷭ҳ",
			"��΢������", "��ť��Ч", "���ñ���ͼ", "ָʾ��", "����ǩ��", "3D����",
			"��㴥��", "��������", "gif����", "��ʱͼƬ", "���Toast", "tabѡ�", "�����ؼ�",
			"ͼƬ����", "��ά��", "opengl", "�ǹ�������", "ViewPager" };
	private Class[] crr = { MiLaucherDemo.class,ViewFlowDemo.class, NeteaseListDemo.class,
			CropDemo.class, OpenDoorDemo.class, PanelDemo.class,
			InterpolatorsDemo.class, SwitcherDemo.class, FireworksDemo.class,
			FlipEffectDemo.class, AnimationsDemo.class,
			QQMainDemo.class, SelectCityDemo.class,
			SimpleDragDemo.class,
			CustomColumnDemo.class,ListViewGroupDemo.class, PullRefreshDemo.class,
			PullGridViewDemo.class, VotePageDemo.class, D3AnimationDemo.class,
			TurnPageDemo.class, WeixinChatDemo.class, PathButtonDemo.class,
			RoundCornerDemo.class, IndicatorDemo.class, SignPageDemo.class,
			CoverFlowDemo.class,
			MatrixImageDemo.class, ZoomImageDemo.class, GifViewDemo.class,
			AlphaImageDemo.class, ToastDemo.class, TabDemo.class,
			CalendarDemo.class, ImageToSQLiteDemo.class, BarcodeDemoRoot.class,
			OpenglMainClass.class, AsqareActivity.class,ViewPagerDemo.class };

	public void onCreate(Bundle save) {
		super.onCreate(save);
		// ���ñ���
		// this.getWindow().setBackgroundDrawableResource(R.drawable.login_back);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		// ��������
		// this.getWindow().setTitle(this.getResources().getText(R.string.app_name));
		setContentView(R.layout.ck_main);
		helper = this.getThemeSettingHelper();
		curTheme = PrefHelper.getString(this, "theme",
				ThemeSettingHelper.THEME_DEFAULT);
		list = (ListView) findViewById(R.id.main_listview);
		onApplyTheme();// Ӧ������
		setAdapter();
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(new Intent(RootClassDemo.this, crr[position]));
			}
		});
		initMenu(R.layout.ck_main);
	}

	private void setAdapter() {
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < srr.length; i++) {
			data.add(srr[i]);
		}
		adapter = new RootAdapter(this, data);
		list.setAdapter(adapter);
	}

	// ���������ThemeSettingHelper.changeThemeʱ���������
	public void onApplyTheme() {
		super.onApplyTheme();
		helper.setViewIdBackgroudColor1(this, R.id.main_root,
				R.color.ck_gray_white);
		helper.setViewBackgroud(this, R.id.base_top, R.drawable.ck_red_top);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// �¼���Ӧ��Ϊ������ͼ�㣻�����ڻ��ʱ�¼��������ͼ����д����Ӧ���Σ���֮��Ӧһ��
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			System.out.println("��Ӧ�˵���");
			flag = 1;
			/**
			 * ��һ������ֻ����һ������---�ٷ��ĵ�ָ��Ҫ���Ǹ��಼��--Ƿ��popup����ʾλ�������ڲ��ֱ���������������
			 * �������Ѿ�����������������Ч
			 */
			if (mainView != null)
				popup.showAtLocation(mainView, Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void initMenu(int layoutView) {
		window = LayoutInflater.from(this).inflate(R.layout.menu_1, null);
		mainView = LayoutInflater.from(this).inflate(layoutView, null);
		LinearLayout lay1 = (LinearLayout) window
				.findViewById(R.id.menu_switch_theme);
		lay1.setOnClickListener(menuListener);
		// ��һ�������˵������ļ��� �ڶ��������˵���� �������������˵��߶ȡ�4�˵��Ƿ�ɻ�
		popup = new PopupWindow(window, -1, -2, true);
		popup.setAnimationStyle(R.style.menu_animation);
		popup.setOutsideTouchable(true);// ����popup֮����Դ���
		window.setFocusableInTouchMode(true);
		window.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_MENU:
				case KeyEvent.KEYCODE_BACK:
					System.out.println("��Ӧ��ͼ��");
					if (flag == 1) {
						flag = 2;
					} else if (flag == 2) {
						popup.dismiss();
					}
					break;
				}
				return true;
			}
		});
	}

	// �˵�����������
	View.OnClickListener menuListener = new View.OnClickListener() {
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.menu_switch_theme:
				if (curTheme.equals(ThemeSettingHelper.THEME_DEFAULT)) {
					helper.changeTheme(RootClassDemo.this,
							ThemeSettingHelper.THEME_NIGHT);
					adapter.notifyDataSetChanged();
					curTheme = ThemeSettingHelper.THEME_NIGHT;
				} else {
					helper.changeTheme(RootClassDemo.this,
							ThemeSettingHelper.THEME_DEFAULT);
					adapter.notifyDataSetChanged();
					curTheme = ThemeSettingHelper.THEME_DEFAULT;
				}
				popup.dismiss();
				break;
			}
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			if (curTheme.equals(ThemeSettingHelper.THEME_DEFAULT)) {
				helper.changeTheme(this, ThemeSettingHelper.THEME_NIGHT);
				adapter.notifyDataSetChanged();
				curTheme = ThemeSettingHelper.THEME_NIGHT;
			} else {
				helper.changeTheme(this, ThemeSettingHelper.THEME_DEFAULT);
				adapter.notifyDataSetChanged();
				curTheme = ThemeSettingHelper.THEME_DEFAULT;
			}
		}
		return super.onOptionsItemSelected(item);
	}
}