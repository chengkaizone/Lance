package opengl.lance.demo_4;

import org.lance.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class HexDemo extends Activity {
	private ToggleButton tb;
	private HexSurfaceView hsv;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.main4_5);
		tb = (ToggleButton) findViewById(R.id.ToggleButton01);
		LinearLayout lay = (LinearLayout) findViewById(R.id.main_liner);
		hsv = new HexSurfaceView(this);
		hsv.requestFocus();
		hsv.setFocusableInTouchMode(true);
		lay.addView(hsv);
		tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				hsv.setPerspective(!hsv.isPerspective());
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		hsv.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		hsv.onPause();
	}

}
