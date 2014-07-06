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
 * �첽����ͼƬ ������Ҫ����ͼƬ
 * 
 * @author lance
 * 
 */
public class AsyncImageView extends ImageView {
	private BitmapConfig mConfig;
	// ��ʾ����ִ��ʱ��ʾ��ͼƬ
	private BitmapDrawable loadingDrawable;
	// ����ʧ����ʾ��ͼƬ
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
	 * �������ص�ַ
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
	 * �������ص�ַ�ӻ�������
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
	 * �������ص�ַ
	 * 
	 * @param urlParam
	 * @param conf
	 *            λͼ����
	 */
	public void setImageWithURL(String urlParam, BitmapConfig conf) {
		new AsyncImageTask(this, urlParam).execute(urlParam);
	}

	/**
	 * λͼ������---����λͼ���ܳ���һ���Ŀ��---����������ڴ�й¶
	 * 
	 * @author lance
	 * 
	 */
	public class BitmapConfig {
		// �����
		public static final int MAX_WIDTH = 450;
		// ���߶�
		public static final int MAX_HEIGHT = 450;
		public int width;
		public int height;

		public BitmapConfig(Resources resources) {// Ĭ��Ϊ��Ļ��1/2�Ŀ��
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
