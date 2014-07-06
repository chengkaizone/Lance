package opengl.lance.demo_7;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Activity_GL_Hyperboloid_2 extends Activity {
	private MyGLSurfaceView10_2 mGLSurfaceView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		mGLSurfaceView = new MyGLSurfaceView10_2(this);
		setContentView(mGLSurfaceView);
		mGLSurfaceView.setFocusableInTouchMode(true);// 设置为可触控
		mGLSurfaceView.requestFocus();// 获取焦点
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