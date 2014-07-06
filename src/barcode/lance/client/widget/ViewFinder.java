package barcode.lance.client.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import barcode.lance.client.camera.CameraManager;
import barcode.lance.assist.ResultPoint;
import barcode.lance.assist.ResultPointCallback;

public class ViewFinder extends View {
	// 滤镜数组
	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192,
			128, 64 };
	// 动画延迟
	private static final long ANIMATION_DELAY = 80L;
	// 当前点不透明
	private static final int CURRENT_POINT_OPACITY = 0xA0;
	// 最大结果点
	private static final int MAX_RESULT_POINTS = 20;
	private Context context;
	private final Paint paint;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private final int frameColor;
	private final int laserColor;
	private final int resultPointColor;
	private int scannerAlpha;
	private final AtomicReference<List<ResultPoint>> possibleResultPoints;
	private final AtomicReference<List<ResultPoint>> lastPossibleResultPoints;

	public ViewFinder(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		paint = new Paint();
		// 模糊色
		maskColor = 0x60000000;
		// 扫描结果的颜色
		resultColor = 0xb0000000;
		// 边框色
		frameColor = 0xff000000;
		// 激光色
		laserColor = 0xffff0000;
		// 结果点的颜色
		resultPointColor = 0xc0ffff00;
		scannerAlpha = 0;
		possibleResultPoints = new AtomicReference<List<ResultPoint>>();
		lastPossibleResultPoints = new AtomicReference<List<ResultPoint>>();
		possibleResultPoints.set(new ArrayList<ResultPoint>(5));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		// Draw the exterior (i.e. outside the framing rect) darkened
		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);

		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(CURRENT_POINT_OPACITY);
			canvas.drawBitmap(resultBitmap, null, frame, paint);
		} else {

			// Draw a two pixel solid black border inside the framing rect
			paint.setColor(frameColor);
			canvas.drawRect(frame.left, frame.top, frame.right + 1,
					frame.top + 2, paint);
			canvas.drawRect(frame.left, frame.top + 2, frame.left + 2,
					frame.bottom - 1, paint);
			canvas.drawRect(frame.right - 1, frame.top, frame.right + 1,
					frame.bottom - 1, paint);
			canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1,
					frame.bottom + 1, paint);

			// Draw a red "laser scanner" line through the middle to show
			// decoding is active
			paint.setColor(laserColor);
			paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
			scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
			int middle = frame.height() / 2 + frame.top;
			canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1,
					middle + 2, paint);

			Rect previewFrame = CameraManager.get().getFramingRectInPreview();
			float scaleX = frame.width() / (float) previewFrame.width();
			float scaleY = frame.height() / (float) previewFrame.height();

			List<ResultPoint> currentPossible = possibleResultPoints.get();
			List<ResultPoint> currentLast = lastPossibleResultPoints.get();
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints.set(null);
			} else {
				possibleResultPoints.set(new ArrayList<ResultPoint>(5));
				lastPossibleResultPoints.set(currentPossible);
				paint.setAlpha(CURRENT_POINT_OPACITY);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentPossible) {
					canvas.drawCircle(frame.left
							+ (int) (point.getX() * scaleX), frame.top
							+ (int) (point.getY() * scaleY), 6.0f, paint);
				}
			}
			if (currentLast != null) {
				paint.setAlpha(CURRENT_POINT_OPACITY / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(frame.left
							+ (int) (point.getX() * scaleX), frame.top
							+ (int) (point.getY() * scaleY), 3.0f, paint);
				}
			}

			// Request another update at the animation interval, but only
			// repaint the laser line,
			// not the entire viewfinder mask.
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
					frame.right, frame.bottom);
		}
	}

	public void drawViewFinder() {
		resultBitmap = null;
		invalidate();
	}

	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		List<ResultPoint> points = possibleResultPoints.get();
		points.add(point);
		if (points.size() > MAX_RESULT_POINTS) {
			// trim it
			points.subList(0, points.size() - MAX_RESULT_POINTS / 2).clear();
		}
	}

	public static class ViewfinderResultPointCallback implements
			ResultPointCallback {

		private final ViewFinder viewFinder;

		public ViewfinderResultPointCallback(ViewFinder viewFinder) {
			this.viewFinder = viewFinder;
		}

		public void foundPossibleResultPoint(ResultPoint point) {
			viewFinder.addPossibleResultPoint(point);
		}

	}

}
