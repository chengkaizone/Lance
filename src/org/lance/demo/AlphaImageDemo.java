package org.lance.demo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import org.lance.main.R;
import org.lance.widget.AlphaImageView;

public class AlphaImageDemo extends BaseActivity {
	private boolean flag = false;
	private AlphaImageView img;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.alpha_main);
		img = (AlphaImageView) findViewById(R.id.alpha_img);
		img.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!flag) {
					img.stop();
					flag = !flag;
				} else {
					img.start();
					flag = !flag;
				}
			}
		});
	}
}
