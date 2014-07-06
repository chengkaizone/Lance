package org.lance.lib;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;



/**
 * 异步加载图片 这里需要缓存图片
 * 
 * @author lance
 * 
 */
public class AsyncImageView extends ImageView {
	private BitmapConfig mConfig;
	// 显示加载执行时显示的图片
	private BitmapDrawable loadingDrawable;
	// 加载失败显示的图片
	private BitmapDrawable loadfailDrawable;

	public AsyncImageView(Context context) {
		super(context);
		init(context);
	}

	public AsyncImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context){
		mConfig=new BitmapConfig(getResources());
	}
	
	public BitmapConfig getmConfig() {
		return mConfig;
	}

	public BitmapDrawable getLoadingDrawable() {
		return loadingDrawable;
	}

	public BitmapDrawable getLoadfailDrawable() {
		return loadfailDrawable;
	}

	public void setLoadingDrawable(int resId) {
		loadingDrawable = new BitmapDrawable(getResources(),
				BitmapFactory.decodeResource(getResources(), resId));
	}

	public void setLoadfailDrawable(int resId) {
		loadfailDrawable = new BitmapDrawable(getResources(),
				BitmapFactory.decodeResource(getResources(), resId));
	}

	/**
	 * 设置下载地址
	 * 
	 * @param urlParam
	 */
	public void setImageWithURL(String urlParam) {
		new AsyncImageTask(this, urlParam).execute(urlParam);
	}

	public void setImageWithURL(String urlParam,int width,int height) {
		mConfig.width=width;
		mConfig.height=height;
		new AsyncImageTask(this, urlParam).execute(urlParam);
	}

	public void setImageWithURL(String urlParam,int size) {
		setImageWithURL(urlParam,size,size);
	}
	
	/**
	 * 设置下载地址加缓存引用
	 * 
	 * @param urlParam
	 * @param imageCache
	 */
	public void setImageWithURL(String urlParam,
			HashMap<String, SoftReference<Bitmap>> imageCache) {
		new AsyncImageTask(this, urlParam, imageCache).execute(urlParam);
	}
	
	public void setImageWithURL(String urlParam,
			HashMap<String, SoftReference<Bitmap>> imageCache,int size) {
		setImageWithURL(urlParam,imageCache,size,size);
	}

	public void setImageWithURL(String urlParam,
			HashMap<String, SoftReference<Bitmap>> imageCache,int width,int height) {
		mConfig.width=width;
		mConfig.height=height;
		new AsyncImageTask(this, urlParam, imageCache).execute(urlParam);
	}
	
	/**
	 * 设置下载地址
	 * 
	 * @param urlParam
	 * @param conf
	 *            位图配置
	 */
	public void setImageWithURL(String urlParam, BitmapConfig conf) {
		new AsyncImageTask(this, urlParam).execute(urlParam);
	}

	/**
	 * 位图的配置---限制位图不能超过一定的宽高---否则易造成内存泄露
	 * 
	 * @author lance
	 * 
	 */
	public class BitmapConfig {
		// 最大宽度
		public static final int MAX_WIDTH = 450;
		// 最大高度
		public static final int MAX_HEIGHT = 450;
		public int width;
		public int height;

		public BitmapConfig(Resources resources) {// 默认为屏幕的1/2的宽高
			DisplayMetrics displayMetrics = resources.getDisplayMetrics();
			int defaultWidth = (int) Math.floor(displayMetrics.widthPixels / 2);
			if (defaultWidth >= MAX_WIDTH) {
				defaultWidth = MAX_WIDTH;
			}
			width = defaultWidth;
			height = defaultWidth;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			if(width>MAX_WIDTH){
				width=MAX_WIDTH;
			}
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			if(height>MAX_HEIGHT){
				height=MAX_HEIGHT;
			}
			this.height = height;
		}
	}

}
