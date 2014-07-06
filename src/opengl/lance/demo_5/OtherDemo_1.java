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
		msv = new BallSurfaceView_1(this); // ʵ����MySurfaceView
		setContentView(R.layout.main5_1); // ����Acitivity������
		msv.requestFocus(); // ��ȡ����
		msv.setFocusableInTouchMode(true); // ����Ϊ�ɴ���
		LinearLayout lla = (LinearLayout) findViewById(R.id.lla);
		lla.addView(msv); // ��SurfaceView����LinearLayout��
		tb = (ToggleButton) findViewById(R.id.ToggleButton01);// ��Ӽ�����
		tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				msv.setOpenFlag(!msv.isOpenFlag());
			}
		});
	}

	@Override
	protected void onPause() { // ����һ��Acitvity�ڵ��ŵ�ǰ��Activityʱ����
		super.onPause();
		msv.onPause();
	}

	@Override
	protected void onResume() { // ��Acitvity����û�����ʱ����
		super.onResume();
		msv.onResume();
	}
}
