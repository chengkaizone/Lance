package opengl.lance.demo_11;

import android.app.Activity;
import android.os.Bundle;

public class Sample11_2 extends Activity {
	private FlagSurfaceView mGLSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGLSurfaceView = new FlagSurfaceView(this);
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