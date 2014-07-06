package org.lance.anim;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;

/**
 * 3d动画的旋转原理---使用一个摄像机类围绕目标拍摄； 然后将过程记录在Matrix对象中； 之后直接操作这个Matrix对象以实现3d特效
 * 
 * @author lance
 * 
 */
public class Rotate3DAnimation extends Animation {
	// 默认起始翻转角度---from
	public static final float PRE_FROMDEGREE = 0f;
	// 默认起始翻转角度---to
	public static final float PRE_TODEGREE = 90f;
	// 默认紧随翻转角度---from
	public static final float NEXT_FROMDEGREE = -90f;
	// 默认紧随翻转角度---to
	public static final float NEXT_TODEGREE = 0f;
	// 默认翻转时间
	public static final long DURATION = 500L;
	// 默认深度
	public static final float DEPTHZ = 500f;
	// 默认保持翻转后的状态
	public static final boolean FILLAFTER = true;
	// 默认加速插值器
	public static final Interpolator INTERPOLATOR = new AccelerateInterpolator();
	// 默认减速插值器
	public static final Interpolator NEXTINTERPOLATOR = new DecelerateInterpolator();
	// 摄像机类
	private Camera mCamera;
	// X中心坐标
	private float mCenterX;
	// Y中心坐标
	private float mCenterY;
	// 动画转换深度
	private float mDepthZ;
	// 起始角度
	private float mFromDegrees;
	// 结束角度
	private float mToDegrees;
	// 是否允许显示背面
	private boolean mReverse;
	// 旋转模式---默认绕Y轴旋转
	private Model model = Model.ROTATE_Y;

	public static enum Model {
		ROTATE_X, ROTATE_Y
	}

	public Rotate3DAnimation(float fromDegrees, float toDegrees, float centerX,
			float centerY, float depthz, boolean bool, Model model,
			OnSwapListener swapListener) {
		init(fromDegrees, toDegrees, centerX, centerY, depthz, bool,
				swapListener);
		this.model = model;
	}

	public Rotate3DAnimation(float fromDegrees, float toDegrees, float centerX,
			float centerY, float depthz, boolean bool, OnSwapListener swapListener) {
		init(fromDegrees, toDegrees, centerX, centerY, depthz, bool,
				swapListener);
	}

	private void init(float fromDegrees, float toDegrees, float centerX,
			float centerY, float depthz, boolean bool, OnSwapListener swapListener) {
		this.mFromDegrees = fromDegrees;
		this.mToDegrees = toDegrees;
		this.mCenterX = centerX;
		this.mCenterY = centerY;
		this.mDepthZ = depthz;
		this.mReverse = bool;
	}

	// 3d动画前的初始化操作
	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		// 必须调用
		super.initialize(width, height, parentWidth, parentHeight);
		// 初始化摄像头
		mCamera = new Camera();
	}

	// 在初始化函数调用后被调用
	@Override
	protected void applyTransformation(float time, Transformation tran) {
		// time是一个系统内置时间；时间间隔很小；因此系统默认的跳转时间都很短
		Matrix matrix = tran.getMatrix();
		// 新的转角
		float newDegrees = mFromDegrees + time * (mToDegrees - mFromDegrees);
		mCamera.save();// 旋转前保存当前画面
		if (!this.mReverse) {
			mCamera.translate(0F, 0F, this.mDepthZ * (1F - time));
		} else {
			mCamera.translate(0F, 0F, time * this.mDepthZ);
		}
		if (model == Model.ROTATE_X) {
			// 饶着X轴旋转新计算的角度
			mCamera.rotateX(newDegrees);
		} else {
			// 饶着Y轴旋转新计算的角度
			mCamera.rotateY(newDegrees);
		}
		// 传递矩阵对象到底层；获得底层matrix的实例
		mCamera.getMatrix(matrix);
		// 摄像机恢复原状
		mCamera.restore();
		// 操作Matrix对象准备平移
		matrix.preTranslate(-mCenterX, -mCenterY);
		// 传递平移
		matrix.postTranslate(mCenterX, mCenterY);
	}

	/**
	 * 返回默认的起始跳转的动画
	 * 
	 * @param container
	 * @return
	 */
	public static Rotate3DAnimation getDefaultPreAnim(ViewGroup container,
			Model model, final OnSwapListener swapListener) {
		Rotate3DAnimation d3 = new Rotate3DAnimation(PRE_FROMDEGREE,
				PRE_TODEGREE, container.getWidth() / 2f,
				container.getHeight() / 2f, DEPTHZ, true, model, swapListener);
		d3.setAnimationListener(new NextView(container,false,model,swapListener));
		d3.setDuration(DURATION);
		d3.setFillAfter(FILLAFTER);
		d3.setInterpolator(INTERPOLATOR);
		return d3;
	}

	/**
	 * 返回起始跳转的动画
	 * 
	 * @param container
	 * @return
	 */
	public static Rotate3DAnimation getPreAnim(ViewGroup container,
			Model model, final OnSwapListener swapListener) {
		Rotate3DAnimation d3 = new Rotate3DAnimation(PRE_FROMDEGREE,
				-PRE_TODEGREE, container.getWidth() / 2f,
				container.getHeight() / 2f, DEPTHZ, true, model, swapListener);
		d3.setAnimationListener(new NextView(container,true,model,swapListener));
		d3.setDuration(DURATION);
		d3.setFillAfter(FILLAFTER);
		d3.setInterpolator(INTERPOLATOR);
		return d3;
	}

	/**
	 * 返回默认的紧随跳转动画
	 * 
	 * @param container
	 * @return
	 */
	public static Rotate3DAnimation getDefaultNextAnim(ViewGroup container,
			Model model, final OnSwapListener swapListener) {
		Rotate3DAnimation d3 = new Rotate3DAnimation(NEXT_FROMDEGREE,
				NEXT_TODEGREE, container.getWidth() / 2f,
				container.getHeight() / 2f, DEPTHZ, false, model, swapListener);
		d3.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				swapListener.onSwapFinished(false);
			}
		});
		d3.setDuration(DURATION);
		d3.setFillAfter(FILLAFTER);
		d3.setInterpolator(NEXTINTERPOLATOR);
		return d3;
	}

	/**
	 * 返回紧随跳转动画
	 * 
	 * @param container
	 * @return
	 */
	public static Rotate3DAnimation getNextAnim(ViewGroup container,
			Model model, final OnSwapListener swapListener) {
		Rotate3DAnimation d3 = new Rotate3DAnimation(-NEXT_FROMDEGREE,
				NEXT_TODEGREE, container.getWidth() / 2f,
				container.getHeight() / 2f, DEPTHZ, false, model, swapListener);
		d3.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				swapListener.onSwapFinished(true);
			}
		});
		d3.setDuration(DURATION);
		d3.setFillAfter(FILLAFTER);
		d3.setInterpolator(NEXTINTERPOLATOR);
		return d3;
	}

	// 执行跳转的监听器
	private static class NextView implements Animation.AnimationListener {
		private ViewGroup container;
		private boolean flag;//判断使用哪一种动画
		private Model swapModel;
		private OnSwapListener swapListener;

		public NextView(ViewGroup container,boolean flag,Model model,OnSwapListener swapListener) {
			this.container=container;
			this.flag = flag;
			this.swapModel=model;
			this.swapListener=swapListener;
		}

		public void onAnimationEnd(Animation animation) {
			swapListener.onSwapFinished(false);
			container.post(new SwapView(container,flag,swapModel,swapListener));
		}

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	private static class SwapView implements Runnable {
		private ViewGroup container;
		private boolean flag;
		private Model swapModel;
		private OnSwapListener swapListener;

		public SwapView(ViewGroup container,boolean flag,Model model,OnSwapListener swapListener) {
			this.container=container;
			this.flag = flag;
			this.swapModel=model;
			this.swapListener=swapListener;
		}

		// 如果传入的参数是SOURCE--->显示原图 如果传入的参数是EFFECT---->显示效果图
		public void run() {
			if (swapListener != null) {
				swapListener.onSwap(flag);
			}
			Rotate3DAnimation d3 = null;
			if (flag) {
				d3 = Rotate3DAnimation.getNextAnim(container, swapModel,swapListener);
			} else {
				d3 = Rotate3DAnimation.getDefaultNextAnim(container,swapModel,swapListener);
			}
			container.startAnimation(d3);
		}
	}

	// 交换界面监听
	public interface OnSwapListener {
		/** 是否翻转 */
		public void onSwap(boolean flag);
		/** 翻转结束 true 代表是反面 */
		public void onSwapFinished(boolean isNext);
	}

}