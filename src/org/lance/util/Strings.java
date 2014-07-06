package org.lance.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * 
 * @author lance
 * 
 */
public final class Strings {
	private static final char[] DIGITS;
	private static final char[] UPPER_CASE_DIGITS;

	static {
		char[] arrayOfChar = new char[36];
		arrayOfChar[0] = 48;
		arrayOfChar[1] = 49;
		arrayOfChar[2] = 50;
		arrayOfChar[3] = 51;
		arrayOfChar[4] = 52;
		arrayOfChar[5] = 53;
		arrayOfChar[6] = 54;
		arrayOfChar[7] = 55;
		arrayOfChar[8] = 56;
		arrayOfChar[9] = 57;
		arrayOfChar[10] = 97;
		arrayOfChar[11] = 98;
		arrayOfChar[12] = 99;
		arrayOfChar[13] = 100;
		arrayOfChar[14] = 101;
		arrayOfChar[15] = 102;
		arrayOfChar[16] = 103;
		arrayOfChar[17] = 104;
		arrayOfChar[18] = 105;
		arrayOfChar[19] = 106;
		arrayOfChar[20] = 107;
		arrayOfChar[21] = 108;
		arrayOfChar[22] = 109;
		arrayOfChar[23] = 110;
		arrayOfChar[24] = 111;
		arrayOfChar[25] = 112;
		arrayOfChar[26] = 113;
		arrayOfChar[27] = 114;
		arrayOfChar[28] = 115;
		arrayOfChar[29] = 116;
		arrayOfChar[30] = 117;
		arrayOfChar[31] = 118;
		arrayOfChar[32] = 119;
		arrayOfChar[33] = 120;
		arrayOfChar[34] = 121;
		arrayOfChar[35] = 122;
		DIGITS = arrayOfChar;
		arrayOfChar = new char[36];
		arrayOfChar[0] = 48;
		arrayOfChar[1] = 49;
		arrayOfChar[2] = 50;
		arrayOfChar[3] = 51;
		arrayOfChar[4] = 52;
		arrayOfChar[5] = 53;
		arrayOfChar[6] = 54;
		arrayOfChar[7] = 55;
		arrayOfChar[8] = 56;
		arrayOfChar[9] = 57;
		arrayOfChar[10] = 65;
		arrayOfChar[11] = 66;
		arrayOfChar[12] = 67;
		arrayOfChar[13] = 68;
		arrayOfChar[14] = 69;
		arrayOfChar[15] = 70;
		arrayOfChar[16] = 71;
		arrayOfChar[17] = 72;
		arrayOfChar[18] = 73;
		arrayOfChar[19] = 74;
		arrayOfChar[20] = 75;
		arrayOfChar[21] = 76;
		arrayOfChar[22] = 77;
		arrayOfChar[23] = 78;
		arrayOfChar[24] = 79;
		arrayOfChar[25] = 80;
		arrayOfChar[26] = 81;
		arrayOfChar[27] = 82;
		arrayOfChar[28] = 83;
		arrayOfChar[29] = 84;
		arrayOfChar[30] = 85;
		arrayOfChar[31] = 86;
		arrayOfChar[32] = 87;
		arrayOfChar[33] = 88;
		arrayOfChar[34] = 89;
		arrayOfChar[35] = 90;
		UPPER_CASE_DIGITS = arrayOfChar;
	}

	public static String bytesToHexString(byte[] paramArrayOfByte,
			boolean paramBoolean) {
		char[] arrayOfChar1;
		if (!paramBoolean) {
			arrayOfChar1 = DIGITS;
		} else {
			arrayOfChar1 = UPPER_CASE_DIGITS;
		}
		char[] arrayOfChar2 = new char[2 * paramArrayOfByte.length];
		int i = paramArrayOfByte.length;
		int i1 = 0;
		int k = 0;
		while (i1 < i) {
			int l = paramArrayOfByte[i1];
			int j = k + 1;
			arrayOfChar2[k] = arrayOfChar1[(0xF & l >> 4)];
			k = j + 1;
			arrayOfChar2[j] = arrayOfChar1[(l & 0xF)];
			++i1;
		}
		return new String(arrayOfChar2);
	}

	public static final String construct(byte[] paramArrayOfByte,
			int paramInt1, int paramInt2, Charset paramCharset) {
		try {
			String str = new String(paramArrayOfByte, paramInt1, paramInt2,
					paramCharset.name());
			return str;
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			throw new IllegalArgumentException(
					localUnsupportedEncodingException);
		}
	}

	public static byte[] getBytes(String paramString, Charset paramCharset) {
		try {
			byte[] arrayOfByte = paramString.getBytes(paramCharset.name());
			return arrayOfByte;
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			throw new IllegalArgumentException(
					localUnsupportedEncodingException);
		}
	}

	public static final boolean isEmpty(String paramString) {
		if ((paramString == null) || (paramString.length() == 0)) {
			return true;
		}
		return false;
	}
}