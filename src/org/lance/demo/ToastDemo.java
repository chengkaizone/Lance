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
		ts = new Toasts(this, "永久显示--->", R.drawable.olympic_slidebar, true);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toasts.showHint(ToastDemo.this,
						"这是一个测当数据库风很大skf集合多少级可发货的身份和端口试");
				ts.show();
			}
		});
		btn2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(ToastDemo.this, "的身份几点睡看风景的思考几分宽带山看到了杀伐决断skf几",
						2000).show();
				ts.hide();
			}
		});
		initMenu();
	}

	private final int[] mImgRes = { R.drawable.icon_chat_unselect,
			R.drawable.icon_aboutus_unselect, R.drawable.icon_checkin_unselect,
			R.drawable.icon_feedback_unselect, };
	private final String[] menuTexts = { "聊天", "关于", "核对", "反馈" };

	private PopupMenu mPopupMenu;

	private void initMenu() {
		mPopupMenu = new PopupMenu(this);
		mPopupMenu.setMenuIcons(mImgRes);
		mPopupMenu.setAnimStyle(R.style.menu_animation);
		mPopupMenu.setBackgroundResource(R.drawable.circle_frame_select);// 璁剧疆鑳屾櫙鍥剧墖
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
