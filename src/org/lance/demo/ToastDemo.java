package org.lance.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import org.lance.main.R;
import org.lance.widget.PopupMenu;
import org.lance.widget.PopupMenu.OnMenuItemClickListener;
import org.lance.widget.Toasts;

public class ToastDemo extends BaseActivity {
	private Button btn, btn2;
	Toasts ts;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.toasts_main);
		btn = (Button) findViewById(R.id.toast_btn);
		btn2 = (Button) findViewById(R.id.toast_btn2);
		ts = new Toasts(this, "������ʾ--->", R.drawable.olympic_slidebar, true);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toasts.showHint(ToastDemo.this,
						"����һ���⵱���ݿ��ܴ�skf���϶��ټ��ɷ�������ݺͶ˿���");
				ts.show();
			}
		});
		btn2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(ToastDemo.this, "����ݼ���˯���羰��˼�����ֿ��ɽ������ɱ������skf��",
						2000).show();
				ts.hide();
			}
		});
		initMenu();
	}

	private final int[] mImgRes = { R.drawable.icon_chat_unselect,
			R.drawable.icon_aboutus_unselect, R.drawable.icon_checkin_unselect,
			R.drawable.icon_feedback_unselect, };
	private final String[] menuTexts = { "����", "����", "�˶�", "����" };

	private PopupMenu mPopupMenu;

	private void initMenu() {
		mPopupMenu = new PopupMenu(this);
		mPopupMenu.setMenuIcons(mImgRes);
		mPopupMenu.setAnimStyle(R.style.menu_animation);
		mPopupMenu.setBackgroundResource(R.drawable.circle_frame_select);// 设置背景图片
		mPopupMenu.setMenuTexts(menuTexts);
		mPopupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(AdapterView<?> parent, View view,
					int position) {
				startActivity(new Intent(ToastDemo.this, VoteSubmitDemo.class));
			}

			@Override
			public void hideMenu() {
				mPopupMenu.hide();
			}

		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_MENU:
				mPopupMenu.show();
				break;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		ts.hide();
		super.onDestroy();
	}

}
