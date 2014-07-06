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
 * ���繤����
 * 
 * @author lance
 * 
 */
public class NetService {
	// �ļ�Ŀ¼
	public static final String RES_LOAD_FOLDER = "/mnt/sdcard/";
	// ��ʱʱ��--10��
	private static final int TIME_OUT = 10000;
	// ���ñ���
	private static final String CHARSET = "utf-8";
	// �����߽��ʶ--- �������
	private static String BOUNDARY = UUID.randomUUID().toString();
	// ����ǰ׺--��β
	private static String PREFIX = "--", LINE_END = "\r\n";
	// ������������
	private static String CONTENT_TYPE = "multipart/form-data";

	/**
	 * �ϴ��ļ�
	 * 
	 * @param file
	 * @param serverUrl
	 * @return
	 */
	public static String uploadFile(File file, String serverUrl) {
		HttpURLConnection conn = getConnect(serverUrl);
		String result = null;
		try {
			// conn.connect();//��������
			if (file != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				// ������������װ�ļ��ϴ�
				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				/**
				 * �����ص�ע�⣺ name�����ֵΪ����������Ҫkey ֻ�����key �ſ��Եõ���Ӧ���ļ�
				 * filename���ļ������֣�������׺���� ����:abc.png
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
				 * ��ȡ��Ӧ�� 200=�ɹ� ����Ӧ�ɹ�����ȡ��Ӧ����
				 */
				int res = conn.getResponseCode();
				System.out.println("��Ӧ��--->" + res);
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
					System.out.println("�ϴ��ļ�ʧ�ܣ�");
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
	 *            �ϴ��Լ����ڵĵ���λ��
	 * @return ������Ӧ������
	 */
	public static String uploadLoc(String link) {
		String result = null;
		try {
			HttpURLConnection conn = getConnect(link);
			conn.connect();// ��������
			// ������Ӧ��
			int res = conn.getResponseCode();
			System.out.println("������Ӧ����" + res);
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
				System.out.println("��Ӧ���-->" + result);
			} else {
				System.out.println("û����Ӧ��");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ����apk--����ϵͳ�������
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
				// ����1M
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
	 * ����link����HttpURLConnection����ʵ��
	 * 
	 * @param link
	 * @return
	 */
	public static HttpURLConnection getConnect(String link) {
		// ������
		HttpURLConnection conn = null;
		try {
			// ����URL
			URL url = new URL(link);
			conn = (HttpURLConnection) url.openConnection();
			// ���ö�ȡʱ��
			conn.setReadTimeout(TIME_OUT);
			// �������ӳ�ʱʱ��
			conn.setConnectTimeout(TIME_OUT);
			// ����������
			conn.setDoInput(true);
			// ���������
			conn.setDoOutput(true);
			// ������ʹ�û���
			conn.setUseCaches(false);
			// ����ʽ---post����
			conn.setRequestMethod("POST");
			// �����ַ�����--��ز�������
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
	 * ����0�����ļ��Ѿ����ڣ�����1 �����ļ����سɹ�������-1�����ļ�����ʧ��
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
					System.out.println("ƫ����--->" + i);
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
	 * ��ȡ������
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
			System.out.println("�Ƿ������쳣");
			e.printStackTrace();
		}
		return input;
	}

	/**
	 * ��ȡλͼ����
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmap(String url) {
		return BitmapFactory.decodeStream(getInputStream(url));
	}
}
