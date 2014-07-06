package opengl.lance.demo_11;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Sample11_3 extends Activity {
	private LWSurfaceView mGLSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ������Ϊ����ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mGLSurfaceView = new LWSurfaceView(this);
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