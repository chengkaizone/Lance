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
 * �첽����ͼƬ ������ͼƬurl��ַ
 * 
 * @author lance
 * 
 */
@SuppressLint("NewApi")
public class AsyncImageTask extends AsyncTask<String, Long, Bitmap> {
	// �жϱ����Ƿ���ڸ�λͼ---���ھͲ���Ҫ���¼���
	private AsyncImageView mImageView;
	private HashMap<String, SoftReference<Bitmap>> mImageCache;
	// ���ﴫ�����ص�ַ��Ŀ�����жϱ���֪���д��̻���---�ڴ滺����кܶ�����Ҫ����(��ʱ����)
	private String mImageUrl;

	public AsyncImageTask(AsyncImageView imageView, String imageUrl) {
		super();
		this.mImageView = imageView;
		this.mImageUrl = imageUrl;
	}

	// �����ڴ滺��
	public AsyncImageTask(AsyncImageView imageView, String imageUrl,
			HashMap<String, SoftReference<Bitmap>> imageCache) {
		super();
		this.mImageView = imageView;
		this.mImageUrl = imageUrl;
		this.mImageCache = imageCache;
	}

	/**
	 * �����Long������ӦAsyncTask�еĵ�һ������ �����String����ֵ��ӦAsyncTask�ĵ���������
	 * �÷�������������UI�̵߳��У���Ҫ�����첽�����������ڸ÷����в��ܶ�UI���еĿռ�������ú��޸�
	 * ���ǿ��Ե���publishProgress��������onProgressUpdate��UI���в���
	 */
	@Override
	protected Bitmap doInBackground(String... params) {
		String urlParam = params[0];
		return loadImage(mImageCache, urlParam);
	}

	/**
	 * �����String������ӦAsyncTask�еĵ�����������Ҳ���ǽ���doInBackground�ķ���ֵ��
	 * ��doInBackground����ִ�н���֮�������У�����������UI�̵߳��� ���Զ�UI�ռ��������
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
	 * �÷���������UI�̵߳���,����������UI�̵߳��� ���Զ�UI�ռ�������� ����ִ�����֮��Ż�ִ��doIn����
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
	 * �����Intege�������Ͷ�ӦAsyncTask�еĵڶ�������
	 * ��doInBackground�������У���ÿ�ε���publishProgress�������ᴥ��onProgressUpdateִ��
	 * onProgressUpdate����UI�߳���ִ�У����п��Զ�UI�ռ���в��� publishProgress�����Ĳ���Ҳ�Ƿ�����ĵڶ�����������
	 */
	@Override
	protected void onProgressUpdate(Long... values) {
		// ��������
	}
	/** ����ֻ�ж�λͼ�Ƿ�������ڴ�ʹ����� */
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
	 * ��������ͼƬ
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
				saveDirPath.mkdirs();// �����༶Ŀ¼
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
			//Ȼ������ļ���ͼƬ
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
	
	// Ԥ���� �������޷ֱ��ʵ�ͼƬ
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
	 * �����ļ���Ҫ���ص�·��
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
