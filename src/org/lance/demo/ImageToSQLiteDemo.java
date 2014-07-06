package org.lance.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.lance.db.ImageDatabaseHelper;
import org.lance.entity.ImageInfo;
import org.lance.main.R;

public class ImageToSQLiteDemo extends BaseActivity {
	private TextView image;
	private ImageView icon;
	private ImageDatabaseHelper dbhelper;
	int[] irr = { R.drawable.video_camera, R.drawable.bg2, R.drawable.ck_today,
			R.drawable.ck_welcome };

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.image_sqlite);
		image = (TextView) findViewById(R.id.imageinfo_name);
		icon = (ImageView) findViewById(R.id.imageinfo_icon);
		dbhelper = new ImageDatabaseHelper(this);
		init();
	}

	private void init() {
		ImageInfo info = null;
		if (!dbhelper.hasRecode()) {
			for (int i = 0; i < irr.length; i++) {
				info = new ImageInfo();
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						irr[i]);
				byte[] brr = dbhelper.bitmapToString(bitmap);
				info.setName("icon" + i);
				info.setData(brr);
				dbhelper.insertIcon(info);
			}
		}
		info = dbhelper.getImageInfo("icon0");
		if (info != null) {
			image.setText(info.getName());
			byte[] brr = info.getData();
			Bitmap bitmap = BitmapFactory.decodeByteArray(brr, 0, brr.length);
			if (bitmap != null) {
				icon.setImageBitmap(bitmap);
			}
		}
	}

}
