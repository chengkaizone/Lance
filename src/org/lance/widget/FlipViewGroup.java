package org.lance.widget;

import static javax.microedition.khronos.opengles.GL10.GL_AMBIENT;
import static javax.microedition.khronos.opengles.GL10.GL_BACK;
import static javax.microedition.khronos.opengles.GL10.GL_BLEND;
import static javax.microedition.khronos.opengles.GL10.GL_CCW;
import static javax.microedition.khronos.opengles.GL10.GL_CLAMP_TO_EDGE;
import static javax.microedition.khronos.opengles.GL10.GL_COLOR_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_CULL_FACE;
import static javax.microedition.khronos.opengles.GL10.GL_DEPTH_BUFFER_BIT;
import static javax.microedition.khronos.opengles.GL10.GL_DEPTH_TEST;
import static javax.microedition.khronos.opengles.GL10.GL_FLOAT;
import static javax.microedition.khronos.opengles.GL10.GL_LEQUAL;
import static javax.microedition.khronos.opengles.GL10.GL_LIGHT0;
import static javax.microedition.khronos.opengles.GL10.GL_LIGHTING;
import static javax.microedition.khronos.opengles.GL10.GL_LINEAR;
import static javax.microedition.khronos.opengles.GL10.GL_MODELVIEW;
import static javax.microedition.khronos.opengles.GL10.GL_NICEST;
import static javax.microedition.khronos.opengles.GL10.GL_ONE_MINUS_SRC_ALPHA;
import static javax.microedition.khronos.opengles.GL10.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.microedition.khronos.opengles.GL10.GL_POSITION;
import static javax.microedition.khronos.opengles.GL10.GL_PROJECTION;
import static javax.microedition.khronos.opengles.GL10.GL_RGBA;
import static javax.microedition.khronos.opengles.GL10.GL_SMOOTH;
import static javax.microedition.khronos.opengles.GL10.GL_SRC_ALPHA;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_2D;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_COORD_ARRAY;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_MAG_FILTER;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_MIN_FILTER;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_WRAP_S;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_WRAP_T;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLES;
import static javax.microedition.khronos.opengles.GL10.GL_UNSIGNED_BYTE;
import static javax.microedition.khronos.opengles.GL10.GL_UNSIGNED_SHORT;
import static javax.microedition.khronos.opengles.GL10.GL_VERTEX_ARRAY;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.LinkedList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * flipboard特效 作为一个容器使用---使用时只需要添加子视图
 * 
 * @author lance
 * 
 */
public class FlipViewGroup extends ViewGroup {
	public static final String TAG="FlipViewGroup";
	
	private static final int MSG_SURFACE_CREATED = 1;
	private static LinkedList<View> flipViews = new LinkedList<View>();
	private static int currentView = 1;
	// 视图总数
	private static int size;

	private GLSurfaceView surfaceView;
	private static FlipRenderer renderer;

	private int width;
	private int height;
	// 是否自动轻弹
	private boolean autoFlip = false;

	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == MSG_SURFACE_CREATED) {
				width = 0;
				height = 0;
				requestLayout();
				return true;
			}
			return false;
		}
	});

	public FlipViewGroup(Context context) {
		super(context);
		setupSurfaceView();
	}

	private void setupSurfaceView() {
		surfaceView = new GLSurfaceView(getContext());
		renderer = new FlipRenderer(this);
		surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		surfaceView.setZOrderOnTop(true);
		surfaceView.setRenderer(renderer);
		surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		// 主动渲染
		surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		// 主要使用GLSurfaceView显示
		addView(surfaceView);
	}

	public GLSurfaceView getSurfaceView() {
		return surfaceView;
	}

	public FlipRenderer getRenderer() {
		return renderer;
	}

	public void addFlipView(View v) {
		flipViews.add(v);
		addView(v);
	}

	@Override // 执行2
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		size = flipViews.size();
		for (View child : flipViews) {
			child.layout(0, 0, r - l, b - t);
		}

		if (changed || width == 0) {
			int w = r - l;
			int h = b - t;
			surfaceView.layout(0, 0, w, h);
			if (width != w || height != h) {
				width = w;
				height = h;
				if (size >= 2) {
					currentView = 1;
					View frontView = flipViews.get(currentView);
					View backView = flipViews.get(currentView - 1);
					renderer.updateTexture(frontView, backView);
					frontView.setVisibility(View.INVISIBLE);
					backView.setVisibility(View.INVISIBLE);
				}
			}
		}
	}

	@Override // 执行1
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		for (View child : flipViews) {
			child.measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	// 设置自动轻弹
	public void setAutoFlip(boolean bool) {
		FlipCards.setAutoFlip(bool);
	}

	public void onResume() {
		surfaceView.onResume();
	}

	public void onPause() {
		surfaceView.onPause();
	}

	/**
	 * 重新载入纹理
	 */
	public void reloadTexture() {
		handler.sendMessage(Message.obtain(handler, MSG_SURFACE_CREATED));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return renderer.getCards().handleTouchEvent(event);
	}

	public static class FlipRenderer implements GLSurfaceView.Renderer {

		private FlipViewGroup flipViewGroup;
		private FlipCards cards;
		private boolean created = false;

		public FlipRenderer(FlipViewGroup flipViewGroup) {
			this.flipViewGroup = flipViewGroup;
			cards = new FlipCards(flipViewGroup);
		}

		public FlipCards getCards() {
			return cards;
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			gl.glShadeModel(GL_SMOOTH);
			gl.glClearDepthf(1.0f);
			gl.glEnable(GL_DEPTH_TEST);
			gl.glDepthFunc(GL_LEQUAL);
			gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
			created = true;
			cards.invalidateTexture();
			flipViewGroup.reloadTexture();
		}

		public static float[] light0Position = { 0, 0, 100f, 0f };

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL_PROJECTION);
			gl.glLoadIdentity();
			float fovy = 20f;
			float eyeZ = height / 2f / (float) Math.tan(d2r(fovy / 2));
			//透视投影 fovy眼镜睁开的角度 aspect实际窗口的纵横比
			GLU.gluPerspective(gl, fovy, (float) width / (float) height, 15.5f,
					Math.max(2500.0f, eyeZ));
			gl.glMatrixMode(GL_MODELVIEW);
			gl.glLoadIdentity();
			
			GLU.gluLookAt(gl, width / 2.0f, height / 2f, eyeZ, width / 2.0f,
					height / 2.0f, 0.0f, 0.0f, 1.0f, 0.0f);
			
			gl.glEnable(GL_LIGHTING);
			gl.glEnable(GL_LIGHT0);
			float lightAmbient[] = new float[] { 3.5f, 3.5f, 3.5f, 1f };
			gl.glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbient, 0);
			light0Position = new float[] { 0, 0, eyeZ, 0f };
			gl.glLightfv(GL_LIGHT0, GL_POSITION, light0Position, 0);

		}

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			cards.draw(gl);
		}

		public void updateTexture(View frontView, View backView) {
			if (created) {
				cards.reloadTexture(frontView, backView);
			}
		}

		public static void checkError(GL10 gl) {
			int error = gl.glGetError();
			if (error != 0) {
				throw new RuntimeException(GLU.gluErrorString(error));
			}
		}

		public float d2r(float degree) {
			return degree * (float) Math.PI / 180f;
		}
	}

	//纹理
	public static class Texture {
		private int[] id = { 0 };
		private int width, height;
		private int contentWidth, contentHeight;
		private boolean destroyed = false;

		public static Texture createTexture(Bitmap bitmap, GL10 gl) {
			Texture t = new Texture();
			//这里将w,h处理成2的n次方 highestOneBit取高位的补码
			int w = Integer.highestOneBit(bitmap.getWidth() - 1) << 1;
			int h = Integer.highestOneBit(bitmap.getHeight() - 1) << 1;

			t.contentWidth = bitmap.getWidth();
			t.contentHeight = bitmap.getHeight();
			t.width = w;
			t.height = h;

			gl.glGenTextures(1, t.id, 0);
			gl.glBindTexture(GL_TEXTURE_2D, t.id[0]);
			gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA,
					GL_UNSIGNED_BYTE, null);
			GLUtils.texSubImage2D(GL_TEXTURE_2D, 0, 0, 0, bitmap);
			return t;
		}
		//回收纹理占用的内存
		public void destroy(GL10 gl) {
			if (id[0] != 0){
				gl.glDeleteTextures(1, id, 0);
			}
			id[0] = 0;
			destroyed=true;
		}

		public boolean isDestroyed() {
			return destroyed;
		}

		public int[] getId() {
			return id;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		public int getContentWidth() {
			return contentWidth;
		}

		public int getContentHeight() {
			return contentHeight;
		}
	}

	public static class FlipCards {
		// 加速度
		private static final float ACCELERATION = 0.618f;
		private static final float TIP_SPEED = 1f;
		private static final float MOVEMENT_RATE = 1.5f;
		// 最大转动角度
		private static final int MAX_TIP_ANGLE = 60;
		// 轻弹状态
		private static final int STATE_TIP = 0;
		// 触摸状态
		private static final int STATE_TOUCH = 1;
		// 自动轻弹状态
		private static final int STATE_AUTO_ROTATE = 2;
		private FlipViewGroup flipViewGroup;
		private Texture frontTexture;//第一页的位图的纹理
		private Bitmap frontBitmap;//第一页的位图
		private Texture backTexture;
		private Bitmap backBitmap;
		//每个面都有4个卡
		private Card frontTopCard;//前一页的这张卡片上半部分
		private Card frontBottomCard;
		private Card backTopCard;
		private Card backBottomCard;
		private int flipAngle = 0;//活动卡旋转角度,底部->顶部(0-180)
		// 向前翻还是向后翻
		private boolean forward = true;
		private static boolean autoFlip = false;
		// private boolean animating = false;
		// 动画帧---控制加速度
		private int animatedFrame = 0;
		private int state = STATE_TIP;

		public FlipCards(FlipViewGroup flipViewGroup) {
			this.flipViewGroup = flipViewGroup;
			frontTopCard = new Card();
			frontBottomCard = new Card();
			backTopCard = new Card();
			backBottomCard = new Card();
			frontBottomCard.setAxis(Card.AXIS_TOP);
			backTopCard.setAxis(Card.AXIS_BOTTOM);
		}

		public static boolean isAutoFlip() {
			return autoFlip;
		}

		public static void setAutoFlip(boolean autoFlip) {
			FlipCards.autoFlip = autoFlip;
		}

		public void reloadTexture(View frontView, View backView) {
			frontBitmap = takeScreenshot(frontView);
			backBitmap = takeScreenshot(backView);
		}

		/**
		 * 旋转到指定角度
		 * 
		 * @param delta 每次旋转delta度
		 */
		public void rotateBy(float delta) {
			//Log.i(TAG, "start---->"+angle);
			flipAngle += delta;
			if (flipAngle > 180) {
				flipAngle = 180;
			} else if (flipAngle < 0) {
				flipAngle = 0;
			}
			//Log.i(TAG, "end---->"+angle);
		}

		public int getState() {
			return state;
		}

		public void setState(int state) {
			if (this.state != state) {
				this.state = state;
				animatedFrame = 0;
			}
		}

		// 该方法会被连续调用
		public void draw(GL10 gl) {
			applyTexture(gl);
			if (frontTexture == null) {
				return;
			}
			switch (state) {
			case STATE_TIP: {
				if (flipAngle >= 180) {
					forward = false;
				} else if (flipAngle <= 0) {
					forward = true;
				}
				if (autoFlip) {
					rotateBy((forward ? TIP_SPEED : -TIP_SPEED));
				}
				// 90-120向前翻
				if (flipAngle > 90 && flipAngle <= 180 - MAX_TIP_ANGLE) {
					forward = true;
					// 60-90向后翻
				} else if (flipAngle < 90 && flipAngle >= MAX_TIP_ANGLE) {
					forward = false;
				}
			}
				break;
			case STATE_TOUCH:
				break;
			case STATE_AUTO_ROTATE: {
				animatedFrame++;//这里计算旋转加速
				rotateBy((forward ? ACCELERATION : -ACCELERATION)* animatedFrame);
				// 在此处设置视图
				if (flipAngle >= 180 || flipAngle <= 0) {
					setState(STATE_TIP);
				}
			}
				break;
			default:
				Log.i(TAG,"Invalid state: " + state);
				break;
			}
			frontTopCard.draw(gl);
			backBottomCard.draw(gl);
			//主要是根据角度绘制旋转的哪一张卡片
			if (flipAngle < 90) {
				frontBottomCard.setCardAngle(flipAngle);
				frontBottomCard.draw(gl);
			} else {
				backTopCard.setCardAngle(180 - flipAngle);
				backTopCard.draw(gl);
			}
		}

		// TODO 关键在于这个方法出现问题
		private void applyTexture(GL10 gl) {
			if (frontBitmap != null) {
				if (frontTexture != null) {//
					frontTexture.destroy(gl);
				}
				frontTexture = Texture.createTexture(frontBitmap, gl);
				frontTopCard.setTexture(frontTexture);
				frontBottomCard.setTexture(frontTexture);
				Log.i(TAG, "front--->"+frontBitmap.getWidth()+" "+frontBitmap.getHeight()+" "
						+frontTexture.getWidth()+" "+frontTexture.getHeight());
				//第一页的上半部分的顶点坐标
				frontTopCard.setCardVertices(new float[] { 
						0f, frontBitmap.getHeight(), 0f, 
						0f, frontBitmap.getHeight() / 2.0f, 0f, 
						frontBitmap.getWidth(), frontBitmap.getHeight() / 2f,0f,
						frontBitmap.getWidth(), frontBitmap.getHeight(), 0f 
						});
				//第一页的上半部分的纹理坐标
				frontTopCard.setTextureCoordinates(new float[] {
						0f, 0f,
						0f, frontBitmap.getHeight() / 2f
								/ (float) frontTexture.getHeight(),
						frontBitmap.getWidth()
								/ (float) frontTexture.getWidth(),
						frontBitmap.getHeight() / 2f
								/ (float) frontTexture.getHeight(),
						frontBitmap.getWidth()
								/ (float) frontTexture.getWidth(), 0f 
						});
				//第一页下半部分的顶点坐标
				frontBottomCard.setCardVertices(new float[] { 
						0f,frontBitmap.getHeight() / 2f, 0f,
						0f, 0f, 0f,
						frontBitmap.getWidth(), 0f, 0f,
						frontBitmap.getWidth(), frontBitmap.getHeight() / 2f, 0f 
						});
				//第一页下半部分的纹理坐标
				frontBottomCard.setTextureCoordinates(new float[] {
						0f, frontBitmap.getHeight() / 2f
								/ (float) frontTexture.getHeight(),
						0f, frontBitmap.getHeight()
								/ (float) frontTexture.getHeight(),
						frontBitmap.getWidth()
								/ (float) frontTexture.getWidth(),
						frontBitmap.getHeight()
								/ (float) frontTexture.getHeight(),
						frontBitmap.getWidth()
								/ (float) frontTexture.getWidth(),
						frontBitmap.getHeight() / 2f
								/ (float) frontTexture.getHeight() 
						});
				FlipRenderer.checkError(gl);

				frontBitmap.recycle();
				frontBitmap = null;
			}

			if (backBitmap != null) {
				if (backTexture != null) {
					backTexture.destroy(gl);
				}
				backTexture = Texture.createTexture(backBitmap, gl);
				backTopCard.setTexture(backTexture);
				backBottomCard.setTexture(backTexture);
				Log.i(TAG, "back--->"+backBitmap.getWidth()+" "+backBitmap.getHeight()+" "
						+backTexture.getWidth()+" "+backTexture.getHeight());
				//第二页的上半部分顶点坐标
				backTopCard.setCardVertices(new float[] {
						0f,backBitmap.getHeight(), 0f,
						0f, backBitmap.getHeight() / 2.0f, 0f,
						backBitmap.getWidth(), backBitmap.getHeight() / 2f, 0f,
						backBitmap.getWidth(), backBitmap.getHeight(), 0f 
						});
				//第二页的上半部分的纹理坐标
				backTopCard.setTextureCoordinates(new float[] {
						0f, 0f, 0f,
						backBitmap.getHeight() / 2f
								/ (float) backTexture.getHeight(),
						backBitmap.getWidth() / (float) backTexture.getWidth(),
						backBitmap.getHeight() / 2f
								/ (float) backTexture.getHeight(),
						backBitmap.getWidth() / (float) backTexture.getWidth(), 0f 
						});
				//第二页的下半部分的顶点坐标
				backBottomCard.setCardVertices(new float[] { 0f,
						backBitmap.getHeight() / 2f, 0f,
						0f, 0f, 0f,
						backBitmap.getWidth(), 0f, 0f,
						backBitmap.getWidth(), backBitmap.getHeight() / 2f, 0f
						});
				//第二页的下半部分的纹理坐标
				backBottomCard.setTextureCoordinates(new float[] {
						0f, backBitmap.getHeight() / 2f
								/ (float) backTexture.getHeight(),
						0f, backBitmap.getHeight()
								/ (float) backTexture.getHeight(),
						backBitmap.getWidth() / (float) backTexture.getWidth(),
						backBitmap.getHeight()
								/ (float) backTexture.getHeight(),
						backBitmap.getWidth() / (float) backTexture.getWidth(),
						backBitmap.getHeight() / 2f
								/ (float) backTexture.getHeight() 
						});
				FlipRenderer.checkError(gl);
				backBitmap.recycle();
				backBitmap = null;
			}
		}

		public void invalidateTexture() {
			// Texture is vanished when the gl context is gone, no need to
			// delete it explicitly
			frontTexture = null;
			backTexture = null;
		}

		private float lastY = -1;

		public boolean handleTouchEvent(MotionEvent event) {
			if (frontTexture == null) {
				return false;
			}
			float delta;
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				lastY = event.getY();
				// 此状态不渲染
				setState(STATE_TOUCH);
				return true;
			case MotionEvent.ACTION_MOVE:
				// 不断计算距离
				delta = lastY - event.getY();
				float resultDelta=180 * delta / frontTexture.getContentHeight() * MOVEMENT_RATE;
				// 旋转
				rotateBy(resultDelta);
				lastY = event.getY();
				//Log.i(TAG, delta+"   "+resultDelta+"   "+lastY);
				return true;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				delta = lastY - event.getY();
				rotateBy(180 * delta / frontTexture.getContentHeight()
						* MOVEMENT_RATE);
				// 决定向前翻还是向后翻
				if (flipAngle < 90) {
					forward = false;
				} else {
					forward = true;
				}
				setState(STATE_AUTO_ROTATE);
				return true;
			}
			return false;
		}

		// 获取屏幕快照
		public Bitmap takeScreenshot(View view) {
			// assert view.getWidth() > 0 && view.getHeight() > 0;
			Bitmap.Config config = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
					view.getHeight(), config);
			Canvas canvas = new Canvas(bitmap);
			// 这里可以将视图的显示内容绘制到画布上；此时的位图视图画布上的内容
			view.draw(canvas);
			return bitmap;
		}
	}

	/** 卡片 */
	public static class Card {
		public static final int AXIS_TOP = 0;
		public static final int AXIS_BOTTOM = 1;
		private float cardVertices[];
		private short[] indices = { 0, 1, 2, 0, 2, 3 };
		// 顶点缓冲
		private FloatBuffer vertexBuffer;
		// 索引缓冲
		private ShortBuffer indexBuffer;
		// 纹理坐标值
		private float textureCoordinates[];
		// 纹理缓冲
		private FloatBuffer textureBuffer;
		private Texture texture;
		private float cardAngle = 0f;
		private int axis = AXIS_TOP;
		private boolean dirty = false;

		public Texture getTexture() {
			return texture;
		}

		public void setTexture(Texture texture) {
			this.texture = texture;
		}

		public float[] getCardVertices() {
			return cardVertices;
		}

		public short[] getIndices() {
			return indices;
		}

		public ShortBuffer getIndexBuffer() {
			return indexBuffer;
		}

		public void setCardVertices(float[] cardVertices) {
			this.cardVertices = cardVertices;
			this.dirty = true;
		}

		public void setTextureCoordinates(float[] textureCoordinates) {
			this.textureCoordinates = textureCoordinates;
			this.dirty = true;
		}

		
		public void setCardAngle(float angle) {
			this.cardAngle = angle;
		}

		public void setAxis(int axis) {
			this.axis = axis;
		}
		// TODO 问题可能出现在angle过度旋转了
		public void draw(GL10 gl) {
			if (dirty) {
				updateVertices();
			}
			if (cardVertices == null) {
				return;
			}
			gl.glFrontFace(GL_CCW);
			gl.glEnable(GL_CULL_FACE);
			gl.glCullFace(GL_BACK);
			gl.glEnableClientState(GL_VERTEX_ARRAY);
			gl.glEnable(GL_BLEND);
			gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			gl.glColor4f(1f, 1.0f, 1f, 1.0f);
			if (isValidTexture(texture)) {
				gl.glEnable(GL_TEXTURE_2D);
				gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
				gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,
						GL_CLAMP_TO_EDGE);
				gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,
						GL_CLAMP_TO_EDGE);
				gl.glTexCoordPointer(2, GL_FLOAT, 0, textureBuffer);
				gl.glBindTexture(GL_TEXTURE_2D, texture.getId()[0]);
			}
			checkError(gl);
			gl.glPushMatrix();
			if (cardAngle > 0) {
				if (axis == AXIS_TOP) {
					gl.glTranslatef(0, cardVertices[1], 0f);
					gl.glRotatef(-cardAngle, 1f, 0f, 0f);
					gl.glTranslatef(0, -cardVertices[1], 0f);
				} else {
					gl.glTranslatef(0, cardVertices[7], 0f);
					gl.glRotatef(cardAngle, 1f, 0f, 0f);
					gl.glTranslatef(0, -cardVertices[7], 0f);
				}
			}
			gl.glVertexPointer(3, GL_FLOAT, 0, vertexBuffer);
			gl.glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_SHORT,
					indexBuffer);
			checkError(gl);
			gl.glPopMatrix();
			
			if (isValidTexture(texture)) {
				gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
				gl.glDisable(GL_TEXTURE_2D);
			}
			if (cardAngle > 0) {
				gl.glDisable(GL_LIGHTING);
				gl.glDisable(GL_DEPTH_TEST);
				if (axis == AXIS_TOP) {
					float w = cardVertices[9] - cardVertices[0];
					float h = (cardVertices[1] - cardVertices[4])
							* (1f - FloatMath.cos(d2r(cardAngle)));
					float z = (cardVertices[1] - cardVertices[4])
							* FloatMath.sin(d2r(cardAngle));
					float[] shadowVertices = new float[] { cardVertices[0],
							h + cardVertices[4], z, cardVertices[3],
							cardVertices[4], 0f, w, cardVertices[7], 0f, w,
							h + cardVertices[4], z };
					float alpha = 1f * (90f - cardAngle) / 90f;
					gl.glColor4f(0f, 0.0f, 0f, alpha);
					gl.glVertexPointer(3, GL_FLOAT, 0,
							toFloatBuffer(shadowVertices));
					gl.glDrawElements(GL_TRIANGLES, indices.length,
							GL_UNSIGNED_SHORT, indexBuffer);
				} else {
					float w = cardVertices[9] - cardVertices[0];
					float h = (cardVertices[1] - cardVertices[4])
							* (1f - FloatMath.cos(d2r(cardAngle)));
					float z = (cardVertices[1] - cardVertices[4])
							* FloatMath.sin(d2r(cardAngle));
					float[] shadowVertices = new float[] { cardVertices[0],
							cardVertices[1], 0f, cardVertices[3],
							cardVertices[1] - h, z, w, cardVertices[1] - h, z,
							w, cardVertices[1], 0f };
					float alpha = 1f * (90f - cardAngle) / 90f;
					gl.glColor4f(0f, 0.0f, 0f, alpha);
					gl.glVertexPointer(3, GL_FLOAT, 0,
							toFloatBuffer(shadowVertices));
					gl.glDrawElements(GL_TRIANGLES, indices.length,
							GL_UNSIGNED_SHORT, indexBuffer);
				}
				gl.glEnable(GL_DEPTH_TEST);
				gl.glEnable(GL_LIGHTING);
			}
			checkError(gl);
			gl.glDisable(GL_BLEND);
			gl.glDisableClientState(GL_VERTEX_ARRAY);
			gl.glDisable(GL_CULL_FACE);
		}

		private void updateVertices() {
			vertexBuffer = toFloatBuffer(cardVertices);
			indexBuffer = toShortBuffer(indices);
			textureBuffer = toFloatBuffer(textureCoordinates);
			dirty=false;
		}

		public void checkError(GL10 gl) {
			int error = gl.glGetError();
			if (error != 0) {
				throw new RuntimeException(GLU.gluErrorString(error));
			}
		}

		private boolean isValidTexture(Texture t) {
			return t != null && !t.isDestroyed();
		}

		private float d2r(float degree) {
			return degree * (float) Math.PI / 180f;
		}

		private FloatBuffer toFloatBuffer(float[] v) {
			ByteBuffer buf = ByteBuffer.allocateDirect(v.length * 4);
			buf.order(ByteOrder.nativeOrder());
			FloatBuffer buffer = buf.asFloatBuffer();
			buffer.put(v);
			buffer.position(0);
			return buffer;
		}

		private ShortBuffer toShortBuffer(short[] v) {
			ByteBuffer buf = ByteBuffer.allocateDirect(v.length * 2);
			buf.order(ByteOrder.nativeOrder());
			ShortBuffer buffer = buf.asShortBuffer();
			buffer.put(v);
			buffer.position(0);
			return buffer;
		}
	}
}
