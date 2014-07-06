package opengl.lance.demo_15;

import android.app.Activity;
import android.os.Bundle;

public class Sample15_4 extends Activity {
	/** Called when the activity is first created. */
	private MySurfaceView_4 mGLSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLSurfaceView = new MySurfaceView_4(this);
		mGLSurfaceView.requestFocus();// 获取焦点
		mGLSurfaceView.setFocusableInTouchMode(true);// 设置为可触控

		setContentView(mGLSurfaceView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLSurfaceView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLSurfaceView.onPause();
	}
}