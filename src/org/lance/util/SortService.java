package org.lance.util;

import java.util.ArrayList;
import java.util.List;

/**
 * ≈≈–Úπ§æﬂ¿‡
 * 
 * @author lance
 * 
 */
public class SortService {

	public static List<String> sortByChar(List<String> data) {
		String[] srr = new String[data.size()];
		for (int i = 0; i < data.size(); i++) {
			srr[i] = data.get(i);
		}
		return sortByChar(srr);
	}

	public static List<String> sortByChar(String[] srr) {
		List<String> list = new ArrayList<String>();
		String[] tmp = sortByString(srr);
		for (String s : tmp) {
			list.add(s);
		}
		return list;
	}

	public static String[] sortByString(String[] srr) {
		String tmp;
		for (int i = 0; i < srr.length; i++) {
			for (int j = i + 1; j < srr.length; j++) {
				if (compare(srr[i], srr[j]) > 0) {
					tmp = srr[i];
					srr[i] = srr[j];
					srr[j] = tmp;
				}
			}
		}
		return srr;
	}

	public static int compare(String o1, String o2) {
		int MASK = 0xFFDF;
		int length1 = o1.length();
		int length2 = o2.length();
		int length = length1 > length2 ? length2 : length1;
		int c1, c2;
		int d1, d2;
		for (int i = 0; i < length; i++) {
			c1 = o1.charAt(i);
			c2 = o2.charAt(i);
			d1 = c1 & MASK;
			d2 = c2 & MASK;
			if (d1 > d2) {
				return 1;
			} else if (d1 < d2) {
				return -1;
			} else {
				if (c1 > c2) {
					return 1;
				} else if (c1 < c2) {
					return -1;
				}
			}
		}
		if (length1 > length2) {
			return 1;
		} else if (length1 < length2) {
			return -1;
		}
		return 0;
	}

}
