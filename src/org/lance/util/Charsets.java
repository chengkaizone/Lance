package org.lance.util;

import java.nio.charset.Charset;

/**
 * ×Ö·û¼¯³£Á¿
 * 
 * @author lance
 * 
 */
public class Charsets {
	public static final Charset ISO_8859_1;
	public static final Charset US_ASCII;
	public static final Charset UTF_8 = Charset.forName("UTF-8");

	static {
		US_ASCII = Charset.forName("US-ASCII");
		ISO_8859_1 = Charset.forName("ISO-8859-1");
	}
}