package org.lance.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.ListView;

/**
 * ���������
 * 
 * @author lance
 * 
 */
public class ViewService {
	/**
	 * ͨ����Դid��ȡλͼ
	 * 
	 * @param context
	 * @param resid
	 * @return
	 */
	public static Bitmap getBitmap(Context context, int resid) {
		return BitmapFactory.decodeResource(context.getResources(), resid);
	}

	/**
	 * �����Ի������
	 * 
	 * @param context
	 * @param view
	 * @param msg
	 * @return
	 */
	public static Builder createDialog(Context context, View view, String msg) {
		AlertDialog.Builder b = new Builder(context);
		b.setMessage(msg);
		b.setView(view);
		b.create();
		return b;
	}

	/**
	 * ��ȡListView���Ŷ���������
	 * 
	 * @param context
	 * @return
	 */
	public static LayoutAnimationController getScale(Context context) {
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f);
		set.addAnimation(scale);
		scale.setDuration(500);
		LayoutAnimationController lac = new LayoutAnimationController(set);
		lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
		lac.setDelay(0.2f);
		return lac;
	}

	/**
	 * ��ȡListView���ź�͸���ȵĶ���������
	 * 
	 * @param context
	 * @return
	 */
	public static LayoutAnimationController getScaleAlpha(Context context) {
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f);
		AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);
		alpha.setDuration(500);
		set.addAnimation(scale);
		set.addAnimation(alpha);
		scale.setDuration(500);
		LayoutAnimationController lac = new LayoutAnimationController(set);
		lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
		lac.setDelay(0.2f);
		return lac;
	}

	/**
	 * ΪlistView�������Ŷ���
	 * 
	 * @param context
	 * @param list
	 */
	public static void setListScale(Context context, ListView... list) {
		for (ListView e : list) {
			e.setLayoutAnimation(getScale(context));
		}
	}

	/**
	 * ΪlistView�������ź�͸���ȶ���
	 * 
	 * @param context
	 * @param list
	 */
	public static void setListAlphaScale(Context context, ListView... list) {
		for (ListView e : list) {
			e.setLayoutAnimation(getScale(context));
		}
	}
}
