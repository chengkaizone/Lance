package org.lance.demo;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import org.lance.main.R;
import org.lance.widget.FlipViewGroup;

public class FlipEffectDemo extends BaseActivity implements OnClickListener {
	private final String TAG="FlipEffectDemo";
	private FlipViewGroup contentView;
	private TextView start, login;
	private View welcome, chooseRoot;
	Handler handler = new Handler();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		contentView = new FlipViewGroup(this);

		welcome = View.inflate(this, R.layout.flip_run_screen, null);
		chooseRoot = View.inflate(this, R.layout.flip_run_choose_path, null);

		start = (TextView) chooseRoot
				.findViewById(R.id.first_run_get_started_title);
		login = (TextView) chooseRoot
				.findViewById(R.id.first_run_sign_in_title);

		start.setOnClickListener(this);
		login.setOnClickListener(this);
		contentView.addFlipView(chooseRoot);
		contentView.addFlipView(welcome);

		setContentView(contentView);

		contentView.setAutoFlip(true);
		
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		int height = metric.heightPixels;
		float density = metric.density;
		int densityDpi = metric.densityDpi;
		Log.i(TAG, width + "   " + height + "   " + density + "   " + densityDpi);
	}

	@Override
	protected void onResume() {
		super.onResume();
		contentView.onResume();
		handler.postDelayed(new Runnable() {
			public void run() {
				contentView.setAutoFlip(false);
			}
		}, 3500);

	}

	@Override
	protected void onPause() {
		super.onPause();
		contentView.onPause();
	}

	public void test(final View v) {
		View parent = (View) v.getParent();
		parent.post(new Runnable() {
			@Override
			public void run() {
				Rect delegateArea = new Rect();
				View delegate = v;
				delegate.getHitRect(delegateArea);
				delegateArea.bottom += 200;
				TouchDelegate expandedArea = new TouchDelegate(delegateArea,
						delegate);
				if (View.class.isInstance(delegate.getParent())) {
					((View) delegate.getParent())
							.setTouchDelegate(expandedArea);
				}
			}
		});

	}

	private void login() {
		startActivity(new Intent(FlipEffectDemo.this, FlipLoginPage.class));
		overridePendingTransition(R.anim.slide_in_from_right,
				R.anim.stack_push_out);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.first_run_get_started_title:
			System.out.println("¿ªÊ¼");
			break;
		case R.id.first_run_sign_in_title:
			System.out.println("µÇÂ¼");
			break;
		}
	}
}
