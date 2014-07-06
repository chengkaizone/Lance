package org.lance.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.EmbossMaskFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.lance.listener.OnColorChangedListener;
import org.lance.main.R;
import org.lance.widget.ColorPicker;
import org.lance.widget.SignView;

/**
 * ǩ��ҳ��--δʹ��
 * 
 * @author Administrator
 * 
 */
public class SignPageDemo extends BaseActivity implements
		OnColorChangedListener {
	private SignView sign;
	private BlurMaskFilter blur;
	private EmbossMaskFilter emboss;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.ck_sign);
		init();
	}

	private void init() {
		sign = (SignView) findViewById(R.id.sign_signview);
		blur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
		emboss = new EmbossMaskFilter(new float[] { 1.5f, 1.5f, 1.5f }, 0.6f,
				6f, 4.2f);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// ��ȡ�˵�������
		MenuInflater inflater = new MenuInflater(this);
		// ���˵���Դ�������˵�����
		inflater.inflate(R.menu.ck_sign_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.width_1:
			sign.getCachePaint().setStrokeWidth(1);
			break;
		case R.id.width_3:
			sign.getCachePaint().setStrokeWidth(3);
			break;
		case R.id.width_5:
			sign.getCachePaint().setStrokeWidth(5);
			break;
		case R.id.blur:
			sign.getCachePaint().setMaskFilter(blur);
			break;
		case R.id.emboss:
			sign.getCachePaint().setMaskFilter(emboss);
			break;
		case R.id.select_color:
			new ColorPicker(this, this, sign.getCachePaint().getColor()).show();
			break;
		case R.id.save:
			try {
				Toast.makeText(this, "���ڱ���ͼ��...", 2000).show();
				saveBitmap();
				Toast.makeText(this, "����ɹ�", 2000).show();
			} catch (Exception e) {
				Toast.makeText(this, "����ʧ��", 2000).show();
				e.printStackTrace();
			}
			break;
		case R.id.repicture:
			// ֪ͨ���»�ͼ
			sign.initParam();
			break;
		}
		return true;
	}

	private void saveBitmap() {
		String path = "/mnt/sdcard/pictures/";
		String fileName = System.currentTimeMillis() + ".png";
		try {
			File file = new File(path);
			if (!file.exists()) {
				// �����ļ���
				file.mkdir();
			}
			// �����ļ�
			File png = new File(path + fileName);
			// ����ͼƬ�ļ�
			png.createNewFile();
			// ���ļ�������
			FileOutputStream output = new FileOutputStream(png);
			Bitmap cache = sign.getBitmap();
			// ѹ���ɶ����Ƶ�ָ���������ָ����ʽ����ͼ��
			cache.compress(Bitmap.CompressFormat.PNG, 100, output);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void colorChanged(int color) {
		sign.getCachePaint().setColor(color);
	}
}
