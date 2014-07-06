package org.lance.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import org.lance.main.R;
import org.lance.widget.ZoomImageView;

public class ZoomImageDemo extends BaseActivity {
	private ZoomImageView zoomImg;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.zoom_layout);
		zoomImg = (ZoomImageView) findViewById(R.id.zoom_image);
		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.bg2);
		zoomImg.setImage(bitmap);
	}
}
