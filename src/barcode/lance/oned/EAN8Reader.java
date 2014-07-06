package barcode.lance.oned;

import barcode.lance.assist.BarcodeFormat;
import barcode.lance.assist.NotFoundException;
import barcode.lance.common.BitArray;

/**
 * <p>
 * Implements decoding of the EAN-8 format.
 * </p>
 * 
 * @author Sean Owen
 */
public final class EAN8Reader extends UPCEANReader {

	private final int[] decodeMiddleCounters;

	public EAN8Reader() {
		decodeMiddleCounters = new int[4];
	}

	protected int decodeMiddle(BitArray row, int[] startRange,
			StringBuffer result) throws NotFoundException {
		int[] counters = decodeMiddleCounters;
		counters[0] = 0;
		counters[1] = 0;
		counters[2] = 0;
		counters[3] = 0;
		int end = row.getSize();
		int rowOffset = startRange[1];

		for (int x = 0; x < 4 && rowOffset < end; x++) {
			int bestMatch = decodeDigit(row, counters, rowOffset, L_PATTERNS);
			result.append((char) ('0' + bestMatch));
			for (int i = 0; i < counters.length; i++) {
				rowOffset += counters[i];
			}
		}

		int[] middleRange = findGuardPattern(row, rowOffset, true,
				MIDDLE_PATTERN);
		rowOffset = middleRange[1];

		for (int x = 0; x < 4 && rowOffset < end; x++) {
			int bestMatch = decodeDigit(row, counters, rowOffset, L_PATTERNS);
			result.append((char) ('0' + bestMatch));
			for (int i = 0; i < counters.length; i++) {
				rowOffset += counters[i];
			}
		}

		return rowOffset;
	}

	BarcodeFormat getBarcodeFormat() {
		return BarcodeFormat.EAN_8;
	}

}
