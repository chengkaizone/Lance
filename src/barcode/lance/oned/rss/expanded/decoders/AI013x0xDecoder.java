package barcode.lance.oned.rss.expanded.decoders;

import barcode.lance.assist.NotFoundException;
import barcode.lance.common.BitArray;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 */
abstract class AI013x0xDecoder extends AI01weightDecoder {

	private static final int headerSize = 4 + 1;
	private static final int weightSize = 15;

	AI013x0xDecoder(BitArray information) {
		super(information);
	}

	public String parseInformation() throws NotFoundException {
		if (this.information.size != headerSize + gtinSize + weightSize) {
			throw NotFoundException.getNotFoundInstance();
		}

		StringBuffer buf = new StringBuffer();

		encodeCompressedGtin(buf, headerSize);
		encodeCompressedWeight(buf, headerSize + gtinSize, weightSize);

		return buf.toString();
	}
}
