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
		mGLSurfaceView.requestFocus();// ��ȡ����
		mGLSurfaceView.setFocusableInTouchMode(true);// ����Ϊ�ɴ���
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