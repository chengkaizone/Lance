package org.lance.demo;

import android.R.anim;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import org.lance.main.R;

public class PathButtonDemo extends BaseActivity {
	private Button buttonCamera, buttonDelete, buttonWith, buttonPlace,
			buttonMusic, buttonThought, buttonSleep;
	private Animation animationTranslate, animationRotate, animationScale;
	private static int height;
	private LayoutParams params = new LayoutParams(0, 0);
	private static Boolean isClick = false;
	private TextView refresh;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.path_button);

		refresh = (TextView) this.findViewById(R.id.refresh);
		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PathButtonDemo.this,
						CustomColumnDemo.class);
				startActivity(intent);
				// 第一个参数为启动时动画效果，第二个参数为退出时动画效果
				overridePendingTransition(
						R.anim.anim_scale_translate_rotate_in,
						R.anim.anim_alpha_out);
			}
		});
		initialButton();
	}

	private void initialButton() {
		Display display = getWindowManager().getDefaultDisplay();
		height = display.getHeight();

		params.height = 50;
		params.width = 50;
		// 设置边距 (int left, int top, int right, int bottom)
		params.setMargins(10, height - 98, 0, 0);

		buttonSleep = (Button) findViewById(R.id.button_composer_sleep);
		buttonSleep.setLayoutParams(params);

		buttonThought = (Button) findViewById(R.id.button_composer_thought);
		buttonThought.setLayoutParams(params);

		buttonMusic = (Button) findViewById(R.id.button_composer_music);
		buttonMusic.setLayoutParams(params);

		buttonPlace = (Button) findViewById(R.id.button_composer_place);
		buttonPlace.setLayoutParams(params);

		buttonWith = (Button) findViewById(R.id.button_composer_with);
		buttonWith.setLayoutParams(params);

		buttonCamera = (Button) findViewById(R.id.button_composer_camera);
		buttonCamera.setLayoutParams(params);

		buttonDelete = (Button) findViewById(R.id.button_friends_delete);
		buttonDelete.setLayoutParams(params);

		buttonDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isClick == false) {
					isClick = true;
					buttonDelete
							.startAnimation(animRotate(-45.0f, 0.5f, 0.45f));
					buttonCamera.startAnimation(animTranslate(0.0f, -180.0f,
							10, height - 240, buttonCamera, 80));
					buttonWith.startAnimation(animTranslate(30.0f, -150.0f, 60,
							height - 230, buttonWith, 100));
					buttonPlace.startAnimation(animTranslate(70.0f, -120.0f,
							110, height - 210, buttonPlace, 120));
					buttonMusic.startAnimation(animTranslate(80.0f, -110.0f,
							150, height - 180, buttonMusic, 140));
					buttonThought.startAnimation(animTranslate(90.0f, -60.0f,
							175, height - 135, buttonThought, 160));
					buttonSleep.startAnimation(animTranslate(170.0f, -30.0f,
							190, height - 90, buttonSleep, 180));

				} else {
					isClick = false;
					buttonDelete.startAnimation(animRotate(90.0f, 0.5f, 0.45f));
					buttonCamera.startAnimation(animTranslate(0.0f, 140.0f, 10,
							height - 98, buttonCamera, 180));
					// 30.0f, -150.0f
					buttonWith.startAnimation(animTranslate(-50.0f, 130.0f, 10,
							height - 98, buttonWith, 160));
					buttonPlace.startAnimation(animTranslate(-100.0f, 110.0f,
							10, height - 98, buttonPlace, 140));
					buttonMusic.startAnimation(animTranslate(-140.0f, 80.0f,
							10, height - 98, buttonMusic, 120));
					buttonThought.startAnimation(animTranslate(-160.0f, 40.0f,
							10, height - 98, buttonThought, 80));
					buttonSleep.startAnimation(animTranslate(-170.0f, 0.0f, 10,
							height - 98, buttonSleep, 50));

				}

			}
		});
		buttonCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonCamera.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonWith.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonPlace.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonSleep.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
		buttonWith.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonWith.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonCamera.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonPlace.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonSleep.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
		buttonPlace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonPlace.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonWith.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonCamera.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonSleep.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
		buttonMusic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonMusic.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonPlace.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonWith.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonCamera.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonSleep.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
		buttonThought.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonThought.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonPlace.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonWith.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonCamera.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonSleep.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
		buttonSleep.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonSleep.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonPlace.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonWith.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonCamera.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});

	}

	protected Animation setAnimScale(float toX, float toY) {
		animationScale = new ScaleAnimation(1f, toX, 1f, toY,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.45f);
		animationScale.setInterpolator(PathButtonDemo.this,
				anim.accelerate_decelerate_interpolator);
		animationScale.setDuration(500);
		animationScale.setFillAfter(false);
		return animationScale;

	}

	protected Animation animRotate(float toDegrees, float pivotXValue,
			float pivotYValue) {
		animationRotate = new RotateAnimation(0, toDegrees,
				Animation.RELATIVE_TO_SELF, pivotXValue,
				Animation.RELATIVE_TO_SELF, pivotYValue);
		animationRotate.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				animationRotate.setFillAfter(true);
			}
		});
		return animationRotate;
	}

	protected Animation animTranslate(float toX, float toY, final int lastX,
			final int lastY, final Button button, long durationMillis) {
		animationTranslate = new TranslateAnimation(0, toX, 0, toY);
		animationTranslate.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				params = new LayoutParams(0, 0);
				params.height = 50;
				params.width = 50;
				params.setMargins(lastX, lastY, 0, 0);
				button.setLayoutParams(params);
				button.clearAnimation();
			}
		});
		animationTranslate.setDuration(durationMillis);
		return animationTranslate;
	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}

}