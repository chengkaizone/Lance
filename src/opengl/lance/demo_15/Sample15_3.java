package opengl.lance.demo_15;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;

public class Sample15_3 extends Activity {
	/** Called when the activity is first created. */
	MySurfaceView_3 mySurfaceView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mySurfaceView = new MySurfaceView_3(this);
		mySurfaceView.requestFocus();
		mySurfaceView.setFocusableInTouchMode(true);
		setContentView(mySurfaceView);
		Display dis = this.getWindowManager().getDefaultDisplay();
		System.out.println(dis.getWidth() + " " + dis.getHeight());
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