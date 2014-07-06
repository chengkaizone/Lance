package org.lance.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

import org.lance.interpolator.BackInterpolator;
import org.lance.interpolator.EasingType;
import org.lance.interpolator.ElasticInterpolator;
import org.lance.main.R;
import org.lance.widget.Switcher;

public class SwitcherDemo extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.switcher_main);
		String[] data0 = { "january", "february", "march", "april", "may",
				"june", "july", "august", "september", "october", "november",
				"december", };
		ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(this,
				R.layout.switcher_view, data0);
		final Switcher switcher0 = (Switcher) findViewById(R.id.switcher0);
		switcher0.setAdapter(adapter0);

		String[] data1 = { "0 zero", "1 one", "2 two", "3 three", "4 four",
				"5 fi", "8 eight", "9 nine", };
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				R.layout.switcher_view, data1);
		final Switcher switcher1 = (Switcher) findViewById(R.id.switcher1);
		switcher1.setAdapter(adapter1);
		String[] data2 = { "0 null", "1 eins", "2 zwei", "3 drei", "4 vier",
				"5 fün", "7 sieben", "8 acht", "9 neun", };
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				R.layout.switcher_view, data2);
		final Switcher switcher2 = (Switcher) findViewById(R.id.switcher2);
		switcher2.setAdapter(adapter2);

		findViewById(R.id.button0).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				switcher0.setSelection(4, true);
				switcher1.setSelection(4, true);
				switcher2.setSelection(4, true);
			}
		});
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				switcher0.setSelection(7, true);
				switcher1.setSelection(7, true);
				switcher2.setSelection(7, true);
			}
		});

		switcher0.setInterpolator(new BackInterpolator(EasingType.INOUT, 2));
		switcher1.setInterpolator(new ElasticInterpolator(EasingType.OUT, 1.0f,
				0.3f));
	}
}
