package org.lance.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 文件处理工具
 * 
 * @author lance
 * 
 */
public class FileService {

	public static String downFile(String url) {
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		String tmp = "";
		try {

			reader = new BufferedReader(new InputStreamReader(
					getInputStream(url)));
			while ((tmp = reader.readLine()) != null) {
				builder.append(tmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	/**
	 * 返回0代表文件已经存在；返回1 代表文件下载成功；返回-1代表文件下载失败
	 * 
	 * @param url
	 *            下载url;
	 * @param path
	 *            手机存储路径;默认sd卡为根路径
	 * @param fileName
	 *            存储文件名
	 * @return
	 */
	public static int downFile(String url, String path, String fileName) {
		File dir = Environment.getExternalStorageDirectory();
		String tmp = "";
		if (path != null && (!"".equals(path))) {
			File dir1 = new File(dir + "/" + path);
			if (!dir1.exists()) {
				dir1.mkdir();
			}
			tmp = dir1 + "/" + fileName;
		} else {
			tmp = dir + "/" + fileName;
		}
		File file = new File(tmp);
		PrintStream ps = null;
		if (file.exists()) {
			return 0;
		} else {
			try {
				file.createNewFile();
				OutputStream out = new FileOutputStream(file);
				InputStream input = getInputStream(url);
				byte[] brr = new byte[4 * 1024];
				int i = 0;
				ps = new PrintStream(out);
				while ((i = input.read(brr)) != -1) {
					System.out.println("偏移量--->" + i);
					ps.write(brr, 0, i);
				}
				ps.flush();
				ps.close();
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}
		return 1;
	}

	public static InputStream getInputStream(String url) {
		InputStream input = null;
		HttpClient client = new DefaultHttpClient();
		try {
			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			input = response.getEntity().getContent();
		} catch (Exception e) {
			System.out.println("非法参数异常");
			e.printStackTrace();
		}
		return input;
	}

	public static Bitmap getBitmap(String url) {
		return BitmapFactory.decodeStream(getInputStream(url));
	}
}
