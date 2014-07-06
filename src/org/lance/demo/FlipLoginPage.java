package org.lance.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import org.lance.main.R;

public class FlipLoginPage extends Activity {
	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.flip_login);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.stack_pop_in,
					R.anim.slide_out_to_right);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
