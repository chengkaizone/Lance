package org.lance.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.lance.main.R;

/**
 * List¸ùÄ¿Â¼
 * 
 * @author chengkai
 * 
 */
public class BarcodeDemoRoot extends Activity implements OnClickListener {
	private Button btn1, btn2;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.barcode_root_main);
		btn1 = (Button) findViewById(R.id.root_btn1);
		btn2 = (Button) findViewById(R.id.root_btn2);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.root_btn1:
			intent = new Intent(BarcodeDemoRoot.this, BarcodeDemo.class);
			break;
		case R.id.root_btn2:
			intent = new Intent(BarcodeDemoRoot.this, CreateBarcodeDemo.class);
			break;
		}
		startActivity(intent);
	}
}
