package org.lance.fireworks;

import java.io.InputStream;
import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

/**
 * 显示烟花视图
 * 
 * @author lance
 * 
 */
public class FireworksView extends View {

	public static final int ID_SOUND_UP = 0;
	public static final int ID_SOUND_BLOW = 1;
	public static final int ID_SOUND_MULTIPLE = 2;
	final static int TIME = 5; // 圈数
	private SoundPlay soundPlay;

	private Vector<Fireworks> fList = new Vector<Fireworks>();

	LittleFireworks[] fires = new LittleFireworks[200];
	private FireworksFactory factory;

	boolean running = true;
	Bitmap backgroundBitmap;
	Context mContext;

	public FireworksView(Context context) {
		super(context);
		new FireThread().start();
		mContext = context;
		soundPlay = new SoundPlay();
		soundPlay.initSounds(context);
	}

	public FireworksView(Context context, int bgID) {
		super(context);
		new FireThread().start();
		mContext = context;
		backgroundBitmap = loadBitmap(mContext, bgID);
		soundPlay = new SoundPlay();
		soundPlay.initSounds(context);
	}

	public FireworksView(Context context, int bgID, FireworksFactory factory) {
		super(context);
		new FireThread().start();
		this.factory = factory;
		mContext = context;
		backgroundBitmap = loadBitmap(mContext, bgID);
		soundPlay = new SoundPlay();
		soundPlay.initSounds(context);
	}

	// public void initSound(Context context) {
	// soundPlay.loadSound(context, R.raw.up, ID_SOUND_UP);
	// soundPlay.loadSound(context, R.raw.blow, ID_SOUND_BLOW);
	// soundPlay.loadSound(context, R.raw.multiple, ID_SOUND_MULTIPLE);
	// }

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Rect src = new Rect(0, 0, backgroundBitmap.getWidth(),
				backgroundBitmap.getHeight());
		int width = this.getWidth();
		int height = this.getHeight();
		Rect dst = new Rect(0, 0, width, height);
		canvas.drawBitmap(backgroundBitmap, src, dst, null);
		synchronized (fList) {
			for (int i = 0; i < fList.size(); i++) {
				fList.get(i).draw(canvas, fList);
			}
		}
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Fireworks fire = null;
			int rand = (int) (Math.random() * 99);
			fire = factory.createFire(mContext, rand, (int) event.getX(),
					(int) event.getY());
			synchronized (fList) {
				fList.add(fire);
				soundPlay.play(ID_SOUND_UP, 0);
			}
		}
		return true;
	}

	public FireworksFactory getFactory() {
		return factory;
	}

	public void setFactory(FireworksFactory factory) {
		this.factory = factory;
	}

	public Bitmap getBackgroundBitmap() {
		return backgroundBitmap;
	}

	public void setBackgroundBitmap(Bitmap backgroundBitmap) {
		this.backgroundBitmap = backgroundBitmap;
	}

	public SoundPlay getSoundPlay() {
		return soundPlay;
	}

	public void setSoundPlay(SoundPlay soundPlay) {
		this.soundPlay = soundPlay;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Bitmap loadBitmap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public Bitmap resizeImage(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int newWidth = w;
		int newHeight = h;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap tmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
				true);
		return tmp;
	}

	class FireThread extends Thread {
		// 新建一个进程类来处理重画
		// 用于控制烟火在空中滞留的时间
		int times = 0;

		public void run() {
			Fireworks fire = null;
			while (running) {
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				synchronized (fList) {
					// 防止画面的烟花个数多于50个
					while (fList.size() > 50) {
						for (int i = 0; i < 10; i++) {
							fList.remove(i);
						}
					}
					// 自动添加烟火
					if (fList.size() <= 2) {
						Fireworks tmp = null;
						int rand = (int) (Math.random() * 99);
						Random random = new Random();
						tmp = factory.createFire(mContext, rand,
								random.nextInt(480), 50 + random.nextInt(300));
						fList.add(tmp);
					}
				}
				for (int i = 0; i < fList.size(); i++) {
					fire = (Fireworks) fList.get(i);
					if (fire.state == 1 && !fire.isBlast()) {
						fire.rise();
					}
					// 如果是whetherBlast()返回的是true，那么就把该fire的state设置为2
					else if (fire.state == 1 && fire.state != 2) {
						fire.state = 2;
						soundPlay.play(ID_SOUND_BLOW, 0);
					} else if (fire.state == 3) {
					}
					// 规定，每个爆炸点最多是TIME圈，超过就会消失
					if (fire.circle >= TIME) {
						// 在空中滞留一秒才消失
						if (times >= 10) {
							fire.state = 4;
							times = 0;
						} else {
							times++;
						}
					}
				}
			}
		}
	}
}
