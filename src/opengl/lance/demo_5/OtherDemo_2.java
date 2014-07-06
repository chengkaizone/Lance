package opengl.lance.demo_5;

import org.lance.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

public class OtherDemo_2 extends Activity {
	/** Called when the activity is first created. */
	BallSurfaceView_2 msv;
	RatingBar rb; // ��������������

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		msv = new BallSurfaceView_2(this); // ʵ����SurfaceView����
		setContentView(R.layout.main5_2); // ����Activity����
		rb = (RatingBar) findViewById(R.id.RatingBar01);
		msv.requestFocus(); // ��ȡ����
		msv.setFocusableInTouchMode(true); // ����Ϊ�ɴ���
		LinearLayout lla = (LinearLayout) findViewById(R.id.lla);
		lla.addView(msv);
		rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			// Ϊ
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				if (rating >= 0 && rating <= 1) { // RatingBar�ı仯���ı俪���ƹ��������
					msv.setLightNum(1);
				} else if (rating > 1 && rating <= 2) {
					msv.setLightNum(2);
				} else if (rating > 2 && rating <= 3) {
					msv.setLightNum(3);
				} else if (rating > 3 && rating <= 4) {
					msv.setLightNum(4);
				} else if (rating > 4 && rating <= 5) {
					msv.setLightNum(5);
				}
				Toast.makeText(OtherDemo_2.this,
						"������" + msv.getLightNum() + "ջ��", Toast.LENGTH_SHORT)
						.show();
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
