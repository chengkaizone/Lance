package opengl.lance.demo_4;

import org.lance.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class PairDemo extends Activity implements OnCheckedChangeListener {
	private ToggleButton tb1, tb2, tb3;
	private PairSurfaceView psv;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.main4_3);
		tb1 = (ToggleButton) findViewById(R.id.ToggleButton01);
		tb2 = (ToggleButton) findViewById(R.id.ToggleButton02);
		tb3 = (ToggleButton) findViewById(R.id.ToggleButton03);
		tb1.setOnCheckedChangeListener(this);
		tb2.setOnCheckedChangeListener(this);
		tb3.setOnCheckedChangeListener(this);
		LinearLayout lay = (LinearLayout) findViewById(R.id.main_liner);
		psv = new PairSurfaceView(this);
		psv.requestFocus();
		psv.setFocusableInTouchMode(true);
		lay.addView(psv);
	}

	@Override
	protected void onResume() {
		super.onResume();
		psv.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		psv.onPause();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.ToggleButton01:
			psv.setBackFlag(!psv.isBackFlag());
			break;
		case R.id.ToggleButton02:
			psv.setSmoothFlag(!psv.isSmoothFlag());
			break;
		case R.id.ToggleButton03:
			psv.setCullFlag(psv.isCullFlag());
			break;
		}

	}
}
