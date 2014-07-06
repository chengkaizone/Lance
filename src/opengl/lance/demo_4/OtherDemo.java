package opengl.lance.demo_4;

import org.lance.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class OtherDemo extends Activity {
	private PLSurfaceView osv;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.main4_1);
		osv = new PLSurfaceView(this);
		osv.requestFocus();
		osv.setFocusableInTouchMode(true);
		LinearLayout lay = (LinearLayout) findViewById(R.id.main_liner);
		lay.addView(osv);

	}

	@Override
	protected void onResume() {
		super.onResume();
		osv.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		osv.onPause();
	}

}
