package opengl.lance.demo_15;

import java.util.HashMap;

import org.lance.main.R;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;

/**
 * 穿透效果
 * 
 * @author Administrator
 * 
 */
public class Sample15_6 extends Activity {
	BlastSurfaceView mGLSurfaceView;
	SoundPool soundPool;
	HashMap<Integer, Integer> soundPoolMap;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		mGLSurfaceView = new BlastSurfaceView(this);
		mGLSurfaceView.requestFocus();
		mGLSurfaceView.setFocusableInTouchMode(true);
		initPool();
		setContentView(mGLSurfaceView);
	}

	public void onResume() {
		super.onResume();
		mGLSurfaceView.onResume();
	}

	public void onPause() {
		super.onPause();
		mGLSurfaceView.onPause();
	}

	private void initPool() {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		// 加载声音~设置Id和优先级
		soundPoolMap.put(1, soundPool.load(this, R.raw.explode, 1));
	}

	public void playSound(int sound, int loop) {
		AudioManager mgr = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeMax = mgr
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeCur = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		float volume = streamVolumeCur / streamVolumeMax;
		// 播放短音
		soundPool.play(sound, volume, volume, 1, loop, 1f);
	}
}
