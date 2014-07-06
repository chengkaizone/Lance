package barcode.lance.client.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import barcode.lance.client.handler.BarcodePreferences;

public class BeepManager {

	private static final float BEEP_VOLUMN = 0.10f;
	private static final long VIBRATE_DURATION = 200L;

	private static int resRaw;
	private final Activity act;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private boolean vibrate;

	public BeepManager(Activity act, int resRaw) {
		this.act = act;
		this.resRaw = resRaw;
		this.mediaPlayer = null;

		updatePrefs();
	}

	public void updatePrefs() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(act);
		playBeep = shouldBeep(prefs, act);
		vibrate = prefs.getBoolean(BarcodePreferences.VIBRATE, false);
		if (playBeep && mediaPlayer == null) {
			act.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = this.buildMediaPlayer(act);
		}
	}

	public void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) act
					.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private static boolean shouldBeep(SharedPreferences prefs, Activity act) {
		boolean shouldPlayBeep = prefs.getBoolean(BarcodePreferences.PLAY_BEEP,
				true);
		if (shouldPlayBeep) {
			AudioManager audioService = (AudioManager) act
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
				shouldPlayBeep = false;
			}
		}
		return shouldPlayBeep;
	}

	private static MediaPlayer buildMediaPlayer(Context act) {
		MediaPlayer mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer player) {
				player.seekTo(0);
			}
		});
		AssetFileDescriptor file = act.getResources().openRawResourceFd(resRaw);
		try {
			mediaPlayer.setDataSource(file.getFileDescriptor(),
					file.getStartOffset(), file.getLength());
			file.close();
			mediaPlayer.setVolume(BEEP_VOLUMN, BEEP_VOLUMN);
			mediaPlayer.prepare();
		} catch (Exception e) {
			mediaPlayer = null;
		}
		return mediaPlayer;
	}
}
