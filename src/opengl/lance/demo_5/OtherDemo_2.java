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
	RatingBar rb; // 声明拖拉条引用

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		msv = new BallSurfaceView_2(this); // 实例化SurfaceView对象
		setContentView(R.layout.main5_2); // 设置Activity内容
		rb = (RatingBar) findViewById(R.id.RatingBar01);
		msv.requestFocus(); // 获取焦点
		msv.setFocusableInTouchMode(true); // 设置为可触控
		LinearLayout lla = (LinearLayout) findViewById(R.id.lla);
		lla.addView(msv);
		rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			// 为
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				if (rating >= 0 && rating <= 1) { // RatingBar的变化来改变开启灯光的数量。
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
						"开启了" + msv.getLightNum() + "栈灯", Toast.LENGTH_SHORT)
						.show();
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
