package org.lance.fireworks;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * ����������
 * 
 * @author lance
 * 
 */
public class SoundPlay {
	// ��Ч������
	int streamVolume;
	// ����SoundPool ����
	private SoundPool soundPool;
	// ����HASH��
	private HashMap<Integer, Integer> soundPoolMap;

	/**
	 * ��ʼ������
	 * 
	 * @param context
	 */
	public void initSounds(Context context) {
		// ��ʼ��soundPool ����,��һ�������������ж��ٸ�������ͬʱ����,��2����������������,������������������Ʒ��
		soundPool = new SoundPool(25, AudioManager.STREAM_MUSIC, 100);
		// ��ʼ��HASH��
		soundPoolMap = new HashMap<Integer, Integer>();
		// ��������豸���豸����
		AudioManager mgr = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	/**
	 * ����������Դ
	 * 
	 * @param context
	 * @param raw
	 * @param ID
	 */
	public void loadSound(Context context, int raw, int ID) {
		soundPoolMap.put(ID, soundPool.load(context, raw, 1));
	}

	/**
	 * ��������
	 * 
	 * @param sound
	 * @param uLoop
	 */
	public void play(int sound, int uLoop) {
		soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1,
				uLoop, 1f);
	}
}
