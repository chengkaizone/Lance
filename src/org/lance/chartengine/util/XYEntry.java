package org.lance.chartengine.util;

import java.util.Map.Entry;

/**
 * ӳ��,���ڷ�װX,Y��,������
 * 
 * @author lance
 * 
 * @param <K>
 * @param <V>
 */
public class XYEntry<K, V> implements Entry<K, V> {
	private final K key;

	private V value;

	public XYEntry(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	public V setValue(V object) {
		this.value = object;
		return this.value;
	}
}