package opengl.lance.demo_5;

import org.lance.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class OtherDemo_1 extends Activity {
	private BallSurfaceView_1 msv;
	private ToggleButton tb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		msv = new BallSurfaceView_1(this); // 实例化MySurfaceView
		setContentView(R.layout.main5_1); // 设置Acitivity的内容
		msv.requestFocus(); // 获取焦点
		msv.setFocusableInTouchMode(true); // 设置为可触控
		LinearLayout lla = (LinearLayout) findViewById(R.id.lla);
		lla.addView(msv); // 将SurfaceView加入LinearLayout中
		tb = (ToggleButton) findViewById(R.id.ToggleButton01);// 添加监听器
		tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				msv.setOpenFlag(!msv.isOpenFlag());
			}
		});
	}

	@Override
	protected void onPause() { // 当另一个Acitvity遮挡着当前的Activity时调用
		super.onPause();
		msv.onPause();
	}

	@Override
	protected void onResume() { // 当Acitvity获得用户焦点时调用
		super.onResume();
		msv.onResume();
	}
}
