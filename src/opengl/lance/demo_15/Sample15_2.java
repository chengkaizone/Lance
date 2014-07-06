package opengl.lance.demo_15;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class Sample15_2 extends Activity {
	BallSurfaceView mySurfaceView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mySurfaceView = new BallSurfaceView(this);
		mySurfaceView.requestFocus();
		mySurfaceView.setFocusableInTouchMode(true);
		setContentView(mySurfaceView);
	}

	@Override
	public void onResume() {
		super.onResume();
		mySurfaceView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mySurfaceView.onPause();
	}
}