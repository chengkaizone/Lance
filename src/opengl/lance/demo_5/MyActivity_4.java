package opengl.lance.demo_5;

import android.app.Activity; //引入相关包
import android.os.Bundle; //引入相关包

public class MyActivity_4 extends Activity {
	BallSurfaceView_4 msv; // MySurfaceView引用

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		msv = new BallSurfaceView_4(this); // 实例化MySurfaceView对象
		msv.requestFocus();
		msv.setFocusableInTouchMode(true);
		setContentView(msv); // 设置Activity内容
	}
}