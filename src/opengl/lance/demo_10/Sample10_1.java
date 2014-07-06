package opengl.lance.demo_10;

import android.app.Activity;
import android.os.Bundle;

public class Sample10_1 extends Activity {
	CTSurfaceView mGLSurfaceView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLSurfaceView = new CTSurfaceView(this);
		mGLSurfaceView.requestFocus();// ��ȡ����
		mGLSurfaceView.setFocusableInTouchMode(true);// ����Ϊ�ɴ���

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