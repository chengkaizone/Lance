package opengl.lance.demo_8;

import android.app.Activity;
import android.os.Bundle;

public class Sample8_4 extends Activity {
	private MySurfaceView_4 mGLSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGLSurfaceView = new MySurfaceView_4(this);
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
