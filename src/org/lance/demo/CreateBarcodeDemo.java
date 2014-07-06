package org.lance.demo;

import java.io.File;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import barcode.lance.assist.WriterException;
import barcode.lance.client.common.BarcodeService;

import org.lance.main.R;

/**
 * ���ɶ�ά��
 * 
 * @author chengkai
 * 
 */
public class CreateBarcodeDemo extends BaseActivity implements OnClickListener {

	// private static final int PICK_CONTACT = 1;
	private EditText input;
	private TextView info;
	private Button build, share, read;
	private ImageView image;
	private boolean flag;
	private View inputRoot;
	private View encodeRoot;
	// ��ǰλͼ�����·��
	private String path;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.net_main);
		input = (EditText) findViewById(R.id.net_name);
		info = (TextView) findViewById(R.id.encode_info);
		build = (Button) findViewById(R.id.net_button);
		share = (Button) findViewById(R.id.encode_share_button);
		read = (Button) findViewById(R.id.encode_read_button);
		image = (ImageView) findViewById(R.id.encode_image);
		inputRoot = findViewById(R.id.net_relay);
		encodeRoot = findViewById(R.id.encode_root);

		share.setOnClickListener(this);
		build.setOnClickListener(this);
		read.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (flag) {
				inputRoot.setVisibility(View.VISIBLE);
				encodeRoot.setVisibility(View.GONE);
				flag = false;
			} else {
				finish();
			}
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.net_button:
			info.setText("");
			String str = input.getText().toString().trim();
			if ("".equals(str)) {
				Toast.makeText(CreateBarcodeDemo.this, "������Ҫ�������Ϣ",
						Toast.LENGTH_SHORT).show();
			} else {
				try {
					Bitmap bitmap = BarcodeService.getGeneratedBitmap(str);
					String fileName = "encode" + System.currentTimeMillis()
							+ ".png";
					path = "/mnt/sdcard/" + fileName;
					BarcodeService.saveBarcode(bitmap, fileName);
					image.setImageBitmap(bitmap);
					encodeRoot.setVisibility(View.VISIBLE);
					inputRoot.setVisibility(View.GONE);
				} catch (WriterException e) {
					e.printStackTrace();
					System.out.println("����ʧ��!");
				}
				info.setText("������Ϣ: " + str);
			}
			flag = true;
			break;
		case R.id.encode_share_button:
			// String st = "/mnt/sdcard/svg_test.svg";
			// Bitmap bit = BarcodeService.getDecodeBitmap(st);
			// if (bit != null) {
			// image.setImageBitmap(bit);
			// } else {
			// Toast.makeText(NetDemo.this, "��ȡλͼʧ��", Toast.LENGTH_SHORT)
			// .show();
			// }
			Toast.makeText(CreateBarcodeDemo.this, "��δʵ��", Toast.LENGTH_SHORT)
					.show();
			// Toast.makeText(NetDemo.this, "ִ�з�����", Toast.LENGTH_LONG).show();
			// Intent intent = new Intent(Intent.ACTION_VIEW);
			// startActivityForResult(intent, PICK_CONTACT);
			break;
		case R.id.encode_read_button:
			String info = BarcodeService.getDecodeInfo(path);
			if (info != null) {
				Toast.makeText(CreateBarcodeDemo.this, info, Toast.LENGTH_LONG)
						.show();
			}
			break;
		}
	}

	public void openFile(Context context, File file) {
		try {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// ����intent��Action����
			intent.setAction(Intent.ACTION_VIEW);
			// ��ȡ�ļ�file��MIME����
			String type = getMIMEType(file);
			// ����intent��data��Type���ԡ�
			intent.setDataAndType(/* uri */Uri.fromFile(file), type);
			// ��ת
			context.startActivity(intent);
			// Intent.createChooser(intent, "��ѡ���Ӧ������򿪸ø�����");
		} catch (ActivityNotFoundException e) {
			Toast.makeText(context, "sorry�������ܴ򿪣���������������", 500).show();
		}
	}

	private String getMIMEType(File file) {

		String type = "*/*";
		String fName = file.getName();
		// ��ȡ��׺��ǰ�ķָ���"."��fName�е�λ�á�
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* ��ȡ�ļ��ĺ�׺�� */
		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
		if (end == "")
			return type;
		// ��MIME���ļ����͵�ƥ������ҵ���Ӧ��MIME���͡�
		for (int i = 0; i < MIME_MapTable.length; i++) {

			if (end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}

	// �����Լ��������
	private String[][] MIME_MapTable = {
			// {��׺����MIME����}
			{ ".3gp", "video/3gpp" },
			{ ".apk", "application/vnd.Android.package-archive" },
			{ ".asf", "video/x-ms-asf" },
			{ ".avi", "video/x-msvideo" },
			{ ".bin", "application/octet-stream" },
			{ ".bmp", "image/bmp" },
			{ ".c", "text/plain" },
			{ ".class", "application/octet-stream" },
			{ ".conf", "text/plain" },
			{ ".cpp", "text/plain" },
			{ ".doc", "application/msword" },
			{ ".docx",
					"application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
			{ ".xls", "application/vnd.ms-excel" },
			{ ".xlsx",
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
			{ ".exe", "application/octet-stream" },
			{ ".gif", "image/gif" },
			{ ".gtar", "application/x-gtar" },
			{ ".gz", "application/x-gzip" },
			{ ".h", "text/plain" },
			{ ".htm", "text/html" },
			{ ".html", "text/html" },
			{ ".jar", "application/java-archive" },
			{ ".java", "text/plain" },
			{ ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" },
			{ ".js", "application/x-javascript" },
			{ ".log", "text/plain" },
			{ ".m3u", "audio/x-mpegurl" },
			{ ".m4a", "audio/mp4a-latm" },
			{ ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" },
			{ ".m4u", "video/vnd.mpegurl" },
			{ ".m4v", "video/x-m4v" },
			{ ".mov", "video/quicktime" },
			{ ".mp2", "audio/x-mpeg" },
			{ ".mp3", "audio/x-mpeg" },
			{ ".mp4", "video/mp4" },
			{ ".mpc", "application/vnd.mpohun.certificate" },
			{ ".mpe", "video/mpeg" },
			{ ".mpeg", "video/mpeg" },
			{ ".mpg", "video/mpeg" },
			{ ".mpg4", "video/mp4" },
			{ ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" },
			{ ".ogg", "audio/ogg" },
			{ ".pdf", "application/pdf" },
			{ ".png", "image/png" },
			{ ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".pptx",
					"application/vnd.openxmlformats-officedocument.presentationml.presentation" },
			{ ".prop", "text/plain" }, { ".rc", "text/plain" },
			{ ".rmvb", "audio/x-pn-realaudio" }, { ".rtf", "application/rtf" },
			{ ".sh", "text/plain" }, { ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" },
			{ ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" },
			{ ".wmv", "audio/x-ms-wmv" },
			{ ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" },
			{ ".z", "application/x-compress" },
			{ ".zip", "application/x-zip-compressed" }, { "", "*/*" } };

}
