package opengl.lance.demo_7;

import android.app.Activity;
import android.os.Bundle;

public class Activity_GL_Taper_1 extends Activity {
	private TSurfaceView_1 mGLSurfaceView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGLSurfaceView = new TSurfaceView_1(this);
		setContentView(mGLSurfaceView);
		mGLSurfaceView.setFocusableInTouchMode(true);// ����Ϊ�ɴ���
		mGLSurfaceView.requestFocus();// ��ȡ����
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