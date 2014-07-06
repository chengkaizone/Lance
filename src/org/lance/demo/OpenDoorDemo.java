package org.lance.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.lance.main.R;

public class OpenDoorDemo extends Activity implements OnClickListener {
	private Button btn_start;

	private LinearLayout layout;
	private LinearLayout animLayout;
	private ImageView leftLayout;
	private ImageView rightLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opendoor_main);
		initViews();

	}

	private void initViews() {
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_start.setOnClickListener(this);

		layout = (LinearLayout) findViewById(R.id.layout);
		animLayout = (LinearLayout) findViewById(R.id.animLayout);
		leftLayout = (ImageView) findViewById(R.id.leftLayout);
		rightLayout = (ImageView) findViewById(R.id.rightLayout);

	}

	private void doOpenDoor() {
		layout.setVisibility(View.GONE);
		animLayout.setVisibility(View.VISIBLE);
		Animation leftOutAnimation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.translate_left);
		Animation rightOutAnimation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.translate_right);

		leftLayout.setAnimation(leftOutAnimation);
		rightLayout.setAnimation(rightOutAnimation);
		leftOutAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				leftLayout.setVisibility(View.GONE);
				rightLayout.setVisibility(View.GONE);
				finish();
				overridePendingTransition(R.anim.zoom_out_enter,
						R.anim.zoom_out_exit);
			}
		});
	}

	@Override
	public void onClick(View v) {
		doOpenDoor();
	}
}