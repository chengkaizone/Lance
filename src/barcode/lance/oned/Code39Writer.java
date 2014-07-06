package barcode.lance.oned;

import java.util.Hashtable;

import barcode.lance.assist.BarcodeFormat;
import barcode.lance.assist.WriterException;
import barcode.lance.common.BitMatrix;

/**
 * This object renders a CODE39 code as a {@link BitMatrix}.
 * 
 * @author erik.barbara@gmail.com (Erik Barbara)
 */
public final class Code39Writer extends UPCEANWriter {

	public BitMatrix encode(String contents, BarcodeFormat format, int width,
			int height, Hashtable hints) throws WriterException {
		if (format != BarcodeFormat.CODE_39) {
			throw new IllegalArgumentException(
					"Can only encode CODE_39, but got " + format);
		}
		return super.encode(contents, format, width, height, hints);
	}

	public byte[] encode(String contents) {
		int length = contents.length();
		if (length > 80) {
			throw new IllegalArgumentException(
					"Requested contents should be less than 80 digits long, but got "
							+ length);
		}

		int[] widths = new int[9];
		int codeWidth = 24 + 1 + length;
		for (int i = 0; i < length; i++) {
			int indexInString = Code39Reader.ALPHABET_STRING.indexOf(contents
					.charAt(i));
			toIntArray(Code39Reader.CHARACTER_ENCODINGS[indexInString], widths);
			for (int j = 0; j < widths.length; j++) {
				codeWidth += widths[j];
			}
		}
		byte[] result = new byte[codeWidth];
		toIntArray(Code39Reader.CHARACTER_ENCODINGS[39], widths);
		int pos = appendPattern(result, 0, widths, 1);
		int[] narrowWhite = { 1 };
		pos += appendPattern(result, pos, narrowWhite, 0);
		// append next character to bytematrix
		for (int i = length - 1; i >= 0; i--) {
			int indexInString = Code39Reader.ALPHABET_STRING.indexOf(contents
					.charAt(i));
			toIntArray(Code39Reader.CHARACTER_ENCODINGS[indexInString], widths);
			pos += appendPattern(result, pos, widths, 1);
			pos += appendPattern(result, pos, narrowWhite, 0);
		}
		toIntArray(Code39Reader.CHARACTER_ENCODINGS[39], widths);
		pos += appendPattern(result, pos, widths, 1);
		return result;
	}

	private static void toIntArray(int a, int[] toReturn) {
		for (int i = 0; i < 9; i++) {
			int temp = a & (1 << i);
			toReturn[i] = (temp == 0) ? 1 : 2;
		}
	}

}