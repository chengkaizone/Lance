package org.lance.io;

/**
 * 算法工具收藏
 * 
 * @author lance
 * 
 */
public class Calculate {
	/**
	 * 计算排列组合
	 * 
	 * @param prefix
	 * @param arr
	 */
	public static void arrange(String prefix, int[] arr) {
		if (arr.length == 1) {
			System.out.println(prefix + arr[0]);
		}
		for (int i = 0; i < arr.length; i++) {
			int[] temp = new int[arr.length - 1];
			System.arraycopy(arr, 0, temp, 0, i);
			System.arraycopy(arr, i + 1, temp, i, arr.length - i - 1);
			arrange(prefix + arr[i], temp);
		}
	}
}
