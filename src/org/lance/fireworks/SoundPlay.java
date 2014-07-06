package org.lance.fireworks;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * 声音控制类
 * 
 * @author lance
 * 
 */
public class SoundPlay {
	// 音效的音量
	int streamVolume;
	// 定义SoundPool 对象
	private SoundPool soundPool;
	// 定义HASH表
	private HashMap<Integer, Integer> soundPoolMap;

	/**
	 * 初始化声音
	 * 
	 * @param context
	 */
	public void initSounds(Context context) {
		// 初始化soundPool 对象,第一个参数是允许有多少个声音流同时播放,第2个参数是声音类型,第三个参数是声音的品质
		soundPool = new SoundPool(25, AudioManager.STREAM_MUSIC, 100);
		// 初始化HASH表
		soundPoolMap = new HashMap<Integer, Integer>();
		// 获得声音设备和设备音量
		AudioManager mgr = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	/**
	 * 加载声音资源
	 * 
	 * @param context
	 * @param raw
	 * @param ID
	 */
	public void loadSound(Context context, int raw, int ID) {
		soundPoolMap.put(ID, soundPool.load(context, raw, 1));
	}

	/**
	 * 播放声音
	 * 
	 * @param sound
	 * @param uLoop
	 */
	public void play(int sound, int uLoop) {
		soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1,
				uLoop, 1f);
	}
}
