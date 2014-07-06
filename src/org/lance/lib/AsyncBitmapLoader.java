package org.lance.lib;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;


/**
 * λͼ��������Ҫ���ڱ���λͼ������
 * @author lance
 *
 */
public class AsyncBitmapLoader {
	
	/**
	 * �ڴ�ͼƬ�����û���
	 */
	private HashMap<String, SoftReference<Bitmap>> imageCache = null;

	public AsyncBitmapLoader() {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}
	
	public HashMap<String, SoftReference<Bitmap>> getImageCache() {
		return imageCache;
	}
	/**
	 * ʹ��ͼƬĬ�ϵĳߴ����---������������
	 * @param imageView
	 * @param imageURL
	 */
	public void loadBitmap(final AsyncImageView imageView, final String imageURL) {
		imageView.setImageWithURL(imageURL,imageCache);
	}
	/**
	 * ʹ��ָ���ĳߴ����ͼƬ
	 * @param imageView
	 * @param imageURL
	 * @param size
	 */
	public void loadBitmap(final AsyncImageView imageView, final String imageURL,int size) {
		imageView.setImageWithURL(imageURL,imageCache,size);
	}
	/**
	 * ʹ����ָ����߼���ͼƬ
	 * @param imageView
	 * @param imageURL
	 * @param width
	 * @param height
	 */
	public void loadBitmap(final AsyncImageView imageView, final String imageURL,int width,int height) {
		imageView.setImageWithURL(imageURL,imageCache,width,height);
	}

}
