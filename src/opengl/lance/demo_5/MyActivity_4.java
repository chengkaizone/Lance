package opengl.lance.demo_5;

import android.app.Activity; //������ذ�
import android.os.Bundle; //������ذ�

public class MyActivity_4 extends Activity {
	BallSurfaceView_4 msv; // MySurfaceView����

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		msv = new BallSurfaceView_4(this); // ʵ����MySurfaceView����
		msv.requestFocus();
		msv.setFocusableInTouchMode(true);
		setContentView(msv); // ����Activity����
	}
}