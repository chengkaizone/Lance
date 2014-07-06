package opengl.lance.demo_5;

import org.lance.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class MyActivity_3 extends Activity {
	/** Called when the activity is first created. */
	BallSurfaceView_3 msv;
	SeekBar sb; // ��������������

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		msv = new BallSurfaceView_3(this);
		setContentView(R.layout.main5_3);
		sb = (SeekBar) findViewById(R.id.SeekBar01);
		msv.requestFocus(); // ��ȡ����
		msv.setFocusableInTouchMode(true); // ����Ϊ�ɴ���
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
					boolean fromUser) { // ͨ��SeekBar���ı䶨���ķ���
				System.out.println(progress);
				if (progress < 50) { // ���������
					msv.setxPosition(progress / 5);
				} else if (progress >= 50) { // �������ұ�
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