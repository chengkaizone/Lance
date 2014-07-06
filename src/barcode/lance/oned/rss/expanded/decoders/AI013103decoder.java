package barcode.lance.oned.rss.expanded.decoders;

import barcode.lance.common.BitArray;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 */
final class AI013103decoder extends AI013x0xDecoder {

	AI013103decoder(BitArray information) {
		super(information);
	}

	protected void addWeightCode(StringBuffer buf, int weight) {
		buf.append("(3103)");
	}

	protected int checkWeight(int weight) {
		return weight;
	}
}
