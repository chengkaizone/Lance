package org.lance.lib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.lance.lib.AsyncImageView.BitmapConfig;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;


/**
 * 异步加载图片 参数是图片url地址
 * 
 * @author lance
 * 
 */
@SuppressLint("NewApi")
public class AsyncImageTask extends AsyncTask<String, Long, Bitmap> {
	// 判断本地是否存在该位图---存在就不需要重新加载
	private AsyncImageView mImageView;
	private HashMap<String, SoftReference<Bitmap>> mImageCache;
	// 这里传入下载地址的目的是判断本地知否有磁盘缓存---内存缓存会有很多问题要处理(暂时不用)
	private String mImageUrl;

	public AsyncImageTask(AsyncImageView imageView, String imageUrl) {
		super();
		this.mImageView = imageView;
		this.mImageUrl = imageUrl;
	}

	// 加入内存缓存
	public AsyncImageTask(AsyncImageView imageView, String imageUrl,
			HashMap<String, SoftReference<Bitmap>> imageCache) {
		super();
		this.mImageView = imageView;
		this.mImageUrl = imageUrl;
		this.mImageCache = imageCache;
	}

	/**
	 * 这里的Long参数对应AsyncTask中的第一个参数 这里的String返回值对应AsyncTask的第三个参数
	 * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
	 * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
	 */
	@Override
	protected Bitmap doInBackground(String... params) {
		String urlParam = params[0];
		return loadImage(mImageCache, urlParam);
	}

	/**
	 * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
	 * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
	 */
	@Override
	protected void onPostExecute(Bitmap resultBitmap) {
		if (resultBitmap != null) {
			mImageView.setImageBitmap(resultBitmap);
		} else {
			BitmapDrawable loadfailDrawable = mImageView.getLoadfailDrawable();
			if (loadfailDrawable != null) {
				mImageView.setImageDrawable(loadfailDrawable);
			}
		}
	}

	/**
	 * 该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置 方法执行完成之后才会执行doIn方法
	 */
	@Override
	//
	protected void onPreExecute() {
		BitmapDrawable loadingDrawable = mImageView.getLoadingDrawable();
		if (loadingDrawable != null) {
			mImageView.setImageDrawable(loadingDrawable);
		}
	}

	/**
	 * 这里的Intege参数类型对应AsyncTask中的第二个参数
	 * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
	 * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作 publishProgress方法的参数也是泛型类的第二个参数类型
	 */
	@Override
	protected void onProgressUpdate(Long... values) {
		// 与进度相关
	}
	/** 这里只判断位图是否存在于内存和磁盘中 */
	private Bitmap loadImage(HashMap<String, SoftReference<Bitmap>> imageCache,
			String urlParam) {
		if (imageCache!=null&&imageCache.containsKey(urlParam)) {
			SoftReference<Bitmap> reference = imageCache.get(urlParam);
			Bitmap bitmap = reference.get();
			if (bitmap != null) {
				return bitmap;
			}
		} else {
			BitmapConfig conf=mImageView.getmConfig();
			Bitmap bitmap = loadBitmapFromPath(getCacheFilePath(urlParam),conf.width,conf.height);
			if (bitmap != null) {
				return bitmap;
			}
		}
		return downloadImage(imageCache, urlParam);
	}

	/**
	 * 下载网络图片
	 * 
	 * @param urlParam
	 * @return
	 */
	private Bitmap downloadImage(
			HashMap<String, SoftReference<Bitmap>> imageCache, String urlParam) {
		try {
			Bitmap bitmap=downloadImage(urlParam);
			if(bitmap!=null&&imageCache!=null){
				imageCache.put(urlParam, new SoftReference<Bitmap>(bitmap));
			}
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Bitmap downloadImage(String urlParam) {
		String bitmapName = urlParam.substring(urlParam.lastIndexOf("/") + 1);
		String cacheDir = "/mnt/sdcard/caches";
		FileOutputStream fos=null;
		InputStream is=null;
		try {
			
			File saveDirPath = new File(cacheDir);
			if (!saveDirPath.exists()) {
				saveDirPath.mkdirs();// 创建多级目录
			}
			File file = new File(cacheDir + bitmapName);
			file.createNewFile();
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(urlParam);
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			
			if (is != null) {
				fos=new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int ch = -1;
				while ((ch = is.read(buf)) != -1) {
					fos.write(buf, 0, ch);
				}
			}
			BitmapConfig conf=mImageView.getmConfig();
			//然后解码文件成图片
			return loadBitmapFromPath(file.getAbsolutePath(), conf.width,conf.height);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public Bitmap loadBitmapFromPath(String path,int size) {
		return loadBitmapFromPath(path,size,size);
	}
	
	public Bitmap loadBitmapFromPath(String path,int width,int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		options = prepareOptions(options);
		if(options.outWidth<=width&&options.outHeight<height){
			options.inSampleSize = 1;
		}else if(width==1){
			
		}else{
			int factorw=options.outWidth/width;
			int factorh=options.outHeight/height;
			options.inSampleSize =factorw>factorh?factorw+1:factorh+1;
		}
		options.inJustDecodeBounds = false;
		options.inPurgeable = true;
		bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}
	
	// 预处理 超过极限分辨率的图片
		private BitmapFactory.Options prepareOptions(
				BitmapFactory.Options options) {
			float scaleWidthFactor = options.outWidth / AsyncImageView.BitmapConfig.MAX_WIDTH;
			float scaleHeightFactor = options.outWidth / AsyncImageView.BitmapConfig.MAX_HEIGHT;
			float factor = scaleWidthFactor > scaleHeightFactor ? scaleWidthFactor
					: scaleHeightFactor;
			options.inSampleSize = (int) (factor + 1);
			return options;
		}
	
	/**
	 * 返回文件需要返回的路径
	 * 
	 * @param urlParam
	 * @return
	 */
	private String getCacheFilePath(String urlParam) {
		String bitmapName = urlParam.substring(urlParam.lastIndexOf("/") + 1);
		String cacheDir = "/mnt/sdcard/caches";
		File dirFile = new File(cacheDir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		String filePath = cacheDir + File.separator + bitmapName;
		;
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filePath;
	}
}
