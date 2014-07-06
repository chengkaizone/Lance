package org.lance.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import org.lance.main.R;
import org.lance.widget.Panel;
import org.lance.widget.Panel.OnPanelListener;

public class PanelDemo extends Activity implements OnPanelListener,
		OnClickListener {
	private Panel topPanel;
	private View shadow;
	private ImageView image;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.mypanel_main);
		topPanel = (Panel) findViewById(R.id.topPanel);
		topPanel.setOnPanelListener(this);
		shadow = findViewById(R.id.bottom_shadow);
		image = (ImageView) findViewById(R.id.handle_close);

		image.setOnClickListener(this);
		shadow.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (topPanel.isOpen()) {
				topPanel.setOpen(!topPanel.isOpen(), true);
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onPanelClosed(Panel panel) {

	}

	public void onPanelOpened(Panel panel) {

	}

	public void onClick(View arg0) {
		topPanel.setOpen(!topPanel.isOpen(), true);
	}
}
