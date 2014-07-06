package opengl.lance.demo_15;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class Sample15_7 extends Activity {
	/** Called when the activity is first created. */
	SSurfaceView mGLSurfaceView;
	static float screenWidth;// ��Ļ���
	static float screenHeight;// ��Ļ�߶�

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels; // dm.widthPixels ��ȡ��Ļ����ֱ���
		screenHeight = dm.heightPixels; // dm.heightPixels ��ȡ��Ļ����ֱ���
		mGLSurfaceView = new SSurfaceView(this);
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