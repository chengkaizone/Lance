package opengl.lance.demo_5;

import org.lance.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class MyActivity_3 extends Activity {
	/** Called when the activity is first created. */
	BallSurfaceView_3 msv;
	SeekBar sb; // 声明拖拉条引用

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		msv = new BallSurfaceView_3(this);
		setContentView(R.layout.main5_3);
		sb = (SeekBar) findViewById(R.id.SeekBar01);
		msv.requestFocus(); // 获取焦点
		msv.setFocusableInTouchMode(true); // 设置为可触控
		LinearLayout lla = (LinearLayout) findViewById(R.id.lla);
		lla.addView(msv);
		sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) { // 通过SeekBar来改变定向光的方向
				System.out.println(progress);
				if (progress < 50) { // 光线在左边
					msv.setxPosition(progress / 5);
				} else if (progress >= 50) { // 光线在右边
					msv.setxPosition((progress - 50) / 5);
				}
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		msv.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		msv.onResume();
	}
}