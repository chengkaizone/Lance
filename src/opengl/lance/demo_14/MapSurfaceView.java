package opengl.lance.demo_14;

import java.util.ArrayList;
import java.util.HashMap;

import org.lance.main.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MapSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	DrawLine line;
	Sample14_1 mActivity;
	Paint paint;
	final float span = 15.7f;
	final int LJCD_COUNT = 6;
	int LjcdCount;

	public MapSurfaceView(Sample14_1 mActivity) {
		super(mActivity);
		this.mActivity = mActivity;
		this.getHolder().addCallback(this);
		paint = new Paint();
		paint.setAntiAlias(true);
		line = new DrawLine(this, getHolder());
	}

	@Override
	public void onDraw(Canvas canvas) {
		// 获取地图
		int[][] map = line.map;
		// 地图行数
		int row = map.length;
		// 地图列数
		int col = map[0].length;
		// 设置背景
		canvas.drawARGB(255, 128, 128, 128);
		// 画布宽度
		int width = (int) span * map.length;
		// 画布长度
		int height = (int) span * map[0].length;
		// 设置画布宽高
		// canvas.setViewport(width, height);
		// 开始绘制地图
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (map[i][j] == 1) {
					paint.setColor(Color.BLACK);
				} else {
					paint.setColor(Color.WHITE);
				}
				canvas.drawRect(2 + j * (span + 1), 2 + i * (span + 1), 2 + j
						* (span + 1) + span, 2 + i * (span + 1) + span, paint);
			}
		}
		// 更具路径缓存绘制路径
		ArrayList<int[][]> searchProcess = line.searchProcess;
		for (int k = 0; k < searchProcess.size(); k++) {
			int[][] edge = searchProcess.get(k);
			paint.setColor(Color.BLACK);
			canvas.drawLine(edge[0][0] * (span + 1) + span / 2 + 2, edge[0][1]
					* (span + 1) + span / 2 + 2, edge[1][0] * (span + 1) + span
					/ 2 + 2, edge[1][1] * (span + 1) + span / 2 + 2, paint);
		}
		if (mActivity.mySurfaceView.line.algorithmId == 0
				|| mActivity.mySurfaceView.line.algorithmId == 1
				|| mActivity.mySurfaceView.line.algorithmId == 2) {
			if (line.pathFlag) {
				HashMap<String, int[][]> hm = line.hm;
				int[] temp = line.target;
				int count = 0;
				while (true) {
					int[][] tempA = hm.get(temp[0] + ":" + temp[1]);
					paint.setColor(Color.BLACK);
					paint.setStrokeWidth(3);
					canvas.drawLine(tempA[0][0] * (span + 1) + span / 2 + 2,
							tempA[0][1] * (span + 1) + span / 2 + 2,
							tempA[1][0] * (span + 1) + span / 2 + 2,
							tempA[1][1] * (span + 1) + span / 2 + 2, paint);
					count++;
					if (tempA[1][0] == line.source[0]
							&& tempA[1][1] == line.source[1]) {
						break;
					}
					temp = tempA[1];
				}
				LjcdCount = count;
				// 设置步数
				mActivity.hd.sendEmptyMessage(LJCD_COUNT);
			}
		} else if (mActivity.mySurfaceView.line.algorithmId == 3
				|| mActivity.mySurfaceView.line.algorithmId == 4) {
			if (line.pathFlag) {
				HashMap<String, ArrayList<int[][]>> hmPath = line.hmPath;
				ArrayList<int[][]> alPath = hmPath.get(line.target[0] + ":"
						+ line.target[1]);
				for (int[][] tempA : alPath) {
					paint.setColor(Color.BLACK);
					paint.setStrokeWidth(3);
					canvas.drawLine(tempA[0][0] * (span + 1) + span / 2 + 2,
							tempA[0][1] * (span + 1) + span / 2 + 2,
							tempA[1][0] * (span + 1) + span / 2 + 2,
							tempA[1][1] * (span + 1) + span / 2 + 2, paint);
				}
				LjcdCount = alPath.size();
				mActivity.hd.sendEmptyMessage(LJCD_COUNT);
			}
		}
		Bitmap bitmapTmpS = BitmapFactory.decodeResource(getResources(),
				R.drawable.source);
		canvas.drawBitmap(bitmapTmpS, line.source[0] * (span + 1),
				line.source[1] * (span + 1), paint);
		Bitmap bitmapTmpT = BitmapFactory.decodeResource(getResources(),
				R.drawable.target);
		canvas.drawBitmap(bitmapTmpT, line.target[0] * (span + 1),
				line.target[1] * (span + 1), paint);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Canvas canvas = holder.lockCanvas();
		try {
			synchronized (holder) {
				onDraw(canvas);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null) {
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	public void repaint(SurfaceHolder holder) {
		Canvas canvas = holder.lockCanvas();
		try {
			synchronized (holder) {
				onDraw(canvas);
			}
			;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null) {
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

}
