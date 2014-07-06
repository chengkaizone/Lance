package org.lance.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 支持缩放的图片控件
 * 
 * @author lance
 * 
 */
public class MatrixImageView extends ImageView {
	private Matrix matrix;
	private Matrix savePreMatrix;
	private PointF start;// 第一个触摸点
	private PointF mid;
	private float oldDist = 1.0f;
	// 图片的三种状态
	private final int NONE = 0;// 无状态
	private final int DRAG = 1;// 拖曳状态
	private final int ZOOM = 2;// 缩放状态
	private int MODE = NONE;
	private int width = 0;
	private int height = 0;
	// private Bitmap bitmap;
	private float minScale = 0.5f;
	private float maxScale = 2.0f;

	public MatrixImageView(Context context) {
		super(context);
		Display dis = ((Activity) context).getWindowManager()
				.getDefaultDisplay();
		width = dis.getWidth();
		height = dis.getHeight();
		// bitmap = ((BitmapDrawable) this.getDrawable()).getBitmap();
		init();
	}

	public MatrixImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Display dis = ((Activity) context).getWindowManager()
				.getDefaultDisplay();
		width = dis.getWidth();
		height = dis.getHeight();
		// bitmap = ((BitmapDrawable) this.getDrawable()).getBitmap();
		// System.out.println(bitmap.getHeight()+"==="+bitmap.getWidth());
		init();
	}

	private void init() {
		matrix = new Matrix();
		savePreMatrix = new Matrix();
		start = new PointF();
		mid = new PointF();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int down=MotionEvent.ACTION_POINTER_DOWN;//5
		int up=MotionEvent.ACTION_POINTER_UP;//6
		/*
		ACTION_MASK    0x000000ff 11111111
		ACTION_DOWN    0x00000000 00000000       
		ACTION_UP      0x00000001 00000001     
		ACTION_MOVE    0x00000002 00000010
		ACTION_POINTER_DOWN      0x00000005   00000101         
		ACTION_POINTER_UP        0x00000006   00000110
		ACTION_POINTER_1_DOWN    0x00000005            
		ACTION_POINTER_1_UP      0x00000006
		ACTION_POINTER_2_DOWN    0x00000105            
		ACTION_POINTER_2_UP      0x00000106
		ACTION_POINTER_3_DOWN    0x00000205            
		ACTION_POINTER_3_UP      0x00000206
		*/
		
		// 计算动作掩码
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		switch (action) {
		// 单点按下时触发此事件
		case MotionEvent.ACTION_DOWN:
			savePreMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			MODE = DRAG;// 转换为拖曳状态
			break;
		case MotionEvent.ACTION_MOVE:
			if (MODE == DRAG) {
				matrix.set(savePreMatrix);
				// 水平移动
				matrix.postTranslate(event.getX() - start.x, event.getY()
						- start.y);
			} else if (MODE == ZOOM) {
				float newDist = calDistance(event);
				float scale = newDist / oldDist;
				matrix.set(savePreMatrix);
				matrix.postScale(scale, scale, mid.x, mid.y);

			}
			break;
		case MotionEvent.ACTION_POINTER_DOWN:// 多点状态
			oldDist = calDistance(event);// 计算两点之间的距离
			savePreMatrix.set(matrix);
			mid = this.getMidPoint(mid, event);
			MODE = ZOOM;// 转换为缩放状态
			break;
		// 同时处理这两种状态
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			MODE = NONE;
			break;
		}
		this.setImageMatrix(matrix);
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// bitmap = ((BitmapDrawable) this.getDrawable()).getBitmap();
		// System.out.println(bitmap.getHeight()+"==="+bitmap.getWidth());
		super.onDraw(canvas);
	}

	// 计算两点之间的距离
	private float calDistance(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	// 计算两点的中心点
	private PointF getMidPoint(PointF p, MotionEvent event) {
		float midX = (event.getX(0) + event.getX(1)) / 2;
		float midY = (event.getY(0) + event.getY(1)) / 2;
		p.set(midX, midY);
		return p;
	}

}
