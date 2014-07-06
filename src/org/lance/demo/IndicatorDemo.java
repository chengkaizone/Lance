package org.lance.demo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.lance.main.R;
import org.lance.widget.Indicator;

public class IndicatorDemo extends BaseActivity {
	private Indicator indicator;
	private Button btn;
	private int loc = 0;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.ck_indicator_main);
		indicator = (Indicator) findViewById(R.id.indicator_lay);
		btn = (Button) findViewById(R.id.indicator_btn);
		indicator.setPoints(5);
		indicator.init(R.drawable.ck_gray_icon, R.drawable.ck_red_icon);
		indicator.desPoint(loc);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				++loc;
				if (loc > 4) {
					loc = 0;
				}
				indicator.desPoint(loc);
			}
		});
	}
}
