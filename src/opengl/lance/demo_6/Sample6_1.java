package opengl.lance.demo_6;

import android.app.Activity;
import android.os.Bundle;

public class Sample6_1 extends Activity {
	/** Called when the activity is first created. */
	OSurfaceView_1 mGLSurfaceView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLSurfaceView = new OSurfaceView_1(this);
		setContentView(mGLSurfaceView);
		mGLSurfaceView.requestFocus();// 获取焦点
		mGLSurfaceView.setFocusableInTouchMode(true);// 设置为可触控
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