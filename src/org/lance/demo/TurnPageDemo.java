package org.lance.demo;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.lance.listener.PagePaperFactory;
import org.lance.main.R;
import org.lance.widget.PagePaper;

public class TurnPageDemo extends BaseActivity {
	private PagePaper mPageWidget;
	private String path = "/sdcard/test.txt";
	Bitmap mCurPageBitmap, mNextPageBitmap;
	Canvas mCurPageCanvas, mNextPageCanvas;
	PagePaperFactory pagefactory;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mPageWidget = new PagePaper(this);
		setContentView(mPageWidget);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		// 这个宽高是计算过密度的
		int width = metric.widthPixels;
		int height = metric.heightPixels;
		// float density = metric.density;
		// int densityDpi = metric.densityDpi;
		// System.out.println(width+"==="+height+"=="+density+"=="+densityDpi);
		mPageWidget.setmWidth(width);
		mPageWidget.setmHeight(height);
		mCurPageBitmap = Bitmap.createBitmap(mPageWidget.getmWidth(),
				mPageWidget.getmHeight(), Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap.createBitmap(mPageWidget.getmWidth(),
				mPageWidget.getmHeight(), Bitmap.Config.ARGB_8888);
		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);
		pagefactory = new PagePaperFactory(mPageWidget.getmWidth(),
				mPageWidget.getmHeight());
		pagefactory.setBgBitmap(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.ck_welcome));
		try {
			pagefactory.openbook(path);
			pagefactory.onDraw(mCurPageCanvas);
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "电子书不存在,请将test.txt放在SD卡根目录下",
					Toast.LENGTH_SHORT).show();
		}
		mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
		mPageWidget.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent e) {
				if (v.equals(mPageWidget)) {

					if (e.getAction() == MotionEvent.ACTION_DOWN) {
						mPageWidget.abortAnimation();
						mPageWidget.calcCornerXY(e.getX(), e.getY());

						pagefactory.onDraw(mCurPageCanvas);
						if (mPageWidget.DragToRight()) {
							try {
								pagefactory.prePage();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							if (pagefactory.isfirstPage()) {
								return false;
							}
							pagefactory.onDraw(mNextPageCanvas);
						} else {
							try {
								pagefactory.nextPage();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							if (pagefactory.islastPage()) {
								return false;
							}
							pagefactory.onDraw(mNextPageCanvas);
						}
						mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					}
					return mPageWidget.doTouchEvent(e);
				}
				return false;
			}
		});
	}
}