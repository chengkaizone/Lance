package org.lance.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

/**
 * 网络工具类
 * 
 * @author lance
 * 
 */
public class NetService {
	// 文件目录
	public static final String RES_LOAD_FOLDER = "/mnt/sdcard/";
	// 超时时间--10秒
	private static final int TIME_OUT = 10000;
	// 设置编码
	private static final String CHARSET = "utf-8";
	// 创建边界标识--- 随机生成
	private static String BOUNDARY = UUID.randomUUID().toString();
	// 设置前缀--结尾
	private static String PREFIX = "--", LINE_END = "\r\n";
	// 设置内容类型
	private static String CONTENT_TYPE = "multipart/form-data";

	/**
	 * 上传文件
	 * 
	 * @param file
	 * @param serverUrl
	 * @return
	 */
	public static String uploadFile(File file, String serverUrl) {
		HttpURLConnection conn = getConnect(serverUrl);
		String result = null;
		try {
			// conn.connect();//建立连接
			if (file != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				// 采用数据流包装文件上传
				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				 * filename是文件的名字，包含后缀名的 比如:abc.png
				 */

				sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""
						+ file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */
				int res = conn.getResponseCode();
				System.out.println("响应码--->" + res);
				if (res == 200) {
					InputStream input = conn.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(input));
					StringBuilder sb1 = new StringBuilder();
					String s = "";
					while ((s = reader.readLine()) != null) {
						sb1.append(s);
					}
					result = sb1.toString();
					System.out.println(result);
				} else {
					System.out.println("上传文件失败！");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param link
	 *            上传自己所在的地理位置
	 * @return 返回响应的内容
	 */
	public static String uploadLoc(String link) {
		String result = null;
		try {
			HttpURLConnection conn = getConnect(link);
			conn.connect();// 建立连接
			// 返回响应码
			int res = conn.getResponseCode();
			System.out.println("返回响应参数" + res);
			if (res == 200) {
				InputStream input = conn.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(input));
				StringBuilder sb = new StringBuilder();
				String s = "";
				while ((s = reader.readLine()) != null) {
					sb.append(s);
				}
				result = sb.toString();
				System.out.println("响应结果-->" + result);
			} else {
				System.out.println("没有响应！");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 更新apk--调用系统软件下载
	 * 
	 * @param context
	 * @param link
	 */
	public static void updateApk(Context context, String link) {
		FileOutputStream fileOutputStream = null;
		HttpClient client = new DefaultHttpClient();
		try {
			HttpGet get = new HttpGet(link);
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			long len = entity.getContentLength();
			InputStream is = entity.getContent();
			if (is != null) {
				File file = new File(RES_LOAD_FOLDER, "ocn_android.apk");
				fileOutputStream = new FileOutputStream(file);
				// 缓存1M
				byte[] brr = new byte[1024];
				int ch = -1;
				int count = 0;
				while ((ch = is.read(brr)) != -1) {
					fileOutputStream.write(brr, 0, ch);
					count += ch;
					if (len > 0) {

					}
				}
			}
			fileOutputStream.flush();
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(
				Uri.fromFile(new File(RES_LOAD_FOLDER + "mocnoss.apk")),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 根据link返回HttpURLConnection连接实例
	 * 
	 * @param link
	 * @return
	 */
	public static HttpURLConnection getConnect(String link) {
		// 打开链接
		HttpURLConnection conn = null;
		try {
			// 创建URL
			URL url = new URL(link);
			conn = (HttpURLConnection) url.openConnection();
			// 设置读取时间
			conn.setReadTimeout(TIME_OUT);
			// 设置连接超时时间
			conn.setConnectTimeout(TIME_OUT);
			// 允许输入流
			conn.setDoInput(true);
			// 允许输出流
			conn.setDoOutput(true);
			// 不允许使用缓存
			conn.setUseCaches(false);
			// 请求方式---post请求
			conn.setRequestMethod("POST");
			// 设置字符编码--相关参数设置
			conn.setRequestProperty("Charset", CHARSET);
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}

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
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static int downFile(String url, String path, String fileName) {
		File dir_root = Environment.getExternalStorageDirectory();
		File dir1 = new File(dir_root + "/" + path);
		if (!dir1.exists()) {
			dir1.mkdir();
		}
		String tmp = dir1 + "/" + fileName;
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

	/**
	 * 获取输入流
	 * 
	 * @param url
	 * @return
	 */
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

	/**
	 * 获取位图对象
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmap(String url) {
		return BitmapFactory.decodeStream(getInputStream(url));
	}
}
