package barcode.lance.oned.rss.expanded.decoders;

import barcode.lance.assist.NotFoundException;
import barcode.lance.common.BitArray;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 */
final class AI01392xDecoder extends AI01decoder {

	private static final int headerSize = 5 + 1 + 2;
	private static final int lastDigitSize = 2;

	AI01392xDecoder(BitArray information) {
		super(information);
	}

	public String parseInformation() throws NotFoundException {
		if (this.information.size < headerSize + gtinSize) {
			throw NotFoundException.getNotFoundInstance();
		}

		StringBuffer buf = new StringBuffer();

		encodeCompressedGtin(buf, headerSize);

		int lastAIdigit = this.generalDecoder.extractNumericValueFromBitArray(
				headerSize + gtinSize, lastDigitSize);
		buf.append("(392");
		buf.append(lastAIdigit);
		buf.append(')');

		DecodedInformation decodedInformation = this.generalDecoder
				.decodeGeneralPurposeField(headerSize + gtinSize
						+ lastDigitSize, null);
		buf.append(decodedInformation.getNewString());

		return buf.toString();
	}

}
