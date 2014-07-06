package barcode.lance.oned.rss.expanded;

import java.util.Vector;

import barcode.lance.common.BitArray;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 * @author Eduardo Castillejo, University of Deusto
 *         (eduardo.castillejo@deusto.es)
 */
final class BitArrayBuilder {

	private BitArrayBuilder() {
	}

	static BitArray buildBitArray(Vector pairs) {
		int charNumber = (pairs.size() << 1) - 1;
		if ((((ExpandedPair) pairs.lastElement()).getRightChar()) == null) {
			charNumber -= 1;
		}

		int size = 12 * charNumber;

		BitArray binary = new BitArray(size);
		int accPos = 0;

		ExpandedPair firstPair = (ExpandedPair) pairs.elementAt(0);
		int firstValue = firstPair.getRightChar().getValue();
		for (int i = 11; i >= 0; --i) {
			if ((firstValue & (1 << i)) != 0) {
				binary.set(accPos);
			}
			accPos++;
		}

		for (int i = 1; i < pairs.size(); ++i) {
			ExpandedPair currentPair = (ExpandedPair) pairs.elementAt(i);

			int leftValue = currentPair.getLeftChar().getValue();
			for (int j = 11; j >= 0; --j) {
				if ((leftValue & (1 << j)) != 0) {
					binary.set(accPos);
				}
				accPos++;
			}

			if (currentPair.getRightChar() != null) {
				int rightValue = currentPair.getRightChar().getValue();
				for (int j = 11; j >= 0; --j) {
					if ((rightValue & (1 << j)) != 0) {
						binary.set(accPos);
					}
					accPos++;
				}
			}
		}
		return binary;
	}
}
