package org.lance.lib;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;


/**
 * 位图加载器主要用于保留位图的引用
 * @author lance
 *
 */
public class AsyncBitmapLoader {
	
	/**
	 * 内存图片软引用缓冲
	 */
	private HashMap<String, SoftReference<Bitmap>> imageCache = null;

	public AsyncBitmapLoader() {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}
	
	public HashMap<String, SoftReference<Bitmap>> getImageCache() {
		return imageCache;
	}
	/**
	 * 使用图片默认的尺寸加载---不允许超出极限
	 * @param imageView
	 * @param imageURL
	 */
	public void loadBitmap(final AsyncImageView imageView, final String imageURL) {
		imageView.setImageWithURL(imageURL,imageCache);
	}
	/**
	 * 使用指定的尺寸加载图片
	 * @param imageView
	 * @param imageURL
	 * @param size
	 */
	public void loadBitmap(final AsyncImageView imageView, final String imageURL,int size) {
		imageView.setImageWithURL(imageURL,imageCache,size);
	}
	/**
	 * 使用了指定宽高加载图片
	 * @param imageView
	 * @param imageURL
	 * @param width
	 * @param height
	 */
	public void loadBitmap(final AsyncImageView imageView, final String imageURL,int width,int height) {
		imageView.setImageWithURL(imageURL,imageCache,width,height);
	}

}
