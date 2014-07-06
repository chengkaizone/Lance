package org.lance.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

/**
 * 自定义对话框
 * 
 * @author lance
 * 
 */
public class AlertDialogs extends AlertDialog {
	private Window window;

	protected AlertDialogs(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public AlertDialogs(Context context, int theme) {
		super(context, theme);
	}

	public AlertDialogs(Context context) {
		super(context);
	}

	public void setProperty(int x, int y, int w, int h) {
		window = this.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.x = x;
		params.y = y;
		params.width = LayoutParams.FILL_PARENT;
		params.height = h;
		params.alpha = 1.0f;
		params.dimAmount = 1.0f;
		params.gravity = Gravity.TOP | Gravity.CENTER;
		window.setAttributes(params);
	}

}
