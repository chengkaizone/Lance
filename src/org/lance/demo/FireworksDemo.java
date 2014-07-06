package org.lance.demo;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import org.lance.fireworks.FireworksFactory;
import org.lance.fireworks.FireworksView;
import static org.lance.fireworks.FireworksView.*;
import org.lance.main.R;

/**
 * ÑÌ»¨demo
 * 
 * @author chengkai
 * 
 */
public class FireworksDemo extends BaseActivity {
	private FireworksView fireworkView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// if (getRequestedOrientation() !=
		// ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// }

		FireworksFactory factory = new FireworksFactory();
		factory.addAnimID(new int[] { R.drawable.fw_01, R.drawable.fw_02,
				R.drawable.fw_03, R.drawable.fw_04, R.drawable.fw_05,
				R.drawable.fw_06, R.drawable.fw_07, R.drawable.fw_08,
				R.drawable.fw_09, R.drawable.fw_10, R.drawable.fw_11,
				R.drawable.fw_12, R.drawable.fw_13 });
		factory.addAnimID(new int[] { R.drawable.fw2_01, R.drawable.fw2_02,
				R.drawable.fw2_03, R.drawable.fw2_04, R.drawable.fw2_07,
				R.drawable.fw2_08, R.drawable.fw2_05, R.drawable.fw2_06,
				R.drawable.fw2_09, R.drawable.fw2_10, R.drawable.fw2_11 });
		factory.addAnimID(new int[] { R.drawable.fw3_01, R.drawable.fw3_02,
				R.drawable.fw3_03, R.drawable.fw3_04, R.drawable.fw3_05,
				R.drawable.fw3_06, R.drawable.fw3_07, R.drawable.fw3_08,
				R.drawable.fw3_09, R.drawable.fw3_10, R.drawable.fw3_11,
				R.drawable.fw3_12, R.drawable.fw3_13, R.drawable.fw3_14 });
		factory.addAnimID(new int[] { R.drawable.fw4_01, R.drawable.fw4_02,
				R.drawable.fw4_03, R.drawable.fw4_04, R.drawable.fw4_05,
				R.drawable.fw4_06, R.drawable.fw4_07, R.drawable.fw4_08,
				R.drawable.fw4_09, R.drawable.fw4_10, R.drawable.fw4_11,
				R.drawable.fw4_12, R.drawable.fw4_13 });
		factory.addAnimID(new int[] { R.drawable.fw5_01, R.drawable.fw5_02,
				R.drawable.fw5_03, R.drawable.fw5_04, R.drawable.fw5_05,
				R.drawable.fw5_06, R.drawable.fw5_07, R.drawable.fw5_08,
				R.drawable.fw5_09, R.drawable.fw5_10, R.drawable.fw5_11,
				R.drawable.fw5_12, R.drawable.fw5_13, R.drawable.fw5_14,
				R.drawable.fw5_15 });
		fireworkView = new FireworksView(this, R.drawable.night, factory);
		fireworkView.getSoundPlay().loadSound(this, R.raw.up, ID_SOUND_UP);
		fireworkView.getSoundPlay().loadSound(this, R.raw.blow, ID_SOUND_BLOW);
		fireworkView.getSoundPlay().loadSound(this, R.raw.multiple,
				ID_SOUND_MULTIPLE);
		setContentView(fireworkView);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (fireworkView.isRunning()) {
			fireworkView.setRunning(false);
		}
	}
}
