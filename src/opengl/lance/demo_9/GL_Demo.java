package opengl.lance.demo_9;

import android.app.Activity;
import android.os.Bundle;

public class GL_Demo extends Activity {
	private OtherSurfaceView mGLSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGLSurfaceView = new OtherSurfaceView(this);
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
